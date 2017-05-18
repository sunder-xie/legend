package com.tqmall.legend.biz.sell.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sell.SellOrderPayService;
import com.tqmall.legend.dao.sell.SellOrderPayDao;
import com.tqmall.legend.entity.sell.SellOrderPay;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangdong.qu on 17/2/23 09:31.
 */
@Service
@Slf4j
public class SellOrderPayServiceImpl extends BaseServiceImpl implements SellOrderPayService {

    @Autowired
    private SellOrderPayDao sellOrderPayDao;

    /**
     * 创建支付记录
     *
     * @param sellOrderPay 创建支付记录
     *
     * @return
     */
    @Override
    public SellOrderPay createSellOrderPay(SellOrderPay sellOrderPay) {
        Map<String, Object> param = new HashMap<>();
        param.put("sellOrderSn", sellOrderPay.getSellOrderSn());
        List<SellOrderPay> sellOrderPayList = sellOrderPayDao.select(param);
        if (!CollectionUtils.isEmpty(sellOrderPayList) && sellOrderPayList.size() > 1) {
            return null;
        } else if (sellOrderPayList.size() == 1) {
            return sellOrderPayList.get(0);
        }

        SellOrderPay sellOrderPayTemp = new SellOrderPay();
        sellOrderPayTemp.setSellOrderSn(sellOrderPay.getSellOrderSn());
        sellOrderPayTemp.setSellOrderId(sellOrderPay.getSellOrderId());
        sellOrderPayTemp.setPayOrderSn(sellOrderPay.getPayOrderSn());
        sellOrderPayDao.insert(sellOrderPayTemp);
        return sellOrderPayTemp;
    }

    /**
     * 根据支付商品订单号 获取支付记录
     *
     * @param payOrderSn
     *
     * @return
     */
    @Override
    public SellOrderPay getSellOrderPayBySn(final String payOrderSn) {
        return new BizTemplate<SellOrderPay>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(payOrderSn), "支付商品单号参数错误");
            }

            @Override
            protected SellOrderPay process() throws BizException {
                Map<String, Object> param = new HashMap();
                param.put("payOrderSn", payOrderSn);
                List<SellOrderPay> sellOrderPayList = sellOrderPayDao.select(param);
                if (CollectionUtils.isEmpty(sellOrderPayList) || sellOrderPayList.size() > 1) {
                    throw new BizException("获取支付信息失败");
                }
                return sellOrderPayList.get(0);
            }
        }.execute();
    }

    /**
     * 根据id更新sellOrderPay
     *
     * @param sellOrderPay
     *
     * @return
     */
    @Override
    public int updateSellOrderPayById(SellOrderPay sellOrderPay) {
        return sellOrderPayDao.updateById(sellOrderPay);
    }

    /**
     * 根据售卖订单id获取支付单信息
     *
     * @param sellOrderId 售卖订单Id
     *
     * @return
     */
    @Override
    public SellOrderPay getSellOrderPayBySellOrderId(final Long sellOrderId) {
        return new BizTemplate<SellOrderPay>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(sellOrderId, "订单Id为空");
                Assert.isTrue(sellOrderId > 0, "订单Id错误");
            }

            @Override
            protected SellOrderPay process() throws BizException {
                Map<String, Object> param = new HashMap();
                param.put("sellOrderId", sellOrderId);
                List<SellOrderPay> sellOrderPayList = sellOrderPayDao.select(param);
                if (CollectionUtils.isEmpty(sellOrderPayList) || sellOrderPayList.size() > 1) {
                    throw new BizException("获取支付信息失败");
                }
                return sellOrderPayList.get(0);
            }
        }.execute();
    }
}
