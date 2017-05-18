package com.tqmall.legend.cache;

/**
 * Created by lixiao on 16/6/1.
 */
public interface SnCache {

    /**
     * 生成增加SN
     * @param key
     * @param shopId
     * @param snPrefix
     * @return
     */
    public String getNextOrderSnIncrement(String key, Long shopId, String snPrefix);

}
