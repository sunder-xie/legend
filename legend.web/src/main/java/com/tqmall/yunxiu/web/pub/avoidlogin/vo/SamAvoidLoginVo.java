package com.tqmall.yunxiu.web.pub.avoidlogin.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by zsy on 16/8/23.
 * sam后台免登陆云修的对象
 */
@Data
public class SamAvoidLoginVo {
    private String userGlobalId;//ucShopId
    private String backName;//sam后台登录人
    private String loginTimeStr;//登录时间
}
