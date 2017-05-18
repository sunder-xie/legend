package com.tqmall.legend.web.marketing.ng;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.Condition;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.biz.marketing.MarketingSmsRechargeTplService;
import com.tqmall.legend.biz.marketing.ng.MarketingCenterService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import com.tqmall.legend.entity.marketing.MarketingSmsRechargeTpl;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 营销中心controller
 * Created by wjc on 3/8/16.
 */
@Slf4j
@Controller
@RequestMapping("/marketing/ng/center")
public class MarketingCenterController extends BaseController {
    @Autowired
    private MarketingCenterService marketingCenterService;
    @Autowired
    private MarketingSmsRechargeTplService marketingSmsRechargeTplService;
    @Autowired
    private MarketingShopRelService marketingShopRelService;
    @Autowired
    private MemberCardInfoService memberCardInfoService;

    private void _init(Model model) {
        model.addAttribute("moduleUrl", "marketing");
    }

    @RequestMapping
    public String index(Model model) {
        this._init(model);
        model.addAttribute("subModule", "center");
        return "marketing/ng/center/index";
    }

    @HttpRequestLog(params = {@Param(name = "size", defaultValue = "10", aliasName = "page_size")},
                    conditions = {@Condition(name = "search_minAmount"), @Condition(name = "search_maxAmount"),
                            @Condition(name = "search_minAverage"), @Condition(name = "search_maxAverage"),
                            @Condition(name = "search_number"),
                            @Condition(name = "search_day", aliasName = "search_month"),
                            @Condition(name = "search_carLevelTag"),
                            @Condition(name = "search_minMileage"), @Condition(name = "search_maxMileage"),
                            @Condition(name = "search_memberLevelId"),
                            @Condition(name = "search_carLicense"),
                            @Condition(name = "search_carType"),
                            @Condition(name = "search_mobile"),
                            @Condition(name = "search_tag"),
                            @Condition(name = "search_customerTag"),
                            @Condition(name = "search_sTime"), @Condition(name = "search_eTime")
    })
    @RequestMapping("accurate/list")
    @ResponseBody
    public Object accurateList( @PageableDefault(page = 1, value = 10, sort = "totalNumber", direction = Sort.Direction.DESC) Pageable pageable){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
        params.put("shopId", shopId);
        HtmlParamDecoder.decode(params, "daySign");
        HtmlParamDecoder.decode(params, "numberSign");

        return Result.wrapSuccessfulResult(marketingCenterService.selectAccurate(params,pageable));
    }


    @RequestMapping("accurate")
    public String accurate(Model model, HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        if (log.isInfoEnabled()) {
            log.info("[精准营销跳转上级url]:{}", request.getHeader("Referer"));
        }
        this._init(model);
        model.addAttribute("subModule", "center-accurate");
        List<MemberCardInfo> memberCardInfos = memberCardInfoService.findAllByShopId(shopId, 0);
        /**
         * 组装会员等级字符串
         */
        StringBuilder memberLevelText = new StringBuilder("[");
        StringBuilder memberLevelId = new StringBuilder("[");
        for(MemberCardInfo shopMemberLevel : memberCardInfos){
            memberLevelText.append('"'+shopMemberLevel.getTypeName()+'"'+",");
            memberLevelId.append('"'+shopMemberLevel.getId().toString() +'"'+",");
        }
        memberLevelText.deleteCharAt(memberLevelText.length()-1).append("]");
        memberLevelId.deleteCharAt(memberLevelId.length() - 1).append("]");
        model.addAttribute("memberLevelText",memberLevelText);
        model.addAttribute("memberLevelId",memberLevelId);
        return "marketing/ng/center/accurate";
    }

    @RequestMapping("sms")
    public String sms(Model model) {
        this._init(model);
        UserInfo userInfo = UserUtils.getUserInfo(request);

        if (userInfo.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)){
            model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
            model.addAttribute("subModule", "wechat-coupon");
        }else{
            model.addAttribute("subModule", "center-sms");

            model.addAttribute("moduleUrl", "marketing");
        }


        Long shopId = UserUtils.getShopIdForSession(request);
        Map shopMap = new HashMap();
        shopMap.put("shopId", shopId);
        MarketingShopRel marketingShopRel = marketingShopRelService.selectOne(shopMap);
        model.addAttribute("marketingShopRel", marketingShopRel);

        List<MarketingSmsRechargeTpl> marketingSmsRechargeTplList = marketingSmsRechargeTplService.select(null);
        model.addAttribute("marketingSmsRechargeTplList", marketingSmsRechargeTplList);
        return "marketing/ng/center/sms";
    }

    @RequestMapping("sms/pay_way")
    public String paySms(@RequestParam("rechargeId")Long rechargeId, Model model){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (userInfo.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)){
            model.addAttribute("moduleUrl", ModuleUrlEnum.WECHAT.getModuleUrl());
            model.addAttribute("subModule", "wechat-coupon");
        }else{
            model.addAttribute("subModule", "center-sms");

            model.addAttribute("moduleUrl", "marketing");
        }

        MarketingSmsRechargeTpl chargeTpl = this.marketingSmsRechargeTplService.selectById(rechargeId);
        model.addAttribute("chargeTpl", chargeTpl);
        return "marketing/ng/center/sms_pay_way";
    }
}
