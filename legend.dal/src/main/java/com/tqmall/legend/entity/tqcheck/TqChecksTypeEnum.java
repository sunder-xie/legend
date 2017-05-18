package com.tqmall.legend.entity.tqcheck;

/**
 * Created by lifeilong on 2016/4/13.
 */
public enum TqChecksTypeEnum {
    ZCYX(1, "正常运行"),
    LYGC(2, "留意观察"),
    JYGH(3, "建议更换");

    private final Integer key;
    private final String name;

    private TqChecksTypeEnum(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey(){
        return key;
    }
    public String getName(){
        return name;
    }

    public static String getNameByKey(Integer key){
        TqChecksTypeEnum[] arr = values();
        int len = arr.length;
        for(int i=0;i<len;i++){
            TqChecksTypeEnum tEnum = arr[i];
            if(tEnum.getKey().equals(key)){
                return tEnum.getName();
            }
        }
        return null;
    }

}
