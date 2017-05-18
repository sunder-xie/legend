package com.tqmall.legend.biz.sys;

import com.tqmall.legend.biz.sys.bo.UserOperateDictSearchBO;
import com.tqmall.legend.entity.sys.UserOperateDict;

import java.util.List;

/**
 * Created by zsy on 17/5/9.
 */
public interface UserOperateDictService {
    /**
     * 查询
     * @param userOperateDictSearchBO
     * @return
     */
    List<UserOperateDict> select(UserOperateDictSearchBO userOperateDictSearchBO);
}
