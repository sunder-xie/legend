package com.tqmall.legend.entity.pvg;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PvgRole extends BaseEntity {

    private String name;
    private Integer status;

}

