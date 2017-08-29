package com.nixmash.shiro.twitter;

import com.nixmash.shiro.db.DbTestBase;
import com.nixmash.shiro.models.SocialUser;
import com.nixmash.shiro.utils.TwitterTestUtils;
import com.nixmash.shiro.utils.TwitterUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.net.URISyntaxException;

@RunWith(JUnit4.class)
public class TwitterTest extends DbTestBase {

    private static final Logger logger = LoggerFactory.getLogger(TwitterTest.class);
    private static final String NO_ACCESSTOKENS = "WARNING: No AccessToken properties found in twitter4j.properies";
    public static final String ONE_TO_FIVE = "12345";


    @Test
    public void getAuthTokenFromUrlTest() throws URISyntaxException {
        String url = "https://api.twitter.com/oauth/authorize?oauth_token=ApUExwAAAAAA2C0wAAABXhTNGhw";
        String oauth_token = url.substring(url.indexOf("=") + 1);
        Assert.assertEquals(oauth_token, "ApUExwAAAAAA2C0wAAABXhTNGhw");
    }

    @Test
    public void createSocialUserTest() {
        SocialUser socialUser = TwitterTestUtils.getSocialUser(ONE_TO_FIVE);
        SocialUser saved = userService.addSocialUser(socialUser);
        MatcherAssert.assertThat(saved.getSocialId().intValue(), Matchers.greaterThan(0));
    }

    @Test
    public void showStatusTest() throws TwitterException {
        SocialUser socialUser = TwitterTestUtils.getSocialUser(ONE_TO_FIVE);
        Twitter twitter = new TwitterFactory().getInstance();
        if (TwitterUtils.isCredentialed(twitter)) {
            String twitterScreenName = twitter.showUser(socialUser.getScreenName()).getScreenName();
            Assert.assertEquals(twitterScreenName, socialUser.getScreenName());
        }
        else
            logger.info(NO_ACCESSTOKENS);
    }

}
