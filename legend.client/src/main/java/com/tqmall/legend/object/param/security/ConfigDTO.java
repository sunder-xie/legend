package com.tqmall.legend.object.param.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lilige on 16/9/6.
 */
@Data
public class ConfigDTO implements Serializable {
    private static final long serialVersionUID = -4842000968833776042L;
    //时间校验结果  0 正常   1超出时间
    private Integer time = 0;
    //0未授权，1已授权, 2授权失败,3未申请
    private Integer device = 1;
    //网络校验结果 0 符合  1 不符合
    private Integer network = 0;
    //开始登陆时间
    private String loginBeginTime = "00:00:00";
    //结束登陆时间
    private String loginEndTime = "23:59:59";
    //离可以登录时间
    private Long lastTime = 0L;

}
