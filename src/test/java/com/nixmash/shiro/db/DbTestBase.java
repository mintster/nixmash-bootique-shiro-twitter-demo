package com.nixmash.shiro.db;

import com.nixmash.shiro.auth.NixmashRealm;
import com.nixmash.shiro.config.AppConfig;
import com.nixmash.shiro.config.TwitterConfig;
import com.nixmash.shiro.controller.GeneralController;
import com.nixmash.shiro.controller.TwitterController;
import com.nixmash.shiro.service.UserService;
import com.nixmash.shiro.service.UserServiceImpl;
import com.nixmash.shiro.utils.DbTestUtils;
import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jersey.JerseyModule;
import io.bootique.shiro.ShiroModule;
import io.bootique.test.junit.BQTestFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbTestBase {

    @ClassRule
    public static BQTestFactory TEST_FACTORY = new BQTestFactory();

    public static BQRuntime runtime;

    protected static UserService userService;
    protected static TwitterController twitterController;
    protected static TwitterConfig twitterConfig;

    @BeforeClass
    public static void setupDB() throws SQLException {
        Package pkg = GeneralController.class.getPackage();

        runtime = TEST_FACTORY
                .app("--config=classpath:test.yml")
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(b -> b.bind(TwitterConfig.class))
                .module(binder -> JerseyModule.extend(binder).addPackage(pkg))
                .module(b -> ShiroModule.extend(b).addRealm(NixmashRealm.class))
                .autoLoadModules()
                .createRuntime();

        userService = runtime.getInstance(UserService.class);
        twitterConfig = runtime.getInstance(TwitterConfig.class);
        twitterController = new TwitterController(userService, twitterConfig);

        DataSourceFactory datasource = runtime.getInstance(DataSourceFactory.class);
        AppConfig appConfig = runtime.getInstance(AppConfig.class);
        Connection connection = datasource.forName(appConfig.datasourceName).getConnection();

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

}
