package com.tqmall.legend.biz.article.impl;

import com.tqmall.legend.biz.article.ArticleService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.article.ArticleDao;
import com.tqmall.legend.entity.article.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/2/10.
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl implements ArticleService {

    @Autowired
    ArticleDao articleDao;

    @Override
    public List<Article> select(Map<String, Object> searchParams) {
        return super.select(articleDao,searchParams);
    }

    @Override
    public Article selectById(Long id) {
        Article article = articleDao.selectById(id);
        return article;
    }

    @Override
    public Article getFirstArticle(Integer type) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("type", type);
        searchParams.put("status", 1);
        searchParams.put("offset", 0);
        searchParams.put("limit", 1);
        List<String> sorts = new ArrayList<>();
        sorts.add("id asc");
        searchParams.put("sorts", sorts);
        List<Article> articleList = articleDao.select(searchParams);
        Article article = articleList.get(0);
        return article;
    }
}
