package com.tqmall.legend.facade.report.convert.account;

import com.tqmall.cube.shop.result.account.BusinessMemberInfoVO;
import com.tqmall.legend.facade.report.vo.MonthMemberStripVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tanghao on 16/9/22.
 */
public class BusinessMonthMemberStripConvert implements Converter<BusinessMemberInfoVO,MonthMemberStripVO> {
    @Override
    public MonthMemberStripVO convert(BusinessMemberInfoVO businessMonthMemberInfoVO) {
        MonthMemberStripVO monthMemberStripVO = new MonthMemberStripVO();
        BeanUtils.copyProperties(businessMonthMemberInfoVO,monthMemberStripVO);
        return monthMemberStripVO;
    }
}
