package com.tqmall.legend.biz.article.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tqmall.legend.biz.article.ArticleCatService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.article.ArticleCatDao;
import com.tqmall.legend.dao.article.ArticleDao;
import com.tqmall.legend.entity.article.Article;
import com.tqmall.legend.entity.article.ArticleCat;
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
public class ArticleCatServiceImpl extends BaseServiceImpl implements ArticleCatService {
    @Autowired
    ArticleCatDao articleCatDao;

    @Autowired
    ArticleDao articleDao;

    @Override
    public List<ArticleCat> select(Map<String, Object> searchParams) {
        return super.select(articleCatDao, searchParams);
    }

    @Override
    public List<ArticleCat> getMenuList(int type, Integer shopLevel) {
        Map<String, Object> map = new HashMap<>();
        if (type == 0) {
            map.put("type", type);
        }
        if (shopLevel != null && type != 0) {
            map.put("shopLevel", shopLevel);
        }
        map.put("status", 1);
        List<String> sorts = new ArrayList<>();
        sorts.add("sort asc");
        map.put("sorts", sorts);
        List<ArticleCat> articleCatList = articleCatDao.select(map);
        List<Article> articleList = articleDao.select(map);
        //按照文章分类对文章进行分类
        Multimap<Long,Article> catIdArticleMap=Multimaps.index(articleList, new Function<Article, Long>() {
            @Override
            public Long apply(Article input) {
                return input.getCatId();
            }
        });
        //将文章封装进目录
        if (catIdArticleMap != null && catIdArticleMap.keySet().size()>0){
            for (ArticleCat articleCat:articleCatList){
                articleCat.setArticleList(Lists.newArrayList(catIdArticleMap.get(articleCat.getId())));
            }
        }
        //获取所有一级分类
        List<ArticleCat> menuList = Lists.newArrayList(Collections2.filter(articleCatList, new Predicate<ArticleCat>() {
            @Override
            public boolean apply(ArticleCat input) {
                return input.getParentId().longValue() == 0l;
            }
        }));
        //获取所有非一级分类
        List<ArticleCat> chlidArticleCatList = Lists.newArrayList(Collections2.filter(articleCatList, new Predicate<ArticleCat>() {
            @Override
            public boolean apply(ArticleCat input) {
                return input.getParentId().longValue() != 0l;
            }
        }));
        //key parentId,value ArticleCat 按照parentId对非一级分类进行分组
        Multimap<Long, ArticleCat> parentIdArticleCatMap = Multimaps.index(chlidArticleCatList, new Function<ArticleCat, Long>() {
            @Override
            public Long apply(ArticleCat input) {
                return input.getParentId();
            }
        });
        //封装子分类 将非一级分类封装进一级分类中
        for (ArticleCat articleCat : menuList) {
            if (parentIdArticleCatMap != null && parentIdArticleCatMap.get(articleCat.getId()) != null) {
                articleCat.setChildCatList(Lists.newArrayList(parentIdArticleCatMap.get(articleCat.getId())));
            }
        }

        return menuList;
    }

    /**
     * 根据文章id获取分类名称和id
     * @param id
     * @return
     */
    @Override
    public List<ArticleCat> getArticleCatTitleAndIdByArticleId(Long id) {
        List<ArticleCat> articleCatList = articleCatDao.select(null);
        Article article = articleDao.selectById(id);
        List<ArticleCat> articleCats = new ArrayList<>();
        for (ArticleCat articleCat : articleCatList) {
            if (article.getCatId().equals(articleCat.getId())) {
                //判断是否是主分类
                if (articleCat.getParentId().longValue() == 0L) {
                    articleCats.add(articleCat);
                    return articleCats;
                } else {
                    //存子分类
                    for (ArticleCat articleCat1 : articleCatList) {
                        if (articleCat.getParentId().equals(articleCat1.getId())) {
                            articleCats.add(articleCat1);
                        }
                    }
                    //再存主分类
                    articleCats.add(articleCat);
                    return articleCats;
                }
            }
        }
        return articleCats;
    }
}
