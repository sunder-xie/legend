package com.tqmall.legend.entity.course;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class CourseCat extends BaseEntity {

    private String name;
    private Long parentId;
    private Long sort;
    private Integer status;

    private String parentName;

    private List<CourseCat> childCatList;
    private Integer row;
}

