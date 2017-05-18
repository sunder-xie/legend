package com.tqmall.legend.biz.order;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.order.OrderInfoExt;

/**
 * 工单扩展表服务
 * Created by lixiao on 16/12/15.
 */
public interface OrderInfoExtService {

    int save(OrderInfoExt orderInfoExt , UserInfo userInfo);

}
