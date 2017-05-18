package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by QXD on 2015/6/8.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerSource extends BaseEntity {

    private Long shopId;
    private String source;
}
