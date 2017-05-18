package com.tqmall.legend.web.report;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcOrderInfoDetailService;
import com.tqmall.cube.shop.param.order.OrderServicesReportParam;
import com.tqmall.cube.shop.result.OrderServicesReportDTO;
import com.tqmall.cube.shop.result.RpcOrderServicesVO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.vo.report.order.OrderServiceExcelVo;
import com.tqmall.legend.web.vo.report.order.OrderServicesReportResult;
import com.tqmall.legend.web.vo.report.order.OrderServicesVO;
import com.tqmall.legend.web.vo.report.order.adaptor.OrderServicesReportRequestConverter;
import com.tqmall.legend.web.vo.report.order.adaptor.RpcOrderServicesVOConverter;
import com.tqmall.legend.web.vo.report.order.requestpara.OrderServicesReportRequest;
import com.tqmall.wheel.component.excel.export.DefaultExcelExportor;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuchengdu on 16/7/10.
 */
@Slf4j
@Controller
@RequestMapping("/shop/stats/order/services")
public class OrderServicesReportController extends BaseController {
    @Resource
    private RpcOrderInfoDetailService rpcOrderInfoDetailService;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping("list")
    @ResponseBody
    public Result getOrderServicesReport(@RequestBody OrderServicesReportRequest requestParam, HttpServletRequest request) {
        if (requestParam.getPage() == null) {
            return Result.wrapErrorResult("", "分页参数错误");
        }
        if (requestParam.getPageSize() == null) {
            requestParam.setPageSize(20);
        }
        Result<OrderServicesReportDTO> orderServicesReportDTOResult = getOrderServicesByRpc(requestParam);
        if (orderServicesReportDTOResult == null || orderServicesReportDTOResult.getData() == null) {
            return Result.wrapErrorResult("", "系统异常，请稍后重试");
        }
        OrderServicesReportResult orderServicesReportResult = converterToOrderServicesReportResult(orderServicesReportDTOResult, UserUtils.getShopLevelForSession(request));
        if (orderServicesReportResult.getTotalSize() != null) {
            orderServicesReportResult.setTotalPage((
                    orderServicesReportResult.getTotalSize().intValue()
                            + requestParam.getPageSize().intValue() - 1)
                    / requestParam.getPageSize().intValue());
        }
        orderServicesReportResult.setRequestParam(requestParam);
        return Result.wrapSuccessfulResult(orderServicesReportResult);
    }

    @RequestMapping("orderServiceTotal")
    @ResponseBody
    public Result orderServiceTotal(@RequestBody OrderServicesReportRequest requestParam, HttpServletRequest request) {
        OrderServicesReportParam orderServicesReportParam = new OrderServicesReportParam();
        Long shopId = UserUtils.getShopIdForSession(request);
        new OrderServicesReportRequestConverter().apply(requestParam, orderServicesReportParam);
        orderServicesReportParam.setShopId(shopId);

        Result<TotalReportDataDTO> resultDTO = rpcOrderInfoDetailService.getOrderServicesReportTotalInfo(orderServicesReportParam);
        if (resultDTO == null || resultDTO.getData() == null) {
            return Result.wrapErrorResult("", "系统异常，请稍后重试");
        }
        return Result.wrapSuccessfulResult(resultDTO);
    }

    /**
     * 通过Rpc获取订单服务信息
     *
     * @param requestParam
     * @return
     */
    private Result<OrderServicesReportDTO> getOrderServicesByRpc(OrderServicesReportRequest requestParam) {
        OrderServicesReportParam orderServicesReportParam = new OrderServicesReportParam();
        Long shopId = UserUtils.getShopIdForSession(request);
        new OrderServicesReportRequestConverter().apply(requestParam, orderServicesReportParam);
        orderServicesReportParam.setShopId(shopId);
        Result<OrderServicesReportDTO> orderServicesReportDTOResult = rpcOrderInfoDetailService.getOrderServicesReport(orderServicesReportParam);
        return orderServicesReportDTOResult;
    }

