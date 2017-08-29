package com.nixmash.shiro.service;

import com.google.inject.Inject;
import com.nixmash.shiro.db.UsersDb;
import com.nixmash.shiro.models.CurrentUser;
import com.nixmash.shiro.models.Role;
import com.nixmash.shiro.models.SocialUser;
import com.nixmash.shiro.models.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final String CURRENT_USER = "CurrentUser";
    public static final String SOCIAL_USER = "SocialUser";

    private UsersDb usersDb;

    @Inject
    public UserServiceImpl(UsersDb usersDb) {
        this.usersDb = usersDb;
    }

    // region users

    @Override
    public User getUser(String username) {
        return usersDb.getUser(username);
    }

    @Override
    public User addUser(User user) {
        user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        logger.info("Adding User with email: " + user.getEmail() + " hashedPassword: " +user.getPassword());
        return usersDb.addUser(user);
    }

    @Override
    public List<Role> getRoles(Long userId) {
        return usersDb.getRoles(userId);
    }

    // endregion

    // region CurrentUser

    @Override
    public CurrentUser createCurrentUser(Subject subject) {
        User user = this.getUser(subject.getPrincipals().toString());
        CurrentUser currentUser = new CurrentUser(user);
        List<Role> roles = this.getRoles(user.getUserId());

        for (Role role : roles) {
            if (role.getRoleName().equals("admin")) {
                currentUser.setAdministrator(true);
            }
            currentUser.getRoles().add(role.getRoleName());
            currentUser.getPermissions().add(role.getPermission());
        }
        return currentUser;
    }

    // endregion

    // region Social Users

    @Override
    public SocialUser addSocialUser(SocialUser user) {
        logger.info("Adding Social User with screen name: " + user.getScreenName());
        return usersDb.addSocialUser(user);
    }

    @Override
    public SocialUser getSocialUser(String username) {
        return usersDb.getSocialUser(username);
    }

    @Override
    public SocialUser getSocialUser(String accessToken, String secret) {
        SocialUser socialUser = usersDb.getSocialUserByAccessToken(accessToken, secret);
        return socialUser;
    }
    // endregion
}
