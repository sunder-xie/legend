package com.tqmall.legend.biz.bo.order;

import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by lixiao on 15/11/12.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WashCarBo {
    //APP版本号
    protected String ver;
    //数据来源 1.Android 2.ios
    protected String refer;
    //手机系统
    protected String sys;
    //token用户信息
    protected String token;
    //签名信息
    protected String sign;
    //设备id
    protected String deviceId;
    //手机品牌
    protected String phoneBrand;
    //工作网络
    protected String networkType;
    private Long shopId;
    private List<ShopServiceInfo> shopServiceInfoList;
}
