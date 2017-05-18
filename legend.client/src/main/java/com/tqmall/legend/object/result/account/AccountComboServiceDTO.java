package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class AccountComboServiceDTO implements Serializable{
    /**
     * 计次卡服务实例id
     */
    private Long id;
    /**
     * 计次卡服务名
     */
    private String comboServiceName;
    /**
     * 计次卡服务ID
     */
    private Long comboServiceId;
    /**
     * 服务项目总使用次数
     */
    private Integer totalServiceCount;
    /**
     * 服务项目已使用次数
     */
    private Integer usedServiceCount;

}
