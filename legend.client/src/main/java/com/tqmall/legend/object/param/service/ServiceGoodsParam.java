package com.tqmall.legend.object.param.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 16/12/7.
 * 服务带配件
 */
@Getter
@Setter
public class ServiceGoodsParam implements Serializable {
    private static final long serialVersionUID = -4751744087701238506L;

    private ServiceInfoParam serviceInfoParam;//服务信息
    private List<ServiceGoodsSuiteParam> goodsSuiteParamList;//服务配件信息
}
