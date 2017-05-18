package com.tqmall.legend.facade.customer.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 16/12/21.
 */
@Getter
@Setter
public class AllotBo {
    /**
     * 选择的员工
     */
    private List<Long> choseUserIds;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 操作人id
     */
    private Long operatorId;
}
