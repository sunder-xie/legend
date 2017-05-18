package com.tqmall.legend.object.result.goods;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/11/25.
 */
@Getter
@Setter
public class GoodsBrandDTO implements Serializable {
    private static final long serialVersionUID = 8688559169074520416L;

    private Long id;//品牌id
    private String brandName;//品牌名称
}
