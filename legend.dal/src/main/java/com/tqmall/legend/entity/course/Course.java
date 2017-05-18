package com.tqmall.legend.entity.course;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class Course extends BaseEntity {

    private String title;
    private String pic;
    private String content;
    private Long count;
    private Long catId;
    private Long sort;
    private Integer status;

    private String catName;
    private List<CourseDetail> courseDetailList;
    private String statusName;

    public String getStatusName() {
        return CourseStatusEnum.getMesByCode(status);
    }

}

