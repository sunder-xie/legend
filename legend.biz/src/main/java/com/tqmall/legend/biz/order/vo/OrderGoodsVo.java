package com.tqmall.legend.biz.order.vo;

import com.tqmall.legend.entity.order.OrderGoods;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xin on 16/4/14.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class OrderGoodsVo implements Serializable {

    private static final long serialVersionUID = -6364919608953545998L;

    private Long id;
    private Long shopId;
    private Long orderId;
    private Long goodsId;
    private BigDecimal goodsNumber;
    private BigDecimal goodsPrice;
    private BigDecimal goodsAmount;
    private BigDecimal soldPrice;
    private String orderSn;
    private BigDecimal discount;
    private String goodsSn;
    private String goodsName;
    private String goodsFormat;
    private BigDecimal inventoryPrice;
    private BigDecimal soldAmount;
    private String measureUnit;
    private Long relOrderServiceId;
    private String goodsNote;
    private Long goodsType;

    private BigDecimal outNumber;
    //剩余待出库的数量（总量-已出库数量）
    private BigDecimal remainingNumber;
    private String imgUrl;
    private BigDecimal stock;
    private BigDecimal currentInventoryPrice;
    private String depot;

    private BigDecimal warehouseOutAmount;
    private BigDecimal warehouseInventoryAmount;
    private BigDecimal warehouseOutPrice;

    // 页面比较
    int goodsNumberInt;
    int stockInt;

    // 销售员id
    private Long saleId;
    // 销售员名称
    private String saleName;

    // 工单创建时间
    private String orderCreateTimeStr;

    public int getGoodsNumberInt() {
        return goodsNumber != null ? goodsNumber.intValue() : 0;
    }

    public void setGoodsNumberInt(int goodsNumberInt) {
        this.goodsNumberInt = goodsNumberInt;
    }

    public int getStockInt() {
        return stock != null ? stock.intValue() : 0;
    }

    public void setStockInt(int stockInt) {
        this.stockInt = stockInt;
    }


    /**
     * 转换工单物料VO
     * @param orderGoods
     * @return
     */
    public static OrderGoodsVo getVo(OrderGoods orderGoods){
        OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
        orderGoodsVo.setId(orderGoods.getId());
        orderGoodsVo.setShopId(orderGoods.getShopId());
        orderGoodsVo.setOrderId(orderGoods.getOrderId());
        orderGoodsVo.setGoodsId(orderGoods.getGoodsId());
        orderGoodsVo.setGoodsNumber(orderGoods.getGoodsNumber());
        orderGoodsVo.setGoodsPrice(orderGoods.getGoodsPrice());
        orderGoodsVo.setGoodsAmount(orderGoods.getGoodsAmount());
        orderGoodsVo.setSoldPrice(orderGoods.getSoldPrice());
        orderGoodsVo.setOrderSn(orderGoods.getOrderSn());
        orderGoodsVo.setDiscount(orderGoods.getDiscount());
        orderGoodsVo.setGoodsName(orderGoods.getGoodsName());
        orderGoodsVo.setGoodsSn(orderGoods.getGoodsSn());
        orderGoodsVo.setGoodsFormat(orderGoods.getGoodsFormat());
        orderGoodsVo.setInventoryPrice(orderGoods.getInventoryPrice());
        orderGoodsVo.setSoldAmount(orderGoods.getSoldAmount());
        orderGoodsVo.setMeasureUnit(orderGoods.getMeasureUnit());
        orderGoodsVo.setRelOrderServiceId(orderGoods.getRelOrderServiceId());
        orderGoodsVo.setGoodsNote(orderGoods.getGoodsNote());
        orderGoodsVo.setGoodsType(orderGoods.getGoodsType());
        orderGoodsVo.setOutNumber(orderGoods.getOutNumber());
        orderGoodsVo.setRemainingNumber(orderGoods.getRemainingNumber());
        orderGoodsVo.setImgUrl(orderGoods.getImgUrl());
        orderGoodsVo.setStock(orderGoods.getStock());
        orderGoodsVo.setCurrentInventoryPrice(orderGoods.getCurrentInventoryPrice());
        orderGoodsVo.setDepot(orderGoods.getDepot());
        orderGoodsVo.setWarehouseOutAmount(orderGoods.getWarehouseOutAmount());
        orderGoodsVo.setWarehouseInventoryAmount(orderGoods.getWarehouseInventoryAmount());
        orderGoodsVo.setWarehouseOutPrice(orderGoods.getWarehouseOutPrice());
        orderGoodsVo.setGoodsNumberInt(orderGoods.getGoodsNumberInt());
        orderGoodsVo.setStockInt(orderGoods.getStockInt());
        orderGoodsVo.setSaleId(orderGoods.getSaleId());
        orderGoodsVo.setOrderCreateTimeStr(orderGoods.getOrderCreateTimeStr());
        return orderGoodsVo;
    }
}
