package com.tqmall.legend.facade.service.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wushuai on 16/8/6.
 */
@Data
public class SaveShopServiceInfoVo{
    private Long shopServiceId;// 服务id
    private Long serviceTplId;//服务模版id
    private BigDecimal servicePrice;//服务实体价格
    private BigDecimal marketPrice;//市场价
    private BigDecimal downPayment;//预约定金
}
