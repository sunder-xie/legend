package com.tqmall.legend.web.report;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcOrderInfoDetailService;
import com.tqmall.cube.shop.param.order.OrderGoodsReportParam;
import com.tqmall.cube.shop.result.OrderGoodsReportDTO;
import com.tqmall.cube.shop.result.RpcOrderGoodsVO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.vo.report.order.OrderGoodsExcelVo;
import com.tqmall.legend.web.vo.report.order.OrderGoodsReportResult;
import com.tqmall.legend.web.vo.report.order.OrderGoodsVO;
import com.tqmall.legend.web.vo.report.order.adaptor.OrderGoodsReportRequestConverter;
import com.tqmall.legend.web.vo.report.order.adaptor.RpcOrderGoodsVOConverter;
import com.tqmall.legend.web.vo.report.order.requestpara.OrderGoodsReportRequest;
import com.tqmall.wheel.component.excel.export.DefaultExcelExportor;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/10.
 */
@Controller
@RequestMapping("/shop/stats/order/goods")
@Slf4j
public class OrderGoodsReportController extends BaseController {
    @Resource
    private RpcOrderInfoDetailService rpcOrderInfoDetailService;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping
    public String index(Model model,
                        @RequestParam(value = "orderSettleStartTime", required = false) String orderSettleStartTime,
                        @RequestParam(value = "orderSettleEndTime", required = false) String orderSettleEndTime) {
        model.addAttribute("moduleUrlTab", "order-sale-detail");

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(orderSettleStartTime) && StringUtils.isNotEmpty(orderSettleEndTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = df.parse(orderSettleStartTime);
                Date endDate = df.parse(orderSettleEndTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "yqx/page/report/statistics/order/sale-detail";
                }

                model.addAttribute("orderSettleStartDate", orderSettleStartTime);
                model.addAttribute("orderSettleEndDate", orderSettleEndTime);
            } catch (ParseException e) {
                log.error("营业报表跳转配件销售明细报表,日期格式错误,参数:orderSettleStartTime={}, orderSettleEndTime={}, 异常信息:", orderSettleStartTime, orderSettleEndTime, e);
                return "yqx/page/report/statistics/order/sale-detail";
            }
        }
        return "yqx/page/report/statistics/order/sale-detail";
    }

    @RequestMapping("orderGoodsTotal")
    @ResponseBody
    public Result orderGoodsTotal(@RequestBody OrderGoodsReportRequest requestParam, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        OrderGoodsReportParam orderGoodsReportParam = new OrderGoodsReportParam();
        new OrderGoodsReportRequestConverter().apply(requestParam, orderGoodsReportParam);
        orderGoodsReportParam.setShopId(userInfo.getShopId());
        Result<TotalReportDataDTO> result = null;
        try{
            result = rpcOrderInfoDetailService.getOrderGoodsSaleReportTotalInfo(orderGoodsReportParam);
        }catch (Exception e){
            log.error("调用cube获取工单物料销售总计接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        TotalReportDataDTO totalReportDataDTO = result.getData();
        return Result.wrapSuccessfulResult(totalReportDataDTO);
    }

    @RequestMapping("list")
    @ResponseBody
    public Result getOrderGoodsReport(@RequestBody OrderGoodsReportRequest requestParam, HttpServletRequest request) {
        if (requestParam.getPage() == null) {
            return Result.wrapErrorResult("", "分页参数错误");
        }
        if (requestParam.getPageSize() == null) {
            requestParam.setPageSize(20);
        }
        Result<OrderGoodsReportDTO> orderGoodsReportDTOResult = getOrderGoodsByRpc(requestParam);
        if (orderGoodsReportDTOResult == null || orderGoodsReportDTOResult.getData() == null) {
            return Result.wrapErrorResult("", "系统异常，请稍后重试");
        }
        //log.info("从dubbo接口获取的订单配件销售明细如下：", JSONUtil.object2Json(orderGoodsReportDTOResult.getData().getRpcOrderGoodsVOList()));
        OrderGoodsReportResult orderGoodsReportResult = converterToOrderGoodsReportResult(orderGoodsReportDTOResult, UserUtils.getShopLevelForSession(request));
        orderGoodsReportResult.setRequestParam(requestParam);
        if (orderGoodsReportResult.getTotalSize() != null) {
            orderGoodsReportResult.setTotalPage((
                    orderGoodsReportResult.getTotalSize().intValue()
                            + requestParam.getPageSize().intValue() - 1)
                    / requestParam.getPageSize().intValue());
        }
        return Result.wrapSuccessfulResult(orderGoodsReportResult);
    }

    /**
     * 通过Rpc获取工单配件数据
     *
     * @param requestParam
     * @return
     */
    private Result<OrderGoodsReportDTO> getOrderGoodsByRpc(OrderGoodsReportRequest requestParam) {
        OrderGoodsReportParam orderGoodsReportParam = new OrderGoodsReportParam();
        Long shopId = UserUtils.getShopIdForSession(request);
        new OrderGoodsReportRequestConverter().apply(requestParam, orderGoodsReportParam);
        orderGoodsReportParam.setShopId(shopId);
        Result<OrderGoodsReportDTO> orderGoodsReportDTOResult = rpcOrderInfoDetailService.getOrderGoodsReport(orderGoodsReportParam);
        return orderGoodsReportDTOResult;
    }

    /**
     * 封装OrderGoodsReportResult数据
     *
     * @param orderGoodsReportDTOResult
     * @return
     */
    private OrderGoodsReportResult converterToOrderGoodsReportResult(Result<OrderGoodsReportDTO> orderGoodsReportDTOResult, Integer shopLevel) {
        List<OrderGoodsVO> orderGoodsVOs = new ArrayList<>();
        if (orderGoodsReportDTOResult.getData().getRpcOrderGoodsVOList() != null) {
            orderGoodsVOs = Lists.newArrayList(Lists.transform(orderGoodsReportDTOResult.getData().getRpcOrderGoodsVOList(),
                    new Function<RpcOrderGoodsVO, OrderGoodsVO>() {
                        @Override
                        public OrderGoodsVO apply(RpcOrderGoodsVO input) {
                            OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
                            new RpcOrderGoodsVOConverter().apply(input, orderGoodsVO);
                            return orderGoodsVO;
                        }
                    }));
        }
        OrderGoodsReportResult orderGoodsReportResult = new OrderGoodsReportResult();
        orderGoodsReportResult.setOrderGoodsList(orderGoodsVOs);
        orderGoodsReportResult.setTotalSize(orderGoodsReportDTOResult.getData().getTotalSize());
        BigDecimal goodsNumberTotal = BigDecimal.ZERO;//配件数量
        BigDecimal inventoryTotal = new BigDecimal(0);//总成本价
        if (orderGoodsVOs.size() > 0) {
            for (OrderGoodsVO orderGoodsVO : orderGoodsVOs) {
                if (Integer.valueOf(6).equals(shopLevel)) {
                    if ("待报价".equals(orderGoodsVO.getOrderStatus())) {
                        orderGoodsVO.setOrderStatus("待结算");
                    }
                }
                if (orderGoodsVO.getGoodsNumber() != null) {
                    goodsNumberTotal = goodsNumberTotal.add(orderGoodsVO.getGoodsNumber());
                }
                if (orderGoodsVO.getInventoryPrice() != null) {
                    inventoryTotal = inventoryTotal.add(orderGoodsVO.getInventoryPrice());
                }
            }
        }
        orderGoodsReportResult.setGoodsNumberTotal(goodsNumberTotal);
        orderGoodsReportResult.setInventoryTotal(inventoryTotal);
        return orderGoodsReportResult;
    }

    /**
     * 导出数据值Excel
     *
     * @param response
     * @param requestParam
     * @throws Exception
     */
    @RequestMapping("export")
    @ResponseBody
    public void exportToExcel(HttpServletResponse response, OrderGoodsReportRequest requestParam, HttpServletRequest request) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        DefaultExcelExportor exportor = null;
        long sTime = System.currentTimeMillis();
        Long shopId = userInfo.getShopId();
        try {
            String fileName = "工单配件销售明细-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            requestParam.setPage(1);
            requestParam.setPageSize(500);
            /**
             * --------------------------
             * 输出Excel表头
             * --------------------------
             */
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工单配件销售明细";
            exportor.writeTitle(null, headline, OrderGoodsExcelVo.class);

            /**
             * 循环输出直到数据完结
             */
            int recordSize = 0;
            while (true) {
                Result<OrderGoodsReportDTO> orderGoodsReportDTOResult = getOrderGoodsByRpc(requestParam);
                if (Langs.isNotNull(orderGoodsReportDTOResult)
                        && orderGoodsReportDTOResult.isSuccess()
                        && Langs.isNotNull(orderGoodsReportDTOResult.getData())
                        && Langs.isNotEmpty(orderGoodsReportDTOResult.getData().getRpcOrderGoodsVOList())) {
                    OrderGoodsReportResult orderGoodsReportResult = converterToOrderGoodsReportResult(orderGoodsReportDTOResult, UserUtils.getShopLevelForSession(request));
                    List<OrderGoodsExcelVo> orderGoodsExcelVo = BeanMapper.mapListIfPossible(orderGoodsReportResult.getOrderGoodsList(), OrderGoodsExcelVo.class);
                    exportor.write(orderGoodsExcelVo);
                    requestParam.setPage(requestParam.getPage() + 1);
                    recordSize += orderGoodsExcelVo.size();
                } else {
                    break;
                }
            }
            OrderGoodsReportParam orderGoodsReportParam = new OrderGoodsReportParam();
            new OrderGoodsReportRequestConverter().apply(requestParam, orderGoodsReportParam);
            orderGoodsReportParam.setShopId(shopId);
            com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcOrderInfoDetailService.getOrderGoodsSaleReportTotalInfo(orderGoodsReportParam);
            if (reportDataDTOResult.isSuccess()) {
                TotalReportDataDTO zj = reportDataDTOResult.getData();

                Row row = exportor.createRow();
                Cell cell = row.createCell(0);
                cell.setCellValue("总计");
                if (null != zj){
                    Cell cell1 = row.createCell(13);
                    cell1.setCellValue(zj.getGoodsAmount().doubleValue());
                    Cell cell2 = row.createCell(14);
                    cell2.setCellValue(zj.getPayAmount().doubleValue());
                    Cell cell3 = row.createCell(15);
                    cell3.setCellValue(zj.getGrossAmount().doubleValue());
                }else {
                    Cell cell1 = row.createCell(13);
                    cell1.setCellValue("0");
                    Cell cell2 = row.createCell(14);
                    cell2.setCellValue("0");
                    Cell cell3 = row.createCell(15);
                    cell3.setCellValue("0");
                }
            }
            long exportTime = System.currentTimeMillis() - sTime;
            log.info(ExportLog.getExportLog("工单配件销售明细", userInfo, recordSize, exportTime));
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }
}
