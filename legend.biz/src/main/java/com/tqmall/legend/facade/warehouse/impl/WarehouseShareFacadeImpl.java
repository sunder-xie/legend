package com.tqmall.legend.facade.warehouse.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.BizAssert;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.sensitive.SensitiveWordsService;
import com.tqmall.legend.biz.warehouse.WarehouseShareService;
import com.tqmall.legend.biz.warehouse.WarehouseShareShopContactService;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsQueryRequest;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.warehouseshare.GoodsStatusEnum;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;
import com.tqmall.legend.facade.warehouse.WarehouseShareFacade;
import com.tqmall.legend.facade.warehouse.vo.Over2MonthTurnoverVO;
import com.tqmall.legend.facade.warehouse.vo.PublishGoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/11/10.
 */
@Service
@Slf4j
public class WarehouseShareFacadeImpl implements WarehouseShareFacade {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SensitiveWordsService sensitiveWordsService;
    @Autowired
    private WarehouseShareService warehouseShareService;
    @Autowired
    private WarehouseShareShopContactService warehouseShareShopContactService;
    @Autowired
    private ShopManagerService shopManagerService;

    @Override
    public Over2MonthTurnoverVO getOver2MonthTurnoverInfo(Long shopId,Integer month) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.notNull(month,"查询月份范围不能为空.");
        Integer goodsKindsNum = goodsService.getNMonthUnChangeGoodsNum(shopId,month);
        Goods goodsAmount = goodsService.getNMonthUnChangeGoodsCostAndPrice(shopId,month);
        //成本金额
        BigDecimal goodsCost = goodsAmount.getInventoryPrice();
        //出售金额
        BigDecimal goodsPrice = goodsAmount.getPrice();
        /**
         * 利润金额 = 出售金额 - 成本金额
         */
        BigDecimal benefitAmount = goodsPrice.subtract(goodsCost);
        /**
         * 平均库存 = (期初库存 + 期末库存)/2
         */
        BigDecimal avgCostAmount = goodsService.getAvgGoodsStock(shopId,month);
        /**
         * 周转率 = 出库金额除/平均库存
         */
        BigDecimal turnoverRate = BigDecimal.ZERO;
        if(BigDecimal.ZERO.compareTo(avgCostAmount) != 0){
            turnoverRate = goodsCost.divide(avgCostAmount,2, RoundingMode.HALF_UP);
        }

