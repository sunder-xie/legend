package com.tqmall.legend.object.result.subsidy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by xiangDong.qu on 16/2/24.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SubsidyActivityDTO implements Serializable {
    private static final long serialVersionUID = 5020470652579534787L;

    private Long id;//补贴活动id
    private Date startTime;//补贴活动开始时间
    private Date endTime;//补贴活动结束时间
    private String imageUrl;//补贴活动图片
    private BigDecimal subsidyAmount;//补贴金额
    private Integer subsidyType;//1:现金补贴
    private String subsidyName;//补贴活动名称
    private String subsidyAwardName;//补贴奖品名称
    private Long actId;//活动id

    private Long applyNum;    //补贴申请人次数
    private Long remainNum;   //补贴剩余数
    private Long applyTotalNum; //申请总数
}
