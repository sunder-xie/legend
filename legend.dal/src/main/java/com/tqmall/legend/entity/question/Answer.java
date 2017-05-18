package com.tqmall.legend.entity.question;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Answer extends BaseEntity {

    private Long shopId;
    private String content;
    private String contentImage;
    private String contentAudio;
    private String contentVideo;
    private Long userId;
    private String nickName;
    private int isBest;
    private String contentType;
    private Long questionId;
    private Long parentId;
    private String tag;
    private String shopName;
    private String refer;
    private String ver;
    private Integer audioSize;

}

