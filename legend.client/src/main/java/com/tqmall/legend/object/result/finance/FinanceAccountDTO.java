package com.tqmall.legend.object.result.finance;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zsy on 16/9/17.
 * 门店用户财务账号
 */
@Data
public class FinanceAccountDTO implements Serializable {
    private static final long serialVersionUID = 9140013727706742110L;

    private String accountUser;          //收款人
    private String mobile;            //联系电话
    private String account;           //银行卡号
    private String bank;              //开户银行
    private String accountBank;       //开户支行
    private Integer bankProvinceId;   //开户省份id
    private String bankProvince;      //银行卡省份
    private Integer bankCityId;       //开户城市id
    private String bankCity;          //银行卡城市
    private Integer ucShopId;         //门店id
}
