package com.tqmall.legend.object.param.zhidao;

/**
 * Created by yoho_tail on 16/3/29.
 */
public enum RpcExpertStatusEnum {
    NO_ACTIVE(0,"未激活"),
    ACTIVE(1,"激活");

    private Integer code;
    private String name;

    RpcExpertStatusEnum(Integer code, String name) {
        this.code = code ;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
