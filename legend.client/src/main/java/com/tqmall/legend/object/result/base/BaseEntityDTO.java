package com.tqmall.legend.object.result.base;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zsy on 15/12/10.
 */
@Data
public class BaseEntityDTO implements Serializable {
    private static final long serialVersionUID = -7259462310349646861L;
    private String source;//系统请求来源
}
