package com.nixmash.shiro.utils;

import twitter4j.Twitter;

public class TwitterUtils {

    public static boolean isCredentialed(Twitter twitter) {
        return twitter.getConfiguration().getOAuthAccessTokenSecret() != null;
    }
}
