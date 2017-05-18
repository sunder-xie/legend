package com.tqmall.legend.server.security;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.config.ShopNetworkConfigService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import com.tqmall.legend.entity.config.ShopNetworkConfig;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.enums.config.ManagerDeviceConfigStatusEnum;
import com.tqmall.legend.enums.security.ShopLoginLevelEnum;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.enums.shop.ShopStatusEnum;
import com.tqmall.legend.facade.security.UserLoginFacade;
import com.tqmall.legend.object.param.security.ConfigDTO;
import com.tqmall.legend.object.param.security.ConfigParam;
import com.tqmall.legend.object.param.security.LoginParam;
import com.tqmall.legend.object.param.security.ManagerDeviceDTO;
import com.tqmall.legend.object.param.security.ShopNetworkConfigDTO;
import com.tqmall.legend.object.result.security.UserInfoDTO;
import com.tqmall.legend.service.security.RpcLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/8/11.
 */
@Service("rpcLoginService")
@Slf4j
public class RpcLoginServiceImpl implements RpcLoginService {

    @Autowired
    private UserLoginFacade userLoginFacade;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private ShopManagerDeviceConfigService shopManagerDeviceConfigService;
    @Autowired
    private WhiteAddressService whiteAddressService;
    @Autowired
    private ShopNetworkConfigService shopNetworkConfigService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private IPvgUserOrgService iPvgUserOrgService;

    /**
     * 商家app登陆接口
     *
     * @param loginParam loginParam 登录参数
     * @return
     */
    @Override
    public Result<UserInfoDTO> loginForApp(LoginParam loginParam) {
        return userLoginFacade.loginForApp(loginParam);
    }

    /**
     * 查询一个手机号多个账户接口
     *
     * @param loginParam
     * @return
     */
    @Override
    public Result<List<UserInfoDTO>> getMoreLoginUserInfo(final LoginParam loginParam) {
        return new ApiTemplate<List<UserInfoDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String account = loginParam.getAccount();
                String password = loginParam.getPassword();
                //数据校验
                Assert.notNull(account,"账户不能为空");
                if (loginParam.isCheckPassword()) {
                    Assert.notNull(password, "密码不能为空");
                }
            }

