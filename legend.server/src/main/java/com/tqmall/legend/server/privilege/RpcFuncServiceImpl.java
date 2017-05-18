package com.tqmall.legend.server.privilege;


import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.privilege.FuncService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.entity.privilege.FuncF;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.object.param.privilege.RpcFuncParam;
import com.tqmall.legend.service.privilege.RpcFuncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限
 * Created by zsy on 16/6/16.
 */
@Slf4j
@Service ("rpcFuncService")
public class RpcFuncServiceImpl implements RpcFuncService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private FuncService funcService;
    @Autowired
    private ShopService shopService;
    /**
     * 权限校验
     * @param rpcFuncParam
     * @return
     */
    @Override
    public Result<Boolean> checkFunc(RpcFuncParam rpcFuncParam) {
        log.info("【dubbo：权限校验】：传参{}",rpcFuncParam);
        if(rpcFuncParam == null){
            log.info("【dubbo：权限校验】：参数为空");
            return Result.wrapSuccessfulResult(false);
        }
        String source = rpcFuncParam.getSource();
        if(StringUtils.isBlank(source)){
            log.info("【dubbo：权限校验】：来源为空");
            return Result.wrapSuccessfulResult(false);
        }
        Long userId = rpcFuncParam.getUserId();
        if(userId == null || Long.compare(userId,0l) <= 0){
            log.info("【dubbo：权限校验】：用户id有误,userId：{}",userId);
            return Result.wrapSuccessfulResult(false);
        }
        Long shopId = rpcFuncParam.getShopId();
        if(shopId == null || Long.compare(shopId,0l) <= 0){
            log.info("【dubbo：权限校验】：门店id有误,shopId：{}",shopId);
            return Result.wrapSuccessfulResult(false);
        }
        Shop shop = shopService.selectById(shopId);
        if(shop == null){
            log.info("【dubbo：权限校验】：门店不存在,shopId：{}",shopId);
            return Result.wrapSuccessfulResult(false);
        }
        String funcName = rpcFuncParam.getFuncName();
        if(StringUtils.isBlank(funcName)){
            log.info("【dubbo：权限校验】：权限传参为空");
            return Result.wrapSuccessfulResult(false);
        }
        // 查询用户角色
        ShopManager userInfo = userInfoService.getUserInfo(userId);
        if (userInfo == null) {
            log.info("【dubbo：权限校验】：用户信息不存在,userId：{}",userId);
            return Result.wrapSuccessfulResult(false);
        }
        //超级管理员
        if(userInfo.getIsAdmin() == 1){
            return Result.wrapSuccessfulResult(true);
        }
        Long roleId = userInfo.getRoleId();
        if(roleId == null || Long.compare(shopId,0l) <= 0){
            log.info("【dubbo：权限校验】：roleId有误：{}",roleId);
            return Result.wrapSuccessfulResult(false);
        }
        List<FuncF> funcFList = funcService.getFuncFsForUser(roleId, shopId, shop.getLevel());
        if(!CollectionUtils.isEmpty(funcFList)){
            for(FuncF funcF : funcFList){
                String name = funcF.getName();
                if(funcName.equals(name)){
                    return Result.wrapSuccessfulResult(true);
                }
            }
        }
        return Result.wrapSuccessfulResult(false);
    }
}
