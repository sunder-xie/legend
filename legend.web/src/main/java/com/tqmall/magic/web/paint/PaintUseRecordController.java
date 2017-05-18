package com.tqmall.magic.web.paint;

import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.magic.PaintUseRecordFacade;
import com.tqmall.legend.facade.magic.vo.PaintRecordDetailVo;
import com.tqmall.legend.facade.magic.vo.PaintUseRecordVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.magic.object.param.paint.PaintUseRecordParam;
import com.tqmall.magic.object.result.paint.PaintRecordDetailDTO;
import com.tqmall.magic.service.paint.RpcPaintRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 油漆使用记录
 * Created by shulin on 16/11/15.
 */
@Controller
@RequestMapping("shop/paint/record")
@Slf4j
public class PaintUseRecordController extends BaseController {

    @Autowired
    private RpcPaintRecordService rpcPaintRecordService;

    @Autowired
    private PaintUseRecordFacade paintUseRecordFacade;

    @Autowired
    private SnFactory snFactory;

    @Autowired
    private ShopDao shopDao;

    /**
     * 跳转到油漆记录列表
     *
     * @return
     */
    @RequestMapping("toPaintUseRecordList")
    public String toPaintUseList(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/magic/paint/paintUseRecordList";
    }


    /**
     * 获取油漆记录列表信息
     *
     * @param request
     * @param pageable
     * @return
     */
    @RequestMapping("getPaintUseRecords")
    @ResponseBody
    public Result<DefaultPage<PaintUseRecordVo>> getPaintUseRecordListByPage(HttpServletRequest request, @PageableDefault(page = 1, value = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        DefaultPage<PaintUseRecordVo> recordVoList = null;
        PaintUseRecordParam param = new PaintUseRecordParam();
        param.setShopId(UserUtils.getShopIdForSession(request));
        String startTime = request.getParameter("startTime");
        if (startTime != null) {
            param.setWarehouseOutStartTime(DateUtil.convertStringToDateYMD(startTime));
        }
        String endTime = request.getParameter("endTime");
        if (endTime != null) {
            param.setWarehouseOutEndTime(DateUtil.convertStringToDateYMD(endTime));
        }
        param.setPageNum(pageable.getPageNumber());
        param.setPageSize(pageable.getPageSize());
        try {
            recordVoList = paintUseRecordFacade.getPaintUseRecordListByPage(param, pageable);
        } catch (Exception e) {
            log.error("[油漆使用记录] 分页查询列表失败！", e);
            return Result.wrapErrorResult("", "获取油漆使用记录列表失败！");
        }
        if (null == recordVoList) {
            return Result.wrapErrorResult("", "无油漆使用记录！");
        }
        if (CollectionUtils.isEmpty(recordVoList.getContent())) {
            return Result.wrapErrorResult("", "无油漆使用记录！");
        }
        return Result.wrapSuccessfulResult(recordVoList);
    }

    /**
     * 跳转到油漆记录新增页面
     *
     * @return
     */
    @RequestMapping("toPaintUseRecordAdd")
    public String toPaintUseRecordAdd(Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        UserInfo userInfo = UserUtils.getUserInfo(request);
        model.addAttribute("operatorName", userInfo.getName());
        model.addAttribute("operatorId", userInfo.getUserId());
        Long shopId = userInfo.getShopId();
        model.addAttribute("useRecordSn", snFactory.generateSn(SnFactory.PAINT_USE_RECORD_SN_INCREMENT, shopId, SnFactory.PAINT_USE_RECORD));
        return "yqx/page/magic/paint/paintUseRecordAdd";
    }

    /**
     * 新增油漆使用记录接口
     *
     * @param paintUseRecordVo
     * @return
     */
    @RequestMapping("paintUseRecordAdd")
    @ResponseBody
    public Result<Long> paintUseRecordAdd(@RequestBody PaintUseRecordVo paintUseRecordVo) {
        Long flag = 0l;
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            flag = paintUseRecordFacade.addPaintUseRecord(paintUseRecordVo, shopId);
        } catch (Exception e) {
            log.error("[新增油漆使用记录] 异常！", e);
            return Result.wrapErrorResult("", "新增油漆使用记录失败！");
        }
        if (flag > 0) {
            return Result.wrapSuccessfulResult(flag);
        }
        return Result.wrapErrorResult("", "新增油漆使用记录失败！");
    }


