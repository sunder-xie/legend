package com.tqmall.legend.web.insurance;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.param.insurance.InsuranceUserServicePackageParam;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.insurance.AnxinInsuranceSettleFacade;
import com.tqmall.legend.facade.insurance.bo.ConsumeServiceBo;
import com.tqmall.legend.facade.insurance.bo.ConsumeServiceParamBo;
import com.tqmall.legend.facade.insurance.vo.InsuranceUserServicePackageVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/9/14.
 * 安心保险服务券核销
 */
@Slf4j
@Controller
@RequestMapping("insurance/anxin/settle")
public class AnxinInsuranceSettleController extends BaseController {
    @Autowired
    private AnxinInsuranceSettleFacade anxinInsuranceSettleFacade;
    @Autowired
    private SmsService smsService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private ShopManagerService shopManagerService;

    /**
     * 服务券列表页
     *
     * @return
     */
    @RequestMapping("service-list")
    public String serviceListPage() {
        return "yqx/page/ax_insurance/settle/service-list";
    }

    /**
     * 获取服务券list
     *
     * @return
     */
    @RequestMapping("service-list/list")
    @ResponseBody
    public Result<DefaultPage<InsuranceUserServicePackageVo>> serviceList(@PageableDefault(page = 1, value = 30) Pageable pageable) {
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        DefaultPage<InsuranceUserServicePackageVo> page = null;
        if (StringUtils.isBlank(ucShopId)) {
            page = new DefaultPage(Lists.newArrayList());
            return Result.wrapSuccessfulResult(page);
        }
        //获取参数
        InsuranceUserServicePackageParam servicePackageParam = new InsuranceUserServicePackageParam();
        servicePackageParam.setShopId(Integer.parseInt(ucShopId));
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        //车牌号
        if (searchParams.containsKey("license")) {
            String license = (String) searchParams.get("license");
            servicePackageParam.setVehiclePlateNo(license);
        }
        //生效开始时间
        if (searchParams.containsKey("startTime")) {
            String startTime = (String) searchParams.get("startTime");
            Date gmtValidStart = DateUtil.convertStringToDate(startTime);
            servicePackageParam.setGmtValidStart(gmtValidStart);
        }
        //生效结束时间
        if (searchParams.containsKey("endTime")) {
            String endTime = (String) searchParams.get("endTime");
            Date gmtValidEnd = DateUtil.convertStringToDate(endTime);
            servicePackageParam.setGmtValidEnd(gmtValidEnd);
        }
        try {
            page = anxinInsuranceSettleFacade.getPage(pageable, servicePackageParam);
        } catch (Exception e) {
            log.error("【dubbo:调用insurance】获取服务券列表出现异常", e);
        }
        if (page == null) {
            page = new DefaultPage(Lists.newArrayList());
        }
        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 服务券核销页
     *
     * @return
     */
    @RequestMapping("consume-service")
    public String consumeService(@RequestParam("id") Integer id, Model model) {
        if (id == null) {
            return "redirect:service-list";
        }
        InsuranceUserServicePackageVo insuranceUserServicePackageVo = null;
        try {
            insuranceUserServicePackageVo = anxinInsuranceSettleFacade.getDetail(id);
        } catch (Exception e) {
            log.error("【dubbo:调用insurance】服务券核销，出现异常，入参id：" + id, e);
        }
        if (insuranceUserServicePackageVo == null || !insuranceUserServicePackageVo.getAvailable()) {
            return "redirect:service-list";
        }
        model.addAttribute("insuranceUserServicePackageVo", insuranceUserServicePackageVo);
        return "yqx/page/ax_insurance/settle/consume-service";
    }

    /**
     * 服务券核销接口
     *
     * @return
     */
    @RequestMapping(value = "consume-service/settle", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> settle(@RequestBody ConsumeServiceBo consumeServiceBo) {
        if (consumeServiceBo == null) {
            return Result.wrapErrorResult("", "参数不能为空");
        }
        List<ConsumeServiceParamBo> consumeServiceParamBoList = consumeServiceBo.getConsumeServiceParamBoList();
        if (CollectionUtils.isEmpty(consumeServiceParamBoList)) {
            return Result.wrapErrorResult("", "请选择需要核销的券");
        }
        for (ConsumeServiceParamBo consumeServiceParamBo : consumeServiceParamBoList) {
            Integer serviceItemId = consumeServiceParamBo.getServiceItemId();
            if (serviceItemId == null || serviceItemId <= 0) {
                return Result.wrapErrorResult("", "服务券id有误");
            }
            Integer consumeTimes = consumeServiceParamBo.getConsumeTimes();
            if (consumeTimes == null || consumeTimes <= 0) {
                return Result.wrapErrorResult("", "服务券使用次数有误");
            }
            Integer consumedUserPackageId = consumeServiceParamBo.getConsumedUserPackageId();
            if (consumedUserPackageId == null || consumedUserPackageId <= 0) {
                return Result.wrapErrorResult("", "服务项目id有误");
            }
        }
//        String mobile = consumeServiceBo.getMobile();
//        String code = consumeServiceBo.getCode();
//        if (!StringUtil.isMobileNO(mobile)) {
//            return Result.wrapErrorResult("", "手机号有误");
//        }
//        if (StringUtils.isBlank(code)) {
//            return Result.wrapErrorResult("", "验证码为空");
//        }
//        //校验验证码
//        Jedis slaveJedis = null;
//        Jedis masterJedis = null;
        try {
//            slaveJedis = JedisPoolUtils.getSlaveJedis();
//            String key = Constants.ANXIN_INSURANCE_MOBILE_CODE + mobile;
//            String checkCode = slaveJedis.get(key);
//            if (!code.equals(checkCode)) {
//                return Result.wrapErrorResult("", "验证码不正确，请重新输入验证码");
//            }
            //数据合法，进行数据校验
            anxinInsuranceSettleFacade.consumeServiceItem(consumeServiceBo);
//            //销毁验证码
//            masterJedis = JedisPoolUtils.getMasterJedis();
//            masterJedis.del(key);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("【dubbo服务券核销】出现异常", e);
            return Result.wrapErrorResult("", "核销失败，请稍后再试");
        }
//        finally {
//            if (slaveJedis != null) {
//                JedisPoolUtils.returnSlaveRes(slaveJedis);
//            }
//            if (masterJedis != null) {
//                JedisPoolUtils.returnMasterRes(masterJedis);
//            }
//        }
    }

    /**
     * 服务券核销发短信
     *
     * @return
     */
    @RequestMapping(value = "consume-service/send", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> sendMobile(@RequestParam(value = "mobile") String mobile) {
        if (!StringUtil.isMobileNO(mobile)) {
            return Result.wrapErrorResult("", "手机号有误");
        }
        //发送短信
        String code = smsService.generateCode();
        SmsBase smsBase = new SmsBase();
        smsBase.setAction(Constants.LEGEND_MARKETING_SMS_TPL);
        smsBase.setMobile(mobile);
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("content", "尊敬的车主，本次服务包核销的验证码为：" + code);
        smsBase.setData(smsMap);
        smsService.sendMsg(smsBase, "【安心保险：服务券核销】发短信验证码");
        //将验证码放到redis
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getMasterJedis();
            String key = Constants.ANXIN_INSURANCE_MOBILE_CODE + mobile;
            jedis.set(key, code);
            jedis.expire(key, CacheConstants.INVEST_MOBILE_KEY_EXP_TIME);//半小时失效
        } catch (Exception e) {
            log.error("【安心保险：服务券核销】发短信验证码，redis设置出现异常", e);
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping(value = "bank-card", method = RequestMethod.GET)
    public String bankCard(@RequestParam(value = "id", required = false) Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        FinanceAccount financeAccount;
        if (id != null) {
            financeAccount = financeAccountService.getSettleFinanceAccount(id);
        } else {

            financeAccount = financeAccountService.getSettleFinanceAccount(shopId);
        }
        ShopManager shopManager = shopManagerService.getAdmin(shopId);
        model.addAttribute("mobile", shopManager.getMobile());
        model.addAttribute("account", financeAccount);

        return "yqx/page/ax_insurance/settle/bank-card";
    }

    @RequestMapping(value = "bank-card-routing", method = RequestMethod.GET)
    public String routing(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        FinanceAccount financeAccount = financeAccountService.getSettleFinanceAccount(shopId);
        ShopManager shopManager = shopManagerService.getAdmin(shopId);
        model.addAttribute("mobile", shopManager.getMobile());
        model.addAttribute("account", financeAccount);
        if (financeAccount != null) {
            return "yqx/page/ax_insurance/settle/bank-card-detail";
        }
        return "yqx/page/ax_insurance/settle/bank-card";
    }

    @RequestMapping(value = "bank-card-detail", method = RequestMethod.GET)
    public String bankDetail(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        FinanceAccount account = financeAccountService.getSettleFinanceAccount(shopId);
        if (account == null) {
            return "common/error";
        }
        ShopManager shopManager = shopManagerService.getAdmin(shopId);
        model.addAttribute("mobile", shopManager.getMobile());
        model.addAttribute("account", account);
        return "yqx/page/ax_insurance/settle/bank-card-detail";
    }

    @RequestMapping(value = "bank-card/code", method = RequestMethod.POST)
    @ResponseBody
    public Result sendCode(@RequestParam("mobile") String mobile) {
        if (!StringUtil.isMobileNO(mobile)) {
            return Result.wrapErrorResult("", "手机号码格式有误");
        }
        String code = smsService.generateCode();
        SmsBase smsBase = new SmsBase();
        smsBase.setMobile(mobile);
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("code", code);
        smsBase.setData(smsMap);
        smsBase.setAction(Constants.LEGEND_BANK_BIND_CODE);
        boolean success = smsService.sendMsg(smsBase, "【安心保险】设置银行卡号");
        if (!success) {
            return Result.wrapErrorResult("", "获取验证码失败");
        }
        jedisClient.set(Constants.LEGEND_BANK_BIND_CODE + mobile, CacheConstants.INVEST_MOBILE_KEY_EXP_TIME, code);
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping(value = "bank-card/save", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> bankCardSave(@RequestBody FinanceAccount financeAccount) {
        prefectAccount(financeAccount);
        Long id = financeAccountService.saveOrUpdate(financeAccount);
        //使用一次即失效
        jedisClient.delete(Constants.LEGEND_BANK_BIND_CODE + financeAccount.getMobile());
        return Result.wrapSuccessfulResult(id);
    }

    private void prefectAccount(FinanceAccount financeAccount) {
        if (financeAccount == null) {
            throw new BizException("信息有误,操作失败");
        }
        if (StringUtils.isBlank(financeAccount.getAccount())) {
            throw new BizException("银行卡号是必填项");
        }
        if (StringUtils.isBlank(financeAccount.getBank())) {
            throw new BizException("开户银行未选择");
        }
        if (StringUtils.isBlank(financeAccount.getAccountBank())) {
            throw new BizException("开户支行是必填项");
        }
        if (StringUtils.isBlank(financeAccount.getAccountUser())) {
            throw new BizException("收款人是必填项");
        }
        if (StringUtils.isBlank(financeAccount.getIdentifyingCode())) {
            throw new BizException("验证码是必填项");
        }
        if (StringUtils.isBlank(financeAccount.getMobile())) {
            throw new BizException("联系电话是必填项");
        }
        checkCode(financeAccount.getIdentifyingCode(), financeAccount.getMobile());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        financeAccount.setShopId(userInfo.getShopId());
        financeAccount.setCreator(userInfo.getUserId());
        financeAccount.setUserId(0L); //设置0则为门店账户
        financeAccount.setAccountType(1);//提现账户类型 1.银行卡 2.支付宝
    }

    private void checkCode(String code, String mobile) {
        String cacheCode = jedisClient.get(Constants.LEGEND_BANK_BIND_CODE + mobile, String.class);
        if (cacheCode == null) {
            throw new BizException("验证码已失效");
        }
        if (!code.equals(cacheCode)) {
            throw new BizException("验证码验证失败");
        }
    }
}
