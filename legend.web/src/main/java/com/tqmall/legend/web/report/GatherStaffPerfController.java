package com.tqmall.legend.web.report;

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
import com.tqmall.legend.facade.report.bo.GatherConfigParam;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.StaffPerfConfigUtil;
import com.tqmall.legend.web.report.export.vo.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by tanghao on 16/12/16.
 * 样板店绩效报表
 */
@Controller
@RequestMapping("shop/report/gather/staff/perf")
@Slf4j
public class GatherStaffPerfController extends BaseController {

    @Autowired
    private GatherStaffPerfFacade gatherStaffPerfFacade;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("moduleUrlTab", "report_staff_perf");
        return "yqx/page/report/statistics/staff/staff-perf";
    }

    /**
     * 检测门店是否配置了默认提成比例
     *
     * @return
     */
    @RequestMapping(value = "check_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> checkConfig(final String month) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(month,"查询月份不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                boolean flag = gatherStaffPerfFacade.checkConfig(shopId,month);
                return flag;
            }
        }.execute();
    }

    /**
     * 提交提成配置信息
     *
     * @return
     */
    @RequestMapping(value = "submit_config", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> submitConfig(@RequestBody final GatherConfigParam gatherConfigParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(gatherConfigParam, "提交对象不能为空.");
                if (CollectionUtils.isEmpty(gatherConfigParam.getPerformanceConfigVOs())) {
                    throw new IllegalArgumentException("设置不能为空");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.submitPerformanceConfig(shopId, gatherConfigParam);
            }
        }.execute();
    }

    /**
     * 获取门店查询月份所有提成配置信息
     *
     * @return
     */
    @RequestMapping(value = "get_all_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<EmpPrefConfig>> getAllConfig(final String month) {
        return new ApiTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(month, "查询月份不能为空.");
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, month);
            }
        }.execute();
    }

    /**
     * 获取门店维修提成配置信息
     *
     * @return
     */
    @RequestMapping(value = "get_add_point_config", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<EmpPrefConfig>> getRepairConfig() {
        return new ApiTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.getAddPointConfig(shopId);
            }
        }.execute();
    }

    /**
     * 获取样板店老板业绩汇总
     *
     * @return
     */
    @RequestMapping(value = "collect_boss", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<GatherBossPerformanceInfoVO>> bossCollect(@RequestParam final String dateStr) {
        return new ApiTemplate<List<GatherBossPerformanceInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr, "日期不能为空");
            }

            @Override
            protected List<GatherBossPerformanceInfoVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.queryBossCollect(shopId, dateStr);
            }
        }.execute();
    }

    /**
     * 获取样板店员工业绩汇总
     *
     * @return
     */
    @RequestMapping(value = "collect_user", method = RequestMethod.GET)
    @ResponseBody
    public Result<GatherEmpPerformanceInfoVO> userCollect(@RequestParam final String dateStr) {
        return new ApiTemplate<GatherEmpPerformanceInfoVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr, "日期不能为空");
            }

            @Override
            protected GatherEmpPerformanceInfoVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userId = UserUtils.getUserIdForSession(request);
                return gatherStaffPerfFacade.queryUserCollect(shopId, dateStr, userId);
            }
        }.execute();
    }

    /**
     * 获取样板店加点提成汇总
     *
     * @return
     */
    @RequestMapping(value = "add_point_info", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<List<GatherAddPointPrefVO>> addPointInfo(@RequestParam final String dateStr) {
        return new ApiTemplate<List<GatherAddPointPrefVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr, "日期不能为空");
            }

            @Override
            protected List<GatherAddPointPrefVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.queryAddPointInfo(shopId, dateStr);
            }
        }.execute();
    }

    /**
     * 获取样板店加点提成详情汇总
     *
     * @return
     */
    @RequestMapping(value = "add_point_detail", method = RequestMethod.GET)
    @ResponseBody
    @HttpRequestLog
    public Result<GatherAddPointPrefVO> addPointDetail(@RequestParam final String dateStr, @RequestParam final Long userId) {
        return new ApiTemplate<GatherAddPointPrefVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr, "日期不能为空");
                Assert.notNull(userId, "查询员工id不能为空");
            }

            @Override
            protected GatherAddPointPrefVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.queryAddPointDetail(shopId, dateStr, userId);
            }
        }.execute();
    }

    /**
     * 获取加点详情销售之星列表
     *
     * @return
     */
    @RequestMapping(value = "sale_star", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<GatherPrefSaleStarVO>> saleStarList(@RequestParam final String dateStr, @RequestParam final Long userId) {
        return new ApiTemplate<List<GatherPrefSaleStarVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr, "日期不能为空");
                Assert.notNull(userId, "查询员工id不能为空");
            }

            @Override
            protected List<GatherPrefSaleStarVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherStaffPerfFacade.querySaleStarList(shopId, dateStr, userId);
            }
        }.execute();
    }

    /**
     * 获取加点详情拉新客户分页
     *
     * @return
     */
    @RequestMapping(value = "new_customer", method = RequestMethod.POST)
    @ResponseBody
    public Result<SimplePage<GatherPrefNewCustomerPrefVo>> newCustomerList(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<GatherPrefNewCustomerPrefVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam, "传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(), "日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(), "员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(), "页码不能为空");
            }

            @Override
            protected SimplePage<GatherPrefNewCustomerPrefVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return gatherStaffPerfFacade.queryNewCustomerList(repairPrefParam);
            }
        }.execute();
    }

    /**
     * 获取加点详情业绩归属分页
     *
     * @return
     */
    @RequestMapping(value = "business_belone", method = RequestMethod.POST)
    @ResponseBody
    public Result<SimplePage<GatherBusinessBeloneVO>> businessBeloneList(@RequestBody final RepairPrefParam repairPrefParam) {
        return new ApiTemplate<SimplePage<GatherBusinessBeloneVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(repairPrefParam, "传入参数不能为空");
                Assert.hasText(repairPrefParam.getDateStr(), "日期不能为空");
                Assert.notNull(repairPrefParam.getEmpId(), "员工id不能为空");
                Assert.notNull(repairPrefParam.getPage(), "页码不能为空");
            }

            @Override
            protected SimplePage<GatherBusinessBeloneVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                repairPrefParam.setShopId(shopId);
                return gatherStaffPerfFacade.queryBusinessBeloneList(repairPrefParam);
            }
        }.execute();
    }

    @RequestMapping(value = "export_commission_summary", method = RequestMethod.GET)
    public void exportCommissionSummary(String monthStr, HttpServletResponse response)
            throws IOException, WheelException {
        Assert.hasText(monthStr, "日期不能为空");
        UserInfo userInfo = UserUtils.getUserInfo(request);

        String fileName = "加点提成汇总-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long startTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(
                    userInfo.getShopId(), monthStr);
            String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_ADDPOINT);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——加点提成汇总";
            exportor.writeTitle(profile, headline, AddPointCommissionSummary.class);
            List<GatherAddPointPrefVO> rawItems =
                    gatherStaffPerfFacade.queryAddPointInfo(userInfo.getShopId(), monthStr);
            if (rawItems == null) {
                return;
            }
            List<AddPointCommissionSummary> exportItems =
                    BeanMapper.mapListIfPossible(rawItems, AddPointCommissionSummary.class);
            exportor.write(profile,exportItems);

            logExport(userInfo, fileName, startTime, exportItems.size());
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

        return;
    }

    private void logExport(UserInfo userInfo, String fileName, long startTime,
                           int size) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String exportLog = ExportLog.getExportLog(fileName, userInfo, size, elapsedTime);
        log.info(exportLog);
    }

    @RequestMapping(value = "export_achievement_summary", method = RequestMethod.GET)
    public void exportStaffAchievementSummary(@NotNull String monthStr, HttpServletResponse response) throws IOException, WheelException {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String fileName = "员工业绩汇总-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long startTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);

            List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(
                    userInfo.getShopId(), monthStr);
            String saletype = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_GOODS);
            saletype = "sale_" + saletype;
            String advisorType = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_SERVICEADVISOR);
            advisorType = "advisor_" + advisorType;
            String profile = saletype+","+advisorType;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——员工业绩汇总";
            exportor.writeTitle(profile, headline, AchievementSummary.class);

            List<GatherBossPerformanceInfoVO> rawItems =
                    gatherStaffPerfFacade.queryBossCollect(userInfo.getShopId(), monthStr);
            if (rawItems == null) {
                return;
            }
            List<AchievementSummary> exportItems =
                    BeanMapper.mapListIfPossible(rawItems, AchievementSummary.class);
            exportor.write(profile,exportItems);
            logExport(userInfo, fileName, startTime, exportItems.size());
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }


    @RequestMapping(value = "export_staff_commission", method = RequestMethod.GET)
    public void exportStaffCommission(@NotNull String monthStr,
                                      @NotNull Long empId,
                                      HttpServletResponse response)
            throws IOException, WheelException {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(userInfo.getShopId());
        String fileName = "员工加点提成-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        try {
            long startTime = System.currentTimeMillis();
            exportor = ExcelHelper.createDownloadExportor(response, fileName);

            int size1 = writeSale(exportor, monthStr, shopId, empId,shop);
            int size2 = writeNewCustomer(exportor, monthStr, shopId, empId,shop);
            int size3 = writeAchieveBelong(exportor, monthStr, shopId, empId,shop);

            int size = size1 + size2 + size3;
            logExport(userInfo, fileName, startTime, size);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private int writeNewCustomer(ExcelExportor exportor, String monthStr, Long shopId, Long userId,Shop shop)
            throws WheelException {
        exportor.createSheet("新客户到店奖励");
        String headline = shop.getName() + "——新客户到店奖励";
        exportor.writeTitle(null, headline, NewCustomerCommission.class);
        RepairPrefParam param = new RepairPrefParam();
        param.setDateStr(monthStr);
        param.setEmpId(userId);
        param.setShopId(shopId);
        List<GatherPrefNewCustomerPrefVo> rawItems;

        int size = 0;
        int pageIndex = 1;
        while (true) {
            param.setPage(pageIndex);
            param.setSize(Constants.EXCEL_EXPORT_LIMIT);
            rawItems = gatherStaffPerfFacade.queryNewCustomerList(param).getContent();
            if (CollectionUtils.isEmpty(rawItems)) {
                break;
            }
            List<NewCustomerCommission> exportItems =
                    BeanMapper.mapListIfPossible(rawItems, NewCustomerCommission.class);
            exportor.write(exportItems);
            pageIndex++;
            size += exportItems.size();
        }
        return size;
    }

    private int writeAchieveBelong(ExcelExportor exportor, String monthStr, Long shopId, Long userId,Shop shop)
            throws WheelException {
        exportor.createSheet("业绩归属奖励");
        String headline = shop.getName() + "——业绩归属奖励";
        exportor.writeTitle(null, headline, AchievementBelongCommission.class);
        RepairPrefParam param = new RepairPrefParam();
        param.setDateStr(monthStr);
        param.setEmpId(userId);
        param.setShopId(shopId);
        List<EmpPrefConfig> configs = gatherStaffPerfFacade.getAllPerformanceConfigByMonth(shopId, monthStr);
        String profile = StaffPerfConfigUtil.turnoverOrProfit(configs, EmployeePerformanceConfigVO.CONFIGTYPE_ADDPOINT);
        List<GatherBusinessBeloneVO> rawItems;
        int size = 0;
        int pageIndex = 1;
        while (true) {
            param.setPage(pageIndex);
            param.setSize(Constants.EXCEL_EXPORT_LIMIT);
            rawItems = gatherStaffPerfFacade.queryBusinessBeloneList(param).getContent();
            if (CollectionUtils.isEmpty(rawItems)) {
                break;
            }
            List<AchievementBelongCommission> exportItems =
                    BeanMapper.mapListIfPossible(rawItems, AchievementBelongCommission.class);
            exportor.write(profile,exportItems);
            pageIndex++;
            size += exportItems.size();
        }
        return size;
    }

    private int writeSale(ExcelExportor exportor, String monthStr, Long shopId, Long userId,Shop shop) throws WheelException {
        exportor.createSheet("销售之星奖励");
        String headline = shop.getName() + "——销售之星奖励";
        exportor.writeTitle(null, headline, SaleStarCommission.class);
        List<GatherPrefSaleStarVO> rawItems =
                gatherStaffPerfFacade.querySaleStarList(shopId, monthStr, userId);
        if (Langs.isNotEmpty(rawItems)) {
            List<SaleStarCommission> exportItems = BeanMapper.mapListIfPossible(rawItems, SaleStarCommission.class);
            exportor.write(exportItems);
            return exportItems.size();
        } else {
            return 0;
        }
    }


}
