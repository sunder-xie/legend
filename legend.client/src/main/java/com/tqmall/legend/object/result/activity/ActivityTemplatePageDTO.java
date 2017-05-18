package com.tqmall.legend.object.result.activity;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/07/28.
 */
@Data
public class ActivityTemplatePageDTO extends BaseEntityDTO {
    private static final long serialVersionUID = -6913077881764727937L;
    List<ActivityTemplateDTO> content;
    Integer total;
}
