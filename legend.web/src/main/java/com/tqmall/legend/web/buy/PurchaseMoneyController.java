package com.tqmall.legend.web.buy;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.WebUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.activity.PurchaseBannerConfigService;
import com.tqmall.legend.biz.finance.AvoidLoginService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.PurchaseBannerConfig;
import com.tqmall.legend.entity.finance.LoginStallVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.tqmallstall.domain.param.purchaseamount.RpcPurchaseAmountQueryParam;
import com.tqmall.tqmallstall.domain.result.purchaseamount.UserPurchaseAmountPiplineDetailDTO;
import com.tqmall.tqmallstall.service.purchaseamount.RpcPurchaseAmountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 采购金
 * create by wjc 2015-12-23
 */
@Controller
@Slf4j
@RequestMapping("/shop/yunxiu/purchase")
public class PurchaseMoneyController extends BaseController {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ShopService shopService;

    @Autowired
    private AvoidLoginService avoidLoginService;

    @Autowired
    private RpcPurchaseAmountService rpcPurchaseAmountService;

    @Autowired
    private PurchaseBannerConfigService purchaseBannerConfigService;

    @InitBinder
    public void dataBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(java.util.Date.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                if (getValue() != null) {
                    return sdf.format((java.util.Date) getValue());
                }
                return null;
            }

            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null) {
                    try {
                        setValue(sdf.parse(text));
                    } catch (ParseException e) {
                        log.error("日期解析错误", e);
                    }
                }

            }
        });
    }
    @ResponseBody
    @RequestMapping("list_flow")
    public PagingResult<UserPurchaseAmountPiplineDetailDTO>
        queryFlow(@RequestParam(value = "startTime", required = false)Date startTime,
                  @RequestParam(value = "endTime", required = false)Date endTime,
                  @RequestParam(value = "page", required = false, defaultValue = "1")int page) {
        if (endTime != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(endTime);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            endTime = cal.getTime();
        }

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = this.shopService.selectById(userInfo.getShopId());

        RpcPurchaseAmountQueryParam param = new RpcPurchaseAmountQueryParam();
        param.setShopId(Integer.parseInt(shop.getUserGlobalId()));
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setStart((page - 1) * 10);

        try {
            return rpcPurchaseAmountService.queryUserPurchaseAmountPiplineDetail(param);
        } catch (Exception e) {
            log.error("从电商获取采购金流水失败.", e);
        }

        return PagingResult.wrapErrorResult("", "获取采购金流水失败.");
    }

    @RequestMapping("/detail")
    public String detail(Model model){
        model.addAttribute("moduleUrl", "buy");
        model.addAttribute("buyTab", "purchase_detail");
        return "buy/purchase/detail";
    }

    /**
     * 云修精品和云修工服从云修免登陆到电商，type="jingping"为云修精品,type="work_clothes"为云修工服
     * @param response
     * @return
     */
    @RequestMapping(value = "avoid_login/{type}")
    public String avoidLogin2(HttpServletResponse response, @PathVariable("type") String type) {
        //获取门店联系的手机号码
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        if (log.isInfoEnabled()) {
            log.info("店铺[{}]免登录电商:{}", shopId, type);
        }
        //获取免登录信息
        if (shop != null) {
            LoginStallVo loginStallVo = new LoginStallVo();
            loginStallVo.setMobile(shop.getMobile());
            loginStallVo.setOptType(type);
            Long cityId = shop.getCity();//门店默认城市站
            cityId = avoidLoginService.getCityId(shopId, cityId);//获取实际登陆的城市站
            loginStallVo.setCityId(cityId);//设置登陆城市站
            if (cityId == null) {
                return null;
            }
            avoidLoginService.avoidLoginStall(response, shopId, loginStallVo);
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/getBannerList")
    public Result<List<PurchaseBannerConfig>> getBannerList(){
        //获取门店联系的手机号码
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        Long cityId = shop.getCity();//门店默认城市站
        if (cityId == null) {
            return null;
        }
        Integer shopType = 0;
        String banpenFlag = (String) request.getAttribute(Constants.BPSHARE);
        if("true".equals(banpenFlag)){
            shopType = 2;
        }else{
            shopType = 1;
        }
        List<PurchaseBannerConfig> purchaseBannerConfigs = purchaseBannerConfigService.queryBannerList(cityId,shopId,shopType);

        //指定跳转路径
        for(PurchaseBannerConfig pc : purchaseBannerConfigs){
            if(PurchaseBannerConfig.ACTIVITYTYPE_TQMALL.equals(pc.getActivityType())){
                StringBuilder sb = new StringBuilder();
                sb.append(WebUtils.getHostUrl(request));
                sb.append("/shop/yunxiu/purchase/avoid_login/");
                sb.append(pc.getOptType());
                pc.setCustomRedirectUrl(sb.toString());
            }
        }
        return Result.wrapSuccessfulResult(purchaseBannerConfigs);
    }

}
