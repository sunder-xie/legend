package com.tqmall.legend.biz.settlement.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by xin on 16/6/4.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class DebitTypeVo implements Serializable {
    private static final long serialVersionUID = -4665574875473750177L;
    private Long id;
    private Long shopId;
    private String typeName;
}
