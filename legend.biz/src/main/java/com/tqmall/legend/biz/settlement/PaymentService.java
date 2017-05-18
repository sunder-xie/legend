package com.tqmall.legend.biz.settlement;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.settlement.Payment;

import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/12/26.
 */
public interface PaymentService {
    /**
     * @param shopId 店铺id
     * 根据shopId查询 店铺的付款方式
     * */
    public List<Payment> getPaymentsByShopId(long shopId);

    /**
     * @param id 付款方式
     * 根据付款方式id查询付款方式
     * */
    public Payment selectPaymentById(long id);

    /**
     * 检索结算方式
     * @param paramMap
     * @return
     */
    public List<Payment> searchPayments(Map<String, Object> paramMap);

    /**
     * Created by zsy on 2015/7/21.
     * 查询支付方式
     * @param searchParams
     * @return
     */
    public List<Payment> select(Map<String,Object> searchParams);

    /**
     * Created by zsy on 2015/7/21.
     * 批量插入支付方式
     * @param paymentList
     * @return
     */
    public Result batchInsertPayment(List<Payment> paymentList, UserInfo userInfo);

    /**
     * 更新支付方式
     * @param payment
     * @return
     */
    Integer update(Payment payment);
}
