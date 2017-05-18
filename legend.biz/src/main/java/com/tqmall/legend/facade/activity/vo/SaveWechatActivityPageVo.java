package com.tqmall.legend.facade.activity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/8/6.
 */
@Data
public class SaveWechatActivityPageVo {
    private String uniqueCode;//页面唯一标识，取自模板活动页面表中的unique_code
    private Integer pageIndex;//页面在实体活动中的顺序，从0开始
    private List<SaveWechatActivityModuleVo> moduleVoList;//活动page包含的module列表
}
