package com.tqmall.legend.entity.member;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopMemberServiceRel extends BaseEntity {
    private Long shopId;

    private Long memberId;

    private Long suiteId;

    private String suiteName;

    private Long serviceId;

    private String serviceName;

    private Long serviceCount;

    private Date expireTime;

    private String expireTimeFormat;

    private Long usedCount;

    private Long validDays;



    private Long flowId;//流水表id

    public String getExpireTimeFormat() {
        if (expireTimeFormat != null) {
            return expireTimeFormat;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (expireTime != null) {
            return df.format(expireTime);
        }
        return null;
    }
}
