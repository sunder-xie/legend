package com.tqmall.legend.biz.order.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderGoodsService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.dao.order.OrderGoodsDao;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.order.OrderGoodTypeEnum;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OrderGoods Service implement
 */
@Service
public class OrderGoodsServiceImpl extends BaseServiceImpl implements OrderGoodsService {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderGoodsServiceImpl.class);

    @Autowired
    private OrderGoodsDao orderGoodsDao;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private GoodsDao goodsDao;
   

    @Override
    public OrderGoods selectById(Long id) {
        return orderGoodsDao.selectById(id);
    }

    @Override
    public List<OrderGoods> select(Map<String, Object> map) {
        return orderGoodsDao.select(map);
    }


    @Override
    public List<OrderGoods> selectAndGoods(Map<String, Object> map) {
        List<OrderGoods> orderGoodsList = orderGoodsDao.select(map);
        if (!CollectionUtils.isEmpty(orderGoodsList)) {
            Set<Long> goodsIdSet = new HashSet<>();
            for (OrderGoods orderGoods : orderGoodsList) {
                goodsIdSet.add(orderGoods.getGoodsId());
            }

            if (!CollectionUtils.isEmpty(goodsIdSet)) {
                Long[] goodsIdArr = new Long[goodsIdSet.size()];
                goodsIdArr = goodsIdSet.toArray(goodsIdArr);
                // 批量查询物料
                List<Goods> goodsList = goodsDao.selectByIds(goodsIdArr);
                if (!CollectionUtils.isEmpty(goodsList)) {
                    // list 转 map
                    Map<Long, Goods> goodsMap = new HashMap<>();
                    for (Goods goods : goodsList) {
                        goodsMap.put(goods.getId(), goods);
                    }

                    // 将物料字段赋值给工单物料
                    Goods goods = null;
                    for (OrderGoods orderGoods : orderGoodsList) {
                        Long goodsId = orderGoods.getGoodsId();
                        goods = goodsMap.get(goodsId);
                        if (goods != null) {
                            orderGoods.setStock(goods.getStock());
                            orderGoods.setCurrentInventoryPrice(goods.getInventoryPrice());
                            orderGoods.setCarInfoStr(goods.getCarInfoStr());
                            orderGoods.setImgUrl(goods.getImgUrl());
                            orderGoods.setDepot(goods.getDepot());
                        }
                    }
                }
            }
        }
        return orderGoodsList;
    }

    @Override
    public List<OrderGoods> queryOrderGoodList(@NotNull Long orderId, @NotNull Long shopId, @NotNull OrderGoodTypeEnum orderGoodTypeEnum) {

        // 查询参数
        Map<String, Object> orderGoodsMap = new HashMap<String, Object>(3);
        orderGoodsMap.put("orderId", orderId);
        orderGoodsMap.put("shopId", shopId);
        orderGoodsMap.put("goodsType", orderGoodTypeEnum.getCode());

        List<OrderGoods> orderGoodses = null;
        try {
            orderGoodses = this.selectAndGoods(orderGoodsMap);
        } catch (Exception e) {
            LOGGER.error("获取配件物料异常,异常信息:{}", e);
            return new ArrayList<OrderGoods>();
        }

        if (CollectionUtils.isEmpty(orderGoodses)) {
            orderGoodses = new ArrayList<OrderGoods>();
        }

        return orderGoodses;
    }

    @Override
    public List<OrderGoods> queryOrderGoodList(@NotNull Long orderId, Long shopId) {
        Map<String, Object> orderGoodsMap = new HashMap<String, Object>(2);
        orderGoodsMap.put("orderId", orderId);
        orderGoodsMap.put("shopId", shopId);

        List<OrderGoods> orderGoodsList = null;
        try {
            orderGoodsList = orderGoodsDao.select(orderGoodsMap);
        } catch (Exception e) {
            LOGGER.error("[DB]query legend_goods failure,工单ID:{} 异常信息:{}", orderId, e);
            orderGoodsList = new ArrayList<OrderGoods>();
        }

        if (CollectionUtils.isEmpty(orderGoodsList)) {
            orderGoodsList = new ArrayList<OrderGoods>();
        }

        return orderGoodsList;
    }


    @Override
    public List<OrderGoods> queryOrderGoodesByGoodsId(@NotNull Long goodsId, Long shopId) {
        Map<String, Object> orderGoodsMap = new HashMap<String, Object>(2);
        orderGoodsMap.put("goodsId", goodsId);
        orderGoodsMap.put("shopId", shopId);

        List<OrderGoods> orderGoodsList = null;
        try {
            orderGoodsList = orderGoodsDao.select(orderGoodsMap);
        } catch (Exception e) {
            LOGGER.error("[DB]query legend_goods failure,配件ID:{} 异常信息:{}", goodsId, e);
            orderGoodsList = new ArrayList<OrderGoods>();
        }

        if (CollectionUtils.isEmpty(orderGoodsList)) {
            orderGoodsList = new ArrayList<OrderGoods>();
        }

        return orderGoodsList;
    }


    @Override
    public int save(OrderGoods orderGoods) {
        return orderGoodsDao.insert(orderGoods);
    }

    @Override
    public int update(OrderGoods orderGood) {
        return orderGoodsDao.updateById(orderGood);
    }

    @Override
    @Transactional
    public int batchInsert(List<OrderGoods> orderGoodsList) {
        return super.batchInsert(orderGoodsDao, orderGoodsList, 300);
    }

    @Override
    public int batchDel(Object[] ids) {
        return orderGoodsDao.deleteByIds(ids);
    }

    @Override
    public Optional<List<OrderGoods>> getOrderGoodList(long orderId, long shopId) {
        Map<String, Object> paramMap = new HashMap<String, Object>(2);
        paramMap.put("orderId", orderId);
        paramMap.put("shopId", shopId);

        List<OrderGoods> orderGoodsList = null;
        try {
            orderGoodsList = this.selectAndGoods(paramMap);
        } catch (Exception e) {
            LOGGER.error("获取工单物料异常，工单ID:{} 门店ID:{} 异常原因:{}",
                    orderId, shopId, e.toString());
            return Optional.absent();
        }

        return Optional.fromNullable(orderGoodsList);
    }


    @Override
    public Page<OrderGoods> getHistoryGoodList(Pageable pageable, Long customerCarId, Long shopId) {
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("customerCarId", customerCarId);
        searchParams.put("shopId", shopId);
        List<OrderInfo> historyOrderList = orderService.getHistoryOrderByCustomerCarId(customerCarId, shopId);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        List<OrderGoods> orderGoodsList = new ArrayList<>();
        //数据为空，返回空数据
        if (CollectionUtils.isEmpty(historyOrderList)) {
            return new DefaultPage<OrderGoods>(orderGoodsList, pageRequest, 0);
        }
        List<Long> historyOrderIdList = new ArrayList<>(historyOrderList.size());
        Map<Long, OrderInfo> orderMap = new HashMap<>(historyOrderList.size());
        for (OrderInfo orderInfo : historyOrderList) {
            historyOrderIdList.add(orderInfo.getId());
            orderMap.put(orderInfo.getId(), orderInfo);
        }
        Integer totalSize = orderGoodsDao.countHistoryGoodList(historyOrderIdList);
        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());
        List sorts = new ArrayList();
        sorts.add("id desc");
        searchParams.put("sorts", sorts);
        searchParams.put("orderIdList", historyOrderIdList);
        orderGoodsList = orderGoodsDao.getHistoryGoodList(searchParams);

        // 工单信息(工单ID,创建时间)
        for (OrderGoods goods : orderGoodsList) {
            Long referOrderId = goods.getOrderId();
            if (referOrderId != null && orderMap.containsKey(referOrderId)) {
                OrderInfo referOrder = orderMap.get(referOrderId);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                goods.setOrderCreateTimeStr(format.format(referOrder.getCreateTime()));
            }
        }

        DefaultPage<OrderGoods> page = new DefaultPage<OrderGoods>(orderGoodsList, pageRequest, totalSize);
        page.setSearchParam("customerCarId:" + customerCarId + "&shopId=" + shopId);
        return new DefaultPage<OrderGoods>(orderGoodsList, pageRequest, totalSize);
    }

    @Override
    public List<OrderGoods> selectByOrderIds(Long... orderIds) {
        if (orderIds == null) {
            return new ArrayList<>();
        }
        return orderGoodsDao.selectByOrderIds(orderIds);
    }

    @Override
    public List<OrderGoods> selectActual(List<Long> orderIds, Long shopId) {
        // 查询参数
        Map<String, Object> orderGoodsMap = new HashMap<>(3);
        orderGoodsMap.put("orderIds", orderIds);
        orderGoodsMap.put("shopId", shopId);
        orderGoodsMap.put("goodsType", OrderGoodTypeEnum.ACTUAL.getCode());

        List<OrderGoods> orderGoodsList = this.selectAndGoods(orderGoodsMap);

        if (CollectionUtils.isEmpty(orderGoodsList)) {
            return new ArrayList<>();
        }

        return orderGoodsList;
    }
}
