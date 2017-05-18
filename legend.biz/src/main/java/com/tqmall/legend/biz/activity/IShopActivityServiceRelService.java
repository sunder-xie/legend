package com.tqmall.legend.biz.activity;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.activity.ShopActivityServiceRel;

import java.util.List;
import java.util.Map;

/**
 * 活动与活动服务关系 服务
 */
public interface IShopActivityServiceRelService {


	/**
	 * 获取服务关联活动ID
	 *
	 * @param serviceId 服务ID
	 * @return 活动ID
	 */
	Optional<ShopActivityServiceRel> getActivityId(Long serviceId);

	List<ShopActivityServiceRel> select(Map<String, Object> searchMap);

	public Integer delete(Map<String, Object> delActServiceParam);

	public Integer batchInsert(List<ShopActivityServiceRel> shopActivityServiceRelList);
}
