package com.tqmall.legend.entity.article;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

/**
 * Created by sven on 16/7/22.
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class News extends BaseEntity {

    private String newsTitle;//资讯标题
    private String imgUrl;//图片url
    private String newsSummary;//摘要
    private String newsContent;//资讯内容
    private String newsSource;//资讯来源
    private Long newsTypeId;//资讯类型id
    private Integer isPublished;//是否发布
    private Integer isTop;//是否置顶
    private Integer newsSort;
    private Long viewTimes ;//pc浏览次数
    private String appTag;//资讯打标，以，隔开，1热2广告3顶置
    private Integer showStyle;//资讯展示样式,0纯文字(使用标题) 1文字+缩略图(使用标题+图片上传)
    private String imgThumbUrl;//缩略图，json串格式，存放缩略图url
    private Long appUpvoteCount;//app点赞次数
    private Long appPv;//app浏览次数
    private Date modifiedTime;//业务修改时间

    private String gmtCreateStr;
    private String gmtModifiedStr;
    public String getGmtCreateStr(){
        return DateUtil.convertDateToYMDHHmm(gmtCreate);
    }
    public String getGmtModifiedStr(){
        return DateUtil.convertDateToYMDHHmm(gmtModified);
    }
    public String getModifiedTimeStr(){
        return DateUtil.convertDateToYMDHHmm(modifiedTime);
    }
}

