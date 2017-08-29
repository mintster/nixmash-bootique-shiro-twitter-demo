
package com.nixmash.shiro.models;

import java.io.Serializable;

public class SocialUser implements Serializable {

    private static final Long serialVersionUID = 7649386744083707684L;

    public SocialUser() {
    }

    public SocialUser(String username, String screenName, String providerId, String providerUserid, String displayName, String profileUrl, String imageUrl, String accessToken, String secret) {
        this.username = username;
        this.screenName = screenName;
        this.providerId = providerId;
        this.providerUserid = providerUserid;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
    }

    // region properties

    public Long socialId;
    public String username;
    public String screenName;
    public String providerId;
    public String providerUserid;
    public String displayName;
    public String profileUrl;
    public String imageUrl;
    public String accessToken;
    public String secret;
    public String refreshToken;
    public Long expireTime;


    // endregion

    // region getters/setters

    public Long getSocialId() {
        return socialId;
    }

    public void setSocialId(Long socialId) {
        this.socialId = socialId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserid() {
        return providerUserid;
    }

    public void setProviderUserid(String providerUserid) {
        this.providerUserid = providerUserid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    // endregion

    // region tostring

    @Override
    public String toString() {
        return "SocialUser{" +
                "socialId=" + socialId +
                ", username='" + username + '\'' +
                ", screenName ='" + screenName + '\'' +
                ", providerId='" + providerId + '\'' +
                ", providerUserid='" + providerUserid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", secret='" + secret + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }


    // endregion

}



