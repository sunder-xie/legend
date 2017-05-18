package com.tqmall.legend.web.fileImport.vo;

import com.google.common.collect.Maps;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.entity.shop.CarLevel;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by twg on 16/12/10.
 */
@Getter
@Setter
public class ServiceInfoImportContext extends CommonFileImportContext<ShopServiceInfo> {
    /*存放需要批量新加的服务类别实例*/
    private Map<String, ShopServiceCate> shopServiceCateMap = Maps.newHashMap();
    /*存放需要批量新加的车辆级别实例*/
    private Map<String, CarLevel> carLevelHashMap = Maps.newHashMap();
}
