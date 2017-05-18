package com.tqmall.legend.facade.report.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.legend.bi.entity.StatisticsWarehouseIn;
import com.tqmall.legend.dao.goods.GoodsDao;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.warehousein.WarehouseInDao;
import com.tqmall.legend.dao.warehousein.WarehouseInDetailDao;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsCar;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.statistics.param.WarehouseInReportParam;
import com.tqmall.legend.entity.warehousein.WarehouseIn;
import com.tqmall.legend.entity.warehousein.WarehouseInDetail;
import com.tqmall.legend.facade.report.WarehouseInFacade;
import com.tqmall.legend.pojo.warehouse.WarehouseInTotalVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/9/9.
 */
@Service
@Slf4j
public class WarehouseInReportFacadeImpl implements WarehouseInFacade {
    @Autowired
    private WarehouseInDetailDao warehouseInDetailDao;
    @Autowired
    private WarehouseInDao warehouseInDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ShopManagerDao shopManagerDao;

    @Override
    public SimplePage<StatisticsWarehouseIn> getPage(WarehouseInReportParam warehouseInReportParam) {
        Integer totalSize = warehouseInDetailDao.selectReportCount(warehouseInReportParam);

        Integer pageNum = warehouseInReportParam.getPage();
        pageNum = pageNum < 1 ? 1 : pageNum-1;
        Integer pageSize = warehouseInReportParam.getSize();
        pageSize = pageSize < 1 ? 1 : pageSize;
        Integer offset = pageNum*pageSize;

        warehouseInReportParam.setOffset(offset);
        warehouseInReportParam.setSize(pageSize);

        List<StatisticsWarehouseIn> warehouseInDetailList = warehouseInDetailDao.selectReportInfo(warehouseInReportParam);
        List<Long> warehouseInIds = Lists.transform(warehouseInDetailList, new Function<StatisticsWarehouseIn, Long>() {
            @Override
            public Long apply(StatisticsWarehouseIn statisticsWarehouseIn) {
                return statisticsWarehouseIn.getId();
            }
        });
        if(CollectionUtils.isEmpty(warehouseInIds)){
            SimplePage<StatisticsWarehouseIn> objectSimplePage = new SimplePage<>();
            objectSimplePage.setEmptyPage();
            return objectSimplePage;
        }
        Map param = Maps.newHashMap();
        param.put("warehouseInDetailIds",warehouseInIds);
        List<String> orderBy = Lists.newArrayList();
        orderBy.add("gmt_create desc");
        param.put("sorts",orderBy);
        List<WarehouseInDetail> warehouseInDetails = warehouseInDetailDao.select(param);
        param.put("startTime",warehouseInReportParam.getStartTime());
        param.put("endTime",warehouseInReportParam.getEndTime());
        param.put("shopId",warehouseInReportParam.getShopId());
        List<StatisticsWarehouseIn> statisticsWarehouseIns = getStatisticsWarehouseIns(param,warehouseInDetails);

        SimplePage<StatisticsWarehouseIn> page = new SimplePage<>(totalSize,statisticsWarehouseIns, pageNum, pageSize);
        page.setSize(pageSize);
        return page;
    }

