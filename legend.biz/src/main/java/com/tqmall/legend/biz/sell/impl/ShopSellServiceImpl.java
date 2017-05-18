package com.tqmall.legend.biz.sell.impl;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sell.ShopSellService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.ucenter.object.result.shop.ShopDTO;
import com.tqmall.ucenter.object.result.shop.ShopInfoDTO;
import com.tqmall.ucenter.service.shop.RpcShopInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by feilong.li on 17/2/22.
 */
@Service
@Slf4j
public class ShopSellServiceImpl implements ShopSellService {

    @Autowired
    private RpcShopInfoService rpcShopInfoService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopManagerService shopManagerService;

    @Override
    public Boolean checkMobileIsBAccount(final String mobile) {
        return new BizTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(mobile, "请输入手机号");
            }

            @Override
            protected Boolean process() throws BizException {
                log.info("[校验B账户] 调用uc的dubbo接口获取账户信息,mobile:{}", mobile);
                Result<ShopInfoDTO> result = rpcShopInfoService.getShopInfoByMobileAndOwnerId(Constants.CUST_SOURCE, mobile, 0);
                log.info("[校验B账户] 调用uc的dubbo接口获取账户信息,mobile:{},result:{}", mobile, JSONUtil.object2Json(result));
                if (result != null && result.isSuccess()) {
                    ShopInfoDTO shopInfoDTO = result.getData();
                    if (shopInfoDTO != null && shopInfoDTO.getShopDTO() != null) {
                        ShopDTO shopDTO = shopInfoDTO.getShopDTO();
                        if (shopDTO.getVerifyStatus().equals(2)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    log.info("[校验B账户] 调用uc的dubbo接口获取账户信息失败, [参数]source:{}, mobile:{}, ownerId:{}, [结果]result:{}", Constants.CUST_SOURCE, mobile, 0, JSONUtil.object2Json(result));
                    throw new BizException(result == null ? "系统内部错误，请稍后重试！" : result.getMessage());
                }
            }
        }.execute();
    }

    /**
     * 检查用户是否已开通系统
     *
     * @param mobile
     *
     * @return
     */
    @Override
    public Boolean checkMobileIsExistShop(final String mobile) {
        return new BizTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(StringUtils.isNotBlank(mobile), "请输入手机号码");
            }

            @Override
            protected Boolean process() throws BizException {
                List<Shop> shopList = shopService.getShopsByMobile(mobile);
                if (!CollectionUtils.isEmpty(shopList)) {
                    throw new BizException("该手机已开通云修系统,请直接登录系统使用!");
                }
                Map<String, Object> param = new HashedMap();
                param.put("mobile", mobile);
                List<ShopManager> shopManagerList = shopManagerService.select(param);
                if (!CollectionUtils.isEmpty(shopManagerList)) {
                    throw new BizException("该手机号码已存在,购买系统请联系客服! 400-9937288-2-1.");
                }
                return false;
            }
        }.execute();
    }

    @Override
    public void saveMobileInRedis(HttpServletResponse response, String mobile) {
        Jedis master = null;
        try {
            String loginUUID = java.util.UUID.randomUUID().toString();
            CookieUtils.addCookie(response, Constants.SESSION_MOBILE_LOGIN, loginUUID, CacheConstants.SELL_MOBILE_LOGIN_KEY_EXP_TIME);
            master = JedisPoolUtils.getMasterJedis();
            master.hset(loginUUID, Constants.SESSION_MOBILE_LOGIN, mobile);
            master.expire(loginUUID, CacheConstants.SELL_MOBILE_LOGIN_KEY_EXP_TIME);
        } catch (Exception e) {
            log.error("[保存用户登陆手机号码] 保存异常, e:", e);
        } finally {
            JedisPoolUtils.returnMasterRes(master);
        }

    }

    @Override
    public String getMobileFromRedis(final HttpServletRequest request) {
        return new BizTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected String process() throws BizException {
                Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_MOBILE_LOGIN);
                if (null != cookie) {
                    String loginUUID = cookie.getValue();
                    Jedis slave = null;
                    try {
                        slave = JedisPoolUtils.getSlaveJedis();
                        String mobile = slave.hget(loginUUID, Constants.SESSION_MOBILE_LOGIN);
                        if (!StringUtils.isBlank(mobile)) {
                            return mobile;
                        } else {
                            throw new BizException("获取手机号失败");
                        }
                    } catch (Exception e) {
                        log.error("[从redis获取缓存的手机号] 获取异常e:", e);
                        throw new BizException("获取手机号异常");
                    } finally {
                        JedisPoolUtils.returnSlaveRes(slave);
                    }
                } else {
                    throw new BizException("登陆超时,请重新登陆");
                }
            }
        }.execute();
    }

}
