package com.tqmall.legend.object.param.security;

import lombok.Data;

import java.io.Serializable;

/**
 * 安全登陆体系提供app传参
 * Created by lilige on 16/9/5.
 */
@Data
public class ConfigParam implements Serializable {
    private static final long serialVersionUID = 8224146611132014525L;
    private String uuid;
    private Long shopId;
    private Long userId;
    //设备ID
    private String deviceId;
    //路由mac地址
    private String macAddress;
    //ip地址
    private String ipAddress;
    //wifi名称
    private String wifiName;
    //门店配置的ID
    private Long configId;
    //手机品牌和型号
    private String phoneBrand;

}
