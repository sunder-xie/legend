package com.tqmall.legend.entity.subsidy;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopOrderSubsidy extends BaseEntity {

    private String orderSn;//订单编号
    private Long shopId;//门店id
    private Long goodsId;//商品id
    private String goodsName;//商品品类名称
    private Long buyCount;//购买数量总数
    private Long waitActiveCount;//待激活数量
    private Date financeConfirmTime;//财务确认收款时间
    private Date giveCountTime;//返利次数发放时间
    private Date returnTime;//退货时间
    private String returnSn;//退货单编号
    private Long returnCount;//退货中数量
    private Long hasReturnCount;//已退货数量
    private Long hasReceiveCount;//已领数量
    private Long hasActiveCount;//已激活数量
    private Long subsidyActId;//所属补贴包id
    private String subsidyActName;//所属补贴包名称
    private Integer freezeStatus;//冻结状态：0-未冻结，1-冻结
    private BigDecimal subsidyPrice;//返利单价
    private Long subsidyGoodsId;//补贴活动商品表id
    private Integer leastUnit;//最小单位
    private Long hasUsedCount;//已到账数量
    private Long userGlobalId;//uc用户id

}

