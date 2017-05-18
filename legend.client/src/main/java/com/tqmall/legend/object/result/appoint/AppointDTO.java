package com.tqmall.legend.object.result.appoint;

import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dingbao on 16/3/18.
 */
@Data
public class AppointDTO implements Serializable {

    private static final long serialVersionUID = -4141353803276271650L;
    private Long id;
    private Long customerCarId;
    private String appointSn;
    private String license;
    private Long carBrandId;
    private String carBrandName;
    private Long carSeriesId;
    private String carSeriesName;
    private String carAlias;
    private String customerName;
    private String mobile;
    private Date appointTime;
    private String appointContent;
    private Long registrantId;
    private String registrantName;
    private Long shopId;
    private Long orderId;
    private String refer;//来源
    private Long previewType;//是否预览状态

    private String appointTimeFormat;

    private Long pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
    private String comment;//备注

    private BigDecimal appointAmount;//预约单金额
    private Long channel;//0 门店web, 1 商家app, 2车主app, 3 车主微信, 4 橙牛,5 滴滴,6 易迅车联,7 H5商家详情分享页面,8 斑马行车,9 云修系统客服,10夏日活动H5页面,11.门店微信公众号
    private String channelName;//0 门店web, 1 商家app, 2车主app, 3 车主微信, 4 橙牛,5 滴滴,6 易迅车联,7 H5商家详情分享页面,8 斑马行车,9 云修系统客服,10夏日活动H5页面,11.门店微信公众号

    private Long status;//-1 无效, 0 待确认, 1 预约成功, 2 已开单  3 车主取消, 4 门店取消 5 微信端取消
    private String statusName;//-1 无效, 0 待确认, 1 预约成功, 2 已开单  3 车主取消, 4 门店取消 5 微信端取消
    private String cancelReason;//取消原因

    private String echelianid;//易车联下预约单带过来的
    private String customerAddress;//客户居住地址

    private String serviceName;//预约单导出时候多个服务拼接名称
    private String goodName;//预约单导出多个物料拼接名称
    private String carInfo;//车辆型号

    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功

    private String appointServiceJson;

    private String flags;//用来区分是点"保存"还是"确认预约" 1保存2确认

    private List<ShopServiceInfoDTO> serviceInfoDTOList;

    public String getAppointTimeFormat() {
        if(appointTimeFormat != null){
            return appointTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (appointTime != null) {
            return df.format(appointTime);
        } else {
            return null;
        }
    }


}
