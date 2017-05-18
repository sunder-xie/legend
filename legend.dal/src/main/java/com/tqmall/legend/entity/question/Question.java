package com.tqmall.legend.entity.question;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question extends BaseEntity {

    private Long shopId;
    private String content;
    private String contentImage;
    private String contentAudio;
    private String contentVideo;
    private Long userId;
    private String nickName;
    private Integer questionStatus;
    private String status;
    private Integer answerCount;
    private Integer readCount;
    private Long carBrandId;
    private String carBrand;
    private Long carSeriesId;
    private String carSeries;
    private String carCompany;
    private String importInfo;
    private String targetContent;
    private String userPhotoUrl;
    private String shopName;
    private String tag;
    private Integer audioSize;
    private String refer;
    private String ver;
    private String sys;
    private String deviceId;
    private String phoneBrand;
    private String networkType;
    private String unsolvedReason;
    private String faultContent;

}


