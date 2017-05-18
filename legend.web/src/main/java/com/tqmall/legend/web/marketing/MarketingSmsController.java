package com.tqmall.legend.web.marketing;

import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.marketing.MarketingSmsLogServie;
import com.tqmall.legend.biz.marketing.MarketingSmsRechargeTplService;
import com.tqmall.legend.biz.marketing.MarketingSmsService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.MarketingSms;
import com.tqmall.legend.entity.marketing.MarketingSmsLog;
import com.tqmall.legend.entity.shop.SmsPayLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by lixiao on 15/6/8.
 */
@Controller
@RequestMapping("shop/marketing/sms")
public class MarketingSmsController extends BaseController{

    @Autowired
    private MarketingSmsService marketingSmsService;
    @Autowired
    private MarketingSmsLogServie marketingSmsLogServie;
    @Autowired
    private MarketingSmsRechargeTplService marketingSmsRechargeTplService;

    @RequestMapping("list")
    @ResponseBody
    public Object list(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<MarketingSms> page = null;
        page = (DefaultPage<MarketingSms>) marketingSmsService.getPage(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    @HttpRequestLog
    @RequestMapping("list/recharge")
    @ResponseBody
    public Object listRecharge(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                               HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        searchParams.put("shopId",UserUtils.getShopIdForSession(request));
        searchParams.put("shopId",UserUtils.getShopIdForSession(request));
        Page<SmsPayLog> page = marketingSmsRechargeTplService.getPage(pageable, searchParams);
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("log/ng")
    public String log(Model model , HttpServletRequest request){
        model.addAttribute("moduleUrl","marketing");
        return "shop-marketing-sms_log";
    }

    @HttpRequestLog
    @RequestMapping("log_list")
    @ResponseBody
    public Object logList(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        DefaultPage<MarketingSmsLog> page = null;
        page = (DefaultPage<MarketingSmsLog>)marketingSmsLogServie.getPage(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }

    @HttpRequestLog
    @RequestMapping("log_detail")
    @ResponseBody
    public com.tqmall.core.common.entity.Result getSmsLogDetail(@RequestParam("smsLogId") final Long smsLogId, final Pageable pageable) {
        return new ApiTemplate<Page<MarketingSms>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(smsLogId, "smsLogId 不能为空");
            }

            @Override
            protected Page<MarketingSms> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Map param = Maps.newHashMap();
                param.put("shopId", shopId);
                param.put("smsLogId", smsLogId);
                return marketingSmsService.getPage(pageable,param);

            }
        }.execute();
    }

}
