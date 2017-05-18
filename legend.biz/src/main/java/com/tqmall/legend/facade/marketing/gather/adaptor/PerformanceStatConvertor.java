package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.tqmall.cube.shop.result.marketing.gather.PerformanceStatDTO;
import com.tqmall.legend.facade.marketing.gather.vo.PerformanceStatVO;

/**
 * Created by xin on 2016/12/24.
 */
public class PerformanceStatConvertor {

    public static PerformanceStatVO convert(PerformanceStatDTO performanceStatDTO) {
        PerformanceStatVO performanceStatVO = new PerformanceStatVO();
        performanceStatVO.setTotal(DataStatConvertor.convert(performanceStatDTO.getTotal()));
        performanceStatVO.setToStoreNewCustomerReward(DataStatConvertor.convert(performanceStatDTO.getToStoreNewCustomerReward()));
        performanceStatVO.setPerformanceBelongReward(DataStatConvertor.convert(performanceStatDTO.getPerformanceBelongReward()));
        performanceStatVO.setPerformanceStar(DataStatConvertor.convert(performanceStatDTO.getPerformanceStar()));
        performanceStatVO.setRepairPerformance(DataStatConvertor.convert(performanceStatDTO.getRepairPerformance()));
        performanceStatVO.setSalePerformance(DataStatConvertor.convert(performanceStatDTO.getSalePerformance()));
        performanceStatVO.setServiceAdvisorPerformance(DataStatConvertor.convert(performanceStatDTO.getServiceAdvisorPerformance()));
        return performanceStatVO;
    }
}
