package com.tqmall.legend.facade.shop;

import com.tqmall.legend.common.Result;

/**
 * Created by feilong.li on 16/11/1.
 */
public interface ShopManagerBalanceFacade {

    /**
     * 检查用户是否有提现中金额 或 可提现金额、'提现中'记录
     * @param managerId
     * @param shopId
     * @return
     */
    public Result<String> checkUserBalance(Long managerId, Long shopId);

}
