package test.web.app.web;

import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import test.web.app.model.ServiceResponse;
import test.web.app.model.viewModel.QuestionsViewModel;
import test.web.app.service.IQuestionsService;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/questions", asyncSupported = true)
public class QuestionsController extends HttpServlet {

    private static String viewName = "index.jsp";
    private static String contentType = "text/plain;charset=UTF-8";
    private static Logger logger = LogManager.getLogger(QuestionsController.class);

    @Inject
    private IQuestionsService service;

    public void setService(IQuestionsService service) {
        this.service = service;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String searchString = getSearchString(request);

        if (searchString != null) {
            final AsyncContext asyncContext = request.startAsync();

            asyncContext.start(() -> {
                try {
                    HttpServletRequest asyncContextRequest = (HttpServletRequest) asyncContext.getRequest();
                    int page = getValidPageOrDefault(asyncContextRequest, 1);

                    boolean displayResultsInDescendingOrder = false;
                    if (request.getParameter("orderDescending") != null) {
                        displayResultsInDescendingOrder = true;
                    }

                    ServiceResponse serviceResponse = service.getQuestions(searchString, page, displayResultsInDescendingOrder);

                    QuestionsViewModel model = toViewModel(serviceResponse);

                    asyncContextRequest.setAttribute("model", model);

                    HttpServletResponse asyncContextResponse = (HttpServletResponse) asyncContext.getResponse();
                    asyncContextResponse.setContentType(contentType);
                    asyncContextRequest.getRequestDispatcher(viewName).forward(asyncContextRequest, asyncContextResponse);

                    asyncContext.complete();

                } catch (Exception e) {
                    logger.error(e);

                    HttpServletResponse resp = (HttpServletResponse) asyncContext.getResponse();
                    try {
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        asyncContext.complete();
                    }
                }
            });

        } else {
            response.setContentType(contentType);

            try {
                request.getRequestDispatcher(viewName).forward(request, response);
            } catch (IOException | ServletException e) {
                logger.error(e);

                throw new ServletException(e);
            }
        }
    }

    static QuestionsViewModel toViewModel(ServiceResponse serviceResponse) {
        QuestionsViewModel model = new QuestionsViewModel();

        model.setQuestions(serviceResponse.getItems());
        model.setPage(serviceResponse.getPage());
        model.setSearchString(serviceResponse.getSearchString());
        model.setTotalPages(serviceResponse.getTotalPages());
        model.setInDescendingOrder(serviceResponse.isInDescendingOrder());

        return model;
    }

    private String getSearchString(HttpServletRequest request) {
        String result = null;

        String searchString = request.getParameter("searchString");

        if (!StringUtils.isBlank(searchString)) {
            result = searchString.trim();
        }

        return result;
    }

    static int getValidPageOrDefault(HttpServletRequest request, int defaultPage) {
        Integer page;

        String pageQueryParameter = request.getParameter("page");

        if (pageQueryParameter == null) {
            page = defaultPage;
        } else {
            page = Ints.tryParse(pageQueryParameter);

            if (page == null || page <= 0) {
                page = defaultPage;

                logger.warn(String.format(
                        "Invalid page number '%s', page number set to %d",
                        pageQueryParameter,
                        defaultPage));
            }
        }

        return page;
    }
}