package com.tqmall.legend.entity.subsidy;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dingbao on 16/2/24.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class SubsidyActivity extends BaseEntity {

    private Date startTime;//补贴活动开始时间
    private Date endTime;//补贴活动结束时间
    private String imageUrl;//补贴活动图片
    private BigDecimal subsidyAmount;//补贴金额
    private Integer subsidyType;//1:现金补贴
    private String subsidyName;//补贴活动名称
    private String subsidyAwardName;//补贴奖品名称
    private Long actId;//活动id

}


