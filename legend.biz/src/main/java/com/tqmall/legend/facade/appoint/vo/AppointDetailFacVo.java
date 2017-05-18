package com.tqmall.legend.facade.appoint.vo;

import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;

import java.util.List;

/**
 * 预约单大对象
 * Created by wushuai on 16/4/12.
 */
@Data
public class AppointDetailFacVo {
    private AppointVo appointVo;//预约单基本信息(包含客户车辆信息)
    private List<AppointServiceVo> appointServiceVoList;//预约服务列表(包含shopServiceInfo)
    private List<Goods> goodsList;//预约服务对应的物料信息
}
