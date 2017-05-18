package com.tqmall.legend.web.sys;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.sys.IExportTrackService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.sys.ExportTrack;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 导出记录 controller
 */
@Slf4j
@Controller
@RequestMapping("shop/exporttrack")
public class ExportTrackController extends BaseController {

    @Autowired
    IExportTrackService exportTrackService;


    /**
     * 增加导出记录
     *
     * @param operateBrief 导出记录简介
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@RequestParam(value = "brief", required = true) String operateBrief) {
        UserInfo userInfo = UserUtils.getUserInfo(request);

        ExportTrack exportTrack = new ExportTrack();
        exportTrack.setShopId(userInfo.getShopId());
        exportTrack.setOperatorId(userInfo.getUserId().intValue());
        exportTrack.setOperatorName(userInfo.getName());
        exportTrack.setOperateBrief(operateBrief);
        try {
            exportTrackService.insert(exportTrack, userInfo);
        } catch (Exception e) {
            log.error("数据导出记录失败,原因:{}", e);
        }

        return Result.wrapSuccessfulResult("");
    }


    /**
     * 分页记录
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseBody
    public Result page(@PageableDefault(page = 1, value = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        Long shopId = UserUtils.getShopIdForSession(request);
        searchParams.put("shopId", shopId);
        DefaultPage<ExportTrack> page = (DefaultPage<ExportTrack>) exportTrackService.getPage(pageable, searchParams);
        page.setPageUri(request.getRequestURI());
        page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
        return Result.wrapSuccessfulResult(page);
    }
}
