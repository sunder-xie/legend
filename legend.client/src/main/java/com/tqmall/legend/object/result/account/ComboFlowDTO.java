package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by majian on 16/9/6.
 */
@Data
public class ComboFlowDTO implements Serializable {
    private Integer consumeType;//消费类型 3.消费 4.消费撤销
    private String consumeContent;//消费项目
    private Date consumeTime;//消费时间
    private Integer consumeCount = 1;//消费次数

    public enum ConsumeType {
        CONSUME,
        CONSUME_REVERSE
    }
}
