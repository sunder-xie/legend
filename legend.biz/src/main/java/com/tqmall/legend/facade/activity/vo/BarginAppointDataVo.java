package com.tqmall.legend.facade.activity.vo;

import com.tqmall.dandelion.wechat.client.dto.wechat.ShopActDiscountListDTO;
import lombok.Data;

/**
 * Created by pituo on 16/10/28.
 */
@Data
public class BarginAppointDataVo extends ShopActDiscountListDTO {
    private Integer durationDay;//活动进行天数
}
