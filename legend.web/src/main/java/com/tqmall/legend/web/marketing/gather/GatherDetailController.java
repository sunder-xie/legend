package com.tqmall.legend.web.marketing.gather;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.legend.facade.marketing.gather.GatherDetailFacade;
import com.tqmall.legend.facade.marketing.gather.vo.GatherEffectDetailVO;
import com.tqmall.legend.facade.marketing.gather.vo.GatherOperateStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PerformanceStatVO;
import com.tqmall.legend.log.ExportLog;
import com.tqmall.wheel.component.excel.export.ExcelExportor;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by xin on 2016/12/15.
 */
@Slf4j
@Controller
@RequestMapping("/marketing/gather/detail")
public class GatherDetailController {

    @Autowired
    private CustomerUserRelFacade customerUserRelFacade;
    @Autowired
    private GatherDetailFacade gatherDetailFacade;
    @Autowired
    private ShopService shopService;

    @RequestMapping
    public String index(@RequestParam(value = "userId", required = false) Long userId,
                        @RequestParam(value = "dateStr", required = false) String dateStr,
                        Model model, HttpServletRequest request) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        // 如果是管理员
        if (userInfo.getUserIsAdmin().equals(1)) {
            // 查询已分配客户的服务顾问
            List<AllotUserVo> allotUserList = customerUserRelFacade.getAllotUserList(userInfo.getShopId(), true);
            model.addAttribute("userList", allotUserList);
            if (userId != null && userId > 0) {
                model.addAttribute("userId", userId);
            }
        } else {
            model.addAttribute("userId", userInfo.getUserId());
            model.addAttribute("userName", userInfo.getName());
        }

        if (StringUtil.isNotStringEmpty(dateStr)) {
            model.addAttribute("dateStr", dateStr);
        }
        return "yqx/page/marketing/gather/gather_detail";
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @ModelAttribute("subModule")
    public String subModule() {
        return "gather-detail";
    }


    @ResponseBody
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public Result<Page<GatherEffectDetailVO>> getGatherEffectDetailPage(@RequestParam(value = "userId", required = false) final Long userId,
                                                                        @RequestParam("dateStr") final String dateStr,
                                                                        @PageableDefault(page = 1, value = 10) final Pageable pageable,
                                                                        final HttpServletRequest request) {

        return new ApiTemplate<Page<GatherEffectDetailVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasText(dateStr);
            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected Page<GatherEffectDetailVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherDetailFacade.getGatherEffectDetailPage(shopId, userId, dateStr, pageable);
            }
        }.execute();
    }

    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void exportGatherDetail(@RequestParam(value = "userId", required = false) Long userId,
                                   @RequestParam("dateStr") String dateStr,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws IOException, WheelException {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        long startTime = System.currentTimeMillis();
        String fileName = "集客详情客户列表-" + DateUtil.convertDateToStr(new Date(), "yyyyMMdd");
        ExcelExportor exportor = null;
        int totalSize = 0;
        try {
            exportor = ExcelHelper.createDownloadExportor(response, fileName);
            Shop shop = shopService.selectById(userInfo.getShopId());
            String headline = shop.getName() + "——集客详情客户列表";
            exportor.writeTitle(null, headline, GatherEffectDetailVO.class);
            int pageNum = 1;
            int pageSize = 500;
            while (true) {
                List<GatherEffectDetailVO> gatherDetailList = gatherDetailFacade.getGatherDetailList(userInfo.getShopId(), userId, dateStr, pageNum, pageSize);
                if (CollectionUtils.isEmpty(gatherDetailList)) {
                    break;
                }
                exportor.write(gatherDetailList);
                totalSize += gatherDetailList.size();
                pageNum++;
            }
        } finally {
            ExcelHelper.closeQuiet(exportor);
        }
        long endTime = System.currentTimeMillis();
        log.info(ExportLog.getExportLog("集客详情客户列表", userInfo, totalSize, endTime - startTime));
    }


    @ResponseBody
    @RequestMapping(value = "operate/stat", method = RequestMethod.GET)
    public Result<GatherOperateStatVO> getGatherOperateStat(@RequestParam(value = "userId", required = false) final Long userId,
                                                            @RequestParam("dateStr") final String dateStr,
                                                            final HttpServletRequest request) {
        return new ApiTemplate<GatherOperateStatVO>() {
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
            protected GatherOperateStatVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherDetailFacade.getGatherOperateStat(shopId, userId, dateStr);
            }
        }.execute();
    }

    @ResponseBody
    @RequestMapping(value = "performance/stat", method = RequestMethod.GET)
    public Result<PerformanceStatVO> getPerformanceStat(@RequestParam(value = "userId", required = false) final Long userId,
                                                        @RequestParam("dateStr") final String dateStr,
                                                        final HttpServletRequest request) {
        return new ApiTemplate<PerformanceStatVO>() {
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
            protected PerformanceStatVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return gatherDetailFacade.getPerformanceStat(shopId, userId, dateStr);
            }
        }.execute();
    }
}
