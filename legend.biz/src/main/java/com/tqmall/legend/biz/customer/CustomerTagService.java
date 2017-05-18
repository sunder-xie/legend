package com.tqmall.legend.biz.customer;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerTag;

import java.util.List;

/**
 * Created by zsy on 16/4/12.
 */
public interface CustomerTagService {

    /**
     * 获取客户标签
     *
     * @param shopId
     * @param carId
     * @return
     */
    public List<CustomerTag> getCustomerTagByCarId(Long shopId,Long carId);

    /**
     * 获取客户标签（不包含未添加的通用自定义标签）
     *
     * @param shopId
     * @param carId
     * @return
     */
    public List<CustomerTag> getNoCustomCarTagByCarId(Long shopId,Long carId);


    /**
     * 查询标签
     * @param id
     * @return
     */
    public CustomerTag selectById(Long id);

    /**
     * 更新
     * @param customerTag
     * @return
     */
    public Result updateById(CustomerTag customerTag);

    /**
     * 删除
     * @param id
     * @return
     */
    public Result deleteById(Long id);

    /**
     * 添加
     * @param customerTag
     * @return
     */
    public Result insert(CustomerTag customerTag);

    /**
     * 获取门店所有标签
     * @param shopId
     * @return
     */
    List<String> listTag(Long shopId);

    /**
     * 查询门店所有自定义标签
     * @param shopId
     * @return
     */
    List<String> listCustomizeTag(Long shopId);
}
