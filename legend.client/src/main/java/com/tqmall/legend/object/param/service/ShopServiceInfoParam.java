package com.tqmall.legend.object.param.service;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.List;

/**
 * Created by zsy on 15/12/15.
 */
@Data
public class ShopServiceInfoParam extends BaseRpcParam{
    private Long serviceId; //服务id
    private Integer status; //对于服务：0上架，-1下架，对于门店：0参加，-1未参加
    private Long cityId;    //城市id
    private Integer limit;  //分页：一次返回的数据数
    private Integer offset; //分页：查询数据开始
    private Integer page;   //分页：搜索页数

    private Long userGlobalId;
    private List<Long> serviceIds;
    private List<String> sorts;//排序条件
}