        /**
         * 损失金额 = 利润金额 * 周转率
         */
        BigDecimal lossAmount = benefitAmount.multiply(turnoverRate);
        Over2MonthTurnoverVO vo = new Over2MonthTurnoverVO();
        vo.setGoodsKindsNum(goodsKindsNum);
        vo.setInventoryPrice(goodsCost);
        vo.setLossAmount(lossAmount);
        return vo;
    }

    @Override
    public PublishGoodsVO publishGoods(List<WarehouseShare> warehouseShares,Long userId) {
        /**
         * 校验参数
         */
        Assert.notEmpty(warehouseShares,"待发布对象不能为空");
        List<Long> goodsIds = Lists.newArrayListWithCapacity(warehouseShares.size());
        for(WarehouseShare ws : warehouseShares){
            checkPublishParam(ws);
            ws.setGoodsStatus(GoodsStatusEnum.SALE.getCode());
            ws.setLastSaleTime(new Date());
            ws.setCreator(userId);
            goodsIds.add(ws.getGoodsId());
        }

        /**
         * 校验敏感词
         */
        List<String> goodsNames = Lists.newArrayList();
        for(WarehouseShare ws : warehouseShares){
            goodsNames.add(ws.getGoodsName());
        }
        Result<Integer[]> result = sensitiveWordsService.checkStringArrayHasSensitiveWord(goodsNames.toArray(new String[goodsNames.size()]));
        BizAssert.notNull(result,"敏感词检测接口调用异常");
        Integer[] resultInteger = result.getData();
        PublishGoodsVO publishGoodsVO = new PublishGoodsVO();
        if(null == resultInteger){
            publishGoodsVO.setPublishNum(goodsNames.size());
            publishGoodsVO.setFailNum(0);
        }else {
            publishGoodsVO.setPublishNum(goodsNames.size()-resultInteger.length);
            publishGoodsVO.setFailNum(resultInteger.length);
            for(Integer i : resultInteger){
                WarehouseShare ws = warehouseShares.get(i);
                if(null != ws){
                    ws.setGoodsRemark("含敏感词");
                    ws.setGoodsStatus(GoodsStatusEnum.UNPASS.getCode());
                }else {
                    log.error("发现异常数据!请检查敏感词接口.入参:{},出参:{}",
                            ObjectUtils.objectToJSON(goodsNames),ObjectUtils.objectToJSON(resultInteger));
                }
            }
        }
        List<Goods> goodses = goodsService.selectByIds(goodsIds.toArray(new Long[goodsIds.size()]));
        warehouseShares = assembleList(warehouseShares,goodses);
        Integer num = warehouseShareService.batchInsert(warehouseShares);
        BizAssert.notNull(num,"库存出售出错");
        return publishGoodsVO;
    }

    private void checkPublishParam(WarehouseShare ws){
        Assert.notNull(ws.getGoodsName(),"商品名称不能为空.");
        Assert.isTrue(ws.getGoodsName().length()<51,"商品名不能大于50个字符.");
        Assert.notNull(ws.getGoodsId(),"商品id不能为空.");
        Assert.notNull(ws.getGoodsPrice(),"商品售价不能为空.");
        Assert.notNull(ws.getGoodsStock(),"商品数量不能为空.");
        Assert.notNull(ws.getInventoryPrice(),"商品成本不能为空.");
        Assert.isTrue(ws.getGoodsPrice().compareTo(BigDecimal.valueOf(99999999))<0,"售价不能大于8位数");
        Assert.isTrue(ws.getGoodsStock().compareTo(BigDecimal.valueOf(99999999))<0,"库存数量不能大于8位数");
        Assert.isTrue(ws.getSaleNumber().compareTo(BigDecimal.valueOf(99999999))<0,"出售价不能大于8位数");

        Assert.isTrue(ws.getGoodsPrice().compareTo(BigDecimal.ZERO)>0,"商品价格不能小于等于零");
        Assert.isTrue(ws.getGoodsStock().compareTo(BigDecimal.ZERO)>0,"库存数量不能小于等于零");
        Assert.isTrue(ws.getSaleNumber().compareTo(BigDecimal.ZERO)>0,"库存数量不能小于等于零");
        Assert.hasText(ws.getGoodsName(),"商品名称不能为空");
    }

    private List<WarehouseShare> assembleList(List<WarehouseShare> warehouseShares,List<Goods> goodses){
        Map<Long,Goods> map = Maps.newHashMap();
        for(Goods g : goodses){
            map.put(g.getId(),g);
        }
        for(WarehouseShare ws : warehouseShares){
            Goods g = map.get(ws.getGoodsId());
            if(null != g){
                assembleWarehouseShare(ws,g);
            }
        }
        return warehouseShares;
    }

    private void assembleWarehouseShare(WarehouseShare warehouseShare,Goods goods){
        warehouseShare.setBrandName(goods.getBrandName());
        warehouseShare.setCarInfo(goods.getCarInfo());
        warehouseShare.setCatName(goods.getCat2Name());
        warehouseShare.setGoodsOrigin(goods.getOrigin());
        warehouseShare.setGoodsFormat(goods.getFormat());
        warehouseShare.setImgUrl(goods.getImgUrl());
        warehouseShare.setMeasureUnit(goods.getMeasureUnit());
        warehouseShare.setPartUsedTo(goods.getPartUsedTo());
        warehouseShare.setLastInTime(goods.getLastInTime());
        warehouseShare.setInventoryPrice(goods.getInventoryPrice());
        warehouseShare.setGoodsStock(goods.getStock());
    }

    @Override
    public String rePublishGoods(WarehouseShare warehouseShare,Long userId) {
        /**
         * 参数校验
         */
        checkPublishParam(warehouseShare);

        /**
         * 设置属性
         */
        warehouseShare.setGoodsStatus(GoodsStatusEnum.SALE.getCode());
        warehouseShare.setLastSaleTime(new Date());
        warehouseShare.setModifier(userId);

        /**
         * 敏感词检测
         */
        String[] goodsNames = new String[1];
        goodsNames[0] = warehouseShare.getGoodsName();
        Result<Integer[]> result = sensitiveWordsService.checkStringArrayHasSensitiveWord(goodsNames);
        BizAssert.notNull(result,"敏感词检测接口调用异常.");
        BizAssert.isTrue(result.isSuccess(),"敏感词接口调用失败.");

        Optional<Goods> goodsOptional = goodsService.selectById(warehouseShare.getGoodsId());
        if(goodsOptional.isPresent()){
            assembleWarehouseShare(warehouseShare,goodsOptional.get());
        }

        /**
         * 若含有敏感词则返回false,否则更新
         */
        if(null != result.getData()){
            warehouseShare.setGoodsStatus(GoodsStatusEnum.UNPASS.getCode());
            warehouseShare.setGoodsRemark("含敏感词");
            warehouseShareService.update(warehouseShare);
            return "含敏感词,发布失败";
        }else {
            warehouseShare.setGoodsRemark("");
            warehouseShareService.update(warehouseShare);
            return "发布成功";
        }

    }

    @Override
    public WarehouseShareShopContact checkContact(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        List<WarehouseShareShopContact> warehouseShareShopContacts = warehouseShareShopContactService.checkContactWithShopInfoByShopId(shopId);
        if(CollectionUtils.isEmpty(warehouseShareShopContacts)||warehouseShareShopContacts.size()==0){
            return null;
        }
        return warehouseShareShopContacts.get(0);
    }

    @Override
    public List<WarehouseShareShopContactVO> queryShopContact(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        return warehouseShareShopContactService.queryContactWithShopInfoByShopId(shopId);
    }

    @Override
    public boolean changeGoodsStatus(Long id, Integer goodsStatus) {
        Assert.notNull(id,"出售库存id不能为空.");
        Assert.notNull(goodsStatus,"要改变的出售库存状态不能为空.");
        WarehouseShare warehouseShare = new WarehouseShare();
        warehouseShare.setId(id);
        warehouseShare.setGoodsStatus(goodsStatus);
        //如果是改为出售状态,则更新最后出售时间
        if(goodsStatus.equals(GoodsStatusEnum.SALE.getCode())){
            warehouseShare.setLastSaleTime(new Date());
        }
        return warehouseShareService.update(warehouseShare)>0;
    }

    @Override
    public SimplePage<WarehouseShare> querySaleListByGoodsStatus(GoodsQueryRequest goodsQueryRequest) {
        Assert.notNull(goodsQueryRequest,"查询参数对象不能为空.");
        Assert.notNull(goodsQueryRequest.getShopId(),"门店id不能为空.");
        Integer totalSize = warehouseShareService.queryCountForSaleListByGoodsStatus(goodsQueryRequest.getShopId(),goodsQueryRequest.getGoodsStatus());
        List<WarehouseShare> warehouseShares = warehouseShareService.querySaleListByGoodsStatus(goodsQueryRequest.getShopId(),goodsQueryRequest.getGoodsStatus(),goodsQueryRequest.getPage(),goodsQueryRequest.getSize());
        return new SimplePage<WarehouseShare>(totalSize,warehouseShares,goodsQueryRequest.getPage(),goodsQueryRequest.getSize());
    }

    @Override
    @Transactional
    public boolean changeShopContact(Long shopId, Long userId) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.notNull(userId,"员工id不能为空.");
        WarehouseShareShopContact wssc = new WarehouseShareShopContact();
        wssc.setShopId(shopId);
        ShopManager sm = shopManagerService.selectById(userId);
        Assert.notNull(sm,"门店不存在该员工,userId:"+userId);
        wssc.setContactId(userId);
        wssc.setContactMobile(sm.getMobile());
        wssc.setContactName(sm.getName());
        warehouseShareShopContactService.deleteByShopId(wssc.getShopId());
        return warehouseShareShopContactService.insert(wssc)>0;
    }

    @Override
    public WarehouseShareCountVO querySaleCount(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        return warehouseShareService.querySaleCount(shopId);
    }

    @Override
    public boolean isPublish(Long shopId) {
        Assert.notNull(shopId,"店铺id不能为空.");
        Integer num = warehouseShareService.queryCountForSaleListByGoodsStatus(shopId,null);
        return num>0;
    }

}
