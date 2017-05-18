package com.tqmall.legend.facade.security;

import com.tqmall.legend.entity.privilege.ShopManager;

/**
 * Created by yoho_tail on 2017/2/8.
 */
public interface ResetPasswordFacade {
    /**
     * 校验登录账户名
     * @param account 账户名
     * @return
     */
    ShopManager validateAccount(String account);

    /**
     * 发送验证码
     * @param mobile
     * @return
     */
    ShopManager sendValidateCode(String mobile);

    /**
     * 验证码校验
     * @param code
     * @param userId
     * @return
     */
    Boolean loginValidateCode(String code, Long userId);

    /**
     * 重置密码
     * @param userId 用户id
     * @param newPassword 新密码
     */
    void resetPassword(Long userId, String newPassword);
}
