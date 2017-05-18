package com.tqmall.legend.web.marketing.gather.allot;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcTagService;
import com.tqmall.cube.shop.result.tag.FreeSummaryDTO;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.bo.AllotBo;
import com.tqmall.legend.facade.customer.vo.AllotResultVo;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by zsy on 16/12/14.
 * 标准分配客户页
 */
@RequestMapping("marketing/gather/allot/normal-allot-list")
@Controller
public class GatherNormalAllotController extends BaseController {
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private RpcTagService rpcTagService;
    @Autowired
    private ShopManagerService shopManagerService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-allot";
    }

    /**
     * 标准分配客户页
     *
     * @return
     */
    @RequestMapping
    public String normalAllotList(Model model) {
        //设置归属员工（店长+老板+服务顾问）
        Long shopId = UserUtils.getShopIdForSession(request);
        List<AllotUserVo> allotUserVoList = customerUserRelFacade.getAllotUserList(shopId);
        model.addAttribute("allotUserVoList", allotUserVoList);
        //设置统计数据
        RpcResult<FreeSummaryDTO> freeSummary = rpcTagService.getFreeSummary(shopId);
        if (freeSummary.isSuccess()) {
            FreeSummaryDTO freeSummaryDTO = freeSummary.getData();
            model.addAttribute("freeSummary", freeSummaryDTO);
        }
        return "yqx/page/marketing/gather/allot/normal-allot-list";
    }

    /**
     * 查询统计数据
     *
     * @return
     */
    @RequestMapping("get-statistic")
    @ResponseBody
    public Result<FreeSummaryDTO> getStatistic(){
        return new ApiTemplate<FreeSummaryDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected FreeSummaryDTO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                RpcResult<FreeSummaryDTO> freeSummary = rpcTagService.getFreeSummary(shopId);
                if(freeSummary.isSuccess()){
                    return freeSummary.getData();
                }
                throw new BizException("查询未分配客户失败，请稍后再试");
            }
        }.execute();
    }
    /**
     * 一键分配
     *
     * @param allotBo
     * @return
     */
    @RequestMapping(value = "allot", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<AllotResultVo>> allot(@RequestBody final AllotBo allotBo) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        final Long userId = UserUtils.getUserIdForSession(request);
        return new ApiTemplate<List<AllotResultVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(allotBo, "参数为空");
                List<Long> choseUserIds = allotBo.getChoseUserIds();
                if (CollectionUtils.isEmpty(choseUserIds)) {
                    throw new IllegalArgumentException("请选择员工");
                }
                for (Long userId : choseUserIds) {
                    checkUserIdIsExist(userId, shopId);
                }
            }

            @Override
            protected List<AllotResultVo> process() throws BizException {
                allotBo.setShopId(shopId);
                allotBo.setOperatorId(userId);
                List<AllotResultVo> allotResultVoList = customerUserRelFacade.allot(allotBo);
                return allotResultVoList;
            }
        }.execute();
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
}