package com.tqmall.magic.web.goods;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsPaintVo;
import com.tqmall.legend.entity.goods.GoodsQueryParam;
import com.tqmall.legend.entity.goods.GoodsTagEnum;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.magic.object.result.goods.GoodsPaintExtDTO;
import com.tqmall.magic.object.result.paint.PaintInventoryStockDTO;
import com.tqmall.magic.service.goods.RpcGoodsPaintExtService;
import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by dingbao on 16/11/8.
 */
@Controller
@RequestMapping("goods/paintExt")
@Slf4j
public class GoodsPaintExtController extends BaseController {


    @Autowired
    private RpcGoodsPaintExtService rpcGoodsPaintExtService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    GoodsFacade goodsFacade;


    /**
     * 到油漆库存页面
     *
     * @return
     */
    @RequestMapping("/toPaintStock")
    public String toPaintStock(Model model){
        model.addAttribute("moduleUrl", ModuleUrlEnum.WAREHOUSE.getModuleUrl());
        return "yqx/page/magic/goods/paintStock";
    }

    /**
     * 获取油漆库存总金额
     *
     * @return
     */
    @RequestMapping("/totalStockAmount")
    @ResponseBody
    public Result<BigDecimal> totalStockAmount(){
        Long shopId = UserUtils.getShopIdForSession(request);
        if (shopId == null || shopId < 1){
            log.error("门店信息错误，shopId={}",shopId);
            return Result.wrapErrorResult("","门店信息错误");
        }

        GoodsQueryParam queryParam =new GoodsQueryParam();
        queryParam.setShopId(shopId);
        queryParam.setGoodsTagEnum(GoodsTagEnum.PAINT);
        List<Goods> goodsList  = goodsService.queryGoods(queryParam);
        BigDecimal totalAmount = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(goodsList)){
            List<GoodsPaintVo> goodsPaintVos = BdUtil.bo2do4List(goodsList,GoodsPaintVo.class);
            List<Long> goodsIdList = Lists.newArrayList();
            for (GoodsPaintVo goodsPaintVo : goodsPaintVos){
                goodsIdList.add(goodsPaintVo.getId());
            }
            //获取油漆物料扩展表数据
            com.tqmall.core.common.entity.Result<List<GoodsPaintExtDTO>> result = rpcGoodsPaintExtService.getGoodsPaintExtList(shopId, goodsIdList);

            //组装数据
            if (result.isSuccess() && result.getData() != null){
                for (GoodsPaintVo goodsPaintVo : goodsPaintVos){
                    for (GoodsPaintExtDTO goodsPaintExtDTO : result.getData()){
                        if (goodsPaintVo.getId().equals(goodsPaintExtDTO.getGoodsId())){
                            goodsPaintVo.setNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            goodsPaintVo.setNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            goodsPaintVo.setStirNum(goodsPaintExtDTO.getStirNum());

                            //计算库存总成本
                            PaintInventoryStockDTO paintInventoryStockDTO = new PaintInventoryStockDTO();
                            paintInventoryStockDTO.setNetWeight(goodsPaintExtDTO.getNetWeight());
                            paintInventoryStockDTO.setBucketWeight(goodsPaintExtDTO.getBucketWeight());
                            paintInventoryStockDTO.setStirWeight(goodsPaintExtDTO.getStirWeight());
                            paintInventoryStockDTO.setCurrentNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            paintInventoryStockDTO.setCurrentNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            paintInventoryStockDTO.setCurrentStirNum(goodsPaintExtDTO.getStirNum());
                            paintInventoryStockDTO.setCurrentStock(goodsPaintVo.getStock());
                            paintInventoryStockDTO.setRealNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            paintInventoryStockDTO.setRealNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            paintInventoryStockDTO.setRealStirNum(goodsPaintExtDTO.getStirNum());
                            paintInventoryStockDTO.setRealStock(goodsPaintVo.getStock());
                            paintInventoryStockDTO.setInventoryPrice((goodsPaintVo.getInventoryPrice()).setScale(8, BigDecimal.ROUND_HALF_UP));
                            Result<BigDecimal> result1 = calculatePaintConsumption(paintInventoryStockDTO);
                            if (!result1.isSuccess()){
                                return Result.wrapErrorResult("",result1.getErrorMsg());
                            }
                            goodsPaintVo.setTotalStockAmount(result1.getData());

                        }
                    }
                }
            }

            for (GoodsPaintVo goodsPaintVo : goodsPaintVos){
                totalAmount = totalAmount.add(goodsPaintVo.getTotalStockAmount() == null?BigDecimal.ZERO : goodsPaintVo.getTotalStockAmount());
            }
        }
        return Result.wrapSuccessfulResult(totalAmount);
    }

    /**
     * 分页获取油漆物料列表
     *
     * @param pageable
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result getPaintExtList(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,HttpServletRequest request, HttpServletResponse response){

        // 请求参数
        Long shopId = UserUtils.getShopIdForSession(request);

        // searchForm 条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
        goodsRequest.setShopId(shopId.toString());
        // 品牌
        if (searchParams.containsKey("brandId")) {
            Object brandIdParam = searchParams.get("brandId");
            goodsRequest.setBrandId(Integer.valueOf(brandIdParam + ""));
        }
        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            Object goodsNameParam = searchParams.get("goodsNameLike");
            goodsRequest.setGoodsName((String) goodsNameParam);
        }
        // 零件号
        if (searchParams.containsKey("goodsFormatLike")) {
            Object goodsFormatParam = searchParams.get("goodsFormatLike");
            goodsRequest.setGoodsFormat((String) goodsFormatParam);
        }
        // 库位
        if (searchParams.containsKey("depotLike")) {
            Object depotLikeParam = searchParams.get("depotLike");
            goodsRequest.setDepot((String) depotLikeParam);
        }
        // 油漆资料
        goodsRequest.setGoodsTag(GoodsTagEnum.PAINT.getCode());
        // 页参数(页码\每页条数\排序)
        int pageNum = pageable.getPageNumber() - 1;
        Sort sort = pageable.getSort();
        FieldsSort fieldsSort = new FieldsSort(sort);
        PageableRequest pageableRequest = new PageableRequest(pageNum, pageable.getPageSize(), fieldsSort);
        DefaultPage<SearchGoodsVo> page = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);

        DefaultPage<GoodsPaintVo> paintPage = null;

        if (page != null && !CollectionUtils.isEmpty(page.getContent())) {
            List<SearchGoodsVo> goodses = page.getContent();
            List<GoodsPaintVo> goodsPaintVos = new ArrayList<GoodsPaintVo>(goodses.size());
            List<Long> goodsIdList = new ArrayList<Long>(goodses.size());
            GoodsPaintVo goodsPaintVo =null;
            for (SearchGoodsVo searchGoodsVo : goodses){
                goodsPaintVo =new GoodsPaintVo();
                // 配件ID
                Long goodsId =Long.parseLong(searchGoodsVo.getId());
                goodsPaintVo.setId(goodsId);
                // 配件名称
                goodsPaintVo.setName(searchGoodsVo.getName());
                // 配件上下架状态
                goodsPaintVo.setOnsaleStatus(searchGoodsVo.getOnsaleStatus());
                // 配件编号
                goodsPaintVo.setGoodsSn(searchGoodsVo.getGoodsSn());
                // 配件零件号
                goodsPaintVo.setFormat(searchGoodsVo.getFormat());
                // 配件车型
                goodsPaintVo.setCarInfoStr(searchGoodsVo.getCarInfoStr());
                // 配件库存
                Double stock = searchGoodsVo.getStock();
                stock = (stock==null)? Double.valueOf("0"):stock;
                goodsPaintVo.setStock(BigDecimal.valueOf(stock));
                // 配件仓位
                goodsPaintVo.setDepot(searchGoodsVo.getDepot());
                // 成本价
                Double inventoryPrice =searchGoodsVo.getInventoryPrice();
                inventoryPrice = (inventoryPrice ==null)? Double.valueOf("0"):inventoryPrice;
                goodsPaintVo.setInventoryPrice(BigDecimal.valueOf(inventoryPrice));
                goodsPaintVos.add(goodsPaintVo);
                goodsIdList.add(goodsId);
            }

            // 获取油漆物料扩展表数据 TODO AJAX异步获取
            com.tqmall.core.common.entity.Result<List<GoodsPaintExtDTO>> result = rpcGoodsPaintExtService.getGoodsPaintExtList(shopId, goodsIdList);

            // TODO 双层循环
            //组装数据
         if (result.isSuccess() && result.getData() != null){
                for (GoodsPaintVo goodsPaint : goodsPaintVos){
                    for (GoodsPaintExtDTO goodsPaintExtDTO : result.getData()){
                        if (goodsPaint.getId().equals(goodsPaintExtDTO.getGoodsId())){
                            goodsPaint.setNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            goodsPaint.setNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            goodsPaint.setStirNum(goodsPaintExtDTO.getStirNum());

                            //计算库存总成本
                            PaintInventoryStockDTO paintInventoryStockDTO = new PaintInventoryStockDTO();
                            paintInventoryStockDTO.setNetWeight(goodsPaintExtDTO.getNetWeight());
                            paintInventoryStockDTO.setBucketWeight(goodsPaintExtDTO.getBucketWeight());
                            paintInventoryStockDTO.setStirWeight(goodsPaintExtDTO.getStirWeight());
                            paintInventoryStockDTO.setCurrentNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            paintInventoryStockDTO.setCurrentNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            paintInventoryStockDTO.setCurrentStirNum(goodsPaintExtDTO.getStirNum());
                            paintInventoryStockDTO.setCurrentStock(goodsPaint.getStock());
                            paintInventoryStockDTO.setRealNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            paintInventoryStockDTO.setRealNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            paintInventoryStockDTO.setRealStirNum(goodsPaintExtDTO.getStirNum());
                            paintInventoryStockDTO.setRealStock(goodsPaint.getStock());
                            paintInventoryStockDTO.setInventoryPrice((goodsPaint.getInventoryPrice()).setScale(8, BigDecimal.ROUND_HALF_UP));
                            Result<BigDecimal> result1 = calculatePaintConsumption(paintInventoryStockDTO);
                            if (!result1.isSuccess()){
                                return Result.wrapErrorResult("",result1.getErrorMsg());
                            }
                            goodsPaint.setTotalStockAmount(result1.getData());

                        }
                    }
                }
            }

            int pageNumber = pageable.getPageNumber();
            pageNumber = pageNumber < 1 ? 1 : pageNumber;
            int pageSize = pageable.getPageSize();
            pageSize = pageSize < 1 ? 1 : pageSize;
            PageRequest pageRequest = new PageRequest(pageNumber - 1, pageSize);
            paintPage = new DefaultPage(goodsPaintVos,pageRequest, page.getTotalElements());
            return Result.wrapSuccessfulResult(paintPage);

        }
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 全部油漆goodsIds
     * @return
     */
    @RequestMapping("/getAllPaintList")
    @ResponseBody
    public Result<List<Long>> getAllPaintList(HttpServletRequest request){

        Long shopId = UserUtils.getShopIdForSession(request);

        // 查询条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        // 包装查询参数
        GoodsQueryParam queryParam = new GoodsQueryParam();
        // 品牌
        if (searchParams.containsKey("brandId")) {
            queryParam.setBrandId((String) searchParams.get("brandId"));
        }
        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            queryParam.setGoodsNameLike((String) searchParams.get("goodsNameLike"));
        }
        // 配件型号
        if (searchParams.containsKey("goodsFormatLike")) {
            queryParam.setGoodsFormatLike((String) searchParams.get("goodsFormatLike"));
        }
        // 仓位
        if (searchParams.containsKey("depotLike")) {
            queryParam.setDepotLike((String) searchParams.get("depotLike"));
        }
        queryParam.setShopId(shopId);
        queryParam.setGoodsTagEnum(GoodsTagEnum.PAINT);
        List<Goods> goodses = goodsService.queryGoods(queryParam);

        List<Long> goodsIds = new ArrayList<Long>();
        for (Goods goods : goodses) {
            goodsIds.add(goods.getId());
        }

        return Result.wrapSuccessfulResult(goodsIds);
    }

    /**
     * 选中油漆
     *
     * @param goodsIds
     * @return
     */
    @RequestMapping("/getSelectPaintList")
    @ResponseBody
    public Result<List<GoodsPaintVo>> getSelectPaintList(Long[] goodsIds){
        if (goodsIds == null || goodsIds.length < 1){
            return Result.wrapSuccessfulResult(null);
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        List<Goods> goodsList = goodsService.selectPaintByIds(Arrays.asList(goodsIds));
        List<GoodsPaintVo> goodsPaintVos = BdUtil.bo2do4List(goodsList,GoodsPaintVo.class);

        if (!CollectionUtils.isEmpty(goodsPaintVos)){
            //获取油漆物料扩展表数据
            com.tqmall.core.common.entity.Result<List<GoodsPaintExtDTO>> result = null;
            try {
                result = rpcGoodsPaintExtService.getGoodsPaintExtList(shopId, Arrays.asList(goodsIds));
            } catch (Exception e) {
                log.error("远程调用获取油漆物料扩展表数据失败",e);
                return Result.wrapErrorResult("","远程调用获取油漆物料扩展表数据失败");
            }

            //组装数据
            if (result.isSuccess() && result.getData() != null){
                for (GoodsPaintVo goodsPaintVo : goodsPaintVos){
                    for (GoodsPaintExtDTO goodsPaintExtDTO : result.getData()){
                        if (goodsPaintVo.getId().equals(goodsPaintExtDTO.getGoodsId())){
                            goodsPaintVo.setNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                            goodsPaintVo.setNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                            goodsPaintVo.setStirNum(goodsPaintExtDTO.getStirNum());
                            goodsPaintVo.setTotalStockAmount(goodsPaintExtDTO.getTotalStockAmount());
                            goodsPaintVo.setNetWeight(goodsPaintExtDTO.getNetWeight());
                            goodsPaintVo.setBucketWeight(goodsPaintExtDTO.getBucketWeight());
                            goodsPaintVo.setStirWeight(goodsPaintExtDTO.getStirWeight());
                        }
                    }
                }
            }
        }
        return Result.wrapSuccessfulResult(goodsPaintVos);
    }

    /**
     * 导出油漆物料Excel表
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportPaintStockList")
    @ResponseBody
    public Object exportPaintStockList(@PageableDefault(page = 1, value = 5000, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,HttpServletRequest request, HttpServletResponse response) {
        Result<Page<GoodsPaintVo>> result = getPaintExtList(pageable,request,response);
        ModelAndView view = new ModelAndView("yqx/page/magic/goods/paintStockExport");
        view.addObject("page", result.getData());
        // 设置响应头
        response.setContentType("application/x-msdownload");
        String filename = "paint_stock";
        try {
            filename = URLEncoder.encode("油漆库存导出", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("委托单导出URLEncoder转义不正确");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
        return view;
    }

    /**
     * 选择油漆弹出框
     *
     * @param request
     * @return
     */
    @RequestMapping("/selectPaints")
    @ResponseBody
    public Result<List<Goods>> selectPaints(HttpServletRequest request) {

        Long shopId = UserUtils.getShopIdForSession(request);

        // 获取查询条件
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);

        // 包装查询参数
        GoodsQueryParam queryParam = new GoodsQueryParam();
        // 品牌
        if (searchParams.containsKey("brandId")) {
            queryParam.setBrandId((String) searchParams.get("brandId"));
        }
        // 车型
        if (searchParams.containsKey("carInfoLike")) {
            queryParam.setCarInfoLike((String) searchParams.get("carInfoLike"));
        }
        // 配件名称
        if (searchParams.containsKey("goodsNameLike")) {
            queryParam.setGoodsNameLike((String) searchParams.get("goodsNameLike"));
        }
        // 配件型号
        if (searchParams.containsKey("goodsFormatLike")) {
            queryParam.setGoodsFormatLike((String) searchParams.get("goodsFormatLike"));
        }
        // 仓位
        if (searchParams.containsKey("depotLike")) {
            queryParam.setDepotLike((String) searchParams.get("depotLike"));
        }
        queryParam.setShopId(shopId);
        queryParam.setGoodsTagEnum(GoodsTagEnum.PAINT);
        List<String> sorts = new ArrayList<String>();
        sorts.add(" depot ");
        queryParam.setSorts(sorts);
        List<Goods> goodses = goodsService.queryGoods(queryParam);

        return Result.wrapSuccessfulResult(goodses);
    }

    /**
     * 获得单个油漆信息
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/getPaintById")
    @ResponseBody
    public Result<GoodsPaintVo> getPaintById(Long id,HttpServletRequest request){
        if (id == null || id < 1){
            log.error("参数错误。id={}",id);
            return Result.wrapErrorResult("","参数错误");
        }
        Long shopId = UserUtils.getShopIdForSession(request);
        Optional<Goods> goodsOptional = goodsService.selectById(id);
        if (!goodsOptional.isPresent()) {
            log.error("获取油漆信息失败,对应配件不存在,配件ID:{}", id);
            return Result.wrapErrorResult("", "对应配件不存在");
        }
        GoodsPaintVo goodsPaintVo = BdUtil.bo2do(goodsOptional.get(), GoodsPaintVo.class);
        if (goodsPaintVo != null) {
            List<Long> goodsIds = new ArrayList<>();
            goodsIds.add(goodsPaintVo.getId());
            com.tqmall.core.common.entity.Result<List<GoodsPaintExtDTO>> result = null;
            try {
                result = rpcGoodsPaintExtService.getGoodsPaintExtList(shopId, goodsIds);
            } catch (Exception e) {
                log.error("远程调用获取油漆物料扩展表数据失败",e);
                return Result.wrapErrorResult("","远程调用获取油漆物料扩展表数据失败");
            }
            if (result.isSuccess() && !CollectionUtils.isEmpty(result.getData())){
                GoodsPaintExtDTO goodsPaintExtDTO = result.getData().get(0);
                goodsPaintVo.setNoBucketWeight(goodsPaintExtDTO.getNoBucketWeight());
                goodsPaintVo.setNoBucketNum(goodsPaintExtDTO.getNoBucketNum());
                goodsPaintVo.setStirNum(goodsPaintExtDTO.getStirNum());
                goodsPaintVo.setTotalStockAmount(goodsPaintExtDTO.getTotalStockAmount());
                goodsPaintVo.setNetWeight(goodsPaintExtDTO.getNetWeight());
                goodsPaintVo.setBucketWeight(goodsPaintExtDTO.getBucketWeight());
                goodsPaintVo.setStirWeight(goodsPaintExtDTO.getStirWeight());
            }

        }
        return Result.wrapSuccessfulResult(goodsPaintVo);
    }

    /**
     * 计算库存总成本
     *
     * @param paintInventoryStockDTO
     * @return
     */
    private  Result<BigDecimal> calculatePaintConsumption(PaintInventoryStockDTO paintInventoryStockDTO){
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

        //现存搅拌头数量
        BigDecimal currentStirNum = paintInventoryStockDTO.getCurrentStirNum();
        //实盘搅拌头数量
        BigDecimal realStirNum = paintInventoryStockDTO.getRealStirNum();
        //现存非整桶数量-现存搅拌头数量
        BigDecimal currentDiffNum = paintInventoryStockDTO.getCurrentNoBucketNum().subtract(currentStirNum);
        //实盘非整桶数量-实盘搅拌头数量
        BigDecimal realDiffNum = paintInventoryStockDTO.getRealNoBucketNum().subtract(realStirNum);
        BigDecimal maxCurrentDiffNum = currentDiffNum.compareTo(realDiffNum) >= 0 ? currentDiffNum : realDiffNum;
        BigDecimal maxStirNum = currentStirNum.compareTo(realStirNum) >= 0 ? currentStirNum : realStirNum;

        //净含量
        BigDecimal netWeight = paintInventoryStockDTO.getNetWeight();

        BigDecimal currentDiffx = maxCurrentDiffNum.subtract(currentDiffNum);

        BigDecimal currentDiffy = maxStirNum.subtract(currentStirNum);

        BigDecimal realDiffx = maxCurrentDiffNum.subtract(realDiffNum);

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

        return Result.wrapSuccessfulResult(totalStockAmount.setScale(2,BigDecimal.ROUND_HALF_EVEN));
    }



}
