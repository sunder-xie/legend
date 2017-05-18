package com.tqmall.legend.object.param.account;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountSelectedComboParam implements Serializable{
    /**
     * 计次卡服务id
     */
    private Long comboServiceId;
    /**
     * 计次卡服务使用次数
     */
    private Integer useCount;
}
