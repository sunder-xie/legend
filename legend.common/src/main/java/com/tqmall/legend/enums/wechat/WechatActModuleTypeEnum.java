package com.tqmall.legend.enums.wechat;

/**
 * Created by wushuai on 16/10/27.
 */
public enum WechatActModuleTypeEnum {
    SERVICE(1,"Service","预约服务"),
    GAME(2,"","游戏活动"),//游戏活动不是普通活动,没有moduleType
    DISCOUNT(3,"Discount","砍价活动"),
    GROUP_BUY(4,"GroupBuy","拼团活动"),
    DISCOUNT_COUPON(5,"DiscountCoupon","保险优惠券砍价活动");

    private final int value;
    private final String moduleType;//车主端对应的moduleType
    private final String typeName;
    private WechatActModuleTypeEnum(int value,String type, String typeName) {
        this.value = value;
        this.moduleType = type;
        this.typeName = typeName;
    }

    public String getModuleType(){
        return this.moduleType;
    }

    public int getValue() {
        return this.value;
    }


    public static WechatActModuleTypeEnum getValueByModuleType(String moduleType){
        if(moduleType==null){
            return null;
        }
        WechatActModuleTypeEnum[] enumValus = WechatActModuleTypeEnum.values();
        if(enumValus==null){
            return null;
        }
        for (WechatActModuleTypeEnum enumValu : enumValus) {
            if(enumValu.getModuleType().equals(moduleType)){
                return enumValu;
            }
        }
        return null;
    }

}
