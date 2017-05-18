package com.tqmall.legend.web.marketing.gather.allot;

import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcTagService;
import com.tqmall.cube.shop.result.tag.UserStatisticDTO;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zsy on 16/12/14.
 * 分配客户
 */
@Slf4j
@RequestMapping("marketing/gather/allot/allot-list")
@Controller
public class GatherAllotController extends BaseController {

    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private RpcTagService rpcTagService;

    /**
     * 分配方案结果页
     *
     * @return
     */
    @RequestMapping
    public String allotList() {
        return "yqx/page/marketing/gather/allot/allot-list";
    }

    /**
     * 获取员工分配结果统计数据
     *
     * @return
     */
    @RequestMapping("get-statistic")
    @ResponseBody
    public Result<List<UserStatisticDTO>> getUserStatistic() {
        return new ApiTemplate<List<UserStatisticDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<UserStatisticDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                log.info("调用cube查询分配客户统计结果,门店id为:{}", shopId);
                RpcResult<List<UserStatisticDTO>> listRpcResult = rpcTagService.listUserStatistic(shopId);
                List<UserStatisticDTO> userStatisticDTOList;
                if (listRpcResult.isSuccess()) {
                    userStatisticDTOList = listRpcResult.getData();
                } else {
                    userStatisticDTOList = Lists.newArrayList();
                }
                return userStatisticDTOList;
            }
        }.execute();
    }

    /**
     * 单个、多个客户调整接口
     *
     * @param customerCarIds 调整的客户ids
     * @param oldUserIds     原员工ids
     * @param newUserId      新员工id
     * @return
     */
    @RequestMapping(value = "change-binding", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> changeBinding(final Long[] customerCarIds, final Long[] oldUserIds, final Long newUserId) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(customerCarIds, "请选择需要调整的客户");
                Assert.notNull(oldUserIds, "原员工信息为空");
                Assert.notNull(newUserId, "请选择需要调整的员工");
                if(customerCarIds.length == 0){
                    throw new IllegalArgumentException("请选择需要调整的客户");
                }
                if(oldUserIds.length == 0){
                    throw new IllegalArgumentException("请选择需要调整的客户");
                }
                for (Long oldUserId : oldUserIds) {
                    if (Long.compare(oldUserId, 0l) == 1) {
                        checkUserIdIsExist(oldUserId, shopId);
                        checkIsAllot(oldUserId, shopId);
                    }
                }
                checkUserIdIsExist(newUserId, shopId);
            }

            @Override
            protected Boolean process() throws BizException {
                Long operatorId = UserUtils.getUserIdForSession(request);
                List<Long> customerCarIdList = Lists.newArrayList();
                for (Long customerCarId : customerCarIds) {
                    customerCarIdList.add(customerCarId);
                }
                customerUserRelFacade.allotCustomerCars(shopId, newUserId, customerCarIdList, operatorId);
                return true;
            }
        }.execute();
    }

    /**
     * 客户全部调整接口
     *
     * @param oldUserId 老员工
     * @param newUserId 新员工
     * @return
     */
    @RequestMapping(value = "change-binding-all", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> changeBindingAll(final Long oldUserId, final Long newUserId) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {

            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(oldUserId, "请选择需要调整的员工");
                Assert.notNull(newUserId, "前选择需要调整的员工");
                if (oldUserId.equals(newUserId)) {
                    throw new IllegalArgumentException("员工相同，无需调整");
                }
                checkUserIdIsExist(oldUserId, shopId);
                checkUserIdIsExist(newUserId, shopId);
                checkIsAllot(oldUserId, shopId);
            }

            @Override
            protected Boolean process() throws BizException {
                Long operatorId = UserUtils.getUserIdForSession(request);
                customerUserRelFacade.transformAllotCustomerCars(shopId, oldUserId, newUserId, operatorId);
                return true;
            }
        }.execute();
    }

    /**
     * 客户全部解绑接口
     *
     * @param userId 解绑客户
     * @return
     */
    @RequestMapping(value = "unbinding-all", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> unbindingAll(final Long userId) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(userId, "参数为空");
                checkUserIdIsExist(userId, shopId);
                checkIsAllot(userId, shopId);
            }

            @Override
            protected Boolean process() throws BizException {
                Long operatorId = UserUtils.getUserIdForSession(request);
                customerUserRelFacade.unAllotByUserId(shopId, userId, operatorId);
                return true;
            }
        }.execute();
    }

    /**
     * 客户单个、多个解绑接口
     *
     * @param customerCarIds 解绑客户ids
     * @return
     */
    @RequestMapping(value = "unbinding", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> unbinding(final Long[] customerCarIds) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(customerCarIds, "参数为空");
                if (customerCarIds.length == 0) {
                    throw new IllegalArgumentException("请选择需要解绑的客户");
                }
            }

            @Override
            protected Boolean process() throws BizException {
                Long operatorId = UserUtils.getUserIdForSession(request);
                customerUserRelFacade.unAllotCustomerCarIds(shopId,operatorId, customerCarIds);
                return true;
            }
        }.execute();
    }

    /**
     * 校验是否有可分配的客户
     *
     * @param userId
     * @param shopId
     * @throws BizException
     */
    private void checkIsAllot(Long userId, Long shopId) throws IllegalArgumentException {
        //校验是否有分配数据
        boolean isAllot = customerUserRelFacade.isAllot(shopId, userId);
        if (!isAllot) {
            throw new IllegalArgumentException("员工id:" + userId + "没有归属客户");
        }
    }

    /**
     * 校验客户是否存在
     *
     * @param userId
     * @param shopId
     * @return
     */
    private void checkUserIdIsExist(Long userId, Long shopId) throws IllegalArgumentException {
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerId(shopId, userId);
        if (shopManager == null) {
            StringBuffer errorMsg = new StringBuffer("员工不存在，id：");
            errorMsg.append(userId);
            throw new IllegalArgumentException(errorMsg.toString());
        }
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-allot";
    }
}