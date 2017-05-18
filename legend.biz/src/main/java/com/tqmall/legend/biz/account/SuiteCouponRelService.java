package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.SuiteCouponRel;

import java.util.List;
import java.util.Map;

public interface SuiteCouponRelService {

    public Integer insert(SuiteCouponRel suiteCouponRel);

    public List<SuiteCouponRel> select(Map param);

    List<SuiteCouponRel> findBySuitId(Long shopId, Long suitId);
}
