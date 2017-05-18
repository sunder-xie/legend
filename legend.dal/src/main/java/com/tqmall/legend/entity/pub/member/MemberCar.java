package com.tqmall.legend.entity.pub.member;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by jason on 15-07-09.
 * 我的车库VO对象
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class MemberCar {

    //车牌
    private String license;
    //品牌
    private String carBrandName;
    //车型
    private String carSeriesName;
    //厂家
    private String carCompany;
    //进出口
    private String importInfo;
    //公里数
    private Long mileage;
    //上次保养时间
    private Date latestMaintain;

    private String latestMaintainStr;
    public String getLatestMaintainStr() {
        return DateUtil.convertDate1(latestMaintain);
    }






}
