package com.tqmall.legend.entity.question;

/**
 * Created by macx on 16/4/1.
 */
public enum ExpertBonusStatusEnum {
    WTX(0,"未提现"),
    YTX(1,"提现成功"),
    TXZ(2,"提现中"),
    TXSB(3,"提现失败");


    private Integer key;
    private final String value;

    private ExpertBonusStatusEnum(Integer key, String value) {
        this.key = key ;
        this.value = value;
    }

    public static String getValByKey(Integer key){
        ExpertBonusStatusEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            ExpertBonusStatusEnum iEnum = arr[i];
            if(iEnum.getKey().equals(key)){
                return iEnum.getValue();
            }
        }
        return null;
    }

    public Integer getKey(){
        return key;
    }
    public String getValue() {
        return value;
    }
}
