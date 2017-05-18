package com.tqmall.legend.web.marketing.gather;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.facade.marketing.gather.GatherEffectFacade;
import com.tqmall.legend.facade.marketing.gather.vo.LaXinStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PanHuoStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xin on 2016/12/15.
 * <p/>
 * 集客效果Controller
 */
@Controller
@RequestMapping("/marketing/gather/effect")
public class GatherEffectController {

    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private GatherEffectFacade gatherEffectFacade;

    @RequestMapping
    public String index(Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 如果是管理员
        if (userInfo.getUserIsAdmin().equals(1)) {
            // 查询已分配客户的服务顾问
            List<AllotUserVo> allotUserList = customerUserRelFacade.getAllotUserList(userInfo.getShopId(), true);
            model.addAttribute("userList", allotUserList);
        } else {
            model.addAttribute("userId", userInfo.getUserId());
            model.addAttribute("userName", userInfo.getName());
        }
        return "yqx/page/marketing/gather/gather_effect";
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-effect";
    }

    /**
     * 统计盘活客户效果
     *
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "panhuo/stat", method = RequestMethod.GET)
    public Result<List<PanHuoStatVO>> getGatherPanHuoStat(@RequestParam(value = "userId", required = false) final Long userId,
                                                          @RequestParam("dateStr") final String dateStr,
                                                          final HttpServletRequest request) {
        return new ApiTemplate<List<PanHuoStatVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<PanHuoStatVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherEffectFacade.getPanHuoStat(shopId, userId, dateStr);
            }
        }.execute();
    }

    /**
     * 统计拉新客户效果
     * @param userId
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "laxin/stat", method = RequestMethod.GET)
    public Result<LaXinStatVO> getGatherLaXinStat(@RequestParam(value = "userId", required = false) final Long userId,
                                                  @RequestParam("dateStr") final String dateStr,
                                                  final HttpServletRequest request) {
        return new ApiTemplate<LaXinStatVO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected LaXinStatVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherEffectFacade.getLaXinStat(shopId, userId, dateStr);
            }
        }.execute();
    }
}
