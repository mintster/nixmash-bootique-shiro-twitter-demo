package com.nixmash.shiro.db;

import com.nixmash.shiro.models.Role;
import com.nixmash.shiro.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;

@RunWith(JUnit4.class)
public class UsersDbTest extends DbTestBase {


    @Test
    public void newUserAddsTotalByOneTest() throws Exception {
        User jammer = new User("jammer", "jammer@aol.com", "Jammer", "McGee","password", null);
        User saved = userService.addUser(jammer);

        User retrieved = userService.getUser("jammer");
        assertThat(saved.getUserId().intValue(), greaterThan(0));
        Assert.assertEquals(saved.getUserId(), retrieved.getUserId());
    }

    @Test
    public void addSecondUserTest() throws Exception {
        User reed = new User("reed", "reed@aol.com", "Reed", "Larson", "halo", null);
        User saved = userService.addUser(reed);

        User retrieved = userService.getUser("reed");
        Assert.assertEquals(saved.getUserId(), retrieved.getUserId());
    }

    @Test
    public void permissionsTest() throws Exception {
        List<Role> roles = userService.getRoles(1L);
        Assert.assertEquals(roles.size(), 2);
    }

}
