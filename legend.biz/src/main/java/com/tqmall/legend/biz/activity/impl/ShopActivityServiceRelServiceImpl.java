package com.tqmall.legend.biz.activity.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.activity.IShopActivityServiceRelService;
import com.tqmall.legend.dao.activity.ShopActivityServiceRelDao;
import com.tqmall.legend.entity.activity.ShopActivityServiceRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动与活动服务关系 服务
 */
@Service
public class ShopActivityServiceRelServiceImpl implements IShopActivityServiceRelService {

	@Autowired
	ShopActivityServiceRelDao shopActivityServiceRelDao;


	@Override
	public Optional<ShopActivityServiceRel> getActivityId(@NotNull Long serviceId) {
		ShopActivityServiceRel shopActivityServiceRel = null;
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("serviceId", serviceId);
		List<ShopActivityServiceRel> shopActivityServiceRelList = shopActivityServiceRelDao.select(paramMap);
		if (!CollectionUtils.isEmpty(shopActivityServiceRelList)) {
			shopActivityServiceRel = shopActivityServiceRelList.get(0);
		}
		// 目前仅一条
		return Optional.fromNullable(shopActivityServiceRel);
	}

	@Override
	public List<ShopActivityServiceRel> select(Map<String, Object> searchMap) {
		return shopActivityServiceRelDao.select(searchMap);
	}

	@Override
	public Integer delete(Map<String, Object> delActServiceParam) {
		if(delActServiceParam==null||delActServiceParam.size()<1){
			return 0;
		}
		return shopActivityServiceRelDao.delete(delActServiceParam);
	}

	@Override
	public Integer batchInsert(List<ShopActivityServiceRel> shopActivityServiceRelList) {
		return shopActivityServiceRelDao.batchInsert(shopActivityServiceRelList);
	}
}
