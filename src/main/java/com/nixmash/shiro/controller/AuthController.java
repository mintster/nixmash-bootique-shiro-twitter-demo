/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.nixmash.shiro.controller;

import com.google.inject.Inject;
import com.nixmash.shiro.service.UserService;
import com.nixmash.shiro.views.PageView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

import static com.nixmash.shiro.service.UserServiceImpl.CURRENT_USER;

@Path("/")
public class AuthController {

    private static transient final Logger log = LoggerFactory.getLogger(AuthController.class);

    private UserService userService;

    @Inject
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/login")
    public PageView view() {
        return new PageView("login.mustache", null);
    }

    @POST
    @Path("/login")
    public Response onSubmit(@FormParam("username") String username,
                             @FormParam("password") String password) throws Exception {

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            subject.getSession().setAttribute(CURRENT_USER, userService.createCurrentUser(subject));
        } catch (AuthenticationException e) {
            log.debug("Error authenticating.", e);
            URI targetURIForRedirection = new URI("/login");
            return Response.seeOther(targetURIForRedirection).build();
        }

        URI targetURIForRedirection = new URI("/");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @GET
    @Path("/logout")
    public Response logout() throws URISyntaxException {
        SecurityUtils.getSubject().logout();
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthAccessToken(null);
        URI targetURIForRedirection = new URI("/login?logout=true");
        return Response.temporaryRedirect(targetURIForRedirection).build();
    }


    @GET
    @Path("/login.jsp")
    public Response redirectLogin() throws URISyntaxException {
        URI targetURIForRedirection = new URI("/login");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @GET
    @Path("/unauthorized")
    public PageView unauthorized() throws URISyntaxException {
        return new PageView("unauthorized.mustache", null);
    }

}