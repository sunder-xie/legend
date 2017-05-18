package com.tqmall.legend.facade.report.convert.account;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.account.BusinessMonthCouponVO;
import com.tqmall.legend.facade.report.vo.MonthCouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by tanghao on 16/9/22.
 */
public class BusinessMonthCouponListConvert implements Converter<List<BusinessMonthCouponVO>,List<MonthCouponVO>> {
    @Override
    public List<MonthCouponVO> convert(List<BusinessMonthCouponVO> businessMonthCouponVOs) {
        List<MonthCouponVO> list = Lists.newArrayListWithCapacity(businessMonthCouponVOs == null?0:businessMonthCouponVOs.size());
        for(BusinessMonthCouponVO businessMonthCouponVO : businessMonthCouponVOs){
            MonthCouponVO monthCouponVO = new MonthCouponVO();
            BeanUtils.copyProperties(businessMonthCouponVO,monthCouponVO);
            list.add(monthCouponVO);
        }
        return list;
    }
}
