package com.tqmall.legend.server.warehouse;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.billcenter.client.dto.PayBillFlowDTO;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.warehouse.WarehouseInFacade;
import com.tqmall.legend.facade.warehouse.bo.WarehouseInDetailBo;
import com.tqmall.legend.object.param.warehouse.WarehouseInDetailParam;
import com.tqmall.legend.service.warehouse.RpcWarehouseInService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sven on 16/11/22.
 */
@Service("rpcWarehouseInService")
public class RpcWarehouseInServiceImpl implements RpcWarehouseInService {
    @Resource
    private WarehouseInFacade warehouseInFacade;
    @Resource
    private ShopManagerService shopManagerService;

    @Override
    public Result<Boolean> batchWarehouseIn(final List<WarehouseInDetailParam> warehouseInDetailParamList) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notEmpty(warehouseInDetailParamList, "入库商品数据不能为空");
                Assert.notNull(warehouseInDetailParamList.get(0).getShopId(), "门店ID不能为空");
                Assert.notNull(warehouseInDetailParamList.get(0).getUserId(), "操作人不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long userId = warehouseInDetailParamList.get(0).getUserId();
                Long shopId = warehouseInDetailParamList.get(0).getShopId();
                ShopManager shopManager = shopManagerService.selectByShopIdAndManagerId(shopId,userId);
                if(shopManager == null){
                    throw  new BizException("用户信息有误,入库失败");
                }
                List<WarehouseInDetailBo> boList = Lists.newArrayList();
                for (WarehouseInDetailParam param : warehouseInDetailParamList) {
                    WarehouseInDetailBo bo = new WarehouseInDetailBo();
                    BeanUtils.copyProperties(param, bo);
                    boList.add(bo);
                }
                UserInfo userInfo = new UserInfo();
                userInfo.setName(shopManager.getName());
                userInfo.setUserId(userId);
                userInfo.setShopId(shopId);
                warehouseInFacade.batchStock(boList, userInfo);
                return true;
            }
        }.execute();


    }


}
