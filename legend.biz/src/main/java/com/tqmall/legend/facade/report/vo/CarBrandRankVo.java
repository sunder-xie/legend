package com.tqmall.legend.facade.report.vo;

import lombok.Data;

/**
 * Created by 辉辉大侠 on 9/5/16.
 */
@Data
public class CarBrandRankVo {
    /**
     * 车品牌
     */
    private String carBrand;
    /**
     * 排名
     */
    private Integer rank;
    /**
     * 车品牌对应的工单数
     */
    private Integer receptionNumber;
}
