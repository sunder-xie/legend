package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.privilege.RolesService;
import com.tqmall.legend.biz.pvg.IHRMService;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.Roles;
import com.tqmall.legend.entity.privilege.RolesL;
import com.tqmall.legend.enums.privilege.PvgRoleEnum;
import com.tqmall.legend.facade.privilege.RolesFacade;
import com.tqmall.legend.facade.privilege.RolesFuncFacade;
import com.tqmall.legend.facade.privilege.ShopUserInfoFacade;
import com.tqmall.legend.facade.privilege.bo.RolesBo;
import com.tqmall.legend.facade.privilege.bo.RolesFuncBo;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by zsy on 17/1/4.
 * 岗位管理
 */
@Controller
@RequestMapping("shop/setting/roles")
public class RolesSettingController extends BaseController {
    @Autowired
    private FuncService funcService;
    @Autowired
    private IHRMService ihrmService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private RolesFacade rolesFacade;
    @Autowired
    private RolesFuncFacade rolesFuncFacade;
    @Autowired
    private ShopUserInfoFacade shopUserInfoFacade;

    /**
     * 岗位权限列表
     *
     * @return
     */
    @RequestMapping("roles-list")
    public String index(Model model){
        UserInfo userInfo = UserUtils.getUserInfo(request);
        //如果用户没有初始化岗位，就添加默认的标准化岗位
        ihrmService.insertOrgTreeFromTemplate(4, userInfo);
        int isAdmin = UserUtils.getUserInfo(request).getUserIsAdmin();
        //普通员工无权访问
        if (isAdmin == 0) {
            return "common/403";
        }
        return "yqx/page/setting/roles/roles-list";
    }

    /**
     * 获取左侧岗位列表
     * @return
     */
    @RequestMapping("get-roles")
    @ResponseBody
    public Result<RolesL> getFuncF(){
        return new ApiTemplate<RolesL>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected RolesL process() throws BizException {
                long shopId = UserUtils.getShopIdForSession(request);
                RolesL rolesL = rolesService.selectRolesByShopIdL(shopId);
                return rolesL;
            }
        }.execute();
    }

    /**
     * 获取角色的所有功能
     *
     * @param rolesId
     * @param parentId
     * @return
     */
    @RequestMapping(value = "get-all-func", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<FuncF>> getAllFunc(@RequestParam(value = "rolesId") final Long rolesId, @RequestParam(value = "parentId") final Long parentId) {
        return new ApiTemplate<List<FuncF>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesId, "rolesId为空");
                Assert.notNull(parentId, "parentId为空");
            }

            @Override
            protected List<FuncF> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Integer level = UserUtils.getShopLevelForSession(request);
                List<FuncF> allFuncF = funcService.getFuncFsForRoles(shopId, rolesId, parentId, level);
                return allFuncF;
            }
        }.execute();
    }

    /**
     * 获取岗位列表
     *
     * @return
     */
    @RequestMapping(value = "get-roles-list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<RolesL>> getRolesList() {
        return new ApiTemplate<List<RolesL>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<RolesL> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                List<RolesL> rolesList = rolesService.selectRolesByShopId(shopId);
                return rolesList;
            }
        }.execute();
    }

    /**
     * 添加岗位
     *
     * @param rolesBo
     * @return
     */
    @RequestMapping(value = "add-roles", method = RequestMethod.POST)
    @ResponseBody
    public Result<Roles> addRoles(@RequestBody final RolesBo rolesBo) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Roles>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesBo, "参数为空");
                String rolesName = rolesBo.getName();
                Assert.notNull(rolesName, "岗位名称不能为空");
                if(rolesName.length() > 15){
                    throw new IllegalArgumentException("岗位名称过长");
                }
                Assert.notNull(rolesBo.getParentId(), "父id不能为空");
                Assert.notNull(rolesBo.getLevelId(), "levelId不能为空");
                Integer levelId = rolesBo.getLevelId();
                if (levelId >= 5) {
                    throw new IllegalArgumentException("最多只有5层");
                }
                Integer pvgRoleId = rolesBo.getPvgRoleId();
                //不能添加店长级别岗位
                if (pvgRoleId == null || pvgRoleId.equals(PvgRoleEnum.GLY.getId()) || pvgRoleId.equals(PvgRoleEnum.DZ.getId())) {
                    throw new IllegalArgumentException("不能添加店长级别岗位");
                }
                String name = rolesBo.getName();
                Long parentId = rolesBo.getParentId();
                RolesBo checkBo = new RolesBo();
                checkBo.setShopId(shopId);
                checkBo.setParentId(parentId);
                checkBo.setName(name);
                boolean exist = rolesFacade.isExist(checkBo);
                Assert.isTrue(!exist, "岗位已存在");
            }

            @Override
            protected Roles process() throws BizException {
                Long userId = UserUtils.getUserIdForSession(request);
                rolesBo.setShopId(shopId);
                rolesBo.setUserId(userId);
                Roles addRoles = rolesFacade.addRoles(rolesBo);
                return addRoles;
            }
        }.execute();
    }

    /**
     * 删除岗位
     *
     * @param rolesId 岗位id
     * @return
     */
    @RequestMapping(value = "del-roles", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> delRoles(final Long rolesId) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesId, "岗位id不能为空");
                //校验岗位下是否还有员工
                boolean existShopManagerByShopIdAndRoleId = shopUserInfoFacade.isExistShopManagerByShopIdAndRoleId(shopId, rolesId);
                Assert.isTrue(!existShopManagerByShopIdAndRoleId, "岗位下面有员工,请先删除员工");
                //校验岗位下是否还存在子岗位
                boolean exist = rolesFacade.isExistChild(rolesId,shopId);
                Assert.isTrue(!exist, "该岗位下面还有岗位,请先删除子岗位");
            }

            @Override
            protected Boolean process() throws BizException {
                Long userId = UserUtils.getUserIdForSession(request);
                boolean addRoles = rolesFacade.delRoles(rolesId, shopId, userId);
                return addRoles;
            }
        }.execute();
    }


    /**
     * 编辑岗位
     *
     * @param rolesId   岗位id
     * @param rolesName 岗位名称
     * @return
     */
    @RequestMapping(value = "edit-roles", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> editRoles(final Long rolesId, final String rolesName) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesId, "岗位id不能为空");
                Assert.notNull(rolesName, "岗位名称不能为空");
                if(rolesName.length() > 15){
                    throw new IllegalArgumentException("岗位名称过长");
                }
                //校验岗位下是否还存在岗位
                boolean exist = rolesFacade.isExistIgnoreOwn(rolesId, shopId, rolesName);
                Assert.isTrue(!exist, "已存在相同的平级岗位，修改失败");
            }

            @Override
            protected Boolean process() throws BizException {
                Long userId = UserUtils.getUserIdForSession(request);
                RolesBo rolesBo = new RolesBo();
                rolesBo.setId(rolesId);
                rolesBo.setName(rolesName);
                rolesBo.setShopId(shopId);
                rolesBo.setUserId(userId);
                boolean editRoles = rolesFacade.editRoles(rolesBo);
                return editRoles;
            }
        }.execute();
    }

    /**
     * 更新岗位权限功能
     *
     * @param rolesFuncBo
     */
    @RequestMapping(value = "update-roles-func",method = RequestMethod.POST)
    @ResponseBody
    public Result<List<FuncF>> updateRolesFunc(@RequestBody final RolesFuncBo rolesFuncBo) {
        return new ApiTemplate<List<FuncF>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(rolesFuncBo,"参数为空");
                Assert.notNull(rolesFuncBo.getRolesId(),"岗位id为空");
                Assert.notNull(rolesFuncBo.getParentId(),"parentId为空");
            }

            @Override
            protected List<FuncF> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long modifierId = UserUtils.getUserIdForSession(request);
                Long rolesId = rolesFuncBo.getRolesId();
                Long parentId = rolesFuncBo.getParentId();
                rolesFuncBo.setUserId(modifierId);
                rolesFuncBo.setShopId(shopId);
                boolean updateRolesFuncRel = rolesFuncFacade.updateRolesFuncRel(rolesFuncBo);
                if(!updateRolesFuncRel){
                    throw new BizException("更新失败");
                }
                Integer level = UserUtils.getShopLevelForSession(request);
                List<FuncF> allFuncF = funcService.getFuncFsForRoles(shopId, rolesId, parentId, level);
                return allFuncF;
            }
        }.execute();
    }
}
