package com.tqmall.legend.facade.account.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by xin on 2017/3/9.
 */
@Getter
@Setter
public class ComboDiscount {
    private Long accountComboId;
    private String comboName; // 名称
    private List<ComboServiceVo> comboServiceList; // 服务次数
    private Date effectiveDate; // 生效时间
    private Date expireDate; // 失效时间
    private String description; // 描述
}
