package com.tqmall.legend.biz.marketing;

import com.tqmall.legend.entity.marketing.MarketingSmsRechargeTpl;
import com.tqmall.legend.entity.shop.SmsPayLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15/6/10.
 */
public interface MarketingSmsRechargeTplService {

    public List<MarketingSmsRechargeTpl> select(Map map);

    public MarketingSmsRechargeTpl  selectById(Long id);

    /**
     * 查询充值信息
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<SmsPayLog> getPage(Pageable pageable, Map<String, Object> searchParams);
}
