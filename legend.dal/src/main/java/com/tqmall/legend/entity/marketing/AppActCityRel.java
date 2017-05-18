package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jason on 15/11/4.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppActCityRel extends BaseEntity {

    private Long appActId;
    private Long cityId;
    private String cityName;

}
