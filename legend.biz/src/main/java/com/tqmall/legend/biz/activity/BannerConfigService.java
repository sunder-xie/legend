package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.activity.BannerConfig;

import java.util.List;

/**
 * Created by lixiao on 16/2/24.
 */
public interface BannerConfigService {

    /**
     * 根据位置获取banner列表
     * @param postion
     * @return
     */
    public List<BannerConfig> getListByPostion(Integer postion);
}
