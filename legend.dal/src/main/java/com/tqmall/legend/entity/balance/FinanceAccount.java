package com.tqmall.legend.entity.balance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FinanceAccount extends BaseEntity {

    private Long userId;//用户id
    private Long shopId;//店铺id
    private String accountUser;//账号持有人姓名
    private String account;//账户
    private String accountTag;//账号标签
    private String accountBank;//银行账号开户行
    private Integer accountType;//账号类型
    private String isDefault; // 是否是默认账号
    private String bank;    //开户总行
    private String bankProvince; //开户省份
    private String bankCity;    //开户城市
    private String bankDistrict;//开户区县
    private Integer bankProvinceId; //开户省份id
    private Integer bankCityId;    //开户城市id
    private Integer bankDistrictId;//开户区县id
    private Integer checkStatus;   //认证状态 0:待认证 1:认证成功 2:认证失败
    private String mobile;//手机号码
    private String identifyingCode;//临时字段：绑定银行卡的验证码
}


