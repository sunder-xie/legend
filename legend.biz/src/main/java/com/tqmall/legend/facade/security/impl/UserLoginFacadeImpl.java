package com.tqmall.legend.facade.security.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.TechnicianService;
import com.tqmall.legend.biz.pvg.IPvgRoleService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopVersionConfigService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.biz.warehouse.WarehouseShareShopContactService;
import com.tqmall.legend.biz.websocket.base.RedisPublish;
import com.tqmall.legend.cache.CacheConstants;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.pvg.PvgRole;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.ShopVersionConfig;
import com.tqmall.legend.enums.shop.ShopAgreementStatusEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.enums.shop.ShopStatusEnum;
import com.tqmall.legend.enums.shop.ShopTagCodeEnum;
import com.tqmall.legend.enums.websocket.ChannelsEnum;
import com.tqmall.legend.facade.insurance.AnxinInsuranceVirtualFlowFacade;
import com.tqmall.legend.facade.security.UserLoginFacade;
import com.tqmall.legend.facade.shop.GrayReleaseFacade;
import com.tqmall.legend.facade.shop.ShopFacade;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.object.param.security.LoginParam;
import com.tqmall.legend.object.result.security.PvgRoleDTO;
import com.tqmall.legend.object.result.security.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zsy on 16/8/11.
 * 用户登录
 */
@Service
@Slf4j
public class UserLoginFacadeImpl implements UserLoginFacade {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private IPvgUserOrgService pvgUserOrgService;
    @Autowired
    private ShopFacade shopFacade;
    @Autowired
    private IPvgRoleService pvgRoleService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private FuncService funcService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private RedisPublish redisPublish;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private AnxinInsuranceVirtualFlowFacade anxinInsuranceVirtualFlowFacade;
    @Autowired
    private ShopPrintConfigService shopPrintConfigService;
    @Autowired
    private WarehouseShareShopContactService warehouseShareShopContactService;
    @Autowired
    private GrayReleaseFacade grayReleaseFacade;

