package com.tqmall.legend.facade.account.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by xin on 2017/3/10.
 */
@Getter
@Setter
public class ComboServiceVo {
    private String name;//服务名
    private Integer totalCount;//总次数
    private Integer usedCount;//已使用次数
    private Integer leftCount;//剩余次数
}
