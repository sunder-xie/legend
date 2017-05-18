package com.tqmall.legend.object.param.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lilige on 16/9/7.
 */
@Data
public class ShopNetworkConfigDTO implements Serializable{
    private static final long serialVersionUID = -5613263256097976934L;
    private Long id;
    private Long shopId;//门店id
    private String macAddress;//路由器的mac地址
    private String ipAddress;//ip地址
    private String wifiName;//wifi的名称

}
