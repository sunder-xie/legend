package com.tqmall.legend.entity.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 创建表单，提交实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppointEntityVo implements Serializable {

    // 预约单基本信息
    private Appoint appoint;
    // 预约的服务JSON
    private String appointServiceJson;
    // 预约的物料JSON
    private String appointGoodJson;
    // 区分{1:"保存" 2:"确认预约" 3:"新增"}
    private String flags;
}
