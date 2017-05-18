package com.tqmall.legend.server.subsidy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.balance.FinanceAccountDao;
import com.tqmall.legend.dao.privilege.ShopManagerLoginDao;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.object.param.subsidy.RpcUserDefaultACSetParam;
import com.tqmall.legend.object.result.subsidy.UserDefaultBankDTO;
import com.tqmall.legend.pojo.balance.FinanceACCheckEnum;
import com.tqmall.legend.service.subsidy.RpcSubsidyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 16/2/24.
 */
@Slf4j
@Service("rpcSubsidyService")
public class RpcSubsidyServiceImpl implements RpcSubsidyService {
    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private ShopManagerService shopManagerService;

    @Autowired
    private ShopManagerLoginDao shopManagerLoginDao;

    @Autowired
    private FinanceAccountDao financeAccountDao;

    @Override
    public Result<UserDefaultBankDTO> getUserDefaultBank(Long userId, Long shopId) {
        UserDefaultBankDTO userDefaultBankDTO = new UserDefaultBankDTO();
        List<FinanceAccount> financeAccounts = financeAccountService.getDefaultFinanceAccount(userId, shopId, 1);
        if (CollectionUtils.isEmpty(financeAccounts) || financeAccounts.size() > 1) {
            return Result.wrapSuccessfulResult(null);
        }
        FinanceAccount financeAccount = financeAccounts.get(0);
        userDefaultBankDTO.setUserName(financeAccount.getAccountUser());
        userDefaultBankDTO.setAccount(financeAccount.getAccount());
        userDefaultBankDTO.setAccountBank(financeAccount.getAccountBank());
        userDefaultBankDTO.setAccountId(financeAccount.getId());
        userDefaultBankDTO.setAccountType(financeAccount.getAccountType());
        userDefaultBankDTO.setBank(financeAccount.getBank());
        userDefaultBankDTO.setBankProvince(financeAccount.getBankProvince());
        userDefaultBankDTO.setBankCity(financeAccount.getBankCity());
        userDefaultBankDTO.setBankDistrict(financeAccount.getBankDistrict());
        userDefaultBankDTO.setBankProvinceId(financeAccount.getBankProvinceId());
        userDefaultBankDTO.setBankCityId(financeAccount.getBankCityId());
        userDefaultBankDTO.setBankDistrictId(financeAccount.getBankDistrictId());
        return Result.wrapSuccessfulResult(userDefaultBankDTO);
    }

