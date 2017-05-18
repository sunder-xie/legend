package com.tqmall.legend.entity.marketing.activity;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class CzCouponNotice extends BaseEntity implements Comparable<CzCouponNotice> {

    private Long shopId;//门店ID
    private String userGlobalId;//门店全局编码
    private String address;//门店地址
    private String shopName;//门店名称
    private String couponName;//优惠名称
    private String couponDesc;//优惠说明
    private Date startTime;//优惠开始时间
    private Date endTime;//优惠结束时间
    private Integer couponType;//优惠类型:1为满减,2立减,3满返,4赠
    private Integer couponStatus;//活动状态:0草稿1提交待审核2审核通过待发布3审核未通过4已发布
    private String reason;//原因
    private String codeImgUrl;//二维码

    private String startTimeStr;
    private String endTimeStr;

    public String getStartTimeStr() {
        if (null == startTime) {
            return startTimeStr;
        } else {
            return DateUtil.convertDateToStr(startTime, "yyyy年MM月dd日");
        }
    }
    public String getEndTimeStr() {
        if (null == endTime) {
            return endTimeStr;
        } else {
            return DateUtil.convertDateToStr(endTime, "yyyy年MM月dd日");
        }
    }

    @Override
    public int compareTo(CzCouponNotice arg0) {
        return this.getCouponType().compareTo(arg0.getCouponType());
    }


}

