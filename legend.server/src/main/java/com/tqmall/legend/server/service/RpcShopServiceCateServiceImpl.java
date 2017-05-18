package com.tqmall.legend.server.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.legend.object.result.service.ShopServiceCateDTO;
import com.tqmall.legend.service.service.RpcShopServiceCateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/22.
 */
@Service("rpcShopServiceCateService")
public class RpcShopServiceCateServiceImpl implements RpcShopServiceCateService {
    @Autowired
    private ShopServiceCateService shopServiceCateService;

    /**
     * 根据门店id获取服务类别
     *
     * @param shopId
     * @param nameLike 服务名称模糊查询  可不填
     * @return
     */
    @Override
    public Result<List<ShopServiceCateDTO>> getServiceCateList(final Long shopId, final String nameLike) {
        return new ApiTemplate<List<ShopServiceCateDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<ShopServiceCateDTO> process() throws BizException {
                List<ShopServiceCateDTO> shopServiceCateDTOList = Lists.newArrayList();
                Map<String,Object> searchMap = Maps.newHashMap();
                searchMap.put("shopId", shopId);
                if (StringUtils.isNotBlank(nameLike)) {
                    searchMap.put("nameLike", nameLike);
                }
                List<ShopServiceCate> shopServiceCateList = shopServiceCateService.select(searchMap);
                if(CollectionUtils.isEmpty(shopServiceCateList)){
                    return shopServiceCateDTOList;
                }
                for(ShopServiceCate shopServiceCate : shopServiceCateList){
                    ShopServiceCateDTO shopServiceCateDTO = new ShopServiceCateDTO();
                    BeanUtils.copyProperties(shopServiceCate, shopServiceCateDTO);
                    shopServiceCateDTOList.add(shopServiceCateDTO);
                }
                return shopServiceCateDTOList;
            }
        }.execute();
    }
}
