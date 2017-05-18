package com.tqmall.magic.web.paint;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.maxsn.MaxSnService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.magic.object.param.paint.PaintInventoryParam;
import com.tqmall.magic.object.result.goods.GoodsPaintExtDTO;
import com.tqmall.magic.object.result.paint.PaintInventoryRecordDTO;
import com.tqmall.magic.object.result.paint.PaintInventoryStockDTO;
import com.tqmall.magic.service.goods.RpcGoodsPaintExtService;
import com.tqmall.magic.service.paint.RpcPaintInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 16/11/8.
 */
@Controller
@RequestMapping("paint/inventory")
@Slf4j
public class PaintInventoryController extends BaseController {

    @Autowired
    private RpcPaintInventoryService rpcPaintInventoryService;

    @Autowired
    private RpcGoodsPaintExtService rpcGoodsPaintExtService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MaxSnService maxSnService;

    @Autowired
    private ShopService shopService;


    /**
     * 到转到库存盘点单列表页
     * @return
     */
    @RequestMapping("toInventoryPaintList")
    public String toInventoryPaintList(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/magic/paint/inventoryPaintList";
    }

    /**
     * 跳转到添加库存盘点页面
     * @return
     */
    @RequestMapping("/toInventoryPaint")
    public String toInventoryPaint(Model model,HttpServletRequest request){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        UserInfo currentLoginUser = UserUtils.getUserInfo(request);
        Long shopId = currentLoginUser.getShopId();
        model.addAttribute("userInfo", currentLoginUser);
        // 盘点日期
        Date inventoryDate = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        model.addAttribute("inventoryDate", fmt.format(inventoryDate));
        // 预生成盘点编号
        model.addAttribute("inventorySN", maxSnService.getMaxSn(shopId, "PD"));

        return "yqx/page/magic/paint/inventoryPaint";
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/toUpdateInventoryPaint")
    public String toUpdateInventoryPaint(Long id,Model model,HttpServletRequest request){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        Result<PaintInventoryRecordDTO> recordResult = getPaintInventoryRecordInfo(id);
        if (recordResult.isSuccess()){
            model.addAttribute("paintInventoryRecordDTO",recordResult.getData());
        }
        return "yqx/page/magic/paint/inventoryPaint";
    }

    /**
     * 跳转到库存盘点单详情页面
     * @return
     */
    @RequestMapping("/toInventoryPaintDtl")
    public String toInventoryPaintDtl(Long id,Model model,HttpServletRequest request){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        Result<PaintInventoryRecordDTO> recordResult = getPaintInventoryRecordInfo(id);
        if (recordResult.isSuccess()){
            model.addAttribute("paintInventoryRecordDTO",recordResult.getData());
        }
        return "yqx/page/magic/paint/inventoryPaintDtl";
    }

    /**
     * 跳转到库存盘点单打印页面
     * @return
     */
    @RequestMapping("/toInventoryPaintPrint")
    public String toInventoryPaintPrint(Long id,Model model,HttpServletRequest request){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        Long shopId = UserUtils.getShopIdForSession(request);
        Shop shop = shopService.selectById(shopId);
        model.addAttribute("shop",shop);
        Result<PaintInventoryRecordDTO> recordResult = getPaintInventoryRecordInfo(id);
        if (recordResult.isSuccess()){
            model.addAttribute("paintInventoryRecordDTO",recordResult.getData());
        }
        return "yqx/page/magic/paint/inventoryPaintPrint";
    }



    /**
     * 查询油漆盘点单列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result getPintInventoryByPage(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,HttpServletRequest request) {
        //获取参数
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        //获取店铺信息
        Long shopId = UserUtils.getShopIdForSession(request);
        if (shopId == null || shopId < 1) {
            log.error("获取店铺信息错误.shopId={}", shopId);
            return Result.wrapErrorResult("", "获取店铺信息错误");
        }

        //参数检查
        PaintInventoryParam paintInventoryParam = new PaintInventoryParam();
        paintInventoryParam.setShopId(shopId);
        if (searchParams.containsKey("recordSn")) {
            paintInventoryParam.setRecordSn((String) searchParams.get("recordSn"));
        }
        if (searchParams.containsKey("inventoryStartTime")) {
            paintInventoryParam.setInventoryStartTime((String) searchParams.get("inventoryStartTime"));
        }
        if (searchParams.containsKey("inventoryEndTime")) {
            paintInventoryParam.setInventoryEndTime((String) searchParams.get("inventoryEndTime"));
        }
        if (searchParams.containsKey("status")) {
            paintInventoryParam.setStatus(Integer.parseInt(searchParams.get("status").toString()));
        }
        paintInventoryParam.setPageNum(pageable.getPageNumber());
        paintInventoryParam.setPageSize(pageable.getPageSize());

        //调用远程查询油漆盘点信息
        com.tqmall.core.common.entity.Result<List<PaintInventoryRecordDTO>> result = null;
        try {
            result = rpcPaintInventoryService.getPintInventoryInfoByPage(paintInventoryParam);
        } catch (Exception e) {
            log.error("调用远程查询油漆盘点信息失败", e);
            return Result.wrapErrorResult("", "调用远程查询油漆盘点信息失败");
        }

        com.tqmall.core.common.entity.Result<Integer> totalNum = null;
        try {
            totalNum = rpcPaintInventoryService.getTotalPaintNum(paintInventoryParam);
        } catch (Exception e) {
            log.error("调用远程查询油漆盘点信息总数失败", e);
            return Result.wrapErrorResult("", "调用远程查询油漆盘点信息总数失败");
        }


        int pageNumber = pageable.getPageNumber();
        pageNumber = pageNumber < 1 ? 1 : pageNumber;
        int pageSize = pageable.getPageSize();
        pageSize = pageSize < 1 ? 1 : pageSize;
        PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
        DefaultPage<PaintInventoryRecordDTO> page = new DefaultPage(result.getData(), pageRequest,Long.parseLong(totalNum.getData().toString()));

        if (!result.isSuccess()) {
            return Result.wrapErrorResult(result.getCode(), result.getMessage());
        }
        return Result.wrapSuccessfulResult(page);
    }


    /**
     * 查询油漆盘点单详情
     * @param id
     * @return
     */
    @RequestMapping("info")
    @ResponseBody
    public Result<PaintInventoryRecordDTO> getPaintInventoryRecordInfo(Long id){
        //获取店铺信息
        Long shopId = UserUtils.getShopIdForSession(request);
        if (shopId == null || shopId < 1) {
            log.error("获取店铺信息错误.shopId={}", shopId);
            return Result.wrapErrorResult("", "获取店铺信息错误");
        }

        //调用远程查询油漆盘点单详情
        com.tqmall.core.common.entity.Result<PaintInventoryRecordDTO> result = null;
        try {
            result = rpcPaintInventoryService.getPintInventoryDetail(shopId,id);
        } catch (Exception e) {
            log.error("调用远程查询油漆盘点单详情失败",e);
            return Result.wrapErrorResult("","调用远程查询油漆盘点单详情失败");
        }

        if (!result.isSuccess()){
            return Result.wrapErrorResult(result.getCode(),result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    /**
     * 添加/更新 油漆盘点单信息
     * @param paintInventoryRecordDTO
     * @return
     */
    @RequestMapping(value = "/addOrUpdatePaintInventory",method = RequestMethod.POST)
    @ResponseBody
    public Result addOrUpdatePaintInventory(@RequestBody PaintInventoryRecordDTO paintInventoryRecordDTO,HttpServletRequest request){
        if (paintInventoryRecordDTO == null){
            log.error("参数为空。paintInventoryRecordDTO = {}",paintInventoryRecordDTO);
            return Result.wrapErrorResult("","参数为空");
        }

        Long shopId = UserUtils.getShopIdForSession(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        paintInventoryRecordDTO.setShopId(shopId);

        List<PaintInventoryStockDTO> paintInventoryStockDTOs = paintInventoryRecordDTO.getPaintInventoryStockDTOList();
        if (!CollectionUtils.isEmpty(paintInventoryStockDTOs)){
            for (PaintInventoryStockDTO paintInventoryStockDTO : paintInventoryStockDTOs){
                if (paintInventoryStockDTO.getRealStock() != null) {
                    //更新goods表
                    Goods goods = new Goods();
                    goods.setId(paintInventoryStockDTO.getGoodsId());
                    goods.setStock(paintInventoryStockDTO.getRealStock());
                    goodsService.updateById(goods);

                    //更新goods扩展表
                    paintInventoryStockDTO.setShopId(shopId);
                    Result result = updatePaintExt(paintInventoryStockDTO);
                }
            }
        }
        com.tqmall.core.common.entity.Result result = null;
        if (paintInventoryRecordDTO.getId() != null && paintInventoryRecordDTO.getId() > 1){
            //更新
            paintInventoryRecordDTO.setModifier(userInfo.getUserId());
            try {
                result = rpcPaintInventoryService.updateInventoryRecordInfo(paintInventoryRecordDTO);
            } catch (Exception e) {
                log.error("调用远程添加油漆盘点单失败",e);
                return Result.wrapErrorResult("","调用远程添加油漆盘点单失败");
            }
        }else {
            //添加
            paintInventoryRecordDTO.setCreator(userInfo.getUserId());
            if (paintInventoryRecordDTO.getInventoryTimeStr() != null){
                String inventoryTimeStr = paintInventoryRecordDTO.getInventoryTimeStr();
                try {
                    paintInventoryRecordDTO.setInventoryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(inventoryTimeStr));
                } catch (ParseException e) {
                    log.error("日期格式转换错误",e);
                }
            }

            try {
                result = rpcPaintInventoryService.saveInventoryRecordInfo(paintInventoryRecordDTO);
            } catch (Exception e) {
                log.error("调用远程添加油漆盘点单失败", e);
                return Result.wrapErrorResult("", "调用远程添加油漆盘点单失败");
            }
        }
        if (!result.isSuccess()){
            return Result.wrapErrorResult("",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    /**
     * 调用远程删除油漆盘点单
     * @param id
     * @return
     */
    @RequestMapping("/deletePaintInventory")
    @ResponseBody
    public Result deletePaintInventory(Long id){
        com.tqmall.core.common.entity.Result result = null;
        try {
            result = rpcPaintInventoryService.deletePintInventoryInfo(id);
        } catch (Exception e) {
            log.error("调用远程删除油漆盘点单失败",e);
            return Result.wrapErrorResult("","调用远程删除油漆盘点单失败");
        }
        if (!result.isSuccess()){
            return Result.wrapErrorResult("",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());
    }


    /**
     * 计算油漆的消耗量
     *
     * 净含量=Q1，带桶质量=Q2,带桶和搅拌头质量=Q3
     *
     * 现库存：整桶数量=n1，非整桶总质量=M1，非整桶数量=x1，搅拌头数量=y1
     * 实盘库存：整桶数量=n2，非整桶总质量=M2，非整桶数量=x2，搅拌头数量=y2
     *
     *  x = Max(x1-y1,x2-y2),y=Max(y1,y2)
     *
     * 期初库存Q初：Q初=Q1*[n1-(x-x1 )-(y-y1 )]+M1+(x-x1 )*Q2+(y-y1 )*Q3
     * 期末库存Q末：Q末=Q1*[n2-(x-x2 )-(y-y2 )]+M2+(x-x2 )*Q2+(y-y2 )*Q3
     *
     * 油漆消耗量：Q = Q初-Q末；
     *
     * @param paintInventoryStockDTO
     * @return
     */
    @RequestMapping(value = "/calculatePaintConsumption",method = RequestMethod.POST)
    @ResponseBody
    public  Result<PaintInventoryStockDTO> calculatePaintConsumption(@RequestBody PaintInventoryStockDTO paintInventoryStockDTO){
        //参数校验
        if (paintInventoryStockDTO == null){
            log.error("参数为空。PaintInventoryStockDTO={}",paintInventoryStockDTO);
            return Result.wrapErrorResult("","参数为空");
        }
        if (paintInventoryStockDTO.getNetWeight() == null || paintInventoryStockDTO.getBucketWeight() == null || paintInventoryStockDTO.getStirWeight() == null
                || paintInventoryStockDTO.getCurrentStock() == null || paintInventoryStockDTO.getCurrentNoBucketNum() == null || paintInventoryStockDTO.getCurrentNoBucketWeight() == null || paintInventoryStockDTO.getCurrentStirNum() == null
                || paintInventoryStockDTO.getRealStock() == null || paintInventoryStockDTO.getRealNoBucketNum() == null || paintInventoryStockDTO.getRealNoBucketWeight() == null || paintInventoryStockDTO.getRealStirNum() == null
                || paintInventoryStockDTO.getInventoryPrice() == null){
            log.error("参数为空");
            return Result.wrapErrorResult("","参数为空");
        }

        //现存搅拌头数量 y1
        BigDecimal currentStirNum = paintInventoryStockDTO.getCurrentStirNum();
        //实盘搅拌头数量 y2
        BigDecimal realStirNum = paintInventoryStockDTO.getRealStirNum();
        //现存非整桶数量-现存搅拌头数量 x1-y1
        BigDecimal currentDiffNum = paintInventoryStockDTO.getCurrentNoBucketNum().subtract(currentStirNum);
        //实盘非整桶数量-实盘搅拌头数量 x2-y2
        BigDecimal realDiffNum = paintInventoryStockDTO.getRealNoBucketNum().subtract(realStirNum);
        //Max(x1-y1,x2-y2) = x
        BigDecimal maxCurrentDiffNum = currentDiffNum.compareTo(realDiffNum) >= 0 ? currentDiffNum : realDiffNum;
        //Max(y1,y2) = y
        BigDecimal maxStirNum = currentStirNum.compareTo(realStirNum) >= 0 ? currentStirNum : realStirNum;

        //净含量
        BigDecimal netWeight = paintInventoryStockDTO.getNetWeight();

        //x-x1--->x-(x1-y1)
        BigDecimal currentDiffx = maxCurrentDiffNum.subtract(currentDiffNum);

        //y-y1
        BigDecimal currentDiffy = maxStirNum.subtract(currentStirNum);

        //x-x2---->x-(x2-y2)
        BigDecimal realDiffx = maxCurrentDiffNum.subtract(realDiffNum);

        //y-y2
        BigDecimal realDiffy = maxStirNum.subtract(realStirNum);

        //期初库存
        BigDecimal beginStock = (((netWeight.multiply((paintInventoryStockDTO.getCurrentStock().subtract(currentDiffx)).subtract(currentDiffy)))
                .add(paintInventoryStockDTO.getCurrentNoBucketWeight())).add(currentDiffx.multiply(paintInventoryStockDTO.getBucketWeight())))
                .add(currentDiffy.multiply(paintInventoryStockDTO.getStirWeight()));

        //期末库存
        BigDecimal endStock = (((netWeight.multiply((paintInventoryStockDTO.getRealStock().subtract(realDiffx)).subtract(realDiffy)))
                .add(paintInventoryStockDTO.getRealNoBucketWeight())).add(realDiffx.multiply(paintInventoryStockDTO.getBucketWeight())))
                .add(realDiffy.multiply(paintInventoryStockDTO.getStirWeight()));

        //油漆消耗量
        BigDecimal diffStock = beginStock.subtract(endStock);

        //油漆消耗成本价
        BigDecimal diffStockPrice = (diffStock.divide(netWeight,8, BigDecimal.ROUND_HALF_UP)).multiply(paintInventoryStockDTO.getInventoryPrice());

        //库存总成本
        BigDecimal totalStockAmount = ((beginStock.divide(netWeight,8, BigDecimal.ROUND_HALF_UP)).multiply(paintInventoryStockDTO.getInventoryPrice())).subtract(diffStockPrice);

        paintInventoryStockDTO.setDiffStock(diffStock);
        paintInventoryStockDTO.setDiffStockPrice(diffStockPrice.setScale(2,BigDecimal.ROUND_HALF_EVEN));
        paintInventoryStockDTO.setTotalStockAmount(totalStockAmount.setScale(2,BigDecimal.ROUND_HALF_EVEN));

        return Result.wrapSuccessfulResult(paintInventoryStockDTO);
    }

    /**
     * 更新油漆扩展表
     * @param paintInventoryStockDTO
     * @return
     */
    private Result updatePaintExt(PaintInventoryStockDTO paintInventoryStockDTO){
        GoodsPaintExtDTO goodsPaintExtDTO = new GoodsPaintExtDTO();
        goodsPaintExtDTO.setNoBucketWeight(paintInventoryStockDTO.getRealNoBucketWeight());
        goodsPaintExtDTO.setNoBucketNum(paintInventoryStockDTO.getRealNoBucketNum());
        goodsPaintExtDTO.setStirNum(paintInventoryStockDTO.getRealStirNum());
        goodsPaintExtDTO.setTotalStockAmount(paintInventoryStockDTO.getTotalStockAmount());
        goodsPaintExtDTO.setGoodsId(paintInventoryStockDTO.getGoodsId());
        goodsPaintExtDTO.setShopId(paintInventoryStockDTO.getShopId());
        com.tqmall.core.common.entity.Result<Boolean> result = null;
        try {
            result = rpcGoodsPaintExtService.updateByGoodsId(goodsPaintExtDTO);
        } catch (Exception e) {
            log.error("更新油漆扩展表失败",e);
            return Result.wrapErrorResult("","更新油漆扩展表失败");
        }
        if (!result.isSuccess()){
            return Result.wrapErrorResult("",result.getMessage());
        }
        return Result.wrapSuccessfulResult(result.getData());

    }

    /**
     * 导出油漆盘点单
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/inventoryPaintDtlExport")
    @ResponseBody
    public Object inventoryPaintDtlExport(Long id,HttpServletRequest request,HttpServletResponse response){
        Result<PaintInventoryRecordDTO> recordResult = getPaintInventoryRecordInfo(id);
        ModelAndView view = new ModelAndView("yqx/page/magic/paint/inventoryPaintDtlExport");
        if (recordResult.isSuccess() && recordResult.getData() != null){

            view.addObject("paintInventoryStockList", recordResult.getData().getPaintInventoryStockDTOList());
        }
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "inventory_paint";
        try {
            filename = URLEncoder.encode("油漆盘点单导出", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("委托单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        return view;
    }



}
