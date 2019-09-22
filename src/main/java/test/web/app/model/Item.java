package test.web.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private List<String> tags = null;
    private Owner owner;
    private Boolean isAnswered;
    private Integer viewCount;
    private Integer answerCount;
    private Integer score;
    private Integer lastActivityDate;
    private Integer creationDate;
    private Integer lastEditDate;
    private Integer questionId;
    private String link;
    private String title;
    private Integer acceptedAnswerId;
    private Integer closedDate;
    private String closedReason;
    private Integer bountyAmount;
    private Integer bountyClosesDate;
    private Integer protectedDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Boolean getIsAnswered() {
        return isAnswered;
    }

    @JsonSetter("is_answered")
    public void setIsAnswered(Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    @JsonSetter("view_count")
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    @JsonSetter("answer_count")
    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLastActivityDate() {
        return lastActivityDate;
    }

    @JsonSetter("last_activity_date")
    public void setLastActivityDate(Integer lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public Integer getCreationDate() {
        return creationDate;
    }

    @JsonSetter("creation_date")
    public void setCreationDate(Integer creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getLastEditDate() {
        return lastEditDate;
    }

    @JsonSetter("last_edit_date")
    public void setLastEditDate(Integer lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    @JsonSetter("question_id")
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAcceptedAnswerId() {
        return acceptedAnswerId;
    }

    @JsonSetter("accepted_answer_id")
    public void setAcceptedAnswerId(Integer acceptedAnswerId) {
        this.acceptedAnswerId = acceptedAnswerId;
    }

    public Integer getClosedDate() {
        return closedDate;
    }

    @JsonSetter("closed_date")
    public void setClosedDate(Integer closedDate) {
        this.closedDate = closedDate;
    }

    public String getClosedReason() {
        return closedReason;
    }

    @JsonSetter("closed_reason")
    public void setClosedReason(String closedReason) {
        this.closedReason = closedReason;
    }

    public Integer getBountyAmount() {
        return bountyAmount;
    }

    @JsonSetter("bounty_amount")
    public void setBountyAmount(Integer bountyAmount) {
        this.bountyAmount = bountyAmount;
    }

    public Integer getBountyClosesDate() {
        return bountyClosesDate;
    }

    @JsonSetter("bounty_closes_date")
    public void setBountyClosesDate(Integer bountyClosesDate) {
        this.bountyClosesDate = bountyClosesDate;
    }

    public Integer getProtectedDate() {
        return protectedDate;
    }

    @JsonSetter("protected_date")
    public void setProtectedDate(Integer protectedDate) {
        this.protectedDate = protectedDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}