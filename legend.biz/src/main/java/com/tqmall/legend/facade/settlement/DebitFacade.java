package com.tqmall.legend.facade.settlement;

import com.tqmall.legend.billcenter.client.param.DebitFlowSearchParam;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.order.bo.DebitBillBo;
import com.tqmall.legend.biz.settlement.vo.BatchDebitVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillAndFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillFlowVo;
import com.tqmall.legend.biz.settlement.vo.DebitBillVo;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowBo;
import com.tqmall.legend.facade.settlement.bo.DebitBillFlowSaveBo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xin on 16/6/3.
 */
public interface DebitFacade {

    /**
     * 保存收款单
     *
     * @param debitBillVo
     */
    void saveBill(DebitBillVo debitBillVo);

    /**
     * 查询收款单和流水
     *
     * @param shopId
     * @param relId
     * @return
     */
    DebitBillAndFlowVo findDebitBillAndFlow(Long shopId, Long relId);

    /**
     * 保存收款流水
     *
     */
    void saveFlowList(DebitBillFlowSaveBo flowSaveBo);

    /**
     * 工单批量收款流水
     *
     * @param batchDebitVo
     */
    void batchSaveFlowList(BatchDebitVo batchDebitVo);

    /**
     * 坏账处理
     * @param orderId
     * @param remark
     */
    void badBill(Long shopId, Long orderId, Long userId, String remark);

    /**
     * 设置预付定金流水
     * @param receivableAmount
     * @param flowList
     * @param orderInfo
     * @return 预付定金的实际流水金额，null表示没有新的预付定金
     */
    BigDecimal setDownPaymentFlow(BigDecimal receivableAmount, List<DebitBillFlowBo> flowList,OrderInfo orderInfo);

    /**
     * 查询收款流水
     * @param param
     * @return
     */
    DefaultPage<DebitBillFlowVo> getDebitBillFlowPage(DebitFlowSearchParam param);
}
