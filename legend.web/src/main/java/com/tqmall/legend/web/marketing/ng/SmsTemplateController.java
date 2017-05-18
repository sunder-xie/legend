package com.tqmall.legend.web.marketing.ng;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.marketing.MarketingShopRelService;
import com.tqmall.legend.biz.marketing.ng.SmsTemplateService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.MarketingShopRel;
import com.tqmall.legend.entity.marketing.ng.SmsTemplate;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 短信模板controller
 * Created by wjc on 3/2/16.
 */
@Slf4j
@Controller
@RequestMapping("/marketing/ng/sms/template")
public class SmsTemplateController extends BaseController{
    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private MarketingShopRelService marketingShopRelService;

    @RequestMapping
    public String index(Model model){
        model.addAttribute("moduleUrl","marketing");
        model.addAttribute("subModule", "center-sms");
        Long shopId = UserUtils.getShopIdForSession(request);
        List<SmsTemplate> templateList = smsTemplateService.getTemplateList(shopId);
        Iterator<SmsTemplate> iter = templateList.iterator();
        int i = 0;
        while(iter.hasNext()){
            SmsTemplate template = iter.next();
            if(template.getTemplateType() != 0) {
                i++;
                model.addAttribute("template" + i, template);
                iter.remove();
            }
        }
        model.addAttribute("otherList",templateList);
        return "marketing/ng/sms_template";
    }


    @RequestMapping("update")
    @ResponseBody
    public Result update(@RequestBody List<SmsTemplate> templateList){
        Long shopId = UserUtils.getShopIdForSession(request);
        smsTemplateService.insert(templateList, shopId);
        return Result.wrapSuccessfulResult("保存成功");
    }

    @RequestMapping("list")
    @ResponseBody
    public Result list(){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<SmsTemplate> list = smsTemplateService.getTemplateList(shopId);
        return Result.wrapSuccessfulResult(list);
    }

    @RequestMapping("get")
    @ResponseBody
    public com.tqmall.core.common.entity.Result getTemplate(final Integer type) {
        return new ApiTemplate<SmsTemplate>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(type);
            }

            @Override
            protected SmsTemplate process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return smsTemplateService.getTemplate(shopId, type);
            }
        }.execute();
    }

    @RequestMapping("remain")
    @ResponseBody
    public Result remain(){
        Long shopId = UserUtils.getShopIdForSession(request);
        Map shopMap = new HashMap();
        shopMap.put("shopId", shopId);
        MarketingShopRel marketingShopRel = marketingShopRelService.selectOne(shopMap);
        if(marketingShopRel == null){
            log.info("该门店未进行过充值.shopId:{}", shopId);
            return Result.wrapSuccessfulResult(0);
        }
        return Result.wrapSuccessfulResult(marketingShopRel.getSmsNum());
    }
}
