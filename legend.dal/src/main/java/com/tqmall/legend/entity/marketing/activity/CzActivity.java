package com.tqmall.legend.entity.marketing.activity;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class CzActivity extends BaseEntity {

    private Long shopId;//门店ID
    private String userGlobalId;//门店全局编码
    private String actName;//活动名称
    private String actDesc;//活动简介
    private String detailDesc;//活动首页详情
    private String imgUrl;//活动广告图
    private String tinyImgUrl;//活动H5分享小图
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private Integer actStatus;//活动状态:0草稿 1提交待审核,2审核通过待发布,3审核未通过,4已发布
    private String reason;//原因
    private String codeImgUrl;//二维码

    private String address;//门店地址
    private String shopName;//门店名称
    private String startTimeStr;
    private String endTimeStr;
    private Long endTimeLong;//结束时间戳

    private List<CzActCateRel> actCateList;//活动类别List
    private String actCateStr;//actCateList json字符串
    private Long cityId;//城市ID


    public String getStartTimeStr() {
        if (null == startTime) {
            return startTimeStr;
        } else {
            return DateUtil.convertDateToStr(startTime, "yyyy.MM.dd");
        }
    }

    public String getEndTimeStr() {
        if (null == endTime) {
            return endTimeStr;
        } else {
            return DateUtil.convertDateToStr(endTime, "yyyy.MM.dd");
        }

    }


}

