package com.tqmall.legend.facade.shop;

import com.tqmall.common.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 灰度发布
 * Created by sven on 2017/1/10.
 */

public interface GrayReleaseFacade {
    /**
     * 将门店模块版本放到初始化到缓存中
     *
     * @param shopId
     */
    void initRelease(Long shopId);

    /**
     * 从缓存在获取,放到request中
     *
     * @param request
     */
    void getRelease(HttpServletRequest request, Long shopId);

    /**
     * 切换版本
     *
     * @param version
     * @param confKey
     * @param userInfo
     * @return
     */
    boolean switchVersion(String version, String confKey, UserInfo userInfo);
}
