package com.tqmall.legend.entity.config;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.enums.config.ManagerDeviceConfigStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Created by zsy on 16/9/6.
 * 安全登录门店员工设备设置表
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopManagerDeviceConfig extends BaseEntity {

    private Long shopId;//门店id
    private Long managerId;//门店员工id
    private String deviceId;//设备号
    private String phoneBrand;//手机品牌和型号
    private Integer authorizeStatus;//'授权状态：0未授权，1已授权,

    private String managerName;//
    private String managerMobile;//

    public String getAuthorizeStatuName(){
        if(authorizeStatus != null){
            return ManagerDeviceConfigStatusEnum.getNameByCode(authorizeStatus);
        }
        return "";
    }

}

