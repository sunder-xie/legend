package com.tqmall.legend.object.param.activity;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ShopActivityParam extends BaseRpcParam{
    private static final long serialVersionUID = -761583633029792765L;

    private Integer offset;
    private Integer limit;
    private List<String> sorts;//排序条件
    private Long actId;//活动实体id
    private Long actTplId;//活动模板id
    private Long userGlobalId;//统一门店id
    private Integer actStatus;//活动状态:0关闭,1草稿,2发布
}
