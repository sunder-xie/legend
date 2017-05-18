package com.tqmall.legend.server.account.converter;

import com.google.common.collect.Lists;
import com.tqmall.legend.entity.account.AccountSuite;
import com.tqmall.legend.object.result.account.CouponSuiteDTO;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Created by majian on 16/9/13.
 */
public class SuiteListConverter implements Converter<List<AccountSuite>,List<CouponSuiteDTO>> {

    @Override
    public List<CouponSuiteDTO> convert(List<AccountSuite> source) {
        List<CouponSuiteDTO> destination = Lists.newArrayList();
        if (source == null) {
            return destination;
        }
        for (AccountSuite suite : source) {
            CouponSuiteDTO dto = new SuiteConverter().convert(suite);
            destination.add(dto);
        }
        return destination;
    }
}
