package com.tqmall.legend.entity.feedback;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zsy on 16/8/3.
 * 反馈问题回答和追问
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FeedbackAnswer extends BaseEntity {

    private Long feedbackId;  //问题反馈id
    private Integer answerType;  //回答类型，0回答，1前台追问
    private String operatorName;  //操作人，包括问题回答和问题追问人
    private String answerContent;  //回答内容，注：富文本编辑,考虑到后台人员文字描述不清楚，截图、视频等直观描述问题


    private String gmtCreateStr;//回答和追问时间

    public String getGmtCreateStr(){
        return DateUtil.convertDateToMDHM(getGmtCreate());
    }
}

