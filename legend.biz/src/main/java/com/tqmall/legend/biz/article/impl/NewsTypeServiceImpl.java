package com.tqmall.legend.biz.article.impl;

import com.tqmall.legend.biz.article.NewsTypeService;
import com.tqmall.legend.dao.article.NewsTypeDao;
import com.tqmall.legend.entity.article.NewsType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/7/24.
 */
@Service
public class NewsTypeServiceImpl implements NewsTypeService {
    @Resource
    private NewsTypeDao newsTypeDao;

    @Override
    public List<NewsType> select(Map<String, Object> param) {
        return newsTypeDao.select(param);
    }
}
