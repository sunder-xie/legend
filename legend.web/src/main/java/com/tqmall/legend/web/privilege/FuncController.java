package com.tqmall.legend.web.privilege;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.enums.shop.ShopTagCodeEnum;
import com.tqmall.legend.enums.wechat.ShopWechatStatusEnum;
import com.tqmall.legend.facade.shop.ShopFunFacade;
import com.tqmall.legend.facade.wechat.WechatFacade;
import com.tqmall.legend.facade.wechat.vo.ShopWechatVo;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by QXD on 2014/11/9.
 */
@Slf4j
@Controller("FuncController")
@RequestMapping("shop/func")
public class FuncController extends BaseController {
    @Autowired
    private ShopFunFacade shopFunFacade;

    @Autowired
    private WechatFacade wechatFacade;

    /**
     * 校验角色是否有某个权限
     *
     * @param funcName
     * @return
     */
    @RequestMapping(value = "check_func", method = RequestMethod.GET)
    @ResponseBody
    public Result checkFunc(@RequestParam(value = "funcName",required = true)String funcName) {
        if(StringUtils.isBlank(funcName)){
            return Result.wrapSuccessfulResult(false);
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        boolean isOwn = shopFunFacade.checkFuncAnd(userInfo,funcName);
        return Result.wrapSuccessfulResult(isOwn);
    }

    /**
     * 校验角色是否有权限列表中任意一个权限
     *
     * @return
     */
    @RequestMapping(value = "check_func_list", method = RequestMethod.GET)
    @ResponseBody
    public Result checkFuncList(@RequestParam("funcNameList") List<String> funcNameList, HttpServletRequest request) {
        if (CollectionUtils.isEmpty(funcNameList)) {
            return Result.wrapErrorResult("", "权限列表为空");
        }
        String contextPath = request.getContextPath();

        UserInfo userInfo = UserUtils.getUserInfo(request);
        //管理员有权限
        if(userInfo.getUserIsAdmin().equals(1)){//userIsAdmin1表示是管理员
            return Result.wrapSuccessfulResult(contextPath + "/marketing");
        }
        //获取权限列表
        String userRoleFunc = userInfo.getUserRoleFunc();
        if(StringUtils.isBlank(userRoleFunc)){
            return Result.wrapErrorResult("", "该用户没有任何权限");
        }
        List<FuncF> funcFList = null;
        try {
            funcFList = new Gson().fromJson(userRoleFunc, new TypeToken<List<FuncF>>() {
            }.getType());

        } catch (JsonSyntaxException e) {
            log.error("【校验角色是否有某个权限】，json解析有误，出现异常,权限为{}",userRoleFunc,e);
            return Result.wrapErrorResult("", "该用户没有任何权限");
        }
        Map<String, String> funcFMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(funcFList)) {
            for(FuncF funcF : funcFList){
                String fName = funcF.getName();
                String fValue = funcF.getValue();
                funcFMap.put(fName, fValue);
            }
            for (String funcName : funcNameList) {
                if (funcFMap.containsKey(funcName)) {
                    return Result.wrapSuccessfulResult(contextPath + funcFMap.get(funcName));
                }
            }
        }
        return Result.wrapErrorResult("", "没有权限");
    }

    /**
     * 获取当前用户权限列表和门店属性
     * @return
     */
    @RequestMapping(value = "get_func_list", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Map<String, Object>> getFuncList() {
        return new ApiTemplate<Map<String, Object>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Map<String, Object> process() throws BizException {
                Map<String,Object> result = new HashedMap();
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                result.put(Constants.SESSION_USER_IS_ADMIN, userInfo.getUserIsAdmin());
                String userRoleFunc = userInfo.getUserRoleFunc();
                if (StringUtils.isNotBlank(userRoleFunc)) {
                    List<FuncF> funcList = new Gson().fromJson(userRoleFunc, new TypeToken<List<FuncF>>() {
                    }.getType());
                    result.put("funcList", funcList);
                }
                result.put(Constants.BPSHARE, request.getAttribute(Constants.BPSHARE));//是否是钣喷中心 true/false
                result.put(Constants.SESSION_SHOP_JOIN_STATUS, request.getAttribute(Constants.SESSION_SHOP_JOIN_STATUS));//共享中心,0不参加 1参加体系
                result.put(Constants.SESSION_SHOP_IS_TQMALL_VERSION, request.getAttribute(Constants.SESSION_SHOP_IS_TQMALL_VERSION));//门店是否为档口版 true/false
                result.put(Constants.SESSION_SHOP_WORKSHOP_STATUS, request.getAttribute(Constants.SESSION_SHOP_WORKSHOP_STATUS));//门店是否使用车间,0：不使用车间 1：使用车间
                result.put(Constants.SESSION_WAREHOUSE_SHARE_ROLE, request.getAttribute(Constants.SESSION_WAREHOUSE_SHARE_ROLE));//库存共享,true/false
                result.put(Constants.SESSION_SHOP_LEVEL, request.getAttribute(Constants.SESSION_SHOP_LEVEL));//门店版本：6档口版，9云修版，10基础版，11标准版，12专业版
                //.是否样板店
                Boolean isYBD = shopFunFacade.isYBD(shopId);
                result.put(ShopTagCodeEnum.YBD.getTagCode(), isYBD.toString());//是否样板店 true/false
                int isWechatShop = getIsWechatShop(shopId);
                result.put(Constants.IS_WECHAT_SHOP, isWechatShop);//是否开通微信公众号 0未开通,1已开通

                //头部菜单版本
                String headerMenuVersion = (String)request.getAttribute("header_menu_version");
                result.put("HEADER_MENU_VERSION" , headerMenuVersion);

                return result;
            }
        }.execute();
    }

    /**
     * 是否开通微信公众号的门店 0未开通,1已开通,方法超时时间是3s
     * @param shopId
     * @return
     */
    private int getIsWechatShop(final Long shopId) {
        final ExecutorService exec = Executors.newFixedThreadPool(1);
        Callable<Integer> call = new Callable<Integer>() {
            public Integer call() throws Exception {
                Result<ShopWechatVo> wechatShopResult = wechatFacade.qryShopWechat(shopId);
                ShopWechatVo shopWechatVo = wechatShopResult.getData();
                int isWechatShop = 0;//0未开通,1已开通
                if (shopWechatVo != null) {
                    if (shopWechatVo.getShopStatus() != null && shopWechatVo.getShopStatus().equals(ShopWechatStatusEnum.REGISTERED.getValue())) {
                        isWechatShop = 1;
                    }
                }
                return isWechatShop;
            }
        };

        Integer isWechatShop = 0;
        try {
            Future<Integer> future = exec.submit(call);
            isWechatShop = future.get(3, TimeUnit.SECONDS); //任务处理超时时间设为3 秒
        } catch (TimeoutException te) {
            log.error("查询门店微信公众号信息超时", te);
        } catch (Exception e) {
            log.error("查询门店微信公众号信息异常", e);
        } finally {
            if (exec != null) {
                // 关闭线程池
                exec.shutdown();
            }
        }
        isWechatShop = isWechatShop == null ? 0 : isWechatShop;
        return isWechatShop;
    }

}
