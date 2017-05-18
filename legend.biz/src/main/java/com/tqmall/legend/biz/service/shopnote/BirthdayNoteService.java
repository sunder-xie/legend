package com.tqmall.legend.biz.service.shopnote;

import java.util.Date;

/**
 * Created by yuchengdu on 16/8/4.
 */
public interface BirthdayNoteService {
    void processBirthdayNoteInfo(final Long shopId, final Date current_time);
}
