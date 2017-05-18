package com.tqmall.legend.dao.setting;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lilige on 16/11/2.
 */

@MyBatisRepository
public interface ShopPrintConfigDao extends BaseDao<ShopPrintConfig> {

    /**
     * 根据shopID和模版 获取打印设置纪录
     * @param shopId
     * @param printTemplate
     * @param printType null 表示忽略打印类型
     * @return
     */
    ShopPrintConfig getConfigByPrintTemplate(@Param("shopId") Long shopId , @Param("printTemplate") Integer printTemplate , @Param("printType") Integer printType);

}
