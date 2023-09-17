package com.jarvlis.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Author:Jarvlis
 * Date:2023-04-20
 * Time:22:22
 */
@Data
public class UserSearchRequest implements Serializable {

    private static final long serialVersionUID = 7284044343178356992L;

    private int current;

    private int pageSize;

    private String user_name;

    private String user_account;

    private String phone;

    private String planet_code;

    private int user_role = -1;

    private int user_status = -2;
}
