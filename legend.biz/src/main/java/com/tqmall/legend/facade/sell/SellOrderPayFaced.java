package com.tqmall.legend.facade.sell;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.sell.SellOrder;
import com.tqmall.legend.entity.sell.SellOrderPay;
import com.tqmall.legend.pojo.sell.SellOrderPayStatusChangeVO;
import com.tqmall.legend.pojo.sell.SellOrderSaveVO;
import com.tqmall.legend.pojo.sell.SellShopTypeVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xiangdong.qu on 17/2/23 10:08.
 */
public interface SellOrderPayFaced {

    /**
     * 创建订单记录
     *
     * @param sellOrderSaveVO
     *
     * @return
     */
    public SellOrder sellOrderSave(SellOrderSaveVO sellOrderSaveVO);

    /**
     * 根据购买单号 创建支付空记录
     *
     * @param sellOrderSn 购买订单号
     * @param payOrderSn  支付商品单号
     *
     * @return
     */
    public SellOrderPay createSellOrderPay(String sellOrderSn, String payOrderSn);


    /**
     * 连连返回的同步结果 调用账务校验后 使用
     *
     * @param sellOrderPayStatusChangeVO
     *
     * @return
     */
    public Boolean updatePayStatusForSyn(SellOrderPayStatusChangeVO sellOrderPayStatusChangeVO);

    /**
     * 根据订单号 获取订单详情 且校验订单的折扣信息 用于支付时订单信息获取.
     *
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    public SellOrder getSellOrderByOrderSnAndCheckDiscount(String sellOrderSn);

    /**
     * 根据支付订单号 获取订单详情.
     *
     * @param payOrderSn 支付订单单号
     *
     * @return
     */
    public SellOrder getSellOrderByPayOrderSn(String payOrderSn);

    /**
     * 根据订单号 获取订单详情.
     *
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    public SellOrder getSellOrderByOrder(String sellOrderSn);

    /**
     * 根据电话号码和订单号 获取订单详情
     *
     * @param mobile      购买电话号
     * @param sellOrderSn 购买订单单号
     *
     * @return
     */
    public SellOrder getSellOrder(String mobile, String sellOrderSn);

    /**
     * 根据电话号码 获取订单列表
     *
     * @param mobile 购买电话号
     *
     * @return
     */
    public List<SellOrder> getSellOrder(String mobile);

    /**
     * 获取售卖门店各版本列表
     * @param mobile
     * @return
     */
    public List<SellShopTypeVO> getSellShopTypeList(String mobile);

    /**
     * 获取售卖门店详情信息
     *
     * @param level
     * @param mobile
     * @return
     */
    public SellShopTypeVO getSellShopTypeDetail(Integer level, String mobile);

    /**
     * 根据售卖订单开通门店
     *
     * @param sellOrderId 售卖订单Id
     *
     * @return 开通门店的 USER_GLOBAL_ID
     */
    public Long createShop(Long sellOrderId);


    /**
     * 根据电话号码获取折扣信息
     *
     * @param mobile 购买电话号码
     *
     * @return 0.5-1
     */
    public Result<BigDecimal> getSellDiscount(String mobile);


}
