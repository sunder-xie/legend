package com.tqmall.legend.server.shop;

import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.object.result.shop.*;
import com.tqmall.legend.object.param.shop.QryShopParam;
import com.tqmall.legend.service.shop.RpcShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/12/9.
 */
@Slf4j
@Service("rpcShopService")
public class RpcShopServiceImpl implements RpcShopService{

    @Autowired
    ShopService shopService;

    @Autowired
    private ShopFunFacade shopFunFacade;

    @Override
    public Result getShopById(String source , Long id) {
        Shop shop = shopService.selectById(id);
        return Result.wrapSuccessfulResult(shop);
    }

    @Override
    public Result<QryShopPageDTO> getShopPage(QryShopParam qryShopParam) {
        if(qryShopParam==null){
            log.error("[dubbo]查询门店列表失败,参数为空");
            return Result.wrapErrorResult("-1","参数为空");
        }
        QryShopPageDTO qryShopPageDTO= new QryShopPageDTO();
        Integer offset = 0;
        Integer limit = 10;
        if(qryShopParam.getLimit()!=null){
            limit = qryShopParam.getLimit();
        }
        if(qryShopParam.getOffset()!=null){
            offset = qryShopParam.getOffset();
        }
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("offset",offset);
        searchMap.put("limit",limit);
        searchMap.put("sorts",qryShopParam.getSorts());
        searchMap.put("shopStatus",qryShopParam.getShopStatus());
        searchMap.put("nameLike",qryShopParam.getName());
        searchMap.put("citys",qryShopParam.getCityList());
        searchMap.put("userGlobalId",qryShopParam.getUcShopId());
        Integer total = shopService.getCount(searchMap);
        if(total==null||total==0){
            qryShopPageDTO.setTotal(0);
            return Result.wrapSuccessfulResult(qryShopPageDTO);
        }
        List<Shop> shopList = shopService.select(searchMap);
        List<QryShopDTO> content = new ArrayList<>();
        if(!CollectionUtils.isEmpty(shopList)){
            for(Shop shop:shopList){
                QryShopDTO qryShopDTO = new QryShopDTO();
                BeanUtils.copyProperties(shop,qryShopDTO);
                content.add(qryShopDTO);
            }
        }
        qryShopPageDTO.setTotal(total);
        qryShopPageDTO.setContent(content);
        return Result.wrapSuccessfulResult(qryShopPageDTO);
    }

    @Override
    public Result<ShopInfoDTO> findByShopId(Long shopId) {
        if (shopId == null) {
            return Result.wrapErrorResult("", "店铺id必须传入.");
        }

        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return Result.wrapErrorResult("", "根据id查找不到对应的店铺信息.");
        }

        ShopInfoDTO shopInfoDTO = new ShopInfoDTO();
        shopInfoDTO.setShopId(shop.getId());
        shopInfoDTO.setName(shop.getName());
        shopInfoDTO.setContact(shop.getContact());
        shopInfoDTO.setMobile(shop.getMobile());
        shopInfoDTO.setLevel(shop.getLevel());
        shopInfoDTO.setUserGlobalId(shop.getUserGlobalId());
        shopInfoDTO.setShopStatus(shop.getShopStatus());

        return Result.wrapSuccessfulResult(shopInfoDTO);
    }

    @Override
    public Result<ShopVoDTO> getShopInfo(Long shopId) {
        if (null == shopId || shopId < 1) {
            log.error("获取店铺信息失败 shopId={}", shopId);
        }
        Shop shop = shopService.selectById(shopId);
        if(null != shop){
            ShopVoDTO shopVoDTO = BdUtil.bo2do(shop,ShopVoDTO.class);
            return Result.wrapSuccessfulResult(shopVoDTO);
        }
        return Result.wrapErrorResult("","获取店铺信息失败");
    }

    /**
     * 通过userGlobalId 获取 shop
     *
     * @param userGlobalId
     * @return
     */
    @Override
    public Result<ShopDTO> getShopInfoByUserGlobalId(Long userGlobalId) {
        Shop shop = shopService.getShopByUserGlobalId(userGlobalId);
        if (null == shop){
            return Result.wrapErrorResult("",userGlobalId + "没有找到对应的门店");
        }
        ShopDTO shopDTO = BdUtil.bo2do(shop,ShopDTO.class);
        return Result.wrapSuccessfulResult(shopDTO);
    }

    @Override
    public Result<Boolean> isUseWorkshop(Long shopId) {
        if (null == shopId || shopId == 0) {
            return Result.wrapErrorResult("", "门店ID错误！");
        }
        boolean flag = false;
        try {
            flag = shopFunFacade.isUseWorkshop(shopId);
        } catch (Exception e) {
            log.error("[门店信息] 判断是否使用车间失败！shopId=" + shopId, e);
            return Result.wrapErrorResult("", "判断是否是车间失败！");
        }
        return Result.wrapSuccessfulResult(flag);
    }

}
