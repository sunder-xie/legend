package com.tqmall.legend.facade.order.vo;

import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.lang.Langs;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by twg on 16/10/10.
 */
@Data
@Excel
public class OrderInfoVo {
    private String id;//工单id, 所有店铺unique
    private Integer shopId;//店铺id
    private Integer orderType;//业务类型
    private Integer parentId;//上级工单id
    @ExcelCol(value = 2, title = "工单编号", width = 16)
    private String orderSn;//工单编号
    private Integer customerId;//用户id
    private Integer customerCarId;//客户车辆id
    private String expectedTime;//预计出厂时间
    @ExcelCol(value = 11, title = "配件费用")
    private Double goodsAmount;//预估物料总价
    @ExcelCol(value = 9, title = "服务费用")
    private Double serviceAmount;//预估其它费用价格
    private Double taxAmount;//税价
    private Double totalAmount;//物料和服务和附加费用总和
    @ExcelCol(value = 23, title = "备注", width = 20)
    private String postscript;//备注
    private String orderStatus;//订单状态：CJDD 创建订单 ，DDBJ订单报价, FPDD 分配订单 ， DDSG 订单施工 ， DDWC 订单完成
    @ExcelCol(value = 4, title = "车牌", width = 10)
    private String carLicense;//车牌号
    private Integer carBrandId;//车品牌ID
    private Integer carSeriesId;//车系列ID
    private Integer carPowerId;//车排量ID
    private Integer carYearId;//车年款ID
    private Integer carModelsId;//车款式ID
    private String carBrand;//车品牌
    private String carSeries;//车系列
    private String carPower;//车排量
    private String carYear;//车年款
    private String carModels;//车款式
    private String carCompany;//车厂家
    private String importInfo;//进出口
    @ExcelCol(value = 6, title = "车主", width = 12)
    private String customerName;//客户名称
    @ExcelCol(value = 7, title = "车主电话", width = 12)
    private String customerMobile;//客户手机号码
    private String vin;//车架号
    private String engineNo;//发动机号
    private Integer receiver;//接单人ID
    private Integer invoiceType;//发票类型，0为不开票，1为普通发票，2为增值发票
    private Double discount;//优惠金额
    @ExcelCol(value = 25, title = "服务顾问", width = 12)
    private String receiverName;//接单人名字
    private String operatorName;//开单人名称

    private String orderAmount;//实际工单金额
    private Integer payStatus;//支付状态，0为未支付，2为已支付，1为挂账
    private String finishTime;//完工时间
    private String payTime;//结算时间
    private Integer goodsCount;//物料总数
    private Integer serviceCount;//服务总数
    private String carAlias;//车别名
    private Double preDiscountRate;//预折扣
    private Double preTaxAmount;//预税费
    private Double prePreferentiaAmount;//预优惠金额
    private Double preCouponAmount;//预代金券金额
    private Double preTotalAmount;//预计结算金额
    private Double payAmount;//实付金额
    private Double signAmount;//挂账金额
    private String otherInsuranceCompanyId;//对方保险公司ID
    private String otherInsuranceCompanyName;//对方保险公司名称
    @ExcelCol(value = 12, title = "配件优惠")
    private Double goodsDiscount;//物料折扣
    @ExcelCol(value = 10, title = "服务优惠")
    private Double serviceDiscount;//服务折扣
    @ExcelCol(value = 14, title = "附加优惠")
    private Double feeDiscount;//附加费用优惠
    @ExcelCol(value = 13, title = "附加费用")
    private Double feeAmount;//附加费用总金额
    private String contactName;//联系人姓名
    private String contactMobile;//联系人电话
    private Integer insuranceCompanyId;//承保公司ID
    private String insuranceCompanyName;//承保公司名称
    private String mileage;//里程数
    private String carColor;//车辆颜色
    private String buyTime;//购买时间
    private String customerAddress;//客户地址
    private String oilMeter;//油表油量
    private Integer isVisit;//是否已回访（1代表已回访，0或null代表未回访）
    private String isLock;//工单是否锁定状态(只有处于订单完成及以后状态的订单，才可能有锁定态)

    private Integer orderTag;


    private Integer proxyType;//委托状态：0：无1：委托2：受委托
    private Integer channelId;//渠道商id
    private String channelName;//渠道商名称

    private String confirmTime;

