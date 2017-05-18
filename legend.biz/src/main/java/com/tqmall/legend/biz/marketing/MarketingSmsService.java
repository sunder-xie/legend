package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingSms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/8.
 */
public interface MarketingSmsService {

    Page<MarketingSms> getPage(Pageable pageable, Map<String, Object> searchParams);

    Integer insert(MarketingSms marketingSms);

    List<MarketingSms> select(Map<String,Object> params);

    MarketingSms selectById(Long id);

    void updateById(MarketingSms marketingSms);

    void batchInsert(List<MarketingSms> smsList);

    List<MarketingSms> listByLogId(Long shopId, Long smsLogId);

    List<MarketingSms> listSucessByLogId(Long shopId, Long smsLogId);
}
