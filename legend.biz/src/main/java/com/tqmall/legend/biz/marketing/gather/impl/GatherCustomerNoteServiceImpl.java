package com.tqmall.legend.biz.marketing.gather.impl;

import com.tqmall.legend.biz.marketing.gather.GatherCustomerNoteService;
import com.tqmall.legend.dao.marketing.gather.GatherCustomerNoteDao;
import com.tqmall.legend.entity.marketing.gather.GatherCustomerNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xin on 2016/12/17.
 */
@Service
public class GatherCustomerNoteServiceImpl implements GatherCustomerNoteService {

    @Autowired
    private GatherCustomerNoteDao gatherCustomerNoteDao;

    @Override
    public GatherCustomerNote save(GatherCustomerNote gatherCustomerNote) {
        gatherCustomerNoteDao.insert(gatherCustomerNote);
        return gatherCustomerNote;
    }
}
