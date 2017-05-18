package com.tqmall.legend.biz.privilege.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.vo.ShopManagerAndLoginVo;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.biz.util.PasswordUtil;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.privilege.ShopManagerLoginDao;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.entity.view.AddUser;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.pojo.ShopManagerCom;
import com.tqmall.magic.object.result.workshop.LineProcessManagerDTO;
import com.tqmall.magic.service.workshop.RpcLineProcessManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by QXD on 2014/10/30.
 */
@Slf4j
@Service
public class ShopManagerServiceImpl implements ShopManagerService {
    public final static Logger LOGGER = LoggerFactory.getLogger(ShopManagerServiceImpl.class);
    @Autowired
    private ShopManagerDao shopManagerDao;
    @Autowired
    private ShopManagerLoginDao shopManagerLoginDao;
    @Autowired
    private IPvgUserOrgService iPvgUserOrgService;
    @Autowired
    private CacheComponent<List<ShopManager>> cacheComponent;
    @Autowired
    private WhiteAddressService whiteAddressService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private ShopManagerFacade shopManagerFacade;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RpcLineProcessManagerService rpcLineProcessManagerService;
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;

    @Override
    public ShopManager selectById(long id) {
        return shopManagerDao.selectById(id);
    }

    @Override
    public List<ShopManager> selectByIds(Long[] shopManagerIds) {
        List<ShopManager> shopManagerList = null;
        try {
            shopManagerList = shopManagerDao.selectByIds(shopManagerIds);
            if (CollectionUtils.isEmpty(shopManagerList)) {
                shopManagerList = new ArrayList<ShopManager>();
            }
        } catch (Exception e) {
            LOGGER.error("batch query shopmanage exception:{}", e.toString());
            shopManagerList = new ArrayList<ShopManager>();
        }

        return shopManagerList;
    }

    /**
     * 根据店铺ID获取店铺的员工
     *
     * @param shopId 店铺id
     */
    @Override
    public List<ShopManager> selectByShopId(long shopId) {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("shopId", shopId);
        List<ShopManager> shopManagerList = shopManagerDao.select(searchMap);
        return shopManagerList;
    }

    /**
     * 根据shopId 和manager_id 获取员工
     *
     * @param shopId    店铺id
     * @param managerId 员工id
     */
    @Override
    public ShopManager selectByShopIdAndManagerId(long shopId, long managerId) {
        if (shopId < Constants.SHOP_ID_FLAG || managerId < Constants.MANAGER_ID_FLAG) {
            LOGGER.warn("shopId,managerId错误");
            return null;
        }
        //通过缓存查找
        List<ShopManager> list = cacheComponent.getCache(CacheKeyConstant.PICKING);
        for (ShopManager shopManager : list) {
            if (shopManager.getShopId().equals(shopId)) {
                if (shopManager.getId().equals(managerId)) {
                    return shopManager;
                }
            }
        }
        return null;
    }

    @Override
    public ShopManager selectByShopIdAndManagerIdDB(long shopId, long managerId) {
        ShopManager shopManager = shopManagerDao.selectByShopIdAndManagerId(shopId, managerId);
        return shopManager;
    }

    @Override
    public List<ShopManager> select(Map<String, Object> parameters) {
        return shopManagerDao.select(parameters);
    }

    @Override
    public Integer selectCount(Map<String, Object> parameters) {
        return shopManagerDao.selectCount(parameters);
    }

    public List<ShopManager> selectWithoutCache(Map<String, Object> parameters) {
        return shopManagerDao.select(parameters);
    }

    @Override
    public List<ShopManager> selectManagerInfoByAccount(String account) {
        List<ShopManager> shopManagerList = Lists.newArrayList();
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("account", account);
        List<ShopManagerLogin> shopManagerLoginList = shopManagerLoginDao.select(searchMap);
        if (CollectionUtils.isEmpty(shopManagerLoginList)) {
            return shopManagerList;
        }
        Integer shopManagerLoginSize = shopManagerLoginList.size();
        Long[] managerIds = new Long[shopManagerLoginSize];
        for (int i = 0; i < shopManagerLoginSize; i++) {
            ShopManagerLogin shopManagerLogin = shopManagerLoginList.get(i);
            managerIds[i] = shopManagerLogin.getManagerId();
        }
        shopManagerList = shopManagerDao.selectByIds(managerIds);
        return shopManagerList;
    }


