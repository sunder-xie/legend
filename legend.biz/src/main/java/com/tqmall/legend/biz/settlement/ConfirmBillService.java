package com.tqmall.legend.biz.settlement;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.billcenter.client.dto.DebitBillDTO;
import com.tqmall.legend.biz.order.bo.ConfirmBillBo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by zsy on 16/6/4.
 * 确认账单
 */
public interface ConfirmBillService {
    /**
     * 确认账单接口
     *
     * @return
     */
    Result<DebitBillDTO> confirmBill(ConfirmBillBo confirmBillBo, OrderInfo orderInfo, UserInfo userInfo);
}
