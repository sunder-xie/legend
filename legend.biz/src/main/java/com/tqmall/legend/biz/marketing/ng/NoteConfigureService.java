package com.tqmall.legend.biz.marketing.ng;

import com.tqmall.legend.entity.shop.NoteShopConfig;

/**
 * Created by wanghui on 3/14/16.
 * 通知配置信息服务类
 */
public interface NoteConfigureService {


    /**
     * 獲取封裝后的提醒設置
     * @param shopId
     * @param type
     * @return
     */
    NoteShopConfig getConfigure(Long shopId, Long type);

}
