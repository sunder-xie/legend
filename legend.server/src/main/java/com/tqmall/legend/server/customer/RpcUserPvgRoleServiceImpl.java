package com.tqmall.legend.server.customer;

import com.google.common.base.Optional;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.object.result.customer.RoleInfoDTO;
import com.tqmall.legend.service.customer.RpcUserPvgRoleService;
import com.tqmall.zenith.errorcode.support.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangDong.qu on 16/4/10.
 */
@Service ("rpcUserPvgRoleService")
public class RpcUserPvgRoleServiceImpl implements RpcUserPvgRoleService {

    @Autowired
    private IPvgUserOrgService pvgUserOrgService;

    /**
     * 获取用户角色列表
     *
     * @param shopId 店铺id
     * @param userId 用户id
     *
     * @return
     */
    @Override
    public Result<List<RoleInfoDTO>> getUserPvgRoleList(Long shopId, Long userId) {
        List<RoleInfoDTO> roleInfoDTOList = new ArrayList<>();
        Optional<List<RoleInfoResp>> roleInfoRespListOptional = pvgUserOrgService.getReferRoleList(Integer.valueOf(userId + ""), shopId);
        if (roleInfoRespListOptional.isPresent()) {
            for (RoleInfoResp roleInfoResp : roleInfoRespListOptional.get()) {
                RoleInfoDTO roleInfoDTO = new RoleInfoDTO();
                roleInfoDTO.setRoleId(roleInfoResp.getRoleId());
                roleInfoDTO.setRoleName(roleInfoResp.getRoleName());
                roleInfoDTOList.add(roleInfoDTO);
            }
        }
        return Result.wrapSuccessfulResult(roleInfoDTOList);
    }
}
