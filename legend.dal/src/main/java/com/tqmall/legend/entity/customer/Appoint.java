package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.utils.DateFormatUtils;
import com.tqmall.legend.object.enums.appoint.AppointStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class Appoint extends BaseEntity {

    private Long customerCarId;
    @ExcelCol(value = 0, title = "预约单号", width = 20)
    private String appointSn;
    @ExcelCol(value = 1, title = "车牌", width = 12)
    private String license;
    private Long carBrandId;
    private String carBrandName;
    private Long carSeriesId;
    private String carSeriesName;
    private String carAlias;
    private String vin;             //vin码
    private String importInfo;//车辆进出口信息
    private String byName;//车辆别名
    @ExcelCol(value = 3, title = "联系人", width = 12)
    private String customerName;
    @ExcelCol(value = 4, title = "联系电话", width = 12)
    private String mobile;
    private Date appointTime;
    @ExcelCol(value = 6, title = "预约内容", width = 30)
    private String appointContent;
    private Long registrantId;
    @ExcelCol(value = 9, title = "预约登记人", width = 10)
    private String registrantName;
    private Long shopId;
    private Long orderId;
    private String refer;//来源
    private Long previewType;//是否预览状态
    @ExcelCol(value = 5, title = "预约时间", width = 16)
    private String appointTimeFormat;
    private String gmtModifiedFormat;

    private Long pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
    private String comment;//备注

    private BigDecimal appointAmount;//预约单金额
    private Long channel;//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛
    @ExcelCol(value = 11, title = "预约单来源", width = 12)
    private String channelName;//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛

    private Long status;//0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消
    @ExcelCol(value = 12, title = "预约单状态", width = 10)
    private String statusName;//-1 无效,0 待确认, 1 已确认, 2 工单生成  3 车主取消, 4 门店取消
    private String cancelReason;//取消原因

    private String echelianid;//易车联下预约单带过来的
    private String customerAddress;//客户居住地址
    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功
    @ExcelCol(value = 7, title = "服务项目", width = 20)
    private String serviceName;//预约单导出时候多个服务拼接名称
    @ExcelCol(value = 8, title = "配件物料", width = 20)
    private String goodName;//预约单导出多个物料拼接名称
    @ExcelCol(value = 2, title = "车辆型号", width = 20)
    private String carInfo;//车辆型号

    private String appointServiceJson;

    private String flags;//用来区分是点"保存"还是"确认预约" 1保存2确认

    private BigDecimal oriAppointAmount;//预约单原金额(由云修根据服务价格计算得出)

    public String getAppointTimeFormat() {
        if(appointTimeFormat != null){
            return appointTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        if (appointTime != null) {
            return df.format(appointTime);
        } else {
            return null;
        }
    }

    public String getGmtModifiedFormat() {
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (gmtModified != null) {
            return df.format(gmtModified);
        } else {
            return null;
        }
    }

    @ExcelCol(value = 10, title = "预约登记时间", width = 20)
    public String getGmtCreateFormat() {
        if (gmtCreate != null) {
            return DateFormatUtils.toYMDHMS(gmtCreate);
        } else {
            return null;
        }
    }

    public String getStatusName() {
        if(status==null){
            return "";
        }
        return AppointStatusEnum.getNameByStatus(status.intValue());
    }

    @Override
    public String toString() {
        return "Appoint{" +
                "customerCarId=" + customerCarId +
                ", appointSn='" + appointSn + '\'' +
                ", license='" + license + '\'' +
                ", carBrandId=" + carBrandId +
                ", carBrandName='" + carBrandName + '\'' +
                ", carSeriesId=" + carSeriesId +
                ", carSeriesName='" + carSeriesName + '\'' +
                ", carAlias='" + carAlias + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", appointTime=" + appointTime +
                ", appointContent='" + appointContent + '\'' +
                ", registrantId=" + registrantId +
                ", registrantName='" + registrantName + '\'' +
                ", shopId=" + shopId +
                ", orderId=" + orderId +
                ", refer='" + refer + '\'' +
                '}';
    }
}

