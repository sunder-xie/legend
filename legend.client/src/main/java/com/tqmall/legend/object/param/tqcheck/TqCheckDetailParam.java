package com.tqmall.legend.object.param.tqcheck;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by lifeilong on 2016/4/13.
 */
@Data
public class TqCheckDetailParam implements Serializable {
    private static final long serialVersionUID = 760962170401478697L;

    private Integer category;   //类目
    private Integer categoryItem ;  //检测类别
    private Integer itemValueType;    //检测结果标志 1:正常运行 2:留意观察 3:建议更换
}