    /**
     * 跳转到油漆记录详情页面，使用freemarker渲染，需要前端在调用的时候判断id是否为空
     *
     * @return
     */
    @RequestMapping("toPaintUseRecord")
    public String toPaintUseRecordDetail(Long id, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        PaintUseRecordVo paintUseRecordVo = null;
        if (id != null) {
            Long shopId = UserUtils.getShopIdForSession(request);
            try {
                paintUseRecordVo = paintUseRecordFacade.getPaintUseRecord(shopId, id);
            } catch (Exception e) {
                log.error("[获取油漆使用记录详情失败]，id=" + id + ",shopId=" + shopId, e);
            }
        }
        model.addAttribute("paintUseRecord", paintUseRecordVo);
        return "yqx/page/magic/paint/paintUseRecordDetail";
    }


    @RequestMapping("printPaintUseRecord")
    public String printPaintUseRecord(Long id, Model model) {
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        PaintUseRecordVo paintUseRecordVo = null;
        String shopName = null;
        if (id != null) {
            Long shopId = UserUtils.getShopIdForSession(request);

            try {
                shopName = shopDao.selectById(shopId).getName();
            } catch (Exception e) {
                log.error("[导出油漆使用记录] 获取门店名称失败！", e);
            }
            try {
                paintUseRecordVo = paintUseRecordFacade.getPaintUseRecord(shopId, id);
            } catch (Exception e) {
                log.error("[获取油漆使用记录详情失败]，id=" + id + ",shopId=" + shopId, e);
            }
        }
        model.addAttribute("shopName", shopName);
        model.addAttribute("paintUseRecord", paintUseRecordVo);
        return "yqx/page/magic/paint/paintUseRecordPrint";
    }

    /**
     * 油漆使用记录详情导出接口
     *
     * @param useRecordId
     * @param response
     * @return
     */
    @RequestMapping("/exportPaintRecord/{useRecordId}")
    @ResponseBody
    public Object exportProxyList(@PathVariable("useRecordId") Long useRecordId, HttpServletResponse response) {
        long sTime = System.currentTimeMillis();
        ModelAndView view = new ModelAndView("yqx/page/magic/paint/paintUseDetailsExport");
        if (useRecordId != null && useRecordId != 0) {
            Long shopId = UserUtils.getShopIdForSession(request);
            com.tqmall.core.common.entity.Result<List<PaintRecordDetailDTO>> result = null;
            try {
                result = rpcPaintRecordService.getPaintUseRecordDetails(useRecordId, shopId);
            } catch (Exception e) {
                log.error("[导出油漆使用记录] 失败！shopId=" + shopId + ",useRecordId=" + useRecordId, e);
                return view;
            }
            if (result == null) {
                return view;
            }
            if (result.isSuccess()) {
                List<PaintRecordDetailDTO> paintRecordDetailDTOList = result.getData();
                List<PaintRecordDetailVo> paintRecordDetailVoList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(paintRecordDetailDTOList)) {
                    Integer seqno = 1;
                    for (PaintRecordDetailDTO paintRecordDetailDTO : paintRecordDetailDTOList) {
                        PaintRecordDetailVo paintRecordDetailVo = BdUtil.bo2do(paintRecordDetailDTO, PaintRecordDetailVo.class);
                        paintRecordDetailVo.setSalePrice(paintRecordDetailVo.getSalePrice());
                        paintRecordDetailVo.setSeqno(seqno++);
                        paintRecordDetailVoList.add(paintRecordDetailVo);
                    }
                }
                view.addObject("record", paintRecordDetailVoList);
            } else {
                log.info("[导出油漆使用记录]为空！Dubbo接口返回false，shopId={},useRecordId={},Msg={}", shopId, useRecordId, result.getMessage());
            }
        }
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "paint_use_details";
        try {
            filename = URLEncoder.encode("油漆使用记录详情", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("油漆使用记录详情导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        log.info("【油漆使用记录详情导出】,耗时{}毫秒", System.currentTimeMillis() - sTime);
        return view;
    }


}
