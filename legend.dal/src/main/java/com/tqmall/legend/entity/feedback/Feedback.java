package com.tqmall.legend.entity.feedback;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lilige on 16/7/18.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Feedback extends BaseEntity {

    private Long shopId;//门店id
    private String feedbackModule;//模块
    private String websiteUrl;//网址
    private String imgUrl;//图片地址
    private String feedbackContent;//反馈内容
    private Integer feedbackStatus;//反馈状态，0待处理，1处理中，2已处理
    private String followName;//问题跟进人姓名,如果为空，默认登录用户

    /**
     * 扩展字段
     */
    private String gmtCreateStr;//反馈时间
    private String feedbackStatusName;//反馈状态名称
    private FeedbackAnswer newFeedbackAnswer;//最新的回复信息

    public String getGmtCreateStr(){
        return DateUtil.convertDateToMDHM(getGmtCreate());
    }
}


