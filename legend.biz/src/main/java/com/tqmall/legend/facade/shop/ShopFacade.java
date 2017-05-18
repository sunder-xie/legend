package com.tqmall.legend.facade.shop;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;

/**
 * Created by zsy on 16/8/12.
 */
public interface ShopFacade {
    /**
     * 同意协议
     * @return
     */
    Result<Boolean> agree(Long shopId, UserInfo userInfo);


    /**
     * 查询门店支付申请状态是否已"测试通过"
     * @param shopId
     * @return
     */
    public Boolean getShopApplyStatusIsSuccess(Long shopId);


    /**
     * 设置门店委托体系状态
     * @param joinStatus
     * @param userInfo
     * @return
     */
    void setShopJoinStatus(int joinStatus, UserInfo userInfo);
}
