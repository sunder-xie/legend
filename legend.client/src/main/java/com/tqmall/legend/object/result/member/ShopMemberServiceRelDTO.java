package com.tqmall.legend.object.result.member;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiangDong.qu on 16/3/20.
 */
@Data
@ToString
public class ShopMemberServiceRelDTO implements Serializable {
    private static final long serialVersionUID = -5864933314643978918L;
    protected Long id;
    protected String isDeleted;
    protected Date gmtCreate;
    protected Long creator;
    protected Date gmtModified;
    protected Long modifier;

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
