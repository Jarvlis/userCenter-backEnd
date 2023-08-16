package com.jarvlis.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Author:Jarvlis
 * Date:2023-04-17
 * Time:11:38
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 7284044343178356992L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;
}
