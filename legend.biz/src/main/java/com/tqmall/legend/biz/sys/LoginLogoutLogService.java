package com.tqmall.legend.biz.sys;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.sys.LoginLogoutLog;
import com.tqmall.legend.entity.sys.WhiteAddress;
/**
 * 白名单相关代码
 */

/**
 * @author litan
 *         <p/>
 *         2014年7月22日
 */
public interface LoginLogoutLogService {

    /**
     * 添加登录登出日志
     * @param log
     * @return
     */
    public Result add(LoginLogoutLog log);
}
