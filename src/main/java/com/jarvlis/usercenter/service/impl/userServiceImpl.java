package com.jarvlis.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jarvlis.usercenter.common.ErrorCode;
import com.jarvlis.usercenter.exception.BussinessException;
import com.jarvlis.usercenter.model.domain.User;
import com.jarvlis.usercenter.service.UserService;
import com.jarvlis.usercenter.mapper.userMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jarvlis.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现类
 *
 * @author 十肆
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-04-13 20:45:50
 */
@Service
@Slf4j
public class userServiceImpl extends ServiceImpl<userMapper, User>
        implements UserService {

    @Resource
    private userMapper userMapper;

    /**
     * 盐值: 混淆密码
     */
    final String SALT = "jarvlis";


    @Override
    public long userRegister(String account, String password, String checkKey, String planetCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(account, password, checkKey, planetCode)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (account.length() < 4) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if (password.length() < 8 || checkKey.length() < 8){
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }

        // 账户不包含特殊字符
        String vaildPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(vaildPattern).matcher(account);
        if (matcher.find()) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }

        // 密码和校验密码相同
        if (!password.equals(checkKey)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "密码和校验密码不同");
        }

        // 账户不重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", account);
        long count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "账户重复");
        }

        if(planetCode.length() > 6) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "星球编号过长");
        }

        // 星球编号不重复
        userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("planet_code", planetCode);
        count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }


        // 2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 3.插入数据
        User user = new User();
        user.setUser_account(account);
        user.setUser_password(encryptPassword);
        user.setPlanet_code(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String account, String password, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }
        if (account.length() < 4) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }
        if (password.length() < 8) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR, "星球编号重复");
        }

        // 账户不包含特殊字符
        String vaildPattern = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(vaildPattern).matcher(account);
        if (matcher.find()) {
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        }

        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", account);
        userQueryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        // 用户不存在
        if(user == null) {
            log.info("user login failed,account can't match password");
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        // 3.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originalUser
     * @return
     */
    @Override
    public User getSafetyUser(User originalUser){
        if(originalUser == null){
            throw new BussinessException(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUser_name(originalUser.getUser_name());
        safetyUser.setUser_account(originalUser.getUser_account());
        safetyUser.setAvatar_url(originalUser.getAvatar_url());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setPlanet_code(originalUser.getPlanet_code());
        safetyUser.setUser_role(originalUser.getUser_role());
        safetyUser.setUser_status(originalUser.getUser_status());
        safetyUser.setCreate_time(originalUser.getCreate_time());
        return safetyUser;
    }

    /**
     * 用户注销
     * @param request 请求对象
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




