package com.tqmall.legend.service.order;

import com.tqmall.legend.object.param.order.CustomerFeedbackDTO;
import com.tqmall.legend.object.param.order.OrderSearchParam;
import com.tqmall.legend.object.param.order.SaveAndUpdateServiceWarnParam;
import com.tqmall.legend.object.param.order.speedily.CreateParam;
import com.tqmall.legend.object.param.service.WashCarServiceParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.customer.CustomerRecordDTO;
import com.tqmall.legend.object.result.order.OrderInfoDTO;
import com.tqmall.legend.object.result.order.OrderReceiverDTO;
import com.tqmall.legend.object.result.order.WorkerDTO;
import com.tqmall.legend.object.result.shop.ShopFeedBackDTO;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by lifeilong on 2016/3/15.
 * mace项目用
 */
public interface RpcAppOrderService {
    /**
     * 获取店铺服务顾问列表
     *
     * @param shopId
     *
     * @return
     */
    public Result<List<OrderReceiverDTO>> getOrderReceiverList(Long shopId);

    /**
     * 工单查询-从搜索获取数据
     *
     * @param orderSearchParam
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> searchOrderInfo(OrderSearchParam orderSearchParam);

    /**
     * 获取工单详情
     *
     * @param shopId  店铺id
     * @param orderId 工单id
     */
    public Result<OrderInfoDTO> getOrderInfoDetail(Long shopId, Long orderId);

    /**
     * 工单无效
     *
     * @param userId  操作人
     * @param orderId 工单id
     *
     * @return
     */
    public Result invalidOrder(Long orderId, Long userId);

    /**
     * 店铺待办工单数统计(预约单,待报价,待结算,待回访,已挂账)
     *
     * @param shopId
     *
     * @return
     */
    public Result<Map<String, Object>> shopOrderCount(Long shopId, String ver);

    /**
     * 车辆待办工单数统计(预约单,待报价,待结算,待回访)
     *
     * @param shopId
     *
     * @return
     */
    public Result<Map<String, Object>> customerOrderCount(Long shopId, Long customerCarId);


    /**
     * 获取店铺的待报价工单列表
     *
     * @param shopId
     * @param page
     * @param size
     * @param customerId
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> getShopDBJOrderList(Long customerId, Long shopId, Integer page, Integer size);

    /**
     * 获取店铺的未结算工单列表(包括cjdd状态的快修快保单)
     *
     * @param shopId
     * @param page
     * @param size
     * @param customerId
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> getShopWJSOrderList(Long customerId, Long shopId, Integer page, Integer size, String ver);

    /**
     * 获取店铺的待回访工单列表
     *
     * @param shopId
     * @param page
     * @param size
     * @param customerId
     *
     * @return
     */
    public Result<PageEntityDTO<ShopFeedBackDTO>> getShopDHFOrderList(Long customerId, Long shopId, Integer page, Integer size);

    /**
     * 获取客户的待处理工单(1:待报价 2：未结算  4:已挂账)
     * 注:3：'待回访' 改为 getCustomerDHFList 获取
     *
     * @param shopId        店铺id
     * @param page
     * @param size
     * @param customerCarId 车辆id
     * @param flag          1:待报价2:未结算  4:已挂账
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> getCustomerDCLOrderList(Integer flag, Long customerCarId, Long shopId, Integer page, Integer size, String ver);


    /**
     * 客户档案-维修工单-获取客户工单列表
     *
     * @param shopId
     * @param customerCarId
     * @param page
     * @param size
     *
     * @Since app3.0
     */
    public Result<CustomerRecordDTO> getCustomerOrderInfoToApp(Long shopId, Long customerCarId, Integer page, Integer size);

    /**
     * 客户档案-回访记录
     *
     * @param shopId        店铺id
     * @param customerCarId 车辆id
     * @param page
     * @param size
     *
     * @Since app3.0
     */
    public Result<PageEntityDTO<ShopFeedBackDTO>> getCustomerFeedbackToApp(Long shopId, Long customerCarId, Integer page, Integer size);

    /**
     * 店铺已挂账工单列表
     *
     * @param customerId
     * @param shopId
     * @param page
     * @param size
     *
     * @return
     */
    public Result<PageEntityDTO<OrderInfoDTO>> getShopYGZOrderList(Long customerId, Long shopId, Integer page, Integer size);

    /**
     * 创建回访单
     *
     * @param customerFeedbackDTO
     *
     * @return
     */
    Result<Boolean> saveCustomerFeedback(CustomerFeedbackDTO customerFeedbackDTO);

    /**
     * 获取客户待回访工单列表
     *
     * @param shopId
     * @param customerCarId
     * @param page
     * @param size
     *
     * @return
     */
    public Result<PageEntityDTO<ShopFeedBackDTO>> getCustomerDHFList(Long shopId, Long customerCarId, Integer page, Integer size);

    /**
     * 根据工单ID判断工单是否有预付定金
     *
     * @param orderId
     *
     * @return
     */
    Result<Boolean> hasDownPayment(Long orderId);

    /**
     * 根据门店id获取排序后的洗车工列表
     *
     * @param shopId
     *
     * @return
     */
    com.tqmall.core.common.entity.Result<List<WorkerDTO>> getCarWashWorkerList(Long shopId);

    /**
     * app创建快修快保单
     *
     * @param createParam
     *
     * @return
     */
    com.tqmall.core.common.entity.Result<Long> createSpeedilyOrder(CreateParam createParam);

    /**
     * app更新快修快保单
     *
     * @param createParam
     *
     * @return
     */
    com.tqmall.core.common.entity.Result<Long> updateSpeedilyOrder(CreateParam createParam);


    /**
     * app预检单 加入保养提醒表
     *
     * @param saveAndUpdateServiceWarnParam
     *
     * @return
     */
    com.tqmall.core.common.entity.Result<String> saveAndUpdateServiceWarns(SaveAndUpdateServiceWarnParam saveAndUpdateServiceWarnParam);

    /**
     * 保存洗车服务价格
     * @param washCarServiceParam
     * @return
     */
    com.tqmall.core.common.entity.Result<Boolean> saveWashCarService(WashCarServiceParam washCarServiceParam);
}