    /**
     * 封装 OrderServicesReportResult结果信息
     *
     * @param orderServicesReportDTOResult
     * @return
     */
    private OrderServicesReportResult converterToOrderServicesReportResult(Result<OrderServicesReportDTO> orderServicesReportDTOResult, final Integer shopLevel) {
        List<OrderServicesVO> orderServicesVOs = new ArrayList<>();
        if (orderServicesReportDTOResult.getData().getRpcOrderServicesVOList() != null) {
            orderServicesVOs = Lists.newArrayList(Lists.transform(orderServicesReportDTOResult.getData().getRpcOrderServicesVOList(), new Function<RpcOrderServicesVO, OrderServicesVO>() {
                @Override
                public OrderServicesVO apply(RpcOrderServicesVO input) {
                    OrderServicesVO orderServicesVO = new OrderServicesVO();
                    new RpcOrderServicesVOConverter().apply(input, orderServicesVO);
                    return orderServicesVO;
                }
            }));
        }
        OrderServicesReportResult orderServicesReportResult = new OrderServicesReportResult();
        orderServicesReportResult.setOrderServicesList(orderServicesVOs);
        BigDecimal discountTotal = new BigDecimal(0);
        if (orderServicesVOs.size() > 0) {
            for (OrderServicesVO orderServicesVO : orderServicesVOs) {
                if (Integer.valueOf(6).equals(shopLevel)) {
                    if ("待报价".equals(orderServicesVO.getOrderStatus())) {
                        orderServicesVO.setOrderStatus("待结算");
                    }
                }
                if (orderServicesVO.getDiscount() != null) {
                    discountTotal = discountTotal.add(orderServicesVO.getDiscount());
                }
            }
        }
        orderServicesReportResult.setDiscountTotal(discountTotal);
        orderServicesReportResult.setTotalSize(orderServicesReportDTOResult.getData().getTotalSize());
        return orderServicesReportResult;
    }

    @RequestMapping("export")
    @ResponseBody
    public void exportToExcel(HttpServletResponse response, OrderServicesReportRequest requestParam, HttpServletRequest request)
            throws Exception {

        UserInfo userInfo = UserUtils.getUserInfo(request);
        DefaultExcelExportor exportor = null;
        long sTime = System.currentTimeMillis();
        try {
            String fileName = "工单服务销售明细-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            /**
             * --------------------------
             * 输出Excel表头
             * --------------------------
             */
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工单服务销售明细";
            exportor.writeTitle(null, headline, OrderServiceExcelVo.class);

            requestParam.setPage(1);
            requestParam.setPageSize(500);
            int recordSize = 0;
            while (true) {
                Result<OrderServicesReportDTO> orderServicesReportDTOResult = getOrderServicesByRpc(requestParam);
                if (Langs.isNotNull(orderServicesReportDTOResult)
                        && orderServicesReportDTOResult.isSuccess()
                        && Langs.isNotNull(orderServicesReportDTOResult.getData())
                        && Langs.isNotEmpty(orderServicesReportDTOResult.getData().getRpcOrderServicesVOList())) {
                    OrderServicesReportResult orderServicesReportResult = converterToOrderServicesReportResult(orderServicesReportDTOResult, UserUtils.getShopLevelForSession(request));
                    List<OrderServiceExcelVo> dataList = BeanMapper.mapListIfPossible(orderServicesReportResult.getOrderServicesList(), OrderServiceExcelVo.class);
                    exportor.write(dataList);
                    recordSize += dataList.size();
                } else {
                    break;
                }
                requestParam.setPage(requestParam.getPage() + 1);
            }
            OrderServicesReportParam orderServicesReportParam = new OrderServicesReportParam();
            Long shopId = UserUtils.getShopIdForSession(request);
            new OrderServicesReportRequestConverter().apply(requestParam, orderServicesReportParam);
            orderServicesReportParam.setShopId(shopId);

            com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcOrderInfoDetailService.getOrderServicesReportTotalInfo(orderServicesReportParam);
            if (reportDataDTOResult.isSuccess()) {
                TotalReportDataDTO zj = reportDataDTOResult.getData();

                Row row = exportor.createRow();
                Cell cell = row.createCell(0);
                cell.setCellValue("总计");
                if (null != zj){
                    Cell cell1 = row.createCell(10);
                    cell1.setCellValue(zj.getCostAmount().doubleValue());
                    Cell cell2 = row.createCell(11);
                    cell2.setCellValue(zj.getPayAmount().doubleValue());
                    Cell cell3 = row.createCell(12);
                    cell3.setCellValue(zj.getDiscountAmount().doubleValue());
                }else {
                    Cell cell1 = row.createCell(10);
                    cell1.setCellValue("0");
                    Cell cell2 = row.createCell(11);
                    cell2.setCellValue("0");
                    Cell cell3 = row.createCell(12);
                    cell3.setCellValue("0");
                }
            }

            long exportTime = System.currentTimeMillis() - sTime;
            log.info(ExportLog.getExportLog("工单服务销售明细", userInfo, recordSize, exportTime));
        } catch (Exception e) {
            log.error("导出Excel异常.", e);
            throw e;
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }
}
