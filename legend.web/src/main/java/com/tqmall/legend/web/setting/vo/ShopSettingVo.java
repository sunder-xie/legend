package com.tqmall.legend.web.setting.vo;

import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.entity.shop.Shop;
import lombok.Data;

/**
 * ShopSettingController专用,只为前端传值而生
 * Created by lilige on 17/1/9.
 */
@Data
public class ShopSettingVo {
    private Shop shop;
    private CustomerJoinAudit customerJoinAudit;
    private Boolean isCrm;
}
