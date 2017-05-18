package com.tqmall.legend.web.order;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.error.LegendSearchErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.orderhistory.param.LegendOrderHistoryParam;
import com.tqmall.search.dubbo.client.legend.orderhistory.result.LegendOrderHistoryVO;
import com.tqmall.search.dubbo.client.legend.orderhistory.service.LegendOrderHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 维修历史
 * Created by lilige on 15/11/16.
 */
@Slf4j
@Controller
@RequestMapping("shop/order/history")
public class OrderHistoryController extends BaseController {
    @Autowired
    private LegendOrderHistoryService legendOrderHistoryService;

    /**
     * 列表搜索
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Object getPage(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                          HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        return getOrderInfo(searchParams, pageable);
    }

    /**
     * 历史导入工单查询页
     *
     * @param model
     * @return
     */
    @RequestMapping("history-list")
    public String historyList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.RECEPTION.getModuleUrl());
        return "yqx/page/order/history-list";
    }


    /**
     * 调用elasticsearch接口获得维修历史工单信息
     *
     * @param searchParams
     * @param pageable
     * @return
     */
    public Object getOrderInfo(Map<String, Object> searchParams, Pageable pageable) {
        // 参数拼接
        LegendOrderHistoryParam param = new LegendOrderHistoryParam();
        Integer shopId = Integer.valueOf(""+searchParams.get("shopId"));
        param.setShopId(shopId);
        // 车牌
        if (searchParams.containsKey("carLicenseLike")) {
            String carLicenseLike = (String) searchParams.get("carLicenseLike");
            param.setCarLicenseLike(carLicenseLike);
        }
        // 车主
        if (searchParams.containsKey("customerNameLike")) {
            String customerNameLike = (String) searchParams.get("customerNameLike");
            param.setCustomerNameLike(customerNameLike);
        }
        // 车主电话
        if (searchParams.containsKey("customerMobileLike")) {
            String customerMobileLike = (String) searchParams.get("customerMobileLike");
            param.setCustomerMobileLike(customerMobileLike);
        }
        // 开单时间
        if (searchParams.containsKey("startTime")) {
            String startTime = (String) searchParams.get("startTime");
            param.setStartTime(startTime +" 00:00:00");
        }
        // 开单时间
        if (searchParams.containsKey("endTime")) {
            String endTime = (String) searchParams.get("endTime");
            param.setEndTime(endTime + " 23:59:59");
        }
        // 接车人
        if (searchParams.containsKey("receiverLike")) {
            String receiverLike = (String) searchParams.get("receiverLike");
            param.setReceiverLike(receiverLike);
        }
        // 工单状态
        if (searchParams.containsKey("orderStatusLike")) {
            String orderStatusLike = (String) searchParams.get("orderStatusLike");
            param.setOrderStatusLike(orderStatusLike);
        }
        //工单编号
        if (searchParams.containsKey("orderSn")) {
            String orderSn = (String) searchParams.get("orderSn");
            param.setOrderSn(orderSn);
        }
        log.info("请求搜索服务平台.查询维修历史工单列表 param:{}", param);
        try {
            //封装page数据
            int pageNum = pageable.getPageNumber() - 1;
            int pageSize = pageable.getPageSize();
            Sort sort = pageable.getSort();
            FieldsSort fieldsSort = new FieldsSort(sort);
            PageableRequest pageRequest = new PageableRequest(pageNum, pageSize, fieldsSort);
            com.tqmall.search.common.result.Result<Page<LegendOrderHistoryVO>> searchResult =legendOrderHistoryService.getOrderHistories(param,pageRequest);
            if (null == searchResult || !searchResult.isSuccess() ){
                log.error("[请求搜索服务平台.查询维修历史工单列表失败]searchResult:{}", LogUtils.objectToString(searchResult));
                return Result.wrapErrorResult(LegendSearchErrorCode.ORDER_HISTORY_SEARCH_ERROR.getCode(),LegendSearchErrorCode.ORDER_HISTORY_SEARCH_ERROR.getErrorMessage());
            }
            Page<LegendOrderHistoryVO> content = searchResult.getData();
            DefaultPage<LegendOrderHistoryVO> page = new DefaultPage<LegendOrderHistoryVO>(content.getContent(),pageRequest,content.getTotalElements());
            log.info("请求搜索服务平台.查询维修历史工单列表成功");
            return Result.wrapSuccessfulResult(page);
        } catch (Exception e) {
            log.error("请求搜索服务平台.查询维修历史工单列表出现异常,param:{}",param, e);
        }
        return Result.wrapErrorResult(LegendSearchErrorCode.ORDER_HISTORY_SEARCH_ERROR.getCode(),LegendSearchErrorCode.ORDER_HISTORY_SEARCH_ERROR.getErrorMessage());
    }
}
