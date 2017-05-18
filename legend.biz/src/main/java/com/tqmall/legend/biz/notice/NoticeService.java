package com.tqmall.legend.biz.notice;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.notice.NoticeEntity;

public interface NoticeService {

    /**
     *
     * */
    public DefaultPage<NoticeEntity> getNoticeByPage(Pageable pageable, Map<String, Object> param);

    public NoticeEntity getLastPublicNotice(Long userId, String noticeTime, Integer shopLevel);

    public NoticeEntity getLastNotice(Map<String, Object> param);

}
