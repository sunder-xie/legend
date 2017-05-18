package com.tqmall.legend.object.param.finance;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

/**
 * Created by twg on 16/1/25.
 */
@Data
public class QueryParam extends BaseRpcParam {
    private static final long serialVersionUID = 891605088093357969L;
    private String startTime;//开始时间
    private String endTime;//结束时间
    private int offset;//开始页
    private int limit;//每页显示条数
}
