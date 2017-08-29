Bootique Shiro Twitter4J Demo
==========================

Apache Shiro and Twitter4J demonstrating account setup and login with Twitter.

![Bootique Shiro Twitter4J Demo App Home Page](http://nixmash.com/x/blog/2017/shirotwitter0829a.png)

## NixMash Posts (in progress)

- [A Bootique Shiro Demo Application](http://nixmash.com/post/a-bootique-shiro-demo-application)
- [A Custom Shiro Realm Example with Bootique](http://nixmash.com/post/a-custom-shiro-realm-example-with-bootique)
- [Custom Role Filters with Bootique Shiro Web](http://nixmash.com/post/custom-role-filters-with-bootique-shiro-web)
- [Using Encrypted Passwords in Shiro](http://nixmash.com/post/using-encrypted-passwords-in-shiro)

## Installation

The Bootique Shiro Demo uses MySQL which requires configuration. To configure 

1) Create a MySQL database and run `setup.mysql` located in **install/sql**
2) Update the Bootique JDBC Connection properties in `bootique.yml`

The tests use H2 and require no configuration.

## Twitter Oauth Setup

You'll need to register your application with Twitter at `http://dev.twitter.com`. This demo uses `localhost:9000` so your Application Twitter Callback URL should be **http://localhost:9000/authorized**.

Copy the `sample4j.properties` in both `/src/main/resources` and `/src/test/resources` to `twitter4j.properties` and populate all values listed. The application requires *oauth.consumerKey* and *oauth.consumerSecret* values. 

```properties
# supply the following properties in /src/main/resource/twitter4j.properties

oauth.consumerKey=*********
oauth.consumerSecret=*******
```

The test `twitter4j.properties` file requires the additional property values of *oauth.accessToken* and *oauth.accessTokenSecret*. To obtain those properties you will:

1) RUN the application
2) LOGIN with Twitter, and if all goes well you will see an *accessToken* and *accessTokenSecret* in the MySQL table `user_social`, fields `access_token` and `secret.`  
3) Enter these values in the *accessToken/accessTokenSecret* properties in your test `twitter4j.properties` file.

```properties
# enter all properties in /src/test/resource/twitter4j.properties

oauth.consumerKey=*********
oauth.consumerSecret=*******
oauth.accessToken=***********
oauth.accessTokenSecret=*********
```

## To Run

Build the app in your IDE and go to

```bash
http://localhost:9000/
```

To use Maven:

```
mvn clean package
```
Enter the following to launch the app from your application root directory.

```bash
/project/root/$ java -jar target/shiro-twitter.jar 
```

## User Accounts

Two User Accounts are available for login and are listed on the web pages.

1) **bob** - Password: *password* Roles: *admin* and *user*
2) **ken** - Password: *halo* Roles: *user*

