package com.tqmall.legend.object.result.shop;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wushuai on 16/8/9.
 */
@Data
public class QryShopDTO extends BaseEntityDTO {
    private static final long serialVersionUID = 4005641467903352295L;
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
    private Long level;//门店等级，9为直营店版，3为开放版
    private String companyName;//法定公司名称
    private String hasMonitor;
    private Long changeCityId;//切换城市站ID
    private String changeCityName;//切换城市站Name
    private Integer initStatus;
    private String userGlobalId;
    private Long joinAuditId;
    private Long shopStatus;
    private Integer joinStatus;//加入委托体系状态：0：未加入，1：加入
    private BigDecimal rate;//非股东结算比例
    private Integer workshopStatus;  //使用车间状态 0：不使用车间 1：使用车间

    private Long ucShopId;//userGlobalId的Long型值,ddl-wechat使用,

    public Long getUcShopId() {
        if(ucShopId!=null){
            return ucShopId;
        }
        if(userGlobalId!=null &&userGlobalId!="" && !userGlobalId.contains(" ")){
            return Long.parseLong(userGlobalId);
        }
        return null;
    }

}
