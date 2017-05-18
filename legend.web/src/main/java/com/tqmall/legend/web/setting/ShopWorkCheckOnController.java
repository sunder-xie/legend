package com.tqmall.legend.web.setting;

import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.JedisPoolUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.attendance.AppAttendanceService;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.biz.component.converter.DataShopConfigConverter;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.entity.shop.SignInfoConfig;
import com.tqmall.legend.enums.config.ShopWorkTimeConfKeyEnum;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.sql.Time;

/**
 * 设置->功能配置->上下班时间设置
 * Created by wushuai on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/conf/work-check-on")
public class ShopWorkCheckOnController extends BaseController {

    @Autowired
    private AppAttendanceService appAttendanceService;
    @Autowired
    private ShopFunFacade shopFunFacade;
    @Autowired
    private ShopConfigureService shopConfigureService;

    /**
     * 上下班时间设置主页
     *
     * @return
     */
    @RequestMapping("")
    public String index(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        SignTime signTime = this.appAttendanceService.getSigTime(shopId);
        model.addAttribute("signInTime", DateUtil.convertDateToStr(signTime.getSignInTime(), "HH:mm:ss"));
        model.addAttribute("signOffTime", DateUtil.convertDateToStr(signTime.getSignOffTime(), "HH:mm:ss"));
        boolean flag = shopFunFacade.isBpShare(request, shopId);
        model.addAttribute("isBpShare", flag);
        //如果是钣喷中心，才传递午休时间
        if (flag) {
            model.addAttribute("noonBreakStartTime", DateUtil.convertDateToStr(signTime.getNoonBreakStartTime(), "HH:mm:ss"));
            model.addAttribute("noonBreakEndTime", DateUtil.convertDateToStr(signTime.getNoonBreakEndTime(), "HH:mm:ss"));
        }
        return "yqx/page/setting/function/work-check-on";
    }

    /**
     * 门店支付方式开关设置
     *
     * @param signInfoConfig 上班时间配置
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> save(@RequestBody final SignInfoConfig signInfoConfig) {
        final UserInfo userInfo = UserUtils.getUserInfo(request);
        final Long shopId = userInfo.getShopId();
        final boolean isBpShare = shopFunFacade.isBpShare(request, shopId);
        Assert.notNull(signInfoConfig, "上班时间配置不能为空");
        final Time signInTime = signInfoConfig.getSignInTime();//上班时间
        final Time signOffTime = signInfoConfig.getSignOffTime();//下班时间
        final Time noonBreakStartTime = signInfoConfig.getNoonBreakStartTime();//午休开始时间
        final Time noonBreakEndTime = signInfoConfig.getNoonBreakEndTime();//午休结束时间
        return new ApiTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(signInTime, "上班时间不能为空");
                Assert.notNull(signOffTime, "下班时间不能为空");
                if (signOffTime.compareTo(signInTime) <= 0) {
                    throw new BizException("下班时间不能早于上班时间");
                }
                if (isBpShare) {
                    //钣喷店,有午休时间
                    Assert.notNull(noonBreakStartTime, "午休开始时间不能为空");
                    Assert.notNull(noonBreakEndTime, "午休结束时间不能为空");
                    if (noonBreakStartTime.compareTo(signInTime) <= 0) {
                        throw new BizException("午休开始时间不能早于上班时间");
                    }
                    if (noonBreakEndTime.compareTo(noonBreakStartTime) <= 0) {
                        throw new BizException("午休结束时间不能早于开始时间");
                    }
                    if (signOffTime.compareTo(noonBreakEndTime) <= 0) {
                        throw new BizException("下班时间不能早于午休结束时间");
                    }
                }
            }

            @Override
            protected String process() throws BizException {
                log.info("[功能配置]上下班时间设置,门店id:{}，操作人id:{},signInfoConfig:{}", userInfo.getShopId(), userInfo.getUserId(), LogUtils.objectToString(signInfoConfig));
                Jedis jedis = JedisPoolUtils.getMasterJedis();
                //保存上下班时间
                saveSignTime(ShopWorkTimeConfKeyEnum.SIGN_IN.getConfKey(), signInTime.toString());
                saveSignTime(ShopWorkTimeConfKeyEnum.SIGN_OFF.getConfKey(), signOffTime.toString());
                /**
                 * 钣喷magic需要频繁获取上班时间配置
                 * 此处设置缓存在com.tqmall.legend.facade.magic.impl.WorkTimeFacadeImpl.initWorkTime()中会用到
                 */
                jedis.setex(Constants.OPEN_TIME_KEY + shopId, 24 * 3600, signInTime.toString());
                jedis.setex(Constants.CLOSE_TIME_KEY + shopId, 24 * 3600, signOffTime.toString());
                if (isBpShare) {
                    //保存钣喷中心，午休时间
                    saveSignTime(ShopWorkTimeConfKeyEnum.NOON_BREAK_START.getConfKey(), noonBreakStartTime.toString());
                    saveSignTime(ShopWorkTimeConfKeyEnum.NOON_BREAK_END.getConfKey(), noonBreakEndTime.toString());
                    jedis.setex(Constants.NOONBREAK_START_TIME_KEY + shopId, 24 * 3600, noonBreakStartTime.toString());
                    jedis.setex(Constants.NOONBREAK_END_TIME_KEY + shopId, 24 * 3600, noonBreakEndTime.toString());
                }
                return "操作成功";
            }
        }.execute();
    }


    private void saveSignTime(String confKey, String confValue) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        ShopConfigure shopConfigure = new ShopConfigure();
        shopConfigure.setConfKey(confKey);
        shopConfigure.setConfValue(confValue);
        shopConfigure.setShopId(userInfo.getShopId());
        shopConfigure.setCreator(userInfo.getUserId());
        shopConfigure.setModifier(userInfo.getUserId());
        shopConfigure.setConfType(Long.valueOf(ShopConfigureTypeEnum.COMMUTETIME.getCode()));
        shopConfigureService.saveOrUpdateShopConfigure(userInfo.getShopId(), shopConfigure.getConfType().intValue(), confKey ,new DataShopConfigConverter<ShopConfigure>(), shopConfigure);
    }

}
