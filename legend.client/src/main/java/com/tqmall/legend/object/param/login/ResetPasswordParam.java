package com.tqmall.legend.object.param.login;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by macx on 2017/2/8.
 */
@Getter
@Setter
public class ResetPasswordParam implements Serializable{
    private static final long serialVersionUID = -3413190094905288081L;
    private Long userId;//用户id
    private String newPassword;//重置后密码
}
