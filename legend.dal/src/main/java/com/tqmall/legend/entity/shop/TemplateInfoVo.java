package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by changqiang.ke on 16/1/13.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TemplateInfoVo {
    private Long serviceId;//服务id
    private String userGlobalId;//门店userGlobalId
}