    /**
     * 根据shopId 和managerId 获取员工的信息
     *
     * @param shopId    店铺id
     * @param managerId 被请求用户员工id
     */
    @Override
    public ShopManagerCom selectUserInfoByShopIdAndManagerId(long shopId, long managerId) {
        if (shopId < Constants.SHOP_ID_FLAG || managerId < Constants.MANAGER_ID_FLAG) {
            LOGGER.warn("shopId,managerId错误");
            return null;
        }

        ShopManagerCom shopManagerCom = new ShopManagerCom();
        try {
            shopManagerCom = shopManagerDao.selectUserInfoByShopIdAndManagerId(managerId, shopId);
        } catch (Exception e) {
            LOGGER.error("DB操作错误", e);
        }

        return shopManagerCom;
    }

    /**
     * 更新用户信息
     *
     * @param shopManager 用户信息
     * @param managerId   更新操作用户
     */
    @Override
    public Result upDataShopManager(long shopId, ShopManager shopManager, long managerId) {
        // 数据校验
        Result result = validateDataBySelf(shopId, shopManager, managerId);
        if (!result.isSuccess()) {
            return result;
        }
        ShopManager oldShopManager = shopManagerDao.selectById(shopManager.getId());
        if (oldShopManager == null) {
            return Result.wrapErrorResult("error", "用户不存在");
        }
        if (checkMobileIsExist(oldShopManager.getMobile(), shopManager.getMobile(), shopId)) {
            return Result.wrapErrorResult("exist", "该联系方式在门店中已存在,请更换联系方式");
        }

        shopManager.setShopId(shopId);
        shopManager.setModifier(managerId);
        try {
            if (shopManagerDao.updateById(shopManager) > Constants.DB_DEFAULT_INT) {
                cacheComponent.reload(CacheKeyConstant.PICKING);
                return Result.wrapSuccessfulResult("更新成功");
            }
        } catch (Exception e) {
            LOGGER.error("DB操作失败", e);
        }
        return Result.wrapErrorResult("", "更新失败");
    }

