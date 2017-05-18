package com.tqmall.legend.object.param.finance;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.Date;

/**
 * Created by jason on 16/4/5.
 * 退货商品返利检验参数VO
 */

@Data
public class VerifyParam extends BaseRpcParam {
    private static final long serialVersionUID = 528995697807864248L;

    /**
     * UC的shopId
     */
    private Long userGlobalId;

    /**
     * 退货商品数量
     */
    private Integer goodsNum;

    /**
     * 退货商品Id
     */
    private Integer goodsId;

    /**
     * 退货商品orderGoodsId
     */
    private Integer orderGoodsId;

    /**
     * 订单下单时间
     */
    private Date orderTime;
    /**
     * 订单号
     */
    private String orderSn;

}
