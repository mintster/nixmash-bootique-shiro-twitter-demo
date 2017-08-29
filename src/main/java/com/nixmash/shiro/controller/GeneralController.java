package com.nixmash.shiro.controller;

import com.nixmash.shiro.models.User;
import com.nixmash.shiro.views.PageView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.Serializable;

/**
 * Created by daveburke on 6/26/17.
 */
@Path("/")
public class GeneralController implements Serializable {

    private static final long serialVersionUID = -6170883553296173603L;
    private static final Logger logger = LoggerFactory.getLogger(GeneralController.class);
    private static final String PASSWORD = "password";
    private static final String TWITTER = "twitter";

    RequestToken requestToken;
    Twitter twitter;

    @GET
    public PageView home() {
        User user = new User("bob", "bob@aol.com", "Bob", "Planter", "password", null);
        return new PageView("home.mustache", user);
    }

    @GET
    @Path("/users")
    public PageView users() {
        Subject subject = SecurityUtils.getSubject();
        return new PageView("users.mustache", null);
    }

    @GET
    @Path("/admin")
    public PageView admin() {
        return new PageView("admin.mustache", null);
    }

}
