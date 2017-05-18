package com.tqmall.legend.web.goods;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.result.goods.GoodsAttributeDTO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.goods.GoodsAttrbuteFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.tqmallstall.domain.result.Legend.AttrDTO;
import com.tqmall.tqmallstall.service.legend.LegendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-11-14 20:56
 */
@Controller
@RequestMapping("shop/goods_attributes")
public class GoodsAttributesController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsAttributesController.class);
    @Autowired
    private LegendService legendService;

    @Autowired
    GoodsAttrbuteFacade goodsAttrbuteFacade;

    @RequestMapping("get_by_cat_ids")
    @ResponseBody
    public Result getByCatIds(String catIds) {
        com.tqmall.core.common.entity.Result<List<AttrDTO>> result = legendService.getAttrByCatIds(catIds);
        if (logger.isInfoEnabled()) {
            logger.info("从iServer获取分类属性.{}", LogUtils.funToString(catIds, result));
        }
        if(result != null && result.isSuccess()){
            return Result.wrapSuccessfulResult(result.getData());
        }else {
            logger.error("调用接口getAttrByCatIds失败,{}", LogUtils.funToString(catIds,result));
        }
        return Result.wrapErrorResult("",result!=null?result.getMessage():"获取信息失败");
    }

    @RequestMapping(value = "get_by_type_id", method = RequestMethod.GET)
    @ResponseBody
    public Result getByTypeId(@RequestParam(value = "type_id", required = true) Integer goodsTypeId) {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<GoodsAttributeDTO> list = goodsAttrbuteFacade.selectByGoodsTypeId(goodsTypeId,shopId);
        return Result.wrapSuccessfulResult(list);
    }
}
