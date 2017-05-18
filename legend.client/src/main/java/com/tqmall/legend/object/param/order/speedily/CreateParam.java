package com.tqmall.legend.object.param.order.speedily;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macx on 2017/2/9.
 */
@Getter
@Setter
public class CreateParam implements Serializable{
    private static final long serialVersionUID = 4597575416762045591L;

    private Long userId;
    private Long shopId;
    private String userName;
    private String ver;
    private Integer refer;
    private Long virtualOrderId;// 虚拟子单ID
    private String imgUrl;//车牌图片
    private OrderInfoParam orderInfoParam;
    private List<ServiceParam> serviceParams;
    private List<GoodsParam> goodsParams;
}
