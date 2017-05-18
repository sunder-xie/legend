package com.tqmall.legend.object.result.shop;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 辉辉大侠 on 8/10/16.
 */
@Data
public class ShopInfoDTO extends BaseEntityDTO implements Serializable {
    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 门店级别
     */
    private Integer level;
    /**
     * uc id
     */
    private String userGlobalId;
    /**
     * 店铺状态
     */
    private Integer shopStatus;

    /**
     * 是否是档口用户
     * @return
     */
    public boolean isTqmallVersion() {
        return level.equals(Integer.valueOf(6));
    }
}
