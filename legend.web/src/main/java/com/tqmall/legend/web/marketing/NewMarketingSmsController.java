package com.tqmall.legend.web.marketing;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.marketing.ng.CustomerLevelAnalysisService;
import com.tqmall.legend.biz.marketing.ng.CustomerLostAnalysisService;
import com.tqmall.legend.biz.marketing.ng.CustomerTypeAnalysisService;
import com.tqmall.legend.biz.marketing.ng.MarketingCenterService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.facade.sms.SmsSendFacade;
import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import com.tqmall.legend.facade.sms.newsms.SendPositionEnum;
import com.tqmall.legend.facade.sms.newsms.SmsCenter;
import com.tqmall.legend.facade.sms.newsms.param.PreSendParam;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;
import com.tqmall.legend.pojo.shopnote.ShopNoteInfoVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.marketing.ng.HtmlParamDecoder;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 15/6/10.
 */
@Controller
@RequestMapping("shop/marketing/sms/new")
@Slf4j
public class NewMarketingSmsController extends BaseController{

    private static final int PAGE_SIZE = 500;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private SmsSendFacade smsSendFacade;

    @Autowired
    private SmsCenter smsCenter;

    @Autowired
    private CustomerTypeAnalysisService typeAnalysisService;

    @Autowired
    private CustomerLostAnalysisService lostAnalysisService;

    @Autowired
    private CustomerLevelAnalysisService levelAnalysisService;

    @Autowired
    private MarketingCenterService marketingCenterService;

    @Autowired
    private ShopNoteInfoService noteInfoService;


