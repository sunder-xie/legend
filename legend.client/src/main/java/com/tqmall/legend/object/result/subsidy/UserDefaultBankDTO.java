package com.tqmall.legend.object.result.subsidy;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/2/26.
 */
@Data
public class UserDefaultBankDTO implements Serializable {
    private static final long serialVersionUID = 4937224963673182952L;
    private String userName;          //用户姓名
    private String account;           //提现账户
    private Long accountId;           //账户ID
    private Integer accountType;      //账户类型
    private String accountBank;       //银行账户开户行
    private String bank;              //银行卡开户总行
    private String bankProvince;      //银行卡省份
    private String bankCity;          //银行卡城市
    private String bankDistrict;      //银行卡区县
    private Integer bankProvinceId; //开户省份id
    private Integer bankCityId;    //开户城市id
    private Integer bankDistrictId;//开户区县id
}
