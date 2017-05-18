package com.tqmall.legend.facade.report.convert;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.businessoverview.BusinessPaidAmount;
import com.tqmall.cube.shop.result.businessoverview.PaidInStatisticsDTO;
import com.tqmall.legend.facade.report.vo.BusinessPaidInAmountVo;
import com.tqmall.legend.facade.report.vo.PaidInStatisticsVo;
import com.tqmall.wheel.lang.Objects;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
public class BusinessPaidInConvert implements Converter<PaidInStatisticsDTO, PaidInStatisticsVo> {
    @Override
    public PaidInStatisticsVo convert(PaidInStatisticsDTO source) {
        if (Objects.isNull(source)) {
            return null;
        }

        PaidInStatisticsVo paidInStatisticsVo = new PaidInStatisticsVo();
        paidInStatisticsVo.setTotalPaidAmount(source.getTotalPaidAmount());
        paidInStatisticsVo.setMemberCardPayAmount(source.getMemberCardPayAmount());

        List<BusinessPaidInAmountVo> businessPaidAmounts = Lists.newArrayListWithCapacity(source.getBusinessPaidAmountList().size());
        for (BusinessPaidAmount businessPaidAmount : source.getBusinessPaidAmountList()) {
            BusinessPaidInAmountVo businessPaidInAmountVo = new BusinessPaidInAmountVo();
            businessPaidInAmountVo.setBussinessTagId(businessPaidAmount.getBusinessTag().getTag());
            businessPaidInAmountVo.setBusinessTagName(businessPaidAmount.getBusinessTag().getName());
            businessPaidInAmountVo.setPaidAmount(businessPaidAmount.getPaidAmount());
            businessPaidAmounts.add(businessPaidInAmountVo);
        }

        paidInStatisticsVo.setBusinessPaidInAmountVoList(businessPaidAmounts);

        return paidInStatisticsVo;
    }
}
