package com.tqmall.legend.object.param.appoint;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by wushuai on 17/2/13.
 */
@Getter
@Setter
public class SearchAppointPageParam implements Serializable{
    private static final long serialVersionUID = -8013131343499627710L;

    private String keyWord;
    private Long userGlobalId;
    private Integer status;//预约单状态:-1 无效, 0 待确认, 1 预约成功, 2 已开单  3 车主取消, 4 门店取消 5 微信端取消

    private Integer limit;  //分页页长
    private Integer offset; //分页起始,从0开始
    private String sortBy = "id";
    private String sortType = "desc";

}
