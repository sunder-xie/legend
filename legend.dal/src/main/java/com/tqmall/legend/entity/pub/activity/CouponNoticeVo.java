package com.tqmall.legend.entity.pub.activity;

import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by jason on 15/11/23.
 * 车主app2.0优惠公告Vo
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CouponNoticeVo {

    private Integer totalCount;//总条数
    private List<CzCouponNotice> list;
}
