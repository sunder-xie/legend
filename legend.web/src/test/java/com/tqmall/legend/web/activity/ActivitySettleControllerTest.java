package com.tqmall.legend.web.activity;

import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lilige on 16/3/1.
 */

public class ActivitySettleControllerTest extends TestCase {
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private IInsuranceBillService insuranceBillService;

    /*@Test
    public void test_验证银行卡绑定信息() throws Exception {
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("shopId" , 1);
        searchMap.put("userId", 0);
        List<FinanceAccount> financeAccounts = financeAccountService.getUserFinanceAccount(searchMap);
        if (CollectionUtils.isEmpty(financeAccounts)){
            System.out.println("没绑定");
        }
    }*/

    public void testCheckSettle1() throws Exception {
    }

    public void testSettleList() throws Exception {

    }
}