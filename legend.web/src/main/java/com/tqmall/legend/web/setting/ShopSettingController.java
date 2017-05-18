package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.dandelion.wechat.client.dto.wechat.ShopDTO;
import com.tqmall.dandelion.wechat.client.wechat.shop.WeChatShopService;
import com.tqmall.holy.provider.entity.customer.CustomerFilePathDTO;
import com.tqmall.holy.provider.entity.customer.CustomerJoinAuditLegendDTO;
import com.tqmall.holy.provider.param.CustomerJoinAuditLegendParam;
import com.tqmall.holy.provider.service.RpcCustomerService;
import com.tqmall.legend.biz.balance.FinanceAccountService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.balance.FinanceAccount;
import com.tqmall.legend.entity.shop.CustomerFilePath;
import com.tqmall.legend.entity.shop.CustomerJoinAudit;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.shop.ShopLevelEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.setting.vo.ShopSettingVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 设置改版-账户资料设置
 * Created by lilige on 17/1/4.
 */
@Slf4j
@Controller
@RequestMapping("shop/setting")
public class ShopSettingController extends BaseController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private RpcCustomerService rpcCustomerService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private WeChatShopService weChatShopService;


    /**
     * 获取账户资料详情 , 包含crm门店资料
     * created by lilige
     *
     * @return
     */
    @RequestMapping("/edit-detail")
    public String shopDetail(Model model) {
        //门店信息
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Shop shop = shopService.selectById(shopId);
        String userGlobalId = shop.getUserGlobalId();
        Integer isAdmin = userInfo.getUserIsAdmin();
        if (isAdmin != 1){
            return "common/403";
        }
        //设置销售版信息
        setSellModel(model, shop);
        model.addAttribute("shop", shop);
        model.addAttribute("NOSHOP", true);
        FinanceAccount financeAccount = financeAccountService.getSettleFinanceAccount(shopId);
        if (null != financeAccount){
            String account = financeAccount.getAccount();
            financeAccount.setAccount(StringUtil.formateBankCardNumber(account));
            model.addAttribute("financeAccount",financeAccount);
        }
        if (StringUtils.isBlank(userGlobalId)) {
            return "yqx/page/setting/shopSetting/edit-detail";
        }
        com.tqmall.core.common.entity.Result<CustomerJoinAuditLegendDTO> result = null;
        try {
            result = rpcCustomerService.showShopInformation(Long.valueOf(userGlobalId));
            if (!result.isSuccess()) {
                //如果从CRM获取数据失败,则弹出提示框,显示异常
                log.error("从CRM获取店铺信息失败.code:{}, error message:{}", result.getCode(), result.getMessage());
            } else {
                CustomerJoinAuditLegendDTO dto = result.getData();
                model.addAttribute("crmShop", dto);
                model.addAttribute("NOSHOP", false);
            }
        } catch (Exception e) {
            log.error("[从CRM获取店铺信息失败]userGlobalId :{}", userGlobalId, e);
        }

        return "yqx/page/setting/shopSetting/edit-detail";
    }


    /**
     * 更新门店的资料,包含crm门店资料
     * isCrm             true-发布到crm ; false-不发布
     * created by lilige
     *
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Result> saveShopDetail(@RequestBody final ShopSettingVo shopSettingVo) {
        //Shop shop, CustomerJoinAudit customerJoinAudit, Boolean isCrm
        return new ApiTemplate<Result>() {
            final Shop shop = shopSettingVo.getShop();
            final Boolean isCrm = shopSettingVo.getIsCrm();
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shop);
                Assert.notNull(isCrm);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Result process() throws BizException {

                long shopId = UserUtils.getShopIdForSession(request);
                long modifier = UserUtils.getUserIdForSession(request);
                String userGlobalIdStr = UserUtils.getUserGlobalIdForSession(request);
                Shop oldShop = shopService.selectById(shopId);
                buildUpdateShop(modifier, oldShop);
                Result result = shopService.update(oldShop);
                if (!isCrm) {
                    if (!result.isSuccess()){
                        throw new BizException(result.getErrorMsg());
                    }
                    // 更新基本信息
                    if (StringUtils.isBlank(userGlobalIdStr)){
                        return Result.wrapSuccessfulResult(true);
                    }
                    Long userGlobalId = Long.valueOf(userGlobalIdStr);
                    com.tqmall.core.common.entity.Result<CustomerJoinAuditLegendDTO> crmResult  = rpcCustomerService.showShopInformation(Long.valueOf(userGlobalId));
                    if (crmResult.isSuccess()){
                        CustomerJoinAuditLegendParam param = new CustomerJoinAuditLegendParam();
                        param.setAddress(shop.getAddress());
                        param.setCompanyName(shop.getName());
                        param.setMobilephone(shop.getTel());//同步门店电话
                        param.setCustomerId(userGlobalId);
                        com.tqmall.core.common.entity.Result<Boolean> crmResult2 = rpcCustomerService.updateShopInformation(param);
                        if (!crmResult2.isSuccess()) {
                            throw new BizException(crmResult.getMessage());
                        }
                    }
                    return Result.wrapSuccessfulResult(true);
                }
                CustomerJoinAudit customerJoinAudit = shopSettingVo.getCustomerJoinAudit();
                if (customerJoinAudit == null || customerJoinAudit.getCustomerId() == null){
                    throw new BizException("门店数据有误，无法同步App资料");
                }
                List<CustomerFilePath> customerFilePathList = customerJoinAudit.getCustomerFilePathList();
                List<CustomerFilePathDTO> dtoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(customerFilePathList)) {
                    int i = 0;
                    for (CustomerFilePath customerFilePath : customerFilePathList) {
                        customerFilePath.setCustomerId(customerJoinAudit.getCustomerId());
                        customerFilePath.setOrderIdx(i);
                        CustomerFilePathDTO dto = new CustomerFilePathDTO();
                        try {
                            BeanUtils.copyProperties(customerFilePath,dto);
                            dtoList.add(dto);
                        } catch (Exception e) {
                            log.error("[编辑app资料保存,类型转换错误]customerFilePath:{}", customerFilePath);
                            continue;
                        } finally {
                            i++;
                        }
                    }
                }
                CustomerJoinAuditLegendParam param = new CustomerJoinAuditLegendParam();
                param.setCustomerFilePathDTOList(dtoList);
                try {
                    BeanUtils.copyProperties(customerJoinAudit,param);
                    param.setLatitude(new BigDecimal(customerJoinAudit.getLatitude()));
                    param.setLongitude(new BigDecimal(customerJoinAudit.getLongitude()));
                } catch (Exception e) {
                    log.error("[编辑app资料保存,类型转换错误]customerJoinAudit:{}", customerJoinAudit);
                    throw new BizException("编辑app资料保存失败");
                }
                try {
                    param.setAddress(shop.getAddress());
                    param.setCompanyName(shop.getName());
                    param.setMobilephone(shop.getTel());//同步门店电话
                    com.tqmall.core.common.entity.Result<Boolean> crmResult = rpcCustomerService.updateShopInformation(param);
                    if (!crmResult.isSuccess()) {
                        throw new BizException(crmResult.getMessage());
                    }
                    ShopDTO shopDTO = new ShopDTO();
                    Long ucShopId = customerJoinAudit.getCustomerId();
                    shopDTO.setUcShopId(ucShopId);
                    String saMobilephone = customerJoinAudit.getSaMobilephone();
                    if (StringUtils.isNotBlank(saMobilephone)) {
                        shopDTO.setSaTelephone(saMobilephone);
                    } else {
                        shopDTO.setSaTelephone(shopDTO.getShopTelephone());
                    }
                    try {
                        //同步异常，不影响正常保存
                        com.tqmall.core.common.entity.Result<String> wechatResult = weChatShopService.updateShopForLegend(shopDTO);
                        logger.info("[dubbo]门店资料更新同步到ddl-wechat{}", LogUtils.funToString(shopDTO, wechatResult));
                    } catch (Exception e) {
                        logger.error("[dubbo]门店资料更新同步到ddl-wechat出现异常，门店userGlobalId：" + customerJoinAudit.getCustomerId(), e);
                    }
                    return Result.wrapSuccessfulResult(true);
                } catch (Exception e) {
                    log.error("[编辑app资料保存,调用dubbo接口失败]param:{}", LogUtils.objectToString(param), e);
                    throw new BizException("编辑app资料保存失败");
                }
            }

            private void buildUpdateShop(long modifier, Shop oldShop) {
                oldShop.setAddress(shop.getAddress());
                oldShop.setCity(shop.getCity());
                oldShop.setCityName(shop.getCityName());
                oldShop.setCompanyName(shop.getCompanyName());
                oldShop.setName(shop.getName());
                oldShop.setContact(shop.getContact());
                oldShop.setDistrict(shop.getDistrict());
                oldShop.setDistrictName(shop.getDistrictName());
                oldShop.setName(shop.getName());
                oldShop.setProvince(shop.getProvince());
                oldShop.setProvinceName(shop.getProvinceName());
                oldShop.setStreet(shop.getStreet());
                oldShop.setStreetName(shop.getStreetName());
                oldShop.setTel(shop.getTel());
                oldShop.setModifier(modifier);
            }
        }.execute();
    }


    @RequestMapping("/app/img-delete")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> deleteAppImg(final Long id){
        return new ApiTemplate<String>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected String process() throws BizException {
                try {
                    com.tqmall.core.common.entity.Result<Boolean> result  = rpcCustomerService.deleteFilePath(id);
                    if (result.isSuccess()){
                        return "图片删除成功";
                    }
                    log.info("[调用CRM接口删除图片失败],图片id:{},原因:{}",id,LogUtils.objectToString(result));
                } catch (Exception e) {
                    log.error("[调用CRM接口删除图片错误],图片id:{}",id,e);
                }
                throw new BizException("图片删除失败");

            }
        }.execute();
    }


    /**
     * 设置售卖版本的门店的信息
     *
     * @param model
     * @param shop
     */
    private void setSellModel(Model model, Shop shop) {
        Integer shopLevel = UserUtils.getShopLevelForSession(request);
        //售卖版本，显示版本信息和有效期
        if (shopLevel.equals(ShopLevelEnum.BASE.getValue())) {
            model.addAttribute("shopModel", ShopLevelEnum.BASE.getName());
        } else if (shopLevel.equals(ShopLevelEnum.STANDARD.getValue())) {
            model.addAttribute("shopModel", ShopLevelEnum.STANDARD.getName());
        } else if (shopLevel.equals(ShopLevelEnum.PROFESSION.getValue())) {
            model.addAttribute("shopModel", ShopLevelEnum.PROFESSION.getName());
        }
        model.addAttribute("gmtCreate", DateUtil.convertDate(shop.getGmtCreate()));
        model.addAttribute("expireTime", DateUtil.convertDate(shop.getExpireTime()));
    }
}
