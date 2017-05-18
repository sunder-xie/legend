package com.tqmall.legend.server.finance;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.finance.FinanceAccountDTO;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.finance.RpcFinanceAccountService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/9/17.
 */
public class RpcFinanceAccountServiceTest extends BaseCaseTest {
    @Autowired
    private RpcFinanceAccountService rpcFinanceAccountService;

    /**
     * 根据ucShopId获取门店的对账银行卡信息
     * 异常返回
     */
    @Test
    public void getShopFinanceAccountNoSourceTest() {
        Result<FinanceAccountDTO> financeAccountDTOResult = rpcFinanceAccountService.getShopFinanceAccount(null, 1);
        Assert.assertFalse(financeAccountDTOResult.isSuccess());
        Assert.assertEquals(financeAccountDTOResult.getMessage(), "来源不能为空");
    }

    /**
     * 根据ucShopId获取门店的对账银行卡信息
     * 异常返回
     */
    @Test
    public void getShopFinanceAccountNoUcShopIdTest() {
        Result<FinanceAccountDTO> financeAccountDTOResult = rpcFinanceAccountService.getShopFinanceAccount("legend", null);
        Assert.assertFalse(financeAccountDTOResult.isSuccess());
        Assert.assertEquals(financeAccountDTOResult.getMessage(), "ucShopId有误");
    }

    /**
     * 根据ucShopId获取门店的对账银行卡信息
     * 正常返回
     */
    @Test
    public void getShopFinanceAccountTest() {
        String querySql = "select * from legend_finance_account where is_deleted= 'N' and user_id = 0 limit 1";
        List<Map<String, Object>> financeAccountList = queryData(querySql);
        Assert.assertNotNull(financeAccountList);
        Map<String, Object> financeAccountMap = financeAccountList.get(0);
        String shopIdStr = financeAccountMap.get("shop_id").toString();
        Map<String, Object> shopMap = getShopInfoMapByShopId(Long.valueOf(shopIdStr));
        String userGlobalId = shopMap.get("user_global_id").toString();
        //userGlobalId必须大于0才执行此case
        Assert.assertFalse(userGlobalId.equals("0"));
        Integer ucShopId = Integer.valueOf(userGlobalId);
        Result<FinanceAccountDTO> financeAccountDTOResult = rpcFinanceAccountService.getShopFinanceAccount("legend", ucShopId);
        Assert.assertTrue(financeAccountDTOResult.isSuccess());
        FinanceAccountDTO financeAccountDTO = financeAccountDTOResult.getData();
        Assert.assertEquals(financeAccountDTO.getAccount(), financeAccountMap.get("account").toString());
        Assert.assertEquals(financeAccountDTO.getAccountBank(), financeAccountMap.get("account_bank").toString());
        Assert.assertEquals(financeAccountDTO.getAccountUser(), financeAccountMap.get("account_user").toString());
        Assert.assertEquals(financeAccountDTO.getBank(), financeAccountMap.get("bank").toString());
        Assert.assertEquals(financeAccountDTO.getBankCity(), financeAccountMap.get("bank_city").toString());
        Assert.assertEquals(financeAccountDTO.getBankCityId().toString(), financeAccountMap.get("bank_city_id").toString());
        Assert.assertEquals(financeAccountDTO.getBankProvince(), financeAccountMap.get("bank_province").toString());
        Assert.assertEquals(financeAccountDTO.getBankProvinceId().toString(), financeAccountMap.get("bank_province_id").toString());
        Assert.assertEquals(financeAccountDTO.getUcShopId(), ucShopId);
        //验证手机号
        String mobileSql = "select mobile from legend_shop_manager where is_deleted= 'N' and is_admin=1 and shop_id = " + shopIdStr
                + " union select mobile from legend_shop where is_deleted= 'N' and id = " + shopIdStr;
        List<Map<String, Object>> mobileList = queryData(mobileSql);
        Assert.assertNotNull(mobileList);
        Map<String, Object> mobileMap = mobileList.get(0);
        String mobile = mobileMap.get("mobile").toString();
        Assert.assertEquals(financeAccountDTO.getMobile(), mobile);
    }
}
