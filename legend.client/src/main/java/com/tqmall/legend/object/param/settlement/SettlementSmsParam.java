package com.tqmall.legend.object.param.settlement;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 17/2/28.
 * 核销短信对象
 */
@Getter
@Setter
public class SettlementSmsParam implements Serializable {

    private static final long serialVersionUID = 5353526713777161193L;

    private Long shopId;
    private String license;
    private String mobile;
}
