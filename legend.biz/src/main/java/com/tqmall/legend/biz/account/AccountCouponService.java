package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.AccountCoupon;
import com.tqmall.legend.entity.account.AccountCouponFlowDetail;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.vo.AccountCouponVo;
import com.tqmall.legend.enums.coupon.AccountCouponSourceEnum;
import com.tqmall.legend.facade.wechat.vo.WechatAccountCouponVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AccountCouponService {

    /**
     * 获取账户下未使用的券的数量
     *
     * @param shopId    门店id
     * @param accountId 账户id
     */
    Integer getAccountCouponCountOfUnUsed(Long shopId, Long accountId);

    /**
     * 判断账户下是否存在券
     *
     * @param shopId    门店id
     * @param accountId 账户id
     */
    boolean isExist(Long shopId, Long accountId);

    public AccountTradeFlow grant(AccountCouponVo accountCouponVo);

    /**
     * 领券
     *
     * @param accountCouponVo
     * @return
     */
    AccountCoupon grant(AccountCouponVo accountCouponVo, AccountCouponSourceEnum couponSource);

    List<AccountCoupon> getAccountCoupon(Long shopId, Long accountId);

    List<AccountCoupon> getAccountCouponListByGroup(Long shopId, Long accountId);

    /**
     * 获取账户下可用的优惠券列表
     *
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountCoupon> findAvailableAccountCoupon(Long shopId, Long accountId);

    /**
     * 获取账户下可用的优惠券列表(包括关联账户)
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCoupon> findAvailableAccountCouponByAccountIds(Long shopId, List<Long> accountIds);

    /**
     * 获取账户下所有的优惠券列表
     *
     * @param shopId
     * @param accountId
     * @return
     */
    List<AccountCoupon> findAllAccountCoupon(Long shopId, Long accountId);

    /**
     * 获取账户下所有的优惠券列表(包括关联账户)
     *
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCoupon> findAllAccountCouponByAccountIds(Long shopId, List<Long> accountIds);

    /**
     * 根据优惠券券码获取优惠券实例信息
     *
     * @param shopId   店铺id
     * @param couponSn 优惠券券码
     * @return 若不存在优惠券则返回null
     */
    List<AccountCoupon> findByCouponSn(Long shopId, String... couponSn);

    void batchInsert(List<AccountCoupon> accountCouponList);

    Boolean consume(Long shopId, Long userId, List<Long> ids, Map<Long, Long> flowIdMap);

    AccountCoupon findById(Long id, Long shopId);

    boolean update(AccountCoupon accountCoupon);

    void delete(Long shopId);

    void reverseConsume(Long userId, Long shopId, Long reverseFlowId, AccountCouponFlowDetail item);

    List<AccountCoupon> findByFlowId(Long shopId, Long suitId);

    List<AccountCoupon> select(Map param);

    Integer deleteByIds(List<Long> ids);

    /**
     * 获取目前未生成的券码
     *
     * @param snList
     * @return
     */
    List<String> selectExistsSn(String[] snList);

    /**
     * 查询账户优惠券列表(包括优惠券类型信息,来源信息)
     *
     * @param searchParams
     * @return
     */
    public List<WechatAccountCouponVo> qryAccountCoupon(Map<String, Object> searchParams);

    public Integer selectCount(Map param);

    void insert(AccountCoupon coupon);

    Page<AccountCoupon> getAccountCouponByPage(Pageable pageable, Map<String, Object> params);

    /**
     * 判断账户是否拥有某种类型类型的优惠券(微信端需求)
     *
     * @param shopId
     * @param accountId
     * @param couponTypeId
     * @return
     */
    boolean hasOwnedCouponByType(Long shopId, Long accountId, Long couponTypeId);

    /**
     * 查询账户下的门店发放券(非套餐)
     *
     * @param shopID
     * @param accountId
     * @return
     */
    List<AccountCoupon> listFreeCoupon(Long shopID, Long accountId);

    /**
     * 查询账户下的套餐券
     *
     * @param shopID
     * @param accountId
     * @return
     */
    List<AccountCoupon> listBundleCoupon(Long shopID, Long accountId);

    /**
     * 通过优惠券账户交易流水id，获取优惠券账户信息
     *
     * @param shopId
     * @param flowIds
     * @return
     */
    List<AccountCoupon> findAccountCouponByFlowIds(Long shopId, List<Long> flowIds);

    /**
     * 批量查询账户下未过期的优惠券数量
     * @param shopId
     * @param accountIds
     * @return
     */
    Map<Long, Long> getUnExpireCouponNum(Long shopId, Collection<Long> accountIds);

    /**
     * 批量查询账户下未过期的优惠券
     * @param shopId
     * @param accountIds
     * @return
     */
    List<AccountCoupon> getUnExpireCouponList(Long shopId, Collection<Long> accountIds);

    List<AccountCoupon> listByIds(Collection<Long> ids);
}
