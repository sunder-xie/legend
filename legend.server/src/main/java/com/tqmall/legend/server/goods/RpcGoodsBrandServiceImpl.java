package com.tqmall.legend.server.goods;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.goods.GoodsBrandFacade;
import com.tqmall.legend.object.result.goods.GoodsBrandDTO;
import com.tqmall.legend.service.goods.RpcGoodsBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * TODO 2017-02-06 legend提供app的dubbo接口迁移到itemcenter
 *
 * Created by zsy on 16/11/25.
 */
@Service("rpcGoodsBrandService")
public class RpcGoodsBrandServiceImpl implements RpcGoodsBrandService {
    @Autowired
    private GoodsBrandFacade goodsBrandFacade;

    @Override
    public Result<List<GoodsBrandDTO>> getGoodsBrand(final Long shopId) {
        return new ApiTemplate<List<GoodsBrandDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空");
            }

            @Override
            protected List<GoodsBrandDTO> process() throws BizException {
                List<com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO> goodsBrandList = goodsBrandFacade.getGoodsBrands(shopId);
                List<GoodsBrandDTO> goodsBrandDTOList = Lists.newArrayList();
                if(CollectionUtils.isEmpty(goodsBrandList)){
                    return goodsBrandDTOList;
                }
                for(com.tqmall.itemcenter.object.result.goods.GoodsBrandDTO goodsBrand : goodsBrandList){
                    GoodsBrandDTO goodsBrandDTO = new GoodsBrandDTO();
                    BeanUtils.copyProperties(goodsBrand,goodsBrandDTO);
                    goodsBrandDTOList.add(goodsBrandDTO);
                }
                return goodsBrandDTOList;
            }
        }.execute();
    }
}
