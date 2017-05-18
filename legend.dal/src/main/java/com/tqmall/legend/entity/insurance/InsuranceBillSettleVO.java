package com.tqmall.legend.entity.insurance;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 门店引流活动收入汇总账单相应实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InsuranceBillSettleVO implements Serializable {
	//模板id
	private Long actTplId;
	// 活动名称
	private String actName;
	// 服务单数
	private Integer serviceNum;
	// 活动服务售总卖价
	private BigDecimal totalServiceAmount;
	// 活动服务总收入
	private BigDecimal totalSettleAmount;
	// 审核通过查询时间
	private String auditPassTimeStr;
	// 是否需要对账
	private boolean isNeedSettle;
}

