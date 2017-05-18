package com.tqmall.legend.entity.marketing;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * Created by jason on 15/10/28.
 * 车主活动对象
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppActivity extends BaseEntity {

    private String actName;//活动名称
    private String imgUrl;//活动banner图
    private String detailImgUrl;//活动详情图
    private String codeImgUrl;//活动预发二维码url
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Integer actStatus;//活动状态，0为关闭，1为预发, 2为开启
    private String remark;//活动描述
    private List<AppActivityCity> actCityList;//活动对应城市list


}



