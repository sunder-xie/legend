package com.tqmall.magic.web.workshop;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.enums.ProductionLineTypeEnum;
import com.tqmall.legend.facade.magic.vo.ProcessVO;
import com.tqmall.legend.facade.magic.vo.ProductionLineProcessVO;
import com.tqmall.legend.facade.magic.vo.ProductionLineTypeVO;
import com.tqmall.legend.facade.magic.vo.ProductionLineVO;
import com.tqmall.magic.object.param.workshop.ProductionLineParam;
import com.tqmall.magic.object.result.workshop.PLineProcessRelDTO;
import com.tqmall.magic.object.result.workshop.ProductionLineDTO;
import com.tqmall.magic.service.workshop.RpcPLineProcessRelService;
import com.tqmall.magic.service.workshop.RpcProductionLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务场景： 生产线管理
 * Created by yanxinyin on 16/7/8.
 */
@Controller
@Slf4j
@RequestMapping("workshop/productionline")
public class ProductionLineController {

    @Autowired
    private RpcProductionLineService rpcProductionLineService;
    @Autowired
    private RpcPLineProcessRelService rpcPLineProcessRelService;


    @RequestMapping("/productionline-list")
    public String toProductionLineistPage(Model model) {
        model.addAttribute("leftNav", "productionline");
        return "yqx/page/magic/workshop/productionline-list";
    }

    @RequestMapping("/productionline-edit")
    public String toProductionLineEditPage(Long lineId, Model model, HttpServletRequest request) {
        try {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            Result<ProductionLineDTO> productionLineDTOResult = rpcProductionLineService.getProductionLineInfo(shopId, lineId);
            log.info("[生产线管理] 调用Magic DUBBO接口获取生产线数据，shopId={},lineId={}，result={}", shopId, lineId, productionLineDTOResult.isSuccess());
            ProductionLineDTO productionLineDTO = productionLineDTOResult.getData();
            List<PLineProcessRelDTO> pLineProcessRelDTOList = productionLineDTO.getProductLineProcessRelList();
            ProductionLineProcessVO productionLineProcessVO = BdUtil.bo2do(productionLineDTO, ProductionLineProcessVO.class);
            productionLineProcessVO.setLineName(productionLineDTO.getName());
            productionLineProcessVO.setType(productionLineDTO.getType().toString());
            productionLineProcessVO.setLineId(productionLineDTO.getId());
            if (!CollectionUtils.isEmpty(pLineProcessRelDTOList)) {
                List<ProcessVO> processVOList = BdUtil.bo2do4List(pLineProcessRelDTOList, ProcessVO.class);
                productionLineProcessVO.setProcessVOList(processVOList);
            }

            model.addAttribute("productionLineProcessVO", productionLineProcessVO);
        } catch (Exception e) {
            log.error("[显示生产线工序详情] 异常！e={}", e);
        }
        return "yqx/page/magic/workshop/productionline-edit";
    }

    @RequestMapping("/productionline-add")
    public String toAddProductionLinePage() {
        return "yqx/page/magic/workshop/productionline-add";
    }