    private List<StatisticsWarehouseIn> getStatisticsWarehouseIns(Map<String, Object> searchParams, List<WarehouseInDetail> warehouseInDetailList) {
        List<StatisticsWarehouseIn> statisticsWarehouseInList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("shopId", searchParams.get("shopId"));
        List<Long> goodsIds = Lists.newArrayList();
        List<String> warehouseInSnList = Lists.newArrayList();

        for(WarehouseInDetail warehouseInDetail : warehouseInDetailList){
            goodsIds.add(warehouseInDetail.getGoodsId());
            warehouseInSnList.add(warehouseInDetail.getWarehouseInSn());
        }
        map.put("warehouseInSns",warehouseInSnList);
        List<WarehouseIn> warehouseInList = warehouseInDao.select(map);
        map.put("ids",goodsIds);
        List<Goods> goodsList = goodsDao.select(map);
        List<ShopManager> shopManagerList = shopManagerDao.select(map);

        Map<Long, Goods> goodsMap = new HashMap();
        for (Goods goods : goodsList) {
            goodsMap.put(goods.getId(), goods);
        }

        for (WarehouseInDetail warehouseInDetail : warehouseInDetailList) {
            StatisticsWarehouseIn item = new StatisticsWarehouseIn();
            item.setCreator(warehouseInDetail.getCreator());                 //创建人id
            item.setModifier(warehouseInDetail.getModifier());               //修改人id
            item.setGmtCreate(warehouseInDetail.getGmtCreate());             //入库日期
            item.setGmtModified(warehouseInDetail.getGmtModified());         //修改日期
            item.setWarehouseInSn(warehouseInDetail.getWarehouseInSn());     //单据号
        /*重构后的单据类型*/
            if (warehouseInDetail.getStatus() != null && warehouseInDetail.getStatus().equals("LZRK")) {
                item.setStatus("蓝字入库");
            } else if (warehouseInDetail.getStatus() != null && warehouseInDetail.getStatus().equals("HZRK")) {
                item.setStatus("红字入库");
            } else {
                item.setStatus("作废");
            }

            for (WarehouseIn warehouseIn : warehouseInList) {
                if (warehouseInDetail.getWarehouseInSn().equals(warehouseIn.getWarehouseInSn())) {
                    item.setSupplierName(warehouseIn.getSupplierName());     //供应商
                    item.setPurchaseAgentName(warehouseIn.getPurchaseAgentName());     //采购人
                    for (ShopManager shopManager : shopManagerList) {
                        if (warehouseIn.getCreator().equals(shopManager.getId())) {
                            item.setCreatorName(shopManager.getName());      //开单人
                        }
                    }
                }
            }
        /*
        for(Goods goods:goodsList){
            if(warehouseInDetail.getGoodsId().equals(goods.getId())){
                item.setGoodsFormat(goods.getFormat());         //零件号
                item.setGoodsName(goods.getName());             //零件名
                item.setDepot(goods.getDepot());                //仓位
                item.setMeasureUnit(goods.getMeasureUnit());  //单位
                for(GoodsCategory goodsCategory:goodsCategoryList){
                    if(goods.getCatId().equals(goodsCategory.getId())){
                        item.setCatName(goodsCategory.getCatName());      //类别
                    }
                }
            }
        }*/
            if (goodsMap.containsKey(warehouseInDetail.getGoodsId())) {
                item.setGoodsFormat(goodsMap.get(warehouseInDetail.getGoodsId()).getFormat());         //零件号
                item.setGoodsName(goodsMap.get(warehouseInDetail.getGoodsId()).getName());             //零件名
                item.setDepot(goodsMap.get(warehouseInDetail.getGoodsId()).getDepot());                //仓位
                item.setMeasureUnit(goodsMap.get(warehouseInDetail.getGoodsId()).getMeasureUnit());  //单位
                //获取类别
                item.setCatName(goodsMap.get(warehouseInDetail.getGoodsId()).getCat2Name());
//                Long catId = goodsMap.get(warehouseInDetail.getGoodsId()).getCatId();//物料的分类id
//                Integer tqmallStatus = goodsMap.get(warehouseInDetail.getGoodsId()).getTqmallStatus();//物料的来源
//                //判断是否来自tqmall
//                if (tqmallStatus == 4) {
//                    //自定义的物料
//                    if (goodsCategoryMap.containsKey(catId)) {
//                        item.setCatName(goodsCategoryMap.get(catId).getCatName());
//                    }
//                } else {
//                    //来自tqmall的物料
//                    if (goodsCategoryFromTqmallMap.containsKey(catId)) {
//                        item.setCatName(goodsCategoryFromTqmallMap.get(catId).getCatName());
//                    }
//                }
            }

            if (!StringUtils.isBlank(warehouseInDetail.getCarInfo())) {
                //将list字符串json转换成list对象 //适用车型
                if (!StringUtils.isBlank(warehouseInDetail.getCarInfo())) {
                    if (warehouseInDetail.getCarInfo().equals("0")) {
                        item.setCarInfoStr("通用配件");
                    } else {
                        try {
                            if(warehouseInDetail.getCarInfo().contains("[{") && warehouseInDetail.getCarInfo().contains("}]")){
                                List<GoodsCar> goodsCarList = new Gson().fromJson(
                                        warehouseInDetail.getCarInfo(),
                                        new TypeToken<List<GoodsCar>>() {
                                        }.getType());
                                item.setCarInfoList(goodsCarList);
                                StringBuilder carSb = new StringBuilder();
                                for (GoodsCar goodsCar : goodsCarList) {
                                    carSb.append(goodsCar.getCarBrandName()).append(" ");
                                }
                                //重构后的车类型
                                item.setCarInfoStr(carSb.toString());
                            }else{
                                item.setCarInfoStr(warehouseInDetail.getCarInfo());
                            }
                        } catch (Exception e) {
                            item.setCarInfoStr(warehouseInDetail.getCarInfo());
                        }
                    }
                }
            }
            item.setGoodsCount(warehouseInDetail.getGoodsCount().longValue());          //入库数量
            item.setGoodsRealCount(warehouseInDetail.getGoodsRealCount().longValue());  //可退数量
            if (item.getGoodsCount() < 0) {
                item.setGoodsReturnCount(item.getGoodsCount());                         //退货数量（等于实际入库数量为负数时的数量，为正数时为0）
            } else {
                item.setGoodsReturnCount(0L);
            }
            item.setPurchasePrice(warehouseInDetail.getPurchasePrice().setScale(2, BigDecimal.ROUND_HALF_UP));                //成本单价
            item.setPurchaseAmount((warehouseInDetail.getPurchasePrice().
                    multiply(warehouseInDetail.getGoodsCount())).setScale(2, BigDecimal.ROUND_HALF_UP));   //成本金额(成本单价*入库数量)
            item.setReturnAmount((warehouseInDetail.getPurchasePrice().
                    multiply(BigDecimal.valueOf(item.getGoodsReturnCount()))).setScale(2, BigDecimal.ROUND_HALF_UP));          //退货成本金额(成本单价*退货数量)
            item.setShopId(warehouseInDetail.getShopId());                              //门店id
            item.setGoodsId(warehouseInDetail.getGoodsId());                            //物料id
            statisticsWarehouseInList.add(item);
        }
        if (!CollectionUtils.isEmpty(statisticsWarehouseInList)) {
            BigDecimal totalPurchase = BigDecimal.ZERO;
            Long totalCount = 0L;
            for (StatisticsWarehouseIn statisticsWarehouseIn : statisticsWarehouseInList) {
                totalPurchase = totalPurchase.add(statisticsWarehouseIn.getPurchaseAmount());//总成本金额
                totalCount += statisticsWarehouseIn.getGoodsCount();                           //入库总数
                statisticsWarehouseIn.setTotalCount(totalCount);
                statisticsWarehouseIn.setTotalPurchase(totalPurchase.setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
        return statisticsWarehouseInList;
    }

    @Override
    public WarehouseInTotalVO getTotalInfo(WarehouseInReportParam warehouseInReportParam) {
        WarehouseInTotalVO totalInfo = warehouseInDetailDao.getTotalInfo(warehouseInReportParam);
        WarehouseInTotalVO totalAmount = warehouseInDetailDao.getTotalAmount(warehouseInReportParam);
        if (totalInfo == null) {
            totalInfo = new WarehouseInTotalVO();
        }
        if (totalAmount != null) {
            totalInfo.setTotalAmount(totalAmount.getTotalAmount());
            totalInfo.setTotalTax(totalAmount.getTotalTax());
            totalInfo.setTotalFreight(totalAmount.getTotalFreight());
        }
        return totalInfo;
    }

    @Override
    public Integer getWarehousInSize(WarehouseInReportParam warehouseInReportParam) {
        return warehouseInDetailDao.selectReportCount(warehouseInReportParam);
    }

    @Override
    public List<StatisticsWarehouseIn> getWarehouseExcelList(WarehouseInReportParam warehouseInReportParam) {
        warehouseInReportParam.setOffset(warehouseInReportParam.getOffset());
        warehouseInReportParam.setSize(warehouseInReportParam.getSize());

        List<StatisticsWarehouseIn> warehouseInDetailList = warehouseInDetailDao.selectReportInfo(warehouseInReportParam);
        List<Long> warehouseInIds = Lists.transform(warehouseInDetailList, new Function<StatisticsWarehouseIn, Long>() {
            @Override
            public Long apply(StatisticsWarehouseIn statisticsWarehouseIn) {
                return statisticsWarehouseIn.getId();
            }
        });
        if(CollectionUtils.isEmpty(warehouseInIds)){
            return Lists.newArrayList();
        }
        Map param = Maps.newHashMap();
        param.put("warehouseInDetailIds",warehouseInIds);
        List<String> orderBy = Lists.newArrayList();
        orderBy.add("gmt_create desc");
        param.put("sorts",orderBy);
        List<WarehouseInDetail> warehouseInDetails = warehouseInDetailDao.select(param);
        param.put("startTime",warehouseInReportParam.getStartTime());
        param.put("endTime",warehouseInReportParam.getEndTime());
        param.put("shopId",warehouseInReportParam.getShopId());
        List<StatisticsWarehouseIn> statisticsWarehouseIns = getStatisticsWarehouseIns(param,warehouseInDetails);
        return statisticsWarehouseIns;
    }

}
