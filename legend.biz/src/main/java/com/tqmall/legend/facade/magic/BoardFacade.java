package com.tqmall.legend.facade.magic;

import java.util.List;

/**
 * 业务场景: 看板
 * 创建人: macx
 * 创建时间: 16/7/27.
 * 最后修改时间: 16/7/27.
 * 修改记录:
 */
public interface BoardFacade {
    /**
     * 发送socket消息(结算)
     * @param shopId
     * @param orderIds
     */
    void sendMessage(Long shopId, List<Long> orderIds);

    /**
     * 发送socket消息(扫描,中断,施工单新建)
     * @param shopId
     * @param workOrderSn
     * @param workOrderId
     */
    void sendMessage(Long shopId,String workOrderSn,Long workOrderId);

    /**
     * 发送socket消息(喷漆状态修改)
     * @param shopId
     * @param workOrderId
     */
    void sendMessage(Long shopId, Long workOrderId);
}
