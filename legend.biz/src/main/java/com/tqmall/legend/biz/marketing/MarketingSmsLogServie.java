package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingSmsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by dingbao on 15/6/12.
 */
public interface MarketingSmsLogServie {

    void insert(MarketingSmsLog marketingSmsLog);

    Page<MarketingSmsLog> getPage(Pageable pageable, Map<String, Object> searchParams);

    Long logConsume(Long shopId, Long userId, String operator, Integer usedCount, Integer position, String template);

    void updateSmsNum(Long smsLogId, int count, Long shopId);

    void deleteById(Long shopId, Long smsLogId);
}
