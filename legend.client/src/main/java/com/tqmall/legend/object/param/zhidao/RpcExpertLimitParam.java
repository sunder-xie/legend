package com.tqmall.legend.object.param.zhidao;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yoho_tail on 16/3/29.
 */
@Data
public class RpcExpertLimitParam implements Serializable{
    private static final long serialVersionUID = -5559186262362708568L;
    private Long id;
    private Long userId;//'用户
    private Integer userStatus;//'激活状态
    private Integer adoptCount;//消耗采纳数量
}
