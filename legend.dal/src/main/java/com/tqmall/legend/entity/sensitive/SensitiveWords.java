package com.tqmall.legend.entity.sensitive;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lifeilong on 2015/11/19.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SensitiveWords extends BaseEntity {

    private String content;
    private Integer level;

}

