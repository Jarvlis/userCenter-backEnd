package com.jarvlis.usercenter;

import com.jarvlis.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.jarvlis.usercenter.model.domain.User;
import javax.annotation.Resource;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private UserService userService;
    @Test
    public void testAddUser() {

    }

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1111";
        long result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword,planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "dogyupi";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword,
                checkPassword, planetCode);
        Assertions.assertTrue(result > 0);
    }

}
