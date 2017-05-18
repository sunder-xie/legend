package com.tqmall.legend.web.privilege;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.privilege.PasswordChangeService;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.sys.WhiteAddressService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.CookieUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.config.ShopManagerDeviceConfig;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.entity.sys.WhiteAddress;
import com.tqmall.legend.facade.magic.ShopManagerFacade;
import com.tqmall.legend.facade.magic.TeamFacade;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.TeamVO;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.pojo.ShopManagerCom;
import com.tqmall.legend.rpc.crm.CrmCustomerService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.workshop.ProcessParam;
import com.tqmall.magic.object.result.workshop.LineProcessManagerDTO;
import com.tqmall.magic.object.result.workshop.ProcessDTO;
import com.tqmall.magic.service.workshop.RpcLineProcessManagerService;
import com.tqmall.magic.service.workshop.RpcProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/29.
 */
@Slf4j
@Controller("PasswordChangeController")
@RequestMapping("shop/member")
public class PasswordChangeController extends BaseController {

    @Autowired
    private PasswordChangeService passwordChangeService;

    @Autowired
    private ShopManagerService shopManagerService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private IPvgUserOrgService pvgUserOrgService;

    @Autowired
    private ShopManagerFacade shopManagerFacade;
    @Autowired
    private TeamFacade teamFacade;
    @Autowired
    private WhiteAddressService whiteAddressService;

    @Autowired
    private ShopFunFacade shopFunFacade;

    @Autowired
    private RpcProcessService rpcProcessService;

    @Autowired
    private RpcLineProcessManagerService rpcLineProcessManagerService;
    @Autowired
    private ShopManagerDeviceConfigService shopManagerDeviceConfigService;
    @Autowired
    private WeChatShopService weChatShopService;
    @Autowired
    private CrmCustomerService crmCustomerService;

    //密码修改页面
    @RequestMapping(value = "change")
    public String change(Model model) {
        model.addAttribute("settingsTab", "set");
        model.addAttribute("moduleUrl", "settings");
        return "privilege/password_change";
    }

    //个人信息页面
    @RequestMapping(value = "show_user_info")
    public String showUserInfo(Model model, HttpServletRequest request) {
        long shopId = UserUtils.getShopIdForSession(request);
        String managerIdStr = request.getParameter("managerId");
        if (!StringUtils.isNumeric(managerIdStr)) {
            return "privilege/set_person_data";  //返回空页面
        }
        long managerId = Long.parseLong(request.getParameter("managerId"));
        long currentLoginManageId = UserUtils.getUserIdForSession(request);
        Integer isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        ShopManagerCom shopManagerCom = shopManagerService.selectUserInfoByShopIdAndManagerId(shopId, managerId);
        if (null == shopManagerCom) {
            return "privilege/set_person_data";  //返回空页面
        }
        model.addAttribute("isAdmin",shopManagerCom.getIsAdminReg());
        ShopManagerExtVO shopManagerExtVO = null;
        List<TeamVO> teamVOs = null;
        List<ProcessDTO> processDTOs = null;
        Boolean isShare = shopFunFacade.isBpShare(request, shopId);
        if (isShare) {
            shopManagerExtVO = shopManagerFacade.getShopManagerExtInfo(shopId, managerId).getData();
            teamVOs = teamFacade.getTeamByShopId(shopId).getData();
            TeamVO teamVO = new TeamVO();
            teamVO.setId(0L);
            teamVO.setName("无");
            teamVOs.add(teamVO);
            processDTOs = rpcProcessService.selectProcessList(new ProcessParam()).getData();
            ProcessDTO processDTO = new ProcessDTO();
            processDTO.setId(0L);
            processDTO.setName("无");
            processDTOs.add(processDTO);
        }


        Map<String, Object> pvgUserOrgMap = new HashMap<>();
        pvgUserOrgMap.put("shopId", shopId);
        pvgUserOrgMap.put("userId", managerId);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.select(pvgUserOrgMap);
        List<RolesL> rolesList = rolesService.selectRolesByShopId(shopId);
        if (CollectionUtils.isEmpty(rolesList)) {
            return "privilege/set_person_data";  //返回空页面
        }
        List<RolesL> rolesResultList = new ArrayList<RolesL>();
        if (shopManagerCom.getIsAdminReg() == 0) {
            for (RolesL roles : rolesList) {
                rolesResultList.add(roles);
            }
            rolesList = rolesResultList;
        }

        Integer channel = shopManagerService.getChannel(isAdmin, currentLoginManageId, managerId);
        //0表示访问自己页面，1表示管理员访问别人信息页面
        if (null == channel) {
            return "privilege/set_person_data"; //返回空页面
        } else if (channel == 0) {
            model.addAttribute("isAdminVal", 0);
        } else {
            model.addAttribute("isAdminVal", 1);
        }
        Boolean isUseWorkShop = shopFunFacade.isUseWorkshop(shopId);
        WhiteAddress whiteAddress = whiteAddressService.getWhiteAddressInfo(managerId, shopId);
        List<ShopManagerDeviceConfig> managerDeviceConfigs = shopManagerDeviceConfigService.getDevicesByShopIdAndManagerId(shopId, managerId);
        model.addAttribute("managerDevices", managerDeviceConfigs);
        model.addAttribute("whiteAddress", whiteAddress);
        model.addAttribute("isShare", isShare);
        model.addAttribute("isUseWorkShop", isUseWorkShop);
        model.addAttribute("shopManagerInfo", shopManagerCom);
        model.addAttribute("shopManagerExtVO", shopManagerExtVO);
        model.addAttribute("pvgUserOrgList", pvgUserOrgList);
        model.addAttribute("rolesList", rolesList);
        model.addAttribute("settingsTab", "set");
        model.addAttribute("moduleUrl", "settings");
        model.addAttribute("teamVOs", teamVOs);
        model.addAttribute("processList", processDTOs);
        return "privilege/set_person_data";
    }

