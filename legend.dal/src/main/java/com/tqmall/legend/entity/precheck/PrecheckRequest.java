package com.tqmall.legend.entity.precheck;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckRequest extends BaseEntity {
    private Long shopId;
    private Long precheckId;
    private String content;
    private String contentGoods;

}


