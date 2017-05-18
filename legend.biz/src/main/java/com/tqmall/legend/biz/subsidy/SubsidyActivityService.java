package com.tqmall.legend.biz.subsidy;


import com.tqmall.legend.entity.subsidy.SubsidyActivity;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 16/2/24.
 */
public interface SubsidyActivityService {

    /**
     * 查询补贴活动列表
     *
     * @param actId 活动id
     * @return
     */
    public List<SubsidyActivity> getSubsidyActivities(Long actId);

    /**
     * 获取用户账户已提现、正在提现金额、待审核金额
     *
     * @param userId
     * @param shopId
     * @return
     */
    public Result getUserAccountBalance(Long userId, Long shopId);

    /**
     * 获取用户余额以及是否已设置过提现银行卡信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    public Result getBalanceAndIsSetBank(Long userId, Long shopId);

}
