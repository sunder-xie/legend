package com.tqmall.legend.entity.question;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class ExpertBonusLimit extends BaseEntity {

    private Long userId;//'用户
    private Integer userStatus;//'激活状态
    private Integer adoptCount;//消耗采纳数量

}

