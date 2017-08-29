package com.nixmash.shiro.auth;

import com.google.inject.Inject;
import com.nixmash.shiro.config.AppConfig;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class NixmashRoleFilter extends RolesAuthorizationFilter {

    private AppConfig appConfig;

    @Inject
    public NixmashRoleFilter(AppConfig appConfig) {
        super.setUnauthorizedUrl(appConfig.unauthorizedUrl);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws IOException {
        Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            this.saveRequestAndRedirectToLogin(request, response);
        } else {
            WebUtils.issueRedirect(request, response, this.getUnauthorizedUrl());
        }
        return false;
    }
}
