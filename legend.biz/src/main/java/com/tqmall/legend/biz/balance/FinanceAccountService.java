package com.tqmall.legend.biz.balance;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.balance.FinanceAccount;

import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
public interface FinanceAccountService extends BaseService {

    /**
     * 获取用户的关联的财务账号
     */
    public List<FinanceAccount> getUserFinanceAccount(Map<String, Object> param);

    /**
     * 获取用户的最新的财务账号
     *
     * @param userId      用户id
     * @param shopId      店铺id
     * @param accountType 账户类型
     */
    public FinanceAccount getLastFinanceAccount(Long userId, Long shopId, Integer accountType);

    /**
     * 获取用户的默认财务账号
     *
     * @param userId      用户id
     * @param shopId      店铺id
     * @param accountType 账户类型 1:银行卡2:支付保
     */
    public List<FinanceAccount> getDefaultFinanceAccount(Long userId, Long shopId, Integer accountType);

    /**
     * 插入用户的财务账号
     */
    public FinanceAccount insertFinanceAccount(FinanceAccount financeAccount);

    /**
     * 更新财务账号
     */
    public int upDateById(FinanceAccount financeAccount);

    /**
     * 把特定id之外的默认提现账户全部修改为非默认提现账户 根据用户id和店铺id和账户类型和特定id
     */
    public int upDateByShopIdAndUserIdAndAccountType(FinanceAccount financeAccount);

    /**
     * 获取门店的结算对账的银行卡
     *
     * @param shopId
     * @return
     */
    public FinanceAccount getSettleFinanceAccount(Long shopId);

    /**
     * 批量查询门店的银行卡信息
     *
     * @param shopIdList
     * @return
     */
    List<FinanceAccount> selectFinanceAccount(List<Long> shopIdList);

    /**
     *账户保存或更新
     * @param financeAccount
     * @return
     */
    Long  saveOrUpdate(FinanceAccount financeAccount);

}