    @Override
    public Result<UserDefaultBankDTO> userDefaultACSet(RpcUserDefaultACSetParam rpcUserDefaultACSetParam) {
        Result result = setUserDefaultACParamCheck(rpcUserDefaultACSetParam);
        if (null == result || !result.isSuccess()) {
            log.error("[用户账户] 设置用户默认提现账户.参数错误.rpcUserDefaultACSetParam:{}", rpcUserDefaultACSetParam);
            return result;
        }
        //如果传了账号id,并校验通过,就不再往下进行
        if (null != rpcUserDefaultACSetParam.getAccountId() && rpcUserDefaultACSetParam.getAccountId() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("userId", rpcUserDefaultACSetParam.getUserId());
            param.put("shopId", rpcUserDefaultACSetParam.getShopId());
            param.put("id", rpcUserDefaultACSetParam.getAccountId());
            param.put("isDeleted", "N");
            param.put("accountType", rpcUserDefaultACSetParam.getAccountType());
            List<FinanceAccount> financeAccountList = financeAccountService.select(financeAccountDao, param);
            if (CollectionUtils.isEmpty(financeAccountList)) {
                log.error("[用户账户] 设置默认提现账户.参数错误.当前账户id非默认账户或不存在 accountId:{}", rpcUserDefaultACSetParam.getAccountId());
                return Result.wrapErrorResult("", "当前账户非默认账户或不存在.");
            } else {
                FinanceAccount financeAccountTemp = new FinanceAccount();
                financeAccountTemp.setId(rpcUserDefaultACSetParam.getAccountId());
                financeAccountTemp.setUserId(rpcUserDefaultACSetParam.getUserId());
                financeAccountTemp.setShopId(rpcUserDefaultACSetParam.getShopId());
                financeAccountTemp.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                financeAccountService.upDateByShopIdAndUserIdAndAccountType(financeAccountTemp);
                FinanceAccount financeAccountTempOne = new FinanceAccount();
                financeAccountTempOne.setId(rpcUserDefaultACSetParam.getAccountId());
                financeAccountTempOne.setGmtModified(new Date());
                if (!rpcUserDefaultACSetParam.getUserName().contains("*")) {
                    financeAccountTempOne.setAccountUser(rpcUserDefaultACSetParam.getUserName());
                }
                financeAccountTempOne.setBank(rpcUserDefaultACSetParam.getBank());
                financeAccountTempOne.setBankProvince(rpcUserDefaultACSetParam.getBankProvince());
                financeAccountTempOne.setBankCity(rpcUserDefaultACSetParam.getBankCity());
                financeAccountTempOne.setBankDistrict(rpcUserDefaultACSetParam.getBankDistrict());
                financeAccountTempOne.setBankProvinceId(rpcUserDefaultACSetParam.getBankProvinceId());
                financeAccountTempOne.setBankCityId(rpcUserDefaultACSetParam.getBankCityId());
                financeAccountTempOne.setBankDistrictId(rpcUserDefaultACSetParam.getBankDistrictId());
                financeAccountTempOne.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                financeAccountTempOne.setCheckStatus(FinanceACCheckEnum.DSH.getCode());
                financeAccountService.upDateById(financeAccountTempOne);

                //设置返回结果
                UserDefaultBankDTO userDefaultBankDTO = new UserDefaultBankDTO();
                userDefaultBankDTO.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                if (!rpcUserDefaultACSetParam.getUserName().contains("*")) {
                    userDefaultBankDTO.setUserName(rpcUserDefaultACSetParam.getUserName());
                } else {
                    userDefaultBankDTO.setUserName(financeAccountList.get(0).getAccountUser());
                }
                userDefaultBankDTO.setAccount(financeAccountList.get(0).getAccount());
                userDefaultBankDTO.setAccountType(financeAccountList.get(0).getAccountType());
                return Result.wrapSuccessfulResult(userDefaultBankDTO);
            }
        } else {
            //如果没传账号id
            Map<String, Object> param = new HashMap<>();
            param.put("userId", rpcUserDefaultACSetParam.getUserId());
            param.put("shopId", rpcUserDefaultACSetParam.getShopId());
            param.put("account", rpcUserDefaultACSetParam.getAccount());
            param.put("isDeleted", "N");
            param.put("accountType", rpcUserDefaultACSetParam.getAccountType());
            param.put("accountUser", rpcUserDefaultACSetParam.getUserName());
            List<FinanceAccount> financeAccountList = financeAccountService.select(financeAccountDao, param);
            if (CollectionUtils.isEmpty(financeAccountList)) {
                FinanceAccount financeAccountTemp = new FinanceAccount();
                financeAccountTemp.setId(0l);
                financeAccountTemp.setUserId(rpcUserDefaultACSetParam.getUserId());
                financeAccountTemp.setShopId(rpcUserDefaultACSetParam.getShopId());
                financeAccountTemp.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                financeAccountService.upDateByShopIdAndUserIdAndAccountType(financeAccountTemp);
                FinanceAccount financeAccount = new FinanceAccount();
                financeAccount.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                financeAccount.setAccount(rpcUserDefaultACSetParam.getAccount());
                financeAccount.setShopId(rpcUserDefaultACSetParam.getShopId());
                financeAccount.setUserId(rpcUserDefaultACSetParam.getUserId());
                financeAccount.setAccountUser(rpcUserDefaultACSetParam.getUserName());
                financeAccount.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                financeAccount.setIsDefault("1");
                financeAccount.setBank(rpcUserDefaultACSetParam.getBank());
                financeAccount.setBankProvince(rpcUserDefaultACSetParam.getBankProvince());
                financeAccount.setBankCity(rpcUserDefaultACSetParam.getBankCity());
                financeAccount.setBankDistrict(rpcUserDefaultACSetParam.getBankDistrict());
                financeAccount.setBankProvinceId(rpcUserDefaultACSetParam.getBankProvinceId());
                financeAccount.setBankCityId(rpcUserDefaultACSetParam.getBankCityId());
                financeAccount.setBankDistrictId(rpcUserDefaultACSetParam.getBankDistrictId());
                financeAccount.setCheckStatus(FinanceACCheckEnum.DSH.getCode());
                if (financeAccountService.insertFinanceAccount(financeAccount) != null) {
                    //设置返回结果
                    UserDefaultBankDTO userDefaultBankDTO = new UserDefaultBankDTO();
                    userDefaultBankDTO.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                    userDefaultBankDTO.setAccount(rpcUserDefaultACSetParam.getAccount());
                    userDefaultBankDTO.setUserName(rpcUserDefaultACSetParam.getUserName());
                    userDefaultBankDTO.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                    return Result.wrapSuccessfulResult(userDefaultBankDTO);
                } else {
                    return Result.wrapErrorResult("", "默认提现账户设置失败.");
                }
            } else {
                if (financeAccountList.size() > 1) {
                    log.error("[用户账户] 设置默认提现账户.用户存在多条相同的账户信息. rpcUserDefaultACSetParam:{}", rpcUserDefaultACSetParam);
                    return Result.wrapErrorResult("", "当前账户信息存在多条.");
                } else {
                    FinanceAccount financeAccountTempOne = new FinanceAccount();
                    financeAccountTempOne.setId(0l);
                    financeAccountTempOne.setUserId(rpcUserDefaultACSetParam.getUserId());
                    financeAccountTempOne.setShopId(rpcUserDefaultACSetParam.getShopId());
                    financeAccountTempOne.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                    financeAccountService.upDateByShopIdAndUserIdAndAccountType(financeAccountTempOne);

                    FinanceAccount financeAccount = financeAccountList.get(0);
                    FinanceAccount financeAccountTemp = new FinanceAccount();
                    financeAccountTemp.setId(financeAccount.getId());
                    financeAccountTemp.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                    financeAccountTemp.setIsDefault("1");
                    financeAccountTemp.setBank(rpcUserDefaultACSetParam.getBank());
                    financeAccountTemp.setBankProvince(rpcUserDefaultACSetParam.getBankProvince());
                    financeAccountTemp.setBankCity(rpcUserDefaultACSetParam.getBankCity());
                    financeAccountTemp.setBankDistrict(rpcUserDefaultACSetParam.getBankDistrict());
                    financeAccountTemp.setBankProvinceId(rpcUserDefaultACSetParam.getBankProvinceId());
                    financeAccountTemp.setBankCityId(rpcUserDefaultACSetParam.getBankCityId());
                    financeAccountTemp.setBankDistrictId(rpcUserDefaultACSetParam.getBankDistrictId());
                    financeAccountTemp.setCheckStatus(FinanceACCheckEnum.DSH.getCode());
                    if (financeAccountService.upDateById(financeAccountTemp) < 1) {
                        return Result.wrapErrorResult("", "默认提现账户设置失败.");
                    } else {
                        //设置返回结果
                        UserDefaultBankDTO userDefaultBankDTO = new UserDefaultBankDTO();
                        userDefaultBankDTO.setAccountBank(rpcUserDefaultACSetParam.getAccountBank());
                        userDefaultBankDTO.setAccount(rpcUserDefaultACSetParam.getAccount());
                        userDefaultBankDTO.setUserName(rpcUserDefaultACSetParam.getUserName());
                        userDefaultBankDTO.setAccountType(rpcUserDefaultACSetParam.getAccountType());
                        return Result.wrapSuccessfulResult(userDefaultBankDTO);
                    }
                }
            }
        }
    }

