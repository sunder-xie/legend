package com.tqmall.legend.web.setting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.TechnicianService;
import com.tqmall.legend.biz.privilege.vo.ShopManagerAndLoginVo;
import com.tqmall.legend.biz.pvg.IHRMService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.biz.util.PasswordUtil;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.entity.view.AddUser;
import com.tqmall.legend.enums.privilege.PvgRoleEnum;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.TeamFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.TeamVO;
import com.tqmall.legend.facade.privilege.PvgRoleFacade;
import com.tqmall.legend.facade.privilege.ShopUserInfoFacade;
import com.tqmall.legend.facade.privilege.bo.UserInfoQueryBo;
import com.tqmall.legend.facade.privilege.vo.UserInfoVo;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.pojo.ShopManagerCom;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.setting.vo.PvgRoleVo;
import com.tqmall.legend.web.setting.vo.UserAddInfoVo;
import com.tqmall.magic.object.param.workshop.ProcessParam;
import com.tqmall.magic.object.result.workshop.ProcessDTO;
import com.tqmall.magic.service.workshop.RpcProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 17/1/3.
 * 人员管理
 */
@Controller
@RequestMapping("shop/setting/user-info")
public class ShopUserInfoController extends BaseController {
    @Autowired
    private TeamFacade teamFacade;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private IPvgUserOrgService pvgUserOrgService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerFacade shopManagerFacade;
    @Autowired
    private RpcProcessService rpcProcessService;
    @Autowired
    private WhiteAddressService whiteAddressService;
    @Autowired
    private ShopManagerDeviceConfigService shopManagerDeviceConfigService;
    @Autowired
    private ShopUserInfoFacade shopUserInfoFacade;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private IHRMService ihrmService;
    @Autowired
    private PvgRoleFacade pvgRoleFacade;

    @RequestMapping("user-info")
    public String index(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        Integer isAdmin = userInfo.getUserIsAdmin();
        ShopManagerAndLoginVo shopManagerAndLoginVo = shopManagerService.findByShopIdAndManagerId(shopId, userId);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.findByShopIdAndUserId(shopId, userId);
        if (!CollectionUtils.isEmpty(pvgUserOrgList)) {
            PvgUserOrg pvgUserOrg = pvgUserOrgList.get(0);
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            /*角色：1.管理员、2.店长、5.技师*/
            if (pvgRoleId == Long.parseLong(PvgRoleEnum.GLY.getId().toString()) ||
                    pvgRoleId == Long.parseLong(PvgRoleEnum.DZ.getId().toString()) ||
                    pvgRoleId == Long.parseLong(PvgRoleEnum.JS.getId().toString())) {
                model.addAttribute("showTechnic","show");
                model.addAttribute("hasAuth", technicianService.hasTechnician(shopId, userId));
            }
        }
        List<FinanceAccount> financeAccounts = financeAccountService.getDefaultFinanceAccount(userId, shopId, 1);
        if (!CollectionUtils.isEmpty(financeAccounts)) {
            FinanceAccount financeAccount = financeAccounts.get(0);
            String account = financeAccount.getAccount();
            financeAccount.setAccount(StringUtil.formateBankCardNumber(account));
            model.addAttribute("financeAccount", financeAccounts.get(0));
        }
        List<String> rolesNameList = pvgRoleFacade.getRolesNameListByShopIdAndUserId(shopId, userId);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("rolesNameList", rolesNameList);
        model.addAttribute("shopManagerInfo", shopManagerAndLoginVo);
        model.addAttribute("shop", shopService.selectById(shopId));
        return "yqx/page/setting/userinfo/user-info";
    }

