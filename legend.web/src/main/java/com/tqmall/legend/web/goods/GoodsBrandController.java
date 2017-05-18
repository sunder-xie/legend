package com.tqmall.legend.web.goods;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandParam;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.goods.GoodsBrandFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.tqmallstall.domain.result.brand.BrandDTO;
import com.tqmall.tqmallstall.service.brand.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Created by lixiao on 14-11-14.
 */
@Controller
@RequestMapping("shop/goods_brand")
public class GoodsBrandController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsBrandController.class);

    @Autowired
    private BrandService brandService;
    @Autowired
    private GoodsBrandFacade goodsBrandFacade;

    @RequestMapping("list")
    @ResponseBody
    public Result list() {
        Result<List<BrandDTO>> brandList = brandService.getBrandAll();
        if (brandList != null && brandList.isSuccess()) {
            if (logger.isDebugEnabled()) {
                logger.debug("调用获取品牌接口成功,{}", LogUtils.wraperResult(brandList));
            }
            return brandList;
        } else {
            logger.error("调用获取品牌接口失败,{}", LogUtils.wraperResult(brandList));
        }
        return Result.wrapErrorResult("9999", "调用获取品牌接口失败");
    }

    @RequestMapping("shop_list")
    @ResponseBody
    public Result shopList(HttpServletRequest request) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<GoodsBrandDTO> goodsBrandList = goodsBrandFacade.getGoodsBrands(shopId);
        return Result.wrapSuccessfulResult(goodsBrandList);
    }


    /**
     * 查询品牌列表 仅供仓库使用
     *
     * @param request
     * @param session
     * @param response
     * @return
     */
    @RequestMapping(value = "inwarehosue/list", method = RequestMethod.GET)
    @ResponseBody
    public Result listForWarehouse(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        Long shopId = UserUtils.getShopIdForSession(request);

        // '淘汽品牌'到'商品品牌' 便于下拉框搜索
        List<GoodsBrandDTO> goodsBrandList = goodsBrandFacade.getGoodsBrands(shopId);
        return Result.wrapSuccessfulResult(goodsBrandList);
    }


    //自定义配件品牌管理
    @RequestMapping("goods-brand-list")
    public String toGoodsBrandsList(){
        return "yqx/page/goods/goods-brand-list";
    }

    //自定义配件品牌添加
    @RequestMapping(value = "goods-brand-save",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> saveGoodsBrand(final @RequestBody GoodsBrandParam goodsBrandParam){
        return new ApiTemplate<String>() {
            Long shopId  = UserUtils.getShopIdForSession(request);
            Long userId = UserUtils.getUserIdForSession(request);
            String brandName = goodsBrandParam.getBrandName();
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasLength(brandName, "配件品牌名称不能为空");
                Assert.notNull(shopId, "门店id不能为空");
                Assert.notNull(userId, "用户id不能为空");
                if (brandName.length() > 60){
                    throw new IllegalArgumentException("配件品牌名称长度不能超过60");
                }
            }

            @Override
            protected String process() throws BizException {
                goodsBrandParam.setShopId(shopId);
                goodsBrandParam.setCreator(userId);
                goodsBrandParam.setModifier(userId);
                return goodsBrandFacade.save(goodsBrandParam);
            }
        }.execute();

    }

    //自定义配件品牌删除
    @RequestMapping(value = "goods-brand-delete",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> deleteGoodsCategory(final @RequestParam(value = "id",required = true)Long id){
        return new ApiTemplate<String>() {
            Long shopId  = UserUtils.getShopIdForSession(request);
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "配件品牌id不能为空");
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected String process() throws BizException {
                return goodsBrandFacade.delete(shopId,id);
            }
        }.execute();
    }

    //自定义配件品牌搜索
    @RequestMapping("search")
    @ResponseBody
    public Result<DefaultPage<GoodsBrandDTO>> getGoodsBrandsFromSearch(final @RequestParam(value = "brandName", defaultValue = "") String brandName,
                                                                                                final @PageableDefault() Pageable pageable){
        return new ApiTemplate<DefaultPage<GoodsBrandDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected DefaultPage<GoodsBrandDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsBrandSearchParam goodsBrandSearchParam = new GoodsBrandSearchParam();
                goodsBrandSearchParam.setShopId(shopId);
                goodsBrandSearchParam.setBrandName(brandName);
                goodsBrandSearchParam.setPage(pageable.getPageNumber());
                goodsBrandSearchParam.setSize(pageable.getPageSize());
                Result<DefaultPage<GoodsBrandDTO>> result = goodsBrandFacade.getGoodsBrandsFromSearch(goodsBrandSearchParam, pageable);
                return result.getData();
            }
        }.execute();


    }
}
