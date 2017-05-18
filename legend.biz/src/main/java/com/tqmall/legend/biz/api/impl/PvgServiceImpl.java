package com.tqmall.legend.biz.api.impl;

import com.google.common.base.Optional;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.api.entity.PvgUserOrgResp;
import com.tqmall.legend.api.entity.RoleInfoResp;
import com.tqmall.legend.api.entity.ShopManagerResp;
import com.tqmall.legend.biz.api.IPvgService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色service impl
 * <p/>
 * Created by dongc on 15/9/21.
 */
@Service
public class PvgServiceImpl implements IPvgService {

    @Autowired
    ShopManagerService shopManagerService;

    @Autowired
    IPvgUserOrgService pvgUserOrgService;

    @Autowired
    private ShopConfigureService shopConfigureService;

    @Autowired
    private ShopFunFacade shopFunFacade;

    @Autowired
    private ShopService shopService;

    @Override
    public ShopManagerResp getUserInfo(Integer managerId, Long shopId) throws BizException {
        //获取店铺信息
        Long userGlobalId = shopService.getUserGlobalId(shopId);
        if (null == userGlobalId) {
            throw new BizException("获取店铺信息失败");
        }
        Optional<ShopManagerResp> shopManagerRespOptional = shopManagerService.getManagerInfo(managerId, shopId);
        if (!shopManagerRespOptional.isPresent()) {
            throw new BizException("用户基本信息不存在");
        }
        ShopManagerResp userInfoResp = shopManagerRespOptional.get();
        Optional<List<RoleInfoResp>> roleInfoRespListOptional = pvgUserOrgService.getReferRoleList(managerId, shopId);
        if (roleInfoRespListOptional.isPresent()) {
            userInfoResp.setRoleList(roleInfoRespListOptional.get());
        } else {
            userInfoResp.setRoleList(new Object());
        }
        // 设置安全等级
        Optional<ShopConfigure> optional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.SHOPSECURITYLEVEL,shopId);
        if (optional.isPresent()) {
            ShopConfigure shopConfigure = optional.get();
            userInfoResp.setSaveLevel(shopConfigure.getConfValue());
        }
        // 设置安心模式
        String axInsuranceModel = shopFunFacade.getAnxinInsuranceModel(null,String.valueOf(userGlobalId));
        userInfoResp.setAxInsuranceModel(axInsuranceModel);
        return userInfoResp;
    }

    @Override
    public Object getRoleList(Long shopid) {
        Optional<List<PvgUserOrgResp>> pvgUserOrgRespListOptional = pvgUserOrgService.getReferRoleList(shopid);
        if (pvgUserOrgRespListOptional.isPresent()) {
            return pvgUserOrgRespListOptional.get();
        } else {
            return new Object();
        }
    }
}
