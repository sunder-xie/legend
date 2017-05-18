package com.tqmall.legend.entity.precheck;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckItems extends BaseEntity {
    private String name;
    private Integer valueType;
    private Integer itemType;

}
