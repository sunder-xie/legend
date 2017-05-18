package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.tqmall.cube.shop.result.marketing.gather.LaXinStatDTO;
import com.tqmall.legend.facade.marketing.gather.vo.LaXinStatVO;

/**
 * Created by xin on 2016/12/22.
 */
public class LaXinStatConvertor {

    public static LaXinStatVO convert(LaXinStatDTO laXinStatDTO) {
        if (laXinStatDTO == null) {
            return null;
        }
        LaXinStatVO laXinStatVO = new LaXinStatVO();
        laXinStatVO.setOperateType(laXinStatDTO.getOperateType());
        laXinStatVO.setOperateTypeName(laXinStatDTO.getOperateTypeName());
        laXinStatVO.setGrantOldCustomerNum(laXinStatDTO.getGrantOldCustomerNum());
        laXinStatVO.setReceiveOldCustomerNum(laXinStatDTO.getReceiveOldCustomerNum());
        laXinStatVO.setReceiveNewCustomerNum(laXinStatDTO.getReceiveNewCustomerNum());
        laXinStatVO.setToStoreOldCustomerNum(laXinStatDTO.getToStoreOldCustomerNum());
        laXinStatVO.setToStoreNewCustomerNum(laXinStatDTO.getToStoreNewCustomerNum());
        laXinStatVO.setIncome(laXinStatDTO.getIncome());
        laXinStatVO.setConversionRate(laXinStatDTO.getConversionRate());
        return laXinStatVO;
    }

}
