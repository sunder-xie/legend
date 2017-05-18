package com.tqmall.legend.entity.article;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by sven on 16/7/22.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class NewsType extends BaseEntity {
    private String typeName;
}
