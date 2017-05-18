package com.tqmall.legend.facade.customer.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 16/12/27.
 * 客户分配归属记录对象
 */
@Getter
@Setter
public class CustomerUserRelAllotBo {
    Integer index = 0;//分配的人的数组下标
    Long preCustomerId;//上次一分配的数据的客户id
    Long preUserId;//上一次分配数据的分配员工id
    boolean isUseIndex = false;//分配的员工数组下标是否用过
}
