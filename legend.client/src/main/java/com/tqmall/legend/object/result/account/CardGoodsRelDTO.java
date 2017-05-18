package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 辉辉大侠 on 03/11/2016.
 */
@Data
public class CardGoodsRelDTO implements Serializable{
    private Integer goodsCatSource;//1:标准分类2:自定义分类
    private Long goodsCatId;//配件id
    private String goodsCatName;//配件名
    private BigDecimal discount;//配件折扣
}
