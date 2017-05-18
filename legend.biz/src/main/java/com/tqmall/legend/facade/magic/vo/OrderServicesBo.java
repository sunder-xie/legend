package com.tqmall.legend.facade.magic.vo;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dingbao on 16/5/18.
 */
@Data
public class OrderServicesBo {

    private Long serviceId;//原服务id
    private Long serviceCatId;//服务类别
    private String serviceCatName;//服务类别名称
    private String serviceName;//服务项目名称
    private String serviceSn;//服务编号
    private BigDecimal serviceHour;//服务工时
    private BigDecimal servicePrice;//c的价格
    private List<ShopServiceInfo> shopServiceInfoList;//同类别的服务，可有多个价格，如钣金，中低高价
    private String serviceNote;//服务备注
    private String flags;//服务标签
    private BigDecimal sharePrice;//共享价格
    private BigDecimal proxyAmount;//委托金额

}
