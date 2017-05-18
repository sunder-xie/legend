package com.tqmall.legend.facade.report.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.*;
import com.tqmall.cube.shop.result.BrandRankDTO;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.SeriesRankDTO;
import com.tqmall.cube.shop.result.VisitantStatisticDTO;
import com.tqmall.cube.shop.result.account.*;
import com.tqmall.cube.shop.result.businessoverview.BusinessOverviewDTO;
import com.tqmall.cube.shop.result.businessoverview.BusinessTrend;
import com.tqmall.cube.shop.result.businessoverview.PaidInStatisticsDTO;
import com.tqmall.cube.shop.result.businessoverview.PaidOutStatisticsDTO;
import com.tqmall.cube.shop.result.order.OrderServiceCatInfoDTO;
import com.tqmall.cube.shop.result.order.OrderServiceDTO;
import com.tqmall.cube.shop.result.order.SprayOrderServiceCateDTO;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateAmountDTO;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateStatDTO;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsCateTotalDTO;
import com.tqmall.cube.shop.result.warehouseanalysis.GoodsStatDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.BusinessMonthlyFacade;
import com.tqmall.legend.facade.report.convert.*;
import com.tqmall.legend.facade.report.convert.account.*;
import com.tqmall.legend.facade.report.vo.*;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailDTO;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailParam;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseOutDetailService;
import com.tqmall.search.dubbo.client.legend.warehouseout.LegendWarehouseTopDTO;
import com.tqmall.wheel.lang.DatePair;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by majian on 16/9/1.
 */
@Service
@Slf4j
public class BusinessMonthlyFacadeImpl implements BusinessMonthlyFacade {
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;
    @Autowired
    private RpcShopBusinessOverviewService rpcShopBusinessOverviewService;
    @Autowired
    private RpcOrderStatService rpcOrderStatService;
    @Autowired
    private RpcCarModelStatisticService carModelStatisticService;
    @Autowired
    private RpcCardCouponRechargeService rpcCardCouponRechargeService;
    @Autowired
    private LegendWarehouseOutDetailService legendWarehouseOutDetailService;
    @Autowired
    private RpcWarehouseAnalysisService rpcWarehouseAnalysisService;

    @Override
    public BusinessOverviewVo getBusinessOverview(Long shopId, String month) throws BizException {
        Result<BusinessOverviewDTO> result = this.rpcShopBusinessOverviewService.getBusinessOverviewInfo(shopId, month);
        if(result == null || !result.isSuccess()) {
            log.error("获取经营总览数据异常.shopId={}, month={}, result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取营业总览数据异常.");
        } else {
            return new BusinessOverviewConvert().convert(result.getData());
        }
    }

    /**
     * 经营总览--面漆总数
     *
     * @param shopId
     * @param month
     * @return
     */
    @Override
    public BigDecimal getTotalSurfaceNumMonthly(Long shopId, String month) throws BizException {
        Result<BigDecimal> result = rpcShopBusinessOverviewService.getTotalSurfaceNumMonthly(shopId, month);
        if (result == null || !result.isSuccess()) {
            log.error("获取经营总览面漆总数异常.shopId={}, month={}, result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取营业总览面漆总数异常.");
        }
        return result.getData();
    }

    @Override
    public PaidInStatisticsVo getBusinessPaidIn(Long shopId, String month) {
        Result<PaidInStatisticsDTO> result = this.rpcShopBusinessOverviewService.getPaidInStatistics(shopId, month);
        if (result == null || !result.isSuccess()) {
            log.error("获取经营总览-实收数据失败.shopId={},month={},result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取营业总览-应收数据异常.");
        } else {
            return new BusinessPaidInConvert().convert(result.getData());
        }
    }

