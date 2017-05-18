package com.tqmall.legend.facade.privilege.bo;

import com.tqmall.common.search.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/1/5.
 * 门店员工账号查询业务对象
 */
@Getter
@Setter
public class UserInfoQueryBo extends BaseQuery {
    private Long shopId;//门店id
    private String keywords;//员工姓名/手机号，模糊查询
    private Long roleId;//岗位id
}
