package com.tqmall.legend.object.result.shop;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/8/9.
 */
@Data
public class QryShopPageDTO extends BaseEntityDTO {
    private static final long serialVersionUID = -5934328634944153093L;
    List<QryShopDTO> content;
    Integer total;
}
