package com.tqmall.legend.dao.activity;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.activity.ActivityTemplateServiceRel;

import java.util.List;

/**
 * Created by zsy on 16/2/24.
 */
@MyBatisRepository
public interface ActivityTemplateServiceRelDao extends BaseDao<ActivityTemplateServiceRel> {

    /**
     * 活动模板Ids
     * @param actTplIds
     * @return
     */
    public List<ActivityTemplateServiceRel> selectByActTplIds(List<Long> actTplIds);
}
