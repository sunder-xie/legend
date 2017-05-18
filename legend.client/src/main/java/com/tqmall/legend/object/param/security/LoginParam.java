package com.tqmall.legend.object.param.security;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;
import lombok.ToString;

/**
 * Created by zsy on 16/8/11.
 */
@Data
@ToString
public class LoginParam extends BaseRpcParam{

    private static final long serialVersionUID = -7548767480751566687L;

    private String account;  //账号
    private String password; //MD5后的密码
    private boolean isCheckPassword; //是否需要校验密码，APP特殊账户用
    private Long shopId; //门店id,一个账户多家门店情况选择时用
}
