package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.bi.entity.StatisticsWarehouseIn;
import com.tqmall.legend.bi.entity.StatisticsWarehouseOut;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.statistics.param.WarehouseInReportParam;
import com.tqmall.legend.entity.statistics.param.WarehouseOutReportParam;
import com.tqmall.legend.facade.report.WarehouseInFacade;
import com.tqmall.legend.facade.report.WarehouseOutFacade;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.pojo.warehouse.WarehouseInTotalVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.vo.StatisticsWarehouseInCommission;
import com.tqmall.legend.web.report.export.vo.StatisticsWarehouseOutCommission;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/1/28.
 */
@Slf4j
@Controller
@RequestMapping(value = "/shop/stats/warehouse-info")
public class WarehouseReportController extends BaseController{

    @Autowired
    private ShopService shopService;
    @Autowired
    private WarehouseOutFacade warehouseOutFacade;
    @Autowired
    private WarehouseInFacade warehouseInFacade;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    /**
     * 出库明细列表展示list
     */
    @RequestMapping(value = "out/list/list")
    @ResponseBody
    public Result<SimplePage<StatisticsWarehouseOut>> statisticsWarehouseOutList(@RequestBody WarehouseOutReportParam warehouseOutReportParam,
                                                                                 HttpServletRequest request, Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try{
            Assert.notNull(warehouseOutReportParam,"传入参数对象不能为空");
            Assert.hasText(warehouseOutReportParam.getStartTime(),"请选择开始时间");
            Assert.notNull(userInfo,"用户信息错误");
        }catch (Exception e){
            return Result.wrapErrorResult("",e.getMessage());
        }

        if(StringUtils.isEmpty(warehouseOutReportParam.getEndTime())){
            warehouseOutReportParam.setEndTime(DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(),"yyyy-MM-dd")));
        }else {
            warehouseOutReportParam.setEndTime(DateUtil.convertToEndStr(warehouseOutReportParam.getEndTime()));
        }
        warehouseOutReportParam.setStartTime(DateUtil.convertToBeginStr(warehouseOutReportParam.getStartTime()));
        long shopId = userInfo.getShopId();

        warehouseOutReportParam.setShopId(shopId);

        //设置分页size
        SimplePage<StatisticsWarehouseOut> page = warehouseOutFacade.getPage(warehouseOutReportParam);
        page.setTotalPage();
        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 出库总计信息接口
     */
    @RequestMapping(value = "out/getTotalOutInfo")
    @ResponseBody
    public Result getTotalOutInfo(@RequestBody WarehouseOutReportParam warehouseOutReportParam,
                                                                HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try{
            Assert.notNull(warehouseOutReportParam,"传入参数对象不能为空");
            Assert.hasText(warehouseOutReportParam.getStartTime(),"请选择开始时间");
            Assert.notNull(userInfo,"用户信息错误");
        }catch (IllegalArgumentException e){
            return Result.wrapErrorResult("",e.getMessage());
        }
        if(StringUtils.isEmpty(warehouseOutReportParam.getEndTime())){
            warehouseOutReportParam.setEndTime(DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(),"yyyy-MM-dd")));
        }else {
            warehouseOutReportParam.setEndTime(DateUtil.convertToEndStr(warehouseOutReportParam.getEndTime()));
        }
        warehouseOutReportParam.setStartTime(DateUtil.convertToBeginStr(warehouseOutReportParam.getStartTime()));
        long shopId = userInfo.getShopId();

        warehouseOutReportParam.setShopId(shopId);

        StatisticsWarehouseOut statisticsWarehouseOut = warehouseOutFacade.getTotalInfo(warehouseOutReportParam);
        return Result.wrapSuccessfulResult(statisticsWarehouseOut);
    }

    /**
     * 跳转入库明细页面
     */
    @RequestMapping("in")
    public String statisticsWarehouseIn(Model model) {
        model.addAttribute("moduleUrlTab", "stats_warehouse_in");
        return "yqx/page/report/statistics/warehouse/warehouse-in";
    }


    /**
     * 入库明细列表展示
     */
    @RequestMapping(value = "in/list/list")
    @ResponseBody
    public Object statisticsWarehouseInList(HttpServletRequest request, @RequestBody WarehouseInReportParam warehouseInReportParam) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try{
            Assert.notNull(warehouseInReportParam,"传入参数对象不能为空");
            Assert.hasText(warehouseInReportParam.getStartTime(),"请选择开始时间");
            Assert.notNull(userInfo,"用户信息错误");
            Assert.notNull(warehouseInReportParam.getPage(),"页码不能为空");
        }catch (IllegalArgumentException e){
            return Result.wrapErrorResult("",e.getMessage());
        }
        if(StringUtils.isEmpty(warehouseInReportParam.getEndTime())){
            warehouseInReportParam.setEndTime(DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(),"yyyy-MM-dd")));
        }else {
            warehouseInReportParam.setEndTime(DateUtil.convertToEndStr(warehouseInReportParam.getEndTime()));
        }
        warehouseInReportParam.setStartTime(DateUtil.convertToBeginStr(warehouseInReportParam.getStartTime()));
        Long shopId = userInfo.getShopId();
        warehouseInReportParam.setShopId(shopId);
        SimplePage<StatisticsWarehouseIn> page = warehouseInFacade.getPage(warehouseInReportParam);
        //设置分页totalPage
        page.setTotalPage();
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 出库总计信息接口
     */
    @RequestMapping(value = "in/getTotalInInfo")
    @ResponseBody
    public Result getTotalWarehouseInInfo(HttpServletRequest request, @RequestBody WarehouseInReportParam warehouseInReportParam) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try{
            Assert.notNull(warehouseInReportParam,"传入参数对象不能为空");
            Assert.hasText(warehouseInReportParam.getStartTime(),"请选择开始时间");
            Assert.notNull(userInfo,"用户信息错误");
        }catch (IllegalArgumentException e){
            return Result.wrapErrorResult("",e.getMessage());
        }
        Long shopId = userInfo.getShopId();
        warehouseInReportParam.setShopId(shopId);
        if(StringUtils.isEmpty(warehouseInReportParam.getEndTime())){
            warehouseInReportParam.setEndTime(DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(),"yyyy-MM-dd")));
        }else {
            warehouseInReportParam.setEndTime(DateUtil.convertToEndStr(warehouseInReportParam.getEndTime()));
        }
        warehouseInReportParam.setStartTime(DateUtil.convertToBeginStr(warehouseInReportParam.getStartTime()));
        //设置分页size 由于是excel不需要分页,所以会一次性导出所以数据
        WarehouseInTotalVO statisticsWarehouseIn = warehouseInFacade.getTotalInfo(warehouseInReportParam);

        return Result.wrapSuccessfulResult(statisticsWarehouseIn);
    }


    /**
     * 出库明细报表导出
     * @param response
     */
    @RequestMapping(value = "out/export", method = RequestMethod.GET)
    public void exporWarehouseOutExcel(String startTime,String endTime,String goodsName,String goodsFormat,
                                       String warehouseOutSn,String orderSn,String customerName,Long goodsReceiver,
                                       String status,final HttpServletResponse response, @PageableDefault(page = 1, value = 500, sort = "oi.create_time", direction = Sort.Direction.DESC) Pageable pageable
    ) throws Exception {
        final long sTime = System.currentTimeMillis();
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Assert.hasText(startTime,"请选择开始时间");
            Assert.notNull(userInfo,"用户信息错误");
        }catch (IllegalArgumentException e){
            log.error("出库明细报表导出异常",e);
            return;
        }
        final Long shopId = userInfo.getShopId();
        if(StringUtils.isEmpty(endTime)){
            endTime = DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(),"yyyy-MM-dd"));
        }else {
            endTime = DateUtil.convertToEndStr(endTime);
        }
        startTime = DateUtil.convertToBeginStr(startTime);
       final WarehouseOutReportParam warehouseOutReportParam = new WarehouseOutReportParam();
        warehouseOutReportParam.setShopId(shopId);
        warehouseOutReportParam.setCustomerName(customerName);
        warehouseOutReportParam.setEndTime(endTime);
        warehouseOutReportParam.setStatus(status);
        warehouseOutReportParam.setOrderSn(orderSn);
        warehouseOutReportParam.setWarehouseOutSn(warehouseOutSn);
        warehouseOutReportParam.setGoodsFormat(goodsFormat);
        warehouseOutReportParam.setGoodsName(goodsName);
        warehouseOutReportParam.setGoodsReceiver(goodsReceiver);
        warehouseOutReportParam.setStartTime(startTime);
        warehouseOutReportParam.setSize(pageable.getPageSize());
        warehouseOutReportParam.setOffset(0);
        warehouseOutReportParam.setPage(0);


        List<StatisticsWarehouseOut> list = warehouseOutFacade.getWarehouseExcelList(warehouseOutReportParam);

        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "出库明细报表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——出库明细报表";
            exportor.writeTitle(null, headline, StatisticsWarehouseOutCommission.class);
            while(list != null && Langs.isNotEmpty(list)) {
                recordSize += list.size();
                List<StatisticsWarehouseOutCommission> commissionList = converter(list);
                exportor.write(commissionList);
                warehouseOutReportParam.setPage(warehouseOutReportParam.getPage() + 1);
                warehouseOutReportParam.setOffset(warehouseOutReportParam.getPage() * warehouseOutReportParam.getSize());
                list = warehouseOutFacade.getWarehouseExcelList(warehouseOutReportParam);
            }
            StatisticsWarehouseOut zj = warehouseOutFacade.getTotalInfo(warehouseOutReportParam);
            Row row = exportor.createRow();
            Cell cell = row.createCell(0);
            cell.setCellValue("总计");
            if (null != zj){
                Cell cell1 = row.createCell(8);
                cell1.setCellValue(zj.getTotalSalePriceAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                Cell cell2 = row.createCell(10);
                cell2.setCellValue(zj.getTotalInventoryPriceAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                Cell cell3 = row.createCell(11);
                cell3.setCellValue(zj.getTotalCount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }else {
                Cell cell1 = row.createCell(8);
                cell1.setCellValue("0");
                Cell cell2 = row.createCell(10);
                cell2.setCellValue("0");
                Cell cell3 = row.createCell(11);
                cell3.setCellValue("0");
            }

            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("出库单导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private List<StatisticsWarehouseOutCommission> converter(List<StatisticsWarehouseOut> orderInfoDetailVOs){
        List<StatisticsWarehouseOutCommission> resultList = Lists.newArrayList();
        if(null != orderInfoDetailVOs && !CollectionUtils.isEmpty(orderInfoDetailVOs)){
            for(StatisticsWarehouseOut detailVO : orderInfoDetailVOs){
                if(null != detailVO){
                    StatisticsWarehouseOutCommission commission = new StatisticsWarehouseOutCommission();
                    BeanUtils.copyProperties(detailVO , commission);
                    resultList.add(commission);
                }
            }
        }
        return resultList;
    }

    private List<StatisticsWarehouseInCommission> converter2(List<StatisticsWarehouseIn> orderInfoDetailVOs){
        List<StatisticsWarehouseInCommission> resultList = Lists.newArrayList();
        if(null != orderInfoDetailVOs && !CollectionUtils.isEmpty(orderInfoDetailVOs)){
            for(StatisticsWarehouseIn detailVO : orderInfoDetailVOs){
                if(null != detailVO){
                    StatisticsWarehouseInCommission commission = new StatisticsWarehouseInCommission();
                    BeanUtils.copyProperties(detailVO , commission);
                    resultList.add(commission);
                }
            }
        }
        return resultList;
    }

    /**
     * 入库明细报表导出
     * @param response
     */
    @RequestMapping(value = "in/export", method = RequestMethod.GET)
    public void exporWarehouseInExcel( String startTime, String endTime,String goodsName,String goodsFormat,
                                       Long supplierId,Long purchaseAgent,String warehouseInSn,
                                       String status,final HttpServletResponse response) throws Exception {
        final long sTime = System.currentTimeMillis();
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            Assert.hasText(startTime, "请选择开始时间");
            Assert.notNull(userInfo, "用户信息错误");
        } catch (IllegalArgumentException e) {
            log.error("入库明细报表导出异常", e);
            return;
        }
        final Long shopId = userInfo.getShopId();

        if (StringUtils.isEmpty(endTime)) {
            endTime = DateUtil.convertToEndStr(DateUtil.convertDateToStr(new Date(), "yyyy-MM-dd"));
        } else {
            endTime = DateUtil.convertToEndStr(endTime);
        }
        startTime = DateUtil.convertToBeginStr(startTime);
        final WarehouseInReportParam warehouseInReportParam = new WarehouseInReportParam();
        warehouseInReportParam.setShopId(shopId);
        warehouseInReportParam.setPurchaseAgent(purchaseAgent);
        warehouseInReportParam.setEndTime(endTime);
        warehouseInReportParam.setStatus(status);
        warehouseInReportParam.setWarehouseInSn(warehouseInSn);
        warehouseInReportParam.setGoodsFormat(goodsFormat);
        warehouseInReportParam.setGoodsName(goodsName);
        warehouseInReportParam.setSupplierId(supplierId);
        warehouseInReportParam.setStartTime(startTime);
        warehouseInReportParam.setSize(500);
        warehouseInReportParam.setOffset(0);
        warehouseInReportParam.setPage(0);
        List<StatisticsWarehouseIn> list = warehouseInFacade.getWarehouseExcelList(warehouseInReportParam);

        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "入库明细报表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——入库明细报表";
            exportor.writeTitle(null, headline, StatisticsWarehouseInCommission.class);
            while (list != null && Langs.isNotEmpty(list)) {
                recordSize += list.size();
                List<StatisticsWarehouseInCommission> commissionList = converter2(list);
                exportor.write(commissionList);
                warehouseInReportParam.setPage(warehouseInReportParam.getPage() + 1);
                warehouseInReportParam.setOffset(warehouseInReportParam.getPage() * warehouseInReportParam.getSize());
                list = warehouseInFacade.getWarehouseExcelList(warehouseInReportParam);
            }
            exportor.createRow();
            WarehouseInTotalVO zj = warehouseInFacade.getTotalInfo(warehouseInReportParam);
            Row row = exportor.createRow();
            Cell cell0 = row.createCell(0);
            cell0.setCellValue("合计: " + zj.getTotalAmount());
            exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), 0, 1);

            Cell cell3 = row.createCell(2);
            cell3.setCellValue("成本金额: " + zj.getTotalPurchase());
            exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), 2, 3);

            Cell cell6 = row.createCell(4);
            cell6.setCellValue("税费: " + zj.getTotalTax().toString());
            exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), 4, 5);

            Cell cell9 = row.createCell(6);
            cell9.setCellValue("运费: " + zj.getTotalFreight());
            exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), 6, 7);

            Cell cell12 = row.createCell(8);
            cell12.setCellValue("入库数量总计: " + zj.getTotalCount());
            exportor.mergeCell(0, row.getRowNum(), row.getRowNum(), 8, 9);

            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("入库单导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }
}

