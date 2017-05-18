package com.tqmall.legend.entity.pub.order;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class OrderVo {

    private Long id;//工单ID
    private String userGlobalId;
    private String shopName;//店铺名称
    private String shopMobile;//店铺手机
    private String shopTel;//店铺固话
    private String shopAddress;//店铺名称
    private String orderStatus;//订单状态
    private String orderStatusClientName;//订单状态名称
    //private Long orderType;//订单类型ID
    private String orderTypeName;//订单类型名称
    private BigDecimal orderAmount;//工单金额
    private BigDecimal payAmount;//工单实付金额
    private Integer payStatus;//支付状态，0为未支付，2为已支付

    private Date finishTime;//完工时间
    private String finishTimeStr;
    public String getFinishTimeStr() {
        return DateUtil.convertsDate(finishTime);
    }
    private Date gmtCreate;//创建时间
    private String gmtCreateStr;
    public String getGmtCreateStr() {
        return this.convertDate(gmtCreate);
    }


    private String categoryName;//服务类别
    private String carLicense;//车牌号


    /**
     * 服务项目
     */
    private List<OrderServicesVo> serviceList;
    /**
     * 配件物料
     */
    private List<OrderGoodsVo> goodsList;

    /**
     * 把时间类型转化为规定格式的字符串
     * "yyyy.MM.dd hh:MM:ss"
     *
     * @param date 时间
     * @return 格式化以后的时间字符串
     */
    public static String convertDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return f.format(date);
    }

    //订单状态名称
    public String getOrderStatusClientName() {
        if (orderStatus != null) {
            return OrderStatusEnum.getorderStatusClientNameByKey(orderStatus);
        } else {
            return null;
        }

    }
}

