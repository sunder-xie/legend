package com.tqmall.legend.object.param.service;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macx on 2017/2/13.
 */
@Getter
@Setter
public class WashCarServiceParam extends BaseRpcParam implements Serializable{
    private static final long serialVersionUID = -914514735012018308L;

    private Long shopId;
    private List<ServicePriceParam> servicePriceParams;
}
