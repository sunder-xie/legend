package com.tqmall.yunxiu.web.pub.avoidlogin;

import com.tqmall.common.Constants;
import com.tqmall.common.util.Base64Util;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.facade.security.UserLoginFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.yunxiu.web.pub.avoidlogin.vo.LegendmAvoidLoginVo;
import com.tqmall.yunxiu.web.pub.avoidlogin.vo.SamAvoidLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by zsy on 16/8/23.
 * 免登陆云修接口
 */
@Slf4j
@Controller
@RequestMapping("pub/avoidLogin")
public class LegendAvoidLoginController extends BaseController{
    @Autowired
    private UserLoginFacade userLoginFacade;

    /**
     * sam后台免登陆legend接口
     * TODO 限制时间（1h内能访问url）,后续可以加上白名单
     * @param params 登录信息
     * @param sign  签名
     * @return
     */
    @RequestMapping("samLogin")
    public String samLogin(@RequestParam(value = "params",required = true)String params,
                        @RequestParam(value = "sign",required = true)String sign,
                        HttpServletResponse response){
        if(StringUtils.isNotBlank(params) && StringUtils.isNotBlank(sign)){
            String checkSign = MD5Util.MD5(Constants.SIGN_PRE + params + Constants.SIGN_POST);
            //签名一致，则解析登录信息
            if(checkSign.equals(sign)){
                String decodeStr = Base64Util.decode(params);//解密
                try {
                    SamAvoidLoginVo samAvoidLoginVo = JSONUtil.readJson(decodeStr, SamAvoidLoginVo.class);
                    log.info("【sam免登陆】登录信息：{}", samAvoidLoginVo);
                    String userGlobalId = samAvoidLoginVo.getUserGlobalId();
                    String backName = samAvoidLoginVo.getBackName();
                    String loginTimeStr = samAvoidLoginVo.getLoginTimeStr();
                    if(StringUtils.isNotBlank(userGlobalId) && StringUtils.isNotBlank(backName) && StringUtils.isNotBlank(loginTimeStr)){
                        //限制登录时间，10分钟
                        Date loginTime = DateUtil.convertStringToDate(loginTimeStr);
                        long loginTimeLong = loginTime.getTime();
                        long sysTimeLong = new Date().getTime();
                        long diffTime = sysTimeLong - loginTimeLong;
                        //超过10分钟登录不了，并且传入的时间不能是当前系统时间后的（比如传了明天等）
                        if(diffTime >= -Constants.AVOID_LOGIN_TIME  && diffTime <= Constants.AVOID_LOGIN_TIME){
                            return userLoginFacade.samAvoidLogin(samAvoidLoginVo.getUserGlobalId(),request,response);
                        }
                    }
                } catch (Exception e) {
                    log.error("【sam免登陆】解析对象装换出错，解密后对象为：{}",decodeStr,e);
                }
            }
        }
        return "login_shop";
    }

    /**
     * 管理后台免登陆legend接口
     * @param params 登录信息
     * @param sign  签名
     * @return
     */
    @RequestMapping("legendmLogin")
    public String legendmLogin(@RequestParam(value = "params", required = true) String params,
                               @RequestParam(value = "sign", required = true) String sign,
                               HttpServletResponse response) {
        if (StringUtils.isNotBlank(params) && StringUtils.isNotBlank(sign)) {
            String checkSign = MD5Util.MD5(Constants.SIGN_PRE + params + Constants.SIGN_POST);
            //签名一致，则解析登录信息
            if (checkSign.equals(sign)) {
                String decodeStr = Base64Util.decode(params);//解密
                try {
                    LegendmAvoidLoginVo legendmAvoidLoginVo = JSONUtil.readJson(decodeStr, LegendmAvoidLoginVo.class);
                    log.info("【legendm免登陆】登录信息：{}", legendmAvoidLoginVo);
                    Long shopId = legendmAvoidLoginVo.getShopId();
                    String backName = legendmAvoidLoginVo.getBackName();
                    if (shopId != null && StringUtils.isNotBlank(backName)) {
                        String loginTimeStr = legendmAvoidLoginVo.getLoginTimeStr();
                        if (shopId > 0 && StringUtils.isNotBlank(backName) && StringUtils.isNotBlank(loginTimeStr)) {
                            //限制登录时间，10分钟内
                            Date loginTime = DateUtil.convertStringToDate(loginTimeStr);
                            long loginTimeLong = loginTime.getTime();
                            long sysTimeLong = new Date().getTime();
                            long diffTime = sysTimeLong - loginTimeLong;
                            //超过10分钟登录不了，并且传入的时间不能是当前系统时间后的（比如传了明天等）
                            if (diffTime >= -Constants.AVOID_LOGIN_TIME && diffTime <= Constants.AVOID_LOGIN_TIME) {
                                return userLoginFacade.legendmAvoidLogin(legendmAvoidLoginVo.getShopId(), request, response);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("【legendm免登陆】解析对象装换出错，解密后对象为：{}", decodeStr, e);
                }
            }
        }
        return "login_shop";
    }
}
