package com.tqmall.legend.server.shop;

import com.google.common.base.Optional;
import com.tqmall.common.exception.ApiCheckedException;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.api.entity.PvgUserOrgResp;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.biz.api.IPvgService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.object.result.pvg.PvgUserOrgRespDTO;
import com.tqmall.legend.object.result.pvg.RoleInfoRespDTO;
import com.tqmall.legend.object.result.pvg.ShopManagerRespDTO;
import com.tqmall.legend.object.result.shop.ShopManagerDTO;
import com.tqmall.legend.service.shop.RpcShopManagerService;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xiangDong.qu on 16/4/13.
 */
@Service("rpcShopManagerService")
@Slf4j
public class RpcShopManagerServiceImpl implements RpcShopManagerService {

    @Autowired
    private ShopManagerDao shopManagerDao;

    @Autowired
    private ShopManagerService shopManagerService;

    @Autowired
    private IPvgUserOrgService pvgUserOrgService;

    @Autowired
    private IPvgService pvgService;

    /**
     * 获取店铺的员工
     *
     * @param shopId
     *
     * @return
     */
    @Override
    public Result<List<ShopManagerDTO>> getShopManager(Long shopId) {
        List<ShopManager> shopManagerList = shopManagerDao.selectShopManagerByShopId(shopId);
        List<ShopManagerDTO> shopManagerDTOs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(shopManagerList)) {
            for (ShopManager shopManager : shopManagerList) {
                ShopManagerDTO shopManagerDTO = new ShopManagerDTO();
                BeanUtils.copyProperties(shopManager, shopManagerDTO);
                shopManagerDTOs.add(shopManagerDTO);
            }
        }
        return Result.wrapSuccessfulResult(shopManagerDTOs);
    }

    /**
     * 检查当前用户是否存在
     *
     * @param userIdSet 用户id集合
     * @param shopId    店铺id
     *
     * @return Set<Long> 存在的用户id集合
     */
    @Override
    public Result<Set<Long>> checkExist(final Long shopId, final Set<Long> userIdSet) {
        com.tqmall.core.common.entity.Result<Set<Long>> result = new ApiTemplate<Set<Long>>() {
            @Override
            protected void checkParams() {
                //参数效验 统一抛出IllegalArgumentException异常  在模板类中统一处理
                Assert.notNull(shopId, "传入shopId不能为空");
                Assert.isTrue(shopId > 0, "shopId错误");
                Assert.notEmpty(userIdSet, "用户id入参为空");
            }

            @Override
            protected Set<Long> process() {
                //业务逻辑处理
                return shopManagerService.checkExist(shopId, userIdSet);
            }
        }.execute();

        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return LegendErrorCode.APP_TEMPLATE_ERROR.newResult(result.getMessage());
        }
    }

    /**
     * 获取店铺员工角色列表
     *
     * @param shopId 店铺Id
     *
     * @return
     */
    @Override
    public com.tqmall.core.common.entity.Result<List<PvgUserOrgRespDTO>> getShopManagerRoleList(final Long shopId) {
        return new ApiTemplate<List<PvgUserOrgRespDTO>>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "店铺信息错误");
                Assert.isTrue(shopId > 0, "店铺信息错误");
            }

            @Override
            protected List<PvgUserOrgRespDTO> process() throws BizException {
                List<PvgUserOrgRespDTO> pvgUserOrgRespDTOList = new ArrayList<>();
                Optional<List<PvgUserOrgResp>> pvgUserOrgRespListOptional = pvgUserOrgService.getReferRoleList(shopId);
                if (pvgUserOrgRespListOptional.isPresent()) {
                    List<PvgUserOrgResp> pvgUserOrgRespList = pvgUserOrgRespListOptional.get();
                    if (!CollectionUtils.isEmpty(pvgUserOrgRespList)) {
                        for (PvgUserOrgResp pvgUserOrgResp : pvgUserOrgRespList) {
                            PvgUserOrgRespDTO pvgUserOrgRespDTO = new PvgUserOrgRespDTO();
                            BeanUtils.copyProperties(pvgUserOrgResp, pvgUserOrgRespDTO);
                            pvgUserOrgRespDTOList.add(pvgUserOrgRespDTO);
                        }
                    }
                }
                return pvgUserOrgRespDTOList;
            }
        }.execute();
    }


    /**
     * 获取店铺员工信息
     *
     * @param shopId    店铺信息
     * @param managerId 员工Id
     *
     * @return
     */
    @Override
    public com.tqmall.core.common.entity.Result<ShopManagerRespDTO> getUserInfo(final Long shopId, final Long managerId) {
        return new ApiTemplate<ShopManagerRespDTO>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "店铺信息错误");
                Assert.notNull(managerId, "用户信息错误");
                Assert.isTrue(shopId > 0, "店铺信息错误");
                Assert.isTrue(managerId > 0, "用户信息错误");
            }

            @Override
            protected ShopManagerRespDTO process() throws BizException {
                ShopManagerResp userInfoResp = null;
                userInfoResp = pvgService.getUserInfo(managerId.intValue(), shopId);
                if (null == userInfoResp) {
                    log.error("[API]获取用户基本信息失败,shopId:{},managerId:{}", shopId, managerId);
                    throw new BizException("获取用户基本信息失败");
                }

                ShopManagerRespDTO shopManagerRespDTO = new ShopManagerRespDTO();
                BeanUtils.copyProperties(userInfoResp, shopManagerRespDTO);

                List<RoleInfoResp> roleInfoRespList = (List<RoleInfoResp>) userInfoResp.getRoleList();
                if (!CollectionUtils.isEmpty(roleInfoRespList)) {
                    List<RoleInfoRespDTO> roleInfoRespDTOList = new ArrayList<RoleInfoRespDTO>();
                    for (RoleInfoResp roleInfoResp : roleInfoRespList) {
                        RoleInfoRespDTO roleInfoRespDTO = new RoleInfoRespDTO();
                        BeanUtils.copyProperties(roleInfoResp, roleInfoRespDTO);
                        roleInfoRespDTOList.add(roleInfoRespDTO);
                    }
                    shopManagerRespDTO.setRoleList(roleInfoRespDTOList);
                }
                return shopManagerRespDTO;
            }
        }.execute();
    }
}
