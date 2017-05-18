package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanghui on 6/17/16.
 */
@Data
public class DiscountComboDTO implements Serializable {
    /**
     * 计次卡id
     */
    private Long accountComboId;
    /**
     * 计次卡名称
     */
    private String comboName;
    /**
     * 计次服务id
     */
    private Long serviceId;
    /**
     * 计次服务名
     */
    private String serviceName;
    /**
     * 计次卡剩余服务数
     */
    private Integer serviceCount;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 是否选中
     */
    private boolean selected;
    /**
     * 使用次数
     */
    private Integer useCount;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 过期日期
     */
    private Date expireDate;
}
