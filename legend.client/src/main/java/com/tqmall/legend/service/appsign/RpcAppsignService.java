package com.tqmall.legend.service.appsign;


import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.appsign.SignTimeDTO;

/**
 * Created by zsy on 16/1/12.
 */
public interface RpcAppsignService {
    /**
     *查询上下班打卡时间
     * @param shopId
     * @return
     */
    Result<SignTimeDTO> getSigTime(Long shopId);

}
