package com.tqmall.legend.web.goods.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/2/23.
 */
@Getter
@Setter
public class GoodsSearchParam {
    private String goodsName;
    private String goodsSn;
    private String goodsFormat;
    private String goodsType = "0";//物料类型，0为实际物料，1为虚开物料，1已没有业务场景，所以默认0
    private String carInfoLike;
    private String goodsBrandLike;
    private String depotLike;
    private Integer zeroStockRange;//0:0库存   1:非0库存
    private Integer onsaleStatus;
    private String goodsCatLike;
    private Integer brandId;
}
