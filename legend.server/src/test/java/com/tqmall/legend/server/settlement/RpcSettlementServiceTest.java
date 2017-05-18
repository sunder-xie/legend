package com.tqmall.legend.server.settlement;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.settlement.GuestMobileCheckParam;
import com.tqmall.legend.object.param.settlement.SettlementSmsParam;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.settlement.RpcSettlementService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by zsy on 17/3/1.
 */
public class RpcSettlementServiceTest extends BaseCaseTest {
    private final String license = "浙A12345";
    private final String errorMobile = "12345678910";
    private final String mobile = "18367118772";
    private final String code = "123456";
    @Autowired
    private RpcSettlementService rpcSettlementService;

    /**
     * 获取门店id
     *
     * @return
     */
    private Long getShopId() {
        Map<String, Object> shopInfoMap = getShopInfoMap();
        Long shopId = (Long) shopInfoMap.get("id");
        return shopId;
    }

    /**
     * 发送短信
     * 异常返回：没有参数
     */
    @Test
    public void sendCodeNoParamTest() {
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(null);
        Assert.assertFalse(sendCodeResult.isSuccess());
        Assert.assertEquals(sendCodeResult.getMessage(), "参数为空");
    }

    /**
     * 发送短信
     * 异常返回：没有车牌
     */
    @Test
    public void sendCodeNoLicenseTest() {
        SettlementSmsParam settlementSmsParam = new SettlementSmsParam();
        settlementSmsParam.setMobile(mobile);
        settlementSmsParam.setShopId(getShopId());
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(settlementSmsParam);
        Assert.assertFalse(sendCodeResult.isSuccess());
        Assert.assertEquals(sendCodeResult.getMessage(), "车牌为空");
    }

    /**
     * 发送短信
     * 异常返回：没有门店id
     */
    @Test
    public void sendCodeNoShopIdTest() {
        SettlementSmsParam settlementSmsParam = new SettlementSmsParam();
        settlementSmsParam.setMobile(mobile);
        settlementSmsParam.setLicense(license);
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(settlementSmsParam);
        Assert.assertFalse(sendCodeResult.isSuccess());
        Assert.assertEquals(sendCodeResult.getMessage(), "shopId为空");
    }

    /**
     * 发送短信
     * 异常返回：没有手机号
     */
    @Test
    public void sendCodeNoMobileTest() {
        SettlementSmsParam settlementSmsParam = new SettlementSmsParam();
        settlementSmsParam.setShopId(getShopId());
        settlementSmsParam.setLicense(license);
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(settlementSmsParam);
        Assert.assertFalse(sendCodeResult.isSuccess());
        Assert.assertEquals(sendCodeResult.getMessage(), "手机号为空或格式有误");
    }

    /**
     * 发送短信
     * 异常返回：错误手机号
     */
    @Test
    public void sendCodeErrorMobileTest() {
        SettlementSmsParam settlementSmsParam = new SettlementSmsParam();
        settlementSmsParam.setMobile(errorMobile);
        settlementSmsParam.setShopId(getShopId());
        settlementSmsParam.setLicense(license);
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(settlementSmsParam);
        Assert.assertFalse(sendCodeResult.isSuccess());
        Assert.assertEquals(sendCodeResult.getMessage(), "手机号为空或格式有误");
    }

    /**
     * 发送短信
     * 正常返回
     */
    @Test
    public void sendCodeTest() {
        SettlementSmsParam settlementSmsParam = new SettlementSmsParam();
        settlementSmsParam.setMobile(mobile);
        settlementSmsParam.setShopId(getShopId());
        settlementSmsParam.setLicense(license);
        Result<Boolean> sendCodeResult = rpcSettlementService.sendCode(settlementSmsParam);
        Assert.assertTrue(sendCodeResult.isSuccess());
    }


    /**
     * 校验短信
     * 异常返回：没有参数
     */
    @Test
    public void checkCodeNoParamTest() {
        Result<Boolean> checkCodeResult = rpcSettlementService.checkCode(null);
        Assert.assertFalse(checkCodeResult.isSuccess());
        Assert.assertEquals(checkCodeResult.getMessage(), "参数为空");
    }

    /**
     * 校验短信
     * 异常返回：没有验证码
     */
    @Test
    public void checkCodeNoCodeTest() {
        GuestMobileCheckParam guestMobileCheckParam = new GuestMobileCheckParam();
        guestMobileCheckParam.setGuestMobile(mobile);
        guestMobileCheckParam.setShopId(getShopId());
        Result<Boolean> checkCodeResult = rpcSettlementService.checkCode(guestMobileCheckParam);
        Assert.assertFalse(checkCodeResult.isSuccess());
        Assert.assertEquals(checkCodeResult.getMessage(), "验证码为空");
    }

    /**
     * 校验短信
     * 异常返回：没有门店id
     */
    @Test
    public void checkCodeNoShopIdTest() {
        GuestMobileCheckParam guestMobileCheckParam = new GuestMobileCheckParam();
        guestMobileCheckParam.setGuestMobile(mobile);
        guestMobileCheckParam.setCode(code);
        Result<Boolean> checkCodeResult = rpcSettlementService.checkCode(guestMobileCheckParam);
        Assert.assertFalse(checkCodeResult.isSuccess());
        Assert.assertEquals(checkCodeResult.getMessage(), "shopId为空");
    }

    /**
     * 校验短信
     * 异常返回：没有手机号
     */
    @Test
    public void checkCodeNoMobileTest() {
        GuestMobileCheckParam guestMobileCheckParam = new GuestMobileCheckParam();
        guestMobileCheckParam.setShopId(getShopId());
        guestMobileCheckParam.setCode(code);
        Result<Boolean> checkCodeResult = rpcSettlementService.checkCode(guestMobileCheckParam);
        Assert.assertFalse(checkCodeResult.isSuccess());
        Assert.assertEquals(checkCodeResult.getMessage(), "手机号为空或格式有误");
    }

    /**
     * 校验短信
     * 异常返回：错误手机号
     */
    @Test
    public void checkCodeErrorMobileTest() {
        GuestMobileCheckParam guestMobileCheckParam = new GuestMobileCheckParam();
        guestMobileCheckParam.setGuestMobile(errorMobile);
        guestMobileCheckParam.setShopId(getShopId());
        guestMobileCheckParam.setCode(code);
        Result<Boolean> checkCodeResult = rpcSettlementService.checkCode(guestMobileCheckParam);
        Assert.assertFalse(checkCodeResult.isSuccess());
        Assert.assertEquals(checkCodeResult.getMessage(), "手机号为空或格式有误");
    }
}

