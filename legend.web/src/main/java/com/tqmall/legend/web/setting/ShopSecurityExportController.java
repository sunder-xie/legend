package com.tqmall.legend.web.setting;

import com.google.common.base.Optional;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.IpUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.ExportPasswordCheckVO;
import com.tqmall.legend.entity.shop.ExportPasswordSetConfig;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 门店信息导出加密设置
 */
@Controller
@Slf4j
@RequestMapping("shop/conf")
public class ShopSecurityExportController extends BaseController {

    @Resource
    private SmsService smsService;
    @Autowired
    ShopManagerService shopManagerService;
    @Autowired
    ShopConfigureService shopConfigureService;
    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;
    @Autowired
    ShopManagerLoginService shopManagerLoginService;

    // IP访问记录{IP:访问时间戳}
    public static final ConcurrentHashMap<String, Long> ipVisitRecordMap = new ConcurrentHashMap<String, Long>();


    /**
     * 安全导出_设置页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "securityexport/toset", method = RequestMethod.GET)
    public String toSet(Model model, HttpServletRequest request) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 当前用户是否有设置权限
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return "common/error";
        }

        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return "common/error";
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        model.addAttribute("mobile", shopManagerResp.getMobile());

        // 已经设置 跳转到重置页面
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (shopConfigureOptional.isPresent()) {
            return "yqx/page/setting/securityexport/toreset";
        }

        return "yqx/page/setting/securityexport/toset";
    }


    /**
     * 安全导出_重置页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "securityexport/toreset", method = RequestMethod.GET)
    public String toReset(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 当前用户是否有设置权限
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return "common/error";
        }

        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return "common/error";
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        model.addAttribute("mobile", shopManagerResp.getMobile());

        return "yqx/page/setting/securityexport/toset";
    }


    /**
     * 安全导出_停用页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "securityexport/todisable", method = RequestMethod.GET)
    public String exportPasswordSettingEdit(Model model, HttpServletRequest request) {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 当前用户是否有设置权限
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return "common/error";
        }

        // 上一次设置的密码
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (!shopConfigureOptional.isPresent()) {
            return "common/error";
        }

        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return "common/error";
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        model.addAttribute("mobile", shopManagerResp.getMobile());

        return "yqx/page/setting/securityexport/toreset";
    }


    /**
     * 设置导出密码_发送短信
     *
     * @return
     */
    @RequestMapping(value = "exportpassword_sms_verify", method = RequestMethod.GET)
    @ResponseBody
    public Result sendValidateCode() {
        Long userId = UserUtils.getUserIdForSession(request);
        Long shopId = UserUtils.getShopIdForSession(request);

        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return Result.wrapErrorResult("", "发送短信验证码失败");
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        String mobile = shopManagerResp.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return Result.wrapErrorResult("", "发送短信验证码失败, 手机号不存在");
        }

        // 60s内仅操作一次
        String ip = IpUtil.getClientIP(request);
        Long lastVisitTime = ipVisitRecordMap.get(ip);
        long concurrentVisitTime = new Date().getTime();
        if (lastVisitTime != null && (concurrentVisitTime - lastVisitTime) < 1000 * 60) {
            return Result.wrapErrorResult("", "您的操作太频繁了，请60秒后再试");
        } else {
            ipVisitRecordMap.put(ip, concurrentVisitTime);
        }

        // 生成验证码
        String sendCode = smsService.generateCode();
        Map<String, Object> smsMap = new HashMap<String, Object>(1);
        smsMap.put("code", sendCode);
        SmsBase smsBase = new SmsBase(mobile, Constants.EXPORTPASSWORD_SMS_TPL, smsMap);
        boolean success = false;
        try {
            success = smsService.sendMsg(smsBase, "信息导出设置密码发送短信");
        } catch (Exception e) {
            log.error("信息导出设置密码,发送验证码失败, 异常信息:{}", e);
            return Result.wrapErrorResult("", "发送短信异常");
        }

        // 发送成功,保存发送记录
        if (success) {
            try {
                mobileVerifyRecordService.saveVerifyRecord(mobile, sendCode);
            } catch (Exception e) {
                log.error("信息导出设置密码,发送验证码失败, 异常信息:{}", e);
                return Result.wrapErrorResult("", "发送短信异常");
            }
        }