    private boolean checkMobileIsExist(String oldMobile, String newMobile, Long shopId) {
        if (!oldMobile.equals(newMobile)) {
            Map<String, Object> searchMap = Maps.newHashMap();
            searchMap.put("mobile", newMobile);
            searchMap.put("shopId", shopId);
            List<ShopManager> managerList = shopManagerDao.select(searchMap);
            if (!CollectionUtils.isEmpty(managerList)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新用户信息 管理员更新
     *
     * @param shopManagerCom 用户信息
     * @param managerId      更新操作用户
     */
    @Override
    public Result upDataShopManagerByAdmin(long shopId, ShopManagerCom shopManagerCom, long managerId) {
        // 数据校验
        Result result = validateData(shopId, shopManagerCom, managerId);
        if (!result.isSuccess()) {
            return result;
        }

        ShopManager shopManager = shopManagerDao.selectById(shopManagerCom.getAccountIdReg());
        if (shopManager == null) {
            return Result.wrapErrorResult("error", "用户不存在");
        }
        String oldMobile = shopManager.getMobile();
        String newMobile = shopManagerCom.getMobileReg();
        if (checkMobileIsExist(oldMobile, newMobile, shopId)) {
            return Result.wrapErrorResult("exist", "该联系方式在门店中已存在,请更换联系方式");
        }
        shopManager.setRoleId(shopManagerCom.getRolesReg());
        shopManager.setMobile(shopManagerCom.getMobileReg());
        shopManager.setName(shopManagerCom.getNameReg());
        shopManager.setShopId(shopId);
        shopManager.setModifier(managerId);

        shopManager.setStartTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(shopManagerCom.getStartTime()))));
        shopManager.setEndTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(shopManagerCom.getEndTime()))));

        ShopManagerLogin shopManagerLoginUser = null;
        try {
            shopManagerLoginUser = shopManagerLoginDao.selectByAccountAndShopId(shopManagerCom.getAccountReg(), shopId);
        } catch (Exception e) {
            LOGGER.error("DB查找用户错误", e);
        }
        if (null == shopManagerLoginUser) {
            return Result.wrapErrorResult("", "查找用户错误，更新失败");
        }
        ShopManagerLogin shopManagerLogin = new ShopManagerLogin();
        String password = shopManagerCom.getPasswordReg();
        if (!password.equals(shopManagerLoginUser.getPassword())) {
            if (!PasswordUtil.checkPassword(password)) {
                return Result.wrapErrorResult("", "密码不符合规则");
            }
            log.info("【PC修改密码】输入的旧密码:{},输入的新密码:{},操作人:{},门店ID:{}", shopManagerLoginUser.getPassword(), password, managerId, shopId);
            shopManagerLogin.setPassword(MD5Util.MD5(password));
            shopManagerLogin.setLowerPassword(MD5Util.MD5(password.toLowerCase()));

        }
        shopManagerLogin.setModifier(managerId);
        shopManagerLogin.setId(shopManagerCom.getAccountLoginIdReg());
        shopManagerLogin.setShopId(shopId);

        //修改关系表
        Map searchMap = new HashMap();
        searchMap.put("userId", shopManagerCom.getAccountIdReg());
        searchMap.put("shopId", shopId);
        List<PvgUserOrg> oldPvgUserOrgList = iPvgUserOrgService.select(searchMap);

        String pvgRoleIds = shopManagerCom.getPvgRoleIds();
        List<PvgUserOrg> newPvgUserOrgList = new ArrayList<>();
        if (!StringUtils.isBlank(pvgRoleIds)) {
            String[] pvgRoleIdArr = pvgRoleIds.split(",");
            for (int j = 0; j < pvgRoleIdArr.length; j++) {
                PvgUserOrg pvgUserOrg = new PvgUserOrg();
                pvgUserOrg.setOrgId(shopManagerCom.getRolesReg());
                pvgUserOrg.setPvgRoleId(Long.parseLong(pvgRoleIdArr[j]));
                pvgUserOrg.setShopId(shopId);
                pvgUserOrg.setUserId(shopManagerCom.getAccountIdReg());
                pvgUserOrg.setCreator(managerId);
                pvgUserOrg.setModifier(managerId);
                newPvgUserOrgList.add(pvgUserOrg);
            }

        }


        Result upDataShopManagerByAdminResult = null;
        try {
            upDataShopManagerByAdminResult = executeSql(shopManager, shopManagerLogin, oldPvgUserOrgList, newPvgUserOrgList);
        } catch (Exception e) {
            LOGGER.error("DB操作失败", e);
            return Result.wrapErrorResult("", "更新失败");
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
        return upDataShopManagerByAdminResult;
    }

    @Transactional
    public Result executeSql(ShopManager shopManager, ShopManagerLogin shopManagerLogin, List<PvgUserOrg> oldPvgUserOrgList, List<PvgUserOrg> newPvgUserOrgList) {
        if (shopManagerDao.updateById(shopManager) > Constants.DB_DEFAULT_INT) {
            cacheComponent.reload(CacheKeyConstant.PICKING);
            if (shopManagerLoginDao.updateById(shopManagerLogin) > Constants.DB_DEFAULT_INT) {
                if (!CollectionUtils.isEmpty(oldPvgUserOrgList)) {
                    for (PvgUserOrg pvgUserOrg : oldPvgUserOrgList) {
                        iPvgUserOrgService.delete(pvgUserOrg.getId());
                    }
                }
                if (!CollectionUtils.isEmpty(newPvgUserOrgList)) {
                    for (PvgUserOrg pvgUserOrg : newPvgUserOrgList) {
                        iPvgUserOrgService.insert(pvgUserOrg);
                    }
                }
                //添加或者更新员工登陆时间
                if (StringUtils.isNotBlank(shopManager.getStartTime()) && StringUtils.isNotBlank(shopManager.getEndTime())) {
                    WhiteAddress whiteAddress = whiteAddressService.getWhiteAddressInfo(shopManager.getId(), shopManager.getShopId());
                    if (whiteAddress == null) {
                        whiteAddress = new WhiteAddress();
                        whiteAddress.setShopId(shopManager.getShopId());
                        whiteAddress.setLoginBeginTime(shopManager.getStartTime());
                        whiteAddress.setLoginEndTime(shopManager.getEndTime());
                        whiteAddress.setCreator(shopManager.getModifier());
                        whiteAddress.setShopManagerId(shopManager.getId());
                        whiteAddressService.saveWhiteAddress(whiteAddress);
                    } else {
                        whiteAddress.setLoginBeginTime(shopManager.getStartTime());
                        whiteAddress.setLoginEndTime(shopManager.getEndTime());
                        whiteAddress.setModifier(shopManager.getModifier());
                        whiteAddressService.updateWhiteAddress(whiteAddress);
                    }
                }
                return Result.wrapSuccessfulResult("更新成功");
            }
        }
        return Result.wrapErrorResult("", "更新失败");
    }

    /**
     * 查询当前是管理员访问，还是普通用户访问
     *
     * @param currentLoginManageId 当前登录用户
     * @param managerId            被查询用户
     * @param isAdmin              管理员标示
     * @return 0表示访问自己页面，1表示管理员访问别人页面, 2表示管理员访问自己页面
     */
    @Override
    public Integer getChannel(Integer isAdmin, long currentLoginManageId, long managerId) {
        if (managerId < Constants.MANAGER_ID_FLAG) {
            LOGGER.warn("managerId错误");
            return null;
        }
        if (null == isAdmin || currentLoginManageId == 0) {
            LOGGER.warn("请求信息错误");
            return null;
        }
        if (isAdmin == 1) {
            if (currentLoginManageId == managerId) {
                return 2;
            } else {
                return 1;
            }
        } else {
            if (currentLoginManageId == managerId) {
                return 0;
            }
            return null;
        }
    }

    /*
    * 管理员更新用户信息，参数校验
    * */
    private Result validateData(long shopId, ShopManagerCom shopManagerCom, long managerId) {
        if (null == shopManagerCom) {
            LOGGER.error("shopManagerCom为空");
            return Result.wrapErrorResult("", "shopManagerCom对象为空");
        }

        if (StringUtils.isBlank(shopManagerCom.getNameReg()) || StringUtils.isBlank(shopManagerCom.getMobileReg()) || StringUtils.isBlank(shopManagerCom.getAccountReg()) || StringUtils.isBlank(shopManagerCom.getPasswordReg()) || shopManagerCom.getRolesReg() < Constants.ROLES_ID_FLAG) {
            LOGGER.error("信息不完整");
            return Result.wrapErrorResult("", "信息不完整 *号为必填项");
        }

        if (!StringUtil.isMobileNO(shopManagerCom.getMobileReg())) {
            LOGGER.error("电话号码格式不正确");
            return Result.wrapErrorResult("", "电话号码格式不正确");
        }

        if (shopId < Constants.ROLES_ID_FLAG) {
            LOGGER.error("shopId为空");
            return Result.wrapErrorResult("", "更新失败");
        }
        if (managerId < Constants.MANAGER_ID_FLAG) {
            LOGGER.error("managerId为空");
            return Result.wrapErrorResult("", "更新失败");
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
        return Result.wrapSuccessfulResult(true);
    }

    /*
    * 用户自己更新自己的信息，参数验证
    * */
    private Result validateDataBySelf(long shopId, ShopManager shopManager, long managerId) {
        if (null == shopManager) {
            LOGGER.warn("shopManagerCom为空");
            return Result.wrapErrorResult("", "shopManagerCom对象为空");
        }

        if (StringUtils.isBlank(shopManager.getName()) || StringUtils.isBlank(shopManager.getMobile()) || shopManager.getRoleId() < Constants.ROLES_ID_FLAG) {
            LOGGER.warn("信息不完整");
            return Result.wrapErrorResult("", "信息不完整 *号为必填项");
        }

        if (!StringUtil.isMobileNO(shopManager.getMobile())) {
            LOGGER.warn("电话号码格式不正确");
            return Result.wrapErrorResult("", "电话号码格式不正确");
        }

        if (shopId < Constants.SHOP_ID_FLAG) {
            LOGGER.warn("shopId为空");
            return Result.wrapErrorResult("", "更新失败");
        }

        if (managerId < Constants.MANAGER_ID_FLAG) {
            LOGGER.warn("managerId为空");
            return Result.wrapErrorResult("", "更新失败");
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 获取用户信息
     *
     * @param managerId 用户ID
     * @param shopId    店铺ID
     * @return
     */
    @Override
    public Optional<ShopManagerResp> getManagerInfo(Integer managerId, Long shopId) {
        ShopManager shopManager = shopManagerDao.selectById(managerId);
        if (shopManager == null) {
            return Optional.absent();
        }
        ShopManagerResp shopManagerResp = new ShopManagerResp();
        BdUtil.do2bo(shopManager, shopManagerResp);
        //查询岗位信息
        Long roleId = shopManager.getRoleId();
        Roles roles = rolesService.selectById(roleId);
        //查询门店信息
        if (roles != null) {
            shopManagerResp.setPostName(roles.getName());
        }
        Shop shop = shopService.selectById(shopId);
        if (shop != null) {
            shopManagerResp.setShopName(shop.getName());
            shopManagerResp.setShopAddress(shop.getAddress());
            shopManagerResp.setShopStatus(shop.getShopStatus());
        }
        return Optional.fromNullable(shopManagerResp);
    }

    @Override
    public Integer update(ShopManager shopManager) {
        return shopManagerDao.updateById(shopManager);
    }

    /**
     * 检查用户id是否存在
     * P
     *
     * @param userIdSet 用户id集合
     * @param shopId    店铺id
     * @return Set<Long> 返回存在的用户id
     */
    @Override
    public Set<Long> checkExist(final Long shopId, final Set<Long> userIdSet) {
        return new BizTemplate<Set<Long>>() {
            protected void checkParams() {
                //参数效验 统一抛出IllegalArgumentException异常  在模板类中统一处理
                Assert.notNull(shopId, "传入shopId不能为空");
                Assert.isTrue(shopId > 0, "shopId错误");
                Assert.notEmpty(userIdSet, "用户id入参为空");
            }

            protected Set<Long> process() {
                List<ShopManager> shopManagers = shopManagerDao.selectByIds(userIdSet.toArray(new Long[userIdSet.size()]));
                if (CollectionUtils.isEmpty(shopManagers)) {
                    return Collections.emptySet();
                }
                Set<Long> resultSet = new HashSet<>();
                for (ShopManager shopManager : shopManagers) {
                    if (null != shopManager.getShopId() && shopManager.getShopId().equals(shopId)) {
                        resultSet.add(shopManager.getId());
                    }
                }
                return resultSet;
            }
        }.execute();
    }

    @Override
    public ShopManager getAdmin(Long shopId) {
        Map<String, Object> shopManagerMap = Maps.newHashMap();
        shopManagerMap.put("shopId", shopId);
        shopManagerMap.put("isAdmin", 1);
        List<ShopManager> shopManagerList = select(shopManagerMap);
        if (CollectionUtils.isEmpty(shopManagerList)) {
            return null;
        }
        return shopManagerList.get(0);
    }

    @Override
    public List<ShopManager> selectAdminByShopIds(List<Long> shopIds) {
        List<ShopManager> shopManagerList = shopManagerDao.selectAdminByShopIds(shopIds);
        return shopManagerList;
    }

    @Override
    public Map<Long, ShopManager> getMapByByIds(Long[] shopManagerIds) {
        Map<Long, ShopManager> shopManagerMap = Maps.newHashMap();
        if (shopManagerIds == null || shopManagerIds.length == 0) {
            return shopManagerMap;
        }
        List<ShopManager> shopManagerList = selectByIds(shopManagerIds);
        for (ShopManager shopManager : shopManagerList) {
            shopManagerMap.put(shopManager.getId(), shopManager);
        }
        return shopManagerMap;
    }

    @Override
    public List<ShopManager> findByShopIdAndRoleId(Long shopId, Long roleId) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("roleId", roleId);
        return shopManagerDao.select(param);
    }

    @Override
    public ShopManagerAndLoginVo findByShopIdAndManagerId(Long shopId, Long managerId) {
        ShopManagerAndLoginVo shopManagerAndLoginVo = new ShopManagerAndLoginVo();
        ShopManager shopManager = shopManagerDao.selectByShopIdAndManagerId(shopId, managerId);
        if (shopManager == null) {
            return shopManagerAndLoginVo;
        }
        ShopManagerLogin shopManagerLogin = shopManagerLoginService.findByShopIdAndManagerId(shopId, shopManager.getId());
        shopManagerAndLoginVo.setName(shopManager.getName());
        shopManagerAndLoginVo.setMobile(shopManager.getMobile());
        shopManagerAndLoginVo.setShopId(shopManager.getShopId());
        shopManagerAndLoginVo.setRoleId(shopManager.getRoleId());
        shopManagerAndLoginVo.setIsAdmin(shopManager.getIsAdmin());
        shopManagerAndLoginVo.setIdentityCard(shopManager.getIdentityCard());
        shopManagerAndLoginVo.setAccount(shopManagerLogin.getAccount());
        shopManagerAndLoginVo.setManagerId(shopManagerLogin.getManagerId());
        shopManagerAndLoginVo.setPassword(shopManagerLogin.getPassword());
        shopManagerAndLoginVo.setManagerLoginId(shopManagerLogin.getId());

        if (shopManager.getRoleId() != null) {
            Long roleId = shopManager.getRoleId();
            Roles roles = rolesService.selectById(roleId);
            shopManagerAndLoginVo.setRolesName(roles.getName());
        }
        return shopManagerAndLoginVo;
    }

    @Override
    public List<ShopManager> findByShopIdAndMobile(Long shopId, String mobile) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("mobile", mobile);
        return shopManagerDao.select(param);
    }

    @Override
    @Transactional
    public void saveShopManagerAandManagerLogin(AddUser addUser) {
        Long shopId = addUser.getShopId();
        Long userId = addUser.getUserId();
        String mobile = addUser.getUserMobile();
        String account = addUser.getUserAccount();
        String pvgRoleIds = addUser.getPvgRoleIds();
        String preUserAccount = addUser.getPreUserAccount();

        List<ShopManager> shopManagerList = this.findByShopIdAndMobile(shopId, mobile);
        if (!CollectionUtils.isEmpty(shopManagerList)) {
            throw new BizException("该联系方式在门店中已存在,请更换联系方式");
        }
        String addAccount = preUserAccount + "-" + account;
        if (addAccount.length() > 100) {
            throw new BizException("账号长度不能超过100");
        }
        addUser.setAddAcount(addAccount);
        ShopManagerLogin shopManagerLogin = shopManagerLoginService.findByShopIdAndAccount(shopId, addAccount);
        if (shopManagerLogin.getId() != null) {
            throw new BizException("用户已存在");
        }
        Roles role = rolesService.selectById(addUser.getUserRoleId());
        if (role == null) {
            throw new BizException("岗位不存在");
        }

        ShopManager shopManager = new ShopManager();
        shopManager.setIsDeleted("N");
        shopManager.setCreator(userId);
        shopManager.setModifier(userId);
        shopManager.setIsAdmin(0);
        shopManager.setName(addUser.getUserName());
        shopManager.setMobile(addUser.getUserMobile());
        shopManager.setRoleId(addUser.getUserRoleId());
        shopManager.setShopId(shopId);
        shopManager.setStatus(1);
        shopManagerDao.insert(shopManager);

        ShopManagerLogin managerLogin = new ShopManagerLogin();
        managerLogin.setIsDeleted("N");
        managerLogin.setCreator(userId);
        managerLogin.setModifier(userId);
        managerLogin.setShopId(shopId);
        managerLogin.setManagerId(shopManager.getId());
        managerLogin.setAccount(addUser.getAddAcount());
        managerLogin.setPassword(MD5Util.MD5(addUser.getUserPassword()));
        managerLogin.setLowerPassword(MD5Util.MD5(addUser.getUserPassword().toLowerCase()));
        shopManagerLoginDao.insert(managerLogin);

        //插入用户岗位角色关系表
        if (!StringUtils.isBlank(pvgRoleIds)) {
            String[] pvgRoleIdArr = pvgRoleIds.split(",");
            for (int j = 0; j < pvgRoleIdArr.length; j++) {
                PvgUserOrg pvgUserOrg = new PvgUserOrg();
                pvgUserOrg.setShopId(shopId);
                pvgUserOrg.setOrgId(addUser.getUserRoleId());
                pvgUserOrg.setPvgRoleId(Long.parseLong(pvgRoleIdArr[j]));
                pvgUserOrg.setUserId(shopManager.getId());
                pvgUserOrg.setCreator(userId);
                pvgUserOrg.setModifier(userId);
                iPvgUserOrgService.insert(pvgUserOrg);
            }
        }

        WhiteAddress whiteAddress = new WhiteAddress();
        whiteAddress.setShopId(shopId);
        whiteAddress.setLoginBeginTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(addUser.getStartTime()))));
        whiteAddress.setLoginEndTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(addUser.getEndTime()))));
        whiteAddress.setCreator(userId);
        whiteAddress.setShopManagerId(shopManager.getId());
        whiteAddressService.saveWhiteAddress(whiteAddress);

        if (shopFunFacade.isUseWorkshop(shopId)) {
            ShopManagerExtVO shopManagerExtVO = BdUtil.bo2do(addUser, ShopManagerExtVO.class);
            shopManagerExtVO.setId(addUser.getExtId());
            shopManagerExtVO.setShopId(shopId);
            shopManagerExtVO.setManagerId(shopManager.getId());
            shopManagerExtVO.setManagerName(addUser.getUserName());
            com.tqmall.legend.common.Result result1 = shopManagerFacade.addShopManagerExtInfo(shopManagerExtVO, userId);
            if (!result1.isSuccess()) {
                log.error("[调用Magic dubbo接口添加员工扩展信息失败] shopId={},managerId={}", shopId, shopManager.getId());
                throw new BizException("[调用Magic dubbo接口添加员工扩展信息失败]");
            }
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
    }

    @Override
    @Transactional
    public void updateShopManager(ShopManagerAndLoginVo shopManagerAndLoginVo) {
        Long shopId = shopManagerAndLoginVo.getShopId();
        ShopManager oldManager = this.selectById(shopManagerAndLoginVo.getManagerId());
        if (oldManager == null) {
            throw new BizException("用户不存在");
        }
        String oldMobile = oldManager.getMobile();
        String newMobile = shopManagerAndLoginVo.getMobile();
        if (checkMobileIsExist(oldMobile, newMobile, shopId)) {
            throw new BizException("该联系方式在门店中已存在,请更换联系方式");
        }
        BeanUtils.copyProperties(shopManagerAndLoginVo, oldManager);
        oldManager.setModifier(shopManagerAndLoginVo.getUserId());
        this.update(oldManager);
        cacheComponent.reload(CacheKeyConstant.PICKING);
    }

    @Override
    @Transactional
    public void updateShopManagerOfAdmin(ShopManagerAndLoginVo shopManagerAndLoginVo) {
        Long shopId = shopManagerAndLoginVo.getShopId();
        Long userId = shopManagerAndLoginVo.getUserId();
        Long accountId = shopManagerAndLoginVo.getManagerId();

        ShopManager shopManager = this.selectById(accountId);
        if (shopManager == null) {
            log.warn("门店：{}，id：{}，用户不存在。", shopId, accountId);
            throw new BizException("用户不存在");
        }
        String oldMobile = shopManager.getMobile();
        String newMobile = shopManagerAndLoginVo.getMobile();
        if (checkMobileIsExist(oldMobile, newMobile, shopId)) {
            log.warn("门店：{}，oldMobile：{}，newMobile：{}，在门店中已存在。", shopId, oldMobile, newMobile);
            throw new BizException("该联系方式在门店中已存在,请更换联系方式");
        }

        ShopManagerLogin shopManagerLogin = shopManagerLoginService.findByShopIdAndAccount(shopId, shopManagerAndLoginVo.getAccount());
        if (shopManagerLogin.getId() == null) {
            log.warn("门店：{}，account：{}，用户登录信息不存在。", shopId, shopManagerAndLoginVo.getAccount());
            throw new BizException("用户登录信息不存在");
        }
        shopManager.setRoleId(shopManagerAndLoginVo.getRoleId());
        shopManager.setMobile(shopManagerAndLoginVo.getMobile());
        shopManager.setName(shopManagerAndLoginVo.getName());
        shopManager.setShopId(shopId);
        shopManager.setModifier(userId);
        shopManager.setStartTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(shopManagerAndLoginVo.getStartTime()))));
        shopManager.setEndTime(DateUtil.convertDateToHMS(DateUtil.convertStringToHMS(StringUtil.checkTime(shopManagerAndLoginVo.getEndTime()))));

        ShopManagerLogin managerLogin = new ShopManagerLogin();
        String password = shopManagerAndLoginVo.getPassword();
        if (!password.equals(shopManagerLogin.getPassword())) {
            if (!PasswordUtil.checkPassword(password)) {
                throw new BizException("密码不符合规则");
            }
            log.info("【PC修改密码】输入的旧密码:{},输入的新密码:{},操作人:{},门店ID:{}", shopManagerLogin.getPassword(), password, userId, shopId);
            managerLogin.setPassword(MD5Util.MD5(password));
            managerLogin.setLowerPassword(MD5Util.MD5(password.toLowerCase()));
        }
        managerLogin.setModifier(userId);
        managerLogin.setShopId(shopId);
        managerLogin.setId(shopManagerAndLoginVo.getManagerLoginId());

        //修改关系表
        List<PvgUserOrg> pvgUserOrgs = iPvgUserOrgService.findByShopIdAndUserId(shopId, accountId);
        String pvgRoleIds = shopManagerAndLoginVo.getPvgRoleIds();
        List<PvgUserOrg> newPvgUserOrgList = Lists.newArrayList();
        if (!StringUtils.isBlank(pvgRoleIds)) {
            String[] pvgRoleIdArr = pvgRoleIds.split(",");
            for (int j = 0; j < pvgRoleIdArr.length; j++) {
                PvgUserOrg pvgUserOrg = new PvgUserOrg();
                pvgUserOrg.setOrgId(shopManagerAndLoginVo.getRoleId());
                pvgUserOrg.setPvgRoleId(Long.parseLong(pvgRoleIdArr[j]));
                pvgUserOrg.setShopId(shopId);
                pvgUserOrg.setUserId(accountId);
                pvgUserOrg.setCreator(userId);
                pvgUserOrg.setModifier(userId);
                newPvgUserOrgList.add(pvgUserOrg);
            }
        }

        this.update(shopManager);
        shopManagerLoginService.updateById(managerLogin);
        for (PvgUserOrg pvgUserOrg : pvgUserOrgs) {
            iPvgUserOrgService.delete(pvgUserOrg.getId());
        }
        for (PvgUserOrg pvgUserOrg : newPvgUserOrgList) {
            iPvgUserOrgService.insert(pvgUserOrg);
        }

        //添加或者更新员工登陆时间
        if (StringUtils.isNotBlank(shopManager.getStartTime()) && StringUtils.isNotBlank(shopManager.getEndTime())) {
            WhiteAddress whiteAddress = whiteAddressService.getWhiteAddressInfo(shopManager.getId(), shopManager.getShopId());
            if (whiteAddress == null) {
                whiteAddress = new WhiteAddress();
                whiteAddress.setShopId(shopManager.getShopId());
                whiteAddress.setLoginBeginTime(shopManager.getStartTime());
                whiteAddress.setLoginEndTime(shopManager.getEndTime());
                whiteAddress.setCreator(shopManager.getModifier());
                whiteAddress.setShopManagerId(shopManager.getId());
                whiteAddressService.saveWhiteAddress(whiteAddress);
            } else {
                whiteAddress.setLoginBeginTime(shopManager.getStartTime());
                whiteAddress.setLoginEndTime(shopManager.getEndTime());
                whiteAddress.setModifier(shopManager.getModifier());
                whiteAddressService.updateWhiteAddress(whiteAddress);
            }
        }

        if (shopFunFacade.isUseWorkshop(shopId)) {
            ShopManagerExtVO shopManagerExtVO = BdUtil.bo2do(shopManagerAndLoginVo, ShopManagerExtVO.class);
            shopManagerExtVO.setId(shopManagerAndLoginVo.getExtId());
            shopManagerExtVO.setManagerName(shopManagerAndLoginVo.getName());
            Result result = shopManagerFacade.modifyShopManagerExt(shopManagerExtVO, userId);
            if (!result.isSuccess()) {
                log.error("[调用DUBBO接口] 修改员工信息失败，shopId={},managerId={}，入参={}", shopId, userId, LogUtils.objectToString(shopManagerAndLoginVo));
            }
            com.tqmall.core.common.entity.Result<List<LineProcessManagerDTO>> lineProcessManagerDTOResult = rpcLineProcessManagerService.selectByManagerId(accountId, shopId);
            if (lineProcessManagerDTOResult.isSuccess() && lineProcessManagerDTOResult.getData() != null) {
                if (!CollectionUtils.isEmpty(lineProcessManagerDTOResult.getData())) {
                    for (LineProcessManagerDTO lineProcessManagerDTO : lineProcessManagerDTOResult.getData()) {
                        lineProcessManagerDTO.setManagerName(shopManagerAndLoginVo.getName());
                        lineProcessManagerDTO.setTeamName(shopManagerAndLoginVo.getTeamName());
                        rpcLineProcessManagerService.updateById(lineProcessManagerDTO);
                    }
                }
            }
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
    }

    @Override
    @Transactional
    public void deleteShopManager(ShopManagerAndLoginVo managerAndLoginVo) {
        Long accountId = managerAndLoginVo.getManagerId();
        Long shopId = managerAndLoginVo.getShopId();
        Long userId = managerAndLoginVo.getUserId();

        ShopManagerAndLoginVo shopManagerAndLoginVo = this.findByShopIdAndManagerId(shopId, accountId);
        if (StringUtils.isBlank(shopManagerAndLoginVo.getName())) {
            throw new BizException("用户不存在");
        }
        if (shopManagerAndLoginVo.getIsAdmin() == 1) {
            throw new BizException("管理员用户不能删除");
        }
        //删除员工绑定信息
        Integer userAllotCount = customerUserRelFacade.userAllotCount(shopId, accountId);
        if (userAllotCount > 0) {
            customerUserRelFacade.unAllotByUserId(shopId, accountId, userId);
        }
        ShopManagerLogin shopManagerLogin = new ShopManagerLogin();
        shopManagerLogin.setIsDeleted("Y");
        shopManagerLogin.setModifier(userId);
        shopManagerLogin.setManagerId(accountId);
        shopManagerLogin.setShopId(shopId);

        ShopManager shopManager = new ShopManager();
        shopManager.setId(accountId);
        shopManager.setIsDeleted("Y");
        shopManager.setModifier(userId);
        shopManager.setShopId(shopId);

        this.update(shopManager);
        shopManagerLoginService.updateByManagerId(shopManagerLogin);

        List<PvgUserOrg> pvgUserOrgs = iPvgUserOrgService.findByShopIdAndUserId(shopId, accountId);
        for (PvgUserOrg pvgUserOrg : pvgUserOrgs) {
            iPvgUserOrgService.delete(pvgUserOrg.getId());
        }
        if (shopFunFacade.isUseWorkshop(shopId)) {
            //删除扩展表的信息 需要删除的员工ID必须传进来
            com.tqmall.legend.common.Result result1 = shopManagerFacade.removeShopMnagerExt(shopId, accountId, userId);
            if (!result1.isSuccess()) {
                log.error("[调用DUUBO接口] 同步删除员工扩展信息失败！门店ID={}，员工ID={}", shopId, accountId);
                throw new BizException("[调用DUUBO接口] 同步删除员工扩展信息失败！");
            }
        }
        cacheComponent.reload(CacheKeyConstant.PICKING);
    }

    @Override
    public List<ShopManager> findByNameOrMobile(Long shopId, String keyword) {
        return shopManagerDao.selectByNameOrMobile(shopId, keyword);
    }

    @Override
    public List<ShopManager> selectByIdsWithDeleted(Long[] ids) {
        return shopManagerDao.selectByIdsWithDeleted(ids);
    }
}
