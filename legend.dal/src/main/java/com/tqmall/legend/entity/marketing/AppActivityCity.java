package com.tqmall.legend.entity.marketing;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by jason on 15/11/4.
 * 活动对应的城市VO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppActivityCity {

    private Long cityId;
    private String cityName;

}
