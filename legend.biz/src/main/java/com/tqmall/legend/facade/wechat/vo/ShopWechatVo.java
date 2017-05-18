package com.tqmall.legend.facade.wechat.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;

/**
 * Created by wushuai on 16/6/6.
 */
@Setter
@Getter
public class ShopWechatVo extends ShopDTO {
    private String onlineTimeStr;//上线时间
    private String signingTimeStr;//签约时间
    private String certificationTimeStr;//认证时间（注册成功时间）
    private String expirationTimeStr;//到期时间（上线时间加365天）
    private String submitTimeStr;//提交时间（用户第一次保存资料的时间，由云修传入）
    private String onlinePaytimeStr;//在线支付时间（由云修系统传入，且不能编辑）
    private String offlinePaytimeStr;//线下支付时间（由sam后台编辑）

    private Integer opSubmitType;

    public String getOnlineTimeStr() {
        if(getOnlineTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getOnlineTime());
    }

    public String getSigningTimeStr() {
        if(getSigningTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getSigningTime());
    }

    public String getCertificationTimeStr() {
        if(getCertificationTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getCertificationTime());
    }

    public String getExpirationTimeStr() {
        if(getExpirationTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMD(getExpirationTime());
    }

    public String getSubmitTimeStr() {
        if(getSubmitTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getSubmitTime());
    }

    public String getOnlinePaytimeStr() {
        if(getOnlinePaytime() == null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getOnlinePaytime());
    }

    public String getOfflinePaytimeStr() {
        if(getOfflinePaytime() == null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getOfflinePaytime());
    }
}
