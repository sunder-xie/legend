package com.tqmall.legend.biz.article;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.article.News;
import org.springframework.data.domain.Pageable;

import java.util.*;

/**
 * Created by sven on 16/7/22.
 */
public interface NewsService {
    /**
     * 分页
     *
     * @return
     */
    DefaultPage<News> getPage(Map<String, Object> param, Pageable pageable);

    List<News> select(Map<String, Object> param);

    int selectCount(Map<String, Object> param);

    News selectById(Long id);

    /**
     * 查询首页云修资讯
     *
     * @return
     */
    List<News> selectHomeNews(int size);

    long addViewTimes(Long id);
    void batchUpdateViewTimes();

    long addAppPv(Long id);

    int updateById(News news);
}
