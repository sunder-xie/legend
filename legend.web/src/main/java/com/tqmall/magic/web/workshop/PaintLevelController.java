package com.tqmall.magic.web.workshop;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.vo.PaintLevelVO;
import com.tqmall.magic.object.param.workshop.PaintLevelParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.workshop.PaintLevelDTO;
import com.tqmall.magic.service.workshop.RpcPaintLevelService;
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
@RequestMapping("workshop/paintlevel")
public class PaintLevelController {

    @Autowired
    private RpcPaintLevelService rpcPaintLevelService;

    @RequestMapping("/paintlevel-list")
    public String toPaintLevelList(Model model) {
        model.addAttribute("leftNav","paintlevel");
        return "yqx/page/magic/workshop/paintlevel-list";
    }
    @RequestMapping("/paintlevel-edit")
    public String toPaintLevelAdd() {
        return "yqx/page/magic/workshop/paintlevel-edit";
    }
    /**
     *
     *
     * @param
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result getPaintLevelListByShopId(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request) {
        try{
            PaintLevelParam paintLevelParam = new PaintLevelParam();
            paintLevelParam.setPageNum(pageable.getPageNumber());
            paintLevelParam.setPageSize(pageable.getPageSize());
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            paintLevelParam.setShopId(shopId);
            log.info("[共享中心] DUBBO接口rpcpaintSpeciesService.getpaintSpeciesListByShopId获取油漆种类列表,参数:{}", JSONUtil.object2Json(paintLevelParam));
            Result<PageEntityDTO<PaintLevelDTO>> page = null;
            try {
                page = rpcPaintLevelService.selectPaintLevelPageList(paintLevelParam);
            }catch (Exception e){
                log.error("分页查询油漆级别列表失败",e);
                return Result.wrapErrorResult("","分页查询油漆级别列表失败");
            }
            log.info("[共享中心] DUBBO接口rpcpaintSpeciesService.getpaintSpeciesListByShopId获取油漆种类列表");
            List<PaintLevelVO> paintSpeciesVOList = Lists.newArrayList();
            if(page.isSuccess() && page != null){
                PageEntityDTO<PaintLevelDTO> paintLevelDTOList = page.getData();
                List<PaintLevelDTO> paintSpeciesDTOs = paintLevelDTOList.getRecordList();
                Integer totalNum = paintLevelDTOList.getTotalNum();
                for(PaintLevelDTO paintLevelDTO :paintSpeciesDTOs ){
                    PaintLevelVO paintLevelVO =  new PaintLevelVO();
                    BeanUtils.copyProperties(paintLevelDTO,paintLevelVO);
                    paintSpeciesVOList.add(paintLevelVO);
                }
                PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<PaintLevelVO> resultPage = new DefaultPage<>(paintSpeciesVOList, pageRequest, totalNum);
                return  Result.wrapSuccessfulResult(resultPage);
            }else {
                return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(),LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
            }
        }catch (Exception e){
            log.error("[查询油漆级别信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTLEVEL_LIST_ERROR.getCode(), LegendErrorCode.PAINTLEVEL_LIST_ERROR.getErrorMessage());

        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    public Result<PaintLevelVO> getPaintLevelList(Long id) {
        try {
            PaintLevelVO  paintLevelVO = new PaintLevelVO();
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.selectPaintLevel查询油漆级别,参数:{}", JSONUtil.object2Json(id));
            Result<PaintLevelDTO> paintLevelDTOResult = null;
            try {
                paintLevelDTOResult = rpcPaintLevelService.selectPaintLevel(id);
            }catch (Exception e){
                log.error("查询油漆级别失败",e);
                return Result.wrapErrorResult("","查询油漆级别失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.selectPaintLevel查询油漆级别,返回:{}");
            if(paintLevelDTOResult.isSuccess() && null != paintLevelDTOResult.getData()){
                BeanUtils.copyProperties(paintLevelDTOResult.getData(),paintLevelVO);
            }
            return Result.wrapSuccessfulResult(paintLevelVO);
        } catch (Exception e) {
            log.error("[查询油漆级别信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTSPECIES_LIST_ERROR.getCode(), LegendErrorCode.PAINTSPECIES_LIST_ERROR.getErrorMessage());
        }
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result editPaintLevel( @RequestBody PaintLevelVO paintLevelVO,HttpServletRequest request) {
        try {
            PaintLevelParam paintLevelParam = new PaintLevelParam();
            BeanUtils.copyProperties(paintLevelVO,paintLevelParam);
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            paintLevelParam.setShopId(shopId);
            if(null != paintLevelVO.getId()){
                log.info("[共享中心] DUBBO接口rpcPaintLevelService.insertPaintSpecies添加油漆级别");
                rpcPaintLevelService.updatePaintLevel(paintLevelParam);
                log.info("[共享中心] DUBBO接口rpcPaintLevelService.insertPaintSpecies添加油漆级别,返回:{}");
                return Result.wrapSuccessfulResult("");
            }
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.insertPaintSpecies添加油漆级别");
            rpcPaintLevelService.insertPaintLevel(paintLevelParam);
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.insertPaintSpecies添加油漆级别,返回:{}");
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[编辑油漆级别信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTLEVEL_EDIT_ERROR.getCode(), LegendErrorCode.PAINTLEVEL_EDIT_ERROR.getErrorMessage());
        }
    }

    @RequestMapping(value="/paintlevel-del" , method = RequestMethod.POST)
    @ResponseBody
    public Result deletePaintLevel( Long id) {
        try {
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.deletePaintSpecies删除油漆级别,参数:{}", id);
            rpcPaintLevelService.deletePaintLevel(id);
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.deletePaintSpecies删除油漆级别,返回:{}");
            return Result.wrapSuccessfulResult("");
        } catch (Exception e) {
            log.error("[修改油漆级别信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTLEVEL_DELETE_ERROR.getCode(), LegendErrorCode.PAINTLEVEL_DELETE_ERROR.getErrorMessage());
        }
    }
    @RequestMapping(value="/selectlist")
    @ResponseBody
    public  Result selectPaintLevel(HttpServletRequest request){
        PaintLevelParam paintLevelParam = new PaintLevelParam();
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        paintLevelParam.setShopId(shopId);
        List<PaintLevelVO>  paintLevelVOList = Lists.newArrayList();
        try {
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.selectPaintLevelList查询油漆级别,参数:{}", JSONUtil.object2Json(paintLevelParam));
            Result<List<PaintLevelDTO>> paintLevelDTOResult = null;
            try {
                paintLevelDTOResult = rpcPaintLevelService.selectPaintLevelList(paintLevelParam);
            }catch (Exception e){
                log.error("查询油漆级别列表失败",e);
                return Result.wrapErrorResult("","查询油漆级别列表失败");
            }
            log.info("[共享中心] DUBBO接口rpcPaintLevelService.selectPaintLevelList查询油漆级别");
            if(paintLevelDTOResult.isSuccess()){
                List<PaintLevelDTO> paintLevelDTOs = paintLevelDTOResult.getData();
                for(PaintLevelDTO paintLevelDTO : paintLevelDTOs){
                    PaintLevelVO paintLevelVO  = new PaintLevelVO();
                    BeanUtils.copyProperties(paintLevelDTO,paintLevelVO);
                    paintLevelVOList.add(paintLevelVO);
                }
            }
        } catch (Exception e) {
            log.error("[查询油漆级别信息] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PAINTLEVEL_LIST_ERROR.getCode(), LegendErrorCode.PAINTLEVEL_LIST_ERROR.getErrorMessage());
        }
        return  Result.wrapSuccessfulResult(paintLevelVOList);
    }
}
