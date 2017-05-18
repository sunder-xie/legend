package com.tqmall.legend.facade.customer.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 16/12/16.
 */
@Getter
@Setter
public class UnbindingBo {
    /**
     * 门店id
     */
    private Long shopId;
    /**
     * 员工id
     */
    private Long userId;
    /**
     * 操作人id
     */
    private Long operatorId;
    /**
     * 没值：客户归属全部调整
     * 有值：客户归属部分调整
     */
    private List<Long> customerCarIds;
}