package com.tqmall.legend.facade.report;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.report.vo.*;
import com.tqmall.wheel.lang.DatePair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by majian on 16/9/1.
 * 门店营业月报表
 */
public interface BusinessMonthlyFacade {

    /**
     * 营业月报-经营总览汇总
     * @param shopId
     * @param month
     * @return
     * @throws BizException
     */
    BusinessOverviewVo getBusinessOverview(Long shopId, String month) throws BizException;

    /**
     * 经营总览--面漆总数
     * @param shopId
     * @param month
     * @return
     */
    BigDecimal getTotalSurfaceNumMonthly(Long shopId, String month) throws BizException;

    /**
     * 经营总览--实收
     * @param shopId
     * @param month
     * @return
     */
    PaidInStatisticsVo getBusinessPaidIn(Long shopId, String month) throws BizException;

    /**
     * 经营总览--实付
     * @param shopId
     * @param month
     * @return
     * @throws BizException
     */
    PaidOutStatisticsVo getBusinessPaidOut(Long shopId, String month) throws BizException;

    /**
     * 营业月趋势-营业额
     * @param shopId
     * @param month
     * @return
     * @throws BizException
     */
    BusinessTrendVo getSumOfBusinessByMonth(Long shopId, String month) throws BizException;

    /**
     * 营业月趋势-实收
     * @param shopId
     * @param month
     * @return
     */
    BusinessTrendVo getPaidInByMonth(Long shopId, String month) throws BizException;

    /**
     * 营业年趋势-营业额
     * @param shopId
     * @param year
     * @return
     * @throws BizException
     */
    BusinessTrendVo getSumOfBusinessByYear(Long shopId, String year) throws BizException;

    /**
     * 营业年趋势-实收金额
     * @param shopId
     * @param year
     * @return
     * @throws BizException
     */
    BusinessTrendVo getPaidInByYear(Long shopId, String year) throws BizException;
    /**
     * 分页获取服务类型统计
     * @param shopId
     * @param month
     * @param page
     * @return
     */
    Page<OrderServiceCatVo> getSortedOrderServiceCatByPage(Long shopId, String month, Pageable page);

    /**
     * 分页获取服务销量统计
     * @param shopId
     * @param month
     * @param key
     * @param page
     * @return
     */
    Page<OrderServiceVo> getSortedOrderServiceByPage(Long shopId, String month, String key, Pageable page) throws BizException;

    /**
     * 服务类别分类饼状图
     * @param shopId
     * @param month
     * @return
     */
    List<OrderServiceCatForPie> getOrderServiceCatForPie(Long shopId, String month);

    /**
     * 查询钣喷服务分类
     * @param shopId
     * @param month
     * @param shared
     * @param pageable
     * @return
     */
    Page<SprayOrderServiceCateVO> getSprayOrderServiceCatePage(Long shopId, String month, boolean shared, Pageable pageable);

    /**
     * 查询钣喷服务销售额总计
     * @param shopId
     * @param month
     * @param shared
     * @return
     */
    BigDecimal getTotalSpraySaleAmount(Long shopId, String month, boolean shared);

    /**
     * 品牌车型统计-品牌Top10
     * @param shopId
     * @param month
     * @return
     */
    List<CarBrandRankVo> getCarBrandTop10(Long shopId, String month);

    /**
     * 访客统计(按月)
     * @param shopId
     * @param month 格式为2016-12
     * @return
     */
    VistantStatisticVo statisticMonthlyVisitant(Long shopId, String month) throws BizException;

    /**
     * 车型排行
     * @param shopId
     * @param monthStr
     * @param pageIndex from 1
     * @param pageSize
     * @return
     */
    Page<SeriesRankVO> statisticSeriesRank(Long shopId, String monthStr, Integer pageIndex, Integer pageSize);

    /**
     * 获取会员卡发放记录列表
      * @param shopId
     * @param month
     * @return
     */
    List<MonthMemberVO> getCardHandOutList(Long shopId,String month)  throws BizException;

    /**
     * 获取月报会员卡条形图信息
     * @param shopId
     * @param month
     * @return
     */
    MonthMemberStripVO getCardStripInfo(Long shopId,String month)  throws BizException;

    /**
     * 获取月计次卡发放列表
     * @param shopId
     * @param month
     * @return
     */
    List<MonthComboVO> getComboHandOutList(Long shopId,String month)  throws BizException;

    /**
     * 获取优惠券条形图接口
     * @param shopId
     * @param month
     * @return
     * @throws BizException
     */
    BigDecimal getCouponStripInfo(Long shopId,String month)  throws BizException;

    /**
     * 获取计次卡条形图接口
     * @param shopId
     * @param month
     * @return
     */
    MonthComboStripVO getComboStripInfo(Long shopId,String month)  throws BizException;

    /**
     * 获取月优惠券发放记录接口
     * @param shopId
     * @param month
     * @return
     */
    List<MonthCouponVO> getCouponHandOutList(Long shopId,String month)  throws BizException;


    /**
     * 配件类型销量排行
     * @param shopId
     * @param timeArea
     * @param tag
     * @param limit
     * @param offset
     * @return
     */
    GoodsCatRankPage statisticGoodsCatRank(Long shopId, DatePair timeArea, String tag, Integer limit, Integer offset);

    /**
     * 配件类型销量top
     * @param shopId
     * @param datePair
     * @param tag
     * @return
     */
    List<GoodsCatSaleTopVo> statisticGoodsCatTop(Long shopId, DatePair datePair, String tag);

    /**
     * 配件销量排行
     * @param shopId
     * @param datePair
     * @param month
     * @param keyword
     * @param limit
     * @param offset
     * @return
     */
    GoodsRankPage statisticGoodsRank(Long shopId, DatePair datePair, String month, String keyword, Integer limit, Integer offset);

    /**
     * 查询当前库存总金额
     * @param shopId
     * @return
     */
    BigDecimal getAllInventoryAmount(Long shopId);

    /**
     * 查询配件分类统计
     * @param shopId
     * @param monthStr
     * @param page
     * @param pageSize
     * @return
     */
    Page<GoodsCateStatVO> getGoodsCateStat(Long shopId, String monthStr, int page, int pageSize);

    /**
     * 查询配件类型总计
     * @param shopId
     * @param monthStr
     * @return
     */
    GoodsCateTotalVO getGoodsCateTotal(Long shopId, String monthStr);

    /**
     * 查询配件类型金额TOP10
     * @param shopId
     * @param monthStr
     * @return
     */
    List<GoodsCateAmountVO> getGoodsCateAmount(Long shopId, String monthStr);

    /**
     * 查询配件统计排行
     * @param shopId
     * @param monthStr
     * @param goodsName
     * @param page
     * @param pageSize
     * @return
     */
    Page<GoodsStatVO> getGoodsStat(Long shopId, String monthStr, String goodsName, int page, int pageSize);
}
