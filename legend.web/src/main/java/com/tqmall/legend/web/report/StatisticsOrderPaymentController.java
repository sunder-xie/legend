package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcStatOrderPaymentService;
import com.tqmall.cube.shop.result.StatOrderPaymentDTO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.annotation.Condition;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.report.export.vo.StatOrderPaymentCommission;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工单结算收款表控制器
 */
@Controller
@RequestMapping("/shop/stats/order_payment")
@Slf4j
public class StatisticsOrderPaymentController extends BaseController{
    @Autowired
    private RpcStatOrderPaymentService rpcStatOrderPaymentService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping
    @HttpRequestLog
    public String index(Model model,
                        @RequestParam(value = "sPayTime", required = false) String sPayTime,
                        @RequestParam(value = "ePayTime", required = false) String ePayTime,
                        @RequestParam(value = "orderTag", required = false) Integer orderTag,
                        @RequestParam(value = "orderTagName", required = false) String orderTagName) {
        model.addAttribute("moduleUrlTab","order_payment");
        Long shopId = UserUtils.getShopIdForSession(request);
        List<ShopManager> shopManagers = shopManagerService.selectByShopId(shopId);
        model.addAttribute("shopManagers",shopManagers);

        // 营业报表跳转过来
        if (StringUtils.isNotEmpty(sPayTime) && StringUtils.isNotEmpty(ePayTime)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date startDate = df.parse(sPayTime);
                Date endDate = df.parse(ePayTime);
                if (startDate.after(new Date()) || startDate.after(endDate)) {
                    return "yqx/page/report/statistics/order/payment";
                }
            } catch (ParseException e) {
                log.error("营业报表跳转工单结算表,日期格式错误:参数:sPayTime={}, ePayTime={}, 异常信息:", sPayTime, ePayTime, e);
                return "yqx/page/report/statistics/order/payment";
            }
            model.addAttribute("sPayTime", sPayTime);
            model.addAttribute("ePayTime", ePayTime);
        }
        if (orderTag != null && StringUtils.isNotEmpty(orderTagName)) {
            model.addAttribute("orderTag", orderTag);
            model.addAttribute("orderTagName", orderTagName);
        }
        return "yqx/page/report/statistics/order/payment";
    }

    @RequestMapping("list")
    @ResponseBody
    @HttpRequestLog(conditions = {@Condition(name="search_license",aliasName = "license"),@Condition(name = "search_flag",aliasName = "flag"),
            @Condition(name = "search_status",aliasName = "status"),@Condition(name = "search_customerName",aliasName = "customerName"),
            @Condition(name = "search_orderSn",aliasName = "orderSn"),@Condition(name = "search_orderTag",aliasName = "orderTag"),
            @Condition(name = "search_serverId",aliasName = "serverId")},
            params = {@Param(name="search_sPayTime",aliasName = "startTime"),@Param(name="search_ePayTime",aliasName = "endTime")})
    public Result list(@PageableDefault(page = 1, value = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        StatOrderPaymentDTO paramDTO = getStatOrderPaymentDTO(param, shopId, pageNumber, pageSize);
        com.tqmall.core.common.entity.Result<List<StatOrderPaymentDTO>> statOrderPayment = null;
        try {
            statOrderPayment = rpcStatOrderPaymentService.getStatOrderPayment(paramDTO);
        }catch(Exception e){
            log.error("调用cube获取工单结算接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        if(statOrderPayment == null || !statOrderPayment.isSuccess()){
            log.error("调用工单结算收款列表接口失败,{}",statOrderPayment.getMessage());
            return Result.wrapErrorResult(statOrderPayment.getCode(),statOrderPayment.getMessage());
        }
        List<StatOrderPaymentDTO> data = statOrderPayment.getData();
        Integer total = 0;
        if(CollectionUtils.isNotEmpty(data)){
            total = data.get(0).getTotal();
        }
        //组装page
        PageRequest pageRequest =
                new PageRequest((pageNumber < 1 ? 1 : pageNumber) - 1,
                        pageSize < 1 ? 1 : pageSize, pageable.getSort());
        DefaultPage<StatOrderPaymentDTO> page = new DefaultPage<>(data, pageRequest, total);
        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("total")
    @ResponseBody
    public Result total() {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        StatOrderPaymentDTO paramDTO = getStatOrderPaymentDTO(param, shopId, 0, 0);
        com.tqmall.core.common.entity.Result<TotalReportDataDTO> result = null;
        try {
            result = rpcStatOrderPaymentService.getTotalOrderPayment(paramDTO);
        }catch(Exception e){
            log.error("调用cube获取工单结算接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        if(result == null || !result.isSuccess()){
            log.error("调用工单结算收款列表接口失败,{}",result.getMessage());
            return Result.wrapErrorResult(result.getCode(),result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }

    private StatOrderPaymentDTO getStatOrderPaymentDTO(Map<String, Object> param, Long shopId, int pageNumber, int pageSize) {
        StatOrderPaymentDTO paramDTO = new StatOrderPaymentDTO();
        paramDTO.setShopId(shopId);
        paramDTO.setPage(pageNumber);
        paramDTO.setLimit(pageSize);
        paramDTO.setLicense((String) param.get("license"));
        paramDTO.setFlag((String) param.get("flag"));
        paramDTO.setStatus((String) param.get("status"));
        paramDTO.setCustomerName((String) param.get("customerName"));
        Object serverId = param.get("serverId");
        if(serverId != null) {
            paramDTO.setServerId(Long.valueOf((String)serverId));
        }
        String sPayTIme = (String) param.get("sPayTime");
        String ePayTIme = (String) param.get("ePayTime");
        Date sTime = DateUtil.convertStringToDate(sPayTIme, "yyyy-MM-dd");
        Date eTime = DateUtil.convertStringToDate(ePayTIme + " 23:59:59","yyyy-MM-dd hh:mm:ss");
        paramDTO.setSPayTime(sTime);
        paramDTO.setEPayTime(eTime);
        paramDTO.setOrderSn((String) param.get("orderSn"));
        Object orderTag = param.get("orderTag");
        if(null != orderTag){
            paramDTO.setOrderTag(Integer.parseInt(orderTag+""));
        }
        return paramDTO;
    }

    @RequestMapping("get_excel")
    @ResponseBody
    public void getExcelList(@PageableDefault(page = 1, value = 500,
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request,
                               HttpServletResponse response, Model model
    )throws Exception {
        Map<String, Object> param = ServletUtils.getParametersMapStartWith(request);

        Long sTime = System.currentTimeMillis();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        StatOrderPaymentDTO paramDTO = getStatOrderPaymentDTO(param, shopId, pageNumber, pageSize);
        com.tqmall.core.common.entity.Result<List<StatOrderPaymentDTO>> statOrderPayment = rpcStatOrderPaymentService.getStatOrderPayment(paramDTO);

        if(statOrderPayment == null || !statOrderPayment.isSuccess()){
            log.error("调用工单结算收款列表接口失败,{}",statOrderPayment.getMessage());
        }
        List<StatOrderPaymentDTO> orderPaymentDTOList = statOrderPayment.getData();
        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "工单结算收款表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工单结算收款表";
            exportor.writeTitle(null, headline, StatOrderPaymentCommission.class);
            while(orderPaymentDTOList != null && Langs.isNotEmpty(orderPaymentDTOList)) {
                recordSize += orderPaymentDTOList.size();
                List<StatOrderPaymentCommission> commissionList = Lists.newArrayList();
                commissionList = converter(commissionList,orderPaymentDTOList);
                exportor.write(commissionList);
                pageable = new PageableRequest(pageable.getPageNumber()+1,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"oi.create_time"});
                paramDTO = getStatOrderPaymentDTO(param, shopId, pageable.getPageNumber(), pageSize);
                statOrderPayment = rpcStatOrderPaymentService.getStatOrderPayment(paramDTO);

                orderPaymentDTOList = statOrderPayment.getData();
            }

            com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcStatOrderPaymentService.getTotalOrderPayment(paramDTO);
            if (reportDataDTOResult.isSuccess()) {
                TotalReportDataDTO zj = reportDataDTOResult.getData();

                Row row = exportor.createRow();
                Cell cell = row.createCell(0);
                cell.setCellValue("总计");
                if (null != zj){
                    Cell cell1 = row.createCell(6);
                    cell1.setCellValue(zj.getPayAmount().doubleValue());
                    Cell cell2 = row.createCell(8);
                    cell2.setCellValue(zj.getGrossAmount().doubleValue());
                    Cell cell3 = row.createCell(21);
                    cell3.setCellValue(zj.getSignAmount().doubleValue());
                    Cell cell4 = row.createCell(23);
                    cell4.setCellValue(zj.getBadAmount().doubleValue());
                }else {
                    Cell cell1 = row.createCell(6);
                    cell1.setCellValue("0");
                    Cell cell2 = row.createCell(8);
                    cell2.setCellValue("0");
                    Cell cell3 = row.createCell(21);
                    cell3.setCellValue("0");
                    Cell cell4 = row.createCell(23);
                    cell4.setCellValue("0");
                }
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("工单结算收款表导出", userInfo, recordSize, exportTime);
            log.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }



    }

    private List<StatOrderPaymentCommission> converter(List<StatOrderPaymentCommission> commissions , List<StatOrderPaymentDTO> orderInfoDetailVOs){
        if(null != orderInfoDetailVOs && !CollectionUtils.isEmpty(orderInfoDetailVOs)){
            for(StatOrderPaymentDTO detailVO : orderInfoDetailVOs){
                if(null != detailVO){
                    StatOrderPaymentCommission commission = new StatOrderPaymentCommission();
                    BeanUtils.copyProperties(detailVO , commission);
                    commissions.add(commission);
                }
            }
        }
        return commissions;
    }

}
