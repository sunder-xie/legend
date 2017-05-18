package com.tqmall.legend.object.result.tqcheck;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lifeilong on 2016/4/14.
 */
@Data
public class TqCheckItemDTO implements Serializable{
    private static final long serialVersionUID = -7820468399906222119L;
    private Integer categoryItem;   //检测类型
    private String categoryItemName;    //检测名称
    private Integer itemValueType;  //检测结果标志 1:正常运行 2:留意观察 3:建议更换
    private String itemValueTypeName; // 检测结果名称

    private String itemValueTypeOne;     //检测结果名称1 正常运行
    private String itemValueTypeTwo;     //检测结果名称2  留意观察
    private String itemValueTypeThree;   //检测结果名称3  建议更换

    private String flagOne;     //标志名称1 如:轮胎-花纹深度的: 低 - 中 - 高
    private String flagTwo;     //标志名称2
    private String flagThree;   //标志名称3
}
