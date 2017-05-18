package com.tqmall.legend.biz.shop;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.ShopServiceTag;
import com.tqmall.legend.entity.shop.ShopServiceTagRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/8/24.
 */
public interface ShopServiceTagRelService {

    public List<ShopServiceTagRel> select(Map<String, Object> searchMap);

    /**
     * 组装服务标签
     * @param searchMap
     * @return
     */
    public Map<Integer,List<ShopServiceTag>> getServiceFlag(Map<String, Object> searchMap);

    /**
     * 添加记录
     * @param shopServiceTagRel
     * @return
     */
    public Result add(ShopServiceTagRel shopServiceTagRel);

    /**
     * 更新记录，不更新null的字段
     * @param shopServiceTagRel
     * @return
     */
    public Integer update(ShopServiceTagRel shopServiceTagRel);

    /**
     * 根据ID软删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id);

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    public ShopServiceTagRel selectById(Long id);

    /**
     * 分页数据获取
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<ShopServiceTagRel> getPage(Pageable pageable, Map<String, Object> searchParams);
}
