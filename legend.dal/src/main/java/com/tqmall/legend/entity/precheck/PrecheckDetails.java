package com.tqmall.legend.entity.precheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckDetails extends BaseEntity {
    private Long shopId;
    private Long precheckId;
    private Long precheckItemId;
    private Long precheckItemType;
    private String precheckItemName;
    private Long precheckValueId;
    private String precheckValue;
    private String precheckValueType;
    private Long handBy;
    private boolean redFlag;
    private String ftlId;
    private String suggestion;
}

