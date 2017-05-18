package com.tqmall.legend.entity.course;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by zsy on 2015/5/8.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CourseDateBo {
    private String date;
    private String dateStr;
}
