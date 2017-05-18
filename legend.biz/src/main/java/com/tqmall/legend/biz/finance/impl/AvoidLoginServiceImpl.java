package com.tqmall.legend.biz.finance.impl;

import com.tqmall.common.util.Base64Util;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.finance.AvoidLoginService;
import com.tqmall.legend.biz.finance.vo.LoginEpcVo;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Constants;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.finance.LoginStallVo;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.tqmallstall.service.region.RpcRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by changqiang.ke on 16/3/29.
 */
@Slf4j
@Service
public class AvoidLoginServiceImpl extends BaseServiceImpl implements AvoidLoginService {
    @Resource
    private RpcRegionService rpcRegionService;
    @Resource
    private ShopService shopService;
    //云修到电商免登陆URL
    @Value("${tqmall.url}")
    private String tqmallUrl;

    @Value("${epc.url}")
    private String epcUrl;

    public void avoidLoginStall(HttpServletResponse response, Long shopId, LoginStallVo loginStallVo) {
        String loginStallVoJson = JSONUtil.object2Json(loginStallVo);
        try {
            String loginStallVoStr = Base64Util.encode(loginStallVoJson);//加密登陆信息
//            String sign = MD5Util.MD5("abcdef" + loginStallVoStr + "1234567");
            String sign = MD5Util.MD5(Constants.SIGN_PRE + loginStallVoStr + Constants.SIGN_POST);
            String url = tqmallUrl + "Legend/avoid_login?yunCode=" + loginStallVoStr + "&sign=" + sign;
            log.info("shopId:{}，登录信息：{} ,从云修免登陆到电商的url：{}", shopId, loginStallVoJson, url);
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("从云修免登陆到电商失败,门店没有对应的登陆信息后，门店id:{},异常信息:{]", shopId, e);
        }
    }

    public String avoidLoginStall(Long shopId, LoginStallVo loginStallVo) {
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            loginStallVo.setMobile(shop.getMobile());
            Long cityId = shop.getCity();//门店默认城市站
            cityId = getCityId(shopId, cityId);//获取实际登陆的城市站
            if (cityId == null || StringUtil.isNull(shop.getMobile())) {
                return null;
            }
            loginStallVo.setCityId(cityId);//设置登陆城市站
            String loginStallVoJson = JSONUtil.object2Json(loginStallVo);
            String loginStallVoStr = Base64Util.encode(loginStallVoJson);//加密登陆信息
            String sign = MD5Util.MD5(Constants.SIGN_PRE + loginStallVoStr + Constants.SIGN_POST);
            String url = tqmallUrl + "Legend/avoid_login?yunCode=" + loginStallVoStr + "&sign=" + sign;
            log.info("shopId:{}，登录信息：{} ,从云修免登陆到电商的url：{}", shopId, loginStallVoJson, url);
            return url;
        }
        return null;

    }

    /**
     * 设置登录信息的城市站
     *
     * @param shopId
     * @param cityId 门店城市站
     * @return
     */
    public Long getCityId(Long shopId, Long cityId) {
        log.info("shopId为：{}的门店调用判断城市站是否开通接口传参，城市站：{}", shopId, cityId);
        com.tqmall.core.common.entity.Result<Boolean> cityResult = rpcRegionService.isOpenTqmallServer(cityId.intValue());
        log.info("调用判断城市站是否开通接口返回结果：{}", LogUtils.objectToString(cityResult));
        if (cityResult == null) {
            return null;
        } else {
            if (!cityResult.getData()) {
                log.info("shopId为：{}的门店调用获取登录城市站接口传参，城市站：{}", shopId, cityId);
                com.tqmall.core.common.entity.Result<Integer> loginResult = rpcRegionService.unOpenedCityConvertOpenedCity(cityId.intValue());
                log.info("调用获取登录城市站接口返回结果：{}", LogUtils.objectToString(loginResult));
                if (loginResult == null || !loginResult.isSuccess()) {
                    return null;
                }
                cityId = loginResult.getData().longValue();//实际登录城市站
            }
        }
        return cityId;
    }

    @Override
    public String avoidLoginEpc(Long shopId, LoginEpcVo loginEpcVo) {
        if (loginEpcVo == null) {
            loginEpcVo = new LoginEpcVo();
        }
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            if (shop.getCity() == null || StringUtil.isNull(shop.getMobile())) {
                return null;
            }
            loginEpcVo.setCityId(shop.getCity());
            loginEpcVo.setMobile(shop.getMobile());
            String loginJson = JSONUtil.object2Json(loginEpcVo);
            String loginStr = Base64Util.encode(loginJson);//加密登陆信息
            String sign = MD5Util.MD5(Constants.SIGN_PRE + loginStr + Constants.SIGN_POST);
            String url = epcUrl + "avoidLogin/legendLogin?param=" + loginStr + "&sign=" + sign;
            log.info("shopId:{}，登录信息：{} ,从云修免登陆到汽配的url：{}", shopId, loginJson, url);
            return url;
        }
        return null;
    }
}
