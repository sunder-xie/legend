package com.tqmall.legend.web.report;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.param.report.RepairPrefParam;
import com.tqmall.cube.shop.result.shop.*;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.report.GatherStaffPerfFacade;
import com.tqmall.legend.facade.report.StaffPerfFacade;
import com.tqmall.legend.facade.report.bo.*;
import com.tqmall.legend.facade.report.convert.RepairPrefGroupByEmpConverter;
import com.tqmall.legend.facade.report.convert.RepairPrefGroupByOrderConverter;
import com.tqmall.legend.facade.report.convert.RepairPrefGroupByServiceConverter;
import com.tqmall.legend.facade.report.convert.SalePrefGroupByEmpConverter;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.StaffPerfConfigUtil;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 辉辉大侠 on 09/10/2016.
 */
@Controller
@RequestMapping("shop/report/staff/perf")
@Slf4j
public class StaffPerfController extends BaseController {

    @Autowired
    private StaffPerfFacade staffPerfFacade;
    @Autowired
    private GatherStaffPerfFacade gatherStaffPerfFacade;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping(method = RequestMethod.GET)
    @HttpRequestLog
    public String index(Model model) {
        model.addAttribute("moduleUrlTab", "report_staff_perf");
        return "yqx/page/report/statistics/staff/perf";
    }

