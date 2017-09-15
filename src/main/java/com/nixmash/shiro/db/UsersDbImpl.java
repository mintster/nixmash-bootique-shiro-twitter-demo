package com.nixmash.shiro.db;

import com.google.inject.Inject;
import com.nixmash.shiro.config.AppConfig;
import com.nixmash.shiro.models.Role;
import com.nixmash.shiro.models.SocialUser;
import com.nixmash.shiro.models.User;
import io.bootique.jdbc.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersDbImpl implements UsersDb {

    private static final Logger logger = LoggerFactory.getLogger(UsersDbImpl.class);

    private DataSourceFactory dataSource;
    private AppConfig appConfig;

    @Inject
    public UsersDbImpl(DataSourceFactory dataSource, AppConfig appConfig) {
        this.dataSource = dataSource;
        this.appConfig = appConfig;
    }

    // region Shiro Users

    @Override
    public List<Role> getRoles(Long userId) {
        List<Role> roles = new ArrayList<>();
        try (Connection cn = dataSource.forName(appConfig.datasourceName).getConnection()) {
            try (Statement statement = cn.createStatement()) {
                String getRolesSql = "SELECT user_roles.user_id, roles.permission, roles.role_name FROM  user_roles " +
                        "INNER JOIN roles ON user_roles.role_id = roles.role_id WHERE user_roles.user_id ='" + userId + "'";
                ResultSet rs = statement.executeQuery(getRolesSql);
                rs.beforeFirst();
                while (rs.next()) {
                    roles.add(new Role(rs.getString("permission"), rs.getString("role_name")));
                }
                rs.close();
                cn.close();
            }
        } catch (SQLException | NullPointerException e) {
            logger.info("Error retrieving roles: " + e.getMessage());
        }
        return roles;
    }

    @Override
    public User getUser(String username) {
        User user = new User();
        try (Connection cn = dataSource.forName(appConfig.datasourceName).getConnection()) {

            try (Statement statement = cn.createStatement()) {
                String getUserSql = "SELECT * FROM users WHERE username = '" + username + "'";
                ResultSet rs = statement.executeQuery(getUserSql);
                rs.beforeFirst();

                while (rs.next()) {
                    user.setUserId(rs.getLong("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                }
                rs.close();
                cn.close();
            }
        } catch (SQLException | NullPointerException e) {
            logger.info("Error retrieving user: " + e.getMessage());
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        try (Connection connection = dataSource.forName(appConfig.datasourceName).getConnection()) {

            try (Statement statement = connection.createStatement()) {
                String createUserSql = "INSERT INTO users (username, email, first_name, last_name, password) VALUES " +
                        "('" + user.getUsername() + "', " +
                        "'" + user.getEmail() + "', " +
                        "'" + user.getFirstName() + "', " +
                        "'" + user.getLastName() + "', " +
                        "'" + user.getPassword() + "'" +
                        ")";
                int result = statement.executeUpdate(createUserSql, Statement.RETURN_GENERATED_KEYS);
                if (result == 1) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getLong(1));
                    }
                    String sqlSocial = String.format("UPDATE user_social SET username = '%s' where provider_userid = '%s'",
                            user.getUsername(), user.getProviderUserid());
                    statement.execute(sqlSocial);
                }
            }
        } catch (SQLException e) {
            logger.info("Error creating new user: " + e.getMessage());
        }
        return user;
    }

    // endregion

    // region Twitter Users
    @Override
    public SocialUser addSocialUser(SocialUser socialUser) {
        {
            try (Connection connection = dataSource.forName(appConfig.datasourceName).getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "INSERT INTO user_social(username, provider_id, provider_userid, screen_name, display_name, profile_url, image_url, access_token, secret) " +
                            "VALUES (" +
                            "'" + socialUser.getUsername() + "'," +
                            "'" + socialUser.getProviderId() + "'," +
                            "'" + socialUser.getProviderUserid() + "'," +
                            "'" + socialUser.getScreenName() + "'," +
                            "'" + socialUser.getDisplayName() + "'," +
                            "'" + socialUser.getProfileUrl() + "'," +
                            "'" + socialUser.getImageUrl() + "'," +
                            "'" + socialUser.getAccessToken() + "'," +
                            "'" + socialUser.getSecret() + "'" +
                            ")";
                    int result = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                    if (result == 1) {
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            socialUser.setSocialId(generatedKeys.getLong(1));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.info("Error creating new SocialUser: " + e.getMessage());
            }
            return socialUser;
        }
    }

    @Override
    public SocialUser getSocialUser(String username) {
        SocialUser socialUser = new SocialUser();
        try (Connection cn = dataSource.forName(appConfig.datasourceName).getConnection()) {

            try (Statement statement = cn.createStatement()) {
                String getUserSql = "SELECT * FROM user_social WHERE username = '" + username + "'";
                ResultSet rs = statement.executeQuery(getUserSql);
                while (rs.next()) {
                    populateSocialUser(rs, socialUser);
                }
                rs.close();
                cn.close();
            }
        } catch (SQLException | NullPointerException e) {
            logger.info("Error retrieving user: " + e.getMessage());
        }
        return socialUser;
    }

    @Override
    public SocialUser getSocialUserByAccessToken(String accessToken, String secret) {
        SocialUser socialUser = new SocialUser();
        try (Connection cn = dataSource.forName(appConfig.datasourceName).getConnection()) {

            try (Statement statement = cn.createStatement()) {
                String getUserSql = "SELECT * FROM user_social WHERE access_token = '" + accessToken + "' " +
                        "AND secret = '" + secret + "'";
                ResultSet rs = statement.executeQuery(getUserSql);
                while (rs.next()) {
                    populateSocialUser(rs, socialUser);
                }
                rs.close();
                cn.close();
            }
        } catch (SQLException | NullPointerException e) {
            logger.info("Error retrieving user: " + e.getMessage());
        }
        return socialUser;
    }

    private void populateSocialUser(ResultSet rs, SocialUser socialUser) throws SQLException {
        socialUser.setSocialId(rs.getLong("social_id"));
        socialUser.setUsername(rs.getString("username"));
        socialUser.setProviderId(rs.getString("provider_id"));
        socialUser.setProviderUserid(rs.getString("provider_userid"));
        socialUser.setScreenName(rs.getString("screen_name"));
        socialUser.setDisplayName(rs.getString("display_name"));
        socialUser.setProfileUrl(rs.getString("profile_url"));
        socialUser.setImageUrl(rs.getString("image_url"));
        socialUser.setAccessToken(rs.getString("access_token"));
        socialUser.setSecret(rs.getString("secret"));
        socialUser.setRefreshToken(rs.getString("refresh_token"));
        socialUser.setExpireTime(rs.getLong("expire_time"));
    }
    // endregion

}
