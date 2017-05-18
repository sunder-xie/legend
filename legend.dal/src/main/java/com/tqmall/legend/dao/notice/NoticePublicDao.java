package com.tqmall.legend.dao.notice;

import java.util.Map;

import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.entity.notice.NoticeEntity;
@MyBatisRepository
public interface NoticePublicDao  extends BaseDao<NoticeEntity>{

	public NoticeEntity getLastNotice(Map<String, Object> paramMap);
}
