package com.tqmall.legend.biz.article;

import com.tqmall.legend.entity.article.Article;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/2/10.
 */
public interface ArticleService {

    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    public List<Article> select(Map<String,Object> searchParams);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Article selectById(Long id);

    public Article getFirstArticle(Integer type);
}
