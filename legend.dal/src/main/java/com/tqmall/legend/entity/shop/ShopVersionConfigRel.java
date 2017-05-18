package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by sven on 2017/1/10.
 */


@Getter
@Setter
public class ShopVersionConfigRel extends BaseEntity implements Comparable<ShopVersionConfigRel> {

    private Long shopId;//门店id
    private Long configId;//关联legend_shop_version_config表
    private Integer releaseVersion;//当前版本，老版本：0，新版本：1，新老版本切换：2

    @Override
    public int compareTo(ShopVersionConfigRel o) {
        if (this.getGmtModified().compareTo(o.getGmtModified()) > 0) {
            return 1;
        }
        if (this.getGmtModified().compareTo(o.getGmtModified()) < 0) {
            return -1;
        }
        if (this.getId() > o.getId()) {
            return 1;
        }
        if (this.getId() < o.getId()) {
            return -1;
        }
        return 0;
    }
}

