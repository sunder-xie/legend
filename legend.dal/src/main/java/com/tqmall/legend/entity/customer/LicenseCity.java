package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/6.
 * 车牌号城市对应实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LicenseCity extends BaseEntity{
    private String license;
    private String firstLetter;
    private String province;
    private String cityName;
    private String cityId;

}
