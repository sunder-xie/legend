package com.tqmall.legend.entity.activity;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 门店活动服务相应实体
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopActivityServiceListVO implements Serializable {

	// 活动实体
	private ShopActivity activity;
	// 服务列表
	List<ShopServiceInfo> servicelist;
}

