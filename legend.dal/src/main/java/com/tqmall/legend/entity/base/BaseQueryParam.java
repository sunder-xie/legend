package com.tqmall.legend.entity.base;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Created by twg on 15/7/21.
 */
@Data
public class BaseQueryParam {
    private Integer cityId;

    private int uid;

    private String device;

    private String deviceId = "tqmall_web";

    private String system;

    private String version = "3.0";

    private int clientId;

    private int start = 0;

    private int limit = 15;

    private int imgType;

    /*  Pipes 2014-8-15 增加sessionId */
    private String sessionId;

    private int userCarId;

    private String source = "legend";//来源参数

    private Integer sellerId = 1;

    @JsonIgnore
    private String userTitle;
    @JsonIgnore
    private String requestSource = "legend";
}
