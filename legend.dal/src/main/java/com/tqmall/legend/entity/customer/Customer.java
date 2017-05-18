package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * Created by litan on 14-11-3.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Customer extends BaseEntity {

    private String customerName;
    private String mobile;

    private String tel;
    private Long shopId;
    private Long haodeUserId;
    private String drivingLicense;
    private String customerAddr;
    private Date birthday;
    private String company;
    private String identityCard;
    private Date licenseCheckDate;
    private Date licenseGetDate;
    private String contact;
    private String contactMobile;
    private String source;

    private String remark;
    // 导入的标识位,uuid
    private String importFlag;
    //判断是否存在
    private String oldMobile;

    /**
     * 一个客户对应的车辆list
     */
    private List<CustomerCar> customerCarList;
    /**
     * 版本号
     */
    private String ver;
    /**
     * 数据来源 0:web,1:android,2:ios
     */
    private String refer = "0";
    private String email;


    private String birthdayStr;
    public String getBirthdayStr() {
        if (null != birthday){
            return DateUtil.convertDateToYMD(birthday);
        }
        return null;
    }
}
