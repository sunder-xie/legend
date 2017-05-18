package com.tqmall.legend.entity.app.member;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by jason on 15/9/10.
 * app端门店会员卡下的服务VO
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopMemberCardService {
    //优惠券ID
    private Long serviceId;
    //优惠券名称
    private String serviceName;
    //优惠券数量
    private Long serviceCount;
    //到期时间
    private Date expireTime;
    //创建时间
    private Date gmtCreate;
    //优惠券类型:1正常2过期3已使用
    private Long type;

}
