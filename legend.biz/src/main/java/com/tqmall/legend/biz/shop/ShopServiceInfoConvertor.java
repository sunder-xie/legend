package com.tqmall.legend.biz.shop;

import com.tqmall.itemcenter.object.result.shopServiceInfo.ServiceGoodsSuiteDTO;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceInfoDTO;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceInfoBo;
import org.springframework.beans.BeanUtils;

/**
 * Created by wushuai on 17/1/8.
 */
public class ShopServiceInfoConvertor {
    public static ShopServiceInfoDTO convert(ShopServiceInfoBo shopServiceInfoBo) {
        if (shopServiceInfoBo == null) {
            return null;
        }
        ShopServiceInfo shopServiceInfo = shopServiceInfoBo.getShopServiceInfo();
        if (shopServiceInfo == null) {
            return null;
        }
        ShopServiceInfoDTO shopServiceInfoDTO = new ShopServiceInfoDTO();
        BeanUtils.copyProperties(shopServiceInfo, shopServiceInfoDTO);
        if(shopServiceInfo.getAppCateId()!=null){
            shopServiceInfoDTO.setAppCateId(shopServiceInfo.getAppCateId().longValue());
        }
        if(shopServiceInfo.getPriceType()!=null){
            shopServiceInfoDTO.setPriceType(shopServiceInfo.getPriceType().intValue());
        }
        ServiceGoodsSuite serviceGoodsSuite = shopServiceInfoBo.getServiceGoodsSuite();
        if (serviceGoodsSuite != null) {
            ServiceGoodsSuiteDTO serviceGoodsSuiteDTO = new ServiceGoodsSuiteDTO();
            BeanUtils.copyProperties(serviceGoodsSuite, serviceGoodsSuiteDTO);
            shopServiceInfoDTO.setServiceGoodsSuiteDTO(serviceGoodsSuiteDTO);
        }
        return shopServiceInfoDTO;
    }
}
