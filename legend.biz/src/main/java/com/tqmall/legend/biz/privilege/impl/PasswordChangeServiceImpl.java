package com.tqmall.legend.biz.privilege.impl;

import com.tqmall.common.Constants;
import com.tqmall.legend.biz.privilege.PasswordChangeService;
import com.tqmall.legend.biz.util.PasswordUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.privilege.ShopManagerLoginDao;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by QXD on 2014/10/29.
 */
@Service
public class PasswordChangeServiceImpl implements PasswordChangeService {
    Logger logger = LoggerFactory.getLogger(PasswordChangeServiceImpl.class);
    @Autowired
    private ShopManagerLoginDao shopManagerLoginDao;


    /**
     * 修改密码
     *
     * @param accountId   修改人
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @Override
    public Result changePassword(long accountId, String oldPassword, String newPassword,
                                 long shopId) {
        logger.info("【PC修改密码】输入的旧密码:{},输入的新密码:{},操作人:{},门店ID:{}", oldPassword, newPassword,accountId,shopId);
        Result result = changePasswordParamCheck(accountId, oldPassword, newPassword, shopId);
        if (!result.isSuccess()) {
            return result;
        }

        ShopManagerLogin shopManagerLogin = new ShopManagerLogin();
        shopManagerLogin.setManagerId(accountId);
        shopManagerLogin.setShopId(shopId);

        ShopManagerLogin shopManagerLoginResult = null;
        try {
            shopManagerLoginResult = shopManagerLoginDao.selectById(shopManagerLogin);
        } catch (Exception e) {
            logger.error("DB操作查询用户失败", e);
        }

        if (null == shopManagerLoginResult) {
            logger.warn("当前用户不存在" + accountId);
            return Result.wrapErrorResult("", "修改失败");
        }
        //原数据库存储的密码md5值
        String md5old = shopManagerLoginResult.getPassword();
        //愿数据库存储的小写md5值
        String md5lower = shopManagerLoginResult.getLowerPassword();
//        newPassword = newPassword.toLowerCase();
        if (md5old.equals(MD5Util.MD5(newPassword))) {
            return Result.wrapErrorResult("", "输入的密码与原密码相同");
        }
        //如果数据库不存在小写的md5值
        if (StringUtils.isBlank(md5lower)) {
            if (MD5Util.MD5(oldPassword).equals(md5old)) {
                if (updateDB(shopManagerLoginResult, newPassword, accountId, shopId)) {
                    return Result.wrapSuccessfulResult("修改成功");
                }
            } else {
                return Result.wrapErrorResult("", "旧密码不匹配");
            }
        }else {
            if (MD5Util.MD5(oldPassword).equals(md5old)||MD5Util.MD5(oldPassword.toLowerCase()).equals(md5lower)){
                if (updateDB(shopManagerLoginResult, newPassword, accountId, shopId)) {
                    return Result.wrapSuccessfulResult("修改成功");
                }
            }else {
                return Result.wrapErrorResult("", "旧密码不匹配");
            }
        }
        return Result.wrapErrorResult("", "修改失败");
    }

    private boolean updateDB(ShopManagerLogin shopManagerLogin, String password, Long account, Long shopId) {
        shopManagerLogin.setPassword(MD5Util.MD5(password));
        shopManagerLogin.setLowerPassword(MD5Util.MD5(password.toLowerCase()));
        shopManagerLogin.setModifier(account);
        shopManagerLogin.setShopId(shopId);
        try {
            return shopManagerLoginDao.updateById(shopManagerLogin) > Constants.FAIL_FLAG;
        } catch (Exception e) {
            logger.error("DB操作密码更新失败", e);
            return false;
        }
    }

    /**
     * 重置密码
     *
     * @param id
     * @param newPassword return
     */
    @Override
    public Result resetPassword(long id, String newPassword, Long shopId) {
        logger.info("【PC重置密码】: id{},输入的新密码{},门店ID:{}",id,newPassword,shopId);
        if (StringUtils.isBlank(id + "") || StringUtils.isBlank(newPassword)) {
            return Result.wrapErrorResult("", "信息缺失");
        }
        if (!PasswordUtil.checkPassword(newPassword)){
            logger.error("【PC重置密码】：密码不符合规则 输入的新密码{}",newPassword);
            return Result.wrapErrorResult("", "请输入6~12位且包含字母和数字的密码");
        }
        ShopManagerLogin login = new ShopManagerLogin();
        login.setId(id);
        login.setPassword(MD5Util.MD5(newPassword));
        login.setLowerPassword(MD5Util.MD5(newPassword.toLowerCase()));
        login.setShopId(shopId);
        try {
            logger.info("【PC重置密码】：密码：{}，更新数据{}",newPassword,login);
            if (shopManagerLoginDao.updateById(login) > Constants.FAIL_FLAG) {
                return Result.wrapSuccessfulResult(true);
            }
        } catch (Exception e) {
            logger.error("DB操作密码更新失败", e);
        }
        return Result.wrapErrorResult("", "重置失败");
    }


    /*密码更新参数校验*/
    private Result changePasswordParamCheck(long accountId, String oldPassword, String newPassword,
                                            long shopId) {
        if (accountId < Constants.MANAGER_ID_FLAG || shopId < Constants.SHOP_ID_FLAG) {
            logger.warn("accountId,shopId为空");
            return Result.wrapErrorResult("", "修改失败");
        }
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            logger.warn("信息缺失");
            return Result.wrapErrorResult("", "修改失败");
        }
        if (!PasswordUtil.checkPassword(newPassword)) {
            return Result.wrapErrorResult("", "密码不符合规则");
        }
        return Result.wrapSuccessfulResult(true);
    }
}
