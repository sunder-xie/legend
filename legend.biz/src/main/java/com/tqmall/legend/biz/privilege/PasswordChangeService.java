package com.tqmall.legend.biz.privilege;

import com.tqmall.legend.common.Result;

/**
 * Created by QXD on 2014/10/29.
 */
public interface PasswordChangeService {
    /**
     * 修改密码
     *
     * @param accountId   修改人id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    public Result changePassword(long accountId, String oldPassword, String newPassword,
        long shopId);

    /**
     * 重置密码
     *
     * @param id
     * @param newPassword
     * @return
     */
    public Result resetPassword(long id, String newPassword, Long shopId);
}
