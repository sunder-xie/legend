package com.tqmall.legend.biz.setting.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lilige on 16/11/9.
 * 存放在session中的VO
 */
@Setter
@Getter
public class PrintSessionVO {
    private Integer printTemplate;
    private Integer fontStyle;
    private Integer printType;
}
