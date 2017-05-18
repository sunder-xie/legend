package com.tqmall.legend.web.portal;

import com.tqmall.legend.web.common.BaseController;

/**
 * 登录前首页BASE模块
 * Created by nawks on 3/12/15.
 */
public class PortalBaseController extends BaseController{
    private String pageName;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
