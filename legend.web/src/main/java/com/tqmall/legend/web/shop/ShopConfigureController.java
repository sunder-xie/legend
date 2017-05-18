package com.tqmall.legend.web.shop;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.TableColumnConfig;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.marketing.ng.NoteConfigureService;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.NoteConfigureVo;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.enums.config.ListStyleEnum;
import com.tqmall.legend.enums.config.PageStyleConfigKeyEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 设置管理
 */
@Controller
@Slf4j
@RequestMapping("shop/conf")
public class ShopConfigureController extends BaseController {
    @Autowired
    private NoteConfigureService noteConfigureService;
    @Autowired
    ShopManagerService shopManagerService;
    @Autowired
    ShopManagerLoginService shopManagerLoginService;
    @Autowired
    ShopConfigureService shopConfigureService;
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 提醒设置-设置页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "notice", method = RequestMethod.GET)
    public String showNoticeSetting(Model model) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = UserUtils.getShopIdForSession(request);

        model.addAttribute("appointConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_APPOINT_CONF_TYPE));
        model.addAttribute("visitConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_VISIT_CONF_TYPE));
        model.addAttribute("insuranceConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_INSURANCE_CONF_TYPE));
        model.addAttribute("auditConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_AUDIT_CONF_TYPE));
        model.addAttribute("keepupConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_KEEPUP_CONF_TYPE));
        model.addAttribute("birthdyConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_BIRTHDY_CONF_TYPE));
        model.addAttribute("lostCustomerConf", noteConfigureService.getConfigure(shopId, Constants.NOTE_LOSTCUSTOMER_CONF_TYPE));
        //档口版的用户跳转至收款类型设置
        if (userInfo.getLevel().equals(Constants.SHOP_LEVEL_TQMALL_VERSION)) {
            return "redirect:/shop/setting/debit-type";
        }
        return "yqx/page/setting/notice";
    }

    /**
     * 提醒设置——信息更新
     *
     * @param noteConfigureVo
     * @param request
     * @return
     */
    @RequestMapping(value = "notice/update", method = RequestMethod.POST)
    @ResponseBody
    public Result updateNotice(NoteConfigureVo noteConfigureVo, HttpServletRequest request) {

        // TODO 2016-12-07 第二次提醒设置存在,第一次提醒设置是否必须存在
        UserInfo userInfo = UserUtils.getUserInfo(request);
        String keepupNoteFirst = noteConfigureVo.getKeepupNoteFirstValue();
        String keepupNoteSecond = noteConfigureVo.getKeepupNoteSecondValue();
        if ((keepupNoteFirst != null && keepupNoteSecond != null) &&
                (Long.parseLong(keepupNoteFirst) < Long.parseLong(keepupNoteSecond))) {
            return Result.wrapErrorResult("", "[保养到期] '第一次提醒'不能晚于'第二次提醒'");
        }

        // 预约提醒
        String appointNoteFirst = noteConfigureVo.getAppointNoteFirstValue();
        String appointNoteInvalid = noteConfigureVo.getAppointNoteInvalidValue();
        if (appointNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.APPOINTPRINT, "0", appointNoteFirst, userInfo);
        }
        if (appointNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.APPOINTPRINT, "1", appointNoteInvalid, userInfo);
        }

        // 回访提醒
        String visitNoteFirst = noteConfigureVo.getVisitNoteFirstValue();
        String visitNoteInvalid = noteConfigureVo.getVisitNoteInvalidValue();
        if (visitNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.HUIFANGPRINT, "0", visitNoteFirst, userInfo);
        }
        if (visitNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.HUIFANGPRINT, "1", visitNoteInvalid, userInfo);
        }

        // 保险提醒
        String insuranceNoteFirst = noteConfigureVo.getInsuranceNoteFirstValue();
        String insuranceNoteInvalid = noteConfigureVo.getInsuranceNoteInvalidValue();
        if (insuranceNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BAOXIANTIXING, "0", insuranceNoteFirst, userInfo);
        }
        if (insuranceNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BAOXIANTIXING, "1", insuranceNoteInvalid, userInfo);
        }

        // 年检提醒
        String auditNoteFirst = noteConfigureVo.getAuditNoteFirstValue();
        String auditNoteInvalid = noteConfigureVo.getAuditNoteInvalidValue();
        if (auditNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.NIANJIANTIXING, "0", auditNoteFirst, userInfo);
        }
        if (auditNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.NIANJIANTIXING, "1", auditNoteInvalid, userInfo);
        }

        // 保养提醒
        String keepupNoteInvalid = noteConfigureVo.getKeepupNoteInvalidValue();
        if (keepupNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BAOYANGTIXING, "0", keepupNoteFirst, userInfo);
        }
        if (keepupNoteSecond != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BAOYANGTIXING, "2", keepupNoteSecond, userInfo);
        }
        if (keepupNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BAOYANGTIXING, "1", keepupNoteInvalid, userInfo);
        }

        // 客户生日提醒
        String birthdyNoteFirst = noteConfigureVo.getBirthdyNoteFirstValue();
        if (birthdyNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.BIRTHDAYTIXING, "0", birthdyNoteFirst, userInfo);
        }

        // 流失客户
        String lostCustomerNoteFirst = noteConfigureVo.getLostCustomerNoteFirstValue();
        String lostCustomerNoteInvalid = noteConfigureVo.getLostCustomerNoteInvalidValue();
        if (lostCustomerNoteFirst != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.LOSTCUSTOMERTIXING, "0", lostCustomerNoteFirst, userInfo);
        }
        if (lostCustomerNoteInvalid != null) {
            updateRemindConfigure(ShopConfigureTypeEnum.LOSTCUSTOMERTIXING, "1", lostCustomerNoteInvalid, userInfo);
        }

        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 获取表格列显示样式
     *
     * @param localKey
     * @return
     */
    @RequestMapping(value = "/get-table-column")
    @ResponseBody
    public Result getTableColumn(@RequestParam(value = "localKey", required = true) String localKey) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        String confKey = PageStyleConfigKeyEnum.getConFKeyByLocalKey(localKey);
        if (StringUtils.isEmpty(confKey)) {
            return Result.wrapErrorResult("-1", "未定义的localKey");
        }
        Integer conType = ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode();
        try {
            String config = shopConfigureService.getShopConfigure(shopId, conType, confKey);
            List<TableColumnConfig> tableColumnConfigList = (List<TableColumnConfig>) shopConfigureService.getConfigureByJson(shopId, conType, confKey, TableColumnConfig.class);
            return Result.wrapSuccessfulResult(tableColumnConfigList);
        } catch (Exception e) {
            log.error("获取页面样式配置异常,信息：{}", e.getMessage());
        }
        return Result.wrapErrorResult("-1", "获取页面样式配置异常");
    }

    /**
     * 获取列表样式
     *
     * @param localKey
     * @return
     */
    @RequestMapping(value = "/get-list-style")
    @ResponseBody
    public Result getConfigure(@RequestParam(value = "localKey", required = true) String localKey) {
        Long shopId = UserUtils.getUserInfo(request).getShopId();
        String confKey = PageStyleConfigKeyEnum.getConFKeyByLocalKey(localKey);
        if (StringUtils.isEmpty(confKey)) {
            return Result.wrapErrorResult("-1", "未定义的localKey");
        }
        Integer conType = ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode();
        try {
            String config = shopConfigureService.getShopConfigure(shopId, conType, confKey);
            return Result.wrapSuccessfulResult(config);
        } catch (Exception e) {
            log.error("获取页面样式配置异常,信息：{}", e.getMessage());
        }
        return Result.wrapErrorResult("-1", "获取页面样式配置异常");
    }

    /**
     * 设置表格列显示样式
     *
     * @param localKey
     * @param confValue
     * @return
     */
    @RequestMapping(value = "/set-table-column", method = RequestMethod.POST)
    @ResponseBody
    public Result setTableColumn(@RequestParam(value = "localKey", required = true) String localKey, @RequestParam(value = "confValue", required = true) String confValue) {
        String confKey = PageStyleConfigKeyEnum.getConFKeyByLocalKey(localKey);
        if (StringUtils.isEmpty(confKey)) {
            return Result.wrapErrorResult("-1", "未定义的confKey");
        }
        try {
            JSONArray jsonArray = JSONArray.fromObject(confValue);
            List<TableColumnConfig> tableColumnConfigList = (List<TableColumnConfig>) JSONArray.toCollection(jsonArray, TableColumnConfig.class);
        } catch (Exception e) {
            log.error("confValue格式错误,errMsg:{}", e.getMessage());
            return Result.wrapErrorResult("-1", "confValue格式错误");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setConfValue(confValue);
        shopConfigure.setShopId(userInfo.getShopId());
        shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode()));
        shopConfigure.setConfKey(confKey);
        shopConfigure.setCreator(userInfo.getUserId());
        shopConfigure.setModifier(userInfo.getUserId());
        Integer confType = ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode();
        try {
            boolean s = shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), confType, new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
            if (s) {
                return Result.wrapSuccessfulResult(null);
            } else {
                return Result.wrapErrorResult("-1", "设置配置失败");
            }
        } catch (Exception e) {
            log.error("设置页面样式配置异常,信息：{}", e.getMessage());
        }
        return Result.wrapErrorResult("-1", "设置页面样式配置异常");
    }

    /**
     * 设置列表样式
     *
     * @param localKey
     * @param confValue
     * @return
     */
    @RequestMapping(value = "/set-list-style", method = RequestMethod.POST)
    @ResponseBody
    public Result setListStyle(@RequestParam(value = "localKey", required = true) String localKey, @RequestParam(value = "confValue", required = true) String confValue) {
        String confKey = PageStyleConfigKeyEnum.getConFKeyByLocalKey(localKey);
        if (StringUtils.isEmpty(confKey)) {
            return Result.wrapErrorResult("-1", "未定义的confKey");
        }
        if (confKey.equals(PageStyleConfigKeyEnum.LISTSTYLE.getConfKey())) {//列表风格:卡片还是列表样式配置
            if (!ListStyleEnum.isExist(confValue)) {
                return Result.wrapErrorResult("-1", "未定义的confValue");
            }
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setConfValue(confValue);
        shopConfigure.setShopId(userInfo.getShopId());
        shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode()));
        shopConfigure.setConfKey(confKey);
        shopConfigure.setCreator(userInfo.getUserId());
        shopConfigure.setModifier(userInfo.getUserId());
        Integer confType = ShopConfigureTypeEnum.SHOPPAGESTYLE.getCode();
        try {
            boolean s = shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), confType, new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
            if (s) {
                return Result.wrapSuccessfulResult(null);
            } else {
                return Result.wrapErrorResult("-1", "设置配置失败");
            }
        } catch (Exception e) {
            log.error("设置表格列样式配置异常,信息：{}", e.getMessage());
        }
        return Result.wrapErrorResult("-1", "设置表格列样式配置异常");
    }

    /**
     * 获取门店的打印新旧版本
     *
     * @return
     */
    @RequestMapping("get-print-version")
    @ResponseBody
    public Result getShopPrintVersion() {
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopConfigure shopConfigure = shopConfigureService.getPrintVersion(shopId);
        return Result.wrapSuccessfulResult(shopConfigure.getConfValue());
    }

    /**
     * 打印版本切换
     *
     * @return
     */
    @RequestMapping("change-print-version")
    @ResponseBody
    public Result changeShopPrintVersion(){
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopConfigure shopConfigure = shopConfigureService.getPrintVersion(shopId);
        String version = shopConfigure.getConfValue();
        version = "new".equals(version) ? "old" : "new";
        shopConfigure.setConfValue(version);
        if (shopConfigure.getShopId() == 0l ){
            //新增
            shopConfigure.setShopId(shopId);
            shopConfigureService.add(shopConfigure);
        }else{
            //更新
            shopConfigureService.update(shopConfigure);
        }
            String versionJson = new Gson().toJson(shopConfigure);
            jedisClient.hset(shopId.toString(),Constants.SHOP_PRINT_VERSION,versionJson);
        return Result.wrapSuccessfulResult(version);
    }

    /**
     * 更改门店提醒设置
     *
     * @param configureTypeEnum 配置类型
     * @param confKey           配置项-子key
     * @param confKeyValue      配置项-子key的value
     * @param userInfo          操作人
     */
    private void updateRemindConfigure(ShopConfigureTypeEnum configureTypeEnum,
                                       String confKey,
                                       String confKeyValue,
                                       UserInfo userInfo) {

        int configureType = configureTypeEnum.getCode();
        // 获取配置
        Optional<ShopConfigure> configureOptional = shopConfigureService.getShopConfigure(configureTypeEnum, confKey, userInfo.getShopId());
        // 已存在:更新; 不存在:新增配置
        ShopConfigure shopConfigure = null;
        if (configureOptional.isPresent()) {
            shopConfigure = configureOptional.get();
            // 新值 != 旧值
            if (!confKeyValue.equals(shopConfigure.getConfValue())) {
                // 更新配置
                updateShopConfigure(confKeyValue, userInfo, shopConfigure);
                // 刷新通知
                int noteType = transformConType2NoteType(configureType);
                shopNoteInfoService.refreshNoteInfo(userInfo.getShopId(), noteType);
            }
        } else {
            buildShopConfigure(configureTypeEnum, confKey, confKeyValue, userInfo);
        }
    }

    /**
     * 配置类型CODE->提醒类型CODE
     *
     * @param confType
     * @return
     */
    private int transformConType2NoteType(int confType) {
        int noteType = 0;
        switch (confType) {
            case 2: // 预约单提醒
                noteType = 0;
                break;
            case 3: // 回访提醒
                noteType = 4;
                break;
            case 4: // 保险提醒
                noteType = 2;
                break;
            case 5: // 年检提醒
                noteType = 3;
                break;
            case 6: // 保养提醒
                noteType = 1;
                break;
            case 7: // 生日提醒
                noteType = 5;
                break;
            case 8: // 流失客户提醒
                noteType = 6;
                break;
            default:
                break;
        }
        return noteType;
    }

    // TODO 2016-12-14 不建议封装
    private void updateShopConfigure(String confValue, UserInfo userInfo, ShopConfigure shopConfigure) {
        shopConfigure.setModifier(userInfo.getUserId());
        shopConfigure.setConfValue(confValue);
        shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), shopConfigure.getConfType().intValue(), new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
    }


    /**
     * 新增配置
     *
     * @param configureTypeEnum 配置项类型
     * @param confKey           配置项-子key
     * @param confValue         配置项-子key的value
     * @param userInfo
     * @return
     */
    private ShopConfigure buildShopConfigure(ShopConfigureTypeEnum configureTypeEnum, String confKey, String confValue, UserInfo userInfo) {
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setShopId(userInfo.getShopId());
        shopConfigure.setConfType(Long.parseLong(configureTypeEnum.getCode() + ""));
        shopConfigure.setConfKey(confKey);
        shopConfigure.setConfValue(confValue);
        shopConfigure.setCreator(userInfo.getUserId());
        shopConfigure.setModifier(userInfo.getUserId());
        shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), shopConfigure.getConfType().intValue(), shopConfigure.getConfKey(), new DataShopConfigConverter<ShopConfigure>(), shopConfigure);

        return shopConfigure;
    }

    /**
     * 设置使用他人账户是否开启
     *
     * @return
     */
    @RequestMapping(value = "change-guest-conf", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> changeGuestConf(@RequestParam(value = "value") final String value) {
        return new ApiTemplate<Boolean>() {
            Long shopId = UserUtils.getShopIdForSession(request);
            Long userId = UserUtils.getUserIdForSession(request);
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(value, "参数为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Optional<ShopConfigure> shopConfigureOptional = shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT, shopId);
                ShopConfigure shopConfigure;
                log.info("门店配置使用他人账户开关，门店：{}，操作人id：{}，配置项：{}", shopId, userId, value);
                if (shopConfigureOptional.isPresent()) {
                    //更新
                    shopConfigure = shopConfigureOptional.get();
                    shopConfigure.setConfValue(value);
                    shopConfigure.setModifier(userId);
                    shopConfigureService.update(shopConfigure);
                    return true;
                }
                //新增
                shopConfigure = new ShopConfigure();
                shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.USE_GUEST_ACCOUNT.getCode()));
                shopConfigure.setShopId(shopId);
                shopConfigure.setConfKey("use_guest_account");
                shopConfigure.setConfValue(value);
                shopConfigure.setCreator(userId);
                shopConfigureService.add(shopConfigure);
                return true;
            }
        }.execute();
    }
}
