package com.tqmall.legend.facade.service.vo;

import com.tqmall.legend.entity.shop.ServiceFlagsEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by twg on 16/10/19.
 */
@Getter
@Setter
public class ShopServiceInfoVo {
    private Integer id;
    private String name;
    private String serviceSn;
    private Integer shopId;
    private Integer cateTag;
    private Double servicePrice;
    private Integer type;
    private Integer categoryId;
    private String categoryName;
    private Integer carLevelId;
    private Integer suiteNum;
    private Integer suiteGoodsNum;
    private String flags;
    private String carLevelName;
    private Integer status;
    private Integer editStatus;
    private Integer priceType;
    private BigDecimal suiteAmount;

    public String getFlagsName(){
        if(ServiceFlagsEnum.getMesByCode(flags) != null){
            return ServiceFlagsEnum.getMesByCode(flags);
        }else{
            return null;
        }
    }
}
