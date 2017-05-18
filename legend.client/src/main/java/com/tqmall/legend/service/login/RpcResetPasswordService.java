package com.tqmall.legend.service.login;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.login.ResetPasswordParam;
import com.tqmall.legend.object.result.shop.ShopManagerDTO;

/**
 * Created by macx on 2017/2/8.
 */
public interface RpcResetPasswordService {

    /**
     * 校验登录账号
     * @param account
     * @return
     */
    Result<ShopManagerDTO> validateAccount(String account);

    /**
     * 发送验证码
     * @param mobile
     * @return
     */
    Result<ShopManagerDTO> sendValidateCode(String mobile);

    /**
     * 验证码校验
     * @param code 验证码
     * @param userId 用户id
     * @return
     */
    Result<Boolean> loginValidateCode(String code, Long userId);

    /**
     * 重置密码
     * @param resetPasswordParam
     * @return
     */
    Result<Boolean> resetPassword(ResetPasswordParam resetPasswordParam);
}
