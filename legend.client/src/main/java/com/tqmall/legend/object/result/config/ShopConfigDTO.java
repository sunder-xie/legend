package com.tqmall.legend.object.result.config;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by twg on 16/10/26.
 */
@Getter
@Setter
public class ShopConfigDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 2827728656248331903L;
    private Long shopId;
    private Long confType;
    private String confKey;
    private String confValue;
}
