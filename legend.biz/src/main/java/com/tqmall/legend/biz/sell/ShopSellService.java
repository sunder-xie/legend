package com.tqmall.legend.biz.sell;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by feilong.li on 17/2/22.
 */
public interface ShopSellService {
    /**
     * 检查用户是否是B账户
     * @param mobile
     * @return
     */
    public Boolean checkMobileIsBAccount(String mobile);

    /**
     * 检查用户是否已开通系统
     * @param mobile
     * @return
     */
    public Boolean checkMobileIsExistShop(String mobile);

    /**
     * 保存手机号到redis
     * @param response
     * @param mobile
     */
    public void saveMobileInRedis(HttpServletResponse response , String mobile);

    /**
     * 从redis获取缓存的手机号
     * @param request
     * @return
     */
    public String getMobileFromRedis(HttpServletRequest request);
}
