package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-11-04 15:54
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopServiceInfoBo {
    private ShopServiceInfo shopServiceInfo;
    private ServiceGoodsSuite serviceGoodsSuite;
}
