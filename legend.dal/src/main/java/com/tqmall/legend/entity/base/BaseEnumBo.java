package com.tqmall.legend.entity.base;

import lombok.Data;

/**
 * Created by sven on 16/6/2.
 * 枚举类型前端获取所有值作为查询条件的实体BO
 * 当两个参数为String时,使用后两个
 */
@Data
public class BaseEnumBo {
    private Integer code;
    private String name;
    private String message;
}
