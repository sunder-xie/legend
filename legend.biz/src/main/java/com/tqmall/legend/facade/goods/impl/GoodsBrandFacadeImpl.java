package com.tqmall.legend.facade.goods.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandParam;
import com.tqmall.itemcenter.object.param.goods.GoodsBrandSearchParam;
import com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO;
import com.tqmall.itemcenter.service.goods.RpcGoodsBrandService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.goods.GoodsBrandFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsy on 16/11/25.
 */
@Slf4j
@Service
public class GoodsBrandFacadeImpl implements GoodsBrandFacade {

    @Autowired
    private RpcGoodsBrandService rpcGoodsBrandService;

    /**
     * 门店配件品牌搜索
     *
     * @param shopId
     * @return
     */
    @Override
    public List<GoodsBrandDTO> getGoodsBrands(Long shopId) {
        return rpcGoodsBrandService.selectByShopId(shopId);
    }

    @Override
    public Result<DefaultPage<GoodsBrandDTO>> getGoodsBrandsFromSearch(GoodsBrandSearchParam goodsBrandSearchParam, Pageable pageable) {
        //
        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        PagingResult<GoodsBrandDTO> pagingResult = rpcGoodsBrandService.getGoodsBrandsFromSearch(goodsBrandSearchParam);
        if (!pagingResult.isSuccess()){
            return Result.wrapSuccessfulResult(new DefaultPage<GoodsBrandDTO>(new ArrayList<GoodsBrandDTO>(),pageable,0));
        }
        int total = pagingResult.getTotal();
        DefaultPage<GoodsBrandDTO> defaultPage = new DefaultPage<GoodsBrandDTO>(pagingResult.getList(), pageRequest, total);
        return Result.wrapSuccessfulResult(defaultPage);
    }

    @Override
    public String save(GoodsBrandParam goodsBrandParam) {
        Result<GoodsBrandDTO> result = rpcGoodsBrandService.saveAndUpdate(goodsBrandParam);
        log.info("配件品牌添加和更新接口返回信息：{}，参数：{}",LogUtils.objectToString(result),LogUtils.objectToString(goodsBrandParam));
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return "配件品牌保存成功";
    }


    @Override
    public String delete(Long shopId, Long id) {
        GoodsBrandParam goodsBrandParam = new GoodsBrandParam();
        goodsBrandParam.setId(id);
        goodsBrandParam.setShopId(shopId);
        Result<String> result = rpcGoodsBrandService.delete(goodsBrandParam);
        log.info("配件品牌删除接口返回信息：{}，参数：{}",LogUtils.objectToString(result),LogUtils.objectToString(goodsBrandParam));
        if (!result.isSuccess()) {
            throw new BizException(result.getMessage());
        }
        return "配件品牌删除成功";
    }

    @Override
    public GoodsBrandDTO addWithoutRepeat(GoodsBrandDTO goodsBrandDto) {
        return rpcGoodsBrandService.addWithoutRepeat(goodsBrandDto);
    }

    @Override
    public Long addDefinedBrand(GoodsBrandDTO goodsBrandDto) {
        return rpcGoodsBrandService.addDefinedBrand(goodsBrandDto);
    }

    @Override
    public GoodsBrandDTO selectById(Long id) {
        return rpcGoodsBrandService.selectById(id);
    }

    @Override
    public List<GoodsBrandDTO> selectByBrandNames(Long shopId, String... brandNames) {
        return rpcGoodsBrandService.selectByBrandNames(shopId, brandNames);
    }

    @Override
    public void batchSave(List<GoodsBrandDTO> goodsBrands) {
        rpcGoodsBrandService.batchSave(goodsBrands);
    }
}
