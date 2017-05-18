package com.tqmall.legend.web.settlement;

import com.google.common.base.Optional;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.insurance.IInsuranceBillService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.activity.ActJumpUrlEnum;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.enums.activity.BankEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/2/29.
 */
@RequestMapping("shop/settlement/activity")
@Slf4j
@Controller
public class ActivitySettleController extends BaseController {
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private IInsuranceBillService insuranceBillService;
    @Autowired
    private IShopActivityService shopActivityService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private SmsService smsService;

    /**
     * 结算：引流活动收入汇总账单
     *
     * @return
     */
    @RequestMapping
    public String index(Model model){
        //查询平台活动，不包含淘汽活动;
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopActivity> shopActivityList = shopActivityService.getShopActivityAllList(shopId);;
        model.addAttribute("shopActivityList",shopActivityList);
        model.addAttribute("moduleUrl", "settlement");
        model.addAttribute("bankList", BankEnum.getMessages());
        return "yqx/page/settlement/activity/settle-bill";
    }

    /**
     * 每个活动的具体对账单价列表
     *
     * @return
     */
    @RequestMapping("detail")
    public String detail(Model model, Integer actTplId,
                         @RequestParam(value = "auditPassStartTime",required = false)String auditPassStartTime,
                         @RequestParam(value = "auditPassEndTime",required = false)String auditPassEndTime){
        if(StringUtils.isNoneBlank(auditPassStartTime)){
            model.addAttribute("auditPassStartTime", auditPassStartTime);
        }
        if(StringUtils.isNoneBlank(auditPassEndTime)){
            model.addAttribute("auditPassEndTime", auditPassEndTime);
        }
        model.addAttribute("moduleUrl", "settlement");
        model.addAttribute("actTplId", actTplId);
        model.addAttribute("bankList", BankEnum.getMessages());

        String jumpUrl = ActJumpUrlEnum.getMesByCode(actTplId);
        //设置需要查询的门店活动id
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<ShopActivity> shopActivityOptional = shopActivityService.get(shopId, actTplId.longValue());
        if(shopActivityOptional.isPresent()){
            ShopActivity shopActivity = shopActivityOptional.get();
            Long shopActId = shopActivity.getId();
            model.addAttribute("shopActId",shopActId);
        }
        if(StringUtils.isBlank(jumpUrl)){
            //默认跳转汇总对账单页面
            return "yqx/page/settlement/activity/settle-bill";
        }else{
            return jumpUrl;
        }
    }

