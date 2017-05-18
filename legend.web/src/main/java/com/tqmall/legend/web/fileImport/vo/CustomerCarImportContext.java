package com.tqmall.legend.web.fileImport.vo;

import com.google.common.collect.Lists;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by twg on 16/12/10.
 */
@Getter
@Setter
public class CustomerCarImportContext extends CommonFileImportContext<CustomerCar> {
    private List<Customer> customers = Lists.newArrayList();
    private String uuid;
    private int hasMobileNum;//有联系方式个数
    private int notMobileNum;//无联系方式个数
}
