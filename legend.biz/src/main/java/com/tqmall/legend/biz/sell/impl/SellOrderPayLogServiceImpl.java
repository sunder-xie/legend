package com.tqmall.legend.biz.sell.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sell.SellOrderPayLogService;
import com.tqmall.legend.dao.sell.SellOrderPayLogDao;
import com.tqmall.legend.entity.sell.SellOrderPayLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiangdong.qu on 17/2/23 09:32.
 */
@Service
@Slf4j
public class SellOrderPayLogServiceImpl extends BaseServiceImpl implements SellOrderPayLogService {

    @Autowired
    private SellOrderPayLogDao sellOrderPayLogDao;

    /**
     * 新建记录
     *
     * @param sellOrderPayLog
     *
     * @return
     */
    @Override
    public SellOrderPayLog insertLog(SellOrderPayLog sellOrderPayLog) {
        sellOrderPayLogDao.insert(sellOrderPayLog);
        return sellOrderPayLog;
    }
}
