package com.tqmall.legend.entity.sys;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/5/9.
 */
@Getter
@Setter
public class UserOperateDict extends BaseEntity {

    private String requestUrl;//用户请求的url
    private String referKey;//全站唯一的来源key值
    private String targetKey;//下一步触发的来源值（refer）
    private String operateRemark;//备注
    private Integer operateApplication;//应用0：legend1：app
    private String operateModule;//所属模块，如首页guide，接车维修reception，财务settlement

}

