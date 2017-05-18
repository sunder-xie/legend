package com.tqmall.legend.web.order;

/**
 * url description enum
 */
public enum SiteUrlNameEnum {

    ADD(SiteUrls.ADD, "进入工单新增"),
    SAVE(SiteUrls.SAVE, "工单保存"),
    EDIT(SiteUrls.EDIT, "进入工单编辑"),
    UPDATE(SiteUrls.UPDATE, "工单更新"),
    DETAIL(SiteUrls.DETAIL, "进入综合维修单详情页"),
    VIRTUALEDIT(SiteUrls.VIRTUALEDIT, "进入虚子工单新增/编辑"),
    VIRTUALUPDATE(SiteUrls.VIRTUALUPDATE, "虚子工单工单更新"),
    CARWASH(SiteUrls.CARWASH, "进入洗车工单"),
    CARWASHSAVE(SiteUrls.CARWASHSAVE, "洗车工单保存"),
    CARWASHPERFECT(SiteUrls.CARWASHPERFECT, "进入洗车单客户信息完善页面"),
    CARWASH_CUSTOMERCOMPLETION(SiteUrls.CARWASHPERFECTSAVE, "洗车单客户信息完善保存"),
    SPEEDILY(SiteUrls.SPEEDILY, "进入快速工单"),
    SPEEDILYSAVE(SiteUrls.SPEEDILYSAVE, "快速工单保存"),
    SPEEDILYDETAIL(SiteUrls.SPEEDILYDETAIL, "进入快修快保单详情页"),
    SELLGOOD(SiteUrls.SELLGOOD, "进入物料销售单"),
    SELLGOODDETAIL(SiteUrls.SELLGOODDETAIL, "进入销售单详情页");


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
