package com.tqmall.legend.biz.bo.dandelion;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by lixiao on 15/7/21.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class TaoqiBaseCouponParam {
	private String couponCode;
	private String mobile;
	private String license;
	private List<String> itemIds;

	@Override
	public String toString() {
		return "TaoqiBaseCouponParam{" +
				"couponCode='" + couponCode + '\'' +
				", mobile='" + mobile + '\'' +
				", license='" + license + '\'' +
				", itemIds=" + itemIds +
				'}';
	}
}
