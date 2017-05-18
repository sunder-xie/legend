package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.businessoverview.BusinessPaidAmount;
import com.tqmall.cube.shop.result.businessoverview.PaidOutStatisticsDTO;
import com.tqmall.legend.facade.report.vo.BusinessPaidOutAmountVo;
import com.tqmall.legend.facade.report.vo.PaidOutStatisticsVo;
import com.tqmall.wheel.lang.Objects;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class BusinessPaidOutConvert implements Converter<PaidOutStatisticsDTO, PaidOutStatisticsVo> {

    @Override
    public PaidOutStatisticsVo convert(PaidOutStatisticsDTO source) {
        if (Objects.isNull(source)) {
            return null;
        }
        PaidOutStatisticsVo paidOutStatisticsVo = new PaidOutStatisticsVo();
        paidOutStatisticsVo.setTotalPaidAmount(source.getTotalPaidAmount());
        List<BusinessPaidOutAmountVo> businessPaidOutAmountVos = Lists.<BusinessPaidOutAmountVo>newArrayListWithCapacity(source.getBusinessPaidAmountList().size());

        for (BusinessPaidAmount businessPaidAmount : source.getBusinessPaidAmountList()) {
            BusinessPaidOutAmountVo businessPaidOutAmountVo = new BusinessPaidOutAmountVo();
            businessPaidOutAmountVo.setBussinessTagId(businessPaidAmount.getBusinessTag().getTag());
            businessPaidOutAmountVo.setBusinessTagName(businessPaidAmount.getBusinessTag().getName());
            businessPaidOutAmountVo.setPaidAmount(businessPaidAmount.getPaidAmount());

            businessPaidOutAmountVos.add(businessPaidOutAmountVo);
        }

        paidOutStatisticsVo.setBusinessPaidOutAmountVoList(businessPaidOutAmountVos);
        return paidOutStatisticsVo;
    }
}
