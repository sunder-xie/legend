package com.tqmall.legend.object.result.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ddl-wechat端查询工单列表内部关联的物料对象
 * Created by wushuai on 16/9/19.
 */
@Data
public class WechatOrdGoodsDTO implements Serializable {
    private static final long serialVersionUID = -3659394008246956733L;
    private Long goodsId;
    private Integer goodsNumber;
    private BigDecimal goodsPrice;
    private String goodsName;
}
