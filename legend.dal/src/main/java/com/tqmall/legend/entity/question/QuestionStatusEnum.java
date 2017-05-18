package com.tqmall.legend.entity.question;

/**
 * Created by macx on 16/01/19.
 */
public enum QuestionStatusEnum {

    JXZ(1,"进行中"),
    YJJ(2,"已解决"),
    WJJ(3,"未解决"),
    GZFK(4,"故障反馈");


    private Integer key;
    private final String value;

    private QuestionStatusEnum(Integer key, String value) {
        this.key = key ;
        this.value = value;
    }

    public Integer getKey(){
        return key;
    }
    public String getValue() {
        return value;
    }
}
