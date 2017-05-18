package com.tqmall.legend.biz.balance;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.balance.UserBalance;

import java.util.List;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
public interface UserBalanceService {
    /**
     * 获取用户余额以及提现开启时间
     *
     * @param userId 用户id
     * @param shopId 店铺id
     */
    public Result getUserBalance(Long userId, Long shopId);

    /**
     * 获取用户余额
     * @param userId 用户id
     * @param shopId 店铺id
     * @return
     */
    public List<UserBalance> getUserBalanceOnly(Long userId, Long shopId);

}
