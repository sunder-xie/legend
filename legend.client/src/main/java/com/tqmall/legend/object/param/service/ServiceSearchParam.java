package com.tqmall.legend.object.param.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by macx on 2017/2/10.
 */
@Getter
@Setter
public class ServiceSearchParam implements Serializable{
    private static final long serialVersionUID = 2344201664650941874L;

    private Long shopId;
    private String serviceSn;
    private String serviceName;
    private String type;
    private String suiteNumLT;
    private Integer size;
}
