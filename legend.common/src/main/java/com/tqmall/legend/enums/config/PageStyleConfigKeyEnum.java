package com.tqmall.legend.enums.config;

/**
 * Created by wushuai on 16/4/6.
 */
public enum PageStyleConfigKeyEnum {

    LISTSTYLE("1","shop.listStyle", "列表风格"),//卡片还是列表
    APPOINT_TABLECOLUMNSET("2","shop.appoint.appoint-list.tableColumnSet","预约单列表列配置"),//设置列表哪些列需要展示
    ORDERLIST_TABLECOLUMNSET("3","shop.order.order-list.tableColumnSet","工单列表列配置"),
    CUSTOMER_TABLECOLUMNSET("4","shop.customer.list.tableColumnSet","客户列表列配置");

    private final String confKey;//ShopConfigure.confKey
    private final String localKey;//存于客户浏览器中localStorage.key
    private final String name;


    public static String getConFKeyByLocalKey(String localKey) {
        for (PageStyleConfigKeyEnum e : PageStyleConfigKeyEnum.values()) {
            if(e.getLocalKey().equals(localKey)){
                return e.getConfKey();
            }
        }
        return null;
    }

    private PageStyleConfigKeyEnum(String conKey, String localKey, String name) {
        this.confKey = conKey;
        this.localKey = localKey;
        this.name = name;
    }


    public String getConfKey() {
        return confKey;
    }

    public String getName() {
        return name;
    }

    public String getLocalKey() {
        return localKey;
    }
}
