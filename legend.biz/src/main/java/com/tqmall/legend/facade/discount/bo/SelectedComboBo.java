package com.tqmall.legend.facade.discount.bo;

import lombok.Data;

/**
 * @Author 辉辉大侠
 * @Date:5:40 PM 02/03/2017
 */
@Data
public class SelectedComboBo {
    /**
     * 本次结算使用的计次卡
     */
    private Long comboServiceId;
    /**
     * 本次结算使用的计次卡服务次数
     */
    private Integer count;
}