    //private String company;//对应于 LegendCustomer.company ，由 customerId 对应
    private String gmtCreate;
    private String createTime;
    private String gmtModified;
    @ExcelCol(value = 1, title = "业务类型")
    private String orderTypeName;
    /*维修内容*/
    @ExcelCol(value = 21, title = "服务项目", width = 24)
    private String serviceContent;
    @ExcelCol(value = 22, title = "配件项目（零件号，名称，数量，单位）", width = 24)
    private String goodsContent;
    /*结算信息*/
    @ExcelCol(value = 19, title = "出库成本")
    private BigDecimal realInventoryAmount;
    private BigDecimal payedAmount;
    // 客户单位
    private String company;

    @ExcelCol(value = 3, title = "下单日期", width = 22)
    public String getGmtCreateStr() {
        if (StringUtils.isNotBlank(createTime)) {
            return this.createTime;
        } else if (StringUtils.isNotBlank(gmtCreate)) {
            return this.gmtCreate;
        } else {
            return null;
        }
    }

    public String getCreateTimeStr() {
        return this.createTime;
    }

    @ExcelCol(value = 24, title = "最新更新时间", width = 20)
    public String getGmtModifiedStr() {
        return this.gmtModified;
    }

    @ExcelCol(value = 5, title = "车型", width = 26)
    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModels())) {
            sb.append(' ').append(getCarModels());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }

    public String getTqmallOrderStatusName() {
        if (orderStatus != null && payStatus != null) {
            //快修快保、销售单，待报价为待结算状态
            if (orderTag != null && (OrderCategoryEnum.SPEEDILY.getCode() == orderTag
                    || OrderCategoryEnum.SELLGOODS.getCode() == orderTag)
                    && orderStatus.equals(OrderNewStatusEnum.DBJ.getOrderStatus())
                    ) {
                return "待结算";
            }
            return OrderNewStatusEnum.getOrderStatusName(orderStatus, payStatus);
        } else {
            return null;
        }
    }

    public String getOrderStatusClientName() {
        if (orderStatus != null) {
            return OrderStatusEnum.getorderStatusClientNameByKey(orderStatus);
        } else {
            return null;
        }

    }

    public String getOrderStatusName() {
        if (orderStatus != null && payStatus != null) {
            return OrderNewStatusEnum.getOrderStatusName(orderStatus, payStatus);
        } else {
            return null;
        }

    }

    @ExcelCol(value = 0, title = "工单类型")
    public String getOrderTagName() {
        if (orderTag != null) {
            return OrderCategoryEnum.getsNameByCode(orderTag);
        }
        return null;
    }

    @ExcelCol(value = 16, title = "应收金额")
    public Double getPayAmountExcel() {
        if ("DDYFK".equals(orderStatus)) {
            return payAmount;
        }
        return null;
    }

    @ExcelCol(value = 17, title = "已收金额")
    public BigDecimal getPayedAmountExcel() {
        if ("DDYFK".equals(orderStatus)) {
            return payedAmount;
        }
        return null;
    }

    @ExcelCol(value = 18, title = "挂账金额")
    public Double getsignAmountExcel() {
        if ("DDYFK".equals(orderStatus)) {
            return signAmount;
        }
        return null;
    }

    @ExcelCol(value = 20, title = "工单状态", profile = "tqmall")
    public String getTqmallStatusName() {
        return getTqmallOrderStatusName();
    }

    @ExcelCol(value = 20, title = "工单状态", profile = "yunxiu,banpen")
    public String getYunxiuStatusName() {
        return getOrderStatusName();
    }

    @ExcelCol(value = 26, title = "是否委托", profile = "banpen")
    public String getProxyTypeExcel() {
        if (proxyType == 1) {
            return "是";
        } else {
            return "否";
        }
    }
    @ExcelCol(value = 15, title = "总计")
    public BigDecimal getOrderAmountForExcel() {
        if (Langs.isNotBlank(this.orderAmount)) {
            try {
                return new BigDecimal(this.orderAmount);
            } catch (Exception e) {
            }
        }
        return null;
    }
    @ExcelCol(value = 8, title = "行驶里程")
    public BigDecimal getMileageForExcel() {
        if (Langs.isNotBlank(this.mileage)) {
            try {
                return new BigDecimal(this.mileage);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
