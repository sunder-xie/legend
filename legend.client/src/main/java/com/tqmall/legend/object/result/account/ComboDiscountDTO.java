package com.tqmall.legend.object.result.account;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 2017/3/9.
 */
@Getter
@Setter
public class ComboDiscountDTO implements Serializable {
    private Long accountComboId;
    private String comboName; // 名称
    private List<ComboServiceDTO> comboServiceList; // 服务次数
    private Date effectiveDate; // 生效时间
    private Date expireDate; // 失效时间
    private String description; // 描述
}
