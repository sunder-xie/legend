package com.tqmall.legend.entity.insurance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 保险报销单 表单实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InsuranceBillFormEntity extends BaseEntity {

	private static final long serialVersionUID = -3610729922271638496L;

	// 保险单
	InsuranceBill orderInfo;
	//0-新增 1-更新 2-提交 3-重新提交
	Integer flag;

}

