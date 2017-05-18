package com.tqmall.legend.facade.privilege.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.privilege.RolesFuncRelService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.RolesFuncRel;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.privilege.RolesFuncFacade;
import com.tqmall.legend.facade.privilege.bo.RolesFuncBo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zsy on 17/1/5.
 */
@Service
public class RolesFuncFacadeImpl implements RolesFuncFacade {
    @Autowired
    private RolesFuncRelService rolesFuncRelService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private FuncService funcService;

    @Override
    @Transactional
    public boolean updateRolesFuncRel(RolesFuncBo rolesFuncBo) {
        String funcIdsStr = rolesFuncBo.getFuncIdsStr();
        //删除旧功能
        Long shopId = rolesFuncBo.getShopId();
        Long rolesId = rolesFuncBo.getRolesId();
        rolesFuncRelService.delete(shopId, rolesId);
        if (StringUtils.isBlank(funcIdsStr)) {
            return true;
        }
        List<RolesFuncRel> insertList = Lists.newArrayList();
        //添加新功能
        String[] funcIdArray = funcIdsStr.split(",");
        for (String funcId : funcIdArray) {
            RolesFuncRel rolesFuncRel = new RolesFuncRel();
            rolesFuncRel.setRolesId(rolesFuncBo.getRolesId());
            rolesFuncRel.setCreator(rolesFuncBo.getUserId());
            rolesFuncRel.setShopId(rolesFuncBo.getShopId());
            rolesFuncRel.setFuncId(Long.valueOf(funcId));
            insertList.add(rolesFuncRel);
        }
        rolesFuncRelService.batchInsert(insertList);
        return true;
    }

    @Override
    public void initAdminRolesFuncRel(Long shopId, Long rolesId) {
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            throw new BizException("门店不存在");
        }
        Integer level = shop.getLevel();
        List<FuncF> funcFList = funcService.getFuncLs(level);
        if (CollectionUtils.isEmpty(funcFList)) {
            return;
        }
        List<Long> rolesIdList = rolesFuncRelService.selectFuncIdsByRolesId(shopId, rolesId);
        //已存在数据，不用初始化
        if(CollectionUtils.isNotEmpty(rolesIdList)){
            return;
        }
        List<RolesFuncRel> insertList = Lists.newArrayList();
        for (FuncF funcF : funcFList) {
            RolesFuncRel rolesFuncRel = new RolesFuncRel();
            rolesFuncRel.setRolesId(rolesId);
            rolesFuncRel.setCreator(0l);
            rolesFuncRel.setShopId(shopId);
            rolesFuncRel.setFuncId(funcF.getId());
            insertList.add(rolesFuncRel);
        }
        rolesFuncRelService.batchInsert(insertList);
    }
}
