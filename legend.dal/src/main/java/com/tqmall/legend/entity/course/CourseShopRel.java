package com.tqmall.legend.entity.course;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zsy on 2015/5/7.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CourseShopRel extends BaseEntity {

    private Long shopId;
    private Long managerLoginId;
    private Long joinCount;
    private Long courseId;
    private Long courseDetailId;

    private String shopName;
    private String managerName;
    private String mobile;
    private Long totalJoinCount;
}

