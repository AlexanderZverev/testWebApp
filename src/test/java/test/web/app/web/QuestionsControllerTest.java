package test.web.app.web;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import test.web.app.model.Item;
import test.web.app.model.viewModel.QuestionsViewModel;
import test.web.app.service.IQuestionsService;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static test.web.app.web.QuestionsControllerTestHelper.setupRequestParameters;
import static test.web.app.web.QuestionsControllerTestHelper.setupServiceResponse;

class QuestionsControllerTest {

    static Stream<String> testData_doGet_ShouldNotCallService_WhenSearchStringIsBlank() {
        return Stream.of("", "  ", null);
    }

    @ParameterizedTest
    @MethodSource("testData_doGet_ShouldNotCallService_WhenSearchStringIsBlank")
    void doGet_ShouldNotCallService_WhenSearchStringIsBlank(final String testValue) throws ServletException, ExecutionException {
        // Arrange.
        IQuestionsService service = mock(IQuestionsService.class);
        setupServiceResponse(service);

        HttpServletRequest request = mock(HttpServletRequest.class);
        setupRequestParameters(request, testValue, null, null);

        QuestionsController controller = new QuestionsController();
        controller.setService(service);

        AsyncContext asyncContext = mock(AsyncContext.class);
        when(asyncContext.getRequest()).thenReturn(request);

        when(request.startAsync()).thenReturn(asyncContext);

        doAnswer(invocation -> {
            Runnable serviceCall = (Runnable) invocation.getArguments()[0];
            serviceCall.run();
            return null;
        }).when(asyncContext).start(any(Runnable.class));

        // Act.
        controller.doGet(request, mock(HttpServletResponse.class));

        // Assert.
        verify(service, never()).getQuestions(anyString(), anyInt(), anyBoolean());
    }

    private static Stream<Arguments> testData_doGet_ShouldCallServiceWithProperParameters_WhenSearchStringIsNotBlank() {
        return Stream.of(
                Arguments.of("testSearchString", "testSearchString", "1", 1, "on", true),
                Arguments.of("C++", "C++", "-1", 1, null, false),
                Arguments.of("<script></script>", "<script></script>", "abc", 1, "on", true)
        );
    }

    @ParameterizedTest
    @MethodSource("testData_doGet_ShouldCallServiceWithProperParameters_WhenSearchStringIsNotBlank")
    void doGet_ShouldCallServiceWithProperParameters_WhenSearchStringIsNotBlank(
            String searchString,
            String expectedSearchString,
            String page,
            int expectedPage,
            String orderDescending,
            boolean expectedIsInDescendingOrder
    ) throws ServletException, ExecutionException {
        // Arrange.
        IQuestionsService service = mock(IQuestionsService.class);
        setupServiceResponse(service);

        QuestionsController controller = new QuestionsController();
        controller.setService(service);

        HttpServletRequest request = mock(HttpServletRequest.class);
        setupRequestParameters(request, searchString, page, orderDescending);

        AsyncContext asyncContext = mock(AsyncContext.class);
        when(asyncContext.getRequest()).thenReturn(request);
        when(asyncContext.getResponse()).thenReturn(mock(HttpServletResponse.class));

        when(request.startAsync()).thenReturn(asyncContext);

        doAnswer(invocation -> {
            Runnable serviceCall = (Runnable) invocation.getArguments()[0];
            serviceCall.run();
            return null;
        }).when(asyncContext).start(any(Runnable.class));

        // Act.
        controller.doGet(request, mock(HttpServletResponse.class));

        // Assert.
        verify(service, times(1))
                .getQuestions(expectedSearchString, expectedPage, expectedIsInDescendingOrder);
    }

    private static Stream<Arguments> testData_doGet_ShouldForwardRequestToViewWithProperViewModel() {
        // Items[], page, searchString, totalPages, isInDescendingOrder
        return Stream.of(
                Arguments.of(List.of(new Item()), 1, "test1", 10, false),
                Arguments.of(List.of(new Item(), new Item()), 2, "test2", 100, true)
        );
    }

    @ParameterizedTest
    @MethodSource("testData_doGet_ShouldForwardRequestToViewWithProperViewModel")
    void doGet_ShouldForwardRequestToViewWithProperViewModel(
            List<Item> items,
            int page,
            String searchString,
            int totalPages,
            boolean isInDescendingOrder) throws ExecutionException, ServletException {
        // Arrange.
        final int pageSize = 42;
        IQuestionsService service = mock(IQuestionsService.class);
        setupServiceResponse(service, items, totalPages * pageSize, pageSize, page, searchString, isInDescendingOrder);

        QuestionsViewModel expectedViewModel = new QuestionsViewModel();
        expectedViewModel.setQuestions(items);
        expectedViewModel.setPage(page);
        expectedViewModel.setSearchString(searchString);
        expectedViewModel.setTotalPages(totalPages);
        expectedViewModel.setInDescendingOrder(isInDescendingOrder);

        HttpServletRequest request = mock(HttpServletRequest.class);
        setupRequestParameters(request);

        AsyncContext asyncContext = mock(AsyncContext.class);
        when(asyncContext.getRequest()).thenReturn(request);
        when(asyncContext.getResponse()).thenReturn(mock(HttpServletResponse.class));

        when(request.startAsync()).thenReturn(asyncContext);

        doAnswer(invocation -> {
            Runnable serviceCall = (Runnable) invocation.getArguments()[0];
            serviceCall.run();
            return null;
        }).when(asyncContext).start(any(Runnable.class));

        QuestionsController controller = new QuestionsController();
        controller.setService(service);

        // Act.
        controller.doGet(request, mock(HttpServletResponse.class));

        // Assert.
        verify(request, times(1)).setAttribute("model", expectedViewModel);
    }
}