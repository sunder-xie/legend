package com.tqmall.legend.facade.settlement;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.billcenter.client.dto.PayBillDTO;
import com.tqmall.legend.billcenter.client.param.PayParam;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.settlement.vo.PayBillVo;
import com.tqmall.legend.biz.settlement.vo.PayResultVo;
import com.tqmall.legend.entity.settlement.WarehouseInPayment;
import com.tqmall.legend.entity.shop.SupplierSettlementVO;
import com.tqmall.legend.entity.warehousein.WarehouseIn;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/6/8.
 */
public interface PayFacade {
    /**
     * 批量付款
     * @param warehouseInList
     * @param payBillDTOList
     * @param payBillVo
     * @param userInfo
     */
    void  batchUpdate(List<WarehouseIn> warehouseInList, List<PayBillDTO> payBillDTOList, PayBillVo payBillVo, UserInfo userInfo);

    List<WarehouseInPayment> getPaidSupplierPaymentLog(Map<String, Object> params);

    /**
     * 获取总计金额
     * @param searchParams
     * @return
     */
    List<SupplierSettlementVO> getSupplierAomountList(Map<String, Object> searchParams);

    /**
     * 保存付款单和流水至结算中心
     * @param payBillVo
     * @param userInfo
     */
    Long saveBill(PayBillVo payBillVo,UserInfo userInfo);

    DefaultPage<PayResultVo> getPayFlowPage(PayParam payParam);

}
