package com.tqmall.legend.pojo.balance;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
public enum BalanceHandleStatusEnum {
    TXZ(3,"提现中"),
    TXCG(4,"提现成功"),
    TXSB(5,"提现失败"),
    HBDSH(0,"待审核"),
    HBYX(1,"有效"),
    HBWX(2,"无效");
    private Integer handleStatusType;
    private final String handleStatusName;

    private BalanceHandleStatusEnum(Integer handleStatusType, String handleStatusName) {
        this.handleStatusType = handleStatusType;
        this.handleStatusName = handleStatusName;
    }

    public Integer getHandleStatusType() {
        return handleStatusType;
    }

    public String getHandleStatusName(){
        return handleStatusName;
    }

    public static String getHandleStatusNameByType(Integer handleStatusType) {
        if (handleStatusType != null) {
            for (BalanceHandleStatusEnum op : BalanceHandleStatusEnum.values()) {
                if (op.getHandleStatusType().equals(handleStatusType)) {
                    return op.getHandleStatusName();
                }
            }
            return null;
        }
        return null;
    }
}
