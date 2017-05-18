package com.tqmall.legend.biz.shop;

import java.util.Map;

public interface SerialNumberService{
    /**
     * 获取编号
     * @param map
     * @return
     */
    public String getSerialNumber(Map map);

    /**
     * 从redis获取对应类型的编号
     * @param map
     * @return
     */
    public Long getSerialNumberFromRedis(Map map);

    /**
     * 支持批量的获取编号方法
     * 批量调用完后调用syncRedis方法
     * @param map
     * @return
     */
    public String getSerialNumbers(Map map);

    /**
     * 通过管道同步内存中的数据到Redis中
     */
    public void syncRedis();

}
