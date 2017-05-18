package com.tqmall.legend.facade.report.convert.account;

import com.tqmall.cube.shop.result.account.BusinessMonthComboInfoVO;
import com.tqmall.legend.facade.report.vo.MonthComboStripVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/9/22.
 */
public class BusinessMonthComboStripConvert implements Converter<BusinessMonthComboInfoVO,MonthComboStripVO> {
    @Override
    public MonthComboStripVO convert(BusinessMonthComboInfoVO businessMonthComboInfoVO) {
        MonthComboStripVO monthComboStripVO = new MonthComboStripVO();
        BeanUtils.copyProperties(businessMonthComboInfoVO,monthComboStripVO);
        return monthComboStripVO;
    }
}
