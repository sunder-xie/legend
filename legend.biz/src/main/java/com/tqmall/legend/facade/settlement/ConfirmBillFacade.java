package com.tqmall.legend.facade.settlement;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.biz.order.bo.SpeedilyBillBo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by zsy on 16/6/4.
 * 确认账单
 */
public interface ConfirmBillFacade {
    /**
     * 确认账单接口
     *
     * @return
     */
    Result<DebitBillDTO> confirmBill(ConfirmBillBo confirmBillBo, UserInfo userInfo);

    /**
     * 快修快保|销售单 账单确认AND收款
     * 1.出库
     * 2.确认账单
     * 3.收款
     *
     * @param drawUpBo 收款实体
     * @param userInfo 当前操作人
     * @return
     */
    Result speedilyDrawUp(SpeedilyBillBo drawUpBo,OrderInfo orderInfo, UserInfo userInfo);

    /**
     * 共享中心确认账单接口
     *
     * @param proxyOrderId 委托单id
     * @param userInfo 用户信息
     * @return
     */
    Result shareConfirmBill(Long proxyOrderId, UserInfo userInfo);

}
