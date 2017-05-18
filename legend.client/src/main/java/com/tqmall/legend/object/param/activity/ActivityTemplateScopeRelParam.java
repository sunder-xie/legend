package com.tqmall.legend.object.param.activity;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
@Data
public class ActivityTemplateScopeRelParam extends BaseRpcParam{
    private static final long serialVersionUID = -3628097991468326449L;

    private Long actTplId;//活动模板id
    private List<Long> userGlobalIds;//统一门店id列表
    private List<Long> cityIds;//城市id列表
}
