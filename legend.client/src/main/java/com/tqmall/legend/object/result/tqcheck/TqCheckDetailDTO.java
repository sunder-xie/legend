package com.tqmall.legend.object.result.tqcheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lifeilong on 2016/4/12.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TqCheckDetailDTO implements Serializable {
    private static final long serialVersionUID = -7548260039438293126L;

    private Long categoryId;   //类目id
    private Integer category;   //类目
    private String categoryName;  // 类目名称
    private String categoryDetailName; // 具体检测项目(范围)名称
    private List<TqCheckItemDTO> tqCheckItemDTOList;    //每个项目下详情列表
    private Long sort;  //排序
}
