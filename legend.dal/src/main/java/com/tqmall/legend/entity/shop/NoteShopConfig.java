package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by zhoukai on 16/3/14.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class NoteShopConfig implements Serializable {

    private Long shopId;
    private Integer confType;
    //首次提前多少天
    private Integer firstValue;
    //第二次提醒提前多少天
    private Integer secondValue;
    //过期多少天失效
    private Integer invalidValue;
}