    /**
     * 确认对账 insuranceBill 对账(批量＋单个)
     * @param ids
     * @return
     */
    @RequestMapping("check_settle")
    @ResponseBody
    public Result checkSettle(Long[] ids){

        List<Long> idsList = Arrays.asList(ids);
        //验证门店有没有绑定银行卡
        if(CollectionUtils.isEmpty(idsList)){
            return Result.wrapErrorResult(LegendErrorCode.PARAMS_ERROR.getCode(),LegendErrorCode.PARAMS_ERROR.getErrorMessage());
        }
        Map<String,Object> searchMap = new HashMap<>();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        searchMap.put("shopId", shopId);
        searchMap.put("userId", 0);
        List<FinanceAccount> financeAccounts = financeAccountService.getUserFinanceAccount(searchMap);
        if (CollectionUtils.isEmpty(financeAccounts)){
            String mobile = getShopAdminMobile().getMobile();
            return Result.wrapErrorResult(LegendErrorCode.NO_BANK_BIND_ERROR.getCode(),mobile);
        }
        Map<String,Object> params = new HashMap<>();
        params.put("updateIds",idsList);
        params.put("shopId" , shopId);
        params.put("shopConfirmStatus",1);
        params.put("modifier",userInfo.getUserId());

        try {
            insuranceBillService.batchUpdate(params);
            return Result.wrapSuccessfulResult(true);
        } catch (BusinessCheckedException e) {
            log.error("引流活动确认对帐错误",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.CHECK_SETTLE_DB_ERROR.getCode(), LegendErrorCode.CHECK_SETTLE_DB_ERROR.getErrorMessage());
    }

    /**
     *  查询门店管理员的手机号
     *
     * @return
     */
    private ShopManager getShopAdminMobile() {
        Map<String, Object> searchMap = new HashMap<>();
        Long shopId = UserUtils.getShopIdForSession(request);
        searchMap.put("shopId", shopId);
        searchMap.put("isAdmin",1);
        List<ShopManager> shopManagerList = shopManagerService.select(searchMap);
        ShopManager shopManager = new ShopManager();
        if(!CollectionUtils.isEmpty(shopManagerList)){
            shopManager = shopManagerList.get(0);
        }
        return shopManager;
    }

    /**
     * 活动级确认对账
     * @param actId
     * @return
     */
    @RequestMapping("act_check_settle")
    @ResponseBody
    public Result checkSettle(Long actId){
        //验证门店有没有绑定银行卡
        Map<String,Object> searchMap = new HashMap<>();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        searchMap.put("shopId" , shopId);
        searchMap.put("userId", 0);
        List<FinanceAccount> financeAccounts = financeAccountService.getUserFinanceAccount(searchMap);
        if (CollectionUtils.isEmpty(financeAccounts)){
            String mobile = getShopAdminMobile().getMobile();
            return Result.wrapErrorResult(LegendErrorCode.NO_BANK_BIND_ERROR.getCode(), mobile);
        }
        //有银行卡查询act_id对应的shop_act_id
        searchMap.clear();
        searchMap.put("actTplId", actId);
        searchMap.put("shopId", shopId);
        List<ShopActivity> act = shopActivityService.select(searchMap);
        if (CollectionUtils.isEmpty(act)){
            //一般情况下是不会为空的！！
            return Result.wrapErrorResult(LegendErrorCode.PARAMS_ERROR.getCode(),LegendErrorCode.PARAMS_ERROR.getErrorMessage());
        }
        Map<String,Object> params = new HashMap<>();
        ShopActivity shopActivity = act.get(0);
        Long shopActId = shopActivity.getId();
        params.put("shopActId",shopActId);
        params.put("shopId" , shopId);
        params.put("shopConfirmStatus",1);
        params.put("modifier",userInfo.getUserId());

        try {
            insuranceBillService.batchUpdate(params);
            return Result.wrapSuccessfulResult(true);
        } catch (BusinessCheckedException e) {
            log.error("引流活动确认对帐错误",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.CHECK_SETTLE_DB_ERROR.getCode(),LegendErrorCode.CHECK_SETTLE_DB_ERROR.getErrorMessage());
    }

    /**
     * 绑定银行卡
     * @param financeAccount
     * @return
     */
    @RequestMapping("bind")
    @ResponseBody
    public Result bindBank(FinanceAccount financeAccount){
        //验证验证码是否正确
        ShopManager shopManager = getShopAdminMobile();
        String identifyingCode = shopManager.getIdentifyingCode();
        String checkCode = financeAccount.getIdentifyingCode();
        if(StringUtils.isBlank(identifyingCode) || StringUtils.isBlank(checkCode) || !identifyingCode.equals(checkCode)){
            return Result.wrapErrorResult(LegendErrorCode.BIND_ERROR.getCode(),"验证码有误，请重新输入");
        }
        //绑定银行卡
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        String userName = userInfo.getName();
        financeAccount.setModifier(userId);
//        Integer bank = Integer.valueOf(financeAccount.getAccountBank());
//        financeAccount.setAccountBank(BankEnum.getMessageByCode(bank));
        if(financeAccount.getId() != null){
            log.info("shopId为：{}门店银行卡修改后的信息:{}，修改用户id:{}", userInfo.getShopId(), JSONUtil.object2Json(financeAccount), userId);
            //更新
            try {
                financeAccountService.upDateById(financeAccount);
            } catch (Exception e) {
                log.error("更新门店对账银行卡信息出现异常，操作人：{}，更新信息：{}", userName, financeAccount, e);
                return Result.wrapErrorResult(LegendErrorCode.BIND_ERROR.getCode(),LegendErrorCode.BIND_ERROR.getErrorMessage());
            }
        }else{
            Long shopId = userInfo.getShopId();
            financeAccount.setShopId(shopId);
            financeAccount.setUserId(0l);
            financeAccount.setCreator(userId);
            financeAccount.setAccountType(1);//1-BANKCARD
            log.info("添加门店对账银行卡信息，操作人：{}，添加信息：{}", userName, financeAccount);
            FinanceAccount result = financeAccountService.insertFinanceAccount(financeAccount);
            if (null == result){
                return Result.wrapErrorResult(LegendErrorCode.BIND_ERROR.getCode(),LegendErrorCode.BIND_ERROR.getErrorMessage());
            }
        }
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping("settle_list")
    @ResponseBody
    public Object settleList(@PageableDefault(page = 1,value = 15,sort = "id",direction = Sort.Direction.DESC)Pageable pageable,
                             @RequestParam(value = "excel",required = false)Integer excel,HttpServletResponse response){
        long sTime = System.currentTimeMillis();
        Map<String,Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        if(!searchParam.containsKey("auditPassStartTime") || !searchParam.containsKey("auditPassEndTime")){
            return Result.wrapErrorResult(LegendErrorCode.PARAMS_ERROR.getCode(),"请输入审核通过时间范围");
        }
        //设置查询时间
        searchParam.put("auditPassStartTime", searchParam.get("auditPassStartTime") + " 00:00:00");
        searchParam.put("auditPassEndTime", searchParam.get("auditPassEndTime") + " 23:59:59");
        if(excel != null){
            log.info("【对账单导出开始】{}", DateUtil.convertDateToYMDHMS(new Date()));
            response.setContentType("application/x-msdownload");
            String filename = "act_settle_excel";
            try {
                filename = java.net.URLEncoder.encode("引流活动对账单", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("【引流活动对账单导出】：中午编码转换出现异常{}",e);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
            pageable = new PageRequest(1,1000);
            DefaultPage page = (DefaultPage) insuranceBillService.getActivitySettlePage(pageable, searchParam);
            ModelAndView mav;
            if(ActJumpUrlEnum.getExcelByCode(excel) != null){
                String jumpExcel = ActJumpUrlEnum.getExcelByCode(excel);
                mav  = new ModelAndView(jumpExcel);
            }else{
                mav  = new ModelAndView("yqx/page/settlement/activity/maintain-act-bill-excel");
            }
            mav.addObject("page", page);
            log.info("【对账单导出结束】,耗时{}毫秒", System.currentTimeMillis() - sTime);
            return mav;
        }
        DefaultPage page = (DefaultPage) insuranceBillService.getActivitySettlePage(pageable, searchParam);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 引流活动收入汇总账单列表
     *
     * @return
     */
    @RequestMapping("month_settle_list")
    @ResponseBody
    public Result monthSettleList(){
        Map<String,Object> searchParam = ServletUtils.getParametersMapStartWith(request);
        Result result = insuranceBillService.getInsuranceBillSettleList(searchParam);
        return result;
    }

    /**
     * 获取搜索时间
     *
     * @return
     */
    @RequestMapping("get_time")
    @ResponseBody
    public Result getFirstMonthDay(){
        String firstDay = DateUtil.getFirstMonthDay(-1);
        String lastMonthDay = DateUtil.getLastMonthDay(-1);
        Map<String,Object> dateMap = new HashMap<>();
        dateMap.put("auditPassStartTime",firstDay);
        dateMap.put("auditPassEndTime",lastMonthDay);
        return Result.wrapSuccessfulResult(dateMap);
    }

    /**
     * 获取银行卡号绑定的验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "get_code",method = RequestMethod.POST)
    @ResponseBody
    public Result getCode(String mobile){
        if (StringUtils.isBlank(mobile)) {
            return Result.wrapErrorResult(LegendErrorCode.MOBILE_NULL_ERROR.getCode(), "手机号码为空");
        }
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("mobile", mobile);
        parameters.put("isAdmin", 1);
        List<ShopManager> list = shopManagerService.select(parameters);
        if(CollectionUtils.isEmpty(list)){
            return Result.wrapErrorResult(LegendErrorCode.MOBILE_PERMISSION_ERROR.getCode(),"该手机号码输入有误，可能原因：所属门店不存在或不是门店主账号");
        }
        String sendCode = smsService.generateCode();
        Map<String,Object> smsMap = new HashMap<>();
        smsMap.put("code",sendCode);
        SmsBase smsBase = new SmsBase(mobile,"bank_bind_code",smsMap);
         boolean success= smsService.sendMsg(smsBase,"获取银行卡号绑定的验证码");
        if (success) {
            // 将随机数存入数据库
            if (!CollectionUtils.isEmpty(list)) {
                ShopManager shopManager = list.get(0);
                shopManager.setSendCodeTime(new Date());
                shopManager.setIdentifyingCode(sendCode);
                Result result = userInfoService.updateUserInfo(shopManager);
                if (result.isSuccess()) {
                    return Result.wrapSuccessfulResult(shopManager);
                }
            }
        }
        return Result.wrapErrorResult(LegendErrorCode.SMS_SEND_ERROR.getCode(), "短信发送失败");
    }

    /**
     * 新版获取银行卡号绑定的验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "get-code", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> getCode(final String mobile, final Boolean isShop) {
        return new ApiTemplate<Boolean>() {

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
            protected Boolean process() throws BizException {
                if (StringUtils.isBlank(mobile)) {
                    throw  new BizException("手机号码为空");
                }
                Long userId = UserUtils.getUserIdForSession(request);
                Map<String, Object> parameters = new HashMap<>(2);
                parameters.put("mobile", mobile);
                if (isShop) {
                    parameters.put("isAdmin", 1);
                }else{
                    parameters.put("id",userId);
                }
                List<ShopManager> list = shopManagerService.select(parameters);
                if (CollectionUtils.isEmpty(list)) {
                    logger.error("获取绑定银行卡验证码失败:手机号有误 mobile:{},isShop:{},userId:{}",mobile,isShop,userId);

                    throw new BizException("该手机号码输入有误");
                }
                String sendCode = smsService.generateCode();
                Map<String, Object> smsMap = new HashMap<>();
                smsMap.put("code", sendCode);
                SmsBase smsBase = new SmsBase(mobile, "bank_bind_code", smsMap);
                boolean success = smsService.sendMsg(smsBase, "获取银行卡号绑定的验证码");
                if (success) {
                    // 将随机数存入数据库
                    if (!CollectionUtils.isEmpty(list)) {
                        ShopManager shopManager = list.get(0);
                        shopManager.setSendCodeTime(new Date());
                        shopManager.setIdentifyingCode(sendCode);
                        shopManager.setModifier(userId);
                        Result result = userInfoService.updateUserInfo(shopManager);
                        if (result.isSuccess()) {
                            return true;
                        }
                    }
                }
                throw new BizException("短信发送失败");
            }
        }.execute();
    }
    @RequestMapping("get-act-list")
    @ResponseBody
    public Result<List<ShopActivity>> getShopActivityAllList() {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopActivity> shopActivityList = shopActivityService.getShopActivityAllList(shopId);
        return Result.wrapSuccessfulResult(shopActivityList);
    }
}
