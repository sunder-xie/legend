package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Created by jason on 15/10/28.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppActServiceRel extends BaseEntity {

    private Long appActId;
    private Long templateId;
    private String templateName;

}


