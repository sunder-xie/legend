package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.biz.bo.goods.GoodsBo;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.facade.magic.BPGoodsFacade;
import com.tqmall.legend.facade.magic.vo.GoodsExtVo;
import com.tqmall.magic.object.result.goods.GoodsPaintExtDTO;
import com.tqmall.magic.service.goods.RpcGoodsPaintExtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 油漆资料
 * Created by shulin on 16/11/11.
 */
@Service
@Slf4j
public class BPGoodsFacadeImpl implements BPGoodsFacade {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private com.tqmall.tqmallstall.service.goods.GoodsService rpcGoodsService;

    @Autowired
    private RpcGoodsPaintExtService rpcGoodsPaintExtService;

    @Transactional
    @Override
    public boolean addBPGoodsInfo(GoodsExtVo goodsExtVo) {
        GoodsBo goodsBo = goodsExtVo.getGoodsBo();
        log.info("[钣喷油漆资料] 新增 start. shopId={}", goodsBo.getShopId());
        Result result = goodsService.addWithAttrCar(goodsBo);
        log.info("[钣喷油漆资料] 调用Legend接口新增物料信息,result={}", result.isSuccess());
        if (!result.isSuccess()) {
            return false;
        }
        Goods goods = (Goods) result.getData();
        Long goodsId = goods.getId();
        Long shopId = goodsBo.getShopId();
        GoodsPaintExtDTO goodsPaintExtDTO = BdUtil.bo2do(goodsExtVo, GoodsPaintExtDTO.class);
        goodsPaintExtDTO.setGoodsId(goodsId);
        goodsPaintExtDTO.setShopId(shopId);
        com.tqmall.core.common.entity.Result<String> tmp = rpcGoodsPaintExtService.addGoodsPaintExtInfo(goodsPaintExtDTO);
        if (tmp == null) {
            throw new BizException("新增油漆资料失败！");
        }
        log.info("[钣喷油漆资料] 滴啊用magic接口新增油漆扩展信息，result={}", tmp.isSuccess());
        if (!tmp.isSuccess()) {
            throw new BizException(tmp.getMessage());
        }
        return true;
    }

    @Transactional
    @Override
    public boolean modifyBPGoodsInfo(GoodsExtVo goodsExtVo) {
        GoodsBo goodsBo = goodsExtVo.getGoodsBo();
        log.info("[钣喷油漆资料] 更新 start. goodsId={},shopId={}", goodsExtVo.getGoodsId(), goodsBo.getShopId());
        Result result = goodsService.updateWithAttrCar(goodsBo);
        if (!result.isSuccess()) {
            return false;
        }
        log.info("[钣喷油漆资料] 调用legend接口更新legend库，result={}", result.isSuccess());
        GoodsPaintExtDTO goodsPaintExtDTO = BdUtil.bo2do(goodsExtVo, GoodsPaintExtDTO.class);
        goodsPaintExtDTO.setShopId(goodsBo.getShopId());
        com.tqmall.core.common.entity.Result<Boolean> tmp = rpcGoodsPaintExtService.updateByGoodsId(goodsPaintExtDTO);
        if (tmp == null) {
            throw new BizException("更新油漆资料失败！");
        }
        log.info("[钣喷油漆资料] 调用Magic接口删除magic中的油漆扩展信息，result={}", tmp.isSuccess());
        if (!tmp.isSuccess()) {
            throw new BizException(tmp.getMessage());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean removeBPGoodsInfo(Long id, Long shopId) {
        log.info("[钣喷油漆资料] 删除油漆资料 start. goodsId={},shopId={}", id, shopId);
        Result tmp = goodsService.deleteByIdAndShopId(id, shopId);
        if (!tmp.isSuccess()) {
            return false;
        }
        log.info("[钣喷油漆资料] 调用Legend接口删除legend库中数据,result={}", tmp.isSuccess());
        com.tqmall.core.common.entity.Result<String> result = rpcGoodsPaintExtService.removeGoodsPaintExtInfo(id, shopId);
        log.info("[钣喷油漆资料] 调用magic接口删除magic中的油漆扩展信息，result={}", result.isSuccess());
        if (result == null) {
            throw new BizException("删除油漆资料失败！");
        }
        if (!tmp.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return true;
    }

    @Override
    public Result isBPGoods(Long goodsId) {
        List<Integer> goodsIdList = new ArrayList<Integer>();
        goodsIdList.add(Integer.valueOf(goodsId.toString()));
        List<Integer> typeList = new ArrayList<Integer>();
        typeList.add(1);
        com.tqmall.core.common.entity.Result<List<Integer>> result = null;
        log.info("【调用电商油漆接口】参数：goodsIdList={},返回:result={}", goodsIdList, JSONUtil.object2Json(result));
        try {
            result = rpcGoodsService.filterGoodsByTags(goodsIdList, typeList);
        } catch (Exception e) {
            log.error("[dubbo->档口]判断配件是否为油漆件 异常 配件ID:{} 异常:{}", goodsId, e);
            return Result.wrapErrorResult("", "系统异常");
        }
        if (result.isSuccess()) {
            List<Integer> tmp = result.getData();
            if (!CollectionUtils.isEmpty(tmp)) {
                return Result.wrapSuccessfulResult(true);
            }
        }

        return Result.wrapSuccessfulResult(false);
    }


}
