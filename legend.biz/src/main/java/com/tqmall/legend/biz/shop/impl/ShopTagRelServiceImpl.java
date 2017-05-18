package com.tqmall.legend.biz.shop.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.shop.ShopTagRelService;
import com.tqmall.legend.biz.shop.ShopTagService;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.shop.ShopTagDao;
import com.tqmall.legend.dao.shop.ShopTagRelDao;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopTag;
import com.tqmall.legend.entity.shop.ShopTagRel;
import com.tqmall.legend.facade.magic.vo.BPShopTagRelVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/12/14.
 */
@Service
@Slf4j
public class ShopTagRelServiceImpl implements ShopTagRelService {
    @Autowired
    private ShopTagRelDao shopTagRelDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopTagService shopTagService;

    @Override
    public List<ShopTagRel> selectByShopIdAndTagId(Long shopId, Long tagId) {
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("tagId", tagId);
        List<ShopTagRel> shopTagRelList = shopTagRelDao.select(searchMap);
        return shopTagRelList;
    }

    @Override
    public List<BPShopTagRelVo> searchShopTagRel(Long shopId, Long tagId, Long cityId) {
        if (tagId == null) {
            throw new BizException("参数不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("shopId", shopId);
        params.put("tagId", tagId);
        List<BPShopTagRelVo> result = new ArrayList<>();
        List<ShopTagRel> shopTagRelList = shopTagRelDao.select(params);
        if (CollectionUtils.isEmpty(shopTagRelList)) {
            return result;
        }
        Long[] shopIds = getShopIds(shopTagRelList);
        List<Shop> shopList = shopDao.selectByIds(shopIds);
        if (CollectionUtils.isEmpty(shopList)) {
            return result;
        }
        result = packageBPShopTagRelVo(shopTagRelList, shopList);
        if (cityId != null) {
            result = searchByCityId(cityId, result);
        }
        return result;
    }

    @Override
    public List<BPShopTagRelVo> searchBPShopTagRelByName(String shopName) {
        List<BPShopTagRelVo> result = new ArrayList<>();
        ShopTag shopTag = shopTagService.selectByTagCode(Constants.BPSHARE);
        if (shopTag == null) {
            return result;
        }
        result = searchShopTagRel(null, shopTag.getId(), null);
        if (!CollectionUtils.isEmpty(result) && shopName!=null) {
            List<BPShopTagRelVo> newResult = new ArrayList<>();
            //按照名称过滤
            for (BPShopTagRelVo bpShopTagRelVo : result) {
                String tmp = bpShopTagRelVo.getShopName();
                if (tmp != null) {
                    if (tmp.contains(shopName)) {
                        newResult.add(bpShopTagRelVo);
                    }
                }
            }
            return newResult;
        }
        return result;
    }

    /**
     * 取得门店ID列表
     *
     * @param shopTagRels
     * @return
     */
    private Long[] getShopIds(List<ShopTagRel> shopTagRels) {
        if (shopTagRels == null) {
            return null;
        }
        int size = shopTagRels.size();
        Long[] shopIds = new Long[size];
        for (int i = 0; i < size; i++) {
            shopIds[i] = shopTagRels.get(i).getShopId();
        }
        return shopIds;
    }

    /**
     * 组装数据
     *
     * @param shopTagRelList
     * @param shopList
     * @return
     */
    private List<BPShopTagRelVo> packageBPShopTagRelVo(List<ShopTagRel> shopTagRelList,
                                                       List<Shop> shopList) {
        BPShopTagRelVo bpShopTagRelVo = null;
        List<BPShopTagRelVo> result = new ArrayList<>();
        for (ShopTagRel shopTagRel : shopTagRelList) {
            bpShopTagRelVo = new BPShopTagRelVo();
            bpShopTagRelVo.setId(shopTagRel.getId());
            Long shopId = shopTagRel.getShopId();
            bpShopTagRelVo.setShopId(shopId);
            bpShopTagRelVo.setTagId(shopTagRel.getTagId());
            for (Shop shop : shopList) {
                if (shopId.equals(shop.getId())) {
                    bpShopTagRelVo.setShopName(shop.getName());
                    bpShopTagRelVo.setCity(shop.getCity());
                    bpShopTagRelVo.setCityName(shop.getCityName());
                }
            }
            result.add(bpShopTagRelVo);
        }
        return result;
    }

    /**
     * 通过cityId过滤数据
     *
     * @param cityId
     * @param bpShopTagRelVos
     */
    private List<BPShopTagRelVo> searchByCityId(Long cityId, List<BPShopTagRelVo> bpShopTagRelVos) {
        List<BPShopTagRelVo> result = new ArrayList<>();
        for (BPShopTagRelVo bpShopTagRelVo : bpShopTagRelVos) {
            if (cityId.equals(bpShopTagRelVo.getCity())) {
                result.add(bpShopTagRelVo);
            }
        }
        return result;
    }


}
