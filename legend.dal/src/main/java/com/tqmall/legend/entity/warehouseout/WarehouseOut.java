package com.tqmall.legend.entity.warehouseout;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class WarehouseOut extends BaseEntity {

    private Long shopId;//门店id
    private String warehouseOutSn;//出库单sn
    private Long customerCarId;//车辆信息id
    private Long goodsReceiver;//领料人id
    private Long orderId;//工单id
    private String status;//状态
    private String inventoryType;//
    private String carLicense;//车牌号码
    private String carType;//车辆型号
    private String carByname;//车辆别名
    private Long customerId;//客户id
    private String customerName;//客户姓名
    private String customerMobile;//客户手机
    private String relSn;//所关联的出库单sn
    private Long relId;//所关联的工单id
    private String outType;//出库类型
    private String comment;//出库单备注

    private List<WarehouseOutDetail> detailList;
    private String gmtCreateStr;
    private String outTypeName;
    public  String getGmtCreateStr(){
        if (null != gmtCreate) {
            return DateUtil.convertDateToYMD(gmtCreate);
        }
        return gmtCreateStr;
    }

}


