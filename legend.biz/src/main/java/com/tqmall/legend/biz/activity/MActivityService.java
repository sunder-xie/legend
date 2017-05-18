package com.tqmall.legend.biz.activity;

import com.tqmall.legend.entity.lottery.Activity;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
public interface MActivityService {
    /**
     * 获取提现开始日期
     *
     */
    public Activity getActivityWithDrawTime();
}