    /**
     * 检测门店是否配置了默认提成比例
     * @return
     */
    @RequestMapping(value = "check_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> checkConfig() {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                boolean flag = staffPerfFacade.checkConfig(shopId);
                return flag;
            }
        }.execute();
    }

    /**
     * 获取门店维修提成配置信息
     * @return
     */
    @RequestMapping(value = "get_repair_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<EmpPrefConfig>> getRepairConfig() {
        return new ApiTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.getRepairPerformanceConfig(shopId);
            }
        }.execute();
    }

    /**
     * 获取门店销售提成配置信息
     * @return
     */
    @RequestMapping(value = "get_sale_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<EmpPrefConfig>> getSaleConfig() {
        return new ApiTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.getSalePerformanceConfig(shopId);
            }
        }.execute();
    }

    /**
     * 获取门店销售提成配置信息
     * @return
     */
    @RequestMapping(value = "get_sa_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<BigDecimal> getSaConfig() {
        return new ApiTemplate<BigDecimal>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected BigDecimal process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.getSaPerformanceConfig(shopId);
            }
        }.execute();
    }

    /**
     * 获取门店查询月份所有提成配置信息
     * @return
     */
    @RequestMapping(value = "get_all_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<EmpPrefConfig>> getAllConfig(final String month) {
        return new ApiTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(month,"查询月份不能为空.");
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.getAllPerformanceConfigByMonth(shopId,month);
            }
        }.execute();
    }



    /**
     * 提交提成配置信息
     * @return
     */
    @RequestMapping(value = "submit_config", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> submitConfig(@RequestBody final List<EmployeePerformanceConfigVO> performanceConfigVOs) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if(CollectionUtils.isEmpty(performanceConfigVOs)){
                    throw new IllegalArgumentException("设置不能为空");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.submitPerformanceConfig(shopId,performanceConfigVOs);
            }
        }.execute();
    }

    /**
     * 查询汇总表绩效信息
     * @return
     */
    @RequestMapping(value = "collect", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<EmpPerformanceInfoVO>> collect(@RequestParam final String dateStr) {
        return new ApiTemplate<List<EmpPerformanceInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr,"日期不能为空");
            }

            @Override
            protected List<EmpPerformanceInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.queryTotalPercentageInfo(shopId,dateStr);
            }
        }.execute();
    }

    /**
     * 查询维修表汇总信息
     * @return
     */
    @RequestMapping(value = "repair_collect", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<EmpRepairInfoVO>> repairCollect(@RequestParam final String dateStr) {
        return new ApiTemplate<List<EmpRepairInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr,"日期不能为空");
            }

            @Override
            protected List<EmpRepairInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return  staffPerfFacade.queryTotalRepairPref(shopId,dateStr);
            }
        }.execute();
    }

    /**
     * 查询维修表信息(服务维度)
     * @return
     */
    @RequestMapping(value = "repair_service", method = RequestMethod.POST)
    @ResponseBody
    @HttpRequestLog
    public Result<SimplePage<EmpRepairInfoVO>> repairService(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<EmpRepairInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam,"传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(),"日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<EmpRepairInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return staffPerfFacade.queryRepairInfoGroupByService(repairPrefParam);
            }
        }.execute();
    }

    /**
     * 查询维修表信息(工单维度)
     * @return
     */
    @RequestMapping(value = "repair_order", method = RequestMethod.POST)
    @ResponseBody
    @HttpRequestLog
    public Result<SimplePage<EmpRepairInfoVO>> repairOrder(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<EmpRepairInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam,"传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(),"日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<EmpRepairInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return staffPerfFacade.queryRepairInfoGroupByOrder(repairPrefParam);
            }
        }.execute();
    }


    /**
     * 查询销售汇总信息
     * @return
     */
    @RequestMapping(value = "sale_collect", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<EmpSaleInfoVO>> saleCollect(@RequestParam final String dateStr) {
        return new ApiTemplate<List<EmpSaleInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr,"日期不能为空");
            }

            @Override
            protected List<EmpSaleInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.queryTotalSaleInfo(shopId,dateStr);
            }
        }.execute();
    }

    /**
     * 查询销售物料信息(物料维度)
     * @return
     */
    @RequestMapping(value = "sale_goods", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<EmpSaleInfoVO>> saleGoods(@RequestParam final String dateStr, @RequestParam final Long empId) {
        return new ApiTemplate<List<EmpSaleInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr,"日期不能为空");
                Assert.notNull(empId,"员工id不能为空");
            }

            @Override
            protected List<EmpSaleInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
               return staffPerfFacade.querySaleInfoGroupByService(shopId,dateStr,empId);
            }
        }.execute();
    }


    /**
     * 查询销售物料信息(工单维度)
     * @return
     */
    @RequestMapping(value = "sale_order", method = RequestMethod.POST)
    @ResponseBody
    @HttpRequestLog
    public Result<SimplePage<EmpSaleInfoVO>> saleOrder(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<EmpSaleInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam,"传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(),"日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<EmpSaleInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return staffPerfFacade.querySaleInfoGroupByOrder(repairPrefParam);
            }
        }.execute();
    }

    /**
     * 查询服务顾问信息(工单维度)
     * @return
     */
    @RequestMapping(value = "sa_order", method = RequestMethod.POST)
    @ResponseBody
    @HttpRequestLog
    public Result<SimplePage<EmpSAInfoVO>> saOrder(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<EmpSAInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam,"传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(),"日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<EmpSAInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return staffPerfFacade.querySAInfoGroupByOrder(repairPrefParam);
            }
        }.execute();
    }

    /**
     * 查询销售汇总信息
     * @return
     */
    @RequestMapping(value = "sa_collect", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<EmpSAInfoVO>> saCollect(@RequestParam final String dateStr) {
        return new ApiTemplate<List<EmpSAInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr,"日期不能为空");
            }

            @Override
            protected List<EmpSAInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return staffPerfFacade.querySATotalInfo(shopId,dateStr);
            }
        }.execute();
    }

    /**
     * 维修绩效统计excel导出(员工维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "repair_collect/export", method = RequestMethod.GET)
    public void exporRepairCollecttExcel(@RequestParam final String dateStr, HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Long sTime = System.currentTimeMillis();
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        List<EmpRepairInfoVO> data = staffPerfFacade.queryTotalRepairPref(shopId,dateStr);

        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "维修业绩统计-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——维修业绩统计";
            exportor.writeTitle(null, headline, RepairPrefByPersonBO.class);
            recordSize += data.size();
            // 实体转换
            final RepairPrefGroupByEmpConverter converter = new RepairPrefGroupByEmpConverter();
            List<RepairPrefByPersonBO> repairPrefByPersonBOs = Lists.transform(data, new Function<EmpRepairInfoVO, RepairPrefByPersonBO>() {
                @Override
                public RepairPrefByPersonBO apply(EmpRepairInfoVO empRepairInfoVO) {
                    RepairPrefByPersonBO repairPrefByPersonBO =converter.convert(empRepairInfoVO);
                    return repairPrefByPersonBO;
                }
            });
            exportor.write(repairPrefByPersonBOs);
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("维修业绩统计导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }


    /**
     * 维修绩效统计excel导出(维修服务维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "repair_service/export", method = RequestMethod.GET)
    public void exporRepairServiceExcel(@RequestParam final String dateStr,@RequestParam final Long empId, HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        Long sTime = System.currentTimeMillis();

        RepairPrefParam param = new RepairPrefParam();
        param.setEmpId(empId);
        param.setDateStr(dateStr);
        param.setShopId(shopId);
        param.setPage(1);
        param.setSize(500);
        SimplePage<EmpRepairInfoVO> data = staffPerfFacade.queryRepairInfoGroupByService(param);

        List<EmpRepairInfoVO> empRepairInfoVOs = data.getContent();
        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "员工服务项目统计-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工服务项目统计";
            exportor.writeTitle(null, headline, RepairPrefByServiceBO.class);
            while(empRepairInfoVOs != null && Langs.isNotEmpty(empRepairInfoVOs)) {
                recordSize += empRepairInfoVOs.size();
                // 实体转换
                final RepairPrefGroupByServiceConverter converter = new RepairPrefGroupByServiceConverter();
                List<RepairPrefByServiceBO> list = Lists.newArrayList();
                for(EmpRepairInfoVO empRepairInfoVO : data.getContent()){
                    RepairPrefByServiceBO repairPrefByServiceBO = converter.convert(empRepairInfoVO);
                    list.add(repairPrefByServiceBO);
                }
                exportor.write(list);
                param.setPage(param.getPage()+1);
                data = staffPerfFacade.queryRepairInfoGroupByService(param);
                empRepairInfoVOs = data.getContent();
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("员工服务项目统计导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }

    /**
     * 维修绩效统计excel导出(工单维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "repair_order/export", method = RequestMethod.GET)
    public void exporRepairOrderExcel(@RequestParam final String dateStr,@RequestParam final Long empId, HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();

        Long sTime = System.currentTimeMillis();

        RepairPrefParam param = new RepairPrefParam();
        param.setEmpId(empId);
        param.setDateStr(dateStr);
        param.setShopId(shopId);
        param.setPage(1);
        param.setSize(500);
        SimplePage<EmpRepairInfoVO> data = staffPerfFacade.queryRepairInfoGroupByOrder(param);

        List<EmpRepairInfoVO> empRepairInfoVOs = data.getContent();
        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "员工服务项目明细-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工服务项目明细";
            exportor.writeTitle(null, headline, RepairPrefByOrderBO.class);
            while(empRepairInfoVOs != null && Langs.isNotEmpty(empRepairInfoVOs)) {
                recordSize += empRepairInfoVOs.size();
                // 实体转换
                final RepairPrefGroupByOrderConverter converter = new RepairPrefGroupByOrderConverter();
                List<RepairPrefByOrderBO> list = Lists.newArrayList();
                for(EmpRepairInfoVO empRepairInfoVO : data.getContent()){
                    RepairPrefByOrderBO repairPrefByOrderBO = converter.convert(empRepairInfoVO);
                    list.add(repairPrefByOrderBO);
                }
                exportor.write(list);
                param.setPage(param.getPage()+1);
                data = staffPerfFacade.queryRepairInfoGroupByOrder(param);
                empRepairInfoVOs = data.getContent();
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("员工服务项目统计导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    /**
     * 销售绩效统计excel导出(员工维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "sale_collect/export", method = RequestMethod.GET)
    public void exporSaleCollectExcel(@RequestParam final String dateStr,
                                      HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Long sTime = System.currentTimeMillis();
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        List<EmpSaleInfoVO> data = staffPerfFacade.queryTotalSaleInfo(shopId,dateStr);

        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "销售业绩统计-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——销售业绩统计";
            exportor.writeTitle(null, headline, SalePrefByPersonBO.class);
            recordSize += data.size();
            // 实体转换
            SalePrefGroupByEmpConverter converter = new SalePrefGroupByEmpConverter();
            List<SalePrefByPersonBO> boList = Lists.newArrayList();
            for(EmpSaleInfoVO vo : data){
                SalePrefByPersonBO bo = converter.convert(vo);
                boList.add(bo);
            }
            exportor.write(boList);
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("维修业绩统计导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    /**
     * 销售绩效统计excel导出(物料维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "sale_goods/export", method = RequestMethod.GET)
    public void exporSaleGoodsExcel(@RequestParam final String dateStr,@RequestParam final Long empId,
                                      HttpServletRequest request, final HttpServletResponse response) throws IOException, WheelException {
        Assert.hasText(dateStr,"查询日期不能为空.");
        Assert.notNull(empId);

        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "员工配件项目统计-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");

        ExcelExportor exportor = null;
        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, dateStr);
            String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_GOODS);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工配件项目统计";
            exportor.writeTitle(profile, headline, SalePrefByGoodsBO.class);
            List<EmpSaleInfoVO> rawItems = staffPerfFacade.querySaleInfoGroupByService(shopId, dateStr, empId);
            if (CollectionUtils.isEmpty(rawItems)) {
                return;
            }
            List<SalePrefByGoodsBO> exportItems =
                    BeanMapper.mapListIfPossible(rawItems, SalePrefByGoodsBO.class);
            exportor.write(profile, exportItems);
            logExport(userInfo, fileName, sTime, exportItems.size());
        }finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private void logExport(UserInfo userInfo, String fileName, long startTime,
                           int size) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String exportLog = ExportLog.getExportLog(fileName, userInfo, size, elapsedTime);
        log.info(exportLog);
    }

    /**
     * 销售绩效统计excel导出(工单维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "sale_order/export", method = RequestMethod.GET)
    public void exporSaleOrderExcel(@RequestParam final String dateStr,@RequestParam final Long empId,
                                    HttpServletRequest request, final HttpServletResponse response) throws IOException, WheelException {
        Assert.hasText(dateStr,"查询日期不能为空.");
        Assert.notNull(empId);
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "员工配件项目明细-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;

        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            List<EmpPrefConfig> configs =
                    gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, dateStr);
            String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_GOODS);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工配件项目明细";
            exportor.writeTitle(profile, headline, SalePrefByOrderBO.class);
            RepairPrefParam param = new RepairPrefParam();
            param.setShopId(shopId);
            param.setDateStr(dateStr);
            param.setEmpId(empId);
            param.setSize(Constants.EXCEL_EXPORT_LIMIT);
            int page = 1;
            int exportSize = 0;
            while (true) {
                param.setPage(page++);
                SimplePage<EmpSaleInfoVO> rawPage = staffPerfFacade.querySaleInfoGroupByOrder(param);
                List<EmpSaleInfoVO> rawItems = rawPage.getContent();
                if (CollectionUtils.isEmpty(rawItems)) {
                    break;
                }
                List<SalePrefByOrderBO> exportItems = BeanMapper.mapListIfPossible(rawItems, SalePrefByOrderBO.class);
                exportor.write(profile, exportItems);
                exportSize += exportItems.size();
            }
            logExport(userInfo, fileName, sTime, exportSize);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    /**
     * 员工业绩汇总excel导出(总共单)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "collect/export", method = RequestMethod.GET)
    public void exporCollectExcel(@RequestParam final String dateStr,
                                    HttpServletRequest request, final HttpServletResponse response) throws IOException, WheelException {
        Assert.hasText(dateStr,"查询日期不能为空.");
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "员工业绩汇总-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, dateStr);
            //profiles
            String saletype = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_GOODS);
            saletype = "sale_" + saletype;
            String advisorType = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_SERVICEADVISOR);
            advisorType = "advisor_" + advisorType;
            String profiles = saletype+","+advisorType;


            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工业绩汇总";
            exportor.writeTitle(profiles, headline, TotalPrefInfoBO.class);
            List<EmpPerformanceInfoVO> rawItems = staffPerfFacade.queryTotalPercentageInfo(shopId, dateStr);
            if (CollectionUtils.isEmpty(rawItems)) {
                return;
            }
            List<TotalPrefInfoBO> exportItems = BeanMapper.mapListIfPossible(rawItems, TotalPrefInfoBO.class);
            exportor.write(profiles, exportItems);

            logExport(userInfo, fileName, sTime, exportItems.size());

        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }

    /**
     * 销售绩效统计excel导出(员工维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "sa_collect/export", method = RequestMethod.GET)
    public void exporSACollectExcel(@RequestParam final String dateStr,
                                      HttpServletRequest request, final HttpServletResponse response) throws IOException, WheelException {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "服务顾问业绩汇总"+"-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, dateStr);
            String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_SERVICEADVISOR);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——服务顾问业绩汇总";
            exportor.writeTitle(profile, headline, SAPrefByPersonBO.class);
            List<EmpSAInfoVO> rawItems = staffPerfFacade.querySATotalInfo(shopId, dateStr);
            if (CollectionUtils.isEmpty(rawItems)) {
                return;
            }
            List<SAPrefByPersonBO> exportItems = BeanMapper.mapListIfPossible(rawItems, SAPrefByPersonBO.class);
            exportor.write(profile, exportItems);
            logExport(userInfo, fileName, sTime, exportItems.size());
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    /**
     * 销售绩效统计excel导出(工单维度)
     * @param dateStr yyyy-MM
     * @param request
     * @param response
     */
    @RequestMapping(value = "sa_order/export", method = RequestMethod.GET)
    public void exporSaOrderExcel(@RequestParam final String dateStr,@RequestParam final Long empId,
                                    HttpServletRequest request, final HttpServletResponse response) throws IOException, WheelException {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        String fileName = "员工服务顾问业绩统计-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long sTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response,fileName);
            List<EmpPrefConfig> configs = staffPerfFacade.getAllPerformanceConfigByMonth(shopId, dateStr);
            String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_SERVICEADVISOR);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工服务顾问业绩统计";
            exportor.writeTitle(profile, headline, SAPrefByOrderBO.class);
            RepairPrefParam param = new RepairPrefParam();
            param.setEmpId(empId);
            param.setShopId(shopId);
            param.setDateStr(dateStr);
            param.setSize(Constants.EXCEL_EXPORT_LIMIT);
            int page = 1;
            int exportSize = 0;
            while (true) {
                param.setPage(page++);
                SimplePage<EmpSAInfoVO> rawItemPage = staffPerfFacade.querySAInfoGroupByOrder(param);
                List<EmpSAInfoVO> rawItems = rawItemPage.getContent();
                if (CollectionUtils.isEmpty(rawItems)) {
                    break;
                }
                List<SAPrefByOrderBO> exportItems = BeanMapper.mapListIfPossible(rawItems, SAPrefByOrderBO.class);
                exportor.write(profile, exportItems);
                exportSize += exportItems.size();
            }
            logExport(userInfo, fileName, sTime, exportSize);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }
}
