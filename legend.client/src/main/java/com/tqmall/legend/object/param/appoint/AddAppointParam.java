package com.tqmall.legend.object.param.appoint;

import com.tqmall.legend.object.param.BaseRpcParam;
import com.tqmall.legend.object.param.customer.CustomerCarParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wushaui on 16/07/29.
 */
@Data
public class AddAppointParam extends BaseRpcParam {
    private static final long serialVersionUID = -2160035242632920590L;

    private Long userGlobalId;//uc的shopId

    private List<Long> serviceIds;//预约的服务id列表
    private Date appointTime;//预约时间
    private String appointContent;//预约内容
    private Long pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
    private Long registrantId;//登记人ID

    private String mobile;//客户手机号
    private String customerName;//客户名称
    private String refer;//数据来源：0：web, 1 : android,  2 :  IOS
    private Long channel;//0 门店web, 1 商家app, 2车主app, 3 车主微信 4 橙牛, 5 滴滴,6 易迅车联,7 H5商家详情分享页面 8 斑马行车, 9 云修系统客服，10 活动分享预约页面 11.门店微信公众号


    private CustomerCarParam carParam;//车辆信息

    private boolean needDownPay = false;//是否需要在线支付预付定金
    private BigDecimal downPayment;//预付定金
    private BigDecimal appointAmount;//预约单金额
}
