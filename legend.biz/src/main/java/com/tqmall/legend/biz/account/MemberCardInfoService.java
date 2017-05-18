package com.tqmall.legend.biz.account;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.account.bo.CardCreateBo;
import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.account.MemberCardInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by wanghui on 6/2/16.
 */
public interface MemberCardInfoService extends BaseService {

    List<MemberCardInfo> findAllByShopId(Long shopId,Integer type);

    @Deprecated
    MemberCardInfo findById(Long id);

    boolean saveMemberCardInfo(MemberCardInfo memberCardInfo);

    boolean deleteMemberCardInfoById(Long shopId, Long id);

    /**
     * 根据会员卡类型名获取会员卡信息
     * @param shopId
     * @param cardTypeName
     * @return
     */
    MemberCardInfo findByCardTypeName(Long shopId, String cardTypeName);

    void enableCardInfoById(Long id,UserInfo userInfo);

    void disableCardInfoById(Long id,UserInfo userInfo);

    List<MemberCardInfo> selectInfoByIds(Long shopId, List<Long> ids);

    /**
     * 查询会员卡类型总数
     * @param shopId
     * @return
     */
    Integer selectCount(Long shopId);

    /**
     * 根据名字批量查询
     * @param names
     * @param shopId
     * @return
     */
    List<MemberCardInfo> selectByNames(List<String> names, Long shopId);

    /**
     * 创建会员卡类型
     * @param cardCreateBo
     */
    void create(CardCreateBo cardCreateBo);

    /**
     * 修改会员卡类型
     * @param updateBo
     */
    void update(CardCreateBo updateBo);

    /**
     * 获取会员卡类型
     * @param shopId
     * @param cardTypeId
     * @return
     */
    MemberCardInfo findById(Long shopId, Long cardTypeId);

    void attachDiscount(MemberCardInfo cardInfo);

    /**
     * 批量组装会员卡折扣关联关系
     * @param cardInfoList
     */
    void attachDiscount(List<MemberCardInfo> cardInfoList);

    String getTypeNameById(Long shopId, Long id);

    List<MemberCardInfo> list(Long shopId);

    Map<Long, String> getIdNameMap(Long shopId, List<Long> cardTypeIds);
}
