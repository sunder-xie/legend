package com.tqmall.legend.web.goods;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.param.goods.GoodsCategoryParam;
import com.tqmall.itemcenter.object.param.goods.GoodsCategorySearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.goods.GoodsCategoryFacade;
import com.tqmall.legend.facade.goods.vo.GoodsCategoryVo;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.vo.goods.TqmallCategoryDTO;
import com.tqmall.tqmallstall.domain.param.category.CategoryQueryParam;
import com.tqmall.tqmallstall.domain.result.category.CategoryDTO;
import com.tqmall.tqmallstall.service.category.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 配件分类Controller
 */

@Slf4j
@Controller
@RequestMapping("shop/goods_category")
public class GoodsCategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsCategoryFacade goodsCategoryFacade;
    @Value("${new.i.search.url}")
    private String iSearchUrl;


    /**
     * 获取分类
     *
     * @param pid
     * @param customCat
     * @param request
     * @return
     */
    @RequestMapping("std/get_by_pid")
    @ResponseBody
    public Result standGetByPid(@RequestParam(value = "pid", required = true)Integer pid,
                                @RequestParam(value = "customCat", required = false, defaultValue = "true") Boolean customCat,
                                HttpServletRequest request){

        Result<List<CategoryDTO>> result;

        if(Integer.valueOf(-1).equals(pid)){
            List<CategoryDTO> catList = new LinkedList<>();
            catList.add(this.newCustomCat2());
            result = Result.wrapSuccessfulResult(catList);
        } else if(Integer.valueOf(-2).equals(pid)){
            result = Result.wrapSuccessfulResult(this.getCustomCatList(UserUtils.getShopIdForSession(request)));
        } else {
            try {
                List<CategoryDTO> categoryDTOs = new LinkedList<>();
                CategoryQueryParam qParam = new CategoryQueryParam();
                //1表示全展示
                qParam.setIsShow(1);
                qParam.setVehicleCode("C");
                qParam.setPid(pid);

                com.tqmall.core.common.entity.Result<List<CategoryDTO>> categoryListResult = this.categoryService.getTrueAllByCondition(qParam);
                if(categoryListResult != null && categoryListResult.isSuccess()){
                    categoryDTOs.addAll(categoryListResult.getData());
                } else {
                    log.error("根据pid获取标准C物料分类失败.{}", LogUtils.funToString(pid, categoryListResult));
                }

                qParam.setVehicleCode("CH");
                qParam.setPid(pid);
                categoryListResult = this.categoryService.getTrueAllByCondition(qParam);
                if(categoryListResult != null && categoryListResult.isSuccess()){
                    categoryDTOs.addAll(categoryListResult.getData());
                } else {
                    log.error("根据pid获取标准CH物料分类失败.{}", LogUtils.funToString(pid, categoryListResult));
                }

                result = Result.wrapSuccessfulResult(categoryDTOs);
                if (customCat && Integer.valueOf(0).equals(pid)) {
                    result.getData().add(this.newCustomerCat1());
                }
            } catch (Exception e) {
                log.error("根据pid获取标准物料分类失败.", e);
                result = Result.wrapErrorResult("", "获取标准物料分类失败.");
            }
        }
        return result;
    }

    /**
     * 搜索结果分为二部分:
     * <ul>
     *     <li>1.电商类目搜索</li>
     *     <li>2.门店自定义类目搜索</li>
     * </ul>
     * @see <a href="http://confluence.weichedao.com/pages/viewpage.action?pageId=10028587">搜索接口文档</a>
     * @param key
     * @param customCat
     * @return
     */
    @RequestMapping("std/get_by_key")
    @ResponseBody
    public Result standGetByKey(@RequestParam(value = "key", defaultValue = "")String key,
                                @RequestParam(value = "customCat", defaultValue = "true", required = false)Boolean customCat,
                                HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        List<GoodsCategoryVo> goodsCategoryVOList = goodsCategoryFacade.getGoodsCategoryListForPc(key,customCat,shopId);
        return Result.wrapSuccessfulResult(goodsCategoryVOList);
    }

    //自定义配件类别管理
    @RequestMapping("goods-category-list")
    public String toGoodsCategoryList(){
        return "yqx/page/goods/goods-category-list";
    }

    //自定义配件类别保存更新
    @RequestMapping(value = "goods-category-save",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> saveGoodsCategory(@RequestBody final GoodsCategoryParam goodsCategoryParam){
        return new ApiTemplate<String>() {
            Long shopId  = UserUtils.getShopIdForSession(request);
            Long userId = UserUtils.getUserIdForSession(request);
            String cateName = goodsCategoryParam.getCatName();
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.hasLength(cateName,"配件类别名称不能为空");
                Assert.notNull(shopId, "门店id不能为空");
                Assert.notNull(userId, "用户id不能为空");
                if (cateName.length() > 90){
                    throw new IllegalArgumentException("配件类别名称长度不能超过90");
                }
            }

            @Override
            protected String process() throws BizException {
                goodsCategoryParam.setCreator(userId);
                goodsCategoryParam.setModifier(userId);
                goodsCategoryParam.setShopId(shopId);
                return goodsCategoryFacade.save(goodsCategoryParam);
            }
        }.execute();

    }

    //自定义配件类别删除
    @RequestMapping(value = "goods-category-delete",method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.core.common.entity.Result<String> deleteGoodsCategory(final @RequestParam(value = "id",required = true)Long id){
        return new ApiTemplate<String>() {
            Long shopId  = UserUtils.getShopIdForSession(request);
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "配件类别id不能为空");
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected String process() throws BizException {
                return goodsCategoryFacade.delete(shopId,id);
            }
        }.execute();
    }

    //自定义配件类别搜索
    @RequestMapping("search")
    @ResponseBody
    public com.tqmall.core.common.entity.Result<DefaultPage<GoodsCategoryDTO>> getCategoriesFromSearch(final @RequestParam(value = "catName", defaultValue = "") String catName, final @PageableDefault() Pageable pageable){
        return new ApiTemplate<DefaultPage<GoodsCategoryDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected DefaultPage<GoodsCategoryDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsCategorySearchParam goodsCategorySearchParam = new GoodsCategorySearchParam();
                goodsCategorySearchParam.setShopId(shopId);
                goodsCategorySearchParam.setCatName(catName);
                goodsCategorySearchParam.setPage(pageable.getPageNumber());
                goodsCategorySearchParam.setSize(pageable.getPageSize());
                com.tqmall.core.common.entity.Result<DefaultPage<com.tqmall.itemcenter.object.result.goods.GoodsCategoryDTO>> result =
                        goodsCategoryFacade.getGoodsCategoriesFromSearch(goodsCategorySearchParam, pageable);
                return result.getData();
            }
        }.execute();

    }



    private CategoryDTO newCustomerCat1(){
        return _newCusomCat(-1);
    }

    private CategoryDTO newCustomCat2(){
        return _newCusomCat(-2);
    }
    private CategoryDTO _newCusomCat(Integer id) {
        TqmallCategoryDTO cat = new TqmallCategoryDTO();
        cat.setCatId(id);
        cat.setCatName("自定义分类");
        cat.setCustomCat(true);
        return cat;
    }

    /**
     * 获取门店自有物料分类
     * @param shopId
     * @return
     */
    private List<CategoryDTO> getCustomCatList(Long shopId){
        List<GoodsCategoryDTO> categoryList = goodsCategoryFacade.getCustomCategoriesByShopId(shopId);
        List<CategoryDTO> categoryDTOList = new ArrayList<>(categoryList.size());
        for (GoodsCategoryDTO goodsCategory : categoryList) {
            TqmallCategoryDTO categoryDTO = new TqmallCategoryDTO();
            categoryDTO.setCustomCat(true);
            categoryDTO.setCatId(goodsCategory.getId().intValue());
            categoryDTO.setCatName(goodsCategory.getCatName());
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }
}
