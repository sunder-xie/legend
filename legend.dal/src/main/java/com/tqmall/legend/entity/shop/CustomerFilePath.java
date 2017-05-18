package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by twg on 15/8/24.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerFilePath {
    private Long id;
    private String isDeleted;
    private Long creator;
    private Long modifier;
    private Long customerExtId;
    private Long customerId;
    private Long customerJoinAuditId;
    private Long nodeId;
    private String dataName;
    private String dataType;
    private String resolution;
    private String fileName;
    private Integer imgStatus;
    private String imgToken;
    private String imgUrl;
    private String isRecommendation;
    private String remarks;
    /**
     * 图片排序功能.
     */
    private Integer orderIdx;
}
