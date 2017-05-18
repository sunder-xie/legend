package com.tqmall.legend.facade.service.vo;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;

/**
 * 车主服务Vo
 * Created by wushuai on 16/10/10.
 */
@Data
public class AppServiceVo extends ShopServiceInfo {
    private Integer appointCount;//服务对应的预约单数量
}
