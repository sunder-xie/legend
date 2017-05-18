package com.tqmall.legend.server.login;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.security.ResetPasswordFacade;
import com.tqmall.legend.object.param.login.ResetPasswordParam;
import com.tqmall.legend.object.result.shop.ShopManagerDTO;
import com.tqmall.legend.service.login.RpcResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by macx on 2017/2/8.
 */
@Service("rpcResetPasswordService")
public class RpcResetPasswordServiceImpl implements RpcResetPasswordService {

    @Autowired
    private ResetPasswordFacade resetPasswordFacade;

    @Override
    public Result<ShopManagerDTO> validateAccount(final String account) {
        return new ApiTemplate<ShopManagerDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(account,"登录名不能为空");
            }

            @Override
            protected ShopManagerDTO process() throws BizException {
                ShopManager shopManager = resetPasswordFacade.validateAccount(account);
                ShopManagerDTO shopManagerDTO = BdUtil.bo2do(shopManager, ShopManagerDTO.class);
                return shopManagerDTO;
            }
        }.execute();
    }

    @Override
    public Result<ShopManagerDTO> sendValidateCode(final String mobile) {
        return new ApiTemplate<ShopManagerDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(mobile,"手机号码不能为空");
            }

            @Override
            protected ShopManagerDTO process() throws BizException {
                ShopManager shopManager = resetPasswordFacade.sendValidateCode(mobile);
                ShopManagerDTO shopManagerDTO = BdUtil.bo2do(shopManager, ShopManagerDTO.class);
                return shopManagerDTO;
            }
        }.execute();
    }

    @Override
    public Result<Boolean> loginValidateCode(final String code, final Long userId) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(code, "验证码不能为空");
                Assert.notNull(userId, "用户ID不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                return resetPasswordFacade.loginValidateCode(code, userId);
            }
        }.execute();
    }

    @Override
    public Result<Boolean> resetPassword(final ResetPasswordParam resetPasswordParam) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(resetPasswordParam, "重置内容不能为空");
                Assert.notNull(resetPasswordParam.getUserId(), "用户ID不能为空");
                Assert.hasText(resetPasswordParam.getNewPassword(), "重置密码不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                resetPasswordFacade.resetPassword(resetPasswordParam.getUserId(), resetPasswordParam.getNewPassword());
                return Boolean.TRUE;
            }
        }.execute();
    }


}
