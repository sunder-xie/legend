package com.tqmall.legend.web.marketing.gather;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.account.AccountInfoService;
import com.tqmall.legend.entity.account.AccountInfo;
import com.tqmall.legend.entity.marketing.gather.GatherCouponConfig;
import com.tqmall.legend.facade.customer.CubeCustomerInfoFacade;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.facade.customer.vo.CubeCustomerInfoVo;
import com.tqmall.legend.facade.marketing.gather.GatherPlanFacade;
import com.tqmall.legend.facade.marketing.gather.adaptor.GatherCouponConfigConvertor;
import com.tqmall.legend.facade.marketing.gather.param.FeedbackByPhoneParam;
import com.tqmall.legend.facade.marketing.gather.vo.CustomerTypeNum;
import com.tqmall.legend.facade.marketing.gather.vo.GatherCustomerVO;
import com.tqmall.legend.facade.sms.bo.MarketingSmsTempBO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2016/12/15.
 *
 * 集客方案Controller:包括盘活客户和老客户带新
 */
@Controller
@RequestMapping("/marketing/gather/plan")
public class GatherPlanController extends BaseController {

    @Autowired
    private GatherPlanFacade gatherPlanFacade;
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private CubeCustomerInfoFacade cubeCustomerInfoFacade;



    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("subModule","gather-plan");
        Long shopId = UserUtils.getShopIdForSession(request);
        // 查询已分配客户的服务顾问
        List<AllotUserVo> allotUserList = customerUserRelFacade.getAllotUserList(shopId, true);
        model.addAttribute("userList", allotUserList);
        return "yqx/page/marketing/gather/gather_plan";
    }

    @ResponseBody
    @RequestMapping(value = "customer/type/num", method = RequestMethod.GET)
    public Result<CustomerTypeNum> getCustomerTypeNum(@RequestParam(value = "userId", required = false) final Long userId,
                                                      final HttpServletRequest request) {
        return new ApiTemplate<CustomerTypeNum>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected CustomerTypeNum process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherPlanFacade.getCustomerTypeNum(shopId, userId);
            }
        }.execute();
    }

    @ResponseBody
    @RequestMapping(value = "customer/list", method = RequestMethod.GET)
    public Result<Page<GatherCustomerVO>> getGatherCustomerPage(@RequestParam(value = "userId", required = false) final Long userId,
                                                                @RequestParam("customerType") final String customerType,
                                                                @PageableDefault(page = 1, value = 10) final Pageable pageable,
                                                                final HttpServletRequest request) {
        return new ApiTemplate<Page<GatherCustomerVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<GatherCustomerVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherPlanFacade.getGatherCustomerPage(shopId, userId, customerType, pageable);
            }
        }.execute();
    }



    /**
     * 电话回访
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "feedback/phone", method = RequestMethod.POST)
    public Result<Boolean> feedbackByPhone(final FeedbackByPhoneParam param,
                                           final HttpServletRequest request) {

        return new ApiTemplate<Boolean>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param, "电话回访参数不能为空");
                Long customerCarId = param.getCustomerCarId();
                String content = param.getContent();
                Assert.isTrue(customerCarId != null && customerCarId > 0, "电话回访车辆不能为空");
                Assert.hasText(content, "电话回访内容不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                return gatherPlanFacade.feedbackByPhone(param, userInfo);
            }
        }.execute();
    }

    /**
     * 查询账户信息
     * @param customerId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "account", method = RequestMethod.GET)
    public Result getAccountInfo(@RequestParam("customerId") Long customerId,
                                 HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        AccountInfo accountInfo = accountInfoService.getAccountInfoByCustomerIdAndShopId(shopId, customerId);
        if (accountInfo != null) {
            return Result.wrapSuccessfulResult(accountInfo);
        } else {
            return Result.wrapErrorResult("", "客户没有账户信息");
        }
    }

    /**
     * 发送盘活短信
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sms", method = RequestMethod.POST)
    public Result<Boolean> sendSms(@RequestParam("key") final String key,
                                   @RequestParam(value = "couponInfoId", required = false) final Long couponInfoId,
                                   @RequestParam(value = "noteType", required = false) final Integer noteType,
                                   final HttpServletRequest request) {

        return new ApiTemplate<Boolean>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(key, "key 不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                return gatherPlanFacade.sendSms(key, couponInfoId, noteType, userInfo);
            }
        }.execute();
    }


    /**
     * 计算全部客户所需发送短信数
     * @param template
     * @return
     */
    @RequestMapping(value = "all/customer/sms", method = RequestMethod.GET)
    @ResponseBody
    public Result<MarketingSmsTempBO> getAllCustomerSms(@RequestParam(value = "userId", required = false) final Long userId,
                                                        @RequestParam("customerType") final String customerType,
                                                        @RequestParam("template") final String template,
                                                        final HttpServletRequest request) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
                Assert.hasText(customerType, "客户类型不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected MarketingSmsTempBO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherPlanFacade.getAllCustomerSms(shopId, userId, customerType, template);
            }
        }.execute();
    }

    /**
     * 计算搜索客户所需发送短信数
     * @param template
     * @return
     */
    @RequestMapping(value = "search/customer/sms", method = RequestMethod.GET)
    @ResponseBody
    public Result<MarketingSmsTempBO> getSearchCustomerSms(@RequestParam("template") final String template,
                                                           @RequestParam(value = "userId", required = false) final Long userId,
                                                           @RequestParam(value = "searchKey", required = false) final String searchKey,
                                                           final HttpServletRequest request) {
        return new ApiTemplate<MarketingSmsTempBO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(template, "模板不能为空");
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected MarketingSmsTempBO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherPlanFacade.getSearchCustomerSms(shopId, userId, searchKey, template);
            }
        }.execute();
    }

    @ResponseBody
    @RequestMapping(value = "gather-coupon/save", method = RequestMethod.POST)
    public Result<GatherCouponConfig> saveGatherCouponConfigs(@RequestBody final GatherCouponConfig gatherCouponConfig) {
        return new ApiTemplate<GatherCouponConfig>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherCouponConfig, "送券信息不能为空");
                Assert.notNull(gatherCouponConfig.getCouponInfoId(), "优惠券id不能为空");
                Assert.notNull(gatherCouponConfig.getTotalCouponNum(), "优惠券赠送数量不能为空");
                Assert.notNull(gatherCouponConfig.getPerAccountNum(), "每个账号限制使用数量不能为空");
                Assert.notNull(gatherCouponConfig.getCustomerId(), "客户ID不能为空");
                Assert.notNull(gatherCouponConfig.getCustomerCarId(), "客户车辆ID不能为空");
            }

            @Override
            protected GatherCouponConfig process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                logger.info("[集客老客户带新]送优惠券,shopId:{},userId:{},gatherCouponConfigList:{}",userInfo.getShopId(),userInfo.getUserId(), LogUtils.objectToString(gatherCouponConfig));
                GatherCouponConfig savedGatherCouponConfig = gatherPlanFacade.saveGatherCouponConfig(gatherCouponConfig, userInfo);
                return savedGatherCouponConfig;
            }
        }.execute();
    }

    /**
     * 关键字搜索客户
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "customer/search")
    public Result<Page<GatherCustomerVO>> searchGatherCustomerPage(@PageableDefault(page = 1, value = 10) final Pageable pageable) {
        return new ApiTemplate<Page<GatherCustomerVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Page<GatherCustomerVO> process() throws BizException {
                Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
                logger.info("[集客老客户带新]送优惠券关键字搜索老客户列表,查询条件:{}",LogUtils.objectToString(searchParams));
                Page<CubeCustomerInfoVo> cubeCustomerInfoVoPage = cubeCustomerInfoFacade.getCubeCustomerInfoFromSearch(pageable, searchParams);
                return GatherCouponConfigConvertor.convertPage(cubeCustomerInfoVoPage);
            }
        }.execute();
    }

}
