package com.tqmall.legend.biz.service.shopnote;

import java.util.Date;

/**
 * Created by yuchengdu on 16/8/4.
 */
public interface AuditingNoteService {
    void processAuditingNoteInfo(Long shopId, Date currentTime);
}
