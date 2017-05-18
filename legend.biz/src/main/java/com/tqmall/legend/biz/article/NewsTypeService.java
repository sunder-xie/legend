package com.tqmall.legend.biz.article;

import com.tqmall.legend.entity.article.NewsType;

import java.util.List;
import java.util.Map;

/**
 * Created by sven on 16/7/24.
 */
public interface NewsTypeService {

    List<NewsType> select(Map<String, Object> param);
}
