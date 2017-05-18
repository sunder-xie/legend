package com.tqmall.legend.dao.balance;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.balance.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@MyBatisRepository
public interface FinanceAccountDao extends BaseDao<FinanceAccount> {
    /**
     * 根据店铺id 和 用户id更新账户信息 银行卡提现 为非默认提现账号
     * userId和shopId必填
     */
    public int updateByShopIdAndUserId(FinanceAccount financeAccount);

    /**
     * 根据id,店铺id 和 用户id 账户类型 更新账户信息为非默认提现账号
     * id,userId和shopId,accountType必填
     */
    public int updateByShopIdAndUserIdAndAccountType(FinanceAccount financeAccount);

    /**
     * 根据shopIds批量查询银行卡信息
     *
     * @param
     * @return
     */
    List<FinanceAccount> selectShopFinanceAccount(@Param("userId") Long userId, @Param("shopIds") List<Long> shopIds);
}