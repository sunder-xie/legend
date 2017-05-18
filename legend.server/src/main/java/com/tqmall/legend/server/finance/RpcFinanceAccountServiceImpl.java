package com.tqmall.legend.server.finance;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.object.result.finance.FinanceAccountDTO;
import com.tqmall.legend.service.finance.RpcFinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zsy on 16/9/17.
 */
@Slf4j
@Service("rpcFinanceAccountService")
public class RpcFinanceAccountServiceImpl implements RpcFinanceAccountService {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private FinanceAccountService financeAccountService;

    @Override
    public Result<FinanceAccountDTO> getShopFinanceAccount(String source, Integer ucShopId) {
        log.info("【dubbo】根据ucShopId获取门店的对账银行卡信息，source:{},ucShopId:{}", source, ucShopId);
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), "来源不能为空");
        }
        if (ucShopId == null || ucShopId <= 0) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), "ucShopId有误");
        }
        Shop shop = shopService.getShopByUserGlobalId(Long.valueOf(ucShopId));
        if (shop == null) {
            return Result.wrapErrorResult(LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getCode(), "门店不存在");
        }
        Long shopId = shop.getId();
        FinanceAccount financeAccount = financeAccountService.getSettleFinanceAccount(shopId);
        if (financeAccount == null) {
            return Result.wrapErrorResult(LegendErrorCode.NO_BANK_BIND_ERROR.getCode(), "门店还未绑定银行卡信息");
        }
        //查询管理员账号
        ShopManager shopManager = shopManagerService.getAdmin(shopId);
        String mobile;
        if (shopManager != null) {
            mobile = shopManager.getMobile();
        } else {
            mobile = shop.getMobile();
        }
        FinanceAccountDTO financeAccountDTO = new FinanceAccountDTO();
        financeAccountDTO.setAccountUser(financeAccount.getAccountUser());
        financeAccountDTO.setMobile(mobile);
        financeAccountDTO.setAccount(financeAccount.getAccount());
        financeAccountDTO.setBank(financeAccount.getBank());
        financeAccountDTO.setAccountBank(financeAccount.getAccountBank());
        financeAccountDTO.setBankProvinceId(financeAccount.getBankProvinceId());
        financeAccountDTO.setBankProvince(financeAccount.getBankProvince());
        financeAccountDTO.setBankProvince(financeAccount.getBankProvince());
        financeAccountDTO.setBankCity(financeAccount.getBankCity());
        financeAccountDTO.setBankCityId(financeAccount.getBankCityId());
        financeAccountDTO.setUcShopId(ucShopId);
        return Result.wrapSuccessfulResult(financeAccountDTO);
    }

    @Override
    public Result<List<FinanceAccountDTO>> selectShopFinanceAccountList(String source, List<Integer> ucShopIdList) {
        List<FinanceAccountDTO> financeAccountDTOs = new ArrayList<>();
        log.info("【dubbo】根据ucShopIdList批量获取门店的银行卡信息，source:{},ucShopIdList:{}", source, LogUtils.objectToString(ucShopIdList));
        if (StringUtils.isBlank(source)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), "来源不能为空");
        }
        if (CollectionUtils.isEmpty(ucShopIdList)) {
            return Result.wrapErrorResult(LegendErrorCode.DUBBO_SOURCE_NULL_EX.getCode(), "ucShopIdList不能为空");
        }
        //对传入的ucShopId去重并转成String类型
        Set<String> ucShopIdSet = new HashSet();
        for (Integer ucShopId : ucShopIdList) {
            if (ucShopId != null && ucShopId != 0) {
                ucShopIdSet.add(ucShopId.toString());
            }
        }
        List<String> ucList = new ArrayList<>(ucShopIdSet);
        //查询门店信息
        List<Shop> shopList = shopService.selectShopByUserGlobalIdList(ucList);
        if (CollectionUtils.isEmpty(shopList)) {
            return Result.wrapErrorResult(LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getCode(), "门店信息均不存在");
        }
        List<Long> shopIds = Lists.newArrayList();
        Map<Long, Shop> shopMap = Maps.newConcurrentMap();
        for (Shop shop : shopList) {
            shopIds.add(shop.getId());
            shopMap.put(shop.getId(), shop);
        }

        //查询银行卡信息
        List<FinanceAccount> financeAccountList = financeAccountService.selectFinanceAccount(shopIds);
        if (CollectionUtils.isEmpty(financeAccountList)) {
            return Result.wrapSuccessfulResult(financeAccountDTOs);
        }
        //查询管理员账号
        List<ShopManager> shopManagerList = shopManagerService.selectAdminByShopIds(shopIds);
        Map<Long, String> managerMap = Maps.newConcurrentMap();
        if (!CollectionUtils.isEmpty(shopManagerList)) {
            for (ShopManager shopManager : shopManagerList) {
                managerMap.put(shopManager.getShopId(), shopManager.getMobile());
            }
        }
        //组装返回字段
        for (FinanceAccount financeAccount : financeAccountList) {
            FinanceAccountDTO financeAccountDTO = new FinanceAccountDTO();
            BeanUtils.copyProperties(financeAccount, financeAccountDTO);
            financeAccountDTO.setUcShopId(Integer.parseInt(shopMap.get(financeAccount.getShopId()).getUserGlobalId()));
            financeAccountDTO.setMobile(managerMap.get(financeAccount.getShopId()));
            if (StringUtil.isMobileNO(financeAccountDTO.getMobile())) {
                financeAccountDTO.setMobile(shopMap.get(financeAccount.getShopId()).getMobile());
            }
            financeAccountDTOs.add(financeAccountDTO);
        }

        return Result.wrapSuccessfulResult(financeAccountDTOs);
    }
}
