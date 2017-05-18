package com.tqmall.legend.facade.shop.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.insurance.domain.result.InsuranceShopConfigDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceShopConfigService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopTagRelService;
import com.tqmall.legend.biz.shop.ShopTagService;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopTag;
import com.tqmall.legend.entity.shop.ShopTagRel;
import com.tqmall.legend.enums.shop.ShopJoinStatusEnum;
import com.tqmall.legend.enums.shop.ShopTagCodeEnum;
import com.tqmall.legend.enums.shop.ShopWorkshopStatusEnum;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zsy on 16/7/7.
 */
@Slf4j
@Service
public class ShopFunFacadeImpl implements ShopFunFacade{

    @Autowired
    private ShopService shopService;
    @Autowired
    private RpcInsuranceShopConfigService rpcInsuranceShopConfigService;
    @Autowired
    private ShopTagService shopTagService;
    @Autowired
    private ShopTagRelService shopTagRelService;

    /**
     * 根据门店id判断是否加入委托体系
     *
     * 状态：0：无，1：加入 默认为0
     * @param shopId
     * @return false 未加入，true 加入
     */
    @Override
    public boolean isJoin(Long shopId) {
        if(shopId == null){
            return false;
        }
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            Integer joinStatus = shop.getJoinStatus();
            if (ShopJoinStatusEnum.YES.getCode().equals(joinStatus)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据门店id判断是否使用车间
     *
     * 使用车间状态 0：不使用车间 1：使用车间
     * @param shopId false 不使用车间，true 使用
     * @return
     */
    @Override
    public boolean isUseWorkshop(Long shopId) {
        if(shopId == null){
            return false;
        }
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            Integer workshopStatus = shop.getWorkshopStatus();
            if (ShopWorkshopStatusEnum.YES.getCode().equals(workshopStatus)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是钣喷共享中心
     *
     * @param shopId
     * @return false 不是，true 是
     */
    @Override
    public boolean isBpShare(HttpServletRequest request, Long shopId) {
        if(shopId == null){
            return false;
        }
        //登陆后的请求
        if(request != null){
            Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
            if(cookie == null){
                return false;
            }
            Jedis jedis = JedisPoolUtils.getSlaveJedis();
            try {
                String key = cookie.getValue();
                Object isBpShare = jedis.hget(key, Constants.BPSHARE);
                if(isBpShare == null){
                    return false;
                }
                if(isBpShare.equals("true")){
                    return true;
                }
            } catch (Exception e) {
                log.error("redis获取门店是否是钣喷中心，出现异常", e);
            } finally {
                JedisPoolUtils.returnSlaveRes(jedis);
            }
        }else{
            //登录前的请求
            try {
                ShopTag shopTag = shopTagService.selectByTagCode(Constants.BPSHARE);
                List<ShopTagRel> shopTagRelList = null;
                if (shopTag!=null) {
                    shopTagRelList = shopTagRelService.selectByShopIdAndTagId(shopId, shopTag.getId());
                }
                if (!CollectionUtils.isEmpty(shopTagRelList)){
                    return true;
                }
            } catch (Exception e) {
                log.error("【识别门店类型异常】",e);
            }
        }
        return false;
    }

    /**
     * 是否使用新的预检单/综合维修
     * 条件：如果可以委托、使用车间或者是钣喷中心
     * @param shopId
     * @return false 不是，true 是
     */
    @Override
    public boolean isUseNewPrecheck(HttpServletRequest request, Long shopId) {
        if(shopId == null){
            return false;
        }
        boolean isBpShare = isBpShare(request, shopId);
        if(isBpShare){
            return true;
        }
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            Integer joinStatus = shop.getJoinStatus();
            Integer workshopStatus = shop.getWorkshopStatus();
            if (ShopJoinStatusEnum.YES.getCode().equals(joinStatus) || ShopWorkshopStatusEnum.YES.getCode().equals(workshopStatus)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取门店安心保险选择的模式
     * @param request
     * @param userGlobalId
     * @return
     */
    @Override
    public String getAnxinInsuranceModel(HttpServletRequest request,String userGlobalId) {
        String anxinInsuranceModel = "";
        if(StringUtils.isBlank(userGlobalId) || "0".equals(userGlobalId)){
            return anxinInsuranceModel;
        }
        if(request == null){
            //登录前的请求
            try {
                //调用insurance查看门店配置的选项
                Result<List<InsuranceShopConfigDTO>> selectShopConfigResult = rpcInsuranceShopConfigService.selectShopConfigByShopId(Integer.parseInt(userGlobalId));
                if(selectShopConfigResult.isSuccess()){
                    List<InsuranceShopConfigDTO> insuranceShopConfigDTOList = selectShopConfigResult.getData();
                    boolean modelOne = false;
                    boolean modelTwo = false;
                    for(InsuranceShopConfigDTO insuranceShopConfigDTO : insuranceShopConfigDTOList){
                        Integer cooperationMode = insuranceShopConfigDTO.getCooperationMode();
                        if(cooperationMode.equals(Constants.ANXIN_INSURANCE_ONE)){
                            //奖励金模式
                            modelOne = true;
                        }else if(cooperationMode.equals(Constants.ANXIN_INSURANCE_TWO)){
                            //买保险送服务包模式
                            modelTwo = true;
                        }else if(cooperationMode.equals(Constants.ANXIN_INSURANCE_THRESS)){
                            //买服务包送保险模式
                            modelTwo = true;
                        }
                    }
                    if(modelOne && modelTwo){
                        //模式1，2
                        return Constants.ANXIN_INSURANCE_MODEL_ZERO;
                    }else if(modelOne && !modelTwo){
                        //模式1
                        return Constants.ANXIN_INSURANCE_MODEL_ONE;
                    } else if(!modelOne && modelTwo){
                        //模式2
                        return Constants.ANXIN_INSURANCE_MODEL_TWO;
                    }
                }
            } catch (Exception e) {
                log.error("【dubbo:安心保险】调用insurance的dubbo接口获取门店选择的模式：出现异常",e);
            }
            return anxinInsuranceModel;
        }
        //登录后
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        if(cookie == null){
            return anxinInsuranceModel;
        }
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            String key = cookie.getValue();
            anxinInsuranceModel = jedis.hget(key, Constants.SESSION_ANXIN_INSURANCE_MODEL);
            if(StringUtils.isBlank(anxinInsuranceModel)){
                return "";
            }
        } catch (Exception e) {
            log.error("redis获取门店安心保险选择的模式，出现异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return anxinInsuranceModel;
    }

    private Set<String> getOwnFuncNameSet(UserInfo userInfo){
        Set<String> ownFuncNameSet = new HashSet<>();
        String userRoleFunc = userInfo.getUserRoleFunc();
        if(StringUtils.isBlank(userRoleFunc)){
            return ownFuncNameSet;
        }
        List<FuncF> funcFList = null;
        try {
            funcFList = new Gson().fromJson(userRoleFunc, new TypeToken<List<FuncF>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            log.error("【校验角色是否有某个权限】，json解析有误，出现异常,权限为{}", userRoleFunc, e);
            return ownFuncNameSet;
        }
        if (CollectionUtils.isEmpty(funcFList)) {
            return ownFuncNameSet;
        }
        for (FuncF funcF : funcFList) {
            ownFuncNameSet.add(funcF.getName());
        }
        return ownFuncNameSet;
    }

    @Override
    public boolean checkFuncAnd(UserInfo userInfo, String... funcNames){
        Assert.notNull(userInfo, "登录用户信息为空");
        Assert.notEmpty(funcNames, "权限名称不能为空");
        if (userInfo.getUserIsAdmin() != null && userInfo.getUserIsAdmin().intValue() == 1) {
            return true;
        }
        Set<String> ownFuncNameSet = getOwnFuncNameSet(userInfo);
        for (String funcName : funcNames) {
            if (!ownFuncNameSet.contains(funcName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkFuncOr(UserInfo userInfo, String... funcNames) {
        Assert.notNull(userInfo, "登录用户信息为空");
        Assert.notEmpty(funcNames, "权限名称不能为空");
        if (userInfo.getUserIsAdmin() != null && userInfo.getUserIsAdmin().intValue() == 1) {
            return true;
        }
        Set<String> ownFuncNameSet = getOwnFuncNameSet(userInfo);
        for (String funcName : funcNames) {
            if (ownFuncNameSet.contains(funcName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询数据是否存在
     *
     * @param request
     * @param shopId
     * @param shopTagCodeEnum
     * @return
     */
    public boolean hasTagName(HttpServletRequest request, Long shopId, ShopTagCodeEnum shopTagCodeEnum) {
        String tagCode = shopTagCodeEnum.getTagCode();
        if (request == null) {
            //登录前的请求
            ShopTag shopTag = shopTagService.selectByTagCode(tagCode);
            if (shopTag == null) {
                return false;
            }
            Long tagId = shopTag.getId();
            List<ShopTagRel> shopTagRelList = shopTagRelService.selectByShopIdAndTagId(shopId, tagId);
            if (CollectionUtils.isEmpty(shopTagRelList)) {
                return false;
            }
            return true;
        }
        //登录后
        Cookie cookie = CookieUtils.getCookieByName(request, Constants.SESSION_UUID);
        if (cookie == null) {
            return false;
        }
        Jedis jedis = JedisPoolUtils.getSlaveJedis();
        try {
            String key = cookie.getValue();
            String hasTagName = jedis.hget(key, tagCode);
            if (StringUtils.isBlank(hasTagName) || hasTagName.equals("false")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("redis获取门店打标信息，出现异常", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return false;
    }

    @Override
    public boolean isYBD(Long shopId) {
        return hasTagName(null, shopId, ShopTagCodeEnum.YBD);
    }
}
