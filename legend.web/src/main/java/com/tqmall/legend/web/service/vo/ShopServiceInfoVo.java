package com.tqmall.legend.web.service.vo;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/9/29.
 */
@Getter
@Setter
public class ShopServiceInfoVo extends ShopServiceInfo {
    //服务名称+价格的组合字段
    private String namePrice;

    public String getNamePrice(){
        StringBuffer namePriceSb = new StringBuffer();
        namePriceSb.append(getName());
        namePriceSb.append(": ");
        namePriceSb.append(getServicePrice().longValue());//洗车价格一般没有角
        namePriceSb.append("元");
        return namePriceSb.toString();
    }
}
