package com.tqmall.legend.facade.customer;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.customer.vo.CubeCustomerInfoVo;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by zsy on 16/12/19.
 */
public interface CubeCustomerInfoFacade {
    /**
     * 查询cube_customer_info
     *
     * @return
     */
    DefaultPage<CubeCustomerInfoVo> getCubeCustomerInfoFromSearch(Pageable pageable, Map<String, Object> searchParams);
}
