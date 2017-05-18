package com.tqmall.legend.object.result.service;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ServiceTemplatePageDTO extends BaseEntityDTO{
    private static final long serialVersionUID = -5914627205224870869L;
    private List<ServiceTemplateDTO> content;
    private Integer total;
}
