package com.tqmall.legend.object.param.warehouseshare;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xin on 2016/11/23.
 */
@Data
public class WarehouseShareSearchParam implements Serializable {
    private String goodsCate;
    private String goodsName;
    private Long shopId;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