    //用户信息修改
    @RequestMapping(value = "user_info_change", method = RequestMethod.POST)
    @ResponseBody
    public Result userInfoChange(ShopManager shopManager, HttpServletRequest request, HttpServletResponse response) {
        if (null == shopManager) {
            return Result.wrapErrorResult("", "参数错误");
        }
        long shopId = UserUtils.getShopIdForSession(request);
        long managerId = UserUtils.getUserIdForSession(request);
        try {
            CookieUtils.addCookie(response, Constants.SESSION_USER_NAME, java.net.URLEncoder.encode(shopManager.getName(), "UTF-8"), 43200);
        } catch (UnsupportedEncodingException e) {
            log.error("用户信息存储为cookie失败.", e);
        }
        return shopManagerService.upDataShopManager(shopId, shopManager, managerId);
    }

    /**
     * 管理员修改用户信息
     *
     * @param shopManagerCom
     * @param request
     * @return
     */
    @RequestMapping(value = "user_info_change_by_admin", method = RequestMethod.POST)
    @ResponseBody
    public Result userInfoChangeByAdmin(ShopManagerCom shopManagerCom, HttpServletRequest request) {
        if (null == shopManagerCom) {
            return Result.wrapErrorResult("", "参数错误");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        long shopId = userInfo.getShopId();
        long managerId = userInfo.getUserId();
        Integer isAdmin = userInfo.getUserIsAdmin();

        if (null == isAdmin || isAdmin == 0) {
            return Result.wrapErrorResult("", "参数错误");
        }
        //管理员无需填写时间
        long accountIdReg = shopManagerCom.getAccountIdReg();
        if(accountIdReg != managerId){
            if(!StringUtil.isPatternTime(shopManagerCom.getStartTime())){
                return Result.wrapErrorResult("","登陆开始时间格式不正确");
            }
            if(!StringUtil.isPatternTime(shopManagerCom.getEndTime())){
                return Result.wrapErrorResult("","登陆结束时间格式不正确");
            }
        }

        //TODO 此处需要加入对扩展信息表的更新
        if (shopFunFacade.isUseWorkshop(shopId)) {
            ShopManagerExtVO shopManagerExtVO = BdUtil.bo2do(shopManagerCom, ShopManagerExtVO.class);
            shopManagerExtVO.setId(shopManagerCom.getExtId());
            shopManagerExtVO.setManagerName(shopManagerCom.getNameReg());
            Result result = shopManagerFacade.modifyShopManagerExt(shopManagerExtVO, managerId);
            if (!result.isSuccess()) {
                log.error("[调用DUBBO接口] 修改员工信息失败，shopId={},managerId={}，入参={}", shopId, managerId, JSONUtil.object2Json(shopManagerCom));
            }

            com.tqmall.core.common.entity.Result<List<LineProcessManagerDTO>> lineProcessManagerDTOResult = null;
            try {
                lineProcessManagerDTOResult = rpcLineProcessManagerService.selectByManagerId(shopManagerCom.getAccountIdReg(), shopId);
            } catch (Exception e) {
                log.error("查询每日生产线人员失败",e);
                return Result.wrapErrorResult("","查询每日生产线人员失败");
            }
            if (lineProcessManagerDTOResult.isSuccess() && lineProcessManagerDTOResult.getData() != null){
                if (!CollectionUtils.isEmpty(lineProcessManagerDTOResult.getData())){
                    for (LineProcessManagerDTO lineProcessManagerDTO : lineProcessManagerDTOResult.getData()){
                        lineProcessManagerDTO.setManagerName(shopManagerCom.getNameReg());
                        lineProcessManagerDTO.setTeamName(shopManagerCom.getTeamName());
                        try {
                            rpcLineProcessManagerService.updateById(lineProcessManagerDTO);
                        } catch (Exception e) {
                            log.error("更新每日排班失败",e);
                            return Result.wrapErrorResult("","更新每日排班失败");
                        }
                    }
                }
            }

        }

        return shopManagerService.upDataShopManagerByAdmin(shopId, shopManagerCom, managerId);
    }


    /**
     * 修改密码
     *
     * @param request
     * @param shopManagerCom 参数
     * @return Result
     */
    @RequestMapping(value = "change_submit", method = RequestMethod.POST)
    @ResponseBody
    public Result passwordChange(HttpServletRequest request, ShopManagerCom shopManagerCom) {
        String oldPassWord = shopManagerCom.getPasswordReg().trim();
        String newPassWord = shopManagerCom.getNewPasswordReg().trim();
        long accountId = UserUtils.getUserIdForSession(request);
        long shopId = UserUtils.getShopIdForSession(request);
        return passwordChangeService.changePassword(accountId, oldPassWord, newPassWord, shopId);
    }
}
