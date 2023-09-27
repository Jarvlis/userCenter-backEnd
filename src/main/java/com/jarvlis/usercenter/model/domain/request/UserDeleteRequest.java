package com.jarvlis.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Author:Jarvlis
 * Date:2023-09-27
 * Time:10:31
 */
@Data
public class UserDeleteRequest implements Serializable {
    private static final long serialVersionUID = 2994116927375906118L;

    private long id;

}
