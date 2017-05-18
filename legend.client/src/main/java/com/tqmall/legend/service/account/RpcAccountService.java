package com.tqmall.legend.service.account;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.account.DiscountSelectedParam;
import com.tqmall.legend.object.param.account.QryAccountComboParam;
import com.tqmall.legend.object.param.account.QryAccountCouponParam;
import com.tqmall.legend.object.result.account.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户服务接口类
 * Created by wanghui on 6/16/16.
 */
public interface RpcAccountService {

    /**
     * 根据车辆id获取会员卡信息
     *
     * @param shopId 店铺id
     * @param customerCarId 车辆id
     * @return 若该车辆无会员信息,则返回null
     */
    Result<List<CardMemberDTO>> getCardMemberByCustomerCarId(Long shopId, Long customerCarId);

    /**
     * 根据客户id获取会员卡信息
     * @param shopId
     * @param customerId
     * @return
     */
    Result<List<CardMemberDTO>> getCardMemberByCustomerId(Long shopId, Long customerId);

    /**
     * 根据车牌获取关联的会员信息
     * @param shopId
     * @param carLicense
     * @return 若该车辆无会员信息,则返回null
     */
    Result<List<CardMemberDTO>> getCardMemberByCarLicense(Long shopId, String carLicense);

    /**
     * 根据客户手机号获取关联的会员信息
     * @param shopId
     * @param mobile
     * @return
     */
    Result<List<CardMemberDTO>> getCardMemberByMobile(Long shopId, String mobile);

    /**
     * 根据会员卡卡号获取会员卡信息
     * @param shopId 店铺id
     * @param cardNumber 会员卡号
     * @return
     */
    Result<CardMemberDTO> getCardMemberByCardNumber(Long shopId,String cardNumber);

    /**
     * 根据车辆id查询该车所有关联的账户下的卡券优惠信息
     * @param shopId
     * @param customerCarId
     * @return
     */
    Result<List<CustomerDiscountInfoDTO>> getCustomerDiscountInfo(Long shopId, Long customerCarId);

    /**
     * 根据车辆id获取可用计次卡信息
     *
     * @param shopId
     * @param customerCarId
     * @return
     */
    Result<List<AccountComboDTO>> getAccountComboByCustomerCarId(Long shopId, Long customerCarId);

    /**
     * 根据车辆id获取计次卡信息
     * @param shopId
     * @param customerCarId
     * @param avaiable true为可用计次卡信息,false为所有未删除计次卡信息
     * @return
     */
    Result<List<AccountComboDTO>> getAccountComboByCustomerCarId(Long shopId, Long customerCarId,boolean avaiable);

    /**
     * 根据车辆id获取当前可使用的优惠券信息
     * 不包含未生效、过期的、已使用的券
     *
     * @param shopId
     * @param customerCarId
     * @return
     */
    Result<List<AccountCouponDTO>> getAccountCouponByCustomerCarId(Long shopId, Long customerCarId);

    /**
     * 根据车辆id获取当前优惠券信息
     *
     * @param shopId
     * @param customerCarId
     * @param avaiable true为不包含未生效、过期的、已使用的券  false为包含未生效的优惠卷
     * @return
     */
    Result<List<AccountCouponDTO>> getAccountCouponByCustomerCarId(Long shopId, Long customerCarId,boolean avaiable);

    /**
     * 根据工单id获取本次确认账单可使用的优惠信息
     * @param shopId 店铺id
     * @param orderId 工单id
     * @param param 已选择优惠信息
     * @return
     */
    Result<DiscountInfoDTO> getDiscountInfoByOrderId(Long shopId, Long orderId, DiscountSelectedParam param);

