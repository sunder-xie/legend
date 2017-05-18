package com.tqmall.legend.facade.smart;

import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.insurance.domain.result.smart.SmartConsumeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartShopOrderDTO;
import org.springframework.data.domain.Pageable;

/**
 * Created by zwb on 16/12/21.
 */
public interface BihuSmartViewFacade {
    /**
     * 获取门店充值信息 剩余次数
     * @param agentId
     * @return
     */
    SmartShopOrderDTO getShopInfo(Integer agentId);


    /**
     * 分页获取充值记录
     * @param agentId
     * @param start
     * @param pageSize
     * @return
     */
    PagingResult<SmartRechargeRecordDTO>  getRechargeRecordPage(Integer agentId, Integer start, Integer pageSize);

    /**
     * 获取消费记录分页数据
     * @param page
     * @param shopId
     * @return
     */
    PagingResult<SmartConsumeRecordDTO> getConsumeRecordPageList(Pageable page, Integer shopId);

}
