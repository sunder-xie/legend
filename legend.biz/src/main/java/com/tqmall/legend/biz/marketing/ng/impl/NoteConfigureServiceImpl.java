package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.legend.biz.component.converter.DataNoteShopConfigConverter;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.NoteShopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhoukai on 16/3/14.
 */
@Service
public class NoteConfigureServiceImpl implements NoteConfigureService {
    @Autowired
    ShopConfigureService shopConfigureService;



    @Override
    public NoteShopConfig getConfigure(Long shopId, Long type) {
        return shopConfigureService.getShopConfigure(shopId,type.intValue(),new DataNoteShopConfigConverter<NoteShopConfig>());
    }
}
