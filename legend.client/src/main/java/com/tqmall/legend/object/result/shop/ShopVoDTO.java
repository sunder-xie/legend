package com.tqmall.legend.object.result.shop;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by dingbao on 16/9/23.
 */
@Data
public class ShopVoDTO implements Serializable {
    private Date openTime;//开门时间
    private Date closeTime;//关门时间
    private String name;//门店名称
    private String contact;//联系人
    private String tel;//电话
    private String mobile;//手机号码
    private Long province;//省份
    private Long city;//市
    private Long district;//区
    private Long street;//街道
    private String address;//门店地址
    private Long crmCustomerId;//门店在crm系统中的id
    private String abbr;//门店缩写，用于门店人员账号前缀
    private String provinceName;
    private String cityName;
    private String districtName;
    private String streetName;
    private Integer level;//门店版本，6档口版,9云修版
    private String companyName;//法定公司名称
    private String hasMonitor;
    private Long changeCityId;//切换城市站ID
    private String changeCityName;//切换城市站Name
    private Integer initStatus;
    private String userGlobalId;
    private Long joinAuditId;
    private Integer shopStatus;
    private Integer joinStatus;//加入委托体系状态：0：未加入，1：加入
    private BigDecimal rate;//非股东结算比例
    private Integer workshopStatus;  //使用车间状态 0：不使用车间 1：使用车间
    private Integer agreementStatus;  //协议状态，0门店未同意协议，1同意协议，用于档口版门店首次登陆需要同意协议
    private Date expireTime;  //系统使用到期时间

    public Long getChangeCityId() {
        if (changeCityId == null || Long.valueOf(0).equals(changeCityId)) {
            return city;
        }
        return changeCityId;
    }

    public String getChangeCityName() {
        if (StringUtils.isEmpty(changeCityName)) {
            return cityName;
        }
        return changeCityName;
    }
}
