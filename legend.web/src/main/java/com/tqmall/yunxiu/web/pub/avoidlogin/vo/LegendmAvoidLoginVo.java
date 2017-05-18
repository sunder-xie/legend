package com.tqmall.yunxiu.web.pub.avoidlogin.vo;

import lombok.Data;

/**
 * Created by zsy on 16/8/23.
 * legendm后台免登陆云修的对象
 */
@Data
public class LegendmAvoidLoginVo {
    private Long shopId;//门店id
    private String backName;//管理后台登录人
    private String loginTimeStr;//登录时间
}
