package com.tqmall.legend.entity.pub.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by jason on 15/9/18.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderAppVo {

    private List<String> mobileList;
    private Integer dayNumMin;//7天之外
    private Integer dayNumMax;//10天之内
}
