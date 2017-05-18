package com.tqmall.legend.web.goods;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.param.goods.GoodsUnitSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsUnitDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.goods.GoodsUnitFacade;
import com.tqmall.legend.web.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 配件品牌控制器
 */
@Controller
@RequestMapping("shop/goods_unit")
public class GoodsUnitController extends BaseController {

    @Autowired
    GoodsUnitFacade goodsUnitFacade;

    @RequestMapping("get_by_name")
    @ResponseBody
    public com.tqmall.legend.common.Result getByKeyword(@RequestParam(value = "name", required = false) String name) {
        Long shopId = UserUtils.getShopIdForSession(request);
        String unitNameLike = null;
        if (!StringUtils.isEmpty(name)) {
            unitNameLike = name;
        }
        List<GoodsUnitDTO> list = goodsUnitFacade.selectByNameLike(unitNameLike, shopId);
        return com.tqmall.legend.common.Result.wrapSuccessfulResult(list);
    }

    /**
     * 配件单位列表
     *
     * @return
     */
    @RequestMapping("goods-unit-list")
    public String goodsUnitList() {
        return "yqx/page/goods/goods-unit-list";
    }

    /**
     * 根据ids删除配件单位
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "deleteByIds", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> deleteByIds(@RequestParam(value = "ids[]") final List<Long> ids) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.isTrue(!CollectionUtils.isEmpty(ids), "ids为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userId = UserUtils.getUserIdForSession(request);
                logger.info("删除门店配件单位，操作人：{}，门店id：{}，配件ids：{}", userId, shopId, LogUtils.objectToString(ids));
                return goodsUnitFacade.deleteGoodsUnitByIds(ids);
            }
        }.execute();
    }

    /**
     * 删除配件单位
     *
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public Result<Boolean> delete() {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                Long userId = UserUtils.getUserIdForSession(request);
                logger.info("删除门店全部配件单位，操作人：{}，门店id：{}", userId, shopId);
                return goodsUnitFacade.deleteGoodsUnit(shopId);
            }
        }.execute();
    }

    /**
     * 配件单位分配
     *
     * @param nameLike
     * @param pageable
     * @return
     */
    @RequestMapping("get-page")
    @ResponseBody
    public Result<DefaultPage<GoodsUnitDTO>> getPage(final @RequestParam(value = "nameLike", required = false) String nameLike, final @PageableDefault() Pageable pageable) {
        return new ApiTemplate<DefaultPage<GoodsUnitDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
            }

            @Override
            protected DefaultPage<GoodsUnitDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                GoodsUnitSearchParam goodsUnitSearchParam = new GoodsUnitSearchParam();
                goodsUnitSearchParam.setShopId(shopId);
                goodsUnitSearchParam.setNameLike(nameLike);
                goodsUnitSearchParam.setPage(pageable.getPageNumber());
                goodsUnitSearchParam.setSize(pageable.getPageSize());
                List<String> sorts = Lists.newArrayList();
                sorts.add("id desc");
                goodsUnitSearchParam.setSorts(sorts);
                Result<DefaultPage<GoodsUnitDTO>> result = goodsUnitFacade.getPage(goodsUnitSearchParam, pageable);
                return result.getData();
            }
        }.execute();
    }
}
