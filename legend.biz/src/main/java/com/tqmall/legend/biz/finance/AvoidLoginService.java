package com.tqmall.legend.biz.finance;

import com.tqmall.legend.biz.finance.vo.LoginEpcVo;
import com.tqmall.legend.entity.finance.LoginStallVo;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by changqiang.ke on 16/3/29.
 */
public interface AvoidLoginService {
    /**
     * 云修免登录到电商
     *
     * @param response
     * @param shopId
     * @param loginStallVo
     */
    public void avoidLoginStall(HttpServletResponse response, Long shopId, LoginStallVo loginStallVo);

    /**设置登录信息的城市站
     *
     * @param shopId
     * @param cityId 门店城市站
     * @return
     */
    public Long getCityId(Long shopId, Long cityId);

    /**
     *免登陆到汽配管家
     */
     String avoidLoginEpc( Long shopId , LoginEpcVo loginEpcVo);

    String avoidLoginStall( Long shopId, LoginStallVo loginStallVo);
}
