package com.tqmall.legend.web.report;

import com.aliyun.oss.model.OSSObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.template.PageVO;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcBusinessMonthlyService;
import com.tqmall.cube.shop.RpcGoodsPurchaseSaleStatistics;
import com.tqmall.cube.shop.param.report.goods.sale.GoodsCatQueryParam;
import com.tqmall.cube.shop.param.report.goods.sale.GoodsCatTopNQueryParam;
import com.tqmall.cube.shop.param.report.goods.sale.GoodsQueryParam;
import com.tqmall.cube.shop.result.businessmonthly.ReportFileDTO;
import com.tqmall.cube.shop.result.report.goods.GoodsPurchaseSaleCategoryDTO;
import com.tqmall.cube.shop.result.report.goods.GoodsPurchaseSaleCategoryTopDTO;
import com.tqmall.cube.shop.result.report.goods.GoodsPurchaseSaleDTO;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.report.BusinessMonthlyFacade;
import com.tqmall.legend.facade.report.vo.*;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.param.GoodsCategoryPurchaseParam;
import com.tqmall.legend.web.report.param.GoodsPurchaseParam;
import com.tqmall.legend.web.vo.report.month.SeriesRankRequest;
import com.tqmall.oss.OSSClientUtil;
import com.tqmall.oss.ObjectKeyUtil;
import com.tqmall.wheel.lang.DatePair;
import com.tqmall.wheel.lang.Dates;
import com.tqmall.wheel.lang.Objects;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tqmall.wheel.lang.Dates.isYMFormat;

/**
 * Created by tanghao on 16/9/1.
 */
@Controller
@RequestMapping("shop/report/business/month")
@Slf4j
public class BusinessMonthController extends BaseController {

    @Autowired
    private BusinessMonthlyFacade businessMonthlyFacade;
    @Autowired
    private RpcBusinessMonthlyService rpcBusinessMonthlyService;
    @Autowired
    private RpcGoodsPurchaseSaleStatistics rpcGoodsPurchaseSaleStatistics;
    @Autowired
    private ShopService shopService;

