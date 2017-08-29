package com.nixmash.shiro.config;

import com.nixmash.shiro.models.SocialUser;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfig {

    TwitterConfig() {
    }

    public Twitter twitter(SocialUser socialUser) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthAccessToken(socialUser.getAccessToken())
                .setOAuthAccessTokenSecret(socialUser.getSecret());
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }
}
