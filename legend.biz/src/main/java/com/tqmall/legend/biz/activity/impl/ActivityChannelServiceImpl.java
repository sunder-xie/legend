package com.tqmall.legend.biz.activity.impl;

import com.tqmall.legend.biz.activity.ActivityChannelService;
import com.tqmall.legend.dao.activity.ActivityChannelDao;
import com.tqmall.legend.entity.activity.ActivityChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 16/2/25.
 */
@Service
public class ActivityChannelServiceImpl implements ActivityChannelService{

    @Autowired
    private ActivityChannelDao activityChannelDao;

    @Override
    public List<ActivityChannel> getAll() {
        Map<String,Object> searchMap = new HashMap<>();
        List sorts = new ArrayList();
        sorts.add("sort desc");
        searchMap.put("sorts",sorts);
        return activityChannelDao.select(searchMap);
    }

    @Override
    public List<ActivityChannel> getByChannelSource(Integer channelSource) {
        if(channelSource==null){
            return new ArrayList<>();
        }
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("channelSource",channelSource);
        searchMap.put("sorts",new String[]{"sort desc"});
        return activityChannelDao.select(searchMap);
    }

    @Override
    public ActivityChannel selectById(Long channelId) {
        if(channelId==null){
            return null;
        }
        return activityChannelDao.selectById(channelId);
    }
}
