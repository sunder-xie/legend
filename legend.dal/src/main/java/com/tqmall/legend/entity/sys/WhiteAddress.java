package com.tqmall.legend.entity.sys;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

/**
 * 白名单相关代码
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WhiteAddress extends BaseEntity {

    private String ipAddress;
    private String loginBeginTime;
    private String loginEndTime;
    private Long shopId;
    private Long shopManagerId;
    private String shopManagerAccount;
}