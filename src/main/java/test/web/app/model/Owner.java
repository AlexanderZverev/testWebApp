
package test.web.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Owner {
    private Integer reputation;
    private Integer userId;
    private String userType;
    private Integer acceptRate;
    private String profileImage;
    private String displayName;
    private String link;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getUserId() {
        return userId;
    }

    @JsonSetter("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    @JsonSetter("user_type")
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getAcceptRate() {
        return acceptRate;
    }

    @JsonSetter("accept_rate")
    public void setAcceptRate(Integer acceptRate) {
        this.acceptRate = acceptRate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    @JsonSetter("profile_image")
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonSetter("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}