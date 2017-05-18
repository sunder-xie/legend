package com.tqmall.legend.facade.account;

import com.tqmall.legend.facade.account.vo.CustomerDiscountInfoForApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by xin on 2017/3/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-biz-context.xml")
public class AccountFacadeServiceTest {

    @Autowired
    private AccountFacadeService accountFacadeService;

    @Test
    public void testGetCustomerDiscountInfoForApp() throws Exception {
//        Long shopId = 1L;
//        Long customerCarId = 46389L;
//        List<CustomerDiscountInfoForApp> customerDiscountInfoForApp = accountFacadeService.getCustomerDiscountInfoForApp(shopId, customerCarId);
//        Assert.assertNotNull(customerDiscountInfoForApp);
    }
}
