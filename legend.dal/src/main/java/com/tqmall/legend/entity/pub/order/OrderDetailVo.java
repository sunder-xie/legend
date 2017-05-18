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
public class OrderDetailVo {

    private Long orderId;//工单ID
    private String userGlobalId;
    private String shopName;//店铺名称
    private String shopMobile;//店铺手机
    private String shopTel;//店铺固话
    private String shopAddress;//店铺名称
    private String orderStatus;//订单状态
    private String carLicense;//车牌
    private String orderStatusClientName;//订单状态名称
    //private Long orderType;//订单类型ID
    private String orderTypeName;//订单类型名称
    private BigDecimal orderAmount;//工单金额
    private BigDecimal payAmount;//工单实付金额
    private Integer payStatus;//支付状态，0为未支付，2为已支付
    private Date gmtCreate;//创建时间
    private String gmtCreateStr;
    public String getGmtCreateStr() {
        return this.convertDate(gmtCreate);
    }

    private Date gmtModified;//修改时间
    private String gmtModifiedStr;
    public String getGmtModifiedStr() {
        return this.convertDate(gmtModified);
    }

    private Date finishTime;//预计完工时间
    private String finishTimeStr;
    public String getFinishTimeStr() {
        return DateUtil.convertsDate(finishTime);
    }

    private Date completedTime;//完工时间
    private Date payTime;//结算时间
    private Integer orderTag;//工单标签，1为普通工单，2为洗车工单，3为快修快保单，4为保险维修单
    /**
     * 服务项目
     */
    private List<OrderServicesVo> serviceList;
    /**
     * 配件物料
     */
    private List<OrderGoodsVo> goodsList;
    /**
     * 其他物料
     */
    private List<OrderGoodsVo> otherGoodsList;
    /**
     * 附加费用
     */
    private List<OrderServicesVo> otherServiceList;

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

