package com.tqmall.legend.facade.order.vo;

import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.order.OrderCategoryEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.lang.Langs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Created by majian on 16/12/9.
 */
@Data
@Excel
@Slf4j
public class DebitExcelBO {
    @ExcelCol(value = 2, title = "工单编号", width = 16)
    private String orderSn;//工单编号
    @ExcelCol(value = 11, title = "配件费用")
    private Double goodsAmount;//预估物料总价
    @ExcelCol(value = 9, title = "服务费用")
    private Double serviceAmount;//预估其它费用价格
    @ExcelCol(value = 21, title = "备注", width = 20)
    private String postscript;//备注
    private String orderStatus;//订单状态：CJDD 创建订单 ，DDBJ订单报价, FPDD 分配订单 ， DDSG 订单施工 ， DDWC 订单完成
    @ExcelCol(value = 4, title = "车牌", width = 10)
    private String carLicense;//车牌号
    private String carBrand;//车品牌
    private String carSeries;//车系列
    private String carModels;//车款式
    private String importInfo;//进出口
    @ExcelCol(value = 6, title = "车主", width = 12)
    private String customerName;//客户名称
    @ExcelCol(value = 7, title = "车主电话", width = 12)
    private String customerMobile;//客户手机号码
    @ExcelCol(value = 23, title = "服务顾问", width = 12)
    private String receiverName;//接单人名字
    private String orderAmount;//实际工单金额
    private Integer payStatus;//支付状态，0为未支付，2为已支付，1为挂账
    private Double payAmount;//实付金额
    private Double signAmount;//挂账金额
    @ExcelCol(value = 12, title = "配件优惠")
    private Double goodsDiscount;//物料折扣
    @ExcelCol(value = 10, title = "服务优惠")
    private Double serviceDiscount;//服务折扣
    @ExcelCol(value = 14, title = "附加优惠")
    private Double feeDiscount;//附加费用优惠
    @ExcelCol(value = 13, title = "附加费用")
    private Double feeAmount;//附加费用总金额

    private String mileage;//里程数

    private Integer orderTag;
    private Integer proxyType;//委托状态：0：无1：委托2：受委托
    private String gmtCreate;
    private String createTime;
    private String gmtModified;
    @ExcelCol(value = 1, title = "业务类型")
    private String orderTypeName;
    /*维修内容*/
//    @ExcelCol(value = 21, title = "服务项目")
//    private String serviceContent;
    /*结算信息*/
    @ExcelCol(value = 19, title = "出库成本")
    private BigDecimal realInventoryAmount;
    private BigDecimal payedAmount;

    @ExcelCol(value = 3, title = "下单日期", width = 21)
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

    @ExcelCol(value = 22, title = "最新更新时间", width = 20)
    public String getGmtModifiedStr() {
        return this.gmtModified;
    }

    @ExcelCol(value = 5, title = "车型", width = 25)
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

    @ExcelCol(value = 24, title = "是否委托", profile = "banpen")
    public String getProxyTypeExcel() {
        if (proxyType == 1) {
            return "是";
        } else {
            return "否";
        }
    }

    @ExcelCol(value = 8, title = "行驶里程")
    public BigDecimal getMileageForExcel() {
        if (Langs.isNotBlank(this.mileage) && StringUtil.isNumeric(mileage)) {
            try {
                return new BigDecimal(this.mileage);
            } catch (Exception e) {
                log.error("行驶里程转换错误, mileage:{}, 错误信息:{}", mileage, e);
            }
        }
        return null;
    }
    @ExcelCol(value = 15, title = "总计")
    public BigDecimal getOrderAmountForExcel() {
        if (Langs.isNotNull(orderAmount)) {
            try {
                return new BigDecimal(this.orderAmount);
            } catch (Exception e) {
                log.error("e", e);
            }
        }
        return null;
    }
}
