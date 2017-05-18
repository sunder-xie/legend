package com.tqmall.legend.biz.notice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tqmall.common.Constants;
import com.tqmall.core.utils.JedisPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.notice.NoticeService;
import com.tqmall.legend.dao.notice.NoticePublicDao;
import com.tqmall.legend.entity.notice.NoticeEntity;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticePublicDao noticeDao;

    @Override
    public DefaultPage<NoticeEntity> getNoticeByPage(Pageable pageable, Map<String, Object> paramMap) {
        PageRequest pageRequest = null;
        if (pageable != null) {
            pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
            paramMap.put("limit", pageRequest.getPageSize());
            paramMap.put("offset", pageRequest.getOffset());
        }

        paramMap.put("sorts", new String[] { "gmt_modified desc" });
        paramMap.put("publishStatus", 1);
        List<NoticeEntity> data = noticeDao.select(paramMap);
        Long totalCount = noticeDao.selectCount(paramMap).longValue();
        DefaultPage<NoticeEntity> page = new DefaultPage<NoticeEntity>(data, pageRequest, totalCount);
        return page;
    }

    @Override
    public NoticeEntity getLastPublicNotice(Long userId, String noticeTimeStr, Integer shopLevel) {
        Map<String, Object> param = new HashMap<>();
        if (null != noticeTimeStr) {
            param.put("lastNoticeTime", noticeTimeStr);
        }
        //取最后一条
        param.put("limit", 1);
        param.put("sorts", new String[] { "gmt_modified desc" });
        param.put("publishStatus",1);
        param.put("shopLevel",shopLevel);

        List<NoticeEntity> noticeEntities = noticeDao.select(param);

        if (!CollectionUtils.isEmpty(noticeEntities)) {
            NoticeEntity noticeEntity = noticeEntities.get(0);
            return noticeEntity;
        }
        return null;
    }

    @Override
    public NoticeEntity getLastNotice(Map<String, Object> paramMap) {
        return noticeDao.getLastNotice(paramMap);
    }

    private void putUserLastNoticeTime(Long userId, String noticeTime) {
        Jedis jedis;
        jedis = JedisPoolUtils.getMasterJedis();
        try {
            jedis.hset(Constants.PUBLIC_NOTICE_TIME, userId + "", noticeTime);
            jedis.expire(Constants.PUBLIC_NOTICE_TIME, Constants.NOTICE_CACHE_TIME);
        } catch (Exception e) {
            log.error("将用户的公告通知时间存入Redis e={}", e);
        } finally {
            JedisPoolUtils.returnMasterRes(jedis);
        }
    }

    private String getLastNoticeTime(Long userId) {
        String noticeTimeStr = null;
        Jedis jedis;
        jedis = JedisPoolUtils.getSlaveJedis();
        try {
            noticeTimeStr = jedis.hget(Constants.PUBLIC_NOTICE_TIME, userId + "");
        } catch (Exception e) {
            log.error("获取用户最后的公告通知时间失败 e={}", e);
        } finally {
            JedisPoolUtils.returnSlaveRes(jedis);
        }
        return noticeTimeStr;
    }

}
