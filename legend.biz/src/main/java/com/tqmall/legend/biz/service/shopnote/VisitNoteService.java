package com.tqmall.legend.biz.service.shopnote;

import java.util.Date;

/**
 * Created by yuchengdu on 16/8/4.
 */
public interface VisitNoteService {
    void processVisitNoteInfo(Long shopId, Date currentTime);
}
