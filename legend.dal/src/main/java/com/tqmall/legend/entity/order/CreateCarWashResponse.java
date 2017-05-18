package com.tqmall.legend.entity.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 创建洗车单相应
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CreateCarWashResponse implements Serializable {

    private static final long serialVersionUID = -7189423694926317920L;

    // 工单ID
    Object orderId;
    // 是否挂账(1:挂账;0:结清)
    Object isSign;
    // 挂账金额
    Object signAmount;

}
