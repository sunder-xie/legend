package com.tqmall.legend.web.privilege;

import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.shop.ShopManagerBalanceFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by QXD on 2014/10/30.
 * 1.根据shop_id查询店铺的角色
 * 2.添加帐号
 * 3.删除帐号(根据用户帐号id)
 * 4.根据店铺ID获取店铺的员工
 */
@Slf4j
@Controller
@RequestMapping("shop/roles_func")
public class RolesFuncController extends BaseController {
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerBalanceFacade shopManagerBalanceFacade;
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;

    /**
     * 检查员工账号是否有1.'提现中金额' 2.可提现余额 3.'提现中'记录
     * @param request
     * @return
     */
    @RequestMapping(value = "check_balance", method = RequestMethod.GET)
    @ResponseBody
    public Result checkUserBalanceBeforeDelete(@RequestParam(value = "managerId", required = false) Long managerId,  HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (null == managerId || managerId < Constants.MANAGER_ID_FLAG) {
            log.info("[检查用户提现中金额和可提现余额] 检查失败,用户信息错误; managerId:{}", managerId);
            return Result.wrapErrorResult("", "检查用户提现余额失败,用户信息错误。");
        }
        Result result = shopManagerBalanceFacade.checkUserBalance(managerId, shopId);
        return result;
    }

    /**
     * 校验手机号是否存在其他门店，如果存在提示是否添加
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "check-mobile", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> checkMobile(@RequestParam(value = "mobile") final String mobile) {
        //校验是否其他门店存在此mobile
        com.tqmall.core.common.entity.Result<Boolean> result = new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(mobile,"手机号不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                final Long shopId = UserUtils.getShopIdForSession(request);
                Map map = new HashMap(1);
                map.put("mobile", mobile);
                List<ShopManager> shopManagerList = shopManagerService.select(map);
                if(CollectionUtils.isEmpty(shopManagerList)){
                    return false;
                }
                for(ShopManager shopManager : shopManagerList){
                    Long checkShopId = shopManager.getShopId();
                    if(Long.compare(checkShopId,shopId) != 0){
                        return true;
                    }
                }
                return false;
            }
        }.execute();

        if(result.isSuccess()){
            return Result.wrapSuccessfulResult(result.getData());
        }
        return Result.wrapErrorResult(result.getCode(),result.getMessage());
    }

    /**
     * 查询员工客户归属数量
     *
     * @param managerId
     * @return
     */
    @RequestMapping(value = "check-user-allot", method = RequestMethod.GET)
    @ResponseBody
    public Result<Integer> checkUserAllot(@RequestParam(value = "managerId") Long managerId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (null == managerId || managerId < Constants.MANAGER_ID_FLAG) {
            return Result.wrapErrorResult("", "用户信息错误");
        }
        //样板店才查询归属，无需弹框提示
        Integer userAllotCount;
        if(UserUtils.isYBD(request)){
            userAllotCount = customerUserRelFacade.userAllotCount(shopId, managerId);
        } else {
            userAllotCount = 0;
        }
        return Result.wrapSuccessfulResult(userAllotCount);
    }
}
