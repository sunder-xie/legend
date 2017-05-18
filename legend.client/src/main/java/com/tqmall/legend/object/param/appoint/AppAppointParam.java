package com.tqmall.legend.object.param.appoint;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsy on 16/3/27.
 */
@Data
public class AppAppointParam extends BaseRpcParam {
    private static final long serialVersionUID = 6512559370658858308L;

    private Long shopId;//门店id
    private String userGlobalId;//uc的shopId
    private Long serviceId;//门店服务id
    private Date appointTime;//预约时间
    private String mobile;//客户手机号
    /**
     * 渠道
     * 0 门店web, 1 商家app, 2车主app, 3 车主微信
     * 4 橙牛, 5 滴滴,6 易迅车联,7 H5商家详情分享页面
     * 8 斑马行车, 9 云修系统客服，10 活动分享预约页面
     */
    private Long channel;
    private String customerName;//客户名称
    private String license;//车牌
    private String carBrandName;//车品牌
    private String carSeriesName;//车系列
    private Long carBrandId;//车品牌id
    private Long carSeriesId;//车系列id
    private String cancelReason;//取消原因
    private Long status;//预约单状态 0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消 5 微信端取消
    private String customerAddress;//客户居住地址
    private Long templateId;//服务模版Id
}