    @Autowired
    private OSSClientUtil ossClientUtil;
    @Value("${tqmall.oss.bucketName}")
    private String bucketName;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    /**
     * 门店营业月报页面
     *
     * @return
     */
    @HttpRequestLog
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("moduleUrlTab", "report_business_month");
        return "yqx/page/report/statistics/business/month";
    }

    @RequestMapping(value = "overview", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<BusinessOverviewVo> statisticBusinessOverview(@RequestParam(value = "month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }

        BusinessOverviewVo businessOverview = this.businessMonthlyFacade.getBusinessOverview(UserUtils.getShopIdForSession(request), month);
        if (businessOverview == null) {
            return Result.wrapErrorResult("", "获取数据异常.");
        }
        return Result.wrapSuccessfulResult(businessOverview);
    }

    @RequestMapping(value = "surface_num", method = RequestMethod.GET)
    @ResponseBody
    public Result<BigDecimal> getTotalSurfaceNumMonthly(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        try {
            BigDecimal totalSurfaceNum = businessMonthlyFacade.getTotalSurfaceNumMonthly(UserUtils.getShopIdForSession(request), month);
            totalSurfaceNum = totalSurfaceNum == null ? BigDecimal.ZERO : totalSurfaceNum;
            return Result.wrapSuccessfulResult(totalSurfaceNum);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "paidin", method = RequestMethod.GET)
    @ResponseBody
    public Result<PaidInStatisticsVo> statisticBusinessPaidIn(@RequestParam(value = "month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }

        try {
            PaidInStatisticsVo businessPaidIn = this.businessMonthlyFacade.getBusinessPaidIn(UserUtils.getShopIdForSession(request), month);
            if (Objects.isNull(businessPaidIn)) {
                return Result.wrapErrorResult("", "获取经营总览-实收数据失败.");
            } else {
                return Result.wrapSuccessfulResult(businessPaidIn);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "paidout", method = RequestMethod.GET)
    @ResponseBody
    public Result<PaidOutStatisticsVo> statisticBusinessPaidOut(@RequestParam("month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }

        try {
            PaidOutStatisticsVo businessPaidOut = this.businessMonthlyFacade.getBusinessPaidOut(UserUtils.getShopIdForSession(request), month);
            if (Objects.isNull(businessPaidOut)) {
                return Result.wrapErrorResult("", "获取经营总览-实付数据失败.");
            } else {
                return Result.wrapSuccessfulResult(businessPaidOut);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "business_month_trend", method = RequestMethod.GET)
    @ResponseBody
    public Result<BusinessTrendVo> statisticBusinessMonthTrend(@RequestParam("month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }

        try {
            BusinessTrendVo sumOfBusinessByMonth = this.businessMonthlyFacade.getSumOfBusinessByMonth(UserUtils.getShopIdForSession(request), month);
            if (Objects.isNull(sumOfBusinessByMonth)) {
                return Result.wrapErrorResult("", "获取经营总览-营业额月趋势图失败.");
            } else {
                List<DatePointVo> datePointVoList = sumOfBusinessByMonth.getDatePointVoList();
                if (!CollectionUtils.isEmpty(datePointVoList)) {
                    for (DatePointVo datePointVo : datePointVoList) {
                        datePointVo.setDate(datePointVo.getDate().substring(5));
                    }
                }
                return Result.wrapSuccessfulResult(sumOfBusinessByMonth);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "paidin_month_trend", method = RequestMethod.GET)
    @ResponseBody
    public Result<BusinessTrendVo> statisticPaidInMonthTrend(@RequestParam("month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }

        try {
            BusinessTrendVo paidInByMonth = this.businessMonthlyFacade.getPaidInByMonth(UserUtils.getShopIdForSession(request), month);
            if (Objects.isNull(paidInByMonth)) {
                return Result.wrapErrorResult("", "获取经营总览-实收金额月趋势图失败.");
            } else {
                List<DatePointVo> datePointVoList = paidInByMonth.getDatePointVoList();
                if (!CollectionUtils.isEmpty(datePointVoList)) {
                    for (DatePointVo datePointVo : datePointVoList) {
                        datePointVo.setDate(datePointVo.getDate().substring(5));
                    }
                }
                return Result.wrapSuccessfulResult(paidInByMonth);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }


    @RequestMapping("business_year_trend")
    @ResponseBody
    public Result<BusinessTrendVo> statisticBusinessYearTrend(@RequestParam("year") String year) {
        if (!DateFormatUtils.isYFormat(year)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }
        try {
            BusinessTrendVo sumOfBusinessByYear = this.businessMonthlyFacade.getSumOfBusinessByYear(UserUtils.getShopIdForSession(request), year);
            if (Objects.isNull(sumOfBusinessByYear)) {
                return Result.wrapErrorResult("", "获取经营总览-营业额年趋势图失败.");
            } else {
                return Result.wrapSuccessfulResult(sumOfBusinessByYear);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "paidin_year_trend", method = RequestMethod.GET)
    @ResponseBody
    public Result<BusinessTrendVo> statisticPaidInYearTrend(@RequestParam("year") String year) {
        if (!DateFormatUtils.isYFormat(year)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }
        try {
            BusinessTrendVo paidInByYear = this.businessMonthlyFacade.getPaidInByYear(UserUtils.getShopIdForSession(request), year);
            if (Objects.isNull(paidInByYear)) {
                return Result.wrapErrorResult("", "获取经营总览-实收基恩年趋势图失败.");
            } else {
                return Result.wrapSuccessfulResult(paidInByYear);
            }
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }

    }

    @RequestMapping(value = "service_cat_pie")
    @ResponseBody
    @HttpRequestLog
    public Result<List<OrderServiceCatForPie>> statisticServiceCatForPie(@RequestParam("month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }

        try {
            List<OrderServiceCatForPie> orderServiceCatForPie = this.businessMonthlyFacade.getOrderServiceCatForPie(UserUtils.getShopIdForSession(request), month);
            return Result.wrapSuccessfulResult(orderServiceCatForPie);
        } catch (Exception e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "service_cat_rank")
    @ResponseBody
    public Result<Page<OrderServiceCatVo>> statisticServiceCatRank(@RequestParam("month") String monthStr,
                                                                   @PageableDefault(page = 1, value = 6) Pageable pageable) {
        if (!DateFormatUtils.isYMFormat(monthStr)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Page<OrderServiceCatVo> page = null;
        try {
            page = businessMonthlyFacade.getSortedOrderServiceCatByPage(shopId, monthStr, pageable);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }

        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping(value = "service_rank")
    @ResponseBody
    public Result<Page<OrderServiceVo>> statisticServiceRank(@RequestParam("month") String monthStr,
                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                             @PageableDefault(page = 1, value = 10) Pageable pageable) {
        if (!DateFormatUtils.isYMFormat(monthStr)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        Long shopId = UserUtils.getShopIdForSession(request);

        Page<OrderServiceVo> page;
        try {
            page = businessMonthlyFacade.getSortedOrderServiceByPage(shopId, monthStr, keyword, pageable);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
        return Result.wrapSuccessfulResult(page);

    }

    @RequestMapping(value = "spray_service_cat_rank")
    @ResponseBody
    public Result<Page<SprayOrderServiceCateVO>> statisticSprayServiceCatRank(@RequestParam("month") String month,
                                                                              @RequestParam(value = "shared", defaultValue = "true") Boolean shared,
                                                                              @PageableDefault(page = 1, value = 6) Pageable pageable,
                                                                              HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        try {
            Page<SprayOrderServiceCateVO> page = businessMonthlyFacade.getSprayOrderServiceCatePage(UserUtils.getShopIdForSession(request), month, shared, pageable);
            return Result.wrapSuccessfulResult(page);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "spray_sale_amount")
    @ResponseBody
    public Result<BigDecimal> getTotalSpraySaleAmount(@RequestParam("month") String month,
                                                      @RequestParam(value = "shared", defaultValue = "true") Boolean shared,
                                                      HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        try {
            BigDecimal totalSpraySaleAmount = businessMonthlyFacade.getTotalSpraySaleAmount(UserUtils.getShopIdForSession(request), month, shared);
            totalSpraySaleAmount = totalSpraySaleAmount == null ? BigDecimal.ZERO : totalSpraySaleAmount;
            return Result.wrapSuccessfulResult(totalSpraySaleAmount);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        }
    }

    @RequestMapping(value = "car_brand_top10", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<CarBrandRankVo>> statisticCarBrandTop10(@RequestParam("month") String month) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期格式错误.");
        }
        try {
            return Result.wrapSuccessfulResult(this.businessMonthlyFacade.getCarBrandTop10(UserUtils.getShopIdForSession(request), month));
        } catch (Exception e) {
            return Result.wrapErrorResult("", e.getMessage());
        }

    }

    @RequestMapping(value = "visitant", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result statisticMonthlyVisitant(@RequestParam(value = "month") String month) {

        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }

        Long shopID = UserUtils.getShopIdForSession(request);
        VistantStatisticVo vistantStatisticVo = null;
        try {
            vistantStatisticVo = businessMonthlyFacade.statisticMonthlyVisitant(shopID, month);
        } catch (BizException e) {
            log.error("获取月访客统计数据出错,shopId={}, month={}", shopID, month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(vistantStatisticVo);
    }

    @RequestMapping("series_rank")
    @ResponseBody
    public Result statisticSeriesRank(@RequestBody SeriesRankRequest seriesRankRequest, HttpServletRequest request) {
        if (!Objects.isNotNull(seriesRankRequest)) {
            return Result.wrapErrorResult("", "传入参数对象不能为空");
        }
        String month = seriesRankRequest.getMonth();
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        if (seriesRankRequest.getPage() < 1) {
            return Result.wrapErrorResult("", "pageNum不能小于1");
        }
        if (seriesRankRequest.getSize() <= 0) {
            return Result.wrapErrorResult("", "分页的每页size不能小于1");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        Page<SeriesRankVO> seriesRankVODefaultResult = null;
        try {
            seriesRankVODefaultResult = businessMonthlyFacade.statisticSeriesRank(shopID, month, seriesRankRequest.getPage(), seriesRankRequest.getSize());
        } catch (BizException e) {
            log.error("获取月车型排行数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(seriesRankVODefaultResult);
    }

    @RequestMapping("member_list")
    @ResponseBody
    @HttpRequestLog
    public Result memberCardList(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        List<MonthMemberVO> monthMemberVOs = null;
        try {
            monthMemberVOs = businessMonthlyFacade.getCardHandOutList(shopID, month);
        } catch (BizException e) {
            log.error("获取月会员卡发放列表数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(monthMemberVOs);
    }

    @RequestMapping("member_strip")
    @ResponseBody
    public Result memberCardStrip(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        MonthMemberStripVO monthMemberVOs = null;
        try {
            monthMemberVOs = businessMonthlyFacade.getCardStripInfo(shopID, month);
        } catch (BizException e) {
            log.error("获取月会员卡条形图数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(monthMemberVOs);
    }

    @RequestMapping("combo_list")
    @ResponseBody
    public Result comboList(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        List<MonthComboVO> monthComboVOs = null;
        try {
            monthComboVOs = businessMonthlyFacade.getComboHandOutList(shopID, month);
        } catch (BizException e) {
            log.error("获取月计次卡发放列表数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(monthComboVOs);
    }

    @RequestMapping("coupon_strip")
    @ResponseBody
    public Result couponStrip(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        BigDecimal discountAmount = BigDecimal.ZERO;
        try {
            discountAmount = businessMonthlyFacade.getCouponStripInfo(shopID, month);
        } catch (BizException e) {
            log.error("获取月优惠券条形图数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        MonthCouponStripVO monthCouponStripVO = new MonthCouponStripVO();
        monthCouponStripVO.setDiscountAmount(discountAmount);
        return Result.wrapSuccessfulResult(monthCouponStripVO);
    }

    @RequestMapping("combo_strip")
    @ResponseBody
    public Result comboStrip(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        MonthComboStripVO monthComboStripVO = null;
        try {
            monthComboStripVO = businessMonthlyFacade.getComboStripInfo(shopID, month);
        } catch (BizException e) {
            log.error("获取月计次卡条形图数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(monthComboStripVO);
    }

    @RequestMapping("coupon_list")
    @ResponseBody
    public Result couponList(@RequestParam(value = "month") String month, HttpServletRequest request) {
        if (!DateFormatUtils.isYMFormat(month)) {
            return Result.wrapErrorResult("", "日期错误");
        }
        Long shopID = UserUtils.getShopIdForSession(request);
        List<MonthCouponVO> monthCouponVOs = null;
        try {
            monthCouponVOs = businessMonthlyFacade.getCouponHandOutList(shopID, month);
        } catch (BizException e) {
            log.error("获取月优惠券发放列表数据出错,shopId=" + shopID + ", month=" + month, e);
            return Result.wrapErrorResult("", "获取数据出错");
        }
        return Result.wrapSuccessfulResult(monthCouponVOs);
    }

    @RequestMapping(value = "goods_cat_rank", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<PageVO<List<GoodsCatSaleRankVo>, GoodsCatRankParam>> statisticGoodsCatRank(@RequestBody final GoodsCatRankParam param) {
        return new ApiTemplate<PageVO<List<GoodsCatSaleRankVo>, GoodsCatRankParam>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param, "传入参数不能为空");
                checkStringParam(param.getMonth(), "月份输入错误");
                checkStringParam(param.getTag(), "标签输入错误");
            }

            @Override
            protected PageVO<List<GoodsCatSaleRankVo>, GoodsCatRankParam> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                DatePair timeArea = Dates.toMonthOfSETime(param.getMonth());
                Integer pageSize = param.getPageSize();
                Integer pageIndex = param.getPageIndex();
                int offset = pageSize * (pageIndex - 1);
                GoodsCatRankPage goodsCatRankPage = businessMonthlyFacade.statisticGoodsCatRank(shopId, timeArea, param.getTag(), pageSize, offset);
                PageVO<List<GoodsCatSaleRankVo>, GoodsCatRankParam> pageVO = new PageVO<>(goodsCatRankPage.getContent(), param, goodsCatRankPage.getTotalCount());

                return pageVO;
            }
        }.execute();
    }

    @HttpRequestLog
    @RequestMapping(value = "goods_cat_pie_rank", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<GoodsCatSaleTopVo>> statisticGoodsCatPieRank(@RequestParam final String month, @RequestParam final String tag) {
        return new ApiTemplate<List<GoodsCatSaleTopVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkStringParam(month, "月份输入错误");
                checkStringParam(tag, "查询类别标签错误");
            }

            @Override
            protected List<GoodsCatSaleTopVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                DatePair datePair = Dates.toMonthOfSETime(month);

                List<GoodsCatSaleTopVo> content = businessMonthlyFacade.statisticGoodsCatTop(shopId, datePair, tag);

                return content;
            }
        }.execute();
    }

    @RequestMapping("goods_rank")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<PageVO<List<GoodsSaleRankVo>, GoodsRankParam>> statisticGoodsRank(@RequestBody final GoodsRankParam param) {
        return new ApiTemplate<PageVO<List<GoodsSaleRankVo>, GoodsRankParam>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param, "传入参数不能为空");
                checkStringParam(param.getMonth(), "月份输入错误");
            }

            @Override
            protected PageVO<List<GoodsSaleRankVo>, GoodsRankParam> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                DatePair datePair = Dates.toMonthOfSETime(param.getMonth());
                Integer pageSize = param.getPageSize();
                Integer pageIndex = param.getPageIndex();
                int offset = pageSize * (pageIndex - 1);
                GoodsRankPage goodsRankPage = businessMonthlyFacade.statisticGoodsRank(shopId, datePair, param.getMonth(), param.getKeyword(), pageSize, offset);
                PageVO<List<GoodsSaleRankVo>, GoodsRankParam> pageVo = new PageVO(goodsRankPage.getContent(), param, goodsRankPage.getTotalCount());
                return pageVo;
            }
        }.execute();
    }

    private void checkStringParam(String param, String message) {
        if (Strings.isNullOrEmpty(param)) {
            throw new IllegalArgumentException(message);
        }
    }

    @RequestMapping(value = "warehouse_all_inventory_amount", method = RequestMethod.GET)
    @ResponseBody
    public Result getAllInventoryAmount(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        BigDecimal allInventoryAmount = businessMonthlyFacade.getAllInventoryAmount(shopId);
        return Result.wrapSuccessfulResult(allInventoryAmount);
    }

    @RequestMapping(value = "warehouse_goods_cate_stat", method = RequestMethod.GET)
    @ResponseBody
    public Result getGoodsCateStat(HttpServletRequest request,
                                   @RequestParam(value = "month")String month,
                                   @PageableDefault(page = 1, value = 10) Pageable pageable) {
        Result checkResult = checkWarehouseParam(month);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Page<GoodsCateStatVO> page = businessMonthlyFacade.getGoodsCateStat(shopId, month, pageable.getPageNumber(), pageable.getPageSize());
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping(value = "warehouse_goods_cate_total", method = RequestMethod.GET)
    @ResponseBody
    public Result getGoodsCateTotal(HttpServletRequest request, @RequestParam(value = "month")String month) {
        Result checkResult = checkWarehouseParam(month);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        GoodsCateTotalVO goodsCateTotal = businessMonthlyFacade.getGoodsCateTotal(shopId, month);
        return Result.wrapSuccessfulResult(goodsCateTotal);
    }

    @RequestMapping(value = "warehouse_goods_cate_amount", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result getGoodsCateAmount(HttpServletRequest request, @RequestParam(value = "month")String month) {
        Result checkResult = checkWarehouseParam(month);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        List<GoodsCateAmountVO> goodsCateAmountList = businessMonthlyFacade.getGoodsCateAmount(shopId, month);
        return Result.wrapSuccessfulResult(goodsCateAmountList);
    }

    @RequestMapping(value = "warehouse_goods_stat", method = RequestMethod.GET)
    @ResponseBody
    public Result getGoodsStat(HttpServletRequest request,
                               @RequestParam(value = "month")String month,
                               @RequestParam(value = "keyword", required = false) String goodsName,
                               @PageableDefault(page = 1, value = 10) Pageable pageable) {
        Result checkResult = checkWarehouseParam(month);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Page<GoodsStatVO> page = businessMonthlyFacade.getGoodsStat(shopId, month, goodsName, pageable.getPageNumber(), pageable.getPageSize());
        return Result.wrapSuccessfulResult(page);
    }

    private Result checkWarehouseParam(String monthStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = df.parse(monthStr);

            if (date.before(df.parse("2016-10"))) {
                return Result.wrapErrorResult("", "查询日期不能早于2016年10月");
            }
        } catch (ParseException e) {
            return Result.wrapErrorResult("", "日期格式错误");
        }
        return Result.wrapSuccessfulResult(date);
    }

    @RequestMapping(value = "summary")
    @HttpRequestLog
    public String summaryPage(Model model) {
        model.addAttribute("moduleUrlTab", "summary_business_month");
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        if (shop == null) {
            return "common/error";
        }
        String shopName = shop.getName();
        model.addAttribute("shopName", shopName);

        boolean hasReport = true;

        com.tqmall.core.common.entity.Result<ReportFileDTO> rpcResult = rpcBusinessMonthlyService.getLastReportFile(shopId);
        if (rpcResult == null || !rpcResult.isSuccess()) {
            hasReport = false;
            model.addAttribute("hasReport", hasReport);
            return "yqx/page/report/statistics/business/month-summary";
        }
        model.addAttribute("hasReport", hasReport);
        ReportFileDTO reportFileDTO = rpcResult.getData();
        model.addAttribute("reportFile", reportFileDTO);

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        String reportDate = reportFileDTO.getReportDate();
        try {
            date = df.parse(reportDate);
        } catch (ParseException e) {
            Result.wrapErrorResult("", "日期类型错误");
        }
        List<String> dateList = Lists.newArrayList();
        dateList.add(reportDate);
        Date gmtCreate = shop.getGmtCreate();
        Date date2 = DateUtil.addMonth(date, -1);
        if (date2.compareTo(gmtCreate) > 0) {
            dateList.add(df.format(date2));
        }
        Date date3 = DateUtil.addMonth(date, -2);
        if (date3.compareTo(gmtCreate) > 0) {
            dateList.add(df.format(date3));
        }
        model.addAttribute("dateList", dateList);
        return "yqx/page/report/statistics/business/month-summary";
    }

    /**
     * 下载
     *
     * @param monthStr
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "download")
    public void downloadReportFile(@RequestParam(value = "month") final String monthStr, final HttpServletResponse response) throws Exception {

        if (!DateFormatUtils.isYMFormat(monthStr)) {
            return;
        }

        UserInfo user = UserUtils.getUserInfo(request);
        if (log.isInfoEnabled()) {
            log.info("{}[{}]下载了{}-{}经营分析报告.", user.getName(), user.getUserId(), user.getShopId(), monthStr);
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        com.tqmall.core.common.entity.Result<String> result = rpcBusinessMonthlyService.getDownloadReportFile(shopId, monthStr);
        if (result != null && result.isSuccess()) {
            String ossUrl = result.getData();
            String orgObjectKey = ObjectKeyUtil.getObjectKeyByUrl(ossClientUtil.getRegion(), ossUrl);
            OSSObject ossObject = ossClientUtil.downloadObject(bucketName, orgObjectKey);
            try (InputStream inputStream = ossObject.getObjectContent();
                 ServletOutputStream outputStream = response.getOutputStream()) {
                String fileName = Integer.parseInt(monthStr.substring(5)) + "月门店经营分析报告.pdf";
                fileName = URLEncoder.encode(fileName, "UTF-8");
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentType("application/octet-stream");
                IOUtils.copy(inputStream, outputStream);
            }
        }
    }

    /**
     * 预览
     *
     * @param monthStr 格式: yyyy-MM
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "preview")
    @HttpRequestLog(params = {@Param(name = "month")})
    public void previewReportFile(@RequestParam(value = "month") final String monthStr, final HttpServletResponse response) throws IOException {
        if (!DateFormatUtils.isYMFormat(monthStr)) {
            return;
        }
        UserInfo user = UserUtils.getUserInfo(request);
        if (log.isInfoEnabled()) {
            log.info("{}[{}]预览了{}-{}经营分析报告.", user.getName(), user.getUserId(), user.getShopId(), monthStr);
        }
        com.tqmall.core.common.entity.Result<String> result = rpcBusinessMonthlyService.getPreviewReportFile(user.getShopId(), monthStr);
        if (result != null && result.isSuccess()) {
            String ossUrl = result.getData();
            String orgObjectKey = ObjectKeyUtil.getObjectKeyByUrl(ossClientUtil.getRegion(), ossUrl);
            OSSObject ossObject = ossClientUtil.downloadObject(bucketName, orgObjectKey);
            try (InputStream inputStream = ossObject.getObjectContent();
                 ServletOutputStream outputStream = response.getOutputStream()) {
                IOUtils.copy(inputStream, outputStream);
            }
        }
    }

    @RequestMapping(value = "purchase", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleDTO>> statisticGoodsPurchase(@RequestBody final GoodsPurchaseParam webParam) {
        return new ApiTemplate<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(isYMFormat(webParam.getMonth()), "日期格式必须为yyyy-MM");
            }

            @Override
            protected com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsQueryParam param = new GoodsQueryParam();
                param.setShopId(shopId);
                param.setKeyword(webParam.getKeyword());
                param.setMonth(webParam.getMonth());
                param.setPageNum(webParam.getPage());
                param.setPageSize(webParam.getSize());
                RpcResult<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleDTO>> rpcResult = rpcGoodsPurchaseSaleStatistics.statisticByGoods(param);
                if (rpcResult == null || !rpcResult.isSuccess() || rpcResult.getData() == null) {
                    log.error("查询配件采销统计失败,param={},result={}", JSONUtil.object2Json(param), JSONUtil.object2Json(rpcResult));
                    throw new BizException("查询失败");
                }
                com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleDTO> data = rpcResult.getData();
                return data;
            }
        }.execute();
    }

    @RequestMapping(value = "purchase_top", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public com.tqmall.core.common.entity.Result<GoodsPurchaseSaleCategoryTopDTO> statisticGoodsPurchaseTop(@RequestParam(value = "month") final String monthStr) {
        return new ApiTemplate<GoodsPurchaseSaleCategoryTopDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(isYMFormat(monthStr), "日期格式必须为yyyy-MM");
            }

            @Override
            protected GoodsPurchaseSaleCategoryTopDTO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsCatTopNQueryParam param = new GoodsCatTopNQueryParam();
                param.setShopId(shopId);
                param.setTopN(GoodsCatTopNQueryParam.DEFAULT_TOP_N);
                param.setMonth(monthStr);
                RpcResult<GoodsPurchaseSaleCategoryTopDTO> rpcResult = rpcGoodsPurchaseSaleStatistics.statisticByGoodsCatTopN(param);
                if (rpcResult == null || !rpcResult.isSuccess() || rpcResult.getData() == null) {
                    log.error("查询配件采销统计失败,param={},result={}", JSONUtil.object2Json(param), JSONUtil.object2Json(rpcResult));
                    throw new BizException("查询失败");
                }
                GoodsPurchaseSaleCategoryTopDTO data = rpcResult.getData();
                return data;
            }
        }.execute();
    }

    @RequestMapping(value = "category_purchase")
    @ResponseBody
    public com.tqmall.core.common.entity.Result statisticGoodsCategoryPurchase(@RequestBody final GoodsCategoryPurchaseParam webParam) {
        return new ApiTemplate<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleCategoryDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(isYMFormat(webParam.getMonth()), "日期格式必须为yyyy-MM");
            }

            @Override
            protected com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleCategoryDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsCatQueryParam param = new GoodsCatQueryParam();
                param.setShopId(shopId);
                param.setPageNum(webParam.getPage());
                param.setPageSize(webParam.getSize());
                param.setMonth(webParam.getMonth());
                RpcResult<com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleCategoryDTO>> rpcResult = rpcGoodsPurchaseSaleStatistics.statisticByGoodsCat(param);
                if (rpcResult == null || !rpcResult.isSuccess() || rpcResult.getData() == null) {
                    log.error("查询配件采销统计失败,param={},result={}", JSONUtil.object2Json(param), JSONUtil.object2Json(rpcResult));
                    throw new BizException("查询失败");
                }
                com.tqmall.wheel.support.data.Page<GoodsPurchaseSaleCategoryDTO> pageData = rpcResult.getData();
                return pageData;
            }
        }.execute();
    }
}
