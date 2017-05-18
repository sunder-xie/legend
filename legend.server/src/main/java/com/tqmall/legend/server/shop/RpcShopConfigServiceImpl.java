package com.tqmall.legend.server.shop;

import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.component.converter.DataNoteShopConfigConverter;
import com.tqmall.legend.biz.component.converter.DataShopConfigDTOConverter;
import com.tqmall.legend.biz.component.converter.DataShopConfigParamConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.object.param.note.ShopConfigParam;
import com.tqmall.legend.object.result.config.ShopConfigDTO;
import com.tqmall.legend.service.shop.RpcShopConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by twg on 16/4/11.
 */
@Service("rpcShopConfigService")
public class RpcShopConfigServiceImpl implements RpcShopConfigService {
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private ShopService shopService;

    @Override
    public Result getConfValue(Long shopId, Integer confType, String confKey) {
        return Result.wrapSuccessfulResult(shopConfigureService.getShopConfigure(shopId, confType, confKey));
    }

    @Override
    public Result getConfValue(Long shopId, Integer confType) {
        return Result.wrapSuccessfulResult(shopConfigureService.getShopConfigure(shopId,confType,new DataNoteShopConfigConverter<ShopConfigParam>()));
    }

    @Override
    public Result saveOrUpdateValue(Long shopId, Integer confType, ShopConfigParam shopConfigParam) {
        return Result.wrapSuccessfulResult(shopConfigureService.saveOrUpdateShopConfigure(shopId,confType,new DataShopConfigParamConverter<ShopConfigParam>(),shopConfigParam));
    }

    @Override
    public Result<List<ShopConfigDTO>> getShopConfigByShopIdAndConfType(Long userGlobalId, Integer confType) {
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if(shop == null){
            return Result.wrapErrorResult("","根据userGlobalId="+userGlobalId+"，获取门店信息为空");
        }
        if(confType == null){
            return Result.wrapErrorResult("","门店配置类型为空");
        }
        List<ShopConfigDTO> shopConfigDTOs = shopConfigureService.getShopConfigure(shop.getId(), confType, new DataShopConfigDTOConverter<List<ShopConfigDTO>>());
        shopConfigDTOs = BdUtil.do2bo4List(shopConfigDTOs,ShopConfigDTO.class);
        return Result.wrapSuccessfulResult(shopConfigDTOs);
    }
}
