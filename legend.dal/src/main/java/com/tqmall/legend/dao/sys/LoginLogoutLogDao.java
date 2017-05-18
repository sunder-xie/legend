package com.tqmall.legend.dao.sys;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.sys.LoginLogoutLog;
import com.tqmall.legend.entity.sys.WhiteAddress;

/**
 * 登录登出日志表
 */
@MyBatisRepository
public interface LoginLogoutLogDao extends BaseDao<LoginLogoutLog> {
}

