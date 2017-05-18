package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.account.bo.AccountAndCarBO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.customer.Customer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface AccountInfoService{
    /**
     * 创建账户信息
     * @param shopId 门店id
     * @param customerId 客户id
     */
    @Deprecated
    Result generateAccountInfo(Long shopId,Long customerId);

    /**
     * 创建账户信息
     * @param customer
     * @return
     */
    Result generateAccountInfo(Customer customer);

    /**
     * 是否存在账户
     * @param shopId 门店id
     * @param customerId 客户id
     */
    boolean isExist(Long shopId,Long customerId);

    /**
     * 获取账户信息
     * @param shopId 门店id
     * @param customerId 客户id
     */
    AccountInfo getAccountInfoByCustomerIdAndShopId(Long shopId,Long customerId);

    /**
     * 获取账户信息
     *
     * @param shopId
     * @param accountId
     * @return
     */
    AccountInfo getAccountInfoAllById(Long shopId, Long accountId);
    /**
     * 获取账户信息
     * @param shopId 门店id
     * @param carId 车辆id
     * @return
     */
    AccountInfo getAccountInfoByCarIdAndShopId(Long shopId,Long carId);

    /**
     * 通过车辆id，获取关联的账户（客户）信息
     * @param shopId
     * @param carId
     * @return
     */
    List<AccountInfo> getBindAccountInfosByCarId(Long shopId,Long carId);
    /**
     * 获取账户信息
     * @param accountId 账户id
     */
    AccountInfo getAccountInfoById(Long accountId);

    /**
     * 通过会员卡号,获取账户信息
     * @param shopId
     * @param accountId
     */
    AccountInfo getAccountInfoById(Long shopId, Long accountId);


    /**
     * 是否可以删除账户信息（只有无消费记录、无卡券的情况）
     * @param shopId 门店id
     * @param customerId 客户id
     */
    Result isDeleteAccountInfo(Long shopId,Long customerId);

    /**
     * 删除账户信息
     * @param shopId 门店id
     * @param customerId 客户id
     */
    Result deleteAccountInfo(Long shopId,Long customerId);

    /**
     * 根据ids获取账户信息List
     *
     */
    List<AccountInfo> getInfoByIds(Collection<Long> ids);

    List<AccountInfo> getInfoByCustomerIds(List<Long> ids);

    void batchInsert(List<AccountInfo> accountInfoList);

    Map<Long,Long> getCustomerIdAccountIdMap(Long shopId);
    /**
     * 创建来源为微信的云修账户
     * @param shopId
     * @param mobile
     * @return
     */
    boolean createWeChatAccount(Long shopId,String mobile);

    /**
     * 根据手机号获取账户信息
     * @param shopId
     * @param mobile
     * @return
     */
    List<AccountInfo> getAccountInfoByMobile(Long shopId, String mobile);

    List<String> listLicenseByAccountId(Long shopID, Long accountId);

    boolean bundlingCarToAccount(AccountAndCarBO accountAndCarBO);

    void bundCustomerCarToAccountInfo(AccountAndCarBO accountAndCarBO);

    boolean unBundlingCarFromAccount(AccountAndCarBO accountAndCarBO);
}
