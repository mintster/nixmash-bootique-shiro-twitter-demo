package com.nixmash.shiro.utils;

public class DbTestUtils {

    public final static String createSchemaSql =
            "DROP TABLE IF EXISTS roles;\n" +
                    "CREATE TABLE roles (\n" +
                    "  role_id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  permission VARCHAR(80) DEFAULT NULL,\n" +
                    "  role_name VARCHAR(45) DEFAULT NULL\n" +
                    ");\n" +
                    "\n" +
                    "DROP TABLE IF EXISTS users;\n" +
                    "CREATE TABLE users (\n" +
                    "  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "  username VARCHAR (50) NOT NULL,\n" +
                    "  email VARCHAR (150) NOT NULL,\n" +
                    "  first_name VARCHAR (40) NOT NULL,\n" +
                    "  last_name VARCHAR (40) NOT NULL,\n" +
                    "  enabled TINYINT (1) DEFAULT '1' NOT NULL,\n" +
                    "  account_expired TINYINT (1) DEFAULT '0' NOT NULL,\n" +
                    "  account_locked TINYINT (1) DEFAULT '0' NOT NULL,\n" +
                    "  credentials_expired TINYINT (1) DEFAULT '0' NOT NULL,\n" +
                    "  has_avatar TINYINT (1) DEFAULT '0' NOT NULL,\n" +
                    "  user_key VARCHAR (25) DEFAULT '0000000000000000' NOT NULL,\n" +
                    "  provider_id VARCHAR (25) DEFAULT 'SITE' NOT NULL,\n" +
                    "  PASSWORD VARCHAR (255) NOT NULL,\n" +
                    "  version INT DEFAULT '0' NOT NULL\n" +
                    ");\n" +
                    "\n" +
                    "DROP TABLE IF EXISTS user_roles;\n" +
                    "CREATE TABLE user_roles (\n" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                    "  user_id BIGINT DEFAULT NULL,\n" +
                    "  role_id BIGINT DEFAULT NULL\n" +
                    ");" +
                    "\n" +
                    "DROP TABLE IF EXISTS user_social;\n" +
                    "\n" +
                    "CREATE TABLE user_social (\n" +
                    "  social_id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "  username VARCHAR (50) NOT NULL,\n" +
                    "  provider_id VARCHAR (25) NOT NULL,\n" +
                    "  provider_userid VARCHAR (60) DEFAULT '' NOT NULL,\n" +
                    "  screen_name VARCHAR (50) NULL,\n" +
                    "  display_name VARCHAR (255) NULL,\n" +
                    "  profile_url VARCHAR (512) NULL,\n" +
                    "  image_url VARCHAR (512) NULL,\n" +
                    "  access_token VARCHAR (255) NOT NULL,\n" +
                    "  secret VARCHAR (255) NULL,\n" +
                    "  refresh_token VARCHAR (255) NULL,\n" +
                    "  expire_time BIGINT NULL,\n" +
                    "  CONSTRAINT UserConnectionProviderUser UNIQUE (username, provider_userid)\n" +
                    ");";

    public final static String dataSql =
            "INSERT INTO users (username, email, first_name, last_name, password)\n" +
                    "VALUES ('bob', 'bob@aol.com', 'Bob', 'Planter', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');\n" +
                    "INSERT INTO users (username, email, first_name, last_name, password)\n" +
                    "VALUES ('ken', 'ken@aol.com', 'Ken', 'Stark', 'a4e63bcacf6c172ad84f9f4523c8f1acaf33676fa76d3258c67b7e7bbf16d777');\n" +
                    "\n" +
                    "INSERT INTO roles (permission, role_name) VALUES ('nixmash:all', 'admin');\n" +
                    "INSERT INTO roles (permission, role_name) VALUES ('nixmash:view', 'user');\n" +
                    "\n" +
                    "INSERT INTO user_roles (user_id, role_id) VALUES (1,1);\n" +
                    "INSERT INTO user_roles (user_id, role_id) VALUES (1,2);\n" +
                    "INSERT INTO user_roles (user_id, role_id) VALUES (2,2);";

}