    /**
     * 正常登陆接口
     *
     * @param loginParam 登录参数
     * @return
     */
    @Override
    public Result<UserInfoDTO> loginForApp(LoginParam loginParam) {
        log.info("[APP登录] 用户登录. loginParam:{}", loginParam);

        //step 1 参数校验
        Result paramCheckResult = loginParamCheckForApp(loginParam);
        if (!paramCheckResult.isSuccess()) {
            return Result.wrapErrorResult("", paramCheckResult.getMessage());
        }
        //step 2 登录信息校验
        Result<ShopManagerLogin> shopManagerLoginResult = checkLoginForApp(loginParam);
        if (!shopManagerLoginResult.isSuccess()) {
            return Result.wrapErrorResult(shopManagerLoginResult.getCode(), shopManagerLoginResult.getMessage());
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginResult.getData();

        //step 3 获取店铺信息
        Shop shop = shopService.selectById(shopManagerLogin.getShopId());
        if (null == shop) {
            log.error("[APP登录] 获取店铺信息失败. shopId:{}", shopManagerLogin.getShopId());
            return Result.wrapErrorResult("", "内部错误");
        }
        //pc和app公共的校验
        try {
            checkSameLoginForAppAndPC(shop);
        } catch (BizException e) {
            log.error("[APP登录]出现异常", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e) {
            log.error("[APP登录]出现异常", e);
            return Result.wrapErrorResult("", "用户登录异常,请稍后再试");
        }

        //step 4 是否是档口门店判断
        Boolean isTqmallVersion = false; //是否档口店
        Boolean isAgreeProtocol = false; //是否同意协议
        if (shop.getLevel() != null && shop.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)) {
            isTqmallVersion = true;
            if (shop.getAgreementStatus() != null && ShopAgreementStatusEnum.YES.getCode().compareTo(shop.getAgreementStatus()) == 0) {
                isAgreeProtocol = true;
            }
        }

        //step 5 档口门店 是否同意协议判断
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAgreeProtocol(isAgreeProtocol);
        userInfoDTO.setTqmallVersion(isTqmallVersion);
        userInfoDTO.setAccount(shopManagerLogin.getAccount());
        userInfoDTO.setShopId(shop.getId());
        userInfoDTO.setShopLevel(shop.getLevel());
        userInfoDTO.setShopStatus(shop.getShopStatus());
        userInfoDTO.setShopName(shop.getName());
        userInfoDTO.setUserGlobalId(shop.getUserGlobalId());
        userInfoDTO.setShopManagerLoginId(shopManagerLogin.getId());
        userInfoDTO.setManagerId(shopManagerLogin.getManagerId());
        userInfoDTO.setPassword(shopManagerLogin.getPassword().toUpperCase());
        if (isTqmallVersion && !isAgreeProtocol) {
            log.info("[APP登录] 当前登录档口客户未签订协议. 返回信息:{}", userInfoDTO);
            return Result.wrapSuccessfulResult(userInfoDTO);
        }

        //step 6 获取用户基本信息
        ShopManager shopManager = shopManagerService.selectById(shopManagerLogin.getManagerId());
        if (null == shopManager) {
            log.error("[APP登录] 获取登录用户信息失败. shopId:{},shopManagerId:{}", shop.getId(), shopManagerLogin.getManagerId());
            return Result.wrapErrorResult("", "获取用户信息失败");
        }
        userInfoDTO.setName(shopManager.getName());
        userInfoDTO.setNickName(shopManager.getNickName());
        userInfoDTO.setIsAdmin(shopManager.getIsAdmin());
        userInfoDTO.setRoleId(shopManager.getRoleId());
        userInfoDTO.setIdentityCard(shopManager.getIdentityCard());

        //step 7 获取用户角色
        List<PvgRoleDTO> pvgRoleDTOs = new ArrayList<>();
        Optional<List<RoleInfoResp>> roleInfoRespListOptional = pvgUserOrgService.getReferRoleList(shopManager.getId().intValue(), shopManager.getShopId());
        if (roleInfoRespListOptional.isPresent()) {
            for (RoleInfoResp roleInfoResp : roleInfoRespListOptional.get()) {
                PvgRoleDTO pvgRoleDTO = new PvgRoleDTO();
                pvgRoleDTO.setRoleId(roleInfoResp.getRoleId());
                pvgRoleDTO.setRoleName(roleInfoResp.getRoleName());
                pvgRoleDTOs.add(pvgRoleDTO);
            }
        }
        userInfoDTO.setPvgRoleList(pvgRoleDTOs);
        log.info("[APP登录] 用户登录 正常返回. loginParam:{},normal return:{}", loginParam, userInfoDTO);
        return Result.wrapSuccessfulResult(userInfoDTO);
    }

    /**
     * app和pc公共的登录校验
     *
     * @param shop
     */
    private void checkSameLoginForAppAndPC(Shop shop) {
        //档口版需要校验门店是否过期
        Integer shopLevel = shop.getLevel();
        if (shopLevel != null) {
            Date date = shop.getExpireTime();
            if (date != null) {
                Long nowTime = new Date().getTime();
                Long expireTime = date.getTime();
                if (Long.compare(nowTime, expireTime) == 1) {
                    throw new BizException("您的服务时间已到期，续费请联系客服电话：400-9937-288");
                }
            }
        }
    }

    /**
     * 同意协议
     *
     * @param loginParam 登录参数
     * @return
     */
    @Override
    public Result<UserInfoDTO> agreeProtocolForApp(LoginParam loginParam) {

        //step 1 参数校验
        Result paramCheckResult = loginParamCheckForApp(loginParam);
        if (!paramCheckResult.isSuccess()) {
            return Result.wrapErrorResult("", paramCheckResult.getMessage());
        }

        //step 2 登录信息校验
        Result<ShopManagerLogin> shopManagerLoginResult = checkLoginForApp(loginParam);
        if (!shopManagerLoginResult.isSuccess()) {
            return Result.wrapErrorResult(shopManagerLoginResult.getCode(), shopManagerLoginResult.getMessage());
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginResult.getData();

        //step 3 获取店铺信息
        Shop shop = shopService.selectById(shopManagerLogin.getShopId());
        if (null == shop) {
            log.error("[APP档口客户同意协议] 获取店铺信息失败. shopId:{}", shopManagerLogin.getShopId());
            return Result.wrapErrorResult("", "内部错误");
        }

        //step 4 是否是档口门店判断
        Boolean isTqmallVersion = false; //是否档口店
        Boolean isAgreeProtocol = false; //是否同意协议
        if (shop.getLevel() != null && shop.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)) {
            isTqmallVersion = true;
        }
        if (isTqmallVersion) {
            //step 5 同意协议
            com.tqmall.legend.common.Result<Boolean> agreeResult = shopFacade.agree(shopManagerLogin.getShopId(), new UserInfo());
            if (!agreeResult.isSuccess()) {
                log.info("[APP档口客户同意协议] 档口客户同意协议失败. loginParam:{},msg:{}", loginParam, agreeResult.getErrorMsg());
                return Result.wrapErrorResult("", "同意协议失败");
            }
            isAgreeProtocol = true;
        } else {
            log.info("[APP档口客户同意协议] 非档口店不需要同意协议. shopId:{}", shop.getId());
            return Result.wrapErrorResult("", "非档口店不需要同意协议");
        }


        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAgreeProtocol(isAgreeProtocol);
        userInfoDTO.setTqmallVersion(isTqmallVersion);
        userInfoDTO.setAccount(shopManagerLogin.getAccount());
        userInfoDTO.setShopId(shop.getId());
        userInfoDTO.setShopLevel(shop.getLevel());
        userInfoDTO.setShopStatus(shop.getShopStatus());
        userInfoDTO.setShopName(shop.getName());
        userInfoDTO.setUserGlobalId(shop.getUserGlobalId());
        userInfoDTO.setShopManagerLoginId(shopManagerLogin.getId());
        userInfoDTO.setManagerId(shopManagerLogin.getManagerId());
        userInfoDTO.setPassword(shopManagerLogin.getPassword().toUpperCase());
        //step 6 获取用户基本信息
        ShopManager shopManager = shopManagerService.selectById(shopManagerLogin.getManagerId());
        if (null == shopManager) {
            log.error("[APP档口客户同意协议] 获取登录用户信息失败. shopId:{},shopManagerId:{}", shop.getId(), shopManagerLogin.getManagerId());
            return Result.wrapErrorResult("", "获取用户信息失败");
        }
        userInfoDTO.setName(shopManager.getName());
        userInfoDTO.setNickName(shopManager.getNickName());
        userInfoDTO.setIsAdmin(shopManager.getIsAdmin());
        userInfoDTO.setRoleId(shopManager.getRoleId());
        userInfoDTO.setIdentityCard(shopManager.getIdentityCard());

        //step 7 获取用户角色
        List<PvgRoleDTO> pvgRoleDTOs = new ArrayList<>();
        Optional<List<RoleInfoResp>> roleInfoRespListOptional = pvgUserOrgService.getReferRoleList(shopManager.getId().intValue(), shopManager.getShopId());
        if (roleInfoRespListOptional.isPresent()) {
            for (RoleInfoResp roleInfoResp : roleInfoRespListOptional.get()) {
                PvgRoleDTO pvgRoleDTO = new PvgRoleDTO();
                pvgRoleDTO.setRoleId(roleInfoResp.getRoleId());
                pvgRoleDTO.setRoleName(roleInfoResp.getRoleName());
                pvgRoleDTOs.add(pvgRoleDTO);
            }
        }
        userInfoDTO.setPvgRoleList(pvgRoleDTOs);
        log.info("[APP登录] 用户登录 正常返回. loginParam:{},normal return:{}", loginParam, userInfoDTO);
        return Result.wrapSuccessfulResult(userInfoDTO);
    }

    /**
     * 登录校验
     *
     * @param loginParam 用户登录信息
     * @return
     */
    private Result<ShopManagerLogin> checkLoginForApp(LoginParam loginParam) {
        List<ShopManagerLogin> shopManagerLoginLogin = userInfoService.getLoginInfo(loginParam.getAccount());
        if (CollectionUtils.isEmpty(shopManagerLoginLogin)) {
            log.info("[App登录] 获取用户登录信息失败,当前账户不存在. account:{}", loginParam.getAccount());
            return Result.wrapErrorResult("", "当前账户不存在");
        }
        int loginLength = 0;
        List<ShopManagerLogin> loginList = Lists.newArrayList();
        if (loginParam.isCheckPassword()) {
            String password = loginParam.getPassword();
            for (ShopManagerLogin shopManagerLogin : shopManagerLoginLogin) {
                if (StringUtils.equalsIgnoreCase(password, shopManagerLogin.getPassword())) {
                    loginLength++;
                    loginList.add(shopManagerLogin);
                }
            }
            if (loginLength == 0) {
                log.info("[App登录] 用户登录密码错误. loginParam:{}", loginParam);
                return Result.wrapErrorResult("", "登录密码错误");
            }
        } else {
            //不需要校验密码
            for (ShopManagerLogin shopManagerLogin : shopManagerLoginLogin) {
                loginList.add(shopManagerLogin);
            }
            loginLength = shopManagerLoginLogin.size();
        }
        if (loginLength == 1) {
            return Result.wrapSuccessfulResult(loginList.get(0));
        }
        if (loginLength > 1) {
            //一个账户多个手机号，需要传shopId
            Long shopId = loginParam.getShopId();
            if (shopId != null) {
                for (ShopManagerLogin shopManagerLogin : loginList) {
                    Long loginShopId = shopManagerLogin.getShopId();
                    if (Long.compare(shopId, loginShopId) == 0) {
                        return Result.wrapSuccessfulResult(shopManagerLogin);
                    }
                }
            } else {
                //存在多个账户，未选择门店，则返回特殊错误code
                return Result.wrapErrorResult(Constants.MORE_USER_INFO_CODE, "存在多个账号");
            }
        }
        return Result.wrapErrorResult("", "选择的门店有误");
    }

    /**
     * app登录参数校验
     *
     * @param loginParam 登录参数
     * @return
     */
    private Result loginParamCheckForApp(LoginParam loginParam) {
        if (null == loginParam) {
            log.info("[APP登录] 登录参数为空. loginParam:{}", loginParam);
            return Result.wrapErrorResult("", "登录参数为空");
        } else {
            if (StringUtils.isBlank(loginParam.getAccount())) {
                log.info("[APP登录] 登录账号信息为空. loginParam:{}", loginParam);
                return Result.wrapErrorResult("", "登录账号信息为空");
            }
            if (loginParam.isCheckPassword() && StringUtils.isBlank(loginParam.getPassword())) {
                log.info("[APP登录] 登录密码信息为空. loginParam:{}", loginParam);
                return Result.wrapErrorResult("", "登录密码信息为空");
            }
        }
        return Result.wrapSuccessfulResult("校验成功");
    }

    /**
     * sam免登陆
     *
     * @param userGlobalId
     * @param request
     * @param response
     * @return
     */
    @Override
    public String samAvoidLogin(String userGlobalId, HttpServletRequest request, HttpServletResponse response) {
        //过滤如果userGlobalId是0的情况
        if (StringUtils.isNotBlank(userGlobalId) && !userGlobalId.equals("0")) {
            Map<String, Object> searchShopMap = Maps.newHashMap();
            searchShopMap.put("userGlobalId", userGlobalId);
            List<Shop> shopList = shopService.select(searchShopMap);
            if (CollectionUtils.isNotEmpty(shopList)) {
                Shop shop = shopList.get(0);
                Long shopId = shop.getId();
                sameForAvoidLogin(request, response, shop, shopId, 1800);//半小时失效时间
            }
        }
        return "login_shop";
    }

    /**
     * 设置公共的信息到redis
     *
     * @param request
     * @param response
     * @param shop
     * @param shopId
     */
    private void sameForAvoidLogin(HttpServletRequest request, HttpServletResponse response, Shop shop, Long shopId, int expireTime) {
        // 查询UserId
        ShopManager shopManager = userInfoService.getShopAdminUserInfo(shopId);
        List<FuncF> list = getUserFunc(shopId, shopManager.getRoleId(), shop.getLevel());
        // 生成uuid
        String uuid = UUID.randomUUID().toString();
        Jedis jedis = putDataToRedis(shop, shopManager, list, uuid, expireTime);
        try {
            //快捷方式
            putDataToCookie(response, shopManager, uuid, expireTime);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (IOException e) {
            log.error("转发异常", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    /**
     * 管理后台绿色通道跳转到门店
     *
     * @param shopId
     * @param request
     * @param response
     * @return
     */
    @Override
    public String legendmAvoidLogin(Long shopId, HttpServletRequest request, HttpServletResponse response) {
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            sameForAvoidLogin(request, response, shop, shopId, 3600);//1小时失效
        }
        return "common/error";
    }

    /**
     * 二维码登录
     *
     * @param uuid
     * @param userId
     */
    @Override
    public void loginForPCQRCodeLogin(Long shopId, Long userId, String uuid) {
        log.info("【二维码安全登录】shopId：{}，userId：{}，uuid：{}", shopId, userId, uuid);
        Jedis slaveJedis = null;
        Jedis masterJedis = null;
        try {
            slaveJedis = JedisPoolUtils.getSlaveJedis();
            if (!slaveJedis.exists(uuid)) {
                throw new BizException("二维码已失效");
            }
            //校验uuid是否被使用
            String checkShopId = slaveJedis.hget(uuid, Constants.SESSION_SHOP_ID);
            if (StringUtils.isNotBlank(checkShopId)) {
                throw new BizException("二维码已被使用");
            }
            // 校验门店是否存在
            Shop shop = shopService.selectById(shopId);
            if (shop == null) {
                throw new BizException("门店不存在");
            }
            //校验客户是否存在
            ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
            if (shopManager == null) {
                throw new BizException("用户不存在");
            }
            Long roleId = shopManager.getRoleId();
            List<FuncF> funcFList = getUserFunc(shopId, roleId, shop.getLevel());
            // 启动jedis连接池，并向redis存储数据
            masterJedis = putDataToRedis(shop, shopManager, funcFList, uuid, CacheConstants.SECURE_QRCODE_LOGIN_KEY_EXP_TIME);
            //websocket通知pc可登陆，并让前端设置相应的cookie值
            List<String> dataList = Lists.newArrayList();
            dataList.add(uuid);
            dataList.add(shopManager.getName());
            com.tqmall.legend.common.Result<List<String>> result = new com.tqmall.legend.common.Result<List<String>>();
            result.setData(dataList);
            result.setSuccess(true);
            String message = new Gson().toJson(result);
            redisPublish.shareNotice(ChannelsEnum.LOGIN_SHARE_NOTICE.name(), message);
        } finally {
            JedisPoolUtils.returnSlaveRes(slaveJedis);
            JedisPoolUtils.returnMasterRes(masterJedis);
        }

    }

    @Override
    public void loginForPC(List<FuncF> list, ShopManager shopManager, Shop shop, HttpServletResponse response) {
        Jedis jedis = null;
        try {
            // 生成uuid
            String uuid = UUID.randomUUID() + "";
            // 启动jedis连接池，并向redis存储数据
            jedis = putDataToRedis(shop, shopManager, list, uuid, CacheConstants.SHOP_KEY_EXP_TIME);
            //快捷方式,7天失效
            putDataToCookie(response, shopManager, uuid, CacheConstants.SHOP_KEY_EXP_TIME);
        } catch (Exception e) {
            log.error("【pc密码登录】出现异常", e);
            throw new BizException("出现异常", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    @Override
    public void checkShopLevelOneLogin(Long shopId, Long userId) {
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            throw new BizException("门店不存在或关闭");
        }
        //试用云修版门店不能登录pc
        if (shop.getShopStatus().equals(ShopStatusEnum.TRY.getCode()) && ShopLevelEnum.YUNXIU.getValue().equals(shop.getLevel())) {
            throw new BizException("试用门店不能登录,请正式加盟云修");
        }
        checkSameLoginForAppAndPC(shop);
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        if (shopManager == null) {
            throw new BizException("账号不存在");
        }
    }

    /**
     * 根据登录账户获取用户信息
     *
     * @param loginAccount
     * @return
     */
    @Override
    public Result<UserInfoDTO> getUserInfoByLoginAccountForApp(String loginAccount, String password, Long chooseShopId, boolean checkPassword) {
        List<ShopManagerLogin> loginInfoList;
        if (checkPassword) {
            loginInfoList = userInfoService.getLoginInfo(loginAccount, password);
        } else {
            loginInfoList = userInfoService.getLoginInfo(loginAccount);
        }
        if (CollectionUtils.isEmpty(loginInfoList)) {
            log.info("[App登录] 获取用户登录信息失败,当前账号不存在. account:{}", loginAccount);
            return Result.wrapErrorResult("", "当前账号不存在");
        }
        int loginInfoSize = loginInfoList.size();
        ShopManagerLogin login = null;
        if (loginInfoSize == 1) {
            login = loginInfoList.get(0);
        } else {
            if (chooseShopId == null) {
                return Result.wrapErrorResult(Constants.MORE_USER_INFO_CODE, "请先选择门店");
            }
            for (ShopManagerLogin shopManagerLogin : loginInfoList) {
                Long checkShopId = shopManagerLogin.getShopId();
                if (Long.compare(checkShopId, chooseShopId) == 0) {
                    login = shopManagerLogin;
                }
            }
            if (login == null) {
                return Result.wrapErrorResult("", "此门店没有这个账号,请检查账号是否正确");
            }
        }
        Long shopId = login.getShopId();
        Shop shop = shopService.selectById(shopId);
        if (null == shop) {
            log.error("[APP登录] 获取店铺信息失败. shopId:{}", shopId);
            return Result.wrapErrorResult("", "内部错误");
        }

        Boolean isTqmallVersion = false; //是否档口店
        Boolean isAgreeProtocol = false; //是否同意协议
        if (shop.getLevel() != null && shop.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)) {
            isTqmallVersion = true;
            if (shop.getAgreementStatus() != null && ShopAgreementStatusEnum.YES.getCode().compareTo(shop.getAgreementStatus()) == 0) {
                isAgreeProtocol = true;
            }
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAgreeProtocol(isAgreeProtocol);
        userInfoDTO.setTqmallVersion(isTqmallVersion);
        userInfoDTO.setAccount(login.getAccount());
        userInfoDTO.setShopId(shop.getId());
        userInfoDTO.setShopLevel(shop.getLevel());
        userInfoDTO.setShopStatus(shop.getShopStatus());
        return Result.wrapSuccessfulResult(userInfoDTO);
    }

    /**
     * 获取用户权限列表
     *
     * @param shopId
     * @param roleId
     * @return
     */
    private List<FuncF> getUserFunc(Long shopId, Long roleId, Integer shopLevel) {
        return funcService.getFuncFsForUser(roleId, shopId, shopLevel);
    }

    /**
     * 将管理员用户信息放到cookie中
     *
     * @param response
     * @param uuid
     * @param expireTime 失效时间
     * @throws UnsupportedEncodingException
     */
    private void putDataToCookie(HttpServletResponse response, ShopManager shopManager, String uuid, int expireTime) throws UnsupportedEncodingException {
        CookieUtils.addCookie(response, Constants.SESSION_USER_NAME, StringUtils.isEmpty(shopManager.getName()) ? java.net.URLEncoder.encode("管理员", "UTF-8") : java.net.URLEncoder.encode(shopManager.getName(), "UTF-8"), CacheConstants.SHOP_KEY_EXP_TIME);
        CookieUtils.addCookie(response, Constants.SESSION_UUID, uuid, expireTime);
    }

    /**
     * 将用户信息放到redis
     *
     * @param shop
     * @param shopManager
     * @param list
     * @param uuid
     */
    private Jedis putDataToRedis(Shop shop, ShopManager shopManager, List<FuncF> list, String uuid, Integer expireTime) {
        Jedis jedis;
        jedis = JedisPoolUtils.getMasterJedis();
        Long shopId = shop.getId();
        Long userId = shopManager.getId();
        //灰度发布
        grayReleaseFacade.initRelease(shopId);
        jedis.hset(uuid, Constants.SESSION_USER_ID, String.valueOf(userId));
        jedis.hset(uuid, Constants.SESSION_USER_NAME, shopManager.getName());
        jedis.hset(uuid, Constants.SESSION_USER_ROLE, "ROLE_USER");
        jedis.hset(uuid, Constants.SESSION_USER_ROLE_ID, shopManager.getRoleId() + "");
        jedis.hset(uuid, Constants.SESSION_SHOP_ID, shopId + "");
        jedis.hset(uuid, Constants.SESSION_USER_IS_ADMIN, shopManager.getIsAdmin() + "");
        jedis.hset(uuid, Constants.SESSION_SHOP_LEVEL, shop.getLevel() + "");
        jedis.hset(uuid, Constants.SESSION_SHOP_JOIN_STATUS, shop.getJoinStatus() + "");
        jedis.hset(uuid, Constants.SESSION_SHOP_WORKSHOP_STATUS, shop.getWorkshopStatus() + "");
        String userGlobalId = shop.getUserGlobalId();
        if (StringUtils.isNotBlank(userGlobalId)) {
            jedis.hset(uuid, Constants.SESSION_USER_GLOBAL_ID, userGlobalId);
        }
        //标注店铺是否为档口版
        jedis.hset(uuid, Constants.SESSION_SHOP_IS_TQMALL_VERSION, String.valueOf(shop.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)));
        //是否是钣喷中心
        boolean isBpCenter = shopFunFacade.isBpShare(null, shopId);
        jedis.hset(uuid, Constants.BPSHARE, isBpCenter + "");
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            jedis.hset(uuid, Constants.SESSION_USER_ROLE_FUNC, new Gson().toJson(list));
        }
        //是否有库存共享权限
        boolean isWarehouseShare = warehouseShareShopContactService.isContact(userId);
        jedis.hset(uuid, Constants.SESSION_WAREHOUSE_SHARE_ROLE, isWarehouseShare + "");
        //灰度发布

        // 手机号
        jedis.hset(uuid, Constants.SESSION_MOBILE, shopManager.getMobile());
        // 查询角色
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("userId", userId);
        searchMap.put("shopId", shopId);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.select(searchMap);
        if (!org.springframework.util.CollectionUtils.isEmpty(pvgUserOrgList)) {
            PvgUserOrg pvgUserOrg = pvgUserOrgList.get(0);
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            PvgRole pvgRole = pvgRoleService.findPvgRoleById(pvgRoleId);
            if (pvgRole != null) {
                jedis.hset(uuid, Constants.SESSION_PVGROLE_NAME, pvgRole.getName());
            }
        }
        // 是否认证
        com.tqmall.legend.common.Result result = technicianService.getTechnician(shopId, userId);
        if (result.isSuccess() && null != result.getData()) {
            jedis.hset(uuid, Constants.SESSION_READYCHECK, "true");
        }

        //设置失效时间
        //安心保险选择模式
        String anxinInsuranceModel = shopFunFacade.getAnxinInsuranceModel(null, userGlobalId);
        jedis.hset(uuid, Constants.SESSION_ANXIN_INSURANCE_MODEL, anxinInsuranceModel);

        //获取门店的开启单据配置 1-状态开启
        List<ShopPrintConfig> printConfigList = shopPrintConfigService.getShopPrintConfigs(shopId, 1);
        if (CollectionUtils.isNotEmpty(printConfigList)) {
            String printConfigJson = new Gson().toJson(printConfigList);
            jedis.hset("" + shopId, Constants.SHOP_OPEN_PRINT, printConfigJson);
        }

        //设置该城市是否参加保险活动
        if (shop.getCity() != null) {
            Boolean isOpen = anxinInsuranceVirtualFlowFacade.isInsuranceAvaiableInRegion(shop.getCity().intValue());
            jedis.hset(uuid, Constants.SESSION_ANXIN_INSURANCE_CITY_IS_OPEN, isOpen.toString());
        }

        //设置门店标签信息，如是否是样板门店
        ShopTagCodeEnum[] shopTagCodeEnums = ShopTagCodeEnum.getShopTags();
        for (ShopTagCodeEnum shopTagCodeEnum : shopTagCodeEnums) {
            boolean hasTagName = shopFunFacade.hasTagName(null, shopId, shopTagCodeEnum);
            String field = shopTagCodeEnum.getTagCode();
            jedis.hset(uuid, field, hasTagName + "");
        }

        //设置失效时间7天
        jedis.expire(uuid, expireTime);
        return jedis;
    }
}