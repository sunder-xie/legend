package com.tqmall.legend.entity.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/9/6.
 * 安全登录门店网络环境设置表
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopNetworkConfig extends BaseEntity {

    private Long shopId;//门店id
    private String macAddress;//路由器的mac地址
    private String ipAddress;//ip地址
    private String wifiName;//wifi的名称

}

