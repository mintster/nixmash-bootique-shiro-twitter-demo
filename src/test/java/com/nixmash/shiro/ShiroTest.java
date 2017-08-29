package com.nixmash.shiro;

import com.nixmash.shiro.auth.NixmashRealm;
import com.nixmash.shiro.config.AppConfig;
import com.nixmash.shiro.db.UsersDb;
import com.nixmash.shiro.db.UsersDbImpl;
import com.nixmash.shiro.guice.GuiceJUnit4Runner;
import com.nixmash.shiro.models.CurrentUser;
import com.nixmash.shiro.service.UserService;
import com.nixmash.shiro.service.UserServiceImpl;
import com.nixmash.shiro.utils.DbTestUtils;
import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.shiro.ShiroModule;
import io.bootique.test.junit.BQTestFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RunWith(GuiceJUnit4Runner.class)
public class ShiroTest {

    protected final String adminRole = "admin";
    protected final String userRole = "user";
    protected final String adminPermissionString = "nixmash:all";
    protected final String userPermissionString = "nixmash:view";

    @ClassRule
    public static BQTestFactory testFactory = new BQTestFactory();

    private static BQRuntime runtime;

    private static UserService userService;

    @BeforeClass
    public static void beforeClass() throws SQLException {
        runtime = testFactory
                .app("-c", "classpath:test.yml")
                .autoLoadModules()
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .createRuntime();

        DataSourceFactory datasource = runtime.getInstance(DataSourceFactory.class);
        AppConfig appConfig = runtime.getInstance(AppConfig.class);
        Connection connection = datasource.forName(appConfig.datasourceName).getConnection();
        userService = runtime.getInstance(UserService.class);

        if (appConfig.datasourceName.equals("H2")) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(DbTestUtils.createSchemaSql);
                statement.execute(DbTestUtils.dataSql);
            }
        }

        DefaultSecurityManager sm = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(sm);
        ThreadContext.bind(sm);
    }

    @Test
    public void testBobLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        subject.checkPermission(adminPermissionString);
        subject.checkPermission(userPermissionString);
        subject.checkRole("admin");
        subject.checkRole("user");
        subject.logout();
    }

    @Test
    public void testKenLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("ken", "halo"));
        subject.checkPermission(userPermissionString);
        subject.checkRole("user");
        subject.logout();
    }

    @Test(expected = IncorrectCredentialsException.class)
    public void testBadPasswordLogin() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "badpassword"));
        subject.logout();
    }

    @Test(expected = UnauthorizedException.class)
    public void testNotFoundPermission() {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        subject.checkPermission("bad:permission");
        subject.logout();
    }

    @Test
    public void currentUserTest() throws Exception {
        Subject subject = new Subject.Builder(runtime.getInstance(SecurityManager.class)).buildSubject();
        subject.login(new UsernamePasswordToken("bob", "password"));
        CurrentUser currentUser = userService.createCurrentUser(subject);
        Assert.assertTrue(currentUser.getAdministrator().equals(true));
    }
}