    @Override
    public PaidOutStatisticsVo getBusinessPaidOut(Long shopId, String month) throws BizException {
        Result<PaidOutStatisticsDTO> result = this.rpcShopBusinessOverviewService.getPaidOutStatistics(shopId, month);

        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取经营总览-实付数据失败.shopId={},month={},result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取营业总览-实付数据异常.");
        } else {
            return new BusinessPaidOutConvert().convert(result.getData());
        }
    }

    @Override
    public BusinessTrendVo getSumOfBusinessByMonth(Long shopId, String month) throws BizException {
        Result<BusinessTrend> result = this.rpcShopBusinessOverviewService.getSumOfBusinessMonthly(shopId, month);

        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取经营总览-营业额月趋势数据失败.shopId={},month={},result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取经营总览-营业额月趋势数据失败.");
        } else {
            return new BusinessTrendConvert().convert(result.getData());
        }
    }

    @Override
    public BusinessTrendVo getPaidInByMonth(Long shopId, String month) throws BizException {
        Result<BusinessTrend> result = this.rpcShopBusinessOverviewService.getPaidInMonthly(shopId, month);

        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取经营总览-实收金额月趋势数据失败.shopId={},month={},result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取经营总览-实收金额月趋势数据失败");
        } else {
            return new BusinessTrendConvert().convert(result.getData());
        }
    }

    @Override
    public BusinessTrendVo getSumOfBusinessByYear(Long shopId, String year) throws BizException {
        Result<BusinessTrend> result = this.rpcShopBusinessOverviewService.getSumOfBusinessYearly(shopId, year);

        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取经营总览-营业额年趋势数据失败.shopId={},year={},result:{}", shopId, year, Objects.toJSON(result));
            throw new BizException("获取经营总览-营业额年趋势数据失败.");
        } else {
            return new BusinessTrendConvert().convert(result.getData());
        }
    }

    @Override
    public BusinessTrendVo getPaidInByYear(Long shopId, String year) throws BizException {
        Result<BusinessTrend> result = this.rpcShopBusinessOverviewService.getPaidInYearly(shopId, year);

        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取经营总览-实收金额年趋势数据失败.shopId={},year={},result:{}", shopId, year, Objects.toJSON(result));
            throw new BizException("获取经营总览-实收金额年趋势数据失败.");
        } else {
            return new BusinessTrendConvert().convert(result.getData());
        }
    }

    @Override
    public Page<OrderServiceCatVo> getSortedOrderServiceCatByPage(Long shopId, String month, Pageable page) {
        Result<DefaultResult<OrderServiceCatInfoDTO>> result = this.rpcOrderStatService.getBusinessMonthOrderServiceCatInfoPage(shopId, month, page.getPageNumber(), page.getPageSize());
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取服务统计-服务类型统计数据异常.shopId={}, month={}, result={}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取服务统计-服务类型统计数据异常");
        } else {
           return new OrderServiceCatConvert().convert(result.getData());
        }
    }

    @Override
    public Page<OrderServiceVo> getSortedOrderServiceByPage(Long shopId, String month, String key, Pageable page) throws BizException {
        Result<DefaultResult<OrderServiceDTO>> rpcResult = rpcOrderStatService.getBusinessMonthOrderServiceTypePage(shopId, month, key, page.getPageNumber(), page.getPageSize());
        if (Objects.isNull(rpcResult) || !rpcResult.isSuccess()) {
            log.error("获取服务统计-服务销量统计数据异常.shopId={}, month={}, keyword={} result={}", shopId, month,key, Objects.toJSON(rpcResult));
            throw new BizException("获取服务统计-服务销量统计数据异常");
        } else {
            return new OrderServiceRankConverter().convert(rpcResult.getData());
        }
    }

    @Override
    public List<OrderServiceCatForPie> getOrderServiceCatForPie(Long shopId, String month) {
        Result<List<OrderServiceCatInfoDTO>> result = this.rpcOrderStatService.getBusinessMonthOrderServiceCatInfoForPic(shopId, month);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取服务统计-服务类型统计数据异常.shopId={},month={}, result={}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取服务统计-服务类型统计数据异常.");
        } else {
            return new OrderServiceCatForPieConvert().convert(result.getData());
        }
    }

    /**
     * 查询钣喷服务分类
     *
     * @param shopId
     * @param month
     * @param shared
     * @param pageable
     * @return
     */
    @Override
    public Page<SprayOrderServiceCateVO> getSprayOrderServiceCatePage(Long shopId, String month, boolean shared, Pageable pageable) {
        Result<DefaultResult<SprayOrderServiceCateDTO>> result = rpcOrderStatService.getSprayOrderServiceCatePage(shopId, month, shared, pageable.getPageNumber(), pageable.getPageSize());
        if (result == null || !result.isSuccess()) {
            log.error("获取钣喷服务分类异常.shopId={}, month={}, result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取钣喷服务分类异常.");
        }
        DefaultResult<SprayOrderServiceCateDTO> data = result.getData();
        if (data == null) {
            return new DefaultPage<>(Collections.<SprayOrderServiceCateVO>emptyList(), pageable, 0);
        }
        return new DefaultPage<>(SprayOrderServiceCateConvert.convertVOList(data.getContent()), pageable, data.getTotal());
    }

    /**
     * 查询钣喷服务销售额总计
     *
     * @param shopId
     * @param month
     * @param shared
     * @return
     */
    @Override
    public BigDecimal getTotalSpraySaleAmount(Long shopId, String month, boolean shared) {
        Result<BigDecimal> result = rpcOrderStatService.getTotalSpraySaleAmount(shopId, month, shared);
        if (result == null || !result.isSuccess()) {
            log.error("查询钣喷服务销售额总计异常.shopId={}, month={}, result:{}", shopId, month, Objects.toJSON(result));
            throw new BizException("查询钣喷服务销售额总计异常.");
        }
        return result.getData();
    }

    @Override
    public List<CarBrandRankVo> getCarBrandTop10(Long shopId, String month) {
        Result<List<BrandRankDTO>> result = this.carModelStatisticService.statisticBrandRank(shopId, month);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("获取品牌车型统计-品牌TOP10数据异常.shopId={},month={},result={}", shopId, month, Objects.toJSON(result));
            throw new BizException("获取品牌车型统计-品牌TOP10数据异常.");
        } else {
            return new CarBrandRankConvert().convert(result.getData());
        }
    }

    @Override
    public VistantStatisticVo statisticMonthlyVisitant(Long shopId, String month) throws BizException {
        Result<VisitantStatisticDTO> rpcResult = rpcCustomerInfoService.statisticMonthlyVisitant(shopId, month);

        if (!rpcResult.isSuccess() || rpcResult.getData() == null) {
            log.error("调用月度访客统计失败,shopId={}, month={}",shopId, month, rpcResult);
            throw new BizException("获取数据失败");
        }

        VisitantStatisticDTO dto = rpcResult.getData();
        VistantStatisticVo vo = new VistantConverter().convert(dto);
        return vo;
    }

    @Override
    public Page<SeriesRankVO> statisticSeriesRank(Long shopId, String monthStr, Integer pageIndex, Integer pageSize) {
        Result<DefaultResult<SeriesRankDTO>> seriesRankDTOResult =  carModelStatisticService.statisticSeriesRank(shopId,monthStr,pageIndex,pageSize);
        if(!seriesRankDTOResult.isSuccess() || seriesRankDTOResult.getData() == null){
            log.error("调用月度车型排行接口失败,shopId="+shopId+",month="+monthStr, LogUtils.objectToString(seriesRankDTOResult));
            throw new BizException("调用月度车型排行接口失败.");
        }
        return new SeriesRankConverter().convert(seriesRankDTOResult.getData());
    }

    @Override
    public List<MonthMemberVO> getCardHandOutList(Long shopId, String month) throws BizException{
        Result<List<BusinessMonthMemberVO>> result = rpcCardCouponRechargeService.getBusinessMonthMemberCardList(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月度会员卡发放列表接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月度会员卡发放列表接口失败");
        }
        return new BusinessMonthMemberListConvert().convert(result.getData());
    }

    @Override
    public MonthMemberStripVO getCardStripInfo(Long shopId, String month) throws BizException{
        Result<BusinessMemberInfoVO> result = rpcCardCouponRechargeService.getBusinessMonthMemberForStrip(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月度会员卡发放条形图接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月度会员卡发放条形图接口失败");
        }
        return new BusinessMonthMemberStripConvert().convert(result.getData());
    }

    @Override
    public List<MonthComboVO> getComboHandOutList(Long shopId, String month) throws BizException {
        Result<List<BusinessMonthComboVO>> result = rpcCardCouponRechargeService.getBusinessMonthComboList(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月计次卡发放列表接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月计次卡发放列表接口失败");
        }
        return new BusinessMonthComboListConvert().convert(result.getData());
    }

    @Override
    public BigDecimal getCouponStripInfo(Long shopId, String month) throws BizException {
        Result<BusinessMonthCouponInfoVO> result = rpcCardCouponRechargeService.getBusinessMonthCouponForStrip(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月优惠券发放条形图接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月优惠券发放条形图接口失败");
        }
        return result.getData().getDiscountAmount();
    }

    @Override
    public MonthComboStripVO getComboStripInfo(Long shopId, String month) throws BizException {
        Result<BusinessMonthComboInfoVO> result = rpcCardCouponRechargeService.getBusinessMonthComboForStrip(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月计次卡发放条形图接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月计次卡发放条形图接口失败");
        }
        return new BusinessMonthComboStripConvert().convert(result.getData());
    }

    @Override
    public List<MonthCouponVO> getCouponHandOutList(Long shopId, String month) throws BizException {
        Result<List<BusinessMonthCouponVO>> result = rpcCardCouponRechargeService.getBusinessMonthCouponList(shopId,month);
        if(result==null || !result.isSuccess() || null == result.getData()){
            log.error("调用月度优惠券发放列表接口失败,shopId="+shopId+",month="+month, LogUtils.objectToString(result));
            throw new BizException("调用月度优惠券发放列表接口失败");
        }
        return new BusinessMonthCouponListConvert().convert(result.getData());
    }

    @Override
    public GoodsCatRankPage statisticGoodsCatRank(Long shopId, DatePair timeArea, String tag, Integer limit, Integer offset) {
        LegendWarehouseOutDetailParam param = new LegendWarehouseOutDetailParam();
        param.setShopId(shopId.intValue());
        param.setSTime(DateUtil.convertDateToYMD(timeArea.getStartTime()));
        param.setETime(DateUtil.convertDateToYMD(timeArea.getEndTime()));
        param.setTag(tag);
        param.setLimit(limit);
        param.setOffset(offset);
        com.tqmall.search.common.result.Result<Page<LegendWarehouseOutDetailDTO>> searchResult = legendWarehouseOutDetailService.getCatWarehouseSummary(param);
        if (searchResult == null || !searchResult.isSuccess() || searchResult.getData() == null) {
            log.error("调用搜索查询配件销售类型统计失败, param = {}, result={}", param, ObjectUtils.objectToJSON(searchResult));
            throw new BizException("调用搜索查询配件销售类型统计失败");
        }

        Page<LegendWarehouseOutDetailDTO> pageData = searchResult.getData();
        List<GoodsCatSaleRankVo> resultContent = new GoodsCatRankListConverter().convert(pageData.getContent());
        int rank = offset;
        for (GoodsCatSaleRankVo goodsCatSaleRankVo : resultContent) {
            goodsCatSaleRankVo.setRank(rank);
            rank++;
        }
        return new GoodsCatRankPage((int)pageData.getTotalElements(), resultContent);
    }

    @Override
    public List<GoodsCatSaleTopVo> statisticGoodsCatTop(Long shopId, DatePair datePair, String tag) {
        LegendWarehouseOutDetailParam param = new LegendWarehouseOutDetailParam();
        param.setShopId(shopId.intValue());
        param.setTag(tag);
        param.setSTime(DateUtil.convertDateToYMD(datePair.getStartTime()));
        param.setETime(DateUtil.convertDateToYMD(datePair.getEndTime()));
        com.tqmall.search.common.result.Result<LegendWarehouseTopDTO> searchResult =
                legendWarehouseOutDetailService.getLegendWarehouseOutTop(param);
        if (searchResult == null || !searchResult.isSuccess() || searchResult.getData() == null) {
            log.error("调用搜索查询配件销售类型top统计失败, param = {}, result={}", param, ObjectUtils.objectToJSON(searchResult));
            throw new BizException("调用搜索查询配件销售类型top统计失败");
        }
        LegendWarehouseTopDTO data = searchResult.getData();
        List<LegendWarehouseOutDetailDTO> legendWarehouseOutDetailDTOs = data.getLegendWarehouseOutDetailDTOs();

        //取前五
        List<LegendWarehouseOutDetailDTO> top5Dtos = legendWarehouseOutDetailDTOs;
        if (legendWarehouseOutDetailDTOs.size() >=5) {
            top5Dtos = legendWarehouseOutDetailDTOs.subList(0, 5);
        }
        List<GoodsCatSaleTopVo> top5Vos = new GoodsCatTopListConverter().convert(top5Dtos);

        //添加"其他"
        GoodsCatSaleTopVo topElseVo = new GoodsCatSaleTopVo();
        topElseVo.setCatName("其他");
        BigDecimal countSum = BigDecimal.valueOf(data.getGoodsCountSum());
        BigDecimal top5Count = BigDecimal.ZERO;
        for (GoodsCatSaleTopVo top5Vo : top5Vos) {
            top5Count = top5Count.add(top5Vo.getSaleCount());
        }
        topElseVo.setSaleCount(countSum.add(top5Count.negate()));
        BigDecimal top5Amount = BigDecimal.ZERO;
        for (GoodsCatSaleTopVo top5Vo : top5Vos) {
            top5Amount = top5Amount.add(top5Vo.getSaleAmount());
        }
        BigDecimal incomeSum = BigDecimal.valueOf(data.getIncomeSum());
        topElseVo.setSaleAmount(incomeSum.add(top5Amount.negate()));
        top5Vos.add(topElseVo);

        return top5Vos;
    }

    @Override
    public GoodsRankPage statisticGoodsRank(Long shopId, DatePair datePair, String month, String keyword, Integer limit, Integer offset) {
        LegendWarehouseOutDetailParam param = new LegendWarehouseOutDetailParam();
        param.setShopId(shopId.intValue());
        param.setGoodsName(keyword);
        param.setSTime(DateUtil.convertDateToYMD(datePair.getStartTime()));
        param.setETime(DateUtil.convertDateToYMD(datePair.getEndTime()));
        param.setLimit(limit);
        param.setOffset(offset);
        com.tqmall.search.common.result.Result<Page<LegendWarehouseOutDetailDTO>> searchResult = legendWarehouseOutDetailService.getGoodsNameWarehouseSummary(param);

        if (searchResult == null || !searchResult.isSuccess() || searchResult.getData() == null) {
            log.error("调用搜索查询配件销售统计失败, param = {}, result={}", param, ObjectUtils.objectToJSON(searchResult));
            throw new BizException("调用搜索查询配件销售统计失败");
        }
        Page<LegendWarehouseOutDetailDTO> pageData = searchResult.getData();
        List<GoodsSaleRankVo> resultContent = new GoodsRankListConverter().convert(pageData.getContent());
        int rank = offset;
        for (GoodsSaleRankVo goodsSaleRankVo : resultContent) {
            goodsSaleRankVo.setRank(rank);
            rank++;
        }
        return new GoodsRankPage((int)pageData.getTotalElements(), resultContent);
    }

    /**
     * 查询当前库存总金额
     *
     * @param shopId
     * @return
     */
    @Override
    public BigDecimal getAllInventoryAmount(Long shopId) {
        Result<BigDecimal> result = rpcWarehouseAnalysisService.getAllInventoryAmount(shopId);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("【营业月报】库存统计查询当前库存总金额失败, shopId:{}, result:{}", shopId, ObjectUtils.objectToJSON(result));
            throw new BizException("查询当前库存总金额失败");
        }
        return result.getData();
    }

    /**
     * 查询配件分类统计
     *
     * @param shopId
     * @param monthStr
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<GoodsCateStatVO> getGoodsCateStat(Long shopId, String monthStr, int page, int pageSize) {
        Result<DefaultResult<GoodsCateStatDTO>> result = rpcWarehouseAnalysisService.getGoodsCateStat(shopId, monthStr, page, pageSize);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("【营业月报】库存统计查询配件分类统计失败, shopId:{}, monthStr:{}, result:{}", shopId, monthStr, ObjectUtils.objectToJSON(result));
            throw new BizException("查询配件分类统计失败");
        }
        return new GoodsCateStatConverter().convert(result.getData());
    }

    /**
     * 查询配件类型总计
     *
     * @param shopId
     * @param monthStr
     * @return
     */
    @Override
    public GoodsCateTotalVO getGoodsCateTotal(Long shopId, String monthStr) {
        Result<GoodsCateTotalDTO> result = rpcWarehouseAnalysisService.getGoodsCateTotal(shopId, monthStr);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("【营业月报】库存统计查询配件类型总计失败, shopId:{}, monthStr:{}, result:{}", shopId, monthStr, ObjectUtils.objectToJSON(result));
            throw new BizException("查询配件类型总计失败");
        }
        return new GoodsCateTotalConverter().convert(result.getData());
    }

    /**
     * 查询配件类型金额TOP10
     *
     * @param shopId
     * @param monthStr
     * @return
     */
    @Override
    public List<GoodsCateAmountVO> getGoodsCateAmount(Long shopId, String monthStr) {
        Result<List<GoodsCateAmountDTO>> result = rpcWarehouseAnalysisService.getGoodsCateAmount(shopId, monthStr);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("【营业月报】库存统计查询配件类型金额TOP10失败, shopId:{}, monthStr:{}, result:{}", shopId, monthStr, ObjectUtils.objectToJSON(result));
            throw new BizException("查询配件类型金额TOP10失败");
        }
        return new GoodsCateAmountListConverter().convert(result.getData());
    }

    /**
     * 查询配件统计排行
     *
     * @param shopId
     * @param monthStr
     * @param goodsName
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<GoodsStatVO> getGoodsStat(Long shopId, String monthStr, String goodsName, int page, int pageSize) {
        Result<DefaultResult<GoodsStatDTO>> result = rpcWarehouseAnalysisService.getGoodsStat(shopId, monthStr, goodsName, page, pageSize);
        if (Objects.isNull(result) || !result.isSuccess()) {
            log.error("【营业月报】库存统计查询配件统计排行失败, shopId:{}, monthStr:{}, goodsName:{}, result:{}", shopId, monthStr, goodsName, ObjectUtils.objectToJSON(result));
            throw new BizException("查询配件统计排行失败");
        }
        return new GoodsStatConverter().convert(result.getData());
    }
}
