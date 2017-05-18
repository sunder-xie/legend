package com.tqmall.legend.pojo.shopnote;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xin on 16/10/8.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopNoteTypeNum {
    // 提醒类型
    private Integer noteType;
    // 提醒数量
    private Integer num;
}
