package com.tqmall.legend.facade.magic.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by macx on 16/5/16.
 */
@Data
public class ShopPartnerAddVO {
    private List<ShopPartnerSimVO> notAddPartnerSimVOs;//未加入门店
    private List<ShopPartnerSimVO> addPartnerSimVOs;//已加入门店
}