    /**
     * 洗车单获取优惠信息
     * @param shopId 店铺id
     * @param carLicense 车牌
     * @param serviceId 洗车服务id,若自定义,则该字段留空
     * @param amount 洗车服务金额
     * @param param 已选择优惠信息
     * @return
     */
    Result<DiscountInfoDTO> getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId, BigDecimal amount, DiscountSelectedParam param);

    /**
     * 查询工单上次结算使用的会员卡
     * @param shopId
     * @param orderId
     * @return
     */
    Result<CardMemberDTO> getUsedForOrderLastSettle(Long shopId, Long orderId);

    /**
     * 会员优惠信息反转
     *
     * @param shopId    店铺id
     * @param operator  操作人id
     * @param orderId   工单id
     * @param payAmount 工单实付金额
     * @return
     */
    Result<Boolean> revertSettlement(Long shopId, Long operator, Long orderId, BigDecimal payAmount);

    /**
     * 查询账户优惠券列表(包括优惠券类型信息)
     * @param qryAccountCouponParam
     * @return
     */
    Result<AccountCouponInfoPageDTO> qryAccountCouponList(QryAccountCouponParam qryAccountCouponParam);

    /**
     * 分页查询计次卡(包含服务信息)
     * @param qryAccountComboParam
     * @return
     */
    Result<AccountComboPageDTO> qryAccountComboList(QryAccountComboParam qryAccountComboParam);

    /**
     * 在云修系统创建微信账户
     * @param userGlobalId
     * @param mobile
     * @return
     */
    Result<Boolean> createCustomerForWexinByUserGlobalId(Long userGlobalId, String mobile);

    /**
     * 根据优惠券类型id集合获取优惠券类型信息
     * @param ucShopId UC店铺id
     * @param couponInfoIds 优惠券类型ids
     * @return 优惠券类型信息
     */
    Result<List<CouponInfo4WechatDTO>> findCouponInfoById4Wehcat(Long ucShopId, Long ... couponInfoIds);

    /**
     * 计次卡充值方法(针对微信)
     * @param ucShopId UC店铺id
     * @param comboInfoId 计次卡id
     * @param mobile 客户手机号
     * @return 充值是否成功
     */
    Result<Boolean> grantCombo4Wechat(Long ucShopId,Long comboInfoId,String mobile);

    /**
     * 会员卡类型查询(微信)
     * @param ucShopId
     * @param cardInfoIds
     * @return
     */
    Result<List<MemberCardInfoDTO>> listCardInfo4Wechat(Long ucShopId, List<Long> cardInfoIds);

    /**
     * 会员卡实例查询(微信)
     * @param ucShopId
     * @param mobile
     * @return
     */
    Result<List<MemberCardDTO>> listCard4Wechat(Long ucShopId, String mobile);

    /**
     * 会员卡领取
     * @param ucShopId
     * @param cardInfoId
     * @param mobile
     * @param cardNumber
     * @param incomeAmount
     * @return
     */
    Result<MemberCardDTO> grantCard4Wechat(Long ucShopId, Long cardInfoId, String mobile, String cardNumber, BigDecimal incomeAmount);

    /**
     * 会员卡消费记录
     * @param ucShopId
     * @param memberCardId
     * @param mobile
     * @return
     */
    Result<List<MemberCardFlowDTO>> listMemberCardTradeFlow4Wechat(Long ucShopId, Long memberCardId, String mobile);

    /**
     *计次卡消费记录
     * @param ucShopId
     * @param comboId
     * @param mobile
     * @return
     */
    Result<List<ComboFlowDTO>> listComboConsumeFlow4Wechat(Long ucShopId, Long comboId, String mobile);

    /**
     * 查询会员卡详情
     * @param ucShopId
     * @param cardId
     * @return
     */
    Result<MemberCardDetailDTO> getCardDetail4Wechat(Long ucShopId, Long cardId);

    /**
     * 查询计次卡详情
     * @param ucShopId
     * @param comboId
     * @return
     */
    Result<ComboDetailDTO> getComboDetail4Wechat(Long ucShopId, Long comboId);

    /**
     * 获取账户下对应的优惠券(不包含在优惠套餐中)
     * @param ucShopId UC店铺id
     * @param mobile 用户手机号码
     * @return
     */
    Result<List<WxCouponDTO>>  getCouponListWithoutSuite(Long ucShopId, String mobile);

    /**
     * 获取账户下对应的优惠券(包含在优惠套餐中)
     * @param ucShopId
     * @param mobile
     * @return
     */
    Result<List<CouponSuiteDTO>> getCouponSuiteList(Long ucShopId, String mobile);

    /**
     * 优惠卷详情查询接口
     * @param ucShopId
     * @param couponId
     * @return
     */
    Result<WxCouponDTO> getCouponDetail(Long ucShopId, Long couponId);

    /**
     * 判断用户是否拥有某种类型的优惠券
     * @param ucShopId 店铺UC ID
     * @param mobile 手机号码
     * @param couponTypeId 优惠券类型id
     * @return
     */
    Result<Boolean> hasOwnedCouponByCouponTypeId(Long ucShopId,String mobile, Long couponTypeId);

    Result<Boolean> isMultiAccount(Long ucShopId, String mobile);
}
