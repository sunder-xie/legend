package com.tqmall.legend.object.param.service;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Created by zsy on 15/12/15.
 */
@Data
public class ServiceTemplateParam extends BaseRpcParam{
    private static final long serialVersionUID = -8100091830887600515L;
    private Integer limit;  //分页：查询数据开始
    private Integer offset; //分页：一次返回的数据数

    private String serviceName;//服务模版名称
    private Integer status;//模板状态 0有效 -1无效
    private Set<Long> serviceTplIds;//服务模版id集合
    private List<String> sorts;//排序条件
}
