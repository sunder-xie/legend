package com.tqmall.legend.facade.report;

import com.tqmall.legend.facade.report.bo.GrossProfitsBo;
import com.tqmall.legend.facade.report.bo.GrossProfitsSummaryBo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author 辉辉大侠
 * @Date 2017-04-13 5:58 PM
 * @Motto 一生伏首拜阳明
 */
public interface GrossProfitsFacade {

    Page<GrossProfitsBo> getGrossProfitsList(Long shopId, String startDate, String endDate, Pageable page);

    GrossProfitsSummaryBo getGrossProfitsSummary(Long shopId, String startDate, String endDate);

}
