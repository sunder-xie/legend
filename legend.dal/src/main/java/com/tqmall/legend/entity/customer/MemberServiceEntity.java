package com.tqmall.legend.entity.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 结算页，会员服务转换对象
 *
 * Created by dongc on 15/8/3.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberServiceEntity {

    private String id;
    private String serviceCount;
    private String serviceValue;

}
