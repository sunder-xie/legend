package com.tqmall.legend.facade.report.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tqmall.legend.bi.entity.StatisticsWarehouseOut;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.dao.order.OrderGoodsDao;
import com.tqmall.legend.dao.order.OrderInfoDao;
import com.tqmall.legend.dao.order.OrderTypeDao;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.warehouseout.WarehouseOutDao;
import com.tqmall.legend.dao.warehouseout.WarehouseOutDetailDao;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderType;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.statistics.param.WarehouseOutReportParam;
import com.tqmall.legend.entity.warehouseout.WarehouseOut;
import com.tqmall.legend.entity.warehouseout.WarehouseOutDetail;
import com.tqmall.legend.facade.report.WarehouseOutFacade;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by tanghao on 16/9/8.
 */
@Service
@Slf4j
public class WarehouseOutReportFacadeImpl implements WarehouseOutFacade {

    @Autowired
    private WarehouseOutDetailDao warehouseOutDetailDao;
    @Autowired
    WarehouseOutDao warehouseOutDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ShopManagerDao shopManagerDao;
    @Autowired
    OrderInfoDao orderInfoDao;
    @Autowired
    OrderGoodsDao orderGoodsDao;
    @Autowired
    OrderTypeDao orderTypeDao;

    @Override
    public SimplePage<StatisticsWarehouseOut> getPage(WarehouseOutReportParam warehouseOutReportParam) {
        Integer totalSize = warehouseOutDetailDao.selectReportCount(warehouseOutReportParam);

        Integer pageNum = warehouseOutReportParam.getPage();
        pageNum = pageNum < 1 ? 1 : pageNum-1;
        Integer pageSize = warehouseOutReportParam.getSize();
        pageSize = pageSize < 1 ? 1 : pageSize;
        Integer offset = pageNum*pageSize;

        warehouseOutReportParam.setOffset(offset);
        warehouseOutReportParam.setSize(pageSize);
        List<StatisticsWarehouseOut> resultlist = getStatisticsWarehouseOutList(warehouseOutReportParam);


        SimplePage<StatisticsWarehouseOut> page = new SimplePage<>(totalSize,resultlist,pageNum,pageSize);
        page.setSize(pageSize);
        return page;
    }

    @Override
    public StatisticsWarehouseOut getTotalInfo(WarehouseOutReportParam warehouseOutReportParam) {
        return warehouseOutDetailDao.getTotalInfo(warehouseOutReportParam);
    }

    @Override
    public Integer getWarehousOutSize(WarehouseOutReportParam warehouseOutReportParam) {
        return warehouseOutDetailDao.selectReportCount(warehouseOutReportParam);
    }

    @Override
    public List<StatisticsWarehouseOut> getWarehouseExcelList(WarehouseOutReportParam warehouseOutReportParam) {
        warehouseOutReportParam.setOffset(warehouseOutReportParam.getOffset());
        warehouseOutReportParam.setSize(warehouseOutReportParam.getSize());
       return getStatisticsWarehouseOutList(warehouseOutReportParam);
    }


