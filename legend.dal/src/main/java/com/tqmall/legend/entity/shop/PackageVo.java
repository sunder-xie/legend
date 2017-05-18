package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.goods.Goods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by lixiao on 15-1-4.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PackageVo {

    private ShopServiceInfo shopServiceInfo;
    private List<ShopServiceInfo> shopServiceInfoList;
    private List<Goods> goodsList;
}
