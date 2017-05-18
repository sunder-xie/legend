package com.tqmall.legend.entity.customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

/**
 * Created by zsy on 16/4/12.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerTag extends BaseEntity {

    private Long shopId;//门店id
    private Long customerCarId;//客户车辆id
    private Long customerId;//客户id
    private String tagName;//标签名称
    private Integer tagRefer;//标签来源，0系统打标，1门店自定义

    /**
     * 临时字段
     */
    private boolean isChoose = true;//是否选择
}

