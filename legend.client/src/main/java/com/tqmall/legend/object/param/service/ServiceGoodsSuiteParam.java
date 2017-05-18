package com.tqmall.legend.object.param.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/12/7.
 * 服务带配件
 */
@Getter
@Setter
public class ServiceGoodsSuiteParam implements Serializable {
    private static final long serialVersionUID = 9047991239077242996L;

    private Long goodsId;//服务id
    private Integer goodsNumber;//配件数量
}
