package com.jarvlis.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jarvlis.usercenter.common.BaseResponse;
import com.jarvlis.usercenter.common.ErrorCode;
import com.jarvlis.usercenter.common.ResultUtils;
import com.jarvlis.usercenter.exception.BussinessException;
import com.jarvlis.usercenter.model.domain.request.UserDeleteRequest;
import com.jarvlis.usercenter.model.domain.request.UserLoginRequest;
import com.jarvlis.usercenter.model.domain.request.UserRegisterRequest;
import com.jarvlis.usercenter.model.domain.User;
import com.jarvlis.usercenter.model.domain.request.UserSearchRequest;
import com.jarvlis.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.jarvlis.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.jarvlis.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * Author:Jarvlis
 * Date:2023-04-17
 * Time:11:09
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://jarvlis.top", "http://localhost:8000"}, allowCredentials = "true")
public class userController {

    //controller里面要调用业务逻辑,需要引入service
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        String account = userRegisterRequest.getUserAccount();
        String checkKey = userRegisterRequest.getCheckPassword();
        String password = userRegisterRequest.getUserPassword();
        String planetCode= userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(account, checkKey, password, planetCode)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long res = userService.userRegister(account, password, checkKey, planetCode);
        return ResultUtils.success(res);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        String account = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        User user = userService.userLogin(account, password, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        int res = userService.userLogout(request);
        return ResultUtils.success(res);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userobj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userobj;
        if(currentUser == null){
            throw new BussinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // todo校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request){
        System.out.println(userSearchRequest);
        if(!isAdmin(request)){
           throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        String userName = userSearchRequest.getUser_name();
        String userAccount = userSearchRequest.getUser_account();
        String planetCode = userSearchRequest.getPlanet_code();
        String phone = userSearchRequest.getPhone();
        int userRole = userSearchRequest.getUser_role();
        int userStatus = userSearchRequest.getUser_status();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like("user_name", userName);
        }
        if(StringUtils.isNotBlank(userAccount)){
            queryWrapper.like("user_account", userAccount);
        }
        if(StringUtils.isNotBlank(planetCode)){
            queryWrapper.like("planet_code", planetCode);
        }
        if(StringUtils.isNotBlank(phone)){
            queryWrapper.like("phone", phone);
        }
        if(userRole != -1){
            queryWrapper.like("planet_code", userRole);
        }
        if(userStatus != -2){
            queryWrapper.like("user_status",userStatus);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> collect = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request){
        long id = userDeleteRequest.getId();
        if(!isAdmin(request)){
            throw new BussinessException(ErrorCode.NO_AUTH);
        }
        if(id <= 0){
            throw new BussinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean res = userService.removeById(id);
        return ResultUtils.success(res);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUser_role() == ADMIN_ROLE;
    }
}
