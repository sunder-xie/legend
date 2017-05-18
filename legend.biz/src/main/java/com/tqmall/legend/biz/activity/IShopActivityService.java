package com.tqmall.legend.biz.activity;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.activity.ShopActivity;

import java.util.List;
import java.util.Map;

/**
 * 门店活动servcie
 */
public interface IShopActivityService {

	/**
	 * 获取门店活动
	 *
	 * @param shopId             门店ID
	 * @param activityTemplateId 活动模板ID
	 * @return Optional<ShopActivity>
	 */
	Optional<ShopActivity> get(Long shopId, Long activityTemplateId);

	/**
	 * 条件查询
	 *
	 * @param searchParams
	 * @return
	 */
	public List<ShopActivity> select(Map<String,Object> searchParams);

	/**
	 * 获取门店活动map
	 *
	 * @param searchParams
	 * @return key:活动id
	 */
	public Map<Long, ShopActivity> getShopActivityMap(Map<String,Object> searchParams);

	/**
	 * 根据门店id获取需要审核的发布的活动（不包括报名和需要审核的活动）
	 *
	 * @param shopId
	 * @return
	 */
	public List<ShopActivity> getShopActivityList(Long shopId);

	/**
	 * 根据门店id获取需要报名的所有活动（不包括报名和需要审核的活动）
	 * @param shopId
	 * @return
	 */
	public List<ShopActivity> getShopActivityAllList(Long shopId);

	/**
	 * 计数查询
	 * @param searchMap
	 * @return
     */
	public Integer selectCount(Map<String, Object> searchMap);

	public ShopActivity selectById(Long actId);

	public Integer add(ShopActivity shopActivity);

	public Integer update(ShopActivity shopActivity);

	int delete(Map<String, Object> param);
}
