package test.web.app.web;

import test.web.app.model.Item;
import test.web.app.model.ServiceResponse;
import test.web.app.service.IQuestionsService;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionsControllerTestHelper {
    static void setupRequestParameters(HttpServletRequest request) {
        setupRequestParameters(request, "test", "", null);
    }

    static void setupRequestParameters(HttpServletRequest request, String searchString, String page, String orderDescending) {
        when(request.getParameter("searchString")).thenReturn(searchString);
        when(request.getParameter("page")).thenReturn(page);
        when(request.getRequestDispatcher(anyString())).thenReturn(mock(RequestDispatcher.class));
        when(request.getParameter("orderDescending")).thenReturn(orderDescending);
    }

    static void setupServiceResponse(IQuestionsService service) throws ExecutionException {
        setupServiceResponse(service, new ArrayList<>(), 100, 10, 1, "", true);
    }

    static void setupServiceResponse(IQuestionsService service,
                                     List<Item> items,
                                     Integer total,
                                     Integer pageSize,
                                     Integer page,
                                     String searchString,
                                     boolean isInDescendingOrder) throws ExecutionException {

        ServiceResponse serviceResponse = new ServiceResponse();

        if (items != null) {
            serviceResponse.setItems(items);
        }

        serviceResponse.setTotal(total);
        serviceResponse.setPageSize(pageSize);
        serviceResponse.setPage(page);
        serviceResponse.setSearchString(searchString);
        serviceResponse.setInDescendingOrder(isInDescendingOrder);

        when(service.getQuestions(anyString(), anyInt(), anyBoolean())).thenReturn(serviceResponse);
    }
}
