package com.tqmall.legend.entity.sys;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class ExportTrack extends BaseEntity {

    // 门店ID
    private Long shopId;
    private Integer operatorId;//操作人id
    private String operatorName;//操作姓名
    private String operateBrief;//操作内容简介
    private String gmtCreateStr;

    public String getGmtCreateStr() {

        if(gmtCreate != null){
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(gmtCreate);
        }else if (gmtCreate != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return f.format(gmtCreate);
        } else {
            return null;
        }
    }

}

