package com.tqmall.legend.biz.account.bo;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Locale;

/**
 * Created by twg on 17/2/28.
 */
@Getter
@Setter
public class AccountAndCarBO extends BaseBO {
    private String license;//车牌号
    private Long accountId;//账户id
    private Long customerId;//客户id
    private Long customerCarId;//客户车辆id
    private boolean change;//是否变更车主

    public String getLicense(){
        if (StringUtils.isNotBlank(license)){
            license = license.toUpperCase(Locale.SIMPLIFIED_CHINESE);
        }
        return license;
    }

}
