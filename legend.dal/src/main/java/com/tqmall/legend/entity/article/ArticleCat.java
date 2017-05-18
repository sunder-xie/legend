package com.tqmall.legend.entity.article;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ArticleCat extends BaseEntity {

    private String title;
    private Long parentId;
    private Long sort;
    private Integer type;
    private Integer status;
    private Integer shopLevel;

    List<ArticleCat> childCatList;
    List<Article> articleList;
}

