package com.tqmall.legend.web.portal;

import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.util.Base64Util;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.protal.MobileVo;
import com.tqmall.legend.entity.sms.MobileVerifyRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 招商合作登录
 * Created by nawks.J on 3/12/15.
 */
@Controller
@RequestMapping("portal")
public class PortalController extends PortalBaseController implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(PortalController.class);
    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;
    @Resource
    private SmsService smsService;


    @RequestMapping("")
    public String home(Model model) {
        model.addAttribute("portalNav", "index");
        model.addAttribute("join_phone", Constants.JOIN_PHONE);
        return "portal/home";
    }

    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("portalNav", "index");
        model.addAttribute("join_phone", Constants.JOIN_PHONE);
        return "portal/index";
    }

    @RequestMapping("merchant/merchant_download")
    public String merchantDownload() {
        return "portal/merchant/merchant_download";
    }

    /**
     * 向某个手机号码发送手机号码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "send_verify_code")
    @ResponseBody
    public Result sendValidateCode(String mobile) {
        if (StringUtils.isBlank(mobile) || !StringUtil.isMobileNO(mobile)) {
            return Result.wrapErrorResult("", "请输入正确的手机号码");
        }
        Map<String,Object> map = new HashMap();
        map.put("mobile", mobile);
        List<String> sorts = new ArrayList<>();
        sorts.add(" id desc ");
        map.put("sorts", sorts);
        map.put("offset", 0);
        map.put("limit", 1);
        List<MobileVerifyRecord> mobileVerifyRecords = mobileVerifyRecordService.select(map);
        MobileVerifyRecord mobileVerifyRecord = null;
        if (!CollectionUtils.isEmpty(mobileVerifyRecords)) {
            mobileVerifyRecord = mobileVerifyRecords.get(0);
            Long diffTime = new Date().getTime() - mobileVerifyRecord.getGmtModified().getTime();
            if (diffTime < 1000 * 60) {
                return Result.wrapErrorResult("","您的操作太频繁了，请稍后再试");
            }
        }
        String sendCode = smsService.generateCode();
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("code", sendCode);
        SmsBase smsBase = new SmsBase(mobile, Constants.REGISTER_SMS_ACTION_TPL, smsMap);
        boolean success = smsService.sendMsg(smsBase, "招商加盟发送短信");
        if (success) {
            // 将随机数存入数据库
            if (mobileVerifyRecord != null) {
                mobileVerifyRecord.setCode(sendCode);
                mobileVerifyRecord.setGmtModified(new Date());
                mobileVerifyRecordService.update(mobileVerifyRecord);
                return Result.wrapSuccessfulResult("发送短信成功");
            } else {
                mobileVerifyRecord = new MobileVerifyRecord();
                mobileVerifyRecord.setCode(sendCode);
                mobileVerifyRecord.setMobile(mobile);
                mobileVerifyRecord.setGmtModified(new Date());
                mobileVerifyRecord.setGmtCreate(new Date());
                int flag = mobileVerifyRecordService.insert(mobileVerifyRecord);
                if (flag > 0) {
                    return Result.wrapSuccessfulResult("发送短信成功");
                } else {
                    return Result.wrapSuccessfulResult("发送短信异常");
                }
            }
        }
        return Result.wrapErrorResult("", "短信发送失败");
    }

    /**
     * 合作验证码验证
     *
     * @param code
     * @param mobile
     * @return
     */
    @RequestMapping(value = "login_validate_code")
    @ResponseBody
    public Result loginValidateCode(String code, String mobile, HttpServletResponse response) {
        Jedis jedis = null;
        try {
            Map map = new HashMap();
            map.put("code", code);
            map.put("mobile", mobile);
            List<String> sorts = new ArrayList<>();
            sorts.add(" id desc ");
            map.put("sorts", sorts);
            map.put("offset", 0);
            map.put("limit", 1);
            List<MobileVerifyRecord> mobileVerifyRecords = mobileVerifyRecordService.select(map);
            if (!CollectionUtils.isEmpty(mobileVerifyRecords)) {
                MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecords.get(0);
                Long diffTime = new Date().getTime() - mobileVerifyRecord.getGmtModified().getTime();
                if (diffTime < Constants.SMS_DEADLINE) {
                    //手机号码放入redis
                    // 生成uuid
                    String uuid = UUID.randomUUID() + "";
                    // 启动jedis连接池
                    jedis = JedisPoolUtils.getMasterJedis();
                    jedis.hset(uuid, Constants.SESSION_JOIN_MOBILE, mobile);
                    jedis.expire(uuid, CacheConstants.INVEST_MOBILE_KEY_EXP_TIME);
                    CookieUtils.addCookie(response, Constants.SESSION_JOIN_UUID, uuid, 43200);
                    return Result.wrapSuccessfulResult(true);
                } else {
                    return Result.wrapErrorResult("", "验证码过期");
                }
            } else {
                return Result.wrapErrorResult("", "验证码错误");
            }
        } catch (Exception e) {
            logger.error("合作验证码验证验证码错误", e);
            return Result.wrapErrorResult("", "验证码异常");
        } finally {
            if (null != jedis) {
                JedisPoolUtils.returnMasterRes(jedis);
            }
        }

    }

    /**
     * 电商到云修的免登录方法
     *
     * @param yunCode
     * @param sign
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "avoid_login")
    public String avoidLogin(Model model, @RequestParam(value = "yunCode", required = true) String yunCode, @RequestParam(value = "sign", required = true) String sign, HttpServletResponse response) throws IOException {
        //对获取的yunCode进行解密
        String base64Encode = URLDecoder.decode(yunCode, "UTF-8");
        //对解密完的yunCode进行md5摘要
        String md5Str = MD5Util.MD5(com.tqmall.legend.common.Constants.SIGN_PRE + base64Encode + com.tqmall.legend.common.Constants.SIGN_POST);
        //成功则直接免登录
        if (md5Str.equals(sign.toUpperCase())) {
            String base64Decode = Base64Util.decode(base64Encode);
            Gson gson = new Gson();
            MobileVo mobileVo = gson.fromJson(base64Decode, MobileVo.class);
            String mobile = mobileVo.getMobile();
            //手机号码放入redis
            // 生成uuid
            String uuid = UUID.randomUUID() + "";
            // 启动jedis连接池
            Jedis jedis = null;
            try {
                jedis = JedisPoolUtils.getMasterJedis();
                jedis.hset(uuid, Constants.SESSION_JOIN_MOBILE, mobile);
                CookieUtils.addCookie(response, Constants.SESSION_JOIN_UUID, uuid, 43200);
                response.sendRedirect(request.getContextPath() + "/portal/invest/audit_info");
                jedis.expire(uuid, CacheConstants.INVEST_MOBILE_KEY_EXP_TIME);
            } catch (Exception e) {
                // 日志上下文待补充 wanghui.change
                logger.error("电商到云修的免登录方法异常", e);
            } finally {
                if (null != jedis) {
                    JedisPoolUtils.returnMasterRes(jedis);
                }
            }
            return null;
        }
        //失败则返回首页
        return index(model);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setPageName("index");
    }


}
