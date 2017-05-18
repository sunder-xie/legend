package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设置->功能配置->消息推送
 * Created by wushuai on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/msg-push")
public class ShopMsgPushController extends BaseController{

    @Autowired
    ShopConfigureService shopConfigureService;

    /**
     * 消息推送设置页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        model.addAttribute("subModuleUrl", "msg-push");
        Long shopId = UserUtils.getShopIdForSession(request);
        String wechatValue = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.SHOPMSGCONF.getCode(), "wechat_conf");
        String smsValue = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.SHOPMSGCONF.getCode(), "sms_conf");
        model.addAttribute("wechatConf", wechatValue);
        model.addAttribute("smsConf", smsValue);
        return "yqx/page/setting/function/msg-push";
    }

    /**
     * 保存消息推送设置
     *
     * @param confKey
     * @param confValue
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> save(@RequestParam(value = "confKey", required = true) final String confKey,
                                                              @RequestParam(value = "confValue", required = true) final String confValue) {
        return new ApiTemplate<Boolean>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(confKey,"confKey不能为空");
                Assert.notNull(confValue,"confValue不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setConfValue(confValue);
                shopConfigure.setShopId(userInfo.getShopId());
                shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.SHOPMSGCONF.getCode()));
                shopConfigure.setConfKey(confKey);
                shopConfigure.setCreator(userInfo.getUserId());
                shopConfigure.setModifier(userInfo.getUserId());
                Integer confType = ShopConfigureTypeEnum.SHOPMSGCONF.getCode();
                log.info("[功能配置]消息推送设置,门店id:{}，操作人id:{},confKey:{},confValue:{}", userInfo.getShopId(), userInfo.getUserId(), confKey, confValue);
                boolean result = shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), confType, confKey, new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
                return result;
            }
        }.execute();
    }
}
