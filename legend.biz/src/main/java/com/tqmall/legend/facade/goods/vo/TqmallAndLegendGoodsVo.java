package com.tqmall.legend.facade.goods.vo;

import com.tqmall.tqmallstall.domain.result.goods.GoodsListForSearchBaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 档口配件关联采购配件
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TqmallAndLegendGoodsVo extends GoodsListForSearchBaseDTO.GoodsListDTO implements Serializable {
    private static final long serialVersionUID = -233531411218214218L;

    // 采购配件ID
    private Long legendGoodsId;
    // 采购价
    private BigDecimal legendGoodsPrice;
    // 库位
    private String legendGoodsDepot;


}
