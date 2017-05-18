package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.cube.shop.RpcCustomerAnalysisService;
import com.tqmall.cube.shop.param.marketing.analysis.LevelAnalysisParam;
import com.tqmall.cube.shop.result.marketing.analysis.CustomerAnalysisDTO;
import com.tqmall.legend.biz.marketing.ng.CustomerLevelAnalysisService;
import com.tqmall.legend.biz.marketing.ng.adaptor.CustomerInfoConverter;
import com.tqmall.legend.biz.marketing.ng.adaptor.PagedParamConverter;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by wanghui on 2/25/16.
 */
@Service
@Slf4j
public class CustomerLevelAnalysisServiceImpl implements CustomerLevelAnalysisService {

    @Autowired
    private RpcCustomerAnalysisService rpcCustomerAnalysisService;


    @Override
    public Page<CustomerInfo> getCustomerWithTime(final Map<String, Object> params, final Pageable pageable) {
        return new BizTemplate<Page<CustomerInfo>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<CustomerInfo> process() throws BizException {
                LevelAnalysisParam param = new LevelAnalysisParam();
                param.setShopId((Long) params.get("shopId"));
                PagedParamConverter.convert(pageable, param);
                String sTime = (String) params.get("sTime");
                if (Langs.isNotBlank(sTime)) {
                    param.setsTime(sTime);
                }
                String eTime = (String) params.get("eTime");
                if (Langs.isNotBlank(eTime)) {
                    param.seteTime(eTime);
                }
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = null;
                Integer carLevelTag = (Integer) params.get("carLevelTag");
                if (carLevelTag == null) {
                    return null;
                }
                if (carLevelTag == 1) {
                    result = rpcCustomerAnalysisService.getLevelLowCustomer(param);
                } else if (carLevelTag == 2) {
                    result = rpcCustomerAnalysisService.getLevelMiddleCustomer(param);
                } else if (carLevelTag == 3) {
                    result = rpcCustomerAnalysisService.getLevelHighCustomer(param);
                }
                if (result == null || !result.isSuccess()) {
                    logger.error("查询级别客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询级别客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

}
