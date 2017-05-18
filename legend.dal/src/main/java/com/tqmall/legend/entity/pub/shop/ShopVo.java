package com.tqmall.legend.entity.pub.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by jason on 15-07-09.
 * 我服务过的店铺VO对象
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopVo {

    private String userGlobalId;//shopId对应全局的ID
    private Date time;//工单创建时间


}
