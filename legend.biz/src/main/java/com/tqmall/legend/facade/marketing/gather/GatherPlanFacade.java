package com.tqmall.legend.facade.marketing.gather;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import com.tqmall.legend.entity.marketing.gather.GatherCustomerNote;
import com.tqmall.legend.facade.marketing.gather.bo.GatherCustomerNoteBO;
import com.tqmall.legend.facade.marketing.gather.param.FeedbackByPhoneParam;
import com.tqmall.legend.facade.marketing.gather.vo.CustomerTypeNum;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCouponConfigVo;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCustomerVO;
import com.tqmall.legend.facade.marketing.gather.vo.ReceiveCouponResultVo;
import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by xin on 2016/12/16.
 */
public interface GatherPlanFacade {

    /**
     * 查询不同分类客户数
     * @param shopId
     * @param userId
     * @return
     */
    CustomerTypeNum getCustomerTypeNum(Long shopId, Long userId);

    /**
     * 查询不同分类客户分页列表
     * @param shopId
     * @param userId
     * @param customerType
     * @return
     */
    Page<GatherCustomerVO> getGatherCustomerPage(Long shopId, Long userId, String customerType, Pageable pageable);

    /**
     * 电话回访
     * @param param
     * @param userInfo
     * @return
     */
    Boolean feedbackByPhone(FeedbackByPhoneParam param, UserInfo userInfo);

    /**
     * 发送短信
     * @param key
     * @param noteType
     * @return
     */
    Boolean sendSms(String key, Long couponInfoId, Integer noteType, UserInfo userInfo);

    /**
     *计算全部客户所需发送短信数
     * @param shopId
     * @param userId
     * @param customerType
     * @param template
     * @return
     */
    MarketingSmsTempBO getAllCustomerSms(Long shopId, Long userId, String customerType, String template);

    /**
     * 计算搜索客户所需发送短信数
     * @param shopId
     * @param userId
     * @param searchKey
     * @param template
     * @return
     */
    MarketingSmsTempBO getSearchCustomerSms(Long shopId, Long userId, String searchKey, String template);

    /**
     * 保存集客信息
     * @param gatherCustomerNoteBO
     * @return 主键id
     */
    GatherCustomerNote saveGatherCustomer(GatherCustomerNoteBO gatherCustomerNoteBO);

    /**
     * 集客老客户带新送优惠券配置信息保存
     * @param gatherCouponConfig
     * @param userInfo
     * @return
     */
    GatherCouponConfig saveGatherCouponConfig(GatherCouponConfig gatherCouponConfig, UserInfo userInfo);

    /**
     * 获取集客送券配置信息,包括门店名称和地址信息,更新访问状态
     * @param gatherCouponConfigId
     * @return
     */
    GatherCouponConfigVo getGatherCouponInfo(Long gatherCouponConfigId);

    /**
     * 老客户带新领取优惠券
     * @param mobile
     * @param gatherCouponConfigId
     * @return
     */
    ReceiveCouponResultVo grantCoupon(String mobile, Long gatherCouponConfigId);

    /**
     * 集客送券配置首次访问处理
     * @param gatherCouponConfigId
     */
    void accessDeal(Long gatherCouponConfigId);
}
