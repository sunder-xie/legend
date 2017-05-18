package com.tqmall.legend.entity.insurance;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsy on 16/1/7.
 * 保险报销单
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InsuranceBill extends BaseEntity {
	private static final long serialVersionUID = -3049678172763330828L;

	private Long shopId;  //店铺id
	private Long orderId;  //工单id
	private Long serviceId;  //服务id
	private String serviceName;  //服务名称，及修复方案
	private Long customerId;  //用户id
	private Long customerCarId;  //客户车辆id
	private String postscript;  //备注，保存审核失败原因和其他备注信息
	private String carLicense;  //车牌号
	private Long carBrandId;  //车品牌id
	private Long carSeriesId;  //车系列id
	private Long carModelsId;  //车款式id
	private String carBrand;  //车品牌
	private String carSeries;  //车系列
	private String carModels;  //车款式
	private String customerName;  //客户名称
	private String customerMobile;  //客户手机号码
	private String vin;  //车架号
	private String insured;  //保险人
	private String insuredCode;  //保单号
	private String woundPart;  //受损部位
	private String verificationCode;  //核销码
	private String imgUrl;  //车牌图片url
	private String woundSnapshoot;  //划痕图片url
	private String acceptanceSnapshoot;  //修复后照片url
	private String company;  //客户单位
	private String mileage;  //行驶里程
	private Integer auditStatus;  //状态：0已保存，1审核中，2审核成功，3审核失败，4已打款
	private String importInfo;//进出口
	private Long shopActId;//门店活动ID
	private Integer shopConfirmStatus;//门店确认状态，0为未确认，1为已确认
	private String billNote;//报销单备注
	private Date auditPassTime;//审核通过时间

	private String auditStatusName;//临时字段：状态名称
	private String auditPassTimeStr;//临时字段：审核通过时间
	private String gmtCreateStr;//临时字段：状态名称
	private Long carYearId;  //临时字段：年款id
	private Long carPowerId;  //临时字段：排量id
	private Long carGearBoxId;  //临时字段：变速箱id
	private String carYear;  //临时字段：年款
	private String carPower;  //临时字段：排量
	private String carGearBox;  //临时字段：变速箱
	// 服务单类别{'平安保养':'PINGAN.BAOYANG';'平安补漆':'PINGAN.BUQI'}
	private String billType;
	private BigDecimal settlePrice;//结算价
	private Long actTplId;//临时字段：活动模板id
	private String actName;//临时字段：活动名称
	private Integer usedTime;//使用的核销次数

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

	public String getGmtCreateStr() {
		if (gmtCreate != null) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return f.format(gmtCreate);
		} else {
			return null;
		}
	}

	public String getAuditPassTimeStr() {
		return DateUtil.convertDate(auditPassTime);
	}
}

