package com.tqmall.legend.facade.activity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/8/6.
 */
@Data
public class SaveWechatActivityVo{
    private Long actId;//活动实体id
    private Long actTplId;//活动模版id
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private Integer actStatus;//0关闭,1草稿,2发布
    private Integer autoConfigMenu;//0不自动配置,1自动配置
    private List<SaveWechatActivityPageVo> pageVoList;//活动包含的page列表
}
