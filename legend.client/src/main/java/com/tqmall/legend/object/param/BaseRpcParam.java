package com.tqmall.legend.object.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRpcParam implements Serializable {

    private static final long serialVersionUID = -8451796751304071428L;

    private String source;  //系统请求来源
    private Integer creator;   //创建人
    private Integer modifier;  //修改人
}