    /*个人信息编辑*/
    @RequestMapping("user-edit")
    public String editUserInfo(@RequestParam(value = "managerId") Long managerId, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        Integer isAdmin = userInfo.getUserIsAdmin();
        if (isAdmin == 0) {
            return "common/403"; //返回空页面
        }
        ShopManagerAndLoginVo shopManagerAndLoginVo = shopManagerService.findByShopIdAndManagerId(shopId, managerId);
        if (shopManagerAndLoginVo.getIsAdmin() == null) {
            return "yqx/page/setting/userinfo/user-edit"; //返回空页面
        }
        model.addAttribute("isAdmin", shopManagerAndLoginVo.getIsAdmin());
        Integer channel = shopManagerService.getChannel(isAdmin, userId, managerId);
        //0表示访问自己页面，1表示管理员访问别人信息页面
        if (null == channel) {
            return "yqx/page/setting/userinfo/user-edit"; //返回空页面
        } else if (channel == 0) {
            model.addAttribute("isAdminVal", 0);
        } else {
            model.addAttribute("isAdminVal", 1);
        }

        /*设置钣喷相关信息*/
        setModelOfBp(managerId, model, shopId);

        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.findByShopIdAndUserId(shopId, managerId);
        List<RolesL> rolesList = rolesService.selectRolesByShopId(shopId);

        WhiteAddress whiteAddress = whiteAddressService.getWhiteAddressInfo(managerId, shopId);
        List<ShopManagerDeviceConfig> managerDeviceConfigs = shopManagerDeviceConfigService.getDevicesByShopIdAndManagerId(shopId, managerId);
        model.addAttribute("rolesList", rolesList);
        model.addAttribute("whiteAddress", whiteAddress);
        Map<Long, Long> pvgUerOrgMap = Maps.newHashMap();
        for (PvgUserOrg pvgUserOrg : pvgUserOrgList) {
            pvgUerOrgMap.put(pvgUserOrg.getPvgRoleId(), null);
        }
        List<PvgRoleEnum> pvgRoleEnumList = PvgRoleEnum.getAppRoleList(pvgUerOrgMap);
        List<PvgRoleVo> pvgRoleVoList = getAppRoleList(pvgRoleEnumList);
        model.addAttribute("pvgRoleVoList", pvgRoleVoList);
        model.addAttribute("shopManagerInfo", shopManagerAndLoginVo);
        model.addAttribute("managerDevices", managerDeviceConfigs);

        return "yqx/page/setting/userinfo/user-edit";
    }

    private List<PvgRoleVo> getAppRoleList(List<PvgRoleEnum> pvgRoleEnumList) {
        List<PvgRoleVo> pvgRoleVoList = Lists.newArrayList();
        for (PvgRoleEnum pvgRoleEnum : pvgRoleEnumList) {
            PvgRoleVo pvgRoleVo = new PvgRoleVo();
            pvgRoleVo.setId(pvgRoleEnum.getId().longValue());
            pvgRoleVo.setRoleName(pvgRoleEnum.getRoleName());
            pvgRoleVo.setExist(pvgRoleEnum.getExist());
            pvgRoleVoList.add(pvgRoleVo);
        }
        return pvgRoleVoList;
    }

    /**
     * 个人信息添加
     *
     * @param model
     * @return
     */
    @RequestMapping("user-add")
    public String editUserInfo(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Integer isAdmin = userInfo.getUserIsAdmin();
        if (isAdmin == 0) {
            return "common/403"; //返回空页面
        }
        Result<UserAddInfoVo> userAddInfoVoResult = uerAddInfo(null);
        if (userAddInfoVoResult.isSuccess()) {
            UserAddInfoVo userAddInfoVo = userAddInfoVoResult.getData();
            //设置钣喷相关信息
            model.addAttribute("teamVOs", userAddInfoVo.getTeamVOList());
            model.addAttribute("processList", userAddInfoVo.getProcessDTOList());
            //账号前缀
            model.addAttribute("shopAbbr", userAddInfoVo.getShopAbbr());
            model.addAttribute("pvgRoleVoList", userAddInfoVo.getPvgRoleVoList());
        }
        List<RolesL> rolesList = rolesService.selectRolesByShopId(shopId);
        model.addAttribute("rolesList", rolesList);
        return "yqx/page/setting/userinfo/user-add";
    }

