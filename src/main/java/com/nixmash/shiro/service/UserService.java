package com.nixmash.shiro.service;

import com.nixmash.shiro.models.CurrentUser;
import com.nixmash.shiro.models.Role;
import com.nixmash.shiro.models.SocialUser;
import com.nixmash.shiro.models.User;
import org.apache.shiro.subject.Subject;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User getUser(String username);
    List<Role> getRoles(Long userId);
    CurrentUser createCurrentUser(Subject subject);

    SocialUser addSocialUser(SocialUser user);

    SocialUser getSocialUser(String username);

    SocialUser getSocialUser(String accessToken, String secret);
}
