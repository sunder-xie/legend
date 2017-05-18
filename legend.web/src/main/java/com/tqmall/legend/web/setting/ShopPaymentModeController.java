package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopPaymentService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 设置->功能配置->支付方式开关
 * Created by wushuai on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/payment-mode")
public class ShopPaymentModeController extends BaseController {

    @Autowired
    ShopConfigureService shopConfigureService;
    @Autowired
    private ShopPaymentService shopPaymentService;

    /**
     * 支付方式设置
     *
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        model.addAttribute("subModuleUrl", "payment-mode");
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopConfigure> paymentConfigs = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.PAYMENT.getCode(), new DataShopConfigConverter<List<ShopConfigure>>());
        model.addAttribute("paymentConfigs", paymentConfigs);
        return "yqx/page/setting/function/payment-mode";
    }

    /**
     * 判断门店是否开通支付方式
     *
     * @return
     */
    @RequestMapping("has-open-payment")
    @ResponseBody
    public Result<Boolean> hasOpenPayment(@RequestParam(value = "applyType", required = true) final Integer applyType) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(applyType, "支付类型不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                boolean hasApplyPayment = shopPaymentService.hasApplyPayment(shopId, applyType);
                return hasApplyPayment;
            }
        }.execute();
    }

    /**
     * 门店支付方式开关设置
     *
     * @param confKey   配置类型
     * @param confValue 配置的内容
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> save(@RequestParam(value = "confKey", required = true) final String confKey,
                               @RequestParam(value = "confValue", required = true) final String confValue) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (StringUtils.isBlank(confKey)) {
                    throw new IllegalArgumentException("confKey不能为空");
                }
                if (StringUtils.isBlank(confValue)) {
                    throw new IllegalArgumentException("confValue不能为空");
                }
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setConfKey(confKey);
                shopConfigure.setConfValue(confValue);
                shopConfigure.setShopId(userInfo.getShopId());
                shopConfigure.setCreator(userInfo.getUserId());
                shopConfigure.setModifier(userInfo.getUserId());
                shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.PAYMENT.getCode()));
                log.info("[功能配置]支付方式开关设置,门店id:{}，操作人id:{},confKey:{},confValue:{}", userInfo.getShopId(), userInfo.getUserId(), confKey, confValue);
                boolean r = shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), ShopConfigureTypeEnum.PAYMENT.getCode(), new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
                if (r) {
                    return "支付方式设置成功";
                }
                return "支付方式设置失败";
            }
        }.execute();
    }
}
