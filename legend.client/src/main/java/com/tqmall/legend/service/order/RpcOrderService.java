package com.tqmall.legend.service.order;


import com.tqmall.legend.object.param.order.QryOrderParam;
import com.tqmall.legend.object.result.order.WechatOrderDTO;
import com.tqmall.zenith.errorcode.support.Result;

import java.util.List;

/**
 * Created by lixiao on 16/2/29.
 */
public interface RpcOrderService {

    public Result<List<Long>> getServiceTplIdsByOrderId(String source, Long orderId);

    /**
     * 对外的工单列表查询接口,包含工单关联的服务(类目),物料信息. 目前使用的系统有:
     *  <li>1.ddl-wechat</li>
     * @param qryOrderParam
     * @return
     */
    com.tqmall.core.common.entity.Result<List<WechatOrderDTO>> qryOrderList(QryOrderParam qryOrderParam);
}
