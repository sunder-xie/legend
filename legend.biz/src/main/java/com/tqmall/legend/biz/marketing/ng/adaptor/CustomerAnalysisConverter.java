package com.tqmall.legend.biz.marketing.ng.adaptor;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.biz.marketing.ng.bo.CustomerAnalysisBO;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 16/9/21.
 */
public class CustomerAnalysisConverter {

    public static CustomerAnalysisBO convert(CustomerInfo source) {
        if (source == null) {
            return null;
        }
        CustomerAnalysisBO customerAnalysisBO = new CustomerAnalysisBO();
        customerAnalysisBO.setCarLicense(source.getCarLicense());
        customerAnalysisBO.setCustomerName(source.getCustomerName());
        customerAnalysisBO.setMobile(source.getMobile());
        customerAnalysisBO.setCarModel(source.getCarModel());
        customerAnalysisBO.setLastPayTime(source.getLastPayTime());
        customerAnalysisBO.setTotalAmount(source.getTotalAmount());
        customerAnalysisBO.setTotalNumber(source.getTotalNumber());
        return customerAnalysisBO;
    }

    public static List<CustomerAnalysisBO> convertList(List<CustomerInfo> customerInfoList) {
        if (CollectionUtils.isEmpty(customerInfoList)) {
            return Collections.emptyList();
        }
        return Lists.transform(customerInfoList, new Function<CustomerInfo, CustomerAnalysisBO>() {
            @Override
            public CustomerAnalysisBO apply(CustomerInfo customerInfo) {
                return convert(customerInfo);
            }
        });
    }
}
