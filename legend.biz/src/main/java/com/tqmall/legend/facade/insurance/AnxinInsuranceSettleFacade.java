package com.tqmall.legend.facade.insurance;

import com.tqmall.insurance.domain.param.insurance.InsuranceUserServicePackageParam;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.bo.ConsumeServiceBo;
import com.tqmall.legend.facade.insurance.vo.InsuranceUserServicePackageVo;
import org.springframework.data.domain.Pageable;

/**
 * Created by zsy on 16/9/19.
 * 安心保险服务券核销
 */
public interface AnxinInsuranceSettleFacade {

    /**
     * 服务券列表分页接口
     *
     * @param servicePackageParam
     * @return
     */
    DefaultPage<InsuranceUserServicePackageVo> getPage(Pageable pageable, InsuranceUserServicePackageParam servicePackageParam);

    /**
     * 获取服务券详情
     *
     * @param id
     * @return
     */
    InsuranceUserServicePackageVo getDetail(Integer id);

    /**
     * 服务券核销
     *
     * @param consumeServiceBo
     * @return
     */
    void consumeServiceItem(ConsumeServiceBo consumeServiceBo);
}