        return Result.wrapSuccessfulResult("发送短信成功");
    }

    /**
     * 保存 导出密码设置
     * <p/>
     * [remark] 仅限门店管理员isAdmin=1
     *
     * @param passwordSetConfig 导出信息设置实体
     * @param request
     * @return
     */
    @RequestMapping(value = "exportpassword/save/ng", method = RequestMethod.POST)
    @ResponseBody
    public Result exportPasswordSettingSave(ExportPasswordSetConfig passwordSetConfig, HttpServletRequest request) {

        // 当前登录用户ID
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 是否门店管理员
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        // 1:系统管理员
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return Result.wrapErrorResult("", "非管理员,无权限操作");
        }
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();
        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return Result.wrapErrorResult("noqualify", "非管理员,无权限操作");
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        String mobile = shopManagerResp.getMobile();

        // 校验验证码是否正确
        String SMSCode = passwordSetConfig.getSMSCode();
        // 验证码有效时间 30min
        boolean isPass = mobileVerifyRecordService.checkSMSCode(mobile, SMSCode, 60 * 30);
        if (!isPass) {
            return Result.wrapErrorResult("errorSMS", "验证码无效");
        }

        // 校验密码与登录密码是否相同
        String exportPassword = passwordSetConfig.getPassword();
        Optional<ShopManagerLogin> shopManagerLoginOptional = shopManagerLoginService.get(userId, shopId);
        if (!shopManagerLoginOptional.isPresent()) {
            return Result.wrapErrorResult("invalid", "无效用户,无权限操作");
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginOptional.get();
        String loginPassword = shopManagerLogin.getPassword();
        String MD5ExportPassword = MD5Util.MD5(exportPassword);
        if (MD5ExportPassword.equals(loginPassword)) {
            return Result.wrapErrorResult("login", "密码不能与系统登录密码相同");
        }

        // 设置过密码: 更新密码
        ShopConfigure shopConfigure = null;
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (shopConfigureOptional.isPresent()) {
            shopConfigure = shopConfigureOptional.get();
            shopConfigure.setConfValue(MD5ExportPassword);
            shopConfigure.setGmtModified(new Date());
            shopConfigure.setModifier(userId);
            try {
                shopConfigureService.update(shopConfigure);
            } catch (Exception e) {
                log.error("信息导出密码重置异常,异常信息:{}", e);
                return Result.wrapErrorResult("error", "信息导出密码重置失败,请重新设置");
            }
            return Result.wrapSuccessfulResult("");
        }

        // 未设置过密码: 新增密码
        shopConfigure = new ShopConfigure();
        shopConfigure.setShopId(shopId);
        shopConfigure.setConfType(Long.parseLong(ShopConfigureTypeEnum.EXPORTPASSWORD.getCode() + ""));
        shopConfigure.setConfKey("export_password");
        shopConfigure.setConfValue(MD5ExportPassword);
        shopConfigure.setIsDeleted("N");
        shopConfigure.setGmtCreate(new Date());
        shopConfigure.setCreator(userId);

        try {
            shopConfigureService.add(shopConfigure);
        } catch (Exception e) {
            log.error("保存信息导出密码设置异常,异常信息:{}", e);
            return Result.wrapErrorResult("error", "信息导出密码设置失败,请重新设置");
        }

        return Result.wrapSuccessfulResult("");
    }

    /**
     * 保存 导出密码重置
     * <p/>
     * [remark] 仅限门店管理员isAdmin=1
     *
     * @param passwordSetConfig 导出信息设置实体
     * @param request
     * @return
     */
    @RequestMapping(value = "exportpassword/update/ng", method = RequestMethod.POST)
    @ResponseBody
    public Result exportPasswordSettingUpdate(ExportPasswordSetConfig passwordSetConfig, HttpServletRequest request) {

        // 当前登录用户ID
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 是否门店管理员
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        // 1:系统管理员
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return Result.wrapErrorResult("", "非管理员,无权限操作");
        }
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 校验密码与登录密码是否相同
        String exportPassword = passwordSetConfig.getPassword();
        Optional<ShopManagerLogin> shopManagerLoginOptional = shopManagerLoginService.get(userId, shopId);
        if (!shopManagerLoginOptional.isPresent()) {
            return Result.wrapErrorResult("invalid", "无效用户,无权限操作");
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginOptional.get();
        String loginpPassword = shopManagerLogin.getPassword();
        String MD5ExportPassword = MD5Util.MD5(exportPassword);
        if (MD5ExportPassword.equals(loginpPassword)) {
            return Result.wrapErrorResult("login", "密码不能与系统登录密码相同");
        }

        // 不能与上次密码相同
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (!shopConfigureOptional.isPresent()) {
            return Result.wrapErrorResult("", "密码重置无效");
        }
        ShopConfigure shopConfigure = shopConfigureOptional.get();

        // 保存'信息导出密码'重置
        shopConfigure.setConfValue(MD5ExportPassword);
        shopConfigure.setGmtModified(new Date());
        shopConfigure.setModifier(userId);
        try {
            shopConfigureService.update(shopConfigure);
        } catch (Exception e) {
            log.error("信息导出密码重置异常,异常信息:{}", e);
            return Result.wrapErrorResult("error", "信息导出密码重置失败,请重新设置");
        }

        return Result.wrapSuccessfulResult("");
    }

    /**
     * 密码停用
     *
     * @param smscode 验证码
     * @param request
     * @return
     */
    @RequestMapping(value = "exportpassword/disable/ng", method = RequestMethod.GET)
    @ResponseBody
    public Result exportPasswordSettingDisabled(@RequestParam(value = "smscode", required = true) String smscode,
                                                HttpServletRequest request) {

        // 当前登录用户ID
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 是否门店管理员
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        // 1:系统管理员
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return Result.wrapErrorResult("", "非管理员,无权限操作");
        }
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 获取管理员手机号码
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(Integer.parseInt(userId + ""), shopId);
        if (!shopManagerRespOptional.isPresent()) {
            return Result.wrapErrorResult("", "密码停用失败");
        }
        ShopManagerResp shopManagerResp = shopManagerRespOptional.get();
        String mobile = shopManagerResp.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return Result.wrapErrorResult("", "密码停用失败");
        }

        // 校验验证码是否正确
        boolean isPass = mobileVerifyRecordService.checkSMSCode(mobile, smscode, 60 * 30);
        if (!isPass) {
            return Result.wrapErrorResult("errorSMS", "验证码无效");
        }

        // 密码设置
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (!shopConfigureOptional.isPresent()) {
            return Result.wrapErrorResult("", "密码重置无效");
        }
        ShopConfigure shopConfigure = shopConfigureOptional.get();
        shopConfigure.setIsDeleted("Y");
        try {
            shopConfigureService.update(shopConfigure);
        } catch (Exception e) {
            log.error("信息导出密码停用异常,异常信息:{}", e);
            return Result.wrapErrorResult("error", "信息导出密码停用失败,请重新设置");
        }

        return Result.wrapSuccessfulResult("");
    }


    /**
     * 密码导出弹出框—确认
     *
     * @param passwordSetConfig 导出信息设置实体
     * @param request
     * @return
     */
    @RequestMapping(value = "exportpassword/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Result exportPasswordConfirm(ExportPasswordSetConfig passwordSetConfig,
                                        HttpServletRequest request) {

        // 当前登录用户ID
        Long shopId = UserUtils.getShopIdForSession(request);
        String inputPassword = passwordSetConfig.getPassword();
        if (StringUtils.isEmpty(inputPassword)) {
            return Result.wrapErrorResult("no", "未输入密码");
        }
        String MD5ExportPassword = MD5Util.MD5(inputPassword);

        // 导出密码
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (!shopConfigureOptional.isPresent()) {
            return Result.wrapErrorResult("noset", "未设置导出密码");
        }
        ShopConfigure shopConfigure = shopConfigureOptional.get();
        if (MD5ExportPassword.equals(shopConfigure.getConfValue())) {
            return Result.wrapSuccessfulResult("");
        }

        return Result.wrapErrorResult("error", "密码不正确,请重新输入");
    }

    /**
     * 密码导出弹出框——门店导出密码是否设置
     *
     * @return
     */
    @RequestMapping(value = "exportpassword/precheck", method = RequestMethod.GET)
    @ResponseBody
    public Result exportPasswordPrecheck(HttpServletRequest request) {

        // 当前登录用户ID
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        ExportPasswordCheckVO exportPasswordCheckVO = new ExportPasswordCheckVO();

        // 是否设置导出密码
        Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.EXPORTPASSWORD, shopId);
        if (shopConfigureOptional.isPresent()) {
            exportPasswordCheckVO.setIsSetedExportPassword("true");
        } else {
            exportPasswordCheckVO.setIsSetedExportPassword("false");
        }

        // 当前是否是管理员
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        if (userIsAdmin != null && userIsAdmin == 1) {
            exportPasswordCheckVO.setIsAdmin("true");
        } else {
            exportPasswordCheckVO.setIsAdmin("false");
        }

        return Result.wrapSuccessfulResult(exportPasswordCheckVO);
    }

    /**
     * 密码导出弹出框-校验excel导出密码合法性
     *
     * @param passwordSetConfig 导出信息设置实体
     * @param request
     * @return
     */
    @RequestMapping(value = "exportpassword/checkpassword", method = RequestMethod.POST)
    @ResponseBody
    public Result checkpasswordInExportPassword(ExportPasswordSetConfig passwordSetConfig,
                                                HttpServletRequest request) {

        // 当前登录用户ID
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 是否门店管理员
        Integer userIsAdmin = userInfo.getUserIsAdmin();
        // 1:系统管理员
        if (!(userIsAdmin != null && userIsAdmin == 1)) {
            return Result.wrapErrorResult("", "非管理员,无权限操作");
        }
        Long userId = userInfo.getUserId();
        Long shopId = userInfo.getShopId();

        // 校验密码与登录密码是否相同
        String exportPassword = passwordSetConfig.getPassword();
        Optional<ShopManagerLogin> shopManagerLoginOptional = shopManagerLoginService.get(userId, shopId);
        if (!shopManagerLoginOptional.isPresent()) {
            return Result.wrapErrorResult("invalid", "无效用户,无权限操作");
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginOptional.get();
        String loginpPassword = shopManagerLogin.getPassword();
        String MD5ExportPassword = MD5Util.MD5(exportPassword);
        if (MD5ExportPassword.equals(loginpPassword)) {
            return Result.wrapErrorResult("login", "密码不能与系统登录密码相同");
        }

        return Result.wrapSuccessfulResult("");
    }


}
