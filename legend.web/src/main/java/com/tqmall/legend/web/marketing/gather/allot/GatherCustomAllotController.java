package com.tqmall.legend.web.marketing.gather.allot;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.cube.shop.RpcTagService;
import com.tqmall.cube.shop.result.tag.FreeSummaryDTO;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.bo.CustomAllotBo;
import com.tqmall.legend.facade.customer.vo.AllotResultVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by zsy on 16/12/14.
 * 自定义分配客户
 */
@RequestMapping("marketing/gather/allot/custom-allot-list")
@Controller
public class GatherCustomAllotController extends BaseController {

    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private RpcTagService rpcTagService;
    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private MemberCardInfoService memberCardInfoService;

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-allot";
    }

    /**
     * 自定义分配客户页
     *
     * @return
     */
    @RequestMapping
    public String customAllotList(Model model) {
        //设置统计数据
        Long shopId = UserUtils.getShopIdForSession(request);
        RpcResult<FreeSummaryDTO> freeSummary = rpcTagService.getFreeSummary(shopId);
        if (freeSummary.isSuccess()) {
            FreeSummaryDTO freeSummaryDTO = freeSummary.getData();
            model.addAttribute("freeSummary", freeSummaryDTO);
        }
        List<MemberCardInfo> memberCardInfoList = memberCardInfoService.findAllByShopId(shopId, 0);
        model.addAttribute("memberCardInfoList", memberCardInfoList);
        return "yqx/page/marketing/gather/allot/custom-allot-list";
    }

    /**
     * 平均分配
     *
     * @param customAllotBo
     * @return
     */
    @RequestMapping(value = "custom-allot", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<AllotResultVo>> customAllot(@RequestBody final CustomAllotBo customAllotBo) {
        final Long shopId = UserUtils.getShopIdForSession(request);
        final Long userId = UserUtils.getUserIdForSession(request);
        return new ApiTemplate<List<AllotResultVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(customAllotBo, "参数为空");
                List<Long> choseUserIds = customAllotBo.getChoseUserIds();
                if (CollectionUtils.isEmpty(choseUserIds)) {
                    throw new IllegalArgumentException("请选择员工");
                }
                for (Long userId : choseUserIds) {
                    checkUserIdIsExist(userId, shopId);
                }
            }

            @Override
            protected List<AllotResultVo> process() throws BizException {
                customAllotBo.setShopId(shopId);
                customAllotBo.setOperatorId(userId);
                List<AllotResultVo> allotResultVoList = customerUserRelFacade.customAllot(customAllotBo);
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