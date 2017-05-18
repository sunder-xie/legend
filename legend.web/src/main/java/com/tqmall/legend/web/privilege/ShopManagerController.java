package com.tqmall.legend.web.privilege;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.config.ShopManagerDeviceConfigService;
import com.tqmall.legend.biz.privilege.ShopManagerLoginService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.common.MD5Util;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import com.tqmall.legend.enums.config.ManagerDeviceConfigStatusEnum;
import com.tqmall.legend.object.result.order.WorkerDTO;
import com.tqmall.legend.service.order.RpcAppOrderService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by QXD on 2015/1/16.
 */
@Slf4j
@Controller
@RequestMapping("shop/manager")
public class ShopManagerController extends BaseController {

    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private ShopManagerLoginService shopManagerLoginService;
    @Autowired
    private ShopManagerDeviceConfigService shopManagerDeviceConfigService;
    @Autowired
    private RpcAppOrderService rpcAppOrderService;

    /**
     * 根据shop_id查询店铺的员工
     *
     * @param request
     * @return 员工列表
     */
    @RequestMapping(value = "get_manager")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<ShopManager>> getRolesByShopId(final HttpServletRequest request) {
        return new ApiTemplate<List<ShopManager>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<ShopManager> process() throws BizException {
                long shop_id = UserUtils.getShopIdForSession(request);

                List<ShopManager> shopManagerList = shopManagerService.selectByShopId(shop_id);
                if (CollectionUtils.isEmpty(shopManagerList)) {
                    throw new BizException("获取角色列表失败");
                }
                return shopManagerList;
            }
        }.execute();
    }

    /**
     * 获取洗车工
     *
     * @return
     */
    @RequestMapping("get-carwash-worker")
    @ResponseBody
    public Result getCarWashWorker(){
        Long shopId = UserUtils.getShopIdForSession(request);
        com.tqmall.core.common.entity.Result<List<WorkerDTO>> carWashWorkerListResult = rpcAppOrderService.getCarWashWorkerList(shopId);
        if(carWashWorkerListResult.isSuccess()){
            return Result.wrapSuccessfulResult(carWashWorkerListResult.getData());
        }else{
            return Result.wrapErrorResult("",carWashWorkerListResult.getMessage());
        }
    }

    /**
     * 设备授权操作：同意、拒绝、删除
     *
     * @param type
     * @return
     */
    @RequestMapping(value = "devices/{type}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> devicesAutho(@PathVariable final String type, @PathVariable final Long id) {
        return new ApiTemplate<String>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected String process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Integer isAdmin = userInfo.getUserIsAdmin();
                if (isAdmin != 1) {
                    throw new BizException("您无权操作此功能");
                }
                if (StringUtils.equals(type, "agree") &&
                        shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_AGREE.getCode())) {
                    return "设备授权成功";
                }
                if (StringUtils.equals(type, "refuse") &&
                        shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_REFUSE.getCode())) {
                    return "设备授权成功";
                }
                if (StringUtils.equals(type, "delete") &&
                        shopManagerDeviceConfigService.updateDeviceConfigStatus(id, userInfo.getUserId(), ManagerDeviceConfigStatusEnum.AUTHOR_UNBUND.getCode())) {
                    return "设备解绑成功";
                }
                throw new BizException("设备授权操作失败");
            }
        }.execute();
    }

    /**
     * 当前登录账户密码校验
     *
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "check-password", method = RequestMethod.POST)
    @ResponseBody
    public Result checkPassword(@RequestParam(value = "password") final String password) {
        com.tqmall.core.common.entity.Result result = new ApiTemplate<Boolean>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(password, "密码不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                //查询login的密码
                Long userId = userInfo.getUserId();
                ShopManagerLogin shopManagerLogin = shopManagerLoginService.selectShopManagerByManagerId(userId);
                if (shopManagerLogin == null) {
                    throw new BizException("账户不存在，请重新登录");
                }
                String passwordMD5 = shopManagerLogin.getPassword();
                String checkPasswordMD5 = MD5Util.MD5(password);
                if (passwordMD5.equals(checkPasswordMD5)) {
                    return true;
                }
                throw new BizException("密码不正确");
            }
        }.execute();
        //返回前台用common的result
        if (result.isSuccess()) {
            return Result.wrapSuccessfulResult(result.getData());
        } else {
            return Result.wrapErrorResult("", result.getMessage());
        }
    }
}
