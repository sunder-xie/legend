package com.tqmall.legend.object.param.activity;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ActivityTemplateParam extends BaseRpcParam{
    private static final long serialVersionUID = -761583633029792765L;

    private Integer offset;
    private Integer limit;
    private List<String> sorts;//排序条件
    private Long id;//活动模板id
    private List<Long> ids;//活动模板id列表
    private String actName;//活动名称(模糊查询)
    private String actNameExact;//活动名称(精确查询)
    private String keywords;//
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private Integer actStatus;//活动状态:0关闭,1草稿,2发布
    private Long channel;//活动渠道
}
