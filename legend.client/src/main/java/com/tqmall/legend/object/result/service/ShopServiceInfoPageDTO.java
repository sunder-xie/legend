package com.tqmall.legend.object.result.service;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ShopServiceInfoPageDTO extends BaseEntityDTO{
    private static final long serialVersionUID = 7646108016661305946L;
    private List<ShopServiceInfoDTO> content;
    private Integer total;
}
