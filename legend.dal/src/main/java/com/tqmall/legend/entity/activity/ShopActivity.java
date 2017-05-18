package com.tqmall.legend.entity.activity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

/**
 * Created by zsy on 16/2/24.
 * 门店活动
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopActivity extends BaseEntity {

    private Long actTplId;//活动模板id，0表示门店的服务
    private Long shopId;//门店id
    private String actName;//活动名称
    private String keywords;//活动关键字(标签)
    private String summary;//活动摘要
    private String remark;//活动详细描述
    private String imgUrl;//活动banner图url
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private Integer actStatus;//活动状态:0关闭,1草稿,2发布
    private Integer actType;//活动类型，0门店活动，1报名参加，2强制参加
    private Long sort;//排序，默认降序
    private String byName;//活动开服务单入口别名

}

