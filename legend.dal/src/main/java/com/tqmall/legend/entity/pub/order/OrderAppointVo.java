package com.tqmall.legend.entity.pub.order;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.pub.service.ServiceInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderAppointVo {

    private Long appointId;//预约单ID
    private Long shopId;
    private String mobile;
    private String shopName;//店铺名称
    private String userGlobalId;//userGlobalId
    private String shopAddress;//店铺地点
//    private String orderStatus;//订单状态
    private Long appointStatus;//订单状态 0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消

    private Date appointTime;//预约时间
    private String appointTimeStr;
    public String getAppointTimeStr() {
        return DateUtil.convertsDate(appointTime);
    }
    private Date gmtCreate;//创建时间
    private String gmtCreateStr;
    public String getGmtCreateStr() {
        return DateUtil.convertDateYMDHMS(gmtCreate);
    }

    private String appointContent;//预约内容
    private String appointSn;//预约单编号

    private BigDecimal appointAmount;//预约单价格

    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功

    private List<ServiceInfoVo> appointServiceList;

}