    /**
     * 模板内容预处理
     * @param template
     * @return
     */
    @RequestMapping("template_process")
    @ResponseBody
    public Result templatePreProcess(@RequestParam("template") final String template) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected String process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return smsCenter.templatePreProcess(shopId, template);
            }
        }.execute();
    }

    /**
     * 计算所需短信条数,用于发送给指定客户的情况
     * @param template
     * @param caIds
     * @return
     */
    @RequestMapping("calculate_number")
    @ResponseBody
    public Result<MarketingSmsTempBO> groupPreSend(@RequestParam("template") final String template,
                                                   @RequestParam("carIds") final List<Long> caIds,
                                                   @RequestParam("position") final Integer position) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
                Assert.notEmpty(caIds, "请选择客户");
                Assert.notNull(position, "发送位置不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                PreSendParam preSendParam = new PreSendParam(template, shopId, caIds, position);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    /**
     * 确认发送
     * @param key
     * @return
     */
    @HttpRequestLog
    @RequestMapping("send")
    @ResponseBody
    public Result<Integer> send(@RequestParam("key") final String key) {
        return new ApiTemplate<Integer>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(key, "key 不能为空");
            }

            @Override
            protected Integer process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                SendParam sendParam = new SendParam(userInfo.getShopId(), userInfo.getUserId(), userInfo.getName(), key);
                return smsCenter.send(sendParam);
            }
        }.execute();
    }


    /**
     * 确认发送--提醒中心
     * @param key
     * @param noteType
     * @return
     */
    @HttpRequestLog
    @RequestMapping("send_with_note")
    @ResponseBody
    public Result sendWithNote(@RequestParam("key") final String key, @RequestParam("noteType") final Integer noteType){
        return new ApiTemplate<Integer>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(key, "key 不能为空");
                Assert.notNull(noteType, "提醒类型不能为空");
            }

            @Override
            protected Integer process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                String name = userInfo.getName();
                SendParam sendParam = new SendParam(shopId, userInfo.getUserId(), name, key);
                return smsSendFacade.sendForNote(sendParam, noteType);
            }
        }.execute();
    }


    @RequestMapping("wechat_customer_list")
    @ResponseBody
    public Result getWeiChatCustomerInfo(@PageableDefault(page = 1, value = 10, sort = "gmt_create", direction = Sort.Direction.DESC) Pageable pageable,HttpServletRequest request){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        Page<CustomerCar> customerPage = customerCarService.getCustomerPageByParam(pageable,param);
        return Result.wrapSuccessfulResult(customerPage);
    }



    @RequestMapping("get_has_customer_num")
    @ResponseBody
    public Result getCustomerHasMobileNum(HttpServletRequest request){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Integer num = customerCarService.getCustomerHasMobileNum(shopId);
        return Result.wrapSuccessfulResult(num);
    }


    @RequestMapping("calculate_number/type/new")
    @ResponseBody
    public Result getNumberForNewCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return typeAnalysisService.getNewCustomer(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_TYPE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/type/old")
    @ResponseBody
    public Result getNumberForOldCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return typeAnalysisService.getOldCustomer(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_TYPE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/type/active")
    @ResponseBody
    public Result getNumberForActiveCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return typeAnalysisService.getActiveCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_TYPE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/type/sleep")
    @ResponseBody
    public Result getNumberForSleepCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return typeAnalysisService.getSleepCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_TYPE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/type/lost")
    @ResponseBody
    public Result getNumberForLostCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return typeAnalysisService.getLostCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_TYPE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/lost/low")
    @ResponseBody
    public Result getNumberForLowLostCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                long sTime = System.currentTimeMillis();
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return lostAnalysisService.getLostLowCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });
                if (log.isInfoEnabled()) {
                    log.info("query car cost {} ms", System.currentTimeMillis() - sTime);
                }
                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LOST.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/lost/high")
    @ResponseBody
    public Result getNumberForHighLostCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return lostAnalysisService.getLostHighCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LOST.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/lost/middle")
    @ResponseBody
    public Result getNumberForMiddleLostCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return lostAnalysisService.getLostMiddleCustomer(shopId, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LOST.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/level/high")
    @ResponseBody
    public Result getNumberForHighLevelCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                params.put("carLevelTag", 3);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return levelAnalysisService.getCustomerWithTime(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LEVEL.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/level/middle")
    @ResponseBody
    public Result getNumberForMiddleLevelCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                params.put("carLevelTag", 2);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return levelAnalysisService.getCustomerWithTime(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LEVEL.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/level/low")
    @ResponseBody
    public Result getNumberForLowLevelCustomer(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                params.put("carLevelTag", 1);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return levelAnalysisService.getCustomerWithTime(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_ANALYSIS_LEVEL.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/marketing_accurate")
    @ResponseBody
    public Result getNumberForMarketingAccurate(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                HtmlParamDecoder.decode(params, "daySign");
                HtmlParamDecoder.decode(params, "numberSign");
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerInfo>() {
                    @Override
                    public Page<CustomerInfo> getPagedCustomer(int pageIndex) {
                        return marketingCenterService.selectAccurate(params, new PageRequest(pageIndex, PAGE_SIZE));
                    }

                    @Override
                    public Long getCarId(CustomerInfo customerInfo) {
                        return customerInfo.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.MARKETING_ACCURATE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/wechat")
    @ResponseBody
    public Result getNumberForWechat(@RequestParam("template") final String template) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                final Map<String,Object> params = ServletUtils.getParametersMapStartWith(request);
                params.put("shopId", shopId);
                List<Long> carIds = getCarIds(new CarIdCallBack<CustomerCar>() {
                    @Override
                    public Page<CustomerCar> getPagedCustomer(int pageIndex) {
                        return customerCarService.getCustomerPageByParam(new PageRequest(pageIndex, PAGE_SIZE), params);
                    }

                    @Override
                    public Long getCarId(CustomerCar customerCar) {
                        return customerCar.getId();
                    }
                });

                int code = SendPositionEnum.WECHAT_COUPON_SMS.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }

    @RequestMapping("calculate_number/note_center")
    @ResponseBody
    public Result getNumberForNote(@RequestParam("template") final String template,
                                    @RequestParam("noteType") final Integer noteType) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
                Assert.notNull(noteType, "请选择提醒类型");
            }

            @Override
            protected MarketingSmsTempBO process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);

                List<Long> carIds = getCarIds(new CarIdCallBack<ShopNoteInfoVO>() {
                    @Override
                    public Page<ShopNoteInfoVO> getPagedCustomer(int pageIndex) {
                        return noteInfoService.getShopNoteInfo(shopId, noteType, pageIndex, PAGE_SIZE);
                    }

                    @Override
                    public Long getCarId(ShopNoteInfoVO shopNoteInfoVO) {
                        return shopNoteInfoVO.getCustomerCarId();
                    }
                });

                int code = SendPositionEnum.CUSTOMER_MAINTAIN_NOTE.getCode();
                PreSendParam preSendParam = new PreSendParam(template, shopId, carIds, code);
                return smsCenter.preSend(preSendParam);
            }
        }.execute();
    }


    private  <T> List<Long> getCarIds(CarIdCallBack<T> callback) {
        List<Long> carIds = Lists.newArrayList();
        int pageIndex = 1;
        do {
            Page<T> page = callback.getPagedCustomer(pageIndex);
            List<T> content = page.getContent();
            if (CollectionUtils.isEmpty(content)) {
                break;
            }
            for (T item : content) {
                carIds.add(callback.getCarId(item));
            }
            pageIndex++;
        } while (true);
        return carIds;
    }


}



