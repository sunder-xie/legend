package com.tqmall.legend.object.result.appoint;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsy on 16/3/31.
 * 预约单返回对象
 */
@Data
public class AppointResultDTO implements Serializable {

    private static final long serialVersionUID = -3790193616614642577L;

    private Long id;
    private String appointSn;
    private Long customerCarId;
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
    private String refer;
    private String ver;
    private Long orderId;
    private Integer previewType;
    private Integer status;//0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消
    private String cancelReason;
    private BigDecimal appointAmount;
    private Integer channel;//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛,5 滴滴,9 云修系统客服
    private Integer pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
    private String comment;//备注
    private String customerAddress;//客户居住地址
    private String echelianid;//易讯车联个人用户唯一编码
    private String appointTimeStr;
    private String appointAddress;//预约地点

    public String getAppointTimeStr(){
        if (appointTime == null) {
            return "";
        }
        SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm");
        return f.format(appointTime);
    }

    public String getAppointContent(){
        if(StringUtils.isNotBlank(appointContent)){
            return appointContent.replace("\n","<br/>");
        }else{
            return "";
        }
    }
}
