package com.tqmall.legend.entity.notice;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class NoticeEntity extends BaseEntity {

    private String publisher;
    private String noticeTitle;
    private String noticeContent;
    private String publishStatus;
    private Integer sort;
    private Integer shopLevel;

    public String getPublishTime() {
        if(null != gmtModified){
            return DateUtil.convertDateToYMDHMS(gmtModified);
        }
        return null;
    }
}
