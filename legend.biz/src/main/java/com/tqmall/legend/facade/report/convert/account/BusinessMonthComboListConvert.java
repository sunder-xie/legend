package com.tqmall.legend.facade.report.convert.account;

import com.google.common.collect.Lists;
import com.tqmall.cube.shop.result.account.BusinessMonthComboVO;
import com.tqmall.legend.facade.report.vo.MonthComboVO;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by tanghao on 16/9/22.
 */
public class BusinessMonthComboListConvert implements Converter<List<BusinessMonthComboVO>,List<MonthComboVO>> {
    @Override
    public List<MonthComboVO> convert(List<BusinessMonthComboVO> businessMonthComboVOs) {
        List<MonthComboVO> list = Lists.newArrayListWithCapacity(businessMonthComboVOs==null?0:businessMonthComboVOs.size());
;       for(BusinessMonthComboVO businessMonthComboVO : businessMonthComboVOs){
            MonthComboVO monthComboVO = new MonthComboVO();
            BeanUtils.copyProperties(businessMonthComboVO,monthComboVO);
            list.add(monthComboVO);
        }
        return list;
    }
}
