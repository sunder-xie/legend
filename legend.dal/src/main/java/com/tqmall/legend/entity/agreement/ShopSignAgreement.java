package com.tqmall.legend.entity.agreement;

import lombok.Getter;
import lombok.Setter;
import com.tqmall.legend.entity.base.BaseEntity;

import java.util.Date;

@Getter
@Setter
/**
 * Created by litan on 17/2/9.
 */
public class ShopSignAgreement extends BaseEntity {

    private Date signTime;//协议签订时间
    private Long operateId;//签订人id
    private String operateName;//签订人姓名
    private Long shopId;//门店id
    private Long userGlobalId;//uc用户id
    private String agreementAddress;//协议文件地址

}
