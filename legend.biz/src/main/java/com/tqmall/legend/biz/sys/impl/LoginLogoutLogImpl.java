package com.tqmall.legend.biz.sys.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sys.LoginLogoutLogService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.sys.LoginLogoutLogDao;
import com.tqmall.legend.entity.sys.LoginLogoutLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录登出日志
 */
@Service
public class LoginLogoutLogImpl extends BaseServiceImpl implements LoginLogoutLogService {

    @Autowired
    private LoginLogoutLogDao loginLogoutLogDao;


    /**
     * 添加登录登出日志
     *
     * @param log
     * @return
     */
    @Override
    public Result add(LoginLogoutLog log) {
        if(loginLogoutLogDao.insert(log) > 0)
        {
           return Result.wrapSuccessfulResult(log);
        }
        return Result.wrapErrorResult("","添加登录登出日志异常！");
    }
}
