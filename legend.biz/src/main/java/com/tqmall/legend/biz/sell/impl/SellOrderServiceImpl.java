package com.tqmall.legend.biz.sell.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sell.SellOrderService;
import com.tqmall.legend.dao.sell.SellOrderDao;
import com.tqmall.legend.entity.sell.SellOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiangdong.qu on 17/2/23 09:27.
 */
@Service
@Slf4j
public class SellOrderServiceImpl extends BaseServiceImpl implements SellOrderService {

    @Autowired
    private SellOrderDao sellOrderDao;

    /**
     * 根据订单Id获取订单信息
     *
     * @param sellOrderId
     *
     * @return
     */
    @Override
    public SellOrder selectById(final Long sellOrderId) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(sellOrderId, "商品订单Id为空");
                Assert.isTrue(sellOrderId > 0, "商品订单Id错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                SellOrder sellOrder = sellOrderDao.selectById(sellOrderId);
                if (sellOrder == null) {
                    throw new BizException("获取商品订单信息失败");
                }
                return sellOrder;
            }
        }.execute();
    }

    /**
     * 保存购买订单
     *
     * @param sellOrder
     *
     * @return
     */
    @Override
    public int saveSellOrder(final SellOrder sellOrder) {
        return new BizTemplate<Integer>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Integer process() throws BizException {
                return sellOrderDao.insert(sellOrder);
            }
        }.execute();
    }

    /**
     * 根据购买订单号获取订单详情
     *
     * @param sellOrderSn
     *
     * @return
     */
    @Override
    public SellOrder getSellOrderBySn(final String sellOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(sellOrderSn), "参数错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                Map<String, Object> param = new HashMap<>();
                param.put("sellOrderSn", sellOrderSn);
                List<SellOrder> sellOrderList = sellOrderDao.select(param);
                if (CollectionUtils.isEmpty(sellOrderList) || sellOrderList.size() > 1) {
                    log.error("[云修系统售卖在线支付] 获取订单信息有误.sellOrderSn:{}", sellOrderSn);
                    throw new BizException("获取订单信息有误");
                }
                return sellOrderList.get(0);
            }
        }.execute();

    }

    /**
     * 根据购买订单号和电话 获取订单详情
     *
     * @param mobile
     * @param sellOrderSn
     *
     * @return
     */
    @Override
    public SellOrder getSellOrderByMobileAndSn(final String mobile, final String sellOrderSn) {
        return new BizTemplate<SellOrder>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(mobile), "参数错误");
                Assert.isTrue(!StringUtils.isBlank(sellOrderSn), "参数错误");
            }

            @Override
            protected SellOrder process() throws BizException {
                Map<String, Object> param = new HashMap<>();
                param.put("sellOrderSn", sellOrderSn);
                param.put("mobile", mobile);
                List<SellOrder> sellOrderList = sellOrderDao.select(param);
                if (CollectionUtils.isEmpty(sellOrderList) || sellOrderList.size() > 1) {
                    log.error("[云修系统售卖在线支付] 获取订单信息有误.sellOrderSn:{}", sellOrderSn);
                    throw new BizException("获取订单信息有误");
                }
                return sellOrderList.get(0);
            }
        }.execute();
    }

    /**
     * 根据电话号码获取 购买订单列表
     *
     * @param mobile
     *
     * @return
     */
    @Override
    public List<SellOrder> getSellOrderListByMobile(final String mobile) {
        return new BizTemplate<List<SellOrder>>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!StringUtils.isBlank(mobile), "参数错误");
            }

            @Override
            protected List<SellOrder> process() throws BizException {
                Map<String, Object> param = new HashMap<>();
                param.put("mobile", mobile);
                List<SellOrder> sellOrderList = sellOrderDao.select(param);
                return sellOrderList;
            }
        }.execute();
    }

    /**
     * 根据id更新sellOrder
     *
     * @param sellOrder
     *
     * @return
     */
    @Override
    public int updateSellOrderById(SellOrder sellOrder) {
        return sellOrderDao.updateById(sellOrder);
    }
}
