package com.tqmall.legend.biz.activity.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.activity.ActivityTemplateService;
import com.tqmall.legend.biz.activity.IShopActivityService;
import com.tqmall.legend.dao.activity.ShopActivityDao;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.entity.activity.ShopActivity;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 门店活动servcie
 */
@Slf4j
@Service
public class ShopActivityServiceImpl implements IShopActivityService {

	@Autowired
	ShopActivityDao shopActivityDao;
	@Autowired
	private ActivityTemplateService activityTemplateService;

	@Override
	public Optional<ShopActivity> get(Long shopId, Long activityTemplateId) {

		// 活动
		ShopActivity activity = null;

		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("shopId", shopId);
		paramMap.put("actTplId", activityTemplateId);
		try {
			List<ShopActivity> shopActivityList = shopActivityDao.select(paramMap);
			if (CollectionUtils.isEmpty(shopActivityList)) {
				return Optional.absent();
			}
			activity = shopActivityList.get(0);
		} catch (Exception e) {
			log.error("[DB]查询shopActivity门店活动异常,异常信息:{}", e);
			return Optional.absent();
		}

		return Optional.fromNullable(activity);
	}

    @Override
    public List<ShopActivity> select(Map<String, Object> searchParams) {
        List<ShopActivity> shopActivityList = shopActivityDao.select(searchParams);
        return shopActivityList;
    }

    @Override
    public Map<Long, ShopActivity> getShopActivityMap(Map<String, Object> searchParams) {
        Map<Long, ShopActivity> shopActivityMap = new HashMap<>();
        List<ShopActivity> shopActivityList = select(searchParams);
        for (ShopActivity shopActivity : shopActivityList) {
            Long id = shopActivity.getId();
            shopActivityMap.put(id, shopActivity);
        }
        return shopActivityMap;
    }

	/**
	 * 根据门店id获取需要审核的发布的活动（不包括报名和需要审核的活动）
	 *
	 * @param shopId
	 * @return
	 */
	@Override
	public List<ShopActivity> getShopActivityList(Long shopId) {
		List<ShopActivity> shopActivityList = new ArrayList<>();
		List<ActivityTemplate>  activityTemplateList = activityTemplateService.getValidActivityList(shopId);
		if(!CollectionUtils.isEmpty(activityTemplateList)){
			shopActivityList = getShopActivityList(shopId, shopActivityList, activityTemplateList);
		}
		return shopActivityList;
	}

	/**
	 * 根据门店id获取需要报名的所有活动（不包括报名和需要审核的活动）
	 *
	 * @param shopId
	 * @return
	 */
	@Override
	public List<ShopActivity> getShopActivityAllList(Long shopId) {
		List<ShopActivity> shopActivityList = new ArrayList<>();
		List<ActivityTemplate>  activityTemplateList = activityTemplateService.getActivityAllList(shopId);
		if(!CollectionUtils.isEmpty(activityTemplateList)){
			shopActivityList = getShopActivityList(shopId, shopActivityList, activityTemplateList);
		}
		return shopActivityList;
	}

	private List<ShopActivity> getShopActivityList(Long shopId, List<ShopActivity> shopActivityList, List<ActivityTemplate> activityTemplateList) {
		Set<Long> ids = new HashSet<>();
		for(ActivityTemplate activityTemplate : activityTemplateList){
            //需要报名参加的活动
            Long actTplId = activityTemplate.getId();
            int isNeedReimbursed = activityTemplate.getIsNeedReimbursed();
            if (isNeedReimbursed == 0){
				ids.add(actTplId);
			}
        }
		if(!CollectionUtils.isEmpty(ids)){
            Map<String,Object> searchMap = new HashMap<>();
            searchMap.put("shopId", shopId);
            searchMap.put("actTplIds",ids);
            shopActivityList = shopActivityDao.select(searchMap);
        }
		return shopActivityList;
	}

	@Override
	public Integer selectCount(Map<String, Object> searchMap) {
		return shopActivityDao.selectCount(searchMap);
	}

	@Override
	public ShopActivity selectById(Long actId) {
		return shopActivityDao.selectById(actId);
	}

	@Override
	public Integer add(ShopActivity shopActivity) {
		return shopActivityDao.insert(shopActivity);
	}

	@Override
	public Integer update(ShopActivity shopActivity) {
		return shopActivityDao.updateById(shopActivity);
	}

	@Override
	public int delete(Map<String, Object> param) {
		if (param==null||param.isEmpty()){
			return 0;
		}
		return shopActivityDao.delete(param);
	}
}
