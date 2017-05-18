package com.tqmall.legend.biz.balance;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.balance.UserBalanceLog;

import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
public interface UserBalanceLogService extends BaseService{

    /**
     * 获取用户的余额以及最新的默认提现账号
     *
     * @param userId      用户id
     * @param shopId      店铺id
     * @param accountType 账号类型
     */
    public Result getUserBalanceAndAccount(Long userId, Long shopId, Integer accountType);

    /**
     * 查询账户变更流水记录
     * @param params
     * @return
     */
    public List<UserBalanceLog> getUserBalanceLogs(Map<String,Object> params);

 }
