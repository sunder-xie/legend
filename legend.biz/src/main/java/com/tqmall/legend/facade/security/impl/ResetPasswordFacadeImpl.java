package com.tqmall.legend.facade.security.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.facade.security.ResetPasswordFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by macx on 2017/2/8.
 */
@Service
@Slf4j
public class ResetPasswordFacadeImpl implements ResetPasswordFacade {

    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;

    @Override
    public ShopManager validateAccount(String account) {
        List<ShopManager> shopManagers;
        Map<String,Object> params = new HashMap<>();
        if (StringUtil.isMobileNO(account)) {
            params.put("mobile", account);
            shopManagers = shopManagerService.select(params);
        } else {
            shopManagers = shopManagerService.selectManagerInfoByAccount(account);
        }
        if (CollectionUtils.isEmpty(shopManagers)) {
            log.info("[账户名校验] 账户信息不存在 account={}", account);
            throw new BizException("用户信息不存在");
        }
        return shopManagers.get(0);
    }

    @Override
    public ShopManager sendValidateCode(String mobile) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mobile", mobile);
        List<ShopManager> shopManagers = shopManagerService.select(parameters);
        if (CollectionUtils.isEmpty(shopManagers)) {
            throw new BizException("账户信息不存在");
        }
        //随机获取验证码
        String sendCode = smsService.generateCode();
        Map<String, Object> smsMap = new HashMap<>();
        smsMap.put("account", mobile);
        smsMap.put("code",sendCode);
        SmsBase smsBase = new SmsBase(mobile,"legend_password",smsMap);
        boolean success = smsService.sendMsg(smsBase,"发送验证码");
        if (!success) {
            throw new BizException("短信发送失败");
        }
        // 将随机数存入数据库
        ShopManager shopManager = shopManagers.get(0);
        shopManager.setSendCodeTime(new Date());
        shopManager.setIdentifyingCode(sendCode);
        com.tqmall.legend.common.Result result = userInfoService.updateUserInfo(shopManager);
        if (!result.isSuccess()) {
            throw new BizException("短信发送失败");
        }
        return shopManager;
    }

    @Override
    public Boolean loginValidateCode(String code, Long userId) {
        ShopManager shopManager = userInfoService.getUserInfo(userId);
        Date date = new Date();
        date.setTime(date.getTime() - 15 * 60 * 1000);
        if (!StringUtils.equals(code, shopManager.getIdentifyingCode())) {
            throw new BizException("验证码错误");
        }
        if (shopManager.getSendCodeTime().before(date)) {
            throw new BizException("验证码超时");
        }
        return Boolean.TRUE;
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        Map<String,Object> params = new HashMap<>();
        params.put("managerId",userId);
        List<ShopManagerLogin> shopManagerLogins = shopManagerLoginService.select(params);
        if(CollectionUtils.isEmpty(shopManagerLogins)) {
            throw new BizException("登录用户信息不存在");
        }
        ShopManagerLogin shopManagerLogin = shopManagerLogins.get(0);
        ShopManagerLogin login = new ShopManagerLogin();
        Long id = shopManagerLogin.getId();
        Long shopId = shopManagerLogin.getShopId();
        login.setId(id);
        login.setPassword(newPassword);
        login.setGmtModified(new Date());
        login.setShopId(shopId);
        log.info("APP重置密码，id：{}， shopId：{}，newPassword：{}，oldPassword：{}", id, shopId, newPassword, shopManagerLogin.getPassword());
        shopManagerLoginService.updateById(login);
    }
}
