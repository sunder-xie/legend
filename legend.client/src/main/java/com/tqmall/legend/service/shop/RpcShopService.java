package com.tqmall.legend.service.shop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.shop.QryShopParam;
import com.tqmall.legend.object.result.shop.QryShopPageDTO;
import com.tqmall.legend.object.result.shop.ShopDTO;
import com.tqmall.legend.object.result.shop.ShopInfoDTO;
import com.tqmall.legend.object.result.shop.ShopVoDTO;

/**
 * Created by lixiao on 15/12/9.
 */
public interface RpcShopService {

    @Deprecated
    public Result getShopById(String source, Long id);

    /**
     * 分页查询门店
     * @param qryShopParam
     * @return
     */
    public Result<QryShopPageDTO> getShopPage(QryShopParam qryShopParam);

    /**
     * 根据店铺id获取店铺信息
     * @param shopId
     * @return
     */
    Result<ShopInfoDTO> findByShopId(Long shopId);

    /**
     * 获取店铺信息 by mace
     * @param shopId
     * @return
     */
    public Result<ShopVoDTO> getShopInfo(Long shopId);

    /**
     * 通过userGlobalId 获取 shop
     * @param userGlobalId
     * @return
     */
    public Result<ShopDTO> getShopInfoByUserGlobalId(Long userGlobalId);


    /**
     * 判断门店是否开启了车间功能
     * @param shopId
     * @return
     */
    Result<Boolean> isUseWorkshop(Long shopId);
}
