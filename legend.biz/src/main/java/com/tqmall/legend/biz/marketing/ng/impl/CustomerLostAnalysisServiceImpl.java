package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.cube.shop.RpcCustomerAnalysisService;
import com.tqmall.cube.shop.param.marketing.analysis.LostAnalysisParam;
import com.tqmall.cube.shop.result.marketing.analysis.CustomerAnalysisDTO;
import com.tqmall.legend.biz.marketing.ng.CustomerLostAnalysisService;
import com.tqmall.legend.biz.marketing.ng.adaptor.CustomerInfoConverter;
import com.tqmall.legend.biz.marketing.ng.adaptor.PagedParamConverter;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by wanghui on 2/25/16.
 */
@Service
@Slf4j
public class CustomerLostAnalysisServiceImpl implements CustomerLostAnalysisService{

    @Autowired
    private RpcCustomerAnalysisService rpcCustomerAnalysisService;

    @Override
    public Page<CustomerInfo> getLostHighCustomer(final Long shopId, final Pageable pageable) {
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
                LostAnalysisParam param = new LostAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getLostHighCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询高等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getLostMiddleCustomer(final Long shopId, final Pageable pageable) {
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
                LostAnalysisParam param = new LostAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getLostMiddleCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询中等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询中等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getLostLowCustomer(final Long shopId, final Pageable pageable) {
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
                LostAnalysisParam param = new LostAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getLostLowCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询低等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询低等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }
}
