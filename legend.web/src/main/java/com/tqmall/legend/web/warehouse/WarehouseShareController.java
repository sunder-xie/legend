package com.tqmall.legend.web.warehouse;

import com.google.common.collect.Lists;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.BizAssert;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.warehouse.WarehouseShareService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.GoodsQueryRequest;
import com.tqmall.legend.entity.statistics.SimplePage;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;
import com.tqmall.legend.facade.goods.GoodsFacade;
import com.tqmall.legend.facade.goods.vo.SearchGoodsVo;
import com.tqmall.legend.facade.warehouse.WarehouseShareFacade;
import com.tqmall.legend.facade.warehouse.vo.Over2MonthTurnoverVO;
import com.tqmall.legend.facade.warehouse.vo.PublishGoodsVO;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareGoodsDetail;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareVO;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.goods.param.LegendGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by tanghao on 16/11/10.
 */
@Controller
@RequestMapping("shop/warehouse/share")
@Slf4j
public class WarehouseShareController extends BaseController{

    @Autowired
    private WarehouseShareFacade warehouseShareFacade;
    @Autowired
    private GoodsFacade goodsFacade;
    @Autowired
    private WarehouseShareService warehouseShareService;

    // 出售库存配件页
    @RequestMapping(value = "sale", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("moduleUrl", "warehouse");
        model.addAttribute("isShowList",warehouseShareFacade.isPublish(UserUtils.getShopIdForSession(request)));
        return "yqx/page/warehouse/share/sale";
    }
    // 购买库存配件页
    @RequestMapping(value = "buy", method = RequestMethod.GET)
    public String buy(Model model) {
        model.addAttribute("moduleUrl", "warehouse");
        return "yqx/page/warehouse/share/buy";
    }

