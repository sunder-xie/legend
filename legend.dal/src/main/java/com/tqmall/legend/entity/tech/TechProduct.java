package com.tqmall.legend.entity.tech;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zsy on 2015/4/30.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TechProduct extends BaseEntity {

    private String name;
    private String tqmallUrl;
    private String pic;
    private Integer position;
    private Long sort;
    private Integer status;

    String gmtCreateStr;
    String gmtModifiedStr;
    String positionName;

    public String getGmtCreateStr(){
        return DateUtil.convertDateToYMDHMS(this.getGmtCreate());
    }

    public String getGmtModifiedStr(){
        return DateUtil.convertDateToYMDHMS(this.getGmtModified());
    }

    public String getPositionName(){
        return TechProductPositionEnum.getMesByCode(this.position);
    }
}

