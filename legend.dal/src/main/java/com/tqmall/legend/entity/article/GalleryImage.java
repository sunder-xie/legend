package com.tqmall.legend.entity.article;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2015-02-10 19:51
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GalleryImage {
    private String desc;
    private String url;
    private String shortUrl;
    private Boolean isMainImage;
}
