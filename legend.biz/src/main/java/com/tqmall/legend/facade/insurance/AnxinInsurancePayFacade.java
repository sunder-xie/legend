package com.tqmall.legend.facade.insurance;

import com.tqmall.mace.param.anxin.RpcAxPayParam;

/**
 * Created by zwb on 17/3/8.
 * 支付相关
 */
public interface AnxinInsurancePayFacade {
    /**
     * 返回支付宝支付返回页
     *
     * @return
     */
    String getAliPayPageInfo(RpcAxPayParam param);

}
