package com.tqmall.legend.entity.buy;

import lombok.Data;

import java.util.List;

/**
 * Created by Mokala on 7/20/15.
 */
@Data
public class GoodsAttrVo {
    private Long id;
    private String name;
    private List<String> list;
}
