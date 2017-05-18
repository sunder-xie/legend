package com.tqmall.legend.web.vo.goods;

import lombok.Data;

import java.util.List;

/**
 * Created by wanghui on 12/8/15.
 */
@Data
public class ELCategoryResult {
    private Integer id;
    private String name;
    private String vehicleCode;
    private boolean isCustomCat;
    private List<ELCategoryResult> list;

    public ELCategoryResult(){{
        this.isCustomCat = false;
    }}
}
