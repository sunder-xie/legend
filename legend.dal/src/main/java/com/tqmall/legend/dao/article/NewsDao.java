package com.tqmall.legend.dao.article;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.article.News;

@MyBatisRepository
public interface NewsDao extends BaseDao<News> {
    int updateViewTimes(News news);
}
