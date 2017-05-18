package com.tqmall.legend.service.common;

import com.tqmall.core.common.entity.Result;

/**
 * Created by zsy on 16/11/21.
 * 编号统一生产接口
 */
public interface RpcSnService {
    String SERVICE = "FW";//服务编号前缀
    String GOODS = "PJ";//配件编号前缀

    /**
     * 获取各种编号接口
     *
     * @param type 编号类型
     * @return
     */
    Result<String> generateSn(String type, Long shopId);
}
