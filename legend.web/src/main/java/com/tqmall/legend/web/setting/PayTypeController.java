package com.tqmall.legend.web.setting;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.billcenter.client.RpcPayService;
import com.tqmall.legend.billcenter.client.dto.PayTypeDTO;
import com.tqmall.legend.billcenter.client.enums.PayTypeEnum;
import com.tqmall.legend.billcenter.client.param.PayTypeParam;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lilige on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("/shop/setting/pay-type")
public class PayTypeController extends BaseController {

    @Resource
    private RpcPayService rpcPayService;

    private final static Integer OPEN_STATUS = 1;

    /**
     * 付款业务类型
     *
     * @return
     */
    @RequestMapping("")
    public String payTypeSetting(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        return "yqx/page/setting/pay-type";
    }


    /**
     * 跳到钣喷付款类型设置页面
     *
     * @param model
     * @return
     */
    @RequestMapping("bp")
    public String bpPayTypeSetting(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        return "yqx/page/setting/pb-pay-type";
    }

    /**
     * 添加付款类型,支持批量
     *
     * @param payTypeStr
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Result batchSavePayType(@RequestParam("name") String payTypeStr, @RequestParam(value = "costType", required = false) Integer costType) {
        if (StringUtils.isBlank(payTypeStr)) {
            return Result.wrapErrorResult("", "类型名称为空");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            PayTypeParam payTypeParam = new PayTypeParam();
            payTypeParam.setTypeName(payTypeStr);
            payTypeParam.setShopId(userInfo.getShopId());
            payTypeParam.setCreator(userInfo.getUserId());
            payTypeParam.setModifier(userInfo.getUserId());
            payTypeParam.setShowStatus(OPEN_STATUS);
            if (costType != null) {
                if (!costType.equals(1) && !costType.equals(2)) {
                    return Result.wrapErrorResult("", "费用类型错误");
                }
                payTypeParam.setCostType(costType);
            }
            com.tqmall.core.common.entity.Result<Long> result = rpcPayService.saveOrUpdatePayType(payTypeParam);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用billcenter接口批量保存付款类型失败, 参数:{},调用结果:{}", new Gson().toJson(payTypeParam), new Gson().toJson(result));
                return Result.wrapErrorResult("", result.getMessage());
            }
            return Result.wrapSuccessfulResult(result.getData());
        } catch (Exception e) {
            log.error("添加付款类型失败", e);
            return Result.wrapErrorResult("", "保存付款类型失败");
        }
    }

    /**
     * 获取付款类型
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getPayType(@RequestParam(value = "action", required = false) Boolean action) {
        Long shopId = UserUtils.getShopIdForSession(request);
        log.info("[DUBBO]:调用结算中心接口获取付款类型,入参门店ID:{}", shopId);
        List<Long> shopIds = Lists.newArrayList(shopId);
        try {
            com.tqmall.core.common.entity.Result<List<PayTypeDTO>> result = rpcPayService.selectPayType(shopIds, null);
            if (!result.isSuccess()) {
                log.error("[DUBBO]:调用结算中心接口获取付款方式失败,错误原因:{}", JSONUtil.object2Json(result));
                return Result.wrapErrorResult("", "付款方式初始化失败");
            }
            log.info("[DUBBO]:调用结算中心接口获取付款方式成功");
            List<PayTypeDTO> payTypeDTOs = result.getData();
            if (action != null && action) {
                if (!CollectionUtils.isEmpty(payTypeDTOs)) {
                    Iterator it = payTypeDTOs.iterator();
                    while (it.hasNext()) {
                        PayTypeDTO typeDTO = (PayTypeDTO) it.next();
                        if (typeDTO != null) {
                            if (PayTypeEnum.SUPPLIER.getCode().equals(typeDTO.getId()) || PayTypeEnum.MESSAGE.getCode().equals(typeDTO.getId())) {
                                it.remove();
                            }
                        }
                    }
                }
            }
            return Result.wrapSuccessfulResult(payTypeDTOs);
        } catch (Exception e) {
            log.error("[DUBBO]:调用结算中心接口获取付款方式失败,错误原因:{}", e);
            return Result.wrapErrorResult("", "付款方式初始化失败");
        }
    }


    /**
     * 开启或关闭
     *
     * @return
     */
    @RequestMapping("change-status")
    @ResponseBody
    public Result changeStatus(Long id, Integer showStatus) {
        Assert.notNull(id, "[收款类型开启失败]:参数ID为空");
        Assert.notNull(showStatus, "[收款类型开启失败]");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        PayTypeParam payTypeParam = new PayTypeParam();
        payTypeParam.setId(id);
        payTypeParam.setShowStatus(showStatus);
        payTypeParam.setShopId(userInfo.getShopId());
        payTypeParam.setModifier(userInfo.getUserId());
        try {
            com.tqmall.core.common.entity.Result<Long> result = rpcPayService.saveOrUpdatePayType(payTypeParam);
            if (!result.isSuccess()) {
                log.error("[DUBBO]:调用结算中心接口修改付款方式失败,错误原因:{}", JSONUtil.object2Json(result));
                return Result.wrapErrorResult("", "付款方式修改失败");
            }
            log.info("[DUBBO]:调用结算中心接口修改付款方式成功");
        } catch (Exception e) {
            log.error("[DUBBO]:调用结算中心接口修改付款方式失败,错误原因:{}", e);
            return Result.wrapErrorResult("", "付款方式修改失败");
        }
        return Result.wrapSuccessfulResult(true);
    }

}
