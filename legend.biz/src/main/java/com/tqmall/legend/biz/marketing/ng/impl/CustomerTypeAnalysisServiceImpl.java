package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.cube.shop.RpcCustomerAnalysisService;
import com.tqmall.cube.shop.param.marketing.analysis.ActivityAnalysisParam;
import com.tqmall.cube.shop.param.marketing.analysis.NewOldAnalysisParam;
import com.tqmall.cube.shop.result.marketing.analysis.CustomerAnalysisDTO;
import com.tqmall.legend.biz.marketing.ng.CustomerTypeAnalysisService;
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
public class CustomerTypeAnalysisServiceImpl implements CustomerTypeAnalysisService {
    @Autowired
    private RpcCustomerAnalysisService rpcCustomerAnalysisService;

    @Override
    public Page<CustomerInfo> getActiveCustomer(final Long shopId, final Pageable pageable) {
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
                ActivityAnalysisParam param = new ActivityAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getActivityActiveCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询高等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getSleepCustomer(final Long shopId, final Pageable pageable) {
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
                ActivityAnalysisParam param = new ActivityAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getActivitySleepCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询高等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getLostCustomer(final Long shopId, final Pageable pageable) {
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
                ActivityAnalysisParam param = new ActivityAnalysisParam();
                param.setShopId(shopId);
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getActivityLostCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询高等流失客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getNewCustomer(final Map<String, Object> params, final Pageable pageable) {
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
                NewOldAnalysisParam param = new NewOldAnalysisParam();
                param.setShopId((Long) params.get("shopId"));
                String sTime = (String) params.get("sTime");
                if (Langs.isNotBlank(sTime)) {
                    param.setsTime(sTime);
                }
                String eTime = (String) params.get("eTime");
                if (Langs.isNotBlank(eTime)) {
                    param.seteTime(eTime);
                }
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getNewCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询新客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

    @Override
    public Page<CustomerInfo> getOldCustomer(final Map<String, Object> params, final Pageable pageable) {
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
                NewOldAnalysisParam param = new NewOldAnalysisParam();
                param.setShopId((Long) params.get("shopId"));
                String sTime = (String) params.get("sTime");
                if (Langs.isNotBlank(sTime)) {
                    param.setsTime(sTime);
                }
                String eTime = (String) params.get("eTime");
                if (Langs.isNotBlank(eTime)) {
                    param.seteTime(eTime);
                }
                PagedParamConverter.convert(pageable, param);
                RpcResult<com.tqmall.wheel.support.data.DefaultPage<CustomerAnalysisDTO>> result = rpcCustomerAnalysisService.getOldCustomer(param);
                if (result == null || !result.isSuccess()) {
                    logger.error("查询老客户失败，参数: param = {}, 返回值: result = {}", JSONUtil.object2Json(param), JSONUtil.object2Json(result));
                    throw new BizException("查询高等流失客户失败");
                }
                return CustomerInfoConverter.convertAnalysisPage(result.getData());
            }
        }.execute();
    }

}
