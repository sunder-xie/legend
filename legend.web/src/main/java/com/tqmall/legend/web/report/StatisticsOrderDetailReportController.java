package com.tqmall.legend.web.report;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcOrderInfoDetailService;
import com.tqmall.cube.shop.param.OrderDetailParam;
import com.tqmall.cube.shop.result.OrderInfoDetailDTO;
import com.tqmall.cube.shop.result.OrderInfoDetailVO;
import com.tqmall.cube.shop.result.TotalReportDataDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.report.export.vo.OrderInfoDetailCommission;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.helper.ExcelHelper;
import com.tqmall.wheel.lang.Langs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghao on 16/5/19.
 */
@Controller
@RequestMapping(value = "/shop/stats/order_info_detail")
public class StatisticsOrderDetailReportController extends BaseController {
    Logger logger = LoggerFactory.getLogger(StatisticsOrderDetailReportController.class);

    @Autowired
    private RpcOrderInfoDetailService rpcOrderInfoDetailService;

    @Autowired
    private ShopService shopService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "report";
    }

    @RequestMapping("")
    public String index_ng(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (null == userInfo) {
            logger.error("用户信息错误");
            return "yqx/page/report/statistics/order/info-detail";
        }
        model.addAttribute("moduleUrlTab", "order_info_detail");
        return "yqx/page/report/statistics/order/info-detail";
    }


    @RequestMapping("list/{type}")
    @ResponseBody
    public Result getBusinessDaily(@PageableDefault(page = 1, value = 10, sort = "oi.create_time",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request,
                                   HttpServletResponse response, Model model,
                                   @PathVariable("type") String type) {

        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        OrderDetailParam orderDetailParam = setQueryParam(searchParams,userInfo,pageable);
        if(null==orderDetailParam.getCreateTimeStart()&&null==orderDetailParam.getPayTimeStart()){
            return Result.wrapErrorResult("","查询开始时间不能为空");
        }
        if(null==orderDetailParam.getCreateTimeEnd()&&null==orderDetailParam.getPayTimeEnd()){
            return Result.wrapErrorResult("","查询结束时间不能为空");
        }
        Result<OrderInfoDetailDTO> result = null;
        try{
            result = rpcOrderInfoDetailService.getOrderDetail(orderDetailParam);
        }catch (Exception e){
            logger.error("调用cube获取工单详情接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        logger.info("cube的工单详情接口返回成功:shopId:{}",userInfo.getShopId());
        OrderInfoDetailDTO orderInfoDetailDTO = result.getData();
        List<OrderInfoDetailVO> resultlist = orderInfoDetailDTO.getDataList();
        Long totalSize = orderInfoDetailDTO.getSize();

        _transformTemporary(request, resultlist);

        DefaultPage<OrderInfoDetailVO> page = new DefaultPage<OrderInfoDetailVO>(resultlist, pageRequest, totalSize);


        return Result.wrapSuccessfulResult(page);
    }

    @RequestMapping("orderTotal")
    @ResponseBody
    public Result orderTotal(HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        OrderDetailParam orderDetailParam = setQueryParam(searchParams,userInfo,null);
        if(null==orderDetailParam.getCreateTimeStart()&&null==orderDetailParam.getPayTimeStart()){
            return Result.wrapErrorResult("","查询开始时间不能为空");
        }
        if(null==orderDetailParam.getCreateTimeEnd()&&null==orderDetailParam.getPayTimeEnd()){
            return Result.wrapErrorResult("","查询结束时间不能为空");
        }
        Result<TotalReportDataDTO> result = null;
        try{
            result = rpcOrderInfoDetailService.getOrderInfoDetailTotalInfo(orderDetailParam);
        }catch (Exception e){
            logger.error("调用cube获取工单详情接口出错,{}", e);
            return Result.wrapErrorResult("","获取数据失败");
        }
        TotalReportDataDTO totalReportDataDTO = result.getData();
        return Result.wrapSuccessfulResult(totalReportDataDTO);
    }


    @RequestMapping(value = "get_excel", method = RequestMethod.GET)
    @ResponseBody
    public void orderList(
            @PageableDefault(page = 1, value = 500, sort = "oi.create_time", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletResponse response) throws Exception{

        Long sTime = System.currentTimeMillis();
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        OrderDetailParam orderDetailParam = setQueryParam(searchParams,userInfo,pageable);
        Result<OrderInfoDetailDTO> result = null;
        try{
            result = rpcOrderInfoDetailService.getOrderDetail(orderDetailParam);
        }catch (Exception e){
            logger.error("调用cube获取工单详情接口出错,{}", e);
        }
        logger.info("cube的工单详情接口返回message:{},code:{}",result.getMessage(),result.getCode());

        OrderInfoDetailDTO orderInfoDetailDTO = result.getData();
        ExcelExportor exportor = null;

        try {
            exportor = ExcelHelper.createDownloadExportor(response, "工单流水表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd"));
            int recordSize = 0;
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——工单流水表";
            exportor.writeTitle(null, headline, OrderInfoDetailCommission.class);
            while(orderInfoDetailDTO != null && Langs.isNotEmpty(orderInfoDetailDTO.getDataList())) {
                List<OrderInfoDetailVO> resultlist = orderInfoDetailDTO.getDataList();
                _transformTemporary(request, resultlist);
                recordSize += resultlist.size();
                List<OrderInfoDetailCommission> commissionList = Lists.newArrayList();
                commissionList = converter(commissionList,resultlist);
                exportor.write(commissionList);
                pageable = new PageableRequest(pageable.getPageNumber()+1,pageable.getPageSize(),Sort.Direction.DESC,new String[]{"oi.create_time"});
                orderDetailParam = setQueryParam(searchParams,userInfo,pageable);
                result = rpcOrderInfoDetailService.getOrderDetail(orderDetailParam);
                orderInfoDetailDTO = result.getData();
            }
            com.tqmall.core.common.entity.Result<TotalReportDataDTO> reportDataDTOResult = rpcOrderInfoDetailService.getOrderInfoDetailTotalInfo(orderDetailParam);
            if (reportDataDTOResult.isSuccess()) {
                TotalReportDataDTO zj = reportDataDTOResult.getData();

                Row row = exportor.createRow();
                Cell cell = row.createCell(0);
                cell.setCellValue("总计");
                if (null != zj){
                    Cell cell1 = row.createCell(12);
                    cell1.setCellValue(zj.getTotalAmount().doubleValue());
                    Cell cell2 = row.createCell(13);
                    cell2.setCellValue(zj.getPayAmount().doubleValue());
                    Cell cell3 = row.createCell(16);
                    cell3.setCellValue(zj.getGrossAmount().doubleValue());
                }else {
                    Cell cell1 = row.createCell(12);
                    cell1.setCellValue("0");
                    Cell cell2 = row.createCell(13);
                    cell2.setCellValue("0");
                    Cell cell3 = row.createCell(16);
                    cell3.setCellValue("0");
                }
            }
            long exportTime = System.currentTimeMillis() - sTime;
            String exportLog = ExportLog.getExportLog("工单流水表导出", userInfo, recordSize, exportTime);
            logger.info(exportLog);
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
    }

    private List<OrderInfoDetailCommission> converter(List<OrderInfoDetailCommission> commissions , List<OrderInfoDetailVO> orderInfoDetailVOs){
        if(null != orderInfoDetailVOs && !CollectionUtils.isEmpty(orderInfoDetailVOs)){
            for(OrderInfoDetailVO detailVO : orderInfoDetailVOs){
                if(null != detailVO){
                    OrderInfoDetailCommission commission = new OrderInfoDetailCommission();
                    BeanUtils.copyProperties(detailVO , commission);
                    commissions.add(commission);
                }
            }
        }
        return commissions;
    }


    private void _transformTemporary(HttpServletRequest request, List<OrderInfoDetailVO> resultlist) {
        if (Integer.valueOf(6).equals(UserUtils.getShopLevelForSession(request))) {
            if (resultlist != null) {
                for (OrderInfoDetailVO orderInfoDetailVO : resultlist) {
                    if ("待报价".equals(orderInfoDetailVO.getOrderStatus())) {
                        orderInfoDetailVO.setOrderStatus("待结算");
                    }
                }
            }
        }
    }

    private OrderDetailParam setQueryParam(Map<String,Object> searchMap,UserInfo userInfo,Pageable pageable){
        OrderDetailParam orderDetailParam = new OrderDetailParam();
        orderDetailParam.setShopId(userInfo.getShopId());
        if (pageable != null) {
            orderDetailParam.setPageSize(pageable.getPageSize());
            orderDetailParam.setPageNum(pageable.getPageNumber());
        }

        for (String key : searchMap.keySet()) {
            if(key.equals("carLicense")){
                orderDetailParam.setCarLicense((String) searchMap.get("carLicense"));
            }

            if(key.equals("createTimeStart")){
                Date createTimeStart = DateUtil.convertStringToDateYMD(searchMap.get("createTimeStart")+"");
                createTimeStart = DateUtil.getStartTime(createTimeStart);
                orderDetailParam.setCreateTimeStart(createTimeStart);
            }

            if(key.equals("createTimeEnd")){
                Date createTimeEnd = DateUtil.convertStringToDateYMD(searchMap.get("createTimeEnd")+"");
                createTimeEnd = DateUtil.getEndTime(createTimeEnd);
                orderDetailParam.setCreateTimeEnd(createTimeEnd);
            }

            if(key.equals("customerName")){
                orderDetailParam.setCustomerName(searchMap.get("customerName")+"");
            }

            if(key.equals("orderTag")){
                orderDetailParam.setOrderTag(Integer.parseInt(searchMap.get("orderTag")+""));
            }

            if(key.equals("receiver")){
                orderDetailParam.setReceiver(Integer.parseInt(searchMap.get("receiver")+""));
            }

            if(key.equals("worker")){
                orderDetailParam.setWorker(Integer.parseInt(searchMap.get("worker")+""));
            }

            if(key.equals("orderStatus")){
                orderDetailParam.setOrderStatus(searchMap.get("orderStatus")+"");
            }
            if(key.equals("saler")){
                orderDetailParam.setSaler(searchMap.get("saler")+"");
            }
            if(key.equals("orderSn")){
                orderDetailParam.setOrderSn(searchMap.get("orderSn")+"");
            }

            if(key.equals("payTimeStart")){
                Date payTimeStart = DateUtil.convertStringToDateYMD(searchMap.get("payTimeStart")+"");
                payTimeStart = DateUtil.getStartTime(payTimeStart);
                orderDetailParam.setPayTimeStart(payTimeStart);
            }

            if(key.equals("payTimeEnd")){
                Date payTimeEnd = DateUtil.convertStringToDateYMD(searchMap.get("payTimeEnd")+"");
                payTimeEnd = DateUtil.getEndTime(payTimeEnd);
                orderDetailParam.setPayTimeEnd(payTimeEnd);
            }
        }
        return orderDetailParam;
    }

}
