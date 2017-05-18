package com.tqmall.legend.object.param.order;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushuai on 2016/09/19.
 */
@Data
public class QryOrderParam implements Serializable {
    private static final long serialVersionUID = 3696960191985277351L;
    private Long userGlobalId;
    private String mobile;//车主电话
    private List[] licenseList;//车牌号列表
    private Integer limit;
    private Long offset;
}