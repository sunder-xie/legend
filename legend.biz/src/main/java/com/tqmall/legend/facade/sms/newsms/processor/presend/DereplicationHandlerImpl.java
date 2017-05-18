package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.shop.CustomerInfoService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class DereplicationHandlerImpl implements DereplicationHandler {
    @Autowired
    private CustomerInfoService customerInfoService;
    @Override
    public List<TemplateData> dereplicate(List<Long> carIds, Long shopId) {
        Map<String, TemplateData> mobile2TemplateDataMap = Maps.newHashMap();
        Pager pager = new Pager(100, carIds.size());
        int pages = pager.getPages();
        for (int i = 1; i <= pages; i++) {
            int startIndex = pager.getStartIndex(i);
            int endIndex = pager.getEndIndex(i);
            List<Long> subCarIds = carIds.subList(startIndex, endIndex);
            processPage(shopId,subCarIds, mobile2TemplateDataMap);
        }
        List<TemplateData> templateDataList = Lists.newArrayList(mobile2TemplateDataMap.values());
        return templateDataList;
    }

    private void processPage(Long shopId, List<Long> carIds, Map<String, TemplateData> mobile2TemplateDataMap) {
        List<CustomerInfo> customerInfoList = customerInfoService.listByCarIds(shopId, carIds);
        Iterator<CustomerInfo> iterator = customerInfoList.iterator();
        while (iterator.hasNext()) {
            CustomerInfo customerInfo = iterator.next();
            if (customerInfo == null) {
                continue;
            }
            String mobile = customerInfo.getMobile();
            if (!StringUtil.isMobileNO(mobile)) {
               continue;
            }
            if (mobile2TemplateDataMap.containsKey(mobile)) {
                TemplateData templateData = mobile2TemplateDataMap.get(mobile);
                String licenses = templateData.getLicenses();
                licenses += "," + customerInfo.getCarLicense();
                templateData.setLicenses(licenses);
            } else {
                TemplateData templateData = new TemplateData();
                templateData.setMobile(mobile);
                templateData.setCustomerName(customerInfo.getCustomerName());
                templateData.setLicenses(customerInfo.getCarLicense());
                mobile2TemplateDataMap.put(mobile, templateData);
            }
        }
    }

}