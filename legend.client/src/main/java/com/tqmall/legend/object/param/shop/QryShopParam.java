package com.tqmall.legend.object.param.shop;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/8/9.
 */
@Data
public class QryShopParam extends BaseRpcParam{
    private static final long serialVersionUID = -3931760918991817672L;

    private Integer offset;
    private Integer limit;
    private List<String> sorts;//排序条件
    private Long shopStatus;//云修门店状态, 0:缺省值, 1:开通,2:冻结,3:测试,4:试用,5:商用车,6:档口客户
    private String name;//门店名称
    private List<Long> cityList;//市列表
    private Long ucShopId;//对应legend_shop.user_global_id
}