    /**
     * 设置默认提现账号参数校验
     */
    private Result setUserDefaultACParamCheck(RpcUserDefaultACSetParam rpcUserDefaultACSetParam) {
        if (null == rpcUserDefaultACSetParam) {
            log.error("[用户账户] 设置默认提现账户参数为空");
            return Result.wrapErrorResult("", "设置默认提现账户参数为空");
        } else {
            //用户id校验
            if (null == rpcUserDefaultACSetParam.getUserId() || rpcUserDefaultACSetParam.getUserId() < 1) {
                log.error("[用户账户] 设置默认提现账户.用户信息错误. userId:{}", rpcUserDefaultACSetParam.getUserId());
                return Result.wrapErrorResult("", "用户信息错误.");
            }
            //店铺id校验
            if (null == rpcUserDefaultACSetParam.getShopId() || rpcUserDefaultACSetParam.getShopId() < 1) {
                log.error("[用户账户] 设置默认提现账户.店铺信息错误. userId:{}", rpcUserDefaultACSetParam.getShopId());
                return Result.wrapErrorResult("", "店铺信息错误.");
            }
            //用户密码校验
            if (StringUtils.isBlank(rpcUserDefaultACSetParam.getPassWord())) {
                log.error("[用户账户] 设置默认提现账户.用户密码为空.");
                return Result.wrapErrorResult("", "用户登录密码为空");
            } else {
                //1.获取用户信息
                ShopManager shopManager = shopManagerService.selectByShopIdAndManagerId(rpcUserDefaultACSetParam.getShopId(), rpcUserDefaultACSetParam.getUserId());
                if (null == shopManager) {
                    log.error("[用户账户] 设置默认提现账户.用户信息获取失败. userId={},shopId={}", rpcUserDefaultACSetParam.getUserId(), rpcUserDefaultACSetParam.getShopId());
                    return Result.wrapErrorResult("", "获取用户信息失败");
                }

                //2.获取用户的登录账号信息
                Map<String, Object> shopManagerParam = new HashMap<>();
                shopManagerParam.put("managerId", rpcUserDefaultACSetParam.getUserId());
                shopManagerParam.put("shopId", rpcUserDefaultACSetParam.getShopId());
                List<ShopManagerLogin> shopManagerLoginList = shopManagerLoginDao.select(shopManagerParam);
                if (CollectionUtils.isEmpty(shopManagerLoginList) || shopManagerLoginList.size() > 1) {
                    log.error("[用户账户] 设置默认提现账户.获取用户登录账号信息失败. userId:{},shopId:{}", rpcUserDefaultACSetParam.getUserId(), rpcUserDefaultACSetParam.getShopId());
                    return Result.wrapErrorResult("", "获取用户登录账号信息失败");
                }
                ShopManagerLogin shopManagerLogin = shopManagerLoginList.get(0);
                if (!StringUtils.equalsIgnoreCase(rpcUserDefaultACSetParam.getPassWord(), shopManagerLogin.getPassword())) {
                    log.error("[用户账户] 设置默认提现账户.用户提现密码与用户密码不符. userId:{},shopId:{}", rpcUserDefaultACSetParam.getUserId(), rpcUserDefaultACSetParam.getShopId());
                    return Result.wrapErrorResult("", "用户登录密码错误");
                }
            }
            //账户类型判断
            if (null == rpcUserDefaultACSetParam.getAccountType()) {
                log.error("[用户账户] 设置默认提现账户.账户类型为空");
                return Result.wrapErrorResult("", "账户类型为空");
            } else if (rpcUserDefaultACSetParam.getAccountType() <= 0 || rpcUserDefaultACSetParam.getAccountType() >= 3) {
                log.error("[用户账户] 设置默认提现账户.账户类型错误. accountType:{}", rpcUserDefaultACSetParam.getAccountType());
                return Result.wrapErrorResult("", "账户类型错误");
            }
            //账号id判断
            if (null == rpcUserDefaultACSetParam.getAccountId() || rpcUserDefaultACSetParam.getAccountId() < 1) {
                //账户姓名判断
                if (StringUtils.isBlank(rpcUserDefaultACSetParam.getUserName())) {
                    log.error("[用户账户] 设置默认提现账户. 账户持有人为空.");
                    return Result.wrapErrorResult("", "账户持有人为空.");
                }
                //账户账号判断
                if (StringUtils.isBlank(rpcUserDefaultACSetParam.getAccount())) {
                    log.error("[用户账户] 设置默认提现账户. 账户账号为空.");
                    return Result.wrapErrorResult("", "账户账号为空.");
                }
                //银行卡
                if (rpcUserDefaultACSetParam.getAccountType() == 1) {
                    if (StringUtils.isBlank(rpcUserDefaultACSetParam.getAccountBank())) {
                        log.error("[用户账户] 设置默认提现账户. 银行卡开户行为空.");
                        return Result.wrapErrorResult("", "银行卡开户行为空.");
                    }
                    String bankAccount = rpcUserDefaultACSetParam.getAccount().replaceAll(" ", "");
                    if (!StringUtil.isNumberOnly(bankAccount)) {
                        log.error("[用户账户] 设置默认提现账户.银行卡号信息错误. account:{}", bankAccount);
                        return Result.wrapErrorResult("", "银行卡号信息错误");
                    }
                }
            }
        }
        return Result.wrapSuccessfulResult("");
    }
}
