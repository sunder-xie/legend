package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.tqmall.cube.shop.result.marketing.gather.GatherOperateEffectStatDTO;
import com.tqmall.legend.facade.marketing.gather.vo.GatherOperateStatVO;

/**
 * Created by xin on 2016/12/24.
 */
public class GatherOperateStatConvertor {

    public static GatherOperateStatVO convert(GatherOperateEffectStatDTO gatherOperateStatDTO) {
        GatherOperateStatVO gatherOperateStatVO = new GatherOperateStatVO();
        gatherOperateStatVO.setPanHuoCustomer(DataStatConvertor.convert(gatherOperateStatDTO.getPanHuoCustomer()));
        gatherOperateStatVO.setLaXinCustomer(DataStatConvertor.convert(gatherOperateStatDTO.getLaXinCustomer()));
        gatherOperateStatVO.setSms(DataStatConvertor.convert(gatherOperateStatDTO.getSms()));
        gatherOperateStatVO.setPhone(DataStatConvertor.convert(gatherOperateStatDTO.getPhone()));
        gatherOperateStatVO.setWeChat(DataStatConvertor.convert(gatherOperateStatDTO.getWeChat()));
        gatherOperateStatVO.setReceiveCoupon(DataStatConvertor.convert(gatherOperateStatDTO.getReceiveCoupon()));

        gatherOperateStatVO.setToStoreOldCustomer(DataStatConvertor.convert(gatherOperateStatDTO.getToStoreOldCustomer()));
        gatherOperateStatVO.setToStoreNewCustomer(DataStatConvertor.convert(gatherOperateStatDTO.getToStoreNewCustomer()));
        gatherOperateStatVO.setNotToStoreCustomer(DataStatConvertor.convert(gatherOperateStatDTO.getNotToStoreCustomer()));
        gatherOperateStatVO.setTotalConsume(DataStatConvertor.convert(gatherOperateStatDTO.getTotalConsume()));
        gatherOperateStatVO.setOldCustomerConsume(DataStatConvertor.convert(gatherOperateStatDTO.getOldCustomerConsume()));
        gatherOperateStatVO.setNewCustomerConsume(DataStatConvertor.convert(gatherOperateStatDTO.getNewCustomerConsume()));
        return gatherOperateStatVO;
    }
}
