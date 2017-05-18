package com.tqmall.legend.facade.report.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.cube.shop.param.report.grossprofits.GrossProfitsParam;
import com.tqmall.cube.shop.provider.report.RpcGrossProfitsService;
import com.tqmall.cube.shop.result.report.grossprofits.GrossProfitsDTO;
import com.tqmall.cube.shop.result.report.grossprofits.GrossProfitsSummaryDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.report.GrossProfitsFacade;
import com.tqmall.legend.facade.report.bo.GrossProfitsBo;
import com.tqmall.legend.facade.report.bo.GrossProfitsSummaryBo;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 辉辉大侠
 * @Date 2017-04-13 5:58 PM
 * @Motto 一生伏首拜阳明
 */
@Slf4j
@Service
public class GrossProfitsFacadeImpl implements GrossProfitsFacade {
    @Autowired
    private RpcGrossProfitsService rpcGrossProfitsService;


    @Override
    public Page<GrossProfitsBo> getGrossProfitsList(Long shopId, String startDate, String endDate, Pageable page) {

        GrossProfitsParam param = new GrossProfitsParam();
        param.setShopId(shopId);
        param.setStartDate(startDate);
        param.setEndDate(endDate);
        param.setPageNum(page.getPageNumber());
        param.setPageSize(page.getPageSize());
        RpcResult<com.tqmall.wheel.support.data.Page<GrossProfitsDTO>> result = this.rpcGrossProfitsService.getGrossProfitsList(param);
        if (log.isInfoEnabled()) {
            log.info("获取毛利明细信息.Param-->shopId:{},startDate:{},endDate:{}, result:{}", shopId, startDate, endDate, LogUtils.objectToString(result));
        }
        if (result.isSuccess()) {
            com.tqmall.wheel.support.data.Page<GrossProfitsDTO> data = result.getData();
            List<GrossProfitsBo> grossProfitsBos = BeanMapper.mapListIfPossible(data.getRecords(), GrossProfitsBo.class);
            Page p = new DefaultPage(grossProfitsBos, page, data.getTotalNum());
            return p;
        } else {
            throw new BizException("获取数据异常.");
        }
    }

    @Override
    public GrossProfitsSummaryBo getGrossProfitsSummary(Long shopId, String startDate, String endDate) {
        RpcResult<GrossProfitsSummaryDTO> r = this.rpcGrossProfitsService.getGrossProfitsSummary(shopId, startDate, endDate);
        if (log.isInfoEnabled()) {
            log.info("获取毛利统计信息.Param-->shopId:{},startDate:{},endDate:{}, result:{}", shopId, startDate, endDate, LogUtils.objectToString(r));
        }
        if (r.isSuccess()) {
            GrossProfitsSummaryBo grossProfitsSummaryBo = new GrossProfitsSummaryBo();
            BeanUtils.copyProperties(r.getData(), grossProfitsSummaryBo);
            return grossProfitsSummaryBo;
        } else {
            throw new BizException("获取数据异常.");
        }
    }
}
