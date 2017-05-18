package com.tqmall.legend.enums.base;

/**
 * Created by wushuai on 16/4/11.
 */
public enum ModuleUrlEnum {
    NEWHOME("newHome", "新生态首页"),
    HOME("home","首页"),
    NEWS("news","云修资讯"),
    PRODUCTS("products","云修产品"),
    RECEPTION("reception","接车维修"),
    WAREHOUSE("warehouse","仓库"),
    SETTLEMENT("settlement","结算"),
    BUY("buy","淘汽采购"),
    MARKETING("marketing","客户营销"),
    ACTIVITY("activity","引流活动"),
    WECHAT("wechat","微信公众号"),
    OTHER("other","更多"),
    SETTINGS("settings","设置");

    private final String moduleUrl;
    private final String moduleName;

    private ModuleUrlEnum(String moduleUrl, String moduleName) {
        this.moduleUrl = moduleUrl;
        this.moduleName = moduleName;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public String getModuleName() {
        return moduleName;
    }
}
