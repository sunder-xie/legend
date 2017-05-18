package com.tqmall.legend.object.result.order;

import com.tqmall.legend.object.result.goods.WechatOrdGoodsDTO;
import com.tqmall.legend.object.result.service.WechatOrdServiceDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 16/9/19.
 */
@Data
public class WechatOrderDTO implements Serializable {
    private static final long serialVersionUID = 1826685125463180665L;
    private Long id;                        //订单ID
    private Date gmtCreate;

    private String userGlobalId;
    private String shopName;//店铺名称
    private String shopMobile;//店铺手机
    private String shopTel;//店铺固话
    private String shopAddress;//店铺名称


    private String orderStatus;             //订单状态
    private String orderStatusClientName;   //订单状态名称
    private String orderTypeName;           //订单类型
    private String categoryName;             // 服务项目名称
    private BigDecimal orderAmount;         //订单金额
    private BigDecimal payAmount;            //实际付款金额
    private Integer payStatus;             //支付状态，0为未支付，2为已支付
    private Date finishTime;                //完工时间

    private String   carLicense;//车牌号


    private List<WechatOrdServiceDTO> serviceList;
    private List<WechatOrdGoodsDTO> goodsList;




}
