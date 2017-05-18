package com.tqmall.legend.object.result.settlement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/6/28.
 */
@Data
@EqualsAndHashCode
@ToString
public class HistoryFlowDTO implements Serializable {

    private static final long serialVersionUID = -1428820735094882405L;

    private String name;
    private BigDecimal amount;
    private Date gmtCreate;
    private String operatorName;

}
