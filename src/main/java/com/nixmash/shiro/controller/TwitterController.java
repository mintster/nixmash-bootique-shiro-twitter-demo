package com.nixmash.shiro.controller;

import com.google.inject.Inject;
import com.nixmash.shiro.config.TwitterConfig;
import com.nixmash.shiro.models.SocialUser;
import com.nixmash.shiro.models.User;
import com.nixmash.shiro.service.UserService;
import com.nixmash.shiro.utils.TwitterUtils;
import com.nixmash.shiro.views.PageView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import static com.nixmash.shiro.service.UserServiceImpl.CURRENT_USER;
import static com.nixmash.shiro.service.UserServiceImpl.SOCIAL_USER;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class TwitterController implements Serializable {

    private static final long serialVersionUID = -6170883553296173603L;
    private static final Logger logger = LoggerFactory.getLogger(TwitterController.class);
    private static final String PASSWORD = "password";
    private static final String TWITTER = "twitter";

    private RequestToken requestToken;
    private Twitter twitter;


    private UserService userService;
    private TwitterConfig twitterConfig;

    @Inject
    public TwitterController(UserService userService, TwitterConfig twitterConfig) {
        this.userService = userService;
        this.twitterConfig = twitterConfig;
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello() {
        SocialUser socialUser = (SocialUser) SecurityUtils.getSubject().getSession().getAttribute(SOCIAL_USER);
        if (socialUser != null) {
        try {
                Twitter twitter = twitterConfig.twitter(socialUser);
                if (TwitterUtils.isCredentialed(twitter)) {
                    twitter4j.User user = twitter.showUser(socialUser.getScreenName());
                    logger.info(user.getDescription());
                }
            } catch(TwitterException | IllegalStateException e){
                logger.info("Twitter Exception: " + e.getMessage());
            }
        }
        return "hello!";
    }

    @GET
    @Path("/signin/twitter")
    public Response signinTwitter() throws URISyntaxException, TwitterException {
        URI targetURIForRedirection = null;
        if (noSocialUser()) {

            // region get Twitter RequestToken

            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthAccessToken(null);
            requestToken = twitter.getOAuthRequestToken();

            logger.info("Request token: " + requestToken.getToken());
            logger.info("Request token secret: " + requestToken.getTokenSecret());

            String url = requestToken.getAuthorizationURL();
            String oauth_token = url.substring(url.indexOf("=") + 1);

            // endregion

            // region Create Temporary Twitter Shiro User to save RequestToken to subject.session

            Subject subject = SecurityUtils.getSubject();
            subject.logout();

            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("bob", "password");

            try {
                subject.login(usernamePasswordToken);
            } catch (AuthenticationException e) {
                subject.logout();
                logger.info(e.getMessage());
                return Response.temporaryRedirect(new URI("/")).build();
            }
            subject.getSession().setAttribute("requestToken", requestToken);

            // endregion

            // region Obtain authenticationURL from RequestToken for retrieving Twitter AccessToken

            targetURIForRedirection = new URI(requestToken.getAuthenticationURL());

            // endregion

        } else {

            // region Social User exists - Twitter User already authenticated. Redirect to Home Page
            targetURIForRedirection = new URI("/");
            // endregion

        }
        return Response.temporaryRedirect(targetURIForRedirection).build();
    }

    private boolean noSocialUser() {
        return (SocialUser) SecurityUtils.getSubject().getSession().getAttribute(SOCIAL_USER) == null;
    }

    @GET
    @Path("/authorized")
    public PageView authorized(@Context UriInfo info) throws URISyntaxException, TwitterException {

        // region get oauth_verifier from requestToken

        Subject subject = SecurityUtils.getSubject();
        RequestToken requestToken = (RequestToken) subject.getSession().getAttribute("requestToken");
        String oauth_verifier = info.getQueryParameters().getFirst("oauth_verifier");
        String pageView;

        // endregion

        // region get Twitter AccessToken from requestToken

        Twitter twitter = TwitterFactory.getSingleton();
        AccessToken accessToken = twitter.getOAuthAccessToken(oauth_verifier);
        logger.info("Access token: " + accessToken.getToken());
        logger.info("Access token secret: " + accessToken.getTokenSecret());

        // endregion

        if (socialAccountExists(accessToken)) {
            subject.logout();

            // region if SocialUser Twitter Account previously created Login user

            SocialUser socialUser = userService.getSocialUser(accessToken.getToken(), accessToken.getTokenSecret());
            User user = userService.getUser(socialUser.getUsername());
            UsernamePasswordToken token = new UsernamePasswordToken(socialUser.getUsername(), socialUser.providerUserid);
            subject = SecurityUtils.getSubject();
            subject.login(token);
            subject.getSession().setAttribute(CURRENT_USER, userService.createCurrentUser(subject));
            subject.getSession().setAttribute(SOCIAL_USER, socialUser);
            twitter.setOAuthAccessToken(accessToken);
            pageView = "home.mustache";

            // endregion

        } else {

            // region FirstTime User, create SocialUser and load Shiro Account completion page

            twitter4j.User twitterUser = twitter.showUser(accessToken.getScreenName());
            logger.info(twitterUser.getDescription());

            String userId = String.valueOf(twitterUser.getId());
            String profileUrl = String.format("https://twitter.com/%s", twitterUser.getScreenName());
            SocialUser socialUser = new SocialUser(
                    userId,
                    twitterUser.getScreenName(),
                    TWITTER,
                    userId,
                    twitterUser.getName(),
                    profileUrl,
                    twitterUser.getProfileImageURLHttps(),
                    accessToken.getToken(),
                    accessToken.getTokenSecret()
            );

            subject.getSession().setAttribute(SOCIAL_USER, userService.addSocialUser(socialUser));
            pageView = "authorized.mustache";

            // endregion
        }
        return new PageView(pageView, null);
    }

    @POST
    @Path("/signup")
    public Response signupOnSubmit(@FormParam("username") String username,
                                   @FormParam("firstName") String firstName,
                                   @FormParam("lastName") String lastName,
                                   @FormParam("email") String email,
                                   @FormParam("providerUserid") String providerUserid) throws Exception {

        userService.addUser(new User(username, email, firstName, lastName, providerUserid, providerUserid));
        SocialUser socialUser = userService.getSocialUser(username);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, providerUserid);

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        subject.login(usernamePasswordToken);
        subject.getSession().setAttribute(CURRENT_USER, userService.createCurrentUser(subject));
        subject.getSession().setAttribute(SOCIAL_USER, userService.addSocialUser(socialUser));

        URI targetURIForRedirection = new URI("/");
        return Response.seeOther(targetURIForRedirection).build();
    }

    private Boolean socialAccountExists(AccessToken accessToken) {
        return userService.getSocialUser(accessToken.getToken(), accessToken.getTokenSecret()).getSocialId() != null;
    }
}