    /**
     * 超过2个月未周转库存(库存共享顶部信息)
     *
     * @return
     */
    @RequestMapping(value = "/topInfo", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Over2MonthTurnoverVO> getOver2MonthTurnoverInfo() {
        return new ApiTemplate<Over2MonthTurnoverVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected Over2MonthTurnoverVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return warehouseShareFacade.getOver2MonthTurnoverInfo(shopId, 2);
            }
        }.execute();
    }

    /**
     * 推荐的库存配件
     *
     * @return
     */
    @RequestMapping(value = "/adviceGoods", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<Goods>> getAdviceGoodsInfo() {
        return new ApiTemplate<List<Goods>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<Goods> process() throws BizException {

                Long shopId = UserUtils.getShopIdForSession(request);
                int pageNum = 0;
                int pageSize = 20;
                PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize, Sort.Direction.ASC,"last_in_time","id");
                GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
                LegendGoodsRequest goodsRequest = convertParam(goodsQueryRequest,shopId,null);
                try {
                    DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
                    List<Goods> goodses = goodsConvert(searchPage.getContent());
                    return goodses;
                } catch (Exception e) {
                    log.error("根据关键字获取物料列表信息异常，异常信息{}", e);
                    throw new BizException(LegendError.COMMON_ERROR.getMessage());
                }
            }
        }.execute();
    }

    private List<Goods> goodsConvert(List<SearchGoodsVo> goodsVos){
        if (CollectionUtils.isEmpty(goodsVos)) {
            return Collections.emptyList();
        }
        List<Goods> goodses = Lists.newArrayListWithCapacity(goodsVos.size());
        for(SearchGoodsVo vo : goodsVos){
            Goods goods = new Goods();
            goods.setPrice(BigDecimal.valueOf(vo.getPrice()));
            goods.setStock(BigDecimal.valueOf(vo.getStock()));
            goods.setInventoryPrice(BigDecimal.valueOf(vo.getInventoryPrice()));
            goods.setShopId(Long.parseLong(vo.getShopId()));
            goods.setCat2Name(vo.getCatName());
            goods.setId(Long.parseLong(vo.getId()));
            goods.setCarInfo(vo.getCarInfo());
            goods.setCat1Name(vo.getCatName());
            goods.setLastInTimeStr(vo.getLastInTime());
            goods.setLastInTime(DateUtil.convertStringToDate(vo.getLastInTime(),"yyyy-MM-dd ss:mm:ss"));
            goods.setMeasureUnit(vo.getMeasureUnit());
            goods.setGoodsSn(vo.getGoodsSn());
            goods.setName(vo.getName());

            goodses.add(goods);
        }
        return goodses;
    }

    /**
     * 查询库存商品(按最后入库时间排序)
     *
     * @return
     */
    @RequestMapping(value = "/queryGoods", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<DefaultPage<SearchGoodsVo>> queryGoods(@RequestBody final GoodsQueryRequest goodsQueryRequest) {
        return new ApiTemplate<DefaultPage<SearchGoodsVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(goodsQueryRequest,"参数对象不能为空.");
                Assert.notNull(goodsQueryRequest.getPage(),"页数不能为空.");
            }

            @Override
            protected DefaultPage<SearchGoodsVo> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                List<Long> goodsIds = warehouseShareService.queryExistGoodsId(shopId);
                int pageNum = goodsQueryRequest.getPage() -1;
                int pageSize = goodsQueryRequest.getSize();
                PageableRequest pageableRequest = new PageableRequest(pageNum, pageSize, Sort.Direction.ASC,"last_in_time","id");
                LegendGoodsRequest goodsRequest = convertParam(goodsQueryRequest,shopId,goodsIds);
                try {
                    DefaultPage<SearchGoodsVo> searchPage = goodsFacade.goodsPageSearch(goodsRequest, pageableRequest);
                    long total = searchPage.getTotalElements();
                    DefaultPage page = new DefaultPage(searchPage.getContent(), pageableRequest, total);
                    page.setPageUri(request.getRequestURI());
                    page.setSearchParam(ServletUtils.getParametersStringStartWith(request));
                    return page;
                } catch (Exception e) {
                    log.error("根据关键字获取物料列表信息异常，异常信息{}", e);
                    throw new BizException(LegendError.COMMON_ERROR.getMessage());
                }
            }
        }.execute();
    }

    private LegendGoodsRequest convertParam(GoodsQueryRequest goodsQueryRequest,Long shopId,List<Long> notInList){
        LegendGoodsRequest goodsRequest = new LegendGoodsRequest();
        if(null != goodsQueryRequest.getGoodsName()){
            goodsRequest.setGoodsName(goodsQueryRequest.getGoodsName());
        }
        if (null != goodsQueryRequest.getGoodsCat()){
            goodsRequest.setGoodsCat(goodsQueryRequest.getGoodsCat());
        }
        goodsRequest.setShopId(shopId.toString());
        goodsRequest.setNotInGoodsIds(notInList);
        goodsRequest.setZeroStockRange(1);
        return goodsRequest;
    }

    /**
     * 发布商品
     *
     * @return
     */
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<PublishGoodsVO> publish(@RequestBody final List<WarehouseShare> warehouseShare) {
        return new ApiTemplate<PublishGoodsVO>() {
            Long shopId = UserUtils.getShopIdForSession(request);
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notEmpty(warehouseShare,"请选择要发布的商品");
                for(WarehouseShare ws : warehouseShare){
                    ws.setShopId(shopId);
                    checkPublishParam(ws);

                    BigDecimal goodsPrice = ws.getGoodsPrice();
                    goodsPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                    ws.setGoodsPrice(goodsPrice);
                }
            }

            @Override
            protected PublishGoodsVO process() throws BizException {
                UserInfo ui = UserUtils.getUserInfo(request);
                Long userId = 0L;
                if(ui != null){
                    userId = ui.getUserId();
                }
                /**
                 * 发布商品
                 */
                PublishGoodsVO vo = warehouseShareFacade.publishGoods(warehouseShare,userId);
                BizAssert.notNull(vo,"调用发布库存共享接口返回对象为空.");
                return vo;
            }
        }.execute();
    }

    /**
     * 重新发布商品
     *
     * @return
     */
    @RequestMapping(value = "/republish", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> republish(@RequestBody final WarehouseShare warehouseShare) {
        return new ApiTemplate<String>() {
            Long shopId = UserUtils.getShopIdForSession(request);
            @Override
            protected void checkParams() throws IllegalArgumentException {
                checkPublishParam(warehouseShare);
                warehouseShare.setShopId(shopId);
            }

            @Override
            protected String process() throws BizException {
                BigDecimal goodsPrice = warehouseShare.getGoodsPrice();
                goodsPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                warehouseShare.setGoodsPrice(goodsPrice);
                UserInfo ui = UserUtils.getUserInfo(request);
                Long userId = 0L;
                if(ui != null){
                    userId = ui.getUserId();
                }
                /**
                 * 发布商品
                 */
                return warehouseShareFacade.rePublishGoods(warehouseShare,userId);
            }
        }.execute();
    }

    /**
     * 检测发布商品参数
     * @param warehouseShare
     */
    private void checkPublishParam(WarehouseShare warehouseShare){
        Assert.notNull(warehouseShare.getGoodsName(),"商品名称不能为空.");
        Assert.isTrue(warehouseShare.getGoodsName().length()<51,"商品名不能大于50个字.");
        Assert.notNull(warehouseShare.getGoodsId(),"商品id不能为空.");
        Assert.notNull(warehouseShare.getGoodsPrice(),"商品售价不能为空.");
        Assert.notNull(warehouseShare.getGoodsStock(),"商品数量不能为空.");
        Assert.notNull(warehouseShare.getInventoryPrice(),"商品成本不能为空.");
        Assert.isTrue(warehouseShare.getGoodsPrice().compareTo(BigDecimal.valueOf(99999999))<0,"售价不能大于8位数");
        Assert.isTrue(warehouseShare.getGoodsStock().compareTo(BigDecimal.valueOf(99999999))<0,"库存数量不能大于8位数");
        Assert.isTrue(warehouseShare.getSaleNumber().compareTo(BigDecimal.valueOf(99999999))<0,"出售价不能大于8位数");
        Assert.isTrue(warehouseShare.getGoodsPrice().compareTo(BigDecimal.ZERO)>0,"商品价格不能小于等于零");
        Assert.isTrue(warehouseShare.getGoodsStock().compareTo(BigDecimal.ZERO)>0,"库存数量不能小于等于零");
        Assert.isTrue(warehouseShare.getSaleNumber().compareTo(BigDecimal.ZERO)>0,"库存数量不能小于等于零");
        Assert.hasText(warehouseShare.getGoodsName(),"商品名称不能为空");
    }

    /**
     * 检测是否配置了联系人
     *
     * @return
     */
    @RequestMapping(value = "/checkContact", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<WarehouseShareShopContact> checkContact() {
        return new ApiTemplate<WarehouseShareShopContact>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected WarehouseShareShopContact process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                WarehouseShareShopContact warehouseShareShopContact = warehouseShareFacade.checkContact(shopId);
                return warehouseShareShopContact;
            }
        }.execute();
    }

    /**
     * 查询门店联系人
     *
     * @return
     */
    @RequestMapping(value = "/queryShopContact", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<List<WarehouseShareShopContactVO>> queryShopContact() {
        return new ApiTemplate<List<WarehouseShareShopContactVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected List<WarehouseShareShopContactVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return warehouseShareFacade.queryShopContact(shopId);
            }
        }.execute();
    }

    /**
     * 改变门店联系人
     *
     * @return
     */
    @RequestMapping(value = "/changeShopContact", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> changeShopContact(final Long userId) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(userId,"门店员工id不能为空.");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                UserInfo userInfo = UserUtils.getUserInfo(request);//用户信息
                BizAssert.isTrue(userInfo.getUserIsAdmin() == 1,"当前用户权限不足,请联系管理员修改.");
                return warehouseShareFacade.changeShopContact(shopId,userId);
            }
        }.execute();
    }

    /**
     * 改变出售货物状态
     *
     * @return
     */
    @RequestMapping(value = "/changeSaleGoodsStatus", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Boolean> checkContact(final Long id,final Integer goodsStatus) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id,"出售库存id不能为空.");
                Assert.notNull(goodsStatus,"要改变的出售库存状态不能为空.");
            }

            @Override
            protected Boolean process() throws BizException {
                return warehouseShareFacade.changeGoodsStatus(id,goodsStatus);
            }
        }.execute();
    }

    /**
     * 获取各种状态的销售列表数量
     *
     * @return
     */
    @RequestMapping(value = "/querySaleCount", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<WarehouseShareCountVO> querySaleCount() {
        return new ApiTemplate<WarehouseShareCountVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected WarehouseShareCountVO process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return warehouseShareFacade.querySaleCount(shopId);
            }
        }.execute();
    }

    /**
     * 查询正在出售货物列表
     *
     * @return
     */
    @RequestMapping(value = "/querySaleList", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<SimplePage<WarehouseShare>> checkContact(@RequestBody final GoodsQueryRequest goodsQueryRequest) {
        return new ApiTemplate<SimplePage<WarehouseShare>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(goodsQueryRequest,"对象不能为空.");
                Assert.notNull(goodsQueryRequest.getPage(),"页数不能为空.");
                Long shopId = UserUtils.getShopIdForSession(request);
                goodsQueryRequest.setShopId(shopId);
            }

            @Override
            protected SimplePage<WarehouseShare> process() throws BizException {
                return warehouseShareFacade.querySaleListByGoodsStatus(goodsQueryRequest);
            }
        }.execute();
    }

    /**
     * 查询库存共享配件列表
     * @param goodsCate 配件类型
     * @param goodsName 配件名称
     * @param region 1:全国, 2:本省, 3:本市
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShareGoodsList", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<Page<WarehouseShareVO>> getShareGoodsList(@RequestParam(value = "goodsCate", required = false) final String goodsCate,
                                                                                          @RequestParam(value = "goodsName", required = false) final String goodsName,
                                                                                          @RequestParam(value = "region", required = false, defaultValue = "1") final int region,
                                                                                          @PageableDefault(page = 1, size = 20) final Pageable pageable,
                                                                                          final HttpServletRequest request) {
        return new ApiTemplate<Page<WarehouseShareVO>>() {
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
            protected Page<WarehouseShareVO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return warehouseShareService.getWarehouseShareGoodsListByCondition(shopId, goodsCate, goodsName, region, pageable);
            }
        }.execute();
    }

    /**
     * 查询库存共享配件详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/goods/detail", method = RequestMethod.GET)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<WarehouseShareGoodsDetail> getWarehouseShareGoodsDetail(@RequestParam("id") final Long id, final HttpServletRequest request) {
        return new ApiTemplate<WarehouseShareGoodsDetail>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizExce[ption类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected WarehouseShareGoodsDetail process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                return warehouseShareService.getWarehouseShareGoodsDetail(id, shopId);
            }
        }.execute();
    }


}
