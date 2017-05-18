package com.tqmall.legend.entity.order;

/**
 * 编号类型
 */
public enum SerialTypeEnum {
    PRECHECK("YJ", "预检单"),
    IN_BLUEBILL("LR", "蓝字入库"),
    IN_REDBILL("HR", "红字入库"),
    ONLINE_PAY("POP", "在线支付"),//POP:Product,TOP:Test,SOP:Stable add by wanghui 201606013支付迁移
    FLOW_ID("FL", "流水单号"),
    ORDER_SETTLE("JS", "结算单号");

    private final String code;
    private final String sName;

    private SerialTypeEnum(String code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public String getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(String code) {
        for(SerialTypeEnum e : SerialTypeEnum.values()){
            if(e.getCode().equals(code)){
                return e.getsName();
            }
        }
        return null;
    }
}