            @Override
            protected List<UserInfoDTO> process() throws BizException {
                List<UserInfoDTO> userInfoDTOList = Lists.newArrayList();
                boolean checkPassword = loginParam.isCheckPassword();
                List<ShopManagerLogin> shopManagerLoginList;
                if(checkPassword){
                    shopManagerLoginList = userInfoService.getLoginInfo(loginParam.getAccount(),loginParam.getPassword());
                } else {
                    shopManagerLoginList = userInfoService.getLoginInfo(loginParam.getAccount());
                }
                if(CollectionUtils.isEmpty(shopManagerLoginList)){
                    return userInfoDTOList;
                }
                List<Long> shopIds = Lists.newArrayList();
                for(ShopManagerLogin shopManagerLogin : shopManagerLoginList){
                    shopIds.add(shopManagerLogin.getShopId());
                }
                Map<String,Object> searchMap = Maps.newHashMap();
                searchMap.put("shopIds",shopIds);
                List<Shop> shopList = shopService.select(searchMap);
                for(Shop shop : shopList){
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setShopId(shop.getId());
                    userInfoDTO.setShopName(shop.getName());
                    userInfoDTOList.add(userInfoDTO);
                }
                return userInfoDTOList;
            }
        }.execute();
    }

    /**
     * 商家app同意协议接口
     *
     * @param loginParam loginParam 入参
     * @return
     */
    @Override
    public Result<UserInfoDTO> agreeProtocolForApp(LoginParam loginParam) {
        return userLoginFacade.agreeProtocolForApp(loginParam);
    }

    /**
     * 二维码 扫码校验
     *
     * @param configParam
     * @return boolean true:扫码校验成功 false:扫码校验失败
     */
    @Override
    public Result<Boolean> scanCode(ConfigParam configParam) {
        //uuid shopId userId 设备id
        Long shopId = configParam.getShopId();
        String uuid = configParam.getUuid();
        Long userId = configParam.getUserId();
        if (null == shopId || null == userId || StringUtils.isBlank(uuid)) {
            return Result.wrapErrorResult("", "参数错误:shopId:" + shopId + ",userId:" + userId + ",uuid:" + uuid);
        }
        try {
            //level = 6 shopStatus = 4
            Shop shop = shopService.selectById(shopId);
            if (shop.getShopStatus().equals(ShopStatusEnum.TRY.getCode()) && ShopLevelEnum.YUNXIU.getValue().equals(shop.getLevel())) {
                return Result.wrapErrorResult("", "试用门店不能登录,请正式加盟云修");
            }
            userLoginFacade.loginForPCQRCodeLogin(shopId, userId, uuid);
        } catch (BizException e) {
            log.error("[app扫码确认登陆失败]", e);
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e){
            log.error("[app扫码确认登陆失败]", e);
            return Result.wrapErrorResult("", "app扫码确认登陆失败,请联系相关人员");
        }
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 根据不同的安全登陆等级 处理不同的校验:时间,设备,网络环境
     *
     * @param configParam
     * @return boolean:true 校验成功 false:校验失败 未授权的设备需要返回code
     */
    @Override
    public Result<ConfigDTO> levelCheck(ConfigParam configParam) {
        //shopId userId 设备id mac地址 IP地址
        Long shopId = configParam.getShopId();
        Long userId = configParam.getUserId();
        ConfigDTO configDTO = new ConfigDTO();
        //admin都不用校验
        ShopManager manager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        if (manager == null) {
            return Result.wrapErrorResult("", "账户不存在");
        }
        if (manager.getIsAdmin() == 1) {
            return Result.wrapSuccessfulResult(configDTO);
        }
        //校验登陆时间
        Date now = new Date();
        WhiteAddress temp = whiteAddressService.getWhiteAddressInfo(userId, shopId);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        WhiteAddress whiteAddress = whiteAddressService.getWhiteAddressInfo(userId, shopId, time.format(now));
        if (null != temp && null == whiteAddress) {
            configDTO.setTime(1);
        }
        //返回登陆时间
        if (null != temp) {
            configDTO.setLoginBeginTime(temp.getLoginBeginTime());
            configDTO.setLoginEndTime(temp.getLoginEndTime());
        }
        //距离登录时间计算
        if (configDTO.getTime().compareTo(1) == 0) {
            Long lastTime = computeLastTime(configDTO.getLoginBeginTime(), configDTO.getLoginEndTime(), now);
            configDTO.setLastTime(lastTime);
        }
        String level = shopConfigureService.getShopConfigure(shopId, Integer.valueOf(ShopConfigureTypeEnum.SHOPSECURITYLEVEL.getCode()), "");
        if (ShopLoginLevelEnum.LEVEL_ONE.getLevel().equals(level)) {
            return Result.wrapSuccessfulResult(configDTO);
        }
        //校验登陆设备
        String deviceId = configParam.getDeviceId();
        if (StringUtils.isBlank(deviceId)) {
            configDTO.setDevice(ManagerDeviceConfigStatusEnum.AUTHOR_UNBUND.getCode());
        } else {
            ShopManagerDeviceConfig config = shopManagerDeviceConfigService.getShopManagerDevice(userId, deviceId);
            if (null == config) {
                configDTO.setDevice(ManagerDeviceConfigStatusEnum.AUTHOR_UNBUND.getCode());
            } else {
                configDTO.setDevice(config.getAuthorizeStatus());
            }
        }
        if (ShopLoginLevelEnum.LEVEL_TWO.getLevel().equals(level)) {
            return Result.wrapSuccessfulResult(configDTO);
        }
        //校验网络环境
        String macAddress = configParam.getMacAddress();
        String ipAddress = configParam.getIpAddress();
        if (StringUtils.isBlank(macAddress) || StringUtils.isBlank(ipAddress)) {
            //macAddress 或ipAddress为空 返回最好还是true  但network = 1
            configDTO.setNetwork(1);
            return Result.wrapSuccessfulResult(configDTO);
        }
        ShopNetworkConfig networkConfig = shopNetworkConfigService.getShopNetworkConfig(shopId, macAddress);
        if (null == networkConfig) {
            configDTO.setNetwork(1);
            return Result.wrapSuccessfulResult(configDTO);
        }
        return Result.wrapSuccessfulResult(configDTO);
    }

    /**
     * 安全登陆体系 新设备提交授权申请
     *
     * @param configParam
     * @return
     */
    @Override
    public Result<Boolean> authoriza(ConfigParam configParam) {
        //shopId userId 设备id
        Long shopId = configParam.getShopId();
        String deviceId = configParam.getDeviceId();
        Long userId = configParam.getUserId();
        String phoneBrand = configParam.getPhoneBrand();
        ShopManagerDeviceConfig config = shopManagerDeviceConfigService.getShopManagerDevice(userId, deviceId);
        if (null == config) {
            config = new ShopManagerDeviceConfig();
            config.setShopId(shopId);
            config.setDeviceId(deviceId);
            config.setAuthorizeStatus(ManagerDeviceConfigStatusEnum.AUTHOR_DOING.getCode());//未授权
            config.setPhoneBrand(phoneBrand);
            config.setManagerId(userId);
            shopManagerDeviceConfigService.insertShopManagerDevice(config);
        } else {
            Long configId = config.getId();
            shopManagerDeviceConfigService.updateDeviceConfigStatus(configId, userId, 0);
        }
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public Result<List<ShopNetworkConfigDTO>> getNetConfigList(Long shopId) {
        if (null == shopId) {
            return Result.wrapErrorResult("", "参数错误:shopId" + shopId);
        }
        List<ShopNetworkConfig> configList = shopNetworkConfigService.getShopNetworkConfigs(shopId);
        List<ShopNetworkConfigDTO> dtoList = new ArrayList<>();
        for (ShopNetworkConfig config : configList) {
            ShopNetworkConfigDTO dto = new ShopNetworkConfigDTO();
            try {
                BeanUtils.copyProperties(dto, config);
            } catch (Exception e) {
                log.error("属性赋值失败:", e);
                continue;
            }
            dtoList.add(dto);
        }
        return Result.wrapSuccessfulResult(dtoList);
    }

    /**
     * @param configParam
     * @return
     */
    @Override
    public Result<Boolean> insertNetWork(ConfigParam configParam) {
        if (null == configParam) {
            return Result.wrapErrorResult("", "参数错误:configParam为空");
        }
        Long shopId = configParam.getShopId();
        Long userId = configParam.getUserId();
        String macAddress = configParam.getMacAddress();
        String ipAddress = configParam.getIpAddress();
        String wifiName = configParam.getWifiName();
        ShopNetworkConfig temp = shopNetworkConfigService.getShopNetworkConfig(shopId, macAddress);
        if (null != temp) {
            log.error("[添加门店网络环境信息失败]macAddress已存在,macAddress:{},shopId{}", macAddress, shopId);
            return Result.wrapErrorResult("", "插入失败,门店macAddress已存在");
        }
        ShopNetworkConfig shopNetworkConfig = new ShopNetworkConfig();
        shopNetworkConfig.setShopId(shopId);
        shopNetworkConfig.setIpAddress(ipAddress);
        shopNetworkConfig.setWifiName(wifiName);
        shopNetworkConfig.setMacAddress(macAddress);
        shopNetworkConfig.setModifier(userId);
        shopNetworkConfig.setCreator(userId);
        shopNetworkConfigService.insertNetConfig(shopNetworkConfig);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 删除网络配置环境
     *
     * @param configParam
     * @return
     */
    @Override
    public Result<Boolean> deleteNetWork(ConfigParam configParam) {
        Long configId = configParam.getConfigId();
        Long shopId = configParam.getShopId();
        if (null == shopId || null == configId) {
            return Result.wrapErrorResult("", "参数错误:shopId:" + shopId + ",configId:" + configId);
        }
        shopNetworkConfigService.deleteNetConfig(configId, shopId);
        return Result.wrapSuccessfulResult(true);
    }

    @Override
    public Result getDeviceDetail(String account,String password, String deviceId, Long chooseShopId, boolean checkPassword) {
        List<ShopManagerLogin> loginInfoList;
        if (checkPassword) {
            loginInfoList = userInfoService.getLoginInfo(account, password);
        } else {
            loginInfoList = userInfoService.getLoginInfo(account);
        }
        if (CollectionUtils.isEmpty(loginInfoList)) {
            return Result.wrapErrorResult("", "账号没有对应的数据,account:" + account);
        }
        int loginInfoSize = loginInfoList.size();
        ShopManagerLogin login = null;
        if(loginInfoSize == 1){
            login = loginInfoList.get(0);
        } else {
            if(chooseShopId == null){
                return Result.wrapErrorResult(Constants.MORE_USER_INFO_CODE, "请先选择门店");
            }
            for(ShopManagerLogin shopManagerLogin : loginInfoList){
                Long checkShopId = shopManagerLogin.getShopId();
                if(Long.compare(checkShopId,chooseShopId) == 0){
                    login = shopManagerLogin;
                }
            }
            if(login == null){
                return Result.wrapErrorResult("", "此门店没有这个账号,请检查账号是否正确");
            }
        }
        Long shopId = login.getShopId();
        Long userId = login.getManagerId();
        ShopManager manager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, userId);
        ManagerDeviceDTO deviceDTO = new ManagerDeviceDTO();
        deviceDTO.setShopId(shopId);
        deviceDTO.setUserId(userId);
        deviceDTO.setUserPhotoUrl(manager.getUserPhotoUrl());
        deviceDTO.setName(manager.getName());
        Optional<List<RoleInfoResp>> roles = iPvgUserOrgService.getReferRoleList(new Integer(userId + ""), shopId);
        if (roles.isPresent()){
            List<RoleInfoResp> roleInfoResps = roles.get();
            StringBuffer stringBuffer = new StringBuffer();
            for (RoleInfoResp roleInfoResp : roleInfoResps) {
                stringBuffer.append(roleInfoResp.getRoleName()).append(",");
            }
            if (stringBuffer.length() > 0){
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            }
            deviceDTO.setRoleName(stringBuffer.toString());
        }
        ShopManagerDeviceConfig device = shopManagerDeviceConfigService.getShopManagerDevice(userId, deviceId);
        if (null != device) {
            deviceDTO.setDeviceStatus(device.getAuthorizeStatus());
        } else {
            deviceDTO.setDeviceStatus(ManagerDeviceConfigStatusEnum.AUTHOR_UNBUND.getCode());
        }
        return Result.wrapSuccessfulResult(deviceDTO);
    }

    /**
     * 根据登录账户获取用户信息
     *
     * @param loginAccount 登录账户
     *
     * @return
     */
    @Override
    public Result<UserInfoDTO> getUserInfoByLoginAccountForApp(String loginAccount,String password, Long chooseShopId,boolean checkPassword) {
        return userLoginFacade.getUserInfoByLoginAccountForApp(loginAccount,password, chooseShopId,checkPassword);
    }

    /**
     * 计算时间距离现在差值(毫秒)
     *
     * @param beginTime HH:mm:ss
     * @param endTime   HH:mm:ss
     * @param now
     * @return
     */
    private Long computeLastTime(String beginTime, String endTime, Date now) {
        String today = DateUtil.convertDateToYMD(now);
        String startDate = today + " " + beginTime;
        String endDate = today + " " + endTime;
        Long lastToEnd = DateUtil.getSubMilliSecond(endDate, now);
        if (lastToEnd.compareTo(0L) < 0) {
            today = DateUtil.addOneDay(now);
            startDate = today + " " + beginTime;
        }
        Long lastTime = DateUtil.getSubMilliSecond(startDate, now);
        if (lastTime.compareTo(0L) < 0) {
            lastTime = 0L;
        }
        return lastTime;
    }
}
