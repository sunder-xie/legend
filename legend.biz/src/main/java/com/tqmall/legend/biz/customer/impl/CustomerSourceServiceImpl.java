package com.tqmall.legend.biz.customer.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.customer.CustomerSourceService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.CustomerSourceDao;
import com.tqmall.legend.entity.customer.CustomerSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2015/6/8.
 */
@Service
public class CustomerSourceServiceImpl implements CustomerSourceService {
    private static Logger logger = LoggerFactory.getLogger(CustomerSourceServiceImpl.class);

    @Autowired
    private CustomerSourceDao customerSourceDao;

    @Override
    public List<CustomerSource> getCustomerSourceList(Map<String, Object> param) {
        if (null == param || !param.containsKey("shopId")) {
            logger.error("参数错误");
            return new ArrayList<>();
        }
        List<CustomerSource> customerSourceList = null;
        try {
            customerSourceList = customerSourceDao.select(param);
        } catch (Exception e) {
            logger.error("DB操作错误", e);
        }

        if (CollectionUtils.isEmpty(customerSourceList)) {
            logger.info("获取数据为空");
            return new ArrayList<>();
        }

        return customerSourceList;
    }

    @Override
    public Result addCustomerSource(CustomerSource customerSource) {
        if (null == customerSource) {
            logger.error("参数错误");
            return Result.wrapErrorResult("", "参数错误");
        }
        try {
            if (customerSourceDao.insert(customerSource) > Constants.DB_DEFAULT_INT) {
                return Result.wrapSuccessfulResult("添加成功");
            }
        } catch (Exception e) {
            logger.error("DB操作错误");
        }
        return Result.wrapErrorResult("", "添加失败");
    }

    @Override
    public CustomerSource getCustomerSource(String source, long shopId) {
        if (StringUtils.isBlank(source) || shopId < 1) {
            return null;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("shopId", shopId);
        param.put("source", source);
        param.put("offset", 0);
        param.put("limit", 1);

        List<CustomerSource> customerSourceList = null;
        try {
            customerSourceList = customerSourceDao.select(param);
        } catch (Exception e) {
            logger.error("DB操作错误", e);
        }
        if (CollectionUtils.isEmpty(customerSourceList)) {
            return null;
        }
        return customerSourceList.get(0);
    }
}
