package com.tqmall.legend.entity.customer;

/**
 * Created by tanghao on 17/2/24.
 */
import lombok.Getter;
import lombok.Setter;
import com.tqmall.legend.entity.base.BaseEntity;

@Getter
@Setter
public class CustomerCarRel extends BaseEntity {
    private Long shopId;//门店id
    private Long customerId;//关联客户id
    private Long accountId;//关联账户id
    private Long customerCarId;//关联车辆id
}