    /**
     * 生产线模板新增接口
     *
     * @param productionLineProcessVO
     * @param request
     * @return
     */
    @RequestMapping(value = "add", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public Result addProductionLineProcess(@RequestBody ProductionLineProcessVO productionLineProcessVO, HttpServletRequest request) {
        if (productionLineProcessVO.getLineName() == null || productionLineProcessVO.getLineName().equals("") || productionLineProcessVO.getType().equals("") || productionLineProcessVO.getType() == null) {
            return Result.wrapErrorResult("", "新增生产线，名称和类型不能为空！");
        }
        if (productionLineProcessVO.getLineName().length() > 50) {
            return Result.wrapErrorResult("", "生产线名称过长！");
        }
        if (productionLineProcessVO.getRemark().length() > 200) {
            return Result.wrapErrorResult("", "生产线备注信息过长！");
        }
        try {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            productionLineProcessVO.setShopId(shopId);
            ProductionLineDTO productionLineDTO = BdUtil.bo2do(productionLineProcessVO, ProductionLineDTO.class);
            productionLineDTO.setName(productionLineProcessVO.getLineName());
            productionLineDTO.setType(Integer.parseInt(productionLineProcessVO.getType()));
            List<ProcessVO> processVOList = productionLineProcessVO.getProcessVOList();
            if (!CollectionUtils.isEmpty(processVOList)) {
                List<PLineProcessRelDTO> productionLineProcessRelDTO = BdUtil.bo2do4List(processVOList, PLineProcessRelDTO.class);
                productionLineDTO.setProductLineProcessRelList(productionLineProcessRelDTO);
            }
            Result result = rpcProductionLineService.addProductionLine(productionLineDTO);
            log.info("[生产线管理] 调用DUBBO接口添加生产线，result={}", result.isSuccess());
            return result;
        } catch (Exception e) {
            log.error("[生产线管理] 添加生产线 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PRODUCTIONLINE_ADD_ERROR.getCode(), LegendErrorCode.PRODUCTIONLINE_ADD_ERROR.getErrorMessage());
        }
    }

    /**
     * 编辑时更新数据接口
     *
     * @param productionLineProcessVO
     * @param request
     * @return
     */
    @RequestMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public Result editProductionLineProcess(@RequestBody ProductionLineProcessVO productionLineProcessVO, HttpServletRequest request) {
        if (productionLineProcessVO.getLineName() == null || productionLineProcessVO.getLineName().equals("") || productionLineProcessVO.getProcessVOList() == null) {
            return Result.wrapErrorResult("", "生产线名称和类型不能为空！");
        }
        if (productionLineProcessVO.getLineName().length() > 50) {
            return Result.wrapErrorResult("", "生产线名称不能超过50个字符！");
        }
        if (productionLineProcessVO.getRemark().length() > 200) {
            return Result.wrapErrorResult("", "生产线备注不能超过200个字符！");
        }
        try {
            UserInfo userInfo = UserUtils.getUserInfo(request);
            Long shopId = userInfo.getShopId();
            productionLineProcessVO.setShopId(shopId);
            ProductionLineDTO productionLineDTO = BdUtil.bo2do(productionLineProcessVO, ProductionLineDTO.class);
            productionLineDTO.setName(productionLineProcessVO.getLineName());
            List<ProcessVO> processVOList = productionLineProcessVO.getProcessVOList();
            if (!CollectionUtils.isEmpty(processVOList)) {
                List<PLineProcessRelDTO> productionLineProcessRelDTO = BdUtil.bo2do4List(processVOList, PLineProcessRelDTO.class);
                productionLineDTO.setProductLineProcessRelList(productionLineProcessRelDTO);
            }
            Boolean isReScheduling = productionLineProcessVO.getIsReScheduling();
            Result result = rpcProductionLineService.modifyProductionLine(productionLineDTO, isReScheduling);
            log.info("[生产线管理] 调用Magic DUBBO接口修改生产线信息，shopId={},lineId={}，返回结果={}", shopId, productionLineDTO.getId(), result.isSuccess());
            return result;
        } catch (Exception e) {
            log.error("[添加生产线管理信息] 异常！e={}", e);
            return Result.wrapErrorResult("", "编辑生产线操作失败！");
        }
    }


    /**
     * 获取生产线模板列表接口
     *
     * @param type
     * @param request
     * @return
     */
    @RequestMapping("/processList")
    @ResponseBody
    public Result<List<ProcessVO>> productionLineProcessList(Long type, HttpServletRequest request) {
        try {
            Long shopId = 0L;
            Result<ProductionLineDTO> productionLineDTO = null;
            try {
                productionLineDTO = rpcProductionLineService.selectProductionLineByType(shopId, type);
                log.info("[生产线管理] 获取生产线模板，shopId={},type={},返回结果={}", shopId, type, productionLineDTO.isSuccess());
            } catch (Exception e) {
                log.error("[生产线管理] 查询生产线失败,e={}", e);
                return Result.wrapErrorResult("", "查询生产线失败");
            }
            long lineId = 0;
            if (productionLineDTO.isSuccess() && null != productionLineDTO) {
                lineId = productionLineDTO.getData().getId();
            }else{
                return Result.wrapErrorResult("","查询生产线失败！");
            }
            Result<List<PLineProcessRelDTO>> lneProcessRelList = null;
            try {
                lneProcessRelList = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
                log.info("[生产线管理] 获取生产线关联的工序列表数据，shopId={},lineId={},result={}", shopId, lineId, lneProcessRelList.isSuccess());
            } catch (Exception e) {
                log.error("[生产线管理] 查询生产线失败,e={}", e);
                return Result.wrapErrorResult("", "查询生产线失败");
            }
            List<ProcessVO> processVOList = new ArrayList<ProcessVO>();
            if (lneProcessRelList.isSuccess() && null != lneProcessRelList.getData()) {
                List<PLineProcessRelDTO> lineProcessRels = lneProcessRelList.getData();
                for (PLineProcessRelDTO pLineProcessRelDTO : lineProcessRels) {
                    ProcessVO processVO = new ProcessVO();
                    processVO.setName(pLineProcessRelDTO.getProcessName());
                    BeanUtils.copyProperties(pLineProcessRelDTO, processVO);
                    processVOList.add(processVO);
                }
            }
            return Result.wrapSuccessfulResult(processVOList);
        } catch (Exception e) {
            log.error("[显示生产线工序详情] 异常！e={}", e);
            return Result.wrapErrorResult(LegendErrorCode.PRODUCTIONLINE_ADD_ERROR.getCode(), LegendErrorCode.PRODUCTIONLINE_ADD_ERROR.getErrorMessage());
        }
    }

//    @RequestMapping("/list")
//    @ResponseBody
//    public Result getProductionLineListByShopId(HttpServletRequest request) {
//        try {
//            ProductionLineParam productionLineParam = new ProductionLineParam();
//            UserInfo userInfo = UserUtils.getUserInfo(request);
//            Long shopId = userInfo.getShopId();
//            productionLineParam.setShopId(shopId);
//            Result<List<ProductionLineDTO>> page = null;
//            try {
//                page = rpcProductionLineService.selectProductionLineList(productionLineParam);
//            } catch (Exception e) {
//                log.error("查询生产线列表失败", e);
//                return Result.wrapErrorResult("", "查询生产线列表失败");
//            }
//            List<ProductionLineVO> productionLineVOList = Lists.newArrayList();
//            if (page.isSuccess() && page != null) {
//                for (ProductionLineDTO productionLineDTO : page.getData()) {
//                    ProductionLineVO productionLineVO = new ProductionLineVO();
//                    BeanUtils.copyProperties(productionLineDTO, productionLineVO);
//                    productionLineVOList.add(productionLineVO);
//                }
//            }
//            return Result.wrapSuccessfulResult(productionLineVOList);
//        } catch (Exception e) {
//            log.error("[查询生产线信息] 异常！e={}", e);
//            return Result.wrapErrorResult(LegendErrorCode.PRODUCTIONLINE_LIST_ERROR.getCode(), LegendErrorCode.PRODUCTIONLINE_LIST_ERROR.getErrorMessage());
//
//        }
//    }


    /**
     * 业务场景：生产线管理中生产线列表初始化
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result<List<ProductionLineVO>> getProductionLineListByShopId(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        Result<List<ProductionLineDTO>> result = null;
        try {
            result = rpcProductionLineService.selectLineByShopId(shopId);
            log.info("[生产线管理] 调用dubbo接口初始化生产线列表，shopId={},result={}", shopId, result.isSuccess());
        } catch (Exception e) {
            log.error("查询生产线列表失败,e={}", e);
            return Result.wrapErrorResult("", "查询生产线列表失败");
        }
        List<ProductionLineVO> productionLineVOList = null;
        if (result != null) {
            List<ProductionLineDTO> productionLineDTOList = result.getData();
            if (!CollectionUtils.isEmpty(productionLineDTOList)) {
                productionLineVOList = new ArrayList<>();
                for (ProductionLineDTO productionLineDTO : productionLineDTOList) {
                    ProductionLineVO productionLineVO = BdUtil.bo2do(productionLineDTO, ProductionLineVO.class);
                    String typeName = ProductionLineTypeEnum.getNameByType(productionLineDTO.getType());
                    productionLineVO.setTypeName(typeName);
                    productionLineVOList.add(productionLineVO);
                }
            }
        }
        return Result.wrapSuccessfulResult(productionLineVOList);
    }

    /**
     * 删除生产线接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteProductionLineProcess(Long id) {
        //删除生产线数据
        Result<Integer> result = null;
        try {
            result = rpcProductionLineService.deleteProductionLine(id);
            log.info("[生产线管理] 调用Magic DUBBO接口删除生产线，lineId={},result={}", id, result.isSuccess());
        } catch (Exception e) {
            log.error("[生产线管理] 删除生产线失败，e={}", e);
            return Result.wrapErrorResult("", "删除生产线失败");
        }
        return result;
    }

    @RequestMapping(value = "/typelist")
    @ResponseBody
    public Result selectPaintLevel() {
        List<ProductionLineTypeVO> productionLineTypeVOList = new ArrayList<>();
        for (ProductionLineTypeEnum e : ProductionLineTypeEnum.values()) {
            ProductionLineTypeVO productionLineTypeVO = new ProductionLineTypeVO();
            productionLineTypeVO.setType(e.getType());
            productionLineTypeVO.setName(e.getName());
            productionLineTypeVOList.add(productionLineTypeVO);
        }
        return Result.wrapSuccessfulResult(productionLineTypeVOList);
    }
}
