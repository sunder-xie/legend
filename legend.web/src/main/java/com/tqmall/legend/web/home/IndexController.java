package com.tqmall.legend.web.home;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.privilege.PasswordChangeService;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sell.ShopSellService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.sys.LoginLogoutLogService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.biz.websocket.base.RedisPublish;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.sys.LoginLogoutLog;
import com.tqmall.legend.entity.sys.ReferEnum;
import com.tqmall.legend.enums.security.ShopLoginLevelEnum;
import com.tqmall.legend.enums.shop.ShopAgreementStatusEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import com.tqmall.legend.facade.security.UserLoginFacade;
import com.tqmall.legend.pojo.ShopManagerCom;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.CodeGeneration;
import com.tqmall.legend.web.vo.home.ShopLoginVo;
import com.tqmall.oss.OSSClientUtil;
import com.tqmall.oss.OSSConstants;
import com.tqmall.oss.ObjectKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 用户登录判定相关模块代码
 */

/**
 * Created by litan on 14-11-10.
 */
@Controller
@RequestMapping("index")
public class IndexController extends BaseController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    PasswordChangeService passwordChangeService;
    @Autowired
    ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;
    @Autowired
    private LoginLogoutLogService loginLogoutLogService;
    @Resource
    private SmsService smsService;
    @Autowired
    private RedisPublish redisPublish;
    @Autowired
    private UserLoginFacade userLoginFacade;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;
    @Autowired
    private ShopSellService shopSellService;
    @Value("${legend.socket.url}")
    private String socketUrl;
    @Value("${legend.url}")
    private String legendUrl;
    @Autowired
    private OSSClientUtil ossClientUtil;
    @Value("${tqmall.oss.bucketName}")
    private String tqmallBucketName;
    /**
     * 另开线程处理二维码实现逻辑
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * 用户登录后跳转首页
     *
     * @param model
     * @param request
     * @param response
     *
     * @return
     */
    @RequestMapping
    public String homepage(Model model, HttpServletRequest request, HttpServletResponse response) {
        String error = request.getParameter("error");
        if (!StringUtils.isBlank(error)) {
            String msg = "系统异常";
            if (StringUtils.equals(error, "ce")) {
                msg = "验证码错误";
            } else if (StringUtils.equals(error, "pe")) {
                msg = "用户名密码错误";
            } else if (StringUtils.equals(error, "ue")) {
                msg = "用户信息异常错误";
            } else if (StringUtils.equals(error, "se")) {
                msg = "系统异常";
            } else if (StringUtils.equals(error, "we")) {
                msg = "非法访问";
            } else if (StringUtils.equals(error, "sn")) {
                msg = "该门店不存在或已关闭";
            }
            model.addAttribute("errormsg", msg);
        }
        model.addAttribute("socketUrl", socketUrl);
        // 判断缓存是否有数据，如果有则直接跳转到首页。
        Cookie cookieUUID = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        if (null != cookieUUID) {
            try {
                Long shopId = UserUtils.getShopIdForSession(request);
                if (shopId == 0L) {
                    return "/login_shop";
                }
                Shop shop = shopService.selectById(shopId);
                if (shop == null) {
                    return "/login_shop";
                }
                String contextPath = request.getContextPath();
                String url = contextPath + "/guide";
                // 如果是档口门店，且门店未同意协议，则跳转至协议页面
                int level = shop.getLevel().intValue();
                if (level == ShopLevelEnum.TQMALL.getValue() || level == ShopLevelEnum.BASE.getValue() || level == ShopLevelEnum.STANDARD.getValue() || level == ShopLevelEnum.PROFESSION.getValue()) {
                    Integer agreementStatus = shop.getAgreementStatus();
                    if (ShopAgreementStatusEnum.NO.getCode().equals(agreementStatus)) {
                        url = contextPath + "/shop/tqmall-agreement";
                    } else {
                        url = contextPath + "/home";
                    }
                } else {
                    // 如果是管理员，多个账号, 跳转到新首页
                    UserInfo userInfo = UserUtils.getUserInfo(request);
                    List<ShopManager> shopManagerList = shopManagerService.selectByShopId(shopId);
                    if (!CollectionUtils.isEmpty(shopManagerList) && shopManagerList.size() > 1 && userInfo.getUserIsAdmin().equals(1)) {
                        url = contextPath + "/home";
                    }
                }
                response.sendRedirect(url);
            } catch (IOException e) {
                logger.error("转发异常", e);
            }
        }
        return "/login_shop";
    }

    /**
     * 用户登录校验
     *
     * @param username
     * @param password
     * @param validateCode
     *
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public Result check(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "validateCode") String validateCode,
                        @RequestParam(value = "shopId", required = false) Long chooseShopId) {
        // 查询用户登录信息
        if (StringUtils.isBlank(validateCode) || !checkValidateCode(request, validateCode)) {
            return Result.wrapErrorResult("", "验证码错误！");
        }
        username = username.trim();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.wrapErrorResult("", "用户名或密码不能为空！");
        }
        List<ShopManagerLogin> loginList = userInfoService.getLoginInfo(username);
        if (CollectionUtils.isEmpty(loginList)) {
            return Result.wrapErrorResult("", "用户名或密码错误！");
        }
        password = MD5Util.MD5(password);
        List<ShopManagerLogin> existLoginList = Lists.newArrayList();
        for (ShopManagerLogin shopManagerLogin : loginList) {
            if (password.equals(shopManagerLogin.getPassword())) {
                existLoginList.add(shopManagerLogin);
            }
        }
        if (CollectionUtils.isEmpty(existLoginList)) {
            return Result.wrapErrorResult("", "用户名或密码错误！");
        }
        //如果只有1条数据，则可登录
        int loginSize = existLoginList.size();
        ShopManagerLogin login = null;
        if (chooseShopId == null) {
            if (loginSize > 1) {
                //查询门店信息
                Map<String, Object> shopMap = Maps.newHashMap();
                List<Long> shopIds = Lists.newArrayList();
                for (ShopManagerLogin shopManagerLogin : existLoginList) {
                    Long shopId = shopManagerLogin.getShopId();
                    shopIds.add(shopId);
                }
                shopMap.put("shopIds", shopIds);
                List<Shop> shopList = shopService.select(shopMap);
                List<ShopLoginVo> shopLoginList = Lists.newArrayList();
                for (Shop shop : shopList) {
                    ShopLoginVo shopLoginVo = new ShopLoginVo();
                    shopLoginVo.setId(shop.getId());
                    shopLoginVo.setName(shop.getName());
                    shopLoginList.add(shopLoginVo);
                }
                return Result.warpGeneralResult(shopLoginList, false, Constants.MORE_USER_INFO_CODE, "存在多家店");
            } else {
                login = existLoginList.get(0);
            }
        } else {
            //选择多家门店情况
            for (ShopManagerLogin shopManagerLogin : existLoginList) {
                Long checkShopId = shopManagerLogin.getShopId();
                if (Long.compare(chooseShopId, checkShopId) == 0) {
                    login = shopManagerLogin;
                }
            }
            if (login == null) {
                return Result.wrapErrorResult("", "选择的门店有误，请重新选择");
            }
        }
        Long shopId = login.getShopId();
        Long userId = login.getManagerId();
        try {
            userLoginFacade.checkShopLevelOneLogin(shopId, userId);
        } catch (BizException e) {
            logger.error("用户登录失败,原因", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e) {
            logger.error("用户登录失败.", e);
            return Result.wrapErrorResult("", "用户登录异常,请稍后再试");
        }
        //管理员不做限制
        if (login.getIsAdmin() != 1) {
            String level = shopConfigureService.getShopConfigure(shopId, Integer.valueOf(ShopConfigureTypeEnum.SHOPSECURITYLEVEL.getCode()), "");
            if (StringUtils.isNotBlank(level) && !level.equals(ShopLoginLevelEnum.LEVEL_ONE.getLevel())) {
                return Result.wrapErrorResult("", "门店已开启了更高安全等级的设置，无法通过账号密码登录，请用扫描登录试试");
            }
        }
        // 登录成功，记录登录日志。
        addLoginLog(login);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 添加登录日志
     *
     * @param login
     */
    private void addLoginLog(ShopManagerLogin login) {
        LoginLogoutLog log = new LoginLogoutLog();
        log.setShopId(login.getShopId());
        log.setAccount(login.getAccount());
        log.setLoginTime(new Date());
        log.setManagerLoginId(login.getId());
        log.setRefer(ReferEnum.WEB.getCode());
        log.setCreator(login.getId());
        log.setOpUrl(request.getRequestURI());
        loginLogoutLogService.add(log);
    }

    private boolean checkValidateCode(HttpServletRequest request, String validateCode) {
        String redisValidateCode = obtainSessionValidateCode(request);
        boolean checkCode = false;
        if (StringUtils.equals(validateCode.toUpperCase(), redisValidateCode) || StringUtils.equals(StringUtil.fullWidth2halfWidth(validateCode.toUpperCase()), redisValidateCode)) {
            checkCode = true;
        }
        return checkCode;
    }

    private String obtainSessionValidateCode(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_VC_UUID);
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        String code = "";
        try {
            if (cookie == null) {
                return code;
            }
            String value = jedis.hget(cookie.getValue(), Constants.SESSION_VC_UUID);
            if (null != jedis && !StringUtils.isBlank(value)) {
                code = value;
            }
        } catch (Exception e) {
            logger.error("从redis中获取code异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return code;
    }


    /**
     * 去iserver验证
     *
     * @param code
     * @param id
     *
     * @return
     */
    private Result validateCode(String code, String id) {

        try {
            ShopManager shopManager = userInfoService.getUserInfo(Long.valueOf(id));
            Date date = new Date();
            date.setTime(date.getTime() - 10 * 60 * 1000);
            if (!StringUtils.equals(code, shopManager.getIdentifyingCode())) {
                return Result.wrapErrorResult("", "验证码输入错误");
            }
            if (shopManager.getSendCodeTime().before(date)) {
                return Result.wrapErrorResult("", "验证码超时");
            }
        } catch (Exception e) {
            logger.error("验证登录code异常！", e);
            return Result.wrapErrorResult("", "登录码验证异常");
        }
        return Result.wrapSuccessfulResult(true);
    }

    @RequestMapping(value = "flashCode", method = RequestMethod.POST)
    @ResponseBody
    public Result flashCode(String mobile) {

        if (StringUtils.isBlank(mobile) || !StringUtil.isMobileNO(mobile)) {
            return Result.wrapErrorResult("", "请输入正确的手机号码");
        }
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("mobile", mobile);
        parameters.put("isAdmin", 1);
        List<ShopManager> list = shopManagerService.selectWithoutCache(parameters);
        if (CollectionUtils.isEmpty(list)) {
            return Result.wrapErrorResult("", "该手机号码输入有误，可能原因：所属门店不存在或不是门店主账号");
        }
        ShopManager shopManager = list.get(0);
        if (shopManager.getSendCodeTime() != null) {
            Long diffTime = new Date().getTime() - shopManager.getSendCodeTime().getTime();
            if (diffTime < 1000 * 60) {
                return Result.wrapErrorResult("", "您的操作太频繁了，请稍后再试");
            }
        }
        String code = smsService.generateCode();
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("account", mobile);
        smsMap.put("code", code);
        SmsBase smsBase = new SmsBase(mobile, "legend_password", smsMap);
        boolean success = smsService.sendMsg(smsBase, "门店重设密码");
        if (success) {
            // 将随机数存入数据库
            if (shopManager != null) {
                shopManager.setSendCodeTime(new Date());
                shopManager.setIdentifyingCode(code);
                Result result = userInfoService.updateUserInfo(shopManager);
                if (result.isSuccess()) {
                    return Result.wrapSuccessfulResult(shopManager.getId());
                }
            } else {
                return Result.wrapErrorResult("", "短信发送异常");
            }
        }
        return Result.wrapErrorResult("", "短信发送失败");
    }

    /**
     * 限制登陆提醒，账户：{$account}在非工作时间或非常用工作地点请求登陆，验证码为：{$code}，请确认。
     * 此功能已废弃，等前端代码清理后再清理此接口
     *
     * @param shopId
     * @param account
     *
     * @return
     */
    @Deprecated
    @RequestMapping(value = "send_validate_code")
    @ResponseBody
    public Result sendValidateCode(String shopId, String account) {

        if (StringUtils.isBlank(shopId)) {
            return Result.wrapErrorResult("", "门店信息为空");
        }
        ShopManager shopManager = userInfoService.getShopAdminUserInfo(Long.valueOf(shopId));
        if (null == shopManager || StringUtils.isBlank(shopManager.getMobile())) {
            return Result.wrapErrorResult("", "获取门店管理员手机号码错误");
        }
        String code = smsService.generateCode();
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("account", account);
        smsMap.put("code", code);
        SmsBase smsBase = new SmsBase(shopManager.getMobile(), "legend_action", smsMap);
        boolean success = smsService.sendMsg(smsBase, "验证码");
        if (success) {
            // 将随机数存入数据库
            shopManager.setSendCodeTime(new Date());
            shopManager.setIdentifyingCode(code);
            Result result = userInfoService.updateUserInfo(shopManager);
            if (result.isSuccess()) {
                return Result.wrapSuccessfulResult(shopManager.getId());
            } else {
                return Result.wrapErrorResult("", "短信发送异常");
            }
        }
        return Result.wrapErrorResult("", "短信发送失败");
    }

    @RequestMapping(value = "login_validate_code")
    @ResponseBody
    public Result loginValidateCode(String code, String mobile, String id) {

        if (StringUtils.isBlank(id)) {
            return Result.wrapErrorResult("", "请先获取手机验证码！");
        }
        if (StringUtils.isBlank(code)) {
            return Result.wrapErrorResult("", "请输入手机验证码！");
        }
        return validateCode(code, id);
    }


    @RequestMapping(value = "reset")
    public String reset() {
        return "password/reset_password";
    }

    @RequestMapping(value = "newPassword")
    public String newPassword(ShopManagerCom reg, Model model) {

        Map<String, Object> managerMap = new HashMap<>();
        managerMap.put("mobile", reg.getMobileReg());
        managerMap.put("identifyingCode", reg.getCodeReg());
        List<ShopManager> shopManagerList = shopManagerService.selectWithoutCache(managerMap);
        if (CollectionUtils.isEmpty(shopManagerList)) {
            model.addAttribute("error", "手机验证码错误");
            return "password/reset_password";
        }
        ShopManager shopManager = shopManagerList.get(0);
        // 校验验证码是否输入正确
        if (!(validateCode(reg.getCodeReg(), shopManager.getId() + "").isSuccess())) {
            model.addAttribute("error", "手机验证码错误");
            return "password/reset_password";
        }

        // 根据手机号码获取用户信息
        ShopManagerLogin login = userInfoService.getUserInfoByMobile(reg.getMobileReg());
        if (null == login) {
            model.addAttribute("error", "获取用户信息异常");
            return "password/reset_password";
        }
        model.addAttribute("login", login);
        return "password/change_password";
    }

    /**
     * 校验密码
     *
     * @param reg
     *
     * @return
     */
    @RequestMapping(value = "changePassword")
    @ResponseBody
    public Result changePassword(ShopManagerCom reg) {
        // 校验密码
        if (StringUtils.isBlank(reg.getPasswordReg())) {
            return Result.wrapErrorResult("", "请输入6~12位且包含字母和数字的密码");
        }
        // 更新密码
        Result result = passwordChangeService.resetPassword(reg.getId(), reg.getPasswordReg(), reg.getShopReg());
        return result;
    }

    /**
     * 密码修改成功后跳转成功页
     *
     * @return
     */
    @RequestMapping("success")
    public String success() {
        return "common/success";
    }

    @RequestMapping(value = "remind")
    public String newPassword(Model model) {
        return "common/remind";
    }

    /**
     * session过期
     *
     * @param model
     *
     * @return
     */
    @RequestMapping(value = "sessionout")
    public String sessionout(Model model) {
        model.addAttribute("info", "会话已过期，请重新登录");
        return "login_shop";
    }

    /**
     * @param model
     *
     * @return
     */
    @RequestMapping(value = "authority")
    public String authority(Model model) {
        model.addAttribute("error", "您没有权限。");
        return "common/403";
    }

    /**
     * 角色无权访问页面
     *
     * @param model
     *
     * @return
     */
    @RequestMapping(value = "role")
    public String role(Model model) {
        model.addAttribute("error", "403错误,您无权访问此页面。");
        return "common/error";
    }

    /**
     * 商家版本APP下载H5页面
     *
     * @return
     */
    @RequestMapping(value = "app")
    public String app() {
        return "portal/merchant/merchant_download";
    }

    @Deprecated
    @RequestMapping(value = "expertReg", method = RequestMethod.GET)
    @ResponseBody
    public Result expertReg(String account, String pwd) {
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("password", pwd);
        SmsBase smsBase = new SmsBase(account, "mace_expert_register", smsMap);
        boolean success = smsService.sendMsg(smsBase, "密码设定短信");
        if (!success) {
            return Result.wrapErrorResult("", "短信发送失败");
        }
        return Result.wrapSuccessfulResult("短信发送成功");
    }

    /**
     * 获取二维码信息
     *
     * @return
     */
    @RequestMapping("getQRCode")
    @ResponseBody
    public Result<List<String>> getQRCode(HttpServletResponse response) {
        //从cookie中取uuid,如果存在，则从缓存中获取url，否则重新生成二维码
        List<String> resultList = Lists.newArrayList();
        String qrCode;
        String uuid;
        Cookie cookieUUID = CookieUtils.getCookieByName(request, Constants.SESSION_LOGIN_UUID);
        Jedis slaveJedis = null;
        Jedis masterJedis = null;
        try {
            if (null != cookieUUID) {
                slaveJedis = JedisPoolUtils.getSlaveJedis();
                uuid = cookieUUID.getValue();
                qrCode = slaveJedis.hget(uuid, Constants.SESSION_LOGIN_UUID);
            } else {
                //生成二维码
                uuid = UUID.randomUUID().toString();
                CookieUtils.addCookie(response, Constants.SESSION_LOGIN_UUID, uuid, CacheConstants.SECURE_LOGIN_KEY_EXP_TIME);
                masterJedis = JedisPoolUtils.getMasterJedis();
                StringBuffer qcCodeUrl = new StringBuffer();
                qcCodeUrl.append(legendUrl);
                qcCodeUrl.append("html/redirect.html?uuid=");
                qcCodeUrl.append(uuid);
                ByteArrayOutputStream bs = CodeGeneration.create(qcCodeUrl.toString());
                //上传原图
                String fileKey = ObjectKeyUtil.generateOrigObjectkey("jpg");
                //是否测试环境
                if ("Y".equals(ossClientUtil.getIsTest())) {
                    fileKey = OSSConstants.TEST_OSS_URL + fileKey;
                } else {
                    fileKey = OSSConstants.IMG_OSS_URL + fileKey;
                }
                qrCode = ossClientUtil.putObject(tqmallBucketName, fileKey, bs.toByteArray());
                masterJedis.hset(uuid, Constants.SESSION_LOGIN_UUID, qrCode);
                masterJedis.expire(uuid, CacheConstants.SECURE_LOGIN_KEY_EXP_TIME);
                qRCodeInvalidExtThread(uuid);
            }
        } catch (WriterException e) {
            logger.error("【安全登录】生成二维码出错", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "二维码生成失败");
        } catch (IOException e) {
            logger.error("【安全登录】io异常，生成二维码字节数组出错", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "出现异常，请稍后再试");
        } catch (Exception e) {
            logger.error("【安全登录】出现异常", e);
            return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(), "出现异常，请稍后再试");
        } finally {
            if (slaveJedis != null) {
                JedisPoolUtils.returnSlaveRes(slaveJedis);
            }
            if (masterJedis != null) {
                JedisPoolUtils.returnMasterRes(masterJedis);
            }
        }
        resultList.add(uuid);
        resultList.add(qrCode);
        return Result.wrapSuccessfulResult(resultList);
    }

    /**
     * 二维码失效处理
     *
     * @param uuid
     */
    private void qRCodeInvalidExtThread(final String uuid) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int sentMessages = 0;
                Jedis jedis = null;
                Result<List<String>> result;
                try {
                    jedis = JedisPoolUtils.getSlaveJedis();
                    int count = CacheConstants.SECURE_LOGIN_KEY_EXP_TIME / 10;
                    while (sentMessages < count) {
                        //1分钟内查询缓存是否失效，如果失效则提示二维码失效，需要重新刷新二维码
                        Thread.sleep(10000);
                        if (!jedis.exists(uuid)) {
                            result = new Result<List<String>>();
                            List<String> dataList = Lists.newArrayList();
                            dataList.add(uuid);
                            result.setData(dataList);
                            result.setSuccess(false);
                            result.setCode(LegendErrorCode.QC_CODE_ERROR.getCode());
                            result.setErrorMsg("二维码已过期");
                            String message = new Gson().toJson(result);
                            redisPublish.shareNotice(ChannelsEnum.LOGIN_SHARE_NOTICE.name(), message);
                            return;
                        }
                        sentMessages++;
                    }
                } catch (Exception e) {
                    logger.error("【安全登录】出现异常", e);
                } finally {
                    if (jedis != null) {
                        JedisPoolUtils.returnSlaveRes(jedis);
                    }
                }
            }
        });

    }

    /**
     * 售卖门店客户手机登陆
     *
     * @param mobile       手机号
     * @param validateCode 验证码
     * @param mobileCode   手机验证码
     *
     * @return
     */
    @RequestMapping("/sellShop/loginCheck")
    @ResponseBody
    public com.tqmall.core.common.entity.Result loginCheck(@RequestParam(value = "mobile") final String mobile,
                                                           @RequestParam(value = "validateCode") final String validateCode,
                                                           @RequestParam(value = "mobileCode") final String mobileCode,
                                                           final HttpServletRequest request, final HttpServletResponse response) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(mobile, "手机号错误");
                Assert.isTrue(StringUtils.isNoneBlank(mobileCode), "请输入手机验证码");
            }

            @Override
            protected String process() throws BizException {
                //校验输入验证码
                if (StringUtils.isBlank(validateCode) || !checkValidateCode(request, validateCode)) {
                    throw new BizException("验证码错误");
                }
                //校验手机验证码
                mobileVerifyRecordService.checkSMSCodeThrowException(mobile, mobileCode, Constants.SMS_DEADLINE);

                shopSellService.checkMobileIsExistShop(mobile);
                //校验是否是B账户
                Boolean check = null;
                try {
                    check = shopSellService.checkMobileIsBAccount(mobile);
                    if (check == null || !check) {
                        throw new BizException(LegendError.SHOP_IS_NOT_B_ACCOUNT.getMessage(), LegendError.SHOP_IS_NOT_B_ACCOUNT.getCode(), null);
                    }
                } catch (BizException e) {
                    throw new BizException(e.getMessage(), LegendError.SHOP_IS_NOT_B_ACCOUNT.getCode(), e);
                } catch (Exception e) {
                    throw new BizException(LegendError.SHOP_IS_NOT_B_ACCOUNT.getMessage(), LegendError.SHOP_IS_NOT_B_ACCOUNT.getCode(), e);
                }
                //保存登陆信息到缓存
                shopSellService.saveMobileInRedis(response, mobile);
                return "登录成功";
            }
        }.execute();


    }


}