package test.web.app.service;

import test.web.app.model.ServiceResponse;

import java.util.concurrent.ExecutionException;

public interface IQuestionsService {
    ServiceResponse getQuestions(String question, int page, boolean descendingOrder) throws ExecutionException;
}
