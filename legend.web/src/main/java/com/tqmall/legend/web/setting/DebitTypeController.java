package com.tqmall.legend.web.setting;

import com.google.gson.Gson;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.billcenter.client.RpcDebitBillService;
import com.tqmall.legend.billcenter.client.dto.DebitTypeDTO;
import com.tqmall.legend.billcenter.client.param.DebitTypeParam;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lilige on 16/12/5.
 */
@Slf4j
@Controller
@RequestMapping("shop/setting/debit-type")
public class DebitTypeController extends BaseController{
    @Autowired
    private RpcDebitBillService rpcDebitBillService;

    private final static Integer OPEN_STATUS = 1;

    /**
     * 收款业务类型
     * @return
     */
    @RequestMapping("")
    public String debitTypeSetting(Model model){
        model.addAttribute("modelUrl", ModuleUrlEnum.SETTINGS.getModuleUrl());
        return "yqx/page/setting/debit-type";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Result addDebitType(@RequestParam("name") String debitTypeStr){
        if (StringUtils.isBlank(debitTypeStr)){
            return Result.wrapErrorResult("", "类型名称为空");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        try {
            DebitTypeParam debitTypeParam = new DebitTypeParam();
            debitTypeParam.setTypeName(debitTypeStr);
            debitTypeParam.setShopId(userInfo.getShopId());
            debitTypeParam.setCreator(userInfo.getUserId());
            debitTypeParam.setModifier(userInfo.getUserId());
            debitTypeParam.setShowStatus(OPEN_STATUS);
            com.tqmall.core.common.entity.Result<Long> result = rpcDebitBillService.saveOrUpdateDebitType(debitTypeParam);
            if (!result.isSuccess()) {
                log.error("[DUBBO]调用billcenter接口批量保存收款类型失败, 参数:{},调用结果:{}", new Gson().toJson(debitTypeParam), new Gson().toJson(result));
                return Result.wrapErrorResult("", result.getMessage());
            }
            return Result.wrapSuccessfulResult(result.getData());
        } catch (Exception e) {
            log.error("[添加收款类型失败]",e);
            return Result.wrapErrorResult("", "保存收款类型失败");
        }
        //跳转至服务类别页面
    }

    /**
     * 按门店获取收款类型列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result getDebitTypeList(){
        Long shopId =  UserUtils.getShopIdForSession(request);
        try {
            com.tqmall.core.common.entity.Result<List<DebitTypeDTO>> result= rpcDebitBillService.findDebitTypeListByShopId(shopId,null);
            if(null == result || !result.isSuccess()){
                log.error("调用DUBBO接口获取收款业务类型失败,result:{}",result);
                return Result.wrapErrorResult("","调用DUBBO接口获取收款业务类型失败");
            }
            List<DebitTypeDTO> typeList = result.getData();
            Collections.sort(typeList, new Comparator<DebitTypeDTO>() {
                @Override
                public int compare(DebitTypeDTO o1, DebitTypeDTO o2) {
                    Long i = o1.getShopId()-o2.getShopId();
                    return i.intValue();
                }
            });
            return Result.wrapSuccessfulResult(typeList);
        } catch (Exception e) {
            log.error("调用DUBBO接口获取收款业务类型失败,shopId:{}",shopId,e);
        }
        return Result.wrapErrorResult("","调用DUBBO接口获取收款业务类型失败");
    }

    /**
     * 开启或关闭
     * @return
     */
    @RequestMapping("change-status")
    @ResponseBody
    public Result<Boolean> changeStatus( Long id, Integer showStatus){
        Assert.notNull(id , "[收款类型开启失败]:参数ID为空");
        Assert.notNull(showStatus , "[收款类型开启失败]");
        UserInfo userInfo = UserUtils.getUserInfo(request);
        DebitTypeParam debitTypeParam = new DebitTypeParam();
        debitTypeParam.setId(id);
        debitTypeParam.setShowStatus(showStatus);
        debitTypeParam.setShopId(userInfo.getShopId());
        debitTypeParam.setModifier(userInfo.getUserId());
        try {
            com.tqmall.core.common.entity.Result<Long> result = rpcDebitBillService.saveOrUpdateDebitType(debitTypeParam);
            if (!result.isSuccess()) {
                log.error("[DUBBO]:调用结算中心接口修改收款方式失败,错误原因:{}", JSONUtil.object2Json(result));
                return Result.wrapErrorResult("", "收款方式修改失败");
            }
            log.info("[DUBBO]:调用结算中心接口修改收款方式成功");
        } catch (Exception e) {
            log.error("[DUBBO]:调用结算中心接口修改收款方式失败,错误原因:{}", e);
            return Result.wrapErrorResult("", "收款方式修改失败");
        }
        return Result.wrapSuccessfulResult(true) ;
    }


}
