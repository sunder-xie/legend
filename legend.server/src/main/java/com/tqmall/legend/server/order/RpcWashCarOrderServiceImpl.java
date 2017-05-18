package com.tqmall.legend.server.order;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.order.IOrderService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.CustomerCompletionFormEntity;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.WashCarStats;
import com.tqmall.legend.facade.order.CarWashFacade;
import com.tqmall.legend.object.param.activity.PageParam;
import com.tqmall.legend.object.param.order.CustomerCompletionFormEntityParam;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.order.WashCarOrderStatsDTO;
import com.tqmall.legend.service.order.RpcWashCarOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xiangdong.qu on 17/2/10 10:59.
 */
@Service("rpcWashCarOrderService")
@Slf4j
public class RpcWashCarOrderServiceImpl implements RpcWashCarOrderService {


    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private CarWashFacade carWashFacade;

    /**
     * 获取店铺洗车单当天统计
     *
     * @param shopId
     *
     * @return
     */
    @Override
    public Result<WashCarOrderStatsDTO> getShopWashCarTodayStats(Long shopId) {
        if (shopId == null || shopId < 1) {
            return Result.wrapErrorResult("", "店铺信息错误");
        }
        WashCarStats washCarStats = null;
        try {
            washCarStats = iOrderService.getWashCarTodayStats(shopId);
        } catch (Exception e) {
            log.error("[工单API] 获取洗车单今日统计信息异常", e);
        }
        if (null == washCarStats) {
            return Result.wrapErrorResult("", "获取洗车单今日统计信息异常");
        }
        WashCarOrderStatsDTO washCarOrderStatsDTO = new WashCarOrderStatsDTO();
        washCarOrderStatsDTO.setStatsAmount(washCarStats.getStatsAmount());
        washCarOrderStatsDTO.setStatsCount(washCarStats.getStatsCount());
        return Result.wrapSuccessfulResult(washCarOrderStatsDTO);
    }

    /**
     * 获取店铺当天洗车单列表
     *
     * @param shopId
     *
     * @return
     */

    @Override
    public Result<PageEntityDTO<OrderInfoDTO>> getShopWashCarOrderTodayPage(Long shopId, PageParam pageParam) {
        if (pageParam == null) {
            return Result.wrapErrorResult("", "分页参数为空");
        }
        if (shopId == null || shopId < 1) {
            return Result.wrapErrorResult("", "店铺信息错误");
        }
        try {

            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("shopId", shopId);
            searchParams.put("orderTag", 2);
            String currentTime = DateUtil.convertDateToYMD(new Date());
            String startTime = currentTime + " 00:00:00";
            String endTime = currentTime + "23:59:59";
            searchParams.put("startTime", startTime);
            searchParams.put("endTime", endTime);

            List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
            PageEntityDTO<OrderInfoDTO> pageEntityDTO = new PageEntityDTO();
            pageEntityDTO.setPageNum(pageParam.getPage());
            int totalCount = orderInfoService.selectCount(searchParams);
            pageEntityDTO.setTotalNum(Long.valueOf(totalCount));
            if (totalCount < 1) {
                pageEntityDTO.setContent(orderInfoDTOList);
                return Result.wrapSuccessfulResult(pageEntityDTO);
            }
            searchParams.put("offset", pageParam.getOffset());
            searchParams.put("limit", pageParam.getLimit());
            searchParams.put("sorts", pageParam.getSorts());

            List<OrderInfo> orderInfoList = orderInfoService.select(searchParams);
            for (OrderInfo orderInfo : orderInfoList) {
                OrderInfoDTO orderInfoDTOTemp = new OrderInfoDTO();
                BeanUtils.copyProperties(orderInfo, orderInfoDTOTemp);
                orderInfoDTOList.add(orderInfoDTOTemp);
            }
            pageEntityDTO.setContent(orderInfoDTOList);
            return Result.wrapSuccessfulResult(pageEntityDTO);
        } catch (Exception e) {
            log.error("[工单API] 获取店铺的当天洗车单列表失败. e:", e);
            return Result.wrapErrorResult("", "获取订单信息失败");
        }
    }

    /**
     * 洗车单完善客户资料
     *
     * @param customerCompletionFormEntityParam
     *
     * @return
     */
    @Override
    public Result perfectCustomerInfo(CustomerCompletionFormEntityParam customerCompletionFormEntityParam) {
        if (null == customerCompletionFormEntityParam) {
            return Result.wrapErrorResult("", "参数为空");
        }
        CustomerCompletionFormEntity customerCompletionFormEntity = new CustomerCompletionFormEntity();
        try {
            BeanUtils.copyProperties(customerCompletionFormEntityParam, customerCompletionFormEntity);
        } catch (Exception e) {
            log.error("[洗车单API]洗车单完善信息保存异常",e);
            return Result.wrapErrorResult("","完善客户信息失败");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setShopId(customerCompletionFormEntity.getShopId());
        userInfo.setUserId(customerCompletionFormEntity.getUserId());
        userInfo.setName(customerCompletionFormEntity.getUserName());
        //为了解决洗车专用车牌，完善客户信息时 车牌号在数据库已存在
        CustomerCar customerCar = customerCarService.selectByLicenseAndShopId(customerCompletionFormEntity.getCarLicense(), customerCompletionFormEntity.getShopId());
        if (customerCar != null) {
            customerCompletionFormEntity.setCustomerCarId(customerCar.getId());
            customerCompletionFormEntity.setCustomerId(customerCar.getCustomerId());
        }
        try {
            carWashFacade.perfectCustomer(customerCompletionFormEntity, userInfo);
            return Result.wrapSuccessfulResult(true);
        } catch (BizException e) {
            return Result.wrapErrorResult("", e.getMessage());
        } catch (Exception e) {
            log.error("[洗车单API]洗车单完善信息保存异常", e);
            Result errorResult = Result.wrapErrorResult("10002", "洗车单完善信息保存异常");
            return errorResult;
        }
    }
}
