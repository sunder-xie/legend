package com.tqmall.legend.web.report;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.report.GrossProfitsFacade;
import com.tqmall.legend.facade.report.bo.GrossProfitsBo;
import com.tqmall.legend.facade.report.bo.GrossProfitsSummaryBo;
import com.tqmall.wheel.component.excel.export.DefaultExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.support.rpc.RpcAPITemplate;
import com.tqmall.wheel.support.rpc.RpcException;
import com.tqmall.wheel.utils.DateFormatUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date 2017-04-13 6:00 PM
 * @Motto 一生伏首拜阳明
 */

@Controller
@RequestMapping("shop/report/gross-profits")
public class GrossProfitsController {

    @Autowired
    private GrossProfitsFacade facade;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping
    @HttpRequestLog
    public String index(Model model) {
        model.addAttribute("moduleUrlTab", "report_gross_profits");
        return "yqx/page/report/statistics/business/gross-profits";
    }

    @RequestMapping(value = "summary", method = RequestMethod.GET)
    @ResponseBody
    public Result<GrossProfitsSummaryBo> getSummary(@RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate, HttpServletRequest request) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<GrossProfitsSummaryBo>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(startDate);
                Assert.notNull(endDate);
            }

            @Override
            protected GrossProfitsSummaryBo process() throws BizException {
                return facade.getGrossProfitsSummary(shopId, startDate, endDate);
            }
        }.execute();
    }

    @RequestMapping("list")
    @ResponseBody
    @HttpRequestLog
    public Result<Page<GrossProfitsBo>> list(@RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate,
                                             @PageableDefault(page = 1, value = 10) final Pageable pageable, HttpServletRequest request) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Page<GrossProfitsBo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(startDate);
                Assert.notNull(endDate);
            }

            @Override
            protected Page<GrossProfitsBo> process() throws BizException {
                return facade.getGrossProfitsList(shopId, startDate, endDate, pageable);
            }
        }.execute();

    }

    @RequestMapping("export")
    @ResponseBody
    public void exportToExcel(@RequestParam("startDate") final String startDate,
                              @RequestParam("endDate") final String endDate,
                              HttpServletResponse response,
                              HttpServletRequest request) throws Exception {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        DefaultExcelExportor exportor = null;
        try {
            String fileName = "经营毛利明细表";
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            /**
             * --------------------------
             * 输出Excel表头
             * --------------------------
             */
            Shop shop = this.shopService.selectById(userInfo.getShopId());
            exportor.writeTitle(null, shop.getName() + "--经营毛利明细表", GrossProfitsBo.class);

            Pageable pageable = new PageRequest(1, 500);

            List<GrossProfitsBo> grossProfitsBos = facade.getGrossProfitsList(userInfo.getShopId(), startDate, endDate, pageable).getContent();

            while (Langs.isNotEmpty(grossProfitsBos)) {
                exportor.write(grossProfitsBos);
                pageable = new PageRequest(pageable.getPageNumber() + 1, pageable.getPageSize());
                grossProfitsBos = facade.getGrossProfitsList(userInfo.getShopId(), startDate, endDate, pageable).getContent();
            }
            Row row = exportor.createRow();
            GrossProfitsSummaryBo summary = this.facade.getGrossProfitsSummary(userInfo.getShopId(), startDate, endDate);

            StringBuffer sb = new StringBuffer();
            sb.append("查询结果：收入合计：")
                    .append(summary.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
                    .append(" 物料成本：").append(summary.getTotalInventoryAmount().setScale(2, BigDecimal.ROUND_HALF_UP))
                    .append(" 毛利：").append(summary.getGrossProfits().setScale(2, BigDecimal.ROUND_HALF_UP))
                    .append(" 单数：").append(summary.getTotalOrderCount());
            row.createCell(0).setCellValue(sb.toString());
            exportor.mergeCell(exportor.getWorkbook().getActiveSheetIndex(), row.getRowNum(), row.getRowNum(), 0, 11);


        } catch(Exception e) {
            throw new BizException(e);
        }
        finally {
            ExcelHelper.closeQuiet(exportor);
        }

    }
}
