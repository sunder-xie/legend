package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.enums.activity.BankEnum;
import com.tqmall.legend.object.param.subsidy.RpcUserDefaultACSetParam;
import com.tqmall.legend.object.result.subsidy.UserDefaultBankDTO;
import com.tqmall.legend.service.subsidy.RpcSubsidyService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.setting.vo.FinanceAccountVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 设置-银行卡相关信息
 * Created by lilige on 17/1/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/setting/finance")
public class FinanceAccountSettingController extends BaseController {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private RpcSubsidyService rpcSubsidyService;

    /**
     * 编辑银行卡信息,包含门店,个人的
     * created by lilige
     *
     * @param model
     * @return
     */
    @RequestMapping("/finance-detail-shop")
    public String shopFinanceDetail(Model model) {
        String url = userFinanceDetail(model, true);
        return url;
    }
    @RequestMapping("/finance-detail-user")
    public String userFinanceDetail(Model model) {
        String url = userFinanceDetail(model, false);
        return url;
    }

    /**
     * @param isShop true - 门店银行卡信息 ; false - 个人银行卡信息
     * @return
     */
    public String userFinanceDetail(Model model, Boolean isShop) {
        //查询门店管理员手机号
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        model.addAttribute("isShop", ""+isShop);
        if (isShop) {
            //查询门店银行卡信息
            ShopManager shopManager = shopManagerService.getAdmin(shopId);
            model.addAttribute("shopManager", shopManager);
            FinanceAccount financeAccount = financeAccountService.getSettleFinanceAccount(shopId);
            if (null != financeAccount) {
                model.addAttribute("financeAccount", financeAccount);
            }
        } else {
            //查询个人银行卡信息
            ShopManager shopManager = shopManagerService.selectById(userInfo.getUserId());
            model.addAttribute("shopManager", shopManager);
            List<FinanceAccount> financeAccounts = financeAccountService.getDefaultFinanceAccount(userInfo.getUserId(), shopId, 1);
            if (!CollectionUtils.isEmpty(financeAccounts)) {
                FinanceAccount financeAccount = financeAccounts.get(0);
                model.addAttribute("financeAccount", financeAccount);
            }
        }

        BankEnum[] bankEnums = BankEnum.getMessages();
        model.addAttribute("bankEnums", bankEnums);
        return "yqx/page/setting/finance/finance-detail";
    }

    /**
     * 绑定银行卡接口
     * created by lilige
     *
     * @return
     */
    @RequestMapping("/bank-bind")
    @ResponseBody
    public Result financeBind(@RequestBody final FinanceAccountVo financeAccountVo) {

        return new ApiTemplate<Result>() {

            final FinanceAccount financeAccount = financeAccountVo.getFinanceAccount();
            final Boolean isShop = financeAccountVo.getIsShop();
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(financeAccount);
                Assert.notNull(isShop);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Result process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long userId = userInfo.getUserId();
                Long shopId = userInfo.getShopId();
                //验证验证码是否正确
                ShopManager shopManager;
                if (isShop){
                    shopManager = shopManagerService.getAdmin(shopId);
                }else{
                    shopManager = shopManagerService.selectById(userId);
                }
                String identifyingCode = shopManager.getIdentifyingCode();
                String checkCode = financeAccount.getIdentifyingCode();
                if (StringUtils.isBlank(identifyingCode) || StringUtils.isBlank(checkCode) || !identifyingCode.equals(checkCode)) {
                    throw new BizException("验证码有误，请重新输入");
                }
                //绑定银行卡
                String userName = userInfo.getName();
                financeAccount.setModifier(userId);
                //门店银行卡
                if (isShop) {
                    //更新
                    if (financeAccount.getId() != null) {
                        log.info("shopId为：{}门店银行卡修改后的信息:{}，修改用户id:{}", userInfo.getShopId(), JSONUtil.object2Json(financeAccount), userId);
                        financeAccountService.upDateById(financeAccount);
                    }else{
                        financeAccount.setShopId(shopId);
                        financeAccount.setUserId(0l);
                        financeAccount.setCreator(userId);
                        financeAccount.setAccountType(1);//1-BANKCARD
                        log.info("添加门店对账银行卡信息，操作人：{}，添加信息：{}", userName, financeAccount);
                        FinanceAccount result = financeAccountService.insertFinanceAccount(financeAccount);
                        if (null == result) {
                            throw new BizException(LegendErrorCode.BIND_ERROR.getErrorMessage());
                        }
                    }
                    return Result.wrapSuccessfulResult(true);

                }
                //个人银行卡
                String password = financeAccountVo.getPassword();
                if (StringUtils.isBlank(password)){
                    throw new BizException("密码不能为空");
                }
                Result<UserDefaultBankDTO> bankDTOResult = rpcSubsidyService.getUserDefaultBank(userId,shopId);
                RpcUserDefaultACSetParam param = new RpcUserDefaultACSetParam();
                try {
                    BeanUtils.copyProperties(param, financeAccount);
                } catch (IllegalAccessException e) {
                    log.info("[编辑个人银行卡失败]shopId{},UserId{},financeAccount{}", shopId, userId, financeAccount, e);
                    throw new BizException(LegendErrorCode.BIND_ERROR.getErrorMessage());
                } catch (InvocationTargetException e) {
                    log.info("[编辑个人银行卡失败]shopId{},UserId{},financeAccount{}", shopId, userId, financeAccount, e);
                    throw new BizException(LegendErrorCode.BIND_ERROR.getErrorMessage());
                }
                param.setUserId(userId);
                param.setShopId(shopId);
                param.setUserName(financeAccount.getAccountUser());
                param.setPassWord(MD5Util.MD5(password));
                param.setAccountType(1);
                //password怎么办
                Result<UserDefaultBankDTO> result = rpcSubsidyService.userDefaultACSet(param);
                if (!result.isSuccess()) {
                    log.info("[调用接口编辑个人银行卡失败]shopId{},UserId{},financeAccount{}", shopId, userId, financeAccount);
                    throw new BizException(result.getMessage());
                }
                return Result.wrapSuccessfulResult(true);
            }
        }.execute();
    }


}
