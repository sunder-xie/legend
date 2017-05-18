package com.tqmall.legend.biz.customer;

import com.tqmall.legend.entity.customer.CustomerCarFile;

import java.util.List;
import java.util.Map;

public interface CustomerCarFileService {
    /**
     * 添加客户车辆文件
     *
     * @param file,shopId
     * @return
     */
    public Integer add(CustomerCarFile file);

    /**
     * 修改/删除客户车辆文件
     *
     * @param file,shopId
     * @return
     */
    public Integer update(CustomerCarFile file);

    /**
     * 批量查询客户车辆文件
     *
     * @param map
     * @return
     */
    public List<CustomerCarFile> select(Map<String, Object> map);


}
