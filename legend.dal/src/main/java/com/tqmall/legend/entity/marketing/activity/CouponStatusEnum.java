package com.tqmall.legend.entity.marketing.activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 15/11/27.
 * 优惠公告状态枚举类型
 */
public enum CouponStatusEnum {

    //0草稿1提交待审核2审核通过待发布3审核未通过4已发布
    /**
     * 门店优惠公告状态枚举类型
     */
    DRAFT("草稿", 0),
    SUBMIT("提交待审核", 1),
    AUDIT_NOT_RELEASE("审核通过待发布", 2),
    AUDIT_NOT_PASS("审核未通过", 3),
    RELEASED("通过已发布", 4);

    // 成员变量
    private String name;

    private Integer index;

    CouponStatusEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    /**
     * 获取所有status除了index状态
     */
    public static List<Integer> getStatusExceptIndex(Integer index) {
        List<Integer> statusList = new ArrayList<>();
        for (CouponStatusEnum statusEnum : CouponStatusEnum.values()) {
            int status = statusEnum.getIndex();
            if (status != index) {
                statusList.add(status);
            }
        }
        return statusList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
