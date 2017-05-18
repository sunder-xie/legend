package com.tqmall.legend.object.param.service;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by twg on 16/1/7.
 */
@Data
public class FuchsGoodsParam implements Serializable {
    private Long preOrderId;//预售单id
    private Boolean applyResult;//申请结果
    private String reason;//原因
}
