package com.tqmall.legend.service.appoint;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.appoint.*;
import com.tqmall.legend.object.result.appoint.AppointDTO;
import com.tqmall.legend.object.result.appoint.AppointDetailDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;

import java.util.List;

/**
 * Created by zsy on 16/1/12.
 */
public interface RpcAppointService {
    /**
     * 预约单发消息，目前场景：后台下推预约单，给消息中心发消息
     */
    public Result pushMsg(String type, AppointParam appointParam);


    /**
     * 查询客户的待处理的预约单列表(待确认,已确认状态,提醒设置按店铺来)
     *
     * @param shopId
     * @param customerCarId
     * @param page
     * @param size
     *
     * @return
     */
    public Result<PageEntityDTO<AppointDTO>> getCustomerAppointList(Long shopId, Long customerCarId, Integer page, Integer size);

    /**
     * 查询店铺的有效预约单列表(待确认,已确认状态,提醒设置按店铺来)
     * mace用
     *
     * @param shopId 店铺id
     * @param page   页数
     * @param size   单页大小
     */
    public Result<PageEntityDTO<AppointDTO>> getShopAppointList(Long shopId, Integer page, Integer size);

    /**
     * 查询店铺的有效预约单计数(预约时间大于当前,待确认,已确认状态)
     * mace用
     *
     * @param shopId
     *
     * @return
     */
    public Result<Integer> getShopAppointCount(Long shopId);

    /**
     * 新增预约单
     * @param addAppointParam
     * @return
     */
    public Result<AppointDTO> addAppoint(AddAppointParam addAppointParam);

    /**
     * 查询门店指定预约时间范围内的预约单
     * @param appointDateParam
     * @return
     */
    public Result<List<AppointDTO>> getShopDateAppointList(AppointDateParam appointDateParam);

    /**
     * 取消预约单
     * @param cancelAppointParam
     * @return
     */
    Result<Boolean> cancelAppoint(CancelAppointParam cancelAppointParam);

    /**
     * 确认预约单
     * @param appointId
     * @param userGlobalId
     * @param userId
     * @return
     */
    Result<Boolean> confirmAppoint(Long appointId, Long userGlobalId, Long userId);

    /**
     * 分页查询预约单
     * @param searchAppointPageParam
     * @return
     */
    PagingResult<AppointDTO> getAppointPage(SearchAppointPageParam searchAppointPageParam);


    /**
     * chauxn预约单详细信息,包括服务列表,物料列表
     * @param appointId
     * @return
     */
    Result<AppointDetailDTO> getAppointDetail(Long appointId, Long userGlobalId);
}
