package com.tqmall.legend.facade.account;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.facade.account.vo.MemberCardBo;
import com.tqmall.legend.facade.account.vo.MemberGrantVo;
import com.tqmall.legend.facade.account.vo.MemberInfoVo;

import java.util.List;

/**
 * Created by wanghui on 6/2/16.
 */
public interface MemberFacadeService {

    /**
     * 创建新的会员卡类型
     * @return
     */
    void createMemberCardInfo(MemberInfoVo memberInfoVo);

    /**
     * 更新会员卡类型
     * @return
     */
    void updateMemberCardInfo(MemberInfoVo memberInfoVo);

    /**
     * 将会员卡类型置为不启用
     * @param shopId
     * @param memberTypeId
     */
    void disableMemberCardType(Long shopId, Long memberTypeId);

    /**
     * 将会员卡类型置为启用状态
     * @param shopId
     * @param memberTypeId
     */
    void enableMemberCardType(Long shopId, Long memberTypeId);

    /**
     * 根据车牌获取关联的会员信息
     * @param shopId 门店id
     * @param carLicense 会员卡
     * @return 会员卡信息,如无会员卡,则返回null
     */
    List<MemberCard> getMemberCardByCarLicense(Long shopId, String carLicense);

    /**
     * 充值会员卡
     * @param grantVo
     * @param shopId
     * @param userId
     * @param userName @return
     * */
    AccountTradeFlow grantMemberCard(MemberGrantVo grantVo, Long shopId, Long userId, String userName) throws BizException;

    List<MemberCard> getMemberCardByCustomerId(Long shopId, Long customerId);

    /**
     * 根据客户手机号获取关联的会员卡信息
     * @param shopId
     * @param mobile
     * @return
     */
    List<MemberCard> getMemberCardByMobile(Long shopId, String mobile);

    /**
     * 会员卡充值撤销
     * @param shopId
     * @param userId
     * @param flowId
     */
    void reverseRecharge(Long shopId, Long userId, Long flowId);

    /**
     * 查询可用的会员卡列表包括自己的和关联别人的
     * @param shopId
     * @param carId
     * @return
     */
    List<MemberCard> getAvailableListByCarId(Long shopId, Long carId);

    /**
     * 根据工单id查询上一次结算使用的会员卡信息
     * @param shopId
     * @param orderId
     * @return
     */
    MemberCardBo getUsedForOrderLastSettle(Long shopId, Long orderId);

    /**
     * 判断会员卡是否属于其他人（不是归属和关联账户下的）
     * @param shopId
     * @param memberCardId
     * @param customerCarId
     * @return
     */
    boolean isBelongOther(Long shopId, Long memberCardId, Long customerCarId);
}
