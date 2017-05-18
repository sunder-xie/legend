package com.tqmall.legend.service.settlement;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.legend.object.param.settlement.CarwashCreateBillParam;
import com.tqmall.legend.object.param.settlement.ConfirmBillParam;
import com.tqmall.legend.object.param.settlement.DebitBillFlowSaveParam;
import com.tqmall.legend.object.param.settlement.SpeedilyConfirmBillParam;
import com.tqmall.legend.object.param.settlement.SettlementSmsParam;
import com.tqmall.legend.object.param.settlement.GuestMobileCheckParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.settlement.OrderDebitBillDTO;
import com.tqmall.legend.object.result.settlement.PaymentDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 16/3/15.
 * APP 结算相关
 */
public interface RpcSettlementService {
    /**
     * 根据店铺id获取店铺的 结算方式
     * 调用cube获取排序后的支付方式
     * @param shopId 店铺id
     */
    public Result<List<PaymentDTO>> getPaymentList(Long shopId);

    /**
     * 获取店铺的结算工单的数量信息
     *
     * @param shopId 店铺id
     */
    public Result<Map<String, Integer>> getSettlementOrderCount(Long shopId);


    /**
     * 结算工单搜索
     */
    public Result<PageEntityDTO<OrderInfoDTO>> searchOrderInfo(OrderSearchParam orderSearchParam);

    /**
     * 获取淘汽优惠券金额核销接口
     *
     * @param source        来源
     * @param orderId       工单id
     * @param taoqiCouponSn 淘汽优惠券
     * @return
     */
    Result<BigDecimal> getTaoqiCouponSn(String source, Long orderId, String taoqiCouponSn);

    /**
     * 工单收款
     *
     * @param param
     * @return
     */
    Result debitOrder(DebitBillFlowSaveParam param);

    /**
     * 确认账单
     *
     * @return
     */
    Result confirmBill(ConfirmBillParam confirmBillParam);


    /**
     * 快修快保 首次账单确认AND收款
     *
     * @param speedilyConfirmBillParam 表单实体
     * @return
     */
    Result speedilyConfirmBill(SpeedilyConfirmBillParam speedilyConfirmBillParam);


    /**
     * 洗车单 创建入口
     *
     * @param carwashCreateParam 洗车单表单实体
     * @return
     */
    Result carwashCreate(CarwashCreateBillParam carwashCreateParam);

    /**
     * 查询历史收款记录
     *
     * @param shopId
     * @param orderId
     * @return
     */
    Result<OrderDebitBillDTO> findDebitBill(Long shopId, Long orderId);

    /**
     * 使用其他客户优惠信息发送短信验证码
     *
     * @param settlementSmsParam
     * @return
     */
    Result<Boolean> sendCode(SettlementSmsParam settlementSmsParam);

    /**
     * 使用其他客户优惠信息校验短信验证码
     *
     * @param guestMobileCheckParam
     * @return
     */
    Result<Boolean> checkCode(GuestMobileCheckParam guestMobileCheckParam);
}
