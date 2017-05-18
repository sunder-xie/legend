package com.tqmall.legend.web.wechat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ServiceGoodsSuiteService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.facade.service.vo.AppServiceVo;
import com.tqmall.legend.facade.shop.ShopFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店微信管理-发布服务
 * Created by wushuai on 16/10/8.
 */
@RequestMapping("/shop/wechat/appservice")
@Controller
@Slf4j
public class AppServiceController extends BaseController {

    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private ShopServiceCateService shopServiceCateService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private ServiceGoodsSuiteService serviceGoodsSuiteService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private ShopFacade shopFacade;
    @Autowired
    ShopConfigureService shopConfigureService;

    /**
     * 发布车主服务列表页
     *
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-appservice");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        boolean hasSettingFunc = shopFunFacade.checkFuncAnd(userInfo,"设置首页");
        model.addAttribute("hasSettingFunc", hasSettingFunc);
        List<ServiceCateVo> serviceCateVoList = shopServiceCateService.selectFirstCate();
        if (!CollectionUtils.isEmpty(serviceCateVoList)) {
            Collections.sort(serviceCateVoList, new Comparator<ServiceCateVo>() {
                @Override
                public int compare(ServiceCateVo o1, ServiceCateVo o2) {
                    int i1 = o1.getCateSort() == null ? 0 : o1.getCateSort();
                    int i2 = o2.getCateSort() == null ? 0 : o2.getCateSort();
                    return i2 - i1;
                }
            });
        }
        model.addAttribute("serviceCateVoList", serviceCateVoList);
        Boolean isOpenPay=shopFacade.getShopApplyStatusIsSuccess(userInfo.getShopId());
        String payStatus = shopConfigureService.getShopConfigure(userInfo.getShopId(), ShopConfigureTypeEnum.PAYMENT.getCode(), "wechat");
        model.addAttribute("isOpenPay", isOpenPay);
        model.addAttribute("payStatus", payStatus);
        return "yqx/page/wechat/appservice/appservice-list";
    }

    /**
     * 车主服务详情编辑页
     *
     * @param model
     * @return
     */
    @RequestMapping("edit")
    public String edit(Model model, @RequestParam(value = "serviceId", required = false) final Long serviceId) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
        model.addAttribute("subModule", "wechat-appservice");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        boolean hasSettingFunc = shopFunFacade.checkFuncAnd(userInfo,"设置首页");
        model.addAttribute("hasSettingFunc", hasSettingFunc);
        if (serviceId != null) {
            Long shopId = userInfo.getShopId();
            ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectByIdAndShopId(serviceId, shopId);
            Integer appCateId = shopServiceInfo.getAppCateId();
            if (appCateId != null) {
                Map<Long, ShopServiceCate> shopServiceCateMap = shopServiceCateService.dealCateInfo();
                ShopServiceCate shopServiceCate = shopServiceCateMap.get(appCateId.longValue());
                if (shopServiceCate != null) {
                    if (StringUtils.isBlank(shopServiceInfo.getImgUrl())) {
                        shopServiceInfo.setImgUrl(shopServiceCate.getDefaultImgUrl());
                    }
                    if (shopServiceCate.getName() != null && shopServiceCate.getFirstCateName() != null) {
                        shopServiceInfo.setAppCateName(shopServiceCate.getFirstCateName() + "-" + shopServiceCate.getName());
                    }
                }
            }
            //服务套餐价格处理
            if (shopServiceInfo.getSuiteNum() != null && shopServiceInfo.getSuiteNum() > 0) {
                ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(shopServiceInfo.getId());
                if (serviceGoodsSuite != null && serviceGoodsSuite.getSuitePrice() != null) {
                    shopServiceInfo.setServicePrice(serviceGoodsSuite.getSuitePrice());
                }
            }
            model.addAttribute("shopServiceInfo", shopServiceInfo);
            String thirdServiceInfo = shopServiceInfo.getThirdServiceInfo();
            if (StringUtils.isNotBlank(thirdServiceInfo)) {
                List<HashMap> thirdServiceInfoList = new Gson().fromJson(thirdServiceInfo, new TypeToken<List<HashMap>>() {
                }.getType());
                model.addAttribute("thirdServiceInfoList", thirdServiceInfoList);
            }
        }
        Boolean isOpenPay=shopFacade.getShopApplyStatusIsSuccess(userInfo.getShopId());
        String payStatus = shopConfigureService.getShopConfigure(userInfo.getShopId(), ShopConfigureTypeEnum.PAYMENT.getCode(), "wechat");
        model.addAttribute("isOpenPay", isOpenPay);
        model.addAttribute("payStatus", payStatus);
        return "yqx/page/wechat/appservice/appservice-edit";
    }

    /**
     * 查询已发布的车主服务(不分页)
     *
     * @param parentAppCateId
     * @return
     */
    @RequestMapping(value = "/op/get-published-appService-list")
    @ResponseBody
    public Result<List<AppServiceVo>> getPublishedAppServiceList(@RequestParam(value = "parentAppCateId", required = false) final Long parentAppCateId,
                                                                 @RequestParam(value = "isRecommend", required = false) final Integer isRecommend) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<List<AppServiceVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
            }

            @Override
            protected List<AppServiceVo> process() throws BizException {
                List<AppServiceVo> appServiceVoList = shopServiceInfoFacade.getPublishedAppService(shopId, parentAppCateId, isRecommend);
                logger.info("[微信公众号-服务管理]查询已发布的车主服务(不分页),shopId:{},parentAppCateId:{},isRecommend:{}", shopId, parentAppCateId, isRecommend);
                return appServiceVoList;
            }
        }.execute();
    }

    /**
     * 查询待发布的车主服务(分页)
     *
     * @param parentAppCateId
     * @return
     */
    @RequestMapping(value = "/op/get-Prepublished-appService-page")
    @ResponseBody
    public Result<Page<AppServiceVo>> getPrepublishAppServicePage(@PageableDefault(page = 1, value = 10) final Pageable pageable,
                                                                  @RequestParam(value = "parentAppCateId", required = false) final Long parentAppCateId,
                                                                  @RequestParam(value = "isRecommend", required = false) final Integer isRecommend) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Page<AppServiceVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
            }

            @Override
            protected Page<AppServiceVo> process() throws BizException {
                int limit = pageable.getPageSize();
                long offset = (pageable.getPageNumber() - 1) * limit;//从0开始
                Page<AppServiceVo> page = shopServiceInfoFacade.getPrepublishAppServicePage(shopId, parentAppCateId, isRecommend, limit, offset);
                logger.info("[微信公众号-服务管理]查询待发布的车主服务(分页),shopId:{}, parentAppCateId:{}, isRecommend:{}, limit:{}, offset:{}", shopId, parentAppCateId, isRecommend, limit, offset);
                return page;
            }
        }.execute();
    }

    /**
     * 取消将服务发布到车主端
     *
     * @return
     */
    @RequestMapping(value = "/op/cancel-publish-appService", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> cancelPublishAppService(@RequestParam(value = "serviceId", required = true) final Long serviceId) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(userInfo, "操作员信息不能为空");
                Assert.notNull(userInfo.getShopId(), "shopId不能为空");
                Assert.notNull(serviceId, "serviceId不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                boolean result = shopServiceInfoFacade.cancelPublishAppService(userInfo, serviceId);
                logger.info("[微信公众号-服务管理]取消将服务发布到车主端,shopId:{},userId:{},serviceId:{}", userInfo.getShopId(), userInfo.getUserId(), serviceId);
                return result;
            }
        }.execute();
    }

    /**
     * 保存服务列表
     *
     * @return
     */
    @RequestMapping(value = "/op/save-appServiceList", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> saveAppServiceList(@RequestBody final List<AppServiceVo> appServiceVoList) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(userInfo, "操作员信息不能为空");
                Assert.notNull(userInfo.getShopId(), "shopId不能为空");
                Assert.notNull(appServiceVoList, "要保存的服务列表不能为空");
            }

            @Override
            protected String process() throws BizException {
                String preViewUrl = shopServiceInfoFacade.saveAppServiceListAndGetViewUrl(appServiceVoList, userInfo);
                logger.info("[微信公众号-服务管理]保存服务列表,shopId:{},userId:{},appServiceVoList:{}", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(appServiceVoList));
                return preViewUrl;
            }
        }.execute();
    }


    /**
     * 查询可用于发布发布车主服务的服务
     *
     * @return
     */
    @RequestMapping(value = "/op/get-can-add-appService-list")
    @ResponseBody
    public Result<List<ShopServiceInfo>> getCanAddAppServiceList(@RequestParam(value = "serviceName", required = false) final String serviceName,
                                                                 @RequestParam(value = "isSuit", required = false) final Integer isSuit,
                                                                 @RequestParam(value = "limit", required = false) final Integer limit) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<List<ShopServiceInfo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "shopId不能为空");
            }

            @Override
            protected List<ShopServiceInfo> process() throws BizException {
                Map<String, Object> param = new HashMap<>();
                param.put("shopId", shopId);
                if (isSuit != null) {
                    if (isSuit == 1) {
                        param.put("suiteNums", new Integer[]{1, 2});
                    } else {
                        param.put("suiteNum", 0);
                    }
                }
                int qryLimit = limit == null ? 50 : limit;
                param.put("serviceNameLike", serviceName);
                param.put("flagIsEmpty", 1);
                param.put("offset", 0);
                param.put("limit", qryLimit);
                List<ShopServiceInfo> shopServiceInfoList = shopServiceInfoService.selectAllStatus(param);
                shopServiceInfoFacade.setSuitServicePrice(shopServiceInfoList);
                logger.info("[微信公众号-服务管理]查询可用于发布发布车主服务的服务,param:{}", LogUtils.objectToString(param));
                return shopServiceInfoList;
            }
        }.execute();
    }
}
