package com.tqmall.legend.facade.account;


import com.tqmall.common.exception.BizException;
import com.tqmall.legend.entity.account.*;
import com.tqmall.legend.facade.account.vo.CustomerDiscountInfo;
import com.tqmall.legend.facade.account.vo.CustomerDiscountInfoForApp;
import com.tqmall.legend.facade.discount.bo.DiscountInfoBo;
import com.tqmall.legend.facade.discount.bo.DiscountSelectedBo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghui on 6/6/16.
 */
public interface AccountFacadeService {

    /**
     * 根据工单id获取对应的可用优惠信息
     *
     * @param shopId  店铺id
     * @param orderId 工单id
     * @return
     */
    DiscountInfoBo getDiscountInfoByOrderId(Long shopId, Long orderId);

    /**
     * 根据工单信息和已选中优惠信息获取优惠信息
     *
     * @param shopId
     * @param orderId
     * @param selectedItem
     * @return
     */
    DiscountInfoBo getDiscountInfoByOrderId(Long shopId, Long orderId, DiscountSelectedBo selectedItem);


    /**
     * @param shopId        门店id
     * @param carLicense    车牌号码
     * @param serviceId     洗车服务id,如自定义金额,则为空
     * @param amount        洗车单金额
     * @param selectedItem 已选中优惠信息
     * @return
     */
    DiscountInfoBo getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId, BigDecimal amount, DiscountSelectedBo selectedItem);

    /**
     * 获取洗车单的优惠信息
     *
     * @param shopId     店铺id
     * @param carLicense 车牌号码
     * @param serviceId  洗车服务id,如自定义金额,则为空
     * @param amount     洗车单金额
     * @return
     */
    DiscountInfoBo getCarWashDiscountInfo(Long shopId, String carLicense, Long serviceId, BigDecimal amount);

    /**
     * 账单确认(洗车单+快修快保单)
     *
     * @param shopId       店铺id
     * @param userId
     * @param orderId
     * @return
     */
    void settlementAccount(Long shopId, Long userId, Long orderId, DiscountSelectedBo discountSelectedBo);

    /**
     * 工单收款
     *
     * @param shopId        店铺id
     * @param userId
     * @param memberCardId  会员卡id
     * @param cardPayAmount 本次使用会员卡的金额
     * @return
     */
    void debitAccount(Long shopId, Long userId, Long orderId, Long memberCardId, BigDecimal cardPayAmount);

    /**
     * 会员优惠信息反冲
     *  @param shopId
     * @param operator
     * @param orderId
     */
    void revertSettlement(Long shopId, Long operator, Long orderId);


    /**
     * 创建只有单个服务的计次卡类型
     *
     * @param shopId              店铺id
     * @param operatorId          操作人id
     * @param comboName           计次卡名称
     * @param effectivePeriodDays 计次卡有效时间
     * @param salePrice           计次卡售价
     * @param remark              计次卡备注
     * @param serviceId           计次卡关联服务id
     * @param serviceCount        计次卡关联服务次数
     * @return 计次卡类型id
     * @throws BizException 业务异常
     */
    Long createSimpleComboType(Long shopId, Long operatorId, String comboName, Long effectivePeriodDays, BigDecimal salePrice, String remark, Long serviceId, Integer serviceCount) throws BizException;

    /**
     * 根据id查询comboInfo
     * @param shopId
     * @param comboInfoId
     * @return
     */
    ComboInfo getComboInfo(Long shopId, Long comboInfoId);

    /**
     *查询门店设置的所有售价为0且余额为0的所有有效(启用)的会员卡
     * @param shopId
     * @return
     */
    List<MemberCardInfo> listFreeMemberCardInfo(Long shopId) throws BizException;

    /**
     * 查询账户下门店发放的优惠券(非套餐)
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountCoupon> listFreeCoupons(Long shopId, Long accountId);

    /**
     * 查询账户下优惠券(套餐)
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountSuite> listBundleCoupons(Long shopId, Long accountId);


    /**
     * 查询优惠券详情
     * @param shopID
     * @param couponId
     * @return
     */
    AccountCoupon getCouponDetail(Long shopID, Long couponId);

    /**
     * 查询客户优惠信息
     * @param shopId
     * @param customerCarId
     * @return
     */
    List<CustomerDiscountInfo> getCustomerDiscountInfo(Long shopId, Long customerCarId);

    List<CustomerDiscountInfoForApp> getCustomerDiscountInfoForApp(Long shopId, Long customerCarId);

    /**
     * 根据车辆id查询归属和关联账户
     * @param shopId
     * @param customerCarId
     * @return key 账户id，value 客户id
     */
    Map<Long, Long> getAccountIdAndCustomerIdByCarId(Long shopId, Long customerCarId);
}
