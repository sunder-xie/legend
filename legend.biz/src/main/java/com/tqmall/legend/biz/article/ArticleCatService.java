package com.tqmall.legend.biz.article;

import com.tqmall.legend.entity.article.ArticleCat;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/2/10.
 */
public interface ArticleCatService {
    /**
     * 条件查询
     * @param searchParams
     * @return
     */
    public List<ArticleCat> select(Map<String,Object> searchParams);

    /**
     * 获取菜单 包括分类信息也文章id,标题
     * @param type
     * @param shopLevel
     * @return
     */
    public List<ArticleCat> getMenuList(int type,Integer shopLevel);

    /**
     * 根据文章id获取分类名称和id
     * @param id
     * @return
     */
    public List<ArticleCat> getArticleCatTitleAndIdByArticleId(Long id);
}
