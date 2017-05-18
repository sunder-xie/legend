package com.tqmall.legend.object.result.zhidao;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by macx on 16/3/29.
 */
@Data
public class ExpertLimitDTO implements Serializable{
    private static final long serialVersionUID = 8465554634644100749L;
    private Long id;
    private Long userId;//'用户
    private Integer userStatus;//'激活状态
    private Integer adoptCount;//消耗采纳数量
}
