package com.tqmall.legend.service.tqcheck;

import com.tqmall.legend.object.param.tqcheck.TqCheckAddParam;
import com.tqmall.legend.object.result.tqcheck.TqCheckDetailListDTO;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.Map;

/**
 * Created by lifeilong on 2016/4/12.
 */
public interface RpcTqCheckService {
    /**
     * 新增或更新 客户车辆信息 和 检测信息
     * @param tqCheckAddParam
     * @return
     */
    public Result addNewCustomerTqCheckMsg(TqCheckAddParam tqCheckAddParam);

    /**
     * 获取客户检测详情列表
     * @param shopId        店铺id
     * @param customerCarId 车辆id
     * @return
     */
    public Result<TqCheckDetailListDTO> getTqCheckDetailList(Long shopId, String carLicense, Long customerCarId, Long checkLogId);

    /**
     * 根据shopId 和 carId获取所有车辆检测记录
     */
    public Result getTqCheckLogList(Map<String , Object> params);
}
