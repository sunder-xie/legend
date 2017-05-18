package com.tqmall.legend.entity.tqcheck;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lifeilong on 2016/4/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TqCheckDetail extends BaseEntity {
    private Long shopId;    //店铺id
    private Long checkLogId;   //检测id 对应legend_tq_check_log表
    private Long categoryId; // 分类id
    private Integer category;   //类目
    private Integer categoryItem;   //检测类型
    private String categoryItemName;    //检测名称
    private Integer itemValueType;  //检测结果标志 1:正常运行 2:留意观察 3:建议更换
}
