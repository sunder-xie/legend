package com.tqmall.legend.biz.shop;

import com.tqmall.legend.entity.shop.ShopServiceInfoRel;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 2015/8/5.
 */
public interface ShopServiceInfoRelService {

    /**
     * 查询
     * @param searchParams
     * @return
     */
    public List<ShopServiceInfoRel> select(Map<String, Object> searchParams);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public ShopServiceInfoRel selectById(Long id);

}
