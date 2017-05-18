package com.tqmall.legend.facade.sys;

import com.tqmall.legend.entity.sys.UserOperateDict;

/**
 * Created by zsy on 17/5/9.
 * 用户行为分析：用户操作字典表
 */
public interface UserOperateDictFacade {
    /**
     * 通过refer获取用户操作数据
     *
     * @param refer
     * @return
     */
    UserOperateDict getByRefer(String refer);

    void delKey();
}
