package com.tqmall.legend.biz.marketing.ng;


import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

/**
 * Created by wjc on 3/11/2016.
 */
public interface MarketingCenterService {
    Page<CustomerInfo> selectAccurate(Map<String,Object> params,Pageable pageable);
}
