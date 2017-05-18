package com.tqmall.legend.facade.insurance.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.coupon.LegendCouponParam;
import com.tqmall.insurance.domain.result.coupon.InsuranceCouponUserDTO;
import com.tqmall.insurance.domain.result.coupon.ShopCouponDetailDTO;
import com.tqmall.insurance.service.insurance.coupon.RpcInsuranceCouponService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.insurance.AnxinInsuranceCouponFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceCouponUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 2016/12/7.
 */
@Service
@Slf4j
public class AnxinInsuranceCouponFacadeImpl implements AnxinInsuranceCouponFacade {
    @Resource
    private RpcInsuranceCouponService rpcInsuranceCouponService;

    @Override
    public List<InsuranceCouponUserVo> getCouponList(Integer ucShopId, String mobile, BigDecimal amount) {
        log.info("[DUBBO] 获取优惠券列表接口,入参,门店ID:{},手机号:{},金额:{}", ucShopId, mobile, amount);
        Result<List<InsuranceCouponUserDTO>> result = rpcInsuranceCouponService.getUserCouponList(ucShopId, mobile, amount);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 获取优惠券列表失败,错误原因:{}", LogUtils.objectToString(result));
            return new ArrayList();
        }
        if (CollectionUtils.isEmpty(result.getData())) {
            return new ArrayList();
        }
        List<InsuranceCouponUserVo> voList = Lists.newArrayList();
        for (InsuranceCouponUserDTO dto : result.getData()) {
            InsuranceCouponUserVo vo = new InsuranceCouponUserVo();
            BeanUtils.copyProperties(dto, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public void checkcoupon(Integer ucShopId, Integer couponId, Integer mode, BigDecimal amount) {
        log.info("[DUBBO] 校验优惠券接口,入参,门店ID:{},优惠券id:{},模式:{},金额:{}", ucShopId, couponId, mode, amount);
        Result result = rpcInsuranceCouponService.checkCoupon(ucShopId, couponId, mode, amount);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 优惠券校验失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public void thawCoupon(Integer ucShopId, Integer couponId) {
        log.info("[DUBBO] 解冻优惠券接口,入参,门店ID:{},优惠券id:{}", ucShopId, couponId);
        Result result = rpcInsuranceCouponService.thawCoupon(ucShopId, couponId);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 解冻优惠券失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public void useCoupon(Integer ucShopId, Integer couponId, String orderSn, Integer mode) {
        log.info("[DUBBO] 校验优惠券接口,入参,门店ID:{},优惠券id:{},模式:{},订单号:{}", ucShopId, couponId, mode, orderSn);
        Result result = rpcInsuranceCouponService.freezeCoupon(ucShopId, couponId, orderSn, mode);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 优惠券校验失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public DefaultPage<InsuranceCouponUserVo> couponSettleList(LegendCouponParam param, Pageable pageable) {
        DefaultPage<InsuranceCouponUserVo> defaultPage = new DefaultPage(new ArrayList());
        log.info("[DUBBO] 获取优惠券统计接口,入参:{}", LogUtils.objectToString(param));
        pageTranslate(param, pageable);
        PagingResult<InsuranceCouponUserDTO> result = rpcInsuranceCouponService.getUsedCouponList(param);

        if (!result.isSuccess()) {
            log.error("[DUBBO] 优惠券校验失败,错误原因:{}", LogUtils.objectToString(result));
            return defaultPage;
        }
        if (CollectionUtils.isEmpty(result.getList())) {
            return defaultPage;
        }
        List<InsuranceCouponUserVo> voList = Lists.newArrayList();
        for (InsuranceCouponUserDTO insuranceCouponUserDTO : result.getList()) {
            InsuranceCouponUserVo vo = new InsuranceCouponUserVo();
            BeanUtils.copyProperties(insuranceCouponUserDTO, vo);
            voList.add(vo);
        }

        defaultPage = new DefaultPage<>(voList, pageable, result.getTotal());
        return defaultPage;
    }

    @Override
    public ShopCouponDetailDTO selectCouponStatistics(Integer ucShopId) {
        log.info("[DUBBO] 获取优惠券统计接口,入参,门店ID:{}", ucShopId);
        Result<ShopCouponDetailDTO> result = rpcInsuranceCouponService.getShopCouponDetail(ucShopId);
        if (!result.isSuccess()) {
            log.error("[DUBBO] 调用优惠券统计失败,错误原因:{}", LogUtils.objectToString(result));
            throw new BizException(result.getMessage());
        }
        return result.getData();
    }

    /**
     * 创建分页
     *
     * @param
     * @param pageable
     */
    private void pageTranslate(LegendCouponParam param, Pageable pageable) {
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize());
        param.setLimit(pageRequest.getPageSize());
        param.setStart(pageRequest.getOffset());
    }
}
