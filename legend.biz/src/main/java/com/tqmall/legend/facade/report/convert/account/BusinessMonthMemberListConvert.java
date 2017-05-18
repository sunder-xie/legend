package com.tqmall.legend.facade.report.convert.account;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.account.BusinessMonthMemberVO;
import com.tqmall.legend.facade.report.vo.MonthMemberVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by tanghao on 16/9/22.
 */
public class BusinessMonthMemberListConvert implements Converter<List<BusinessMonthMemberVO>,List<MonthMemberVO>> {

    @Override
    public List<MonthMemberVO> convert(List<BusinessMonthMemberVO> businessMonthMemberVOs) {
        List<MonthMemberVO> list = Lists.newArrayListWithCapacity(businessMonthMemberVOs != null?businessMonthMemberVOs.size():0);
        for(BusinessMonthMemberVO businessMonthMemberVO : businessMonthMemberVOs){
            MonthMemberVO monthMemberVO = new MonthMemberVO();
            BeanUtils.copyProperties(businessMonthMemberVO,monthMemberVO);
            list.add(monthMemberVO);
        }
        return list;
    }
}
