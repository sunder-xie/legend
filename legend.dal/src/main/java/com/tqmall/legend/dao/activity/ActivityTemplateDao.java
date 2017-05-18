package com.tqmall.legend.dao.activity;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.activity.ActivityTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/2/24.
 */
@MyBatisRepository
public interface ActivityTemplateDao extends BaseDao<ActivityTemplate> {

    /**
     * 根据渠道获取有效的活动列表
     * @param channel
     * @return
     */
    public List<ActivityTemplate> getValidActivityList(@Param("channel")Long channel);

}
