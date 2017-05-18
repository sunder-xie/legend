package com.tqmall.legend.object.result.account;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by majian on 16/9/9.
 */
@Data
public class ComboServiceDTO implements Serializable {
    private String name;//服务名
    private Integer totalCount;//总次数
    private Integer usedCount;//已使用次数
    private Integer leftCount;//剩余次数
}
