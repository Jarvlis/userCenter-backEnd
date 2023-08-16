package com.jarvlis.usercenter.service;

import com.jarvlis.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 十肆
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-04-13 20:45:50
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param account 用户账户
     * @param password 用户密码
     * @param checkKey 用户校验码
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String account, String password, String checkKey, String planetCode);

    /**
     * 用户登录
     *
     * @param account 用户账户
     * @param password 用户密码
     * @param request 设置session相关的功能
     * @return 用户信息(脱敏后)
     */
    User userLogin(String account, String password, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originalUser
     * @return
     */
    User getSafetyUser(User originalUser);

    /**
     * 用户注销
     *
     * @param request 请求对象
     * @return
     */
    int userLogout(HttpServletRequest request);
}
