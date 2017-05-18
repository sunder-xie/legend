package com.tqmall.legend.biz.customer.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.dao.customer.CustomerCarFileDao;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CustomerCarFileServiceImpl extends BaseServiceImpl implements CustomerCarFileService {
    @Autowired
    private CustomerCarFileDao dao;

    @Override
    public Integer add(CustomerCarFile file) {
        if (log.isInfoEnabled()) {
            log.info("新上传文件:{}", file);
        }
        return this.dao.insert(file);
    }

    @Override
    public Integer update(CustomerCarFile file) {
        return this.dao.updateById(file);
    }

    @Override
    public List<CustomerCarFile> select(Map<String, Object> map) {
        return this.dao.select(map);
    }
}
