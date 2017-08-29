package com.nixmash.shiro;

import com.nixmash.shiro.auth.NixmashRealm;
import com.nixmash.shiro.db.UsersDb;
import com.nixmash.shiro.db.UsersDbImpl;
import com.nixmash.shiro.service.UserService;
import com.nixmash.shiro.service.UserServiceImpl;
import com.nixmash.shiro.views.PageView;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.test.junit.JettyTestFactory;
import io.bootique.shiro.ShiroModule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.Assert.assertEquals;

public class MustacheTest {

    @ClassRule
    public static JettyTestFactory TEST_SERVER = new JettyTestFactory();

    @BeforeClass
    public static void beforeClass() {
        TEST_SERVER.app()
                .args("--config=classpath:test.yml")
                .autoLoadModules()
                .module(b -> b.bind(UserService.class).to(UserServiceImpl.class))
                .module(b -> b.bind(UsersDb.class).to(UsersDbImpl.class))
                .module(binder -> ShiroModule.extend(binder).addRealm(NixmashRealm.class))
                .module(binder -> JerseyModule.extend(binder).addResource(Api.class))
                .start();
    }

    @Test
    public void testV1() {
        WebTarget base = ClientBuilder.newClient().target("http://localhost:9001");
        Response r1 = base.path("/v1").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("v1_string_p1_number_564", r1.readEntity(String.class));
    }

    @Test
    public void testV2() {
        WebTarget base = ClientBuilder.newClient().target("http://localhost:9001");
        Response r1 = base.path("/v2").request().get();
        assertEquals(Status.OK.getStatusCode(), r1.getStatus());
        assertEquals("v2_string_p2_number_5649", r1.readEntity(String.class));
    }

    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public static class Api {

        @GET
        @Path("/v1")
        public PageView getV1() {
            Model m = new Model();
            m.setProp1("p1");
            m.setProp2(564);
            return new PageView("mustacheTests_v1.mustache", m);
        }

        @GET
        @Path("/v2")
        public PageView getV2() {
            Model m = new Model();
            m.setProp1("p2");
            m.setProp2(5649);
            return new PageView("mustacheTests_v2.mustache", m);
        }
    }

    public static class Model {
        private String prop1;
        private int prop2;

        public String getProp1() {
            return prop1;
        }
        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public int getProp2() {
            return prop2;
        }
        public void setProp2(int prop2) {
            this.prop2 = prop2;
        }
    }
}
