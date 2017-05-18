package com.tqmall.legend.facade.customer.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/21.
 * 分配结果
 */
@Getter
@Setter
public class AllotResultVo {
    /**
     * 员工id
     */
    private Long userId;

    /**
     * 员工姓名
     */
    private String userName;

    /**
     * 活跃客户
     */
    private Integer active;

    /**
     * 休眠客户
     */
    private Integer lazy;

    /**
     * 流失客户
     */
    private Integer lost;

    /**
     * 本次分配
     */
    private Integer allot;

    /**
     * 负责客户总数
     */
    private Integer totalAllot;
}
