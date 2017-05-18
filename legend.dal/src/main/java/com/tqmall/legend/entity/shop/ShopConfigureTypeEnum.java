package com.tqmall.legend.entity.shop;

/**
 * 门店配置表 配置类型枚举
 */
public enum ShopConfigureTypeEnum {
    ORDERPRINT(0, "工单打印配置"),
    SETTLEPRINT(1, "结算打印配置"),
    APPOINTPRINT(2, "预约提醒设置"),
    HUIFANGPRINT(3, "回访提醒设置"),
    BAOXIANTIXING(4, "保险到期提醒设置"),
    NIANJIANTIXING(5, "年检到期提醒设置"),
    BAOYANGTIXING(6, "保养到期提醒设置"),
    BIRTHDAYTIXING(7, "生日到期提醒设置"),
    LOSTCUSTOMERTIXING(8, "客户流失到期提醒设置"),
    COMMUTETIME(9, "上下班时间设置"),
    REPORTDISPLAY(10, "报表字段显示"),
    SHOPPAGESTYLE(11,"门店页面样式配置"),
    SHOPCHOOSEVERSION(12,"门店新老版本切换"),
    SHOPMSGCONF(14,"消息推送设置"),
    SHOPSECURITYLEVEL(15,"门店安全级别设置"),
    PAYMENT(16,"支付方式设置"),
    EXPORTPASSWORD(20,"信息导出密码设置"),
    PRINT_VERSION(21,"打印版本配置"),
    PRINT_TITLE(23,"打印抬头"),
    USE_GUEST_ACCOUNT(24,"工单结算是否使用他人账户设置"),
    PRINT_POSTION(25,"打印位置"),
    PRINT_LOGO(26, "打印logo");

    private final int code;
    private final String sName;

    ShopConfigureTypeEnum(int code, String sName) {
        this.code = code;
        this.sName = sName;
    }

    public int getCode() {
        return this.code;
    }

    public String getsName() {
        return this.sName;
    }

    public static String getsNameByCode(int code) {
        for (ShopConfigureTypeEnum e : ShopConfigureTypeEnum.values()) {
            if (e.getCode() == code) {
                return e.getsName();
            }
        }
        return null;
    }

}
