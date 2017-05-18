package com.tqmall.legend.facade.marketing.gather.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.param.marketing.gather.GatherEffectParam;
import com.tqmall.cube.shop.provider.marketing.gather.RpcGatherStatService;
import com.tqmall.cube.shop.result.DefaultResult;
import com.tqmall.cube.shop.result.marketing.gather.GatherEffectDetailDTO;
import com.tqmall.cube.shop.result.marketing.gather.GatherOperateEffectStatDTO;
import com.tqmall.cube.shop.result.marketing.gather.PerformanceStatDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.marketing.gather.GatherDetailFacade;
import com.tqmall.legend.facade.marketing.gather.adaptor.GatherEffectDetailConvertor;
import com.tqmall.legend.facade.marketing.gather.adaptor.GatherOperateStatConvertor;
import com.tqmall.legend.facade.marketing.gather.adaptor.PerformanceStatConvertor;
import com.tqmall.legend.facade.marketing.gather.vo.GatherEffectDetailVO;
import com.tqmall.legend.facade.marketing.gather.vo.GatherOperateStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PerformanceStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2016/12/22.
 */
@Service
public class GatherDetailFacadeImpl implements GatherDetailFacade {

    @Autowired
    private RpcGatherStatService rpcGatherStatService;

    @Override
    public Page<GatherEffectDetailVO> getGatherEffectDetailPage(final Long shopId, final Long userId, final String dateStr, final Pageable pageable) {

        return new BizTemplate<Page<GatherEffectDetailVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0);
                Assert.hasText(dateStr);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<GatherEffectDetailVO> process() throws BizException {
                GatherEffectParam param = new GatherEffectParam();
                param.setShopId(shopId);
                param.setUserId(userId);
                param.setDateStr(dateStr);
                param.setPageNum(pageable.getPageNumber());
                param.setPageSize(pageable.getPageSize());
                Result<DefaultResult<GatherEffectDetailDTO>> result = rpcGatherStatService.getGatherEffectDetailPage(param);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("查询集合详情列表失败");
                }
                DefaultResult<GatherEffectDetailDTO> data = result.getData();
                Page<GatherEffectDetailVO> page = new DefaultPage<>(Collections.<GatherEffectDetailVO>emptyList());
                if (data == null) {
                    return page;
                }
                int total = data.getTotal();
                List<GatherEffectDetailDTO> content = data.getContent();
                PageRequest pageRequest = new PageRequest(pageable.getPageNumber() - 1, pageable.getPageSize());
                return new DefaultPage<>(GatherEffectDetailConvertor.convertList(content), pageRequest, total);
            }
        }.execute();
    }

    @Override
    public GatherOperateStatVO getGatherOperateStat(final Long shopId, final Long userId, final String dateStr) {

        return new BizTemplate<GatherOperateStatVO>() {
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
            protected GatherOperateStatVO process() throws BizException {
                Result<GatherOperateEffectStatDTO> result = rpcGatherStatService.getGatherOperateStat(shopId, userId, dateStr);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("查询集客操作统计失败");
                }
                return GatherOperateStatConvertor.convert(result.getData());
            }
        }.execute();
    }

    @Override
    public PerformanceStatVO getPerformanceStat(final Long shopId, final Long userId, final String dateStr) {
        return new BizTemplate<PerformanceStatVO>() {
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
            protected PerformanceStatVO process() throws BizException {
                Result<PerformanceStatDTO> result = rpcGatherStatService.getPerformanceStat(shopId, userId, dateStr);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("查询业绩提成统计失败");
                }
                return PerformanceStatConvertor.convert(result.getData());
            }
        }.execute();
    }

    @Override
    public List<GatherEffectDetailVO> getGatherDetailList(final Long shopId, final Long userId, final String dateStr, final int pageNum, final int pageSize) {
        return new BizTemplate<List<GatherEffectDetailVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(shopId != null && shopId > 0);
                Assert.hasText(dateStr);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<GatherEffectDetailVO> process() throws BizException {
                GatherEffectParam param = new GatherEffectParam();
                param.setShopId(shopId);
                param.setUserId(userId);
                param.setDateStr(dateStr);
                param.setPageNum(pageNum);
                param.setPageSize(pageSize);
                Result<DefaultResult<GatherEffectDetailDTO>> result = rpcGatherStatService.getGatherEffectDetailPage(param);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("查询集合详情列表失败");
                }
                DefaultResult<GatherEffectDetailDTO> data = result.getData();
                if (data == null) {
                    return Collections.emptyList();
                }
                return GatherEffectDetailConvertor.convertList(data.getContent());
            }
        }.execute();
    }
}
