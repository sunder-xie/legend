package com.tqmall.legend.service.security;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.security.ConfigDTO;
import com.tqmall.legend.object.param.security.ConfigParam;
import com.tqmall.legend.object.param.security.LoginParam;
import com.tqmall.legend.object.param.security.ManagerDeviceDTO;
import com.tqmall.legend.object.param.security.ShopNetworkConfigDTO;
import com.tqmall.legend.object.result.security.UserInfoDTO;

import java.util.List;

/**
 * Created by zsy on 16/6/16.
 * 用户登录
 */
public interface RpcLoginService {

    /**
     * 商家app登陆接口
     *
     * @param loginParam 登录参数
     * @return
     */
    Result<UserInfoDTO> loginForApp(LoginParam loginParam);

    /**
     * 用户登录多账号
     * @param loginParam
     * @return
     */
    Result<List<UserInfoDTO>> getMoreLoginUserInfo(LoginParam loginParam);

    /**
     * 商家app同意协议接口
     *
     * @param loginParam 入参
     * @return
     */
    Result<UserInfoDTO> agreeProtocolForApp(LoginParam loginParam);


    /**
     * 根据登录账户获取用户信息
     *
     * @param loginAccount 登录账户
     * @param password 登录密码 MD5
     * @param shopId 门店id 账号：单个门店可不传，多个门店必须传
     * @param checkPassword 是否校验密码
     *
     * @return
     */
    Result<UserInfoDTO> getUserInfoByLoginAccountForApp(String loginAccount,String password, Long shopId,boolean checkPassword);

    /**
     * 二维码 扫码校验
     * @param configParam
     * @return boolean true:扫码校验成功 false:扫码校验失败
     */
    Result<Boolean> scanCode(ConfigParam configParam);

    /**
     * 根据不同的安全登陆等级 处理不同的校验:时间,设备,网络环境
     *
     * @param configParam
     * @return
     */
    Result<ConfigDTO> levelCheck(ConfigParam configParam);

    /**
     * 安全登陆体系 新设备提交授权申请
     * @param configParam
     * @return
     */
    Result<Boolean> authoriza(ConfigParam configParam);


    /**
     *已设置网络环境列表
     * @return
     */
    Result<List<ShopNetworkConfigDTO>> getNetConfigList(Long shopId);

    /**
     *
     * @param configParam
     * @return
     */
    Result<Boolean> insertNetWork(ConfigParam configParam);

    /**
     * 删除网络配置环境
     * @param configParam
     * @return
     */
    Result<Boolean> deleteNetWork(ConfigParam configParam);

    /**
     * @param account 登录帐号/手机号码
     * @param password 登录密码 MD5加密
     * @param deviceId 设备id
     * @param shopId 门店id 账号：单个门店可不传，多个门店必须传
     * @param checkPassword 是否校验密码
     * @return 员工头像，姓名，权限（老板、财务），设备绑定状态
     */
    Result<ManagerDeviceDTO> getDeviceDetail(String account,String password , String deviceId, Long shopId, boolean checkPassword);
}
