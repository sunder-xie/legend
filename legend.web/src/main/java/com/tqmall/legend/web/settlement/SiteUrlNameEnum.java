package com.tqmall.legend.web.settlement;

/**
 * Created by xin on 16/6/12.
 */
public enum SiteUrlNameEnum {

    SAVE_DEBIT_BILL(SiteUrls.SAVE_DEBIT_BILL, "保存收款单"),
    ORDER_DEBIT_POST(SiteUrls.ORDER_DEBIT_POST, "工单收款");

    private final String url;
    private final String urlName;

    private SiteUrlNameEnum(String url, String urlName) {
        this.url = url;
        this.urlName = urlName;
    }

    public static String getUrlName(String url) {
        for (SiteUrlNameEnum e : SiteUrlNameEnum.values()) {
            if (e.getUrl().equals(url)) {
                return e.getUrlName();
            }
        }
        return "";
    }

    public String getUrl() {
        return url;
    }

    public String getUrlName() {
        return urlName;
    }
}
