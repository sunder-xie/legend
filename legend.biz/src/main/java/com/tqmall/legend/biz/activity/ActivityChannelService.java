package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.ActivityChannel;

import java.util.List;

/**
 * Created by lixiao on 16/2/25.
 */
public interface ActivityChannelService {

    public List<ActivityChannel> getAll();

    public List<ActivityChannel> getByChannelSource(Integer channelSource);

    public ActivityChannel selectById(Long channelId);
}
