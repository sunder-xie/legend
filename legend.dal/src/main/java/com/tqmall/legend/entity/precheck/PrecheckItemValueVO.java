package com.tqmall.legend.entity.precheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by guangxue on 14/10/30.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckItemValueVO {
    private Integer id;
    private String value;
}
