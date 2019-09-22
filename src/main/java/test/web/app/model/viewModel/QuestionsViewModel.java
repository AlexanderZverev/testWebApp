package test.web.app.model.viewModel;

import com.google.common.base.Objects;
import test.web.app.model.Item;

import java.util.List;

public class QuestionsViewModel {
    private List<Item> questions;
    private int page = 1;
    private String searchString;
    private int totalPages;
    private boolean isInDescendingOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionsViewModel that = (QuestionsViewModel) o;
        return page == that.page &&
                totalPages == that.totalPages &&
                isInDescendingOrder == that.isInDescendingOrder &&
                Objects.equal(questions, that.questions) &&
                Objects.equal(searchString, that.searchString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(questions, page, searchString, totalPages, isInDescendingOrder);
    }

    public boolean isInDescendingOrder() {
        return isInDescendingOrder;
    }

    public void setInDescendingOrder(boolean inDescendingOrder) {
        isInDescendingOrder = inDescendingOrder;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setQuestions(List<Item> questions) {
        this.questions = questions;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Item> getQuestions() {
        return questions;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public int getPage() {
        return page;
    }
}