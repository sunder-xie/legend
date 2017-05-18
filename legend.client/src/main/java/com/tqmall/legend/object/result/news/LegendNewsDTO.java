package com.tqmall.legend.object.result.news;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by feilong.li on 17/4/11.
 */
@Setter
@Getter
public class LegendNewsDTO implements Serializable{
    private static final long serialVersionUID = 1239571022114896106L;

    private Long id;
    private Date gmtCreate;     //创建时间
    private String newsTitle;   //资讯标题
    private String newsContent; //资讯内容
    private String newsSource;  //资讯来源
    private Long totalViewTimes;//总的浏览次数
    private Long totalUpvoteNum;    //总的点赞次数
    private String appTag;   //资讯打标，以，隔开，1热2广告3顶置
    private Integer showStyle;  //资讯展示样式,0纯文字(使用标题) 1文字+缩略图(使用标题+图片上传)
    private String imgThumbUrl; //缩略图，json串格式，存放缩略图url

}
