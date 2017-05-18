package com.tqmall.legend.biz.service.shopnote;

import java.util.Date;

/**
 * Created by yuchengdu on 16/8/4.
 */
public interface AppointNoteService {
    void processAppointNoteInfo(Long shopId, Date currentTime);
}
