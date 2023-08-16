package com.jarvlis.usercenter.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称

     */
    private String user_name;

    /**
     * 用户账号
     */
    private String user_account;

    /**
     * 用户头像

     */
    private String avatar_url;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户密码
     */
    private String user_password;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer user_status;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 
     */
    @TableLogic
    private Integer is_delete;

    /**
     * 星球编号
     */
    private String planet_code;

    /**
     * 用户权限  0 - 普通用户 1 - 管理员
     */
    private Integer user_role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}