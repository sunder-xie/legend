package com.tqmall.legend.server.privilege;

import com.google.common.base.Optional;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.UserRoleInfoService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.object.param.service.UserRoleInfoParam;
import com.tqmall.legend.service.service.RpcUserRoleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by twg on 16/1/11.
 */
@Slf4j
@Service("rpcUserRoleInfoService")
public class RpcUserRoleInfoServiceImpl implements RpcUserRoleInfoService {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private IPvgUserOrgService pvgUserOrgService;
    @Autowired
    private UserRoleInfoService userRoleInfoService;

    @Override
    public Result approveUserRoleInfo(UserRoleInfoParam userRoleInfoParam) {
        log.info("[用户信息更新] 员工用户信息更新.userRoleInfoParam={}", LogUtils.objectToString(userRoleInfoParam));
        if (userRoleInfoParam.getId() != null) {
            boolean flag = false;
            ShopManager shopManager = shopManagerService.selectById(userRoleInfoParam.getId());
            Long shopId = shopManager.getShopId();
            Optional<List<RoleInfoResp>> roleInfoRespList = pvgUserOrgService.getReferRoleList(shopManager.getId().intValue(), shopId);
            List<RoleInfoResp> roleInfoList = roleInfoRespList.get();
            for (RoleInfoResp roleInfoResp : roleInfoList) {
                int roleId = roleInfoResp.getRoleId().intValue();
                //当角色为管理员、店长、技师时，要添加技师认证表信息（或者更新）
                if (roleId == 1 || roleId == 2 || roleId == 5) {
                    flag = true;
                    break;
                }
            }
            return userRoleInfoService.approve(userRoleInfoParam, shopManager, flag);
        } else {
            return Result.wrapErrorResult("", "员工id不能为空");
        }
    }
}
