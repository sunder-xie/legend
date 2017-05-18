package com.tqmall.legend.dao.lottery;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.lottery.Activity;

import java.util.List;

@MyBatisRepository
public interface ActivityDao extends BaseDao<Activity> {

    /**
     * 获取当前有效活动
     *
     * @return List<Activity>
     */
    List<Activity> getCurrentActivity();


    /**
     * 判断当前活动是否有效
     *
     * @param id
     * @return
     */
    Activity checkvalid(Long id);


}
