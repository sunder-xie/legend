package com.tqmall.legend.object.result.news;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by litan on 17/4/12.
 */
@Setter
@Getter
public class NewTypeDTO implements Serializable {
    private Long id;
    private String typeName;
    private Integer classifyType;
}
