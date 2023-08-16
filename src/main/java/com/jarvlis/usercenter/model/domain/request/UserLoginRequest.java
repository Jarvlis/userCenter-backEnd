package com.jarvlis.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Author:Jarvlis
 * Date:2023-04-20
 * Time:22:22
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 7284044343178356992L;

    private String userPassword;

    private String userAccount;
}
