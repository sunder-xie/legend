package com.tqmall.legend.biz.account;


import com.tqmall.legend.entity.account.AccountSuite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AccountSuiteRelService {

    Page<AccountSuite> getPage(Pageable pageable, Map<String, Object> param);
}
