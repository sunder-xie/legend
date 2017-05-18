package com.tqmall.legend.facade.customer.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/16.
 * 分配过客户的员工对象
 */
@Getter
@Setter
public class AllotUserVo {
    /**
     * 员工id
     */
    private Long userId;

    /**
     * 员工姓名
     */
    private String userName;
}
