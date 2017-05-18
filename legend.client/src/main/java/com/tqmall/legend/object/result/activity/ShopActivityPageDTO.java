package com.tqmall.legend.object.result.activity;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ShopActivityPageDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 688899440131799521L;
    List<ShopActivityDTO> content;
    Integer total;
}
