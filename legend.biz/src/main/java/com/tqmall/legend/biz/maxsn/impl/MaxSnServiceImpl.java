package com.tqmall.legend.biz.maxsn.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guangxue on 14/11/4.
 */
@Service
public class MaxSnServiceImpl extends BaseServiceImpl implements MaxSnService {

    @Override
    @Transactional
    public String getMaxSn(Long shopId, String snType) {
        if (null == shopId || 0 == shopId)
            return null;
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        String today = dateFormat.format(new Date());
        String latestSn = snType + today;
        return latestSn.toUpperCase();
    }
}
