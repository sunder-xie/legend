package com.tqmall.legend.entity.customer;

import java.text.Collator;
import java.util.Locale;

import lombok.Data;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.comparator.ComparableComparator;

import com.tqmall.legend.entity.base.BaseEntity;

@Data
public class CustomerCarVo extends BaseEntity implements Comparable<CustomerCarVo> {

	private Long shopId;
	// 品牌
	private Long carBrandId;
	private String carBrandName;

	// 车型
	private Long carSeriesId;
	private String carSeriesName;

	// 排量
	private Long carPowerId;
	private String carPowerName;

	// 年份
	private Long carYearId;
	private String carYearName;

	private String carAlias;

	public String getPkId() {
		return "";
	}

	/**
	 * 获取自有车型名
	 * 
	 * @return
	 */
	public String getPkName() {
		return stringJoin(this.carBrandName, this.carSeriesName, this.carPowerName, this.carYearName, this.carAlias);
	}

	private String stringJoin(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String s : strings) {
			if (StringUtils.isNotEmpty(s)) {
				sb.append(s).append(" ");
			}
		}
		return sb.toString().trim();
	}

	public String getPk() {
		StringBuffer sb = new StringBuffer();
		sb.append(emptyValue(this.carBrandId)).append('-');
		sb.append(emptyValue(this.carBrandName)).append('-');
		sb.append(emptyValue(this.carSeriesId)).append('-');
		sb.append(emptyValue(this.carSeriesName)).append('-');
		sb.append(emptyValue(this.carPowerId)).append('-');
		sb.append(emptyValue(this.carPowerName)).append('-');
		sb.append(emptyValue(this.carYearId)).append('-');
		sb.append(emptyValue(this.carYearName)).append('-');
		sb.append(emptyValue(this.carAlias));
		return sb.toString();
	}

	private String emptyValue(Object v) {
		if (v != null && StringUtils.isNoneBlank(v.toString())) {
			return v.toString();
		} else {
			return "null";
		}
	}

	@Override
	public int compareTo(CustomerCarVo o) {
		if (o == null) {
			return 1;
		}
		Collator c = Collator.getInstance(Locale.CHINA);
		return c.compare(getPkName(), o.getPkName());
	}
}
