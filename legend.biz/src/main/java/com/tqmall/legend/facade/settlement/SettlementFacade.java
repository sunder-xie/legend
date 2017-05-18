package com.tqmall.legend.facade.settlement;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.setting.vo.ShopPrintConfigVO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.settlement.bo.SettlementSmsBO;
import org.springframework.ui.Model;

/**
 * Created by zsy on 16/6/18.
 * 结算facade
 */
public interface SettlementFacade {
    /**
     * 获取淘汽优惠券金额
     * @param orderId
     * @param taoqiCouponSn
     * @return
     */
    Result couponCheck(Long orderId, String taoqiCouponSn);


    /**
     * 打印业务沉降到biz层
     * @param model
     * @param shopId
     * @param orderId
     * @param printType
     * @param shopPrintConfigVO
     * @throws BizException
     */
    boolean settlePrint(Model model, Long shopId, Long orderId, String printType, ShopPrintConfigVO shopPrintConfigVO);

    /**
     * 使用其他客户的优惠券、会员卡，发送短信验证码接口
     * 短信模板：legend_order_settlement格式为：
     * [淘汽云修]您的验证码是{$code}，车牌 [{$license}] 正在使用您的卡券消费，请与门店 [{$shopName}] 确认，门店电话：{$tel}
     * @param settlementSmsBO
     * @return
     */
    boolean sendCode(SettlementSmsBO settlementSmsBO, Long shopId);

    /**
     * 校验验证码是否失效
     *
     * @param mobile 使用他人优惠信息时需要输入他人手机号
     * @param code
     * @param shopId
     * @return
     */
    boolean checkCode(String mobile, String code, Long shopId);
}
