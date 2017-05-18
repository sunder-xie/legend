package com.tqmall.legend.entity.shop;

/**
 * Created by litan on 15-5-26.
 */

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopConfigure extends BaseEntity {
    private Long shopId;
    private Long confType;
    private String confValue;
    private String confKey;
    private Long roleId;
}
