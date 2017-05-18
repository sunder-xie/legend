package com.tqmall.legend.biz.service.shopnote;

import java.util.Date;

/**
 * Created by yuchengdu on 16/8/4.
 */
public interface LostCustomerNoteService {
    void processLostCustomerNoteInfo(final Long shopId, final Date current_time);
}
