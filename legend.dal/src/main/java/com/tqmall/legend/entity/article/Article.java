package com.tqmall.legend.entity.article;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class Article extends BaseEntity {

    private String title;
    private String content;
    private String summery;
    private Integer status;
    private Long catId;
    private Long sort;
    private Integer type;
    private String mainImage;
    private String imageGallery;
    private Integer shopLevel;

    private List<GalleryImage> imageGalleryList;

    public List<GalleryImage> getImageGalleryList() {
        return new Gson().fromJson(imageGallery, new TypeToken<List<GalleryImage>>(){}.getType());
    }

}

