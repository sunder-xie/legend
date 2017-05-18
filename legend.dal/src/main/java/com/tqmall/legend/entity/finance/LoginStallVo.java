package com.tqmall.legend.entity.finance;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by changqiang.ke on 16/3/2.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginStallVo {
    public static final String OPT_YUNXIU_LOGIN = "OPT_YUNXIU_LOGIN";
    public static final String OPT_YUNXIU_PURCHASE = "OPT_YUNXIU_PURCHASE";
    public static final String OPT_YUNXIU_THHH = "OPT_YUNXIU_THHH";
    public static final String OPT_YUNXIU_BOUTIQUE = "OPT_YUNXIU_BOUTIQUE";
    public static final String OPT_YUNXIU_WORKCLOTHES = "OPT_YUNXIU_WORKCLOTHES";
    public static final String OPT_SPRAY = "OPT_SPRAY";//钣喷免登陆
    public static final String OPT_YUNXIU_FREEZE = "OPT_YUNXIU_FREEZE";//防冻液免登陆
    public static final String OPT_YUNXIU_OILFILTER = "OPT_YUNXIU_OILFILTER";//机油滤清
    public static final String OPT_YUNXIU_SINGLES_DAY = "OPT_YUNXIU_SINGLES_DAY";//双11活动
    public static final String OPT_YUNXIU_COMMERCIAL = "OPT_YUNXIU_COMMERCIAL";//商用车
    public static final String OPT_YUNXIU_PASSENGER = "OPT_YUNXIU_PASSENGER";//乘用车
    public static final String OPT_YUNXIU_BIGGIFTBAG = "OPT_YUNXIU_BIGGIFTBAG";//安心回家


    private String mobile;//门店联系手机号
    private Integer payType;//支付方式，29：现金，31：货到付款
    private Integer purchaseId;//清单id
    private Long cityId;//城市id
    //免登录的操作类型 采购清单：OPT_YUNXIU_PURCHASE，退换货申请单：OPT_YUNXIU_THHH，淘汽精品OPT_YUNXIU_BOUTIQUE,云修工服OPT_YUNXIU_WORKCLOTHES,钣喷中心下单OPT_SPRAY
    private String optType;
    //退换货sn
    private String exchangeOrderSn;

}
