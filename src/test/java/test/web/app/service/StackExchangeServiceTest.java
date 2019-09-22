package test.web.app.service;

import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.web.app.model.Item;
import test.web.app.model.ServiceResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static javax.ws.rs.client.Invocation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StackExchangeServiceTest {

    private static Stream<Arguments> testData_whenAccessingTheService_urlShouldContainRelevantQueryStringParameters() {
        return Stream.of(
                Arguments.of("test1", 1, true, "&order=desc&intitle=test1&page=1"),
                Arguments.of("test2", 2, false, "&order=asc&intitle=test2&page=2")
        );
    }

    @ParameterizedTest
    @MethodSource("testData_whenAccessingTheService_urlShouldContainRelevantQueryStringParameters")
    public void whenAccessingTheService_urlShouldContainRelevantQueryStringParameters(
            String searchString,
            int page,
            boolean isInDescendingOrder,
            String expectedQueryStringParameters) throws ExecutionException {

        // Arrange.
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getInt("stackExchangeService.cacheDurationMinutes")).thenReturn(30);
        when(mockConfiguration.getInt("stackExchangeService.cacheCapacity")).thenReturn(1000);

        ServiceResponse mockResponse = mock(ServiceResponse.class);
        when(mockResponse.getItems()).thenReturn(List.of(new Item(), new Item()));
        Builder mockBuilder = mock(Builder.class);
        when(mockBuilder.get(ServiceResponse.class)).thenReturn(mockResponse);
        WebTarget mockTarget = mock(WebTarget.class);
        when(mockTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        StackExchangeService service = new StackExchangeService(mockTarget, mockConfiguration);

        StringBuilder queryString = new StringBuilder();

        doAnswer(invocation -> {
            String parameterName = (String) invocation.getArguments()[0];
            String parameterValue = invocation.getArguments()[1].toString();
            queryString.append('&').append(parameterName).append('=').append(parameterValue);
            return mockTarget;
        }).when(mockTarget).queryParam(anyString(), anyObject());

        // Act.
        service.getQuestions(searchString, page, isInDescendingOrder);

        // Assert.
        assertEquals(expectedQueryStringParameters, queryString.toString());
    }


    private static Stream<Arguments>
    testData_whenCallingTheService_fetchingDataShouldOccurOnce_forEachUniqueParameterSet() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                List.of("test", 1, true),
                                List.of("test", 1, true)),
                        1),

                Arguments.of(
                        List.of(
                                List.of("test1", 1, true),
                                List.of("test", 1, true)),
                        2),

                Arguments.of(
                        List.of(
                                List.of("test", 2, true),
                                List.of("test", 1, true)),
                        2),

                Arguments.of(
                        List.of(
                                List.of("test", 1, false),
                                List.of("test", 1, true)),
                        2)
        );
    }

    @ParameterizedTest
    @MethodSource("testData_whenCallingTheService_fetchingDataShouldOccurOnce_forEachUniqueParameterSet")
    void whenCallingTheService_fetchingDataShouldOccurOnce_forEachUniqueParameterSet(
            List<List> parameters,
            int expectedNumberOfFetchingData
    ) throws ExecutionException {

        // Arrange.
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getInt("stackExchangeService.cacheDurationMinutes")).thenReturn(30);
        when(mockConfiguration.getInt("stackExchangeService.cacheCapacity")).thenReturn(1000);

        ServiceResponse mockResponse = mock(ServiceResponse.class);
        when(mockResponse.getItems()).thenReturn(List.of(new Item(), new Item()));

        Builder mockBuilder = mock(Builder.class);
        when(mockBuilder.get(ServiceResponse.class)).thenReturn(mockResponse);

        WebTarget mockTarget = mock(WebTarget.class);
        when(mockTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
        when(mockTarget.queryParam(anyString(), anyObject())).thenReturn(mockTarget);

        StackExchangeService service = new StackExchangeService(mockTarget, mockConfiguration);

        // Act.
        for (List params : parameters) {
            service.getQuestions((String) params.get(0), (int) params.get(1), (boolean) params.get(2));
        }

        // Assert.
        verify(mockBuilder, times(expectedNumberOfFetchingData)).get(ServiceResponse.class);
    }
}