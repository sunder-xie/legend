package com.tqmall.legend.facade.insurance.vo;

import com.tqmall.insurance.domain.result.InsuranceShopCommissionsRatioDTO;

import java.math.BigDecimal;

/**
 * Created by sven on 16/10/10.
 */
public class BonusAllocationRatioVo extends InsuranceShopCommissionsRatioDTO {
    public String getCommercialCommissions() {
        BigDecimal ratio = new BigDecimal("100").multiply(super.getCommercialCommissionsRatio());
        return ratio + "%";
    }

    public String getTrafficCommissions() {
        BigDecimal ratio = new BigDecimal("100").multiply(super.getTrafficCommissionsRatio());
        return ratio + "%";
    }
}
