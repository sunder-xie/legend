package com.tqmall.legend.web.setting;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.config.ShopNetworkConfigService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import com.tqmall.legend.entity.config.ShopNetworkConfig;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.config.ManagerDeviceConfigStatusEnum;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by twg on 17/1/5.
 * 安全登录设置
 */
@Controller
@RequestMapping("shop/security-login")
public class ShopSecurityLoginController extends BaseController {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private ShopManagerDeviceConfigService shopManagerDeviceConfigService;
    @Autowired
    private ShopNetworkConfigService shopNetworkConfigService;

    /*安全登录级别设置页*/
    @RequestMapping("level-setting")
    public String index(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        if (isAdmin == null || isAdmin != 1) {
            return "common/403";
        }
        String value = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.SHOPSECURITYLEVEL.getCode(), "");
        model.addAttribute("level", value);
        return "yqx/page/setting/level-setting";
    }

    /**
     * 门店安全级别切换
     *
     * @param level
     * @return
     */
    @RequestMapping(value = "level-chang/{level}", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> changLevel(@PathVariable final Long level) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
                if (isAdmin == null || isAdmin != 1) {
                    throw new IllegalArgumentException("您无权操作此功能");
                }
            }

            @Override
            protected String process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                ShopConfigure shopConfigure = new ShopConfigure();
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.SHOPSECURITYLEVEL.getCode()));
                shopConfigure.setConfValue(level.toString());
                shopConfigure.setConfKey("security_level");
                if (shopConfigureService.saveOrUpdateShopConfigure(shopId, ShopConfigureTypeEnum.SHOPSECURITYLEVEL.getCode(), new DataShopConfigConverter<ShopConfigure>(), shopConfigure)) {
                    return "门店安全级别切换成功";
                }
                return "门店安全级别切换失败";
            }
        }.execute();
    }

    /*设备列表页*/
    @RequestMapping("device-list")
    public String deviceList() {
        Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        if (isAdmin == null || isAdmin != 1) {
            return "common/403";
        }
        return "yqx/page/setting/device-list";
    }

    @RequestMapping("device-search")
    @ResponseBody
    public Result<Page<ShopManagerDeviceConfig>> searchDevices(@RequestParam(value = "keyword", required = false) final String keyword, @RequestParam(value = "status", required = false) final Integer status, @PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<Page<ShopManagerDeviceConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
                if (isAdmin == null || isAdmin != 1) {
                    throw new IllegalArgumentException("您无权操作此功能");
                }
            }

            @Override
            protected Page<ShopManagerDeviceConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Map<String, Object> param = Maps.newHashMap();
                param.put("shopId", shopId);
                if (StringUtils.isNotBlank(keyword)) {
                    List<ShopManager> shopManagers = shopManagerService.findByNameOrMobile(shopId, keyword);
                    List<Long> managerIds = Lists.transform(shopManagers, new Function<ShopManager, Long>() {
                        @Override
                        public Long apply(ShopManager input) {
                            return input.getId();
                        }
                    });
                    if(!CollectionUtils.isEmpty(managerIds)){
                        param.put("managerIds", managerIds);
                    }else {
                        param.put("managerIds", new Long[]{0L});
                    }
                }
                if (status != null) {
                    param.put("authorizeStatus", status);
                }
                return shopManagerDeviceConfigService.findDevicesByPage(pageable, param);
            }
        }.execute();
    }

    /**
     * 设备授权操作：同意、拒绝、删除
     */
    @RequestMapping(value = "devices/{type}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> devicesAutho(@PathVariable final String type, @PathVariable final Long id) {
        return new ApiTemplate<String>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
                if (isAdmin == null || isAdmin != 1) {
                    throw new IllegalArgumentException("您无权操作此功能");
                }
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                if (StringUtils.equals(type, "agree") && shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_AGREE.getCode())) {
                    return "设备授权成功";
                }
                if (StringUtils.equals(type, "refuse") && shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_REFUSE.getCode())) {
                    return "设备授权成功";
                }
                if (StringUtils.equals(type, "delete") && shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_UNBUND.getCode())) {
                    return "设备解绑成功";
                }
                throw new BizException("设备授权操作失败");
            }
        }.execute();
    }

    /*环境控制设置页*/
    @RequestMapping("network-setting")
    public String setNetWork(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopNetworkConfig> shopNetworkConfigs = shopNetworkConfigService.getShopNetworkConfigs(shopId);
        model.addAttribute("networkConfig", shopNetworkConfigs);
        return "yqx/page/setting/network-setting";
    }


    @RequestMapping("get-device-status")
    @ResponseBody
    public Result getManagerDeviceConfigStatus() {
        List<BaseEnumBo> baseEnumBoList = Lists.newArrayList();
        for (ManagerDeviceConfigStatusEnum deviceConfigStatusEnum : ManagerDeviceConfigStatusEnum.values()) {
            BaseEnumBo baseEnumBo = new BaseEnumBo();
            baseEnumBo.setCode(deviceConfigStatusEnum.getCode());
            baseEnumBo.setName(deviceConfigStatusEnum.getName());
            baseEnumBoList.add(baseEnumBo);
        }
        return Result.wrapSuccessfulResult(baseEnumBoList);
    }


}
