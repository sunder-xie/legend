package com.tqmall.legend.server.account;

import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.account.DiscountSelectedCardParam;
import com.tqmall.legend.object.param.account.DiscountSelectedComboParam;
import com.tqmall.legend.object.param.account.DiscountSelectedParam;
import com.tqmall.legend.object.result.account.DiscountInfoDTO;
import com.tqmall.legend.server.BaseDubboTest;
import com.tqmall.legend.service.account.RpcAccountService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Created by xin on 2017/3/15.
 */
public class RpcAccountServiceTest extends BaseDubboTest {

    private static RpcAccountService rpcAccountService = null;

    @BeforeClass
    public static void beforeClass() throws Exception {
        rpcAccountService = getService(RpcAccountService.class);
    }

    @Test
    public void testGetDiscountInfoByOrderId() throws Exception {
        Long shopId = 1L;
        Long orderId = 46622L;
        DiscountSelectedParam param = new DiscountSelectedParam();
        param.setGuestMobile("13700896609");
        Result<DiscountInfoDTO> result = rpcAccountService.getDiscountInfoByOrderId(shopId, orderId, param);
        System.out.println(JSONUtil.object2Json(result));
    }

    @Test
    public void testGetCarWashDiscountInfo() throws Exception {
        Long shopId = 1L;
        String carLicense = "浙A66614";
        Long serviceId = 151312L;
        BigDecimal amount = BigDecimal.ONE;
        DiscountSelectedParam param = new DiscountSelectedParam();
        param.setGuestMobile("13233334444");
        DiscountSelectedCardParam selectedCardParam = new DiscountSelectedCardParam();
        selectedCardParam.setCardId(5469L);
        param.setSelectedCard(selectedCardParam);
        DiscountSelectedComboParam selectedComboParam = new DiscountSelectedComboParam();
        selectedComboParam.setComboServiceId(151312L);
        selectedComboParam.setUseCount(1);
        param.setSelectedComboList(Arrays.asList(selectedComboParam));
        Result<DiscountInfoDTO> result = rpcAccountService.getCarWashDiscountInfo(shopId, carLicense, serviceId, amount, param);
        System.out.println(JSONUtil.object2Json(result));
    }
}
