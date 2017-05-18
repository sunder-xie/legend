package com.tqmall.legend.object.param.subsidy;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/2/26.
 * 用户默认提现账户设置
 */
@Data
@ToString
public class RpcUserDefaultACSetParam implements Serializable {
    private static final long serialVersionUID = -3296113532762660549L;

    private Long userId;           //用户id   必填
    private Long shopId;           //店铺id   必填
    private String userName;       //提现账号用户名
    private String passWord;       //用户密码  必填
    private Long accountId;        //提现账号Id
    private String account;        //账户卡号
    private String accountBank;    //银行账号开户行
    private String bank;           //开户银行
    private String bankProvince;   //开户行省份
    private String bankCity;        //开户行市
    private String bankDistrict;    //开户区县
    private Integer bankProvinceId; //开户省份id
    private Integer bankCityId;    //开户城市id
    private Integer bankDistrictId;//开户区县id
    private Integer accountType;    //账户类型 1:银行卡 2:支付宝
}
