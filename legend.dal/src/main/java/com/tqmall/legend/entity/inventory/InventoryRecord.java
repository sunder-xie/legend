package com.tqmall.legend.entity.inventory;


import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;

@EqualsAndHashCode(callSuper = false)
@Data
public class InventoryRecord extends BaseEntity {

    private Long shopId;
    private String recordSn;
    private Long goodsCount;
    private String operatorName;
    private Integer status;
    private String inventoryCheckerName;
    private Long inventoryCheckerId;
    private String inventoryRemark;


    private String gmtModifiedStr;

    public String getGmtModifiedStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (gmtModified != null) {
            return df.format(gmtModified);
        } else {
            return null;
        }
    }


}