    //用户信息修改
    @RequestMapping(value = "change", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> updateUserInfo(@RequestBody final ShopManagerAndLoginVo shopManagerAndLoginVo, final HttpServletResponse response) {
        return new ApiTemplate<Object>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (StringUtils.isBlank(shopManagerAndLoginVo.getName())) {
                    throw new IllegalArgumentException("姓名不能为空");
                }
                if (StringUtils.isNotBlank(shopManagerAndLoginVo.getName()) && shopManagerAndLoginVo.getName().length() > 50) {
                    throw new IllegalArgumentException("姓名长度不能超过50");
                }
                if (StringUtils.isBlank(shopManagerAndLoginVo.getMobile()) || !StringUtil.isMobileNO(shopManagerAndLoginVo.getMobile())) {
                    throw new IllegalArgumentException("联系电话格式不正确");
                }
            }

            @Override
            protected Object process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                Long userId = userInfo.getUserId();
                shopManagerAndLoginVo.setUserId(userId);
                shopManagerAndLoginVo.setShopId(shopId);
                try {
                    CookieUtils.addCookie(response, Constants.SESSION_USER_NAME, java.net.URLEncoder.encode(shopManagerAndLoginVo.getName(), "UTF-8"), 43200);
                } catch (UnsupportedEncodingException e) {
                    logger.error("用户信息存储为cookie失败.", e);
                }
                shopManagerService.updateShopManager(shopManagerAndLoginVo);
                return "用户信息更新成功";
            }
        }.execute();
    }

    //管理员修改用户信息
    @RequestMapping(value = "change-admin", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updateUserInfoByAdmin(@RequestBody final ShopManagerAndLoginVo shopManagerAndLoginVo) {
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Integer isAdmin = userInfo.getUserIsAdmin();
                Long userId = userInfo.getUserId();
                Long shopId = userInfo.getShopId();
                Long rolesId = shopManagerAndLoginVo.getRoleId();
                Long accountIdReg = shopManagerAndLoginVo.getManagerId();
                String accountName = shopManagerAndLoginVo.getAccount();
                String mobile = shopManagerAndLoginVo.getMobile();
                Long accountLonginId = shopManagerAndLoginVo.getManagerLoginId();
                shopManagerAndLoginVo.setShopId(shopId);
                shopManagerAndLoginVo.setUserId(userId);
                if (accountIdReg == null || rolesId == null ||
                        isAdmin == null || isAdmin != 1 || accountLonginId == null) {
                    throw new IllegalArgumentException("参数错误");
                }
                if (StringUtils.isBlank(accountName)) {
                    throw new IllegalArgumentException("账户名称不能为空");
                }
                if (StringUtils.isBlank(mobile) || !StringUtil.isMobileNO(mobile)) {
                    throw new IllegalArgumentException("联系电话格式不正确");
                }
                if (StringUtils.isBlank(shopManagerAndLoginVo.getName())) {
                    throw new IllegalArgumentException("姓名不能为空");
                }
                if (StringUtils.isNotBlank(shopManagerAndLoginVo.getName()) && shopManagerAndLoginVo.getName().length() > 50) {
                    throw new IllegalArgumentException("姓名长度不能超过50");
                }
                //管理员无需填写时间
                if (!accountIdReg.equals(userId)) {
                    if (!StringUtil.isPatternTime(shopManagerAndLoginVo.getStartTime())) {
                        throw new IllegalArgumentException("登陆开始时间格式不正确");
                    }
                    if (!StringUtil.isPatternTime(shopManagerAndLoginVo.getEndTime())) {
                        throw new IllegalArgumentException("登陆结束时间格式不正确");
                    }
                }
            }

            @Override
            protected String process() throws BizException {
                shopManagerService.updateShopManagerOfAdmin(shopManagerAndLoginVo);
                return "用户信息更新成功";
            }
        }.execute();
    }

    /*员工添加*/
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> addUserInfo(@RequestBody final AddUser addUser) {
        return new ApiTemplate<Object>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Integer isAdmin = userInfo.getUserIsAdmin();
                if (isAdmin == null || isAdmin != 1) {
                    throw new IllegalArgumentException("您无权操作此功能");
                }
                boolean isCarAddUser = shopUserInfoFacade.isCanAddUser(userInfo.getShopId());
                if (!isCarAddUser) {
                    throw new IllegalArgumentException("您的账号数量已经超出范围，如要添加，请联系我们");
                }
                if (!PasswordUtil.checkPassword(addUser.getUserPassword())) {
                    logger.warn("【人员管理添加账号密码不符合规则】密码{}", addUser.getUserPassword());
                    throw new IllegalArgumentException("密码不符合规则");
                }
                if (StringUtils.isBlank(addUser.getPreUserAccount())) {
                    logger.warn("preUserAccount为空");
                    throw new IllegalArgumentException("信息不完整*号为必填项");
                }
                if (StringUtils.isBlank(addUser.getUserAccount())) {
                    logger.warn("userAccount为空");
                    throw new IllegalArgumentException("信息不完整*号为必填项");
                }
                if (!StringUtils.isBlank(addUser.getUserMobile()) && !StringUtil.isMobileNO(addUser.getUserMobile())) {
                    logger.warn("联系电话格式不正确");
                    throw new IllegalArgumentException("联系电话格式不正确");
                }
                if (!StringUtil.isPatternTime(addUser.getStartTime())) {
                    throw new IllegalArgumentException("登陆开始时间格式不正确");
                }
                if (!StringUtil.isPatternTime(addUser.getEndTime())) {
                    throw new IllegalArgumentException("登陆结束时间格式不正确");
                }
                if (StringUtils.isBlank(addUser.getUserName())) {
                    throw new IllegalArgumentException("姓名不能为空");
                }
                if (StringUtils.isNotBlank(addUser.getUserName()) && addUser.getUserName().length() > 50) {
                    throw new IllegalArgumentException("姓名长度不能超过50");
                }
                if (StringUtils.isBlank(addUser.getPvgRoleIds())) {
                    throw new IllegalArgumentException("APP角色不能为空");
                }
                if (addUser.getUserRoleId() == null) {
                    logger.warn("userRoleId为空");
                    throw new IllegalArgumentException("参数错误");
                }
            }

            @Override
            protected Object process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                addUser.setShopId(userInfo.getShopId());
                addUser.setUserId(userInfo.getUserId());
                shopManagerService.saveShopManagerAandManagerLogin(addUser);
                return rolesService.findByShopIdAndRoleId(addUser.getShopId(), addUser.getUserRoleId());
            }
        }.execute();

    }

    /*员工删除*/
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> deleteUserInfo(@RequestBody final ShopManagerAndLoginVo shopManagerAndLoginVo) {
        return new ApiTemplate<Object>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Integer isAdmin = userInfo.getUserIsAdmin();
                if (isAdmin == null || isAdmin != 1) {
                    throw new IllegalArgumentException("您无权操作此功能");
                }
                if (shopManagerAndLoginVo.getManagerId() == null || shopManagerAndLoginVo.getRoleId() == null) {
                    throw new IllegalArgumentException("参数错误");
                }
            }

            @Override
            protected Object process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long userId = userInfo.getUserId();
                Long shopId = userInfo.getShopId();
                shopManagerAndLoginVo.setShopId(shopId);
                shopManagerAndLoginVo.setUserId(userId);
                shopManagerService.deleteShopManager(shopManagerAndLoginVo);
                return rolesService.findByShopIdAndRoleId(shopId, shopManagerAndLoginVo.getRoleId());
            }
        }.execute();
    }


    private void setModelOfBp(Long managerId, Model model, Long shopId) {
        Boolean isShare = shopFunFacade.isBpShare(request, shopId);
        if (isShare) {
            ShopManagerExtVO shopManagerExtVO = shopManagerFacade.getShopManagerExtInfo(shopId, managerId).getData();
            model.addAttribute("shopManagerExtVO", shopManagerExtVO);
            Result<List<TeamVO>> teamResult = teamFacade.getTeamByShopId(shopId);
            List<TeamVO> teamVOs = Lists.newArrayList();
            if (teamResult.isSuccess()) {
                teamVOs = teamResult.getData();
            }
            TeamVO teamVO = new TeamVO();
            teamVO.setId(0L);
            teamVO.setName("无");
            teamVOs.add(teamVO);
            model.addAttribute("teamVOs", teamVOs);
            Result<List<ProcessDTO>> processResult = rpcProcessService.selectProcessList(new ProcessParam());
            List<ProcessDTO> processDTOs = Lists.newArrayList();
            if (processResult.isSuccess()) {
                processDTOs = processResult.getData();
            }
            ProcessDTO processDTO = new ProcessDTO();
            processDTO.setId(0L);
            processDTO.setName("无");
            processDTOs.add(processDTO);
            model.addAttribute("processList", processDTOs);
        }
    }

    /**
     * 人员管理列表页面
     *
     * @return
     */
    @RequestMapping("user-list")
    public String userList() {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //如果用户没有初始化岗位，就添加默认的标准化岗位
        ihrmService.insertOrgTreeFromTemplate(4, userInfo);
        int isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        //普通员工无权访问
        if (isAdmin == 0) {
            return "common/403";
        }
        return "yqx/page/setting/userinfo/user-list";
    }

    /**
     * 获取人员列表接口
     *
     * @return
     */
    @RequestMapping(value = "user-list/get-list", method = RequestMethod.GET)
    @ResponseBody
    public Result<DefaultPage<UserInfoVo>> getUserList(final UserInfoQueryBo userInfoQueryBo) {
        return new ApiTemplate<DefaultPage<UserInfoVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected DefaultPage<UserInfoVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                userInfoQueryBo.setShopId(shopId);
                DefaultPage<UserInfoVo> userInfoPage = shopUserInfoFacade.getUserInfoPage(userInfoQueryBo);
                return userInfoPage;
            }
        }.execute();
    }


    /**
     * 根据岗位Id查找员工
     *
     * @param rolesId 岗位id
     * @return
     */
    @RequestMapping(value = "user-list/get-roles-user", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ShopManagerCom>> getRolesUser(final Long rolesId) {
        return new ApiTemplate<List<ShopManagerCom>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesId, "岗位id为空");
            }

            @Override
            protected List<ShopManagerCom> process() throws BizException {
                long shopId = UserUtils.getShopIdForSession(request);
                List<ShopManagerCom> shopManagerComList = rolesService.findByShopIdAndRoleId(shopId, rolesId);
                return shopManagerComList;
            }
        }.execute();
    }

    /**
     * 用户添加弹框信息获取
     *
     * @return
     */
    @RequestMapping(value = "user-add-info", method = RequestMethod.GET)
    @ResponseBody
    public Result<UserAddInfoVo> uerAddInfo(final Long pvgRoleId) {
        return new ApiTemplate<UserAddInfoVo>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected UserAddInfoVo process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Shop shop = shopService.selectById(shopId);
                UserAddInfoVo userAddInfoVo = new UserAddInfoVo();
                userAddInfoVo.setShopAbbr(shop.getAbbr());
                List<PvgRoleEnum> pvgRoleEnumList = PvgRoleEnum.getAppRoleList(pvgRoleId);
                List<PvgRoleVo> pvgRoleVoList = getAppRoleList(pvgRoleEnumList);
                userAddInfoVo.setPvgRoleVoList(pvgRoleVoList);
                //钣喷中心设置钣喷信息
                if (shopFunFacade.isBpShare(request, shopId)) {
                    List<TeamVO> teamVOs = teamFacade.getTeamByShopId(shopId).getData();
                    TeamVO teamVO = new TeamVO();
                    teamVO.setId(0L);
                    teamVO.setName("无");
                    teamVOs.add(teamVO);
                    userAddInfoVo.setTeamVOList(teamVOs);
                    List<ProcessDTO> processDTOs = rpcProcessService.selectProcessList(new ProcessParam()).getData();
                    ProcessDTO processDTO = new ProcessDTO();
                    processDTO.setId(0L);
                    processDTO.setName("无");
                    processDTOs.add(processDTO);
                    userAddInfoVo.setProcessDTOList(processDTOs);
                }
                return userAddInfoVo;
            }
        }.execute();
    }
}
