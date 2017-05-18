package com.tqmall.legend.biz.marketing.ng.adaptor;

import com.tqmall.cube.shop.result.marketing.accurate.CustomerAccurateDTO;
import com.tqmall.cube.shop.result.marketing.analysis.CustomerAnalysisDTO;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.wheel.helper.BeanMapper;
import com.tqmall.wheel.support.data.DefaultPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

/**
 * Created by xin on 2017/3/25.
 */
public class CustomerInfoConverter {

    public static Page<CustomerInfo> convertAnalysisPage(DefaultPage<CustomerAnalysisDTO> defaultPage) {
        if (defaultPage == null) {
            return null;
        }
        int totalNum = defaultPage.getTotalNum();
        List<CustomerAnalysisDTO> records = defaultPage.getRecords();
        if (records == null) {
            records = Collections.emptyList();
        }
        List<CustomerInfo> customerInfos = BeanMapper.mapListIfPossible(records, CustomerInfo.class);
        PageRequest pageRequest = new PageRequest(defaultPage.getCurrentPageNum() - 1, defaultPage.getPageSize());
        com.tqmall.legend.biz.common.DefaultPage<CustomerInfo> customerInfoDefaultPage = new com.tqmall.legend.biz.common.DefaultPage<>(customerInfos, pageRequest, totalNum);
        return customerInfoDefaultPage;
    }

    public static Page<CustomerInfo> convertAccuratePage(DefaultPage<CustomerAccurateDTO> defaultPage) {
        if (defaultPage == null) {
            return null;
        }
        int totalNum = defaultPage.getTotalNum();
        List<CustomerAccurateDTO> records = defaultPage.getRecords();
        if (records == null) {
            records = Collections.emptyList();
        }
        List<CustomerInfo> customerInfos = BeanMapper.mapListIfPossible(records, CustomerInfo.class);
        PageRequest pageRequest = new PageRequest(defaultPage.getCurrentPageNum() - 1, defaultPage.getPageSize());
        com.tqmall.legend.biz.common.DefaultPage<CustomerInfo> customerInfoDefaultPage = new com.tqmall.legend.biz.common.DefaultPage<>(customerInfos, pageRequest, totalNum);
        return customerInfoDefaultPage;
    }
}
