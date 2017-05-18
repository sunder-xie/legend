package com.tqmall.legend.object.result.finance;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by twg on 16/1/26.
 */
@Data
public class PurchasePlanDTO implements Serializable {
    private Integer count;//记录总条数
    private List<PurchaseGoodsDTO> purchaseGoodsDTOList;//商品列表
}
