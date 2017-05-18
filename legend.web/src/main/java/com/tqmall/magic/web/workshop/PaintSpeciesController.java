package com.tqmall.magic.web.workshop;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.vo.PaintSpeciesVO;
import com.tqmall.magic.object.param.workshop.PaintSpeciesParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.workshop.PaintSpeciesDTO;
import com.tqmall.magic.service.workshop.RpcPaintSpeciesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yanxinyin on 16/7/8.
 */
@Controller
@Slf4j
@RequestMapping("workshop/paintspecies")
public class PaintSpeciesController {

    @Autowired
    private RpcPaintSpeciesService rpcPaintSpeciesService;

    @RequestMapping("/paintspecies-list")
    public String topaintSpeciesList(Model model)
    {  model.addAttribute("leftNav","paintspecies");
        return "yqx/page/magic/workshop/paintspecies-list";
    }
    @RequestMapping("/paintspecies-edit")
    public String topaintSpeciesAdd() {
        return "yqx/page/magic/workshop/paintspecies-edit";
    }
    /**
     * 查询一家门店的油漆种类
     *
     * @param
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result getpaintSpeciesListByShopId(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request) {
        try{
            PaintSpeciesParam paintSpeciesParam = new PaintSpeciesParam();
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            paintSpeciesParam.setShopId(shopId);
            paintSpeciesParam.setPageNum(pageable.getPageNumber());
            paintSpeciesParam.setPageSize(pageable.getPageSize());
            log.info("[共享中心] DUBBO接口rpcpaintSpeciesService.getpaintSpeciesListByShopId获取油漆种类列表");
            Result<PageEntityDTO<PaintSpeciesDTO>> page = null;
            try {
                page = rpcPaintSpeciesService.selectPaintSpeciesPageList(paintSpeciesParam);
            }catch (Exception e){
                log.error("分页获取油漆种类列表失败",e);
                return Result.wrapErrorResult("","分页获取油漆种类列表失败");
            }
            log.info("[共享中心] DUBBO接口rpcpaintSpeciesService.getpaintSpeciesListByShopId获取油漆种类列表");
            List<PaintSpeciesVO> paintSpeciesVOList = Lists.newArrayList();
            if(page.isSuccess() && page != null){
                PageEntityDTO<PaintSpeciesDTO> paintSpeciesDTOList = page.getData();
                List<PaintSpeciesDTO> paintSpeciesDTOs = paintSpeciesDTOList.getRecordList();
                Integer totalNum = paintSpeciesDTOList.getTotalNum();
                for(PaintSpeciesDTO paintSpeciesDTO :paintSpeciesDTOs ){
                    PaintSpeciesVO paintSpeciesVO =  new PaintSpeciesVO();
                    BeanUtils.copyProperties(paintSpeciesDTO,paintSpeciesVO);
                    paintSpeciesVOList.add(paintSpeciesVO);
                }
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<PaintSpeciesVO> resultPage = new DefaultPage<>(paintSpeciesVOList, pageRequest, totalNum);
                return  Result.wrapSuccessfulResult(resultPage);
            }else {
                return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(),LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
            }
        }catch (Exception e){
            log.error("[查询油漆种类信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
        }
    }


    @RequestMapping("/detail")
    @ResponseBody
    public Result<PaintSpeciesVO> getPaintSpeciesList(Long id) {
        try {
            PaintSpeciesVO  paintSpeciesVO = new PaintSpeciesVO();
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.selectPaintSpecies查询油漆种类");
            Result<PaintSpeciesDTO> paintSpeciesDTOResult = null;
            try {
                paintSpeciesDTOResult = rpcPaintSpeciesService.selectPaintSpecies(id);
            }catch (Exception e){
                log.error("查询油漆种类失败",e);
                return Result.wrapErrorResult("","查询油漆种类失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.selectPaintSpecies查询油漆种类,返回:{}");
            if(paintSpeciesDTOResult.isSuccess() && null!= paintSpeciesDTOResult.getData()){
                BeanUtils.copyProperties(paintSpeciesDTOResult.getData(),paintSpeciesVO);
            }
            return Result.wrapSuccessfulResult(paintSpeciesVO);
        } catch (Exception e) {
            log.error("[显示油漆种类详情] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result editpaintSpecies(@RequestBody PaintSpeciesVO paintSpeciesVO, HttpServletRequest request) {
        try {
            PaintSpeciesParam paintSpeciesParam = new PaintSpeciesParam();
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            BeanUtils.copyProperties(paintSpeciesVO,paintSpeciesParam);
            paintSpeciesParam.setShopId(shopId);
            if(null != paintSpeciesVO.getId()){
                log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.updatePaintSpecies更新油漆种类列表,参数:{}", JSONUtil.object2Json(paintSpeciesParam));
                try {
                    rpcPaintSpeciesService.updatePaintSpecies(paintSpeciesParam);
                }catch (Exception e){
                    log.error("更新油漆种类列表失败",e);
                    return Result.wrapErrorResult("","更新油漆种类列表失败");
                }
                log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.updatePaintSpecies更新油漆种类列表");
                return Result.wrapSuccessfulResult("");
            }
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.insertPaintSpecies添加油漆种类,参数:{}", JSONUtil.object2Json(paintSpeciesParam));
            try {
                rpcPaintSpeciesService.insertPaintSpecies(paintSpeciesParam);
            }catch (Exception e){
                log.error("添加油漆种类失败",e);
                return Result.wrapErrorResult("","添加油漆种类失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.insertPaintSpecies添加油漆种类,返回:{}");
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[编辑油漆种类信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_EDIT_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_EDIT_ERROR.getErrorMessage());
        }
    }

    @RequestMapping(value = "/paintspecies-del", method = RequestMethod.POST)
    @ResponseBody
    public Result deletepaintSpecies(Long id) {
        try {
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.deletePaintSpecies删除油漆种类,参数:{}", id);
            try {
                rpcPaintSpeciesService.deletePaintSpecies(id);
            }catch (Exception e){
                log.error("删除油漆种类失败",e);
                return Result.wrapErrorResult("","删除油漆种类失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.deletePaintSpecies删除油漆种类,返回:{}");
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[删除油漆种类信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_DELETE_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_DELETE_ERROR.getErrorMessage());
        }
    }

    @RequestMapping(value = "/selectlist", method = RequestMethod.GET)
    @ResponseBody
    public Result selectListPaintSpecies(HttpServletRequest request) {
        try {
            PaintSpeciesParam paintSpeciesParam = new PaintSpeciesParam();
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            paintSpeciesParam.setShopId(shopId);
            List<PaintSpeciesVO> paintSpeciesVOList = Lists.newArrayList();
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.selectPaintSpeciesList查询油漆种类,参数:{}", paintSpeciesParam);
            Result<List<PaintSpeciesDTO>> paintSpeciesDTOList = null;
            try {
                paintSpeciesDTOList = rpcPaintSpeciesService.selectPaintSpeciesList(paintSpeciesParam);
            }catch (Exception e){
                log.error("查询油漆种类失败",e);
                return Result.wrapErrorResult("","查询油漆种类失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintSpeciesService.selectPaintSpeciesList查询油漆种类,返回:{}");
            if(paintSpeciesDTOList.isSuccess()){
                List<PaintSpeciesDTO> paintLevelDTOs = paintSpeciesDTOList.getData();
                for(PaintSpeciesDTO PaintSpeciesDTO : paintLevelDTOs){
                    PaintSpeciesVO paintSpeciesVO  = new PaintSpeciesVO();
                    BeanUtils.copyProperties(PaintSpeciesDTO,paintSpeciesVO);
                    paintSpeciesVOList.add(paintSpeciesVO);
                }
            }
            return Result.wrapSuccessfulResult(paintSpeciesVOList);
        } catch (Exception e) {
            log.error("[删除油漆种类信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
        }
    }
}