    private List<StatisticsWarehouseOut> getStatisticsWarehouseOutList(WarehouseOutReportParam warehouseOutReportParam) {
        List<Long> warehouseOutDetailIds = warehouseOutDetailDao.selectReportInfo(warehouseOutReportParam);
        if(CollectionUtils.isEmpty(warehouseOutDetailIds)){
            return Lists.newArrayList();
        }
        Map searchParams = Maps.newHashMap();
        searchParams.put("ids",warehouseOutDetailIds);
        searchParams.put("shopId",warehouseOutReportParam.getShopId());
        searchParams.put("startTime",warehouseOutReportParam.getStartTime());
        searchParams.put("endTime",warehouseOutReportParam.getEndTime());
        List<String> orderBy = Lists.newArrayList();
        orderBy.add("gmt_create desc");
        searchParams.put("sorts",orderBy);
        if(CollectionUtils.isEmpty(warehouseOutDetailIds)){
            return Collections.emptyList();
        }
        List<WarehouseOutDetail> warehouseOutDetailList = warehouseOutDetailDao.select(searchParams);

        if (Langs.isEmpty(warehouseOutDetailList)) {
            return Collections.emptyList();
        }
        List<Long> goodIdList = Lists.newArrayList();
        List<Long> warehouseOutIds = Lists.newArrayList();
        List<Long> orderIdList = Lists.newArrayList();
        for (WarehouseOutDetail warehouseOutDetail : warehouseOutDetailList) {
            goodIdList.add(warehouseOutDetail.getGoodsId());
            warehouseOutIds.add(warehouseOutDetail.getWarehouseOutId());
            orderIdList.add(warehouseOutDetail.getOrderId());
        }

        List<StatisticsWarehouseOut> statisticsWarehouseOutList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", searchParams.get("shopId"));
        List<ShopManager> shopManagerList = shopManagerDao.select(map);

        if(!CollectionUtils.isEmpty(warehouseOutIds)){
            map.put("warehouseOutIds",warehouseOutIds);
        }
        List<WarehouseOut> warehouseOutList = warehouseOutDao.select(map);
        if(!CollectionUtils.isEmpty(goodIdList)){
            map.put("ids",goodIdList);
        }
        List<Goods> goodsList = goodsDao.select(map);

        if(!CollectionUtils.isEmpty(orderIdList)){
            map.put("orderIds",orderIdList);
        }
        List<OrderInfo> orderInfoList = orderInfoDao.select(map);
        List<OrderGoods> orderGoodsList = orderGoodsDao.select(map);

        //处理出库信息
        Map<Long, WarehouseOut> warehouseOutMap = new HashMap();
        for (WarehouseOut warehouseOut : warehouseOutList) {
            warehouseOutMap.put(warehouseOut.getId(), warehouseOut);
        }
        //处理物料信息
        Map<Long, Goods> goodsMap = new HashMap();
        for (Goods goods : goodsList) {
            goodsMap.put(goods.getId(), goods);
        }
        Map<Long, String> shopManagerMap = new HashMap();
        for (ShopManager shopManager : shopManagerList) {
            shopManagerMap.put(shopManager.getId(), shopManager.getName());
        }
        //处理订单信息
        List<Long> orderTypes = Lists.newArrayList();
        Map<Long, OrderInfo> orderInfoMap = new HashMap();
        for (OrderInfo orderInfo : orderInfoList) {
            orderInfoMap.put(orderInfo.getId(), orderInfo);
            orderTypes.add(orderInfo.getOrderType());
        }

        Map<Long,String> orderTypeMap = Maps.newHashMap();
        if(!CollectionUtils.isEmpty(orderTypes)){
            List<OrderType> orderTypeList = orderTypeDao.selectByIds(orderTypes.toArray(new Long[orderTypes.size()]));
            for (OrderType orderType : orderTypeList){
                orderTypeMap.put(orderType.getId(),orderType.getName());
            }
        }

        //处理订单物料信息
        Map<Long, OrderGoods> orderGoodsMap = new HashMap();
        for (OrderGoods orderGoods : orderGoodsList) {
            orderGoodsMap.put(orderGoods.getId(), orderGoods);
        }

        for (WarehouseOutDetail warehouseOutDetail : warehouseOutDetailList) {
            StatisticsWarehouseOut item = new StatisticsWarehouseOut();
            item.setGmtCreate(warehouseOutDetail.getGmtCreate());             //出库日期
            item.setGmtCreateStr(warehouseOutDetail.getGmtCreateStr());             //出库日期
            item.setGmtModified(warehouseOutDetail.getGmtModified());         //修改日期
            item.setWarehouseOutSn(warehouseOutDetail.getWarehouseOutSn()); //单据号
            //重构后的修改
            if (warehouseOutDetail.getStatus().equals("LZCK")) {
                item.setStatus("蓝字出库");
            } else if (warehouseOutDetail.getStatus().equals("HZCK")) {
                item.setStatus("红字出库");
            }

            Long warehouseOutId = warehouseOutDetail.getWarehouseOutId();
            WarehouseOut warehouseOut = warehouseOutMap.get(warehouseOutId);
            if (warehouseOut != null) {
                item.setCarLicense(warehouseOut.getCarLicense());//车辆牌照号
                item.setCustomerName(warehouseOut.getCustomerName()); //客户名
                item.setCustomerMobile(warehouseOut.getCustomerMobile());//客户手机号
            }
            Long goodsReceiver = warehouseOut.getGoodsReceiver();
            String shopManagerName = shopManagerMap.get(goodsReceiver);
            if (Langs.isNotBlank(shopManagerName)) {
                item.setGoodsReceiverName(shopManagerName);//领料人
            }

            Long goodsId = warehouseOutDetail.getGoodsId();
            Goods goods = goodsMap.get(goodsId);
            if (goods != null) {
                item.setDepot(goods.getDepot());//仓位
                item.setMeasureUnit(goods.getMeasureUnit()); //单位
                //获取类别
                item.setCatName(goods.getCat2Name());
            }

            Long orderId = warehouseOutDetail.getOrderId();
            OrderInfo orderInfo = orderInfoMap.get(orderId);
            if (orderInfo != null) {
                item.setReceiverName(orderInfo.getReceiverName());        //维修接待
                item.setOrderSn(orderInfo.getOrderSn());                  //工单号
                item.setOperatorName(orderInfo.getOperatorName());        //开单人
                item.setOrderTypeName(orderTypeMap.get(orderInfo.getOrderType()));
            }

            item.setGoodsFormat(warehouseOutDetail.getGoodsFormat());         //零件号
            item.setGoodsName(warehouseOutDetail.getGoodsName());             //零件名
            item.setGoodsCount(warehouseOutDetail.getGoodsCount());           //出库数量
            item.setGoodsRealCount(warehouseOutDetail.getGoodsRealCount()); //可退数量
            if (item.getGoodsCount().compareTo(BigDecimal.ZERO) < 0) {
                item.setGoodsReturnCount(warehouseOutDetail.getGoodsCount());//退库数量
            } else {
                item.setGoodsReturnCount(BigDecimal.ZERO);
            }
            item.setSalePrice(warehouseOutDetail.getSalePrice());             //出库价
            item.setInventoryPrice(warehouseOutDetail.getInventoryPrice());   //成本价
            item.setShopId(warehouseOutDetail.getShopId());                   //门店id
            item.setGoodsId(goodsId);                 //物料id
            item.setOrderId(orderId);                 //工单id
            item.setWarehouseOutId(warehouseOutId);   //出库单id
            item.setSalePriceAmount(warehouseOutDetail.getSalePrice().
                    multiply(warehouseOutDetail.getGoodsCount()));  //出库金额（出库价*出库数量）
            item.setReturnAmount(warehouseOutDetail.getSalePrice().
                    multiply(BigDecimal.valueOf(item.getGoodsReturnCount().longValue()))); //退库金额（出库价*退库数量）
            item.setInventoryPriceAmount(warehouseOutDetail.getInventoryPrice().
                    multiply(warehouseOutDetail.getGoodsCount())); //成本金额（成本价*出库数量）


            BigDecimal discountAmount = BigDecimal.valueOf(0);
            if (orderGoodsMap.containsKey(warehouseOutDetail.getOrderGoodsId())) {
                discountAmount = orderGoodsMap.get(warehouseOutDetail.getOrderGoodsId()).getDiscount().
                        multiply(warehouseOutDetail.getGoodsCount());                  //材料优惠
            }
            item.setRelPayAmount(item.getSalePriceAmount().subtract(discountAmount).
                    setScale(2, BigDecimal.ROUND_HALF_UP)); //实付金额：配件的材料费-材料优惠
            item.setProfitAmount(item.getRelPayAmount().subtract(item.getInventoryPriceAmount()).
                    setScale(2, BigDecimal.ROUND_HALF_UP));//利润：材料费-材料优惠-成本 即 实付金额-成本
            statisticsWarehouseOutList.add(item);
        }
        return statisticsWarehouseOutList;
    }
}
