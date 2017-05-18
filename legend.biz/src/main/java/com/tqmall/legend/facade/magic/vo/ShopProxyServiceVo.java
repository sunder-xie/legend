package com.tqmall.legend.facade.magic.vo;

import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.order.vo.OrderServicesVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dingbao on 16/5/17.
 */
@Data
public class ShopProxyServiceVo {

    private Shop shop;

    private List<OrderServicesVo> orderServicesVos;

}
