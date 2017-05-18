package com.tqmall.legend.object.result.service;

import com.tqmall.legend.object.result.goods.SearchPackageGoodsDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macx on 2017/2/10.
 */
@Getter
@Setter
public class ServicePackageDTO implements Serializable{
    private static final long serialVersionUID = -9116461522025279771L;
    private ShopServiceInfoDTO shopServiceInfoDTO;
    private List<ShopServiceInfoExtDTO> shopServiceInfoExtDTOS;
    private List<SearchPackageGoodsDTO> searchPackageGoodsDTOS;
}
