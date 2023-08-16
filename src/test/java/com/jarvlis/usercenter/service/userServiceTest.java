package com.jarvlis.usercenter.service;

import com.jarvlis.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;

/**
 * Author:Jarvlis
 * Date:2023-04-12
 * Time:16:20
 */
@SpringBootTest
class userServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUser_name("jesiis");
        user.setUser_account("12345");
        user.setAvatar_url("https://i1.hdslb.com/bfs/face/89b25cad74abd9e42a94b11e456bc21fe36b8763.png@360w_360h.webp");
        user.setGender(0);
        user.setUser_password("1234567890");
        user.setPhone("123");
        user.setEmail("456");
        user.setUser_status(0);
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }
}