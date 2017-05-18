package com.tqmall.legend.dao.privilege;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.privilege.Func;
import com.tqmall.legend.entity.privilege.FuncF;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by QXD on 2014/10/29.
 */
@MyBatisRepository
public interface FuncDao extends BaseDao<Func> {
    /**
     * 查询全部功能列表
     *
     * @return
     */
    List<FuncF> selectAll(@Param("shopLevel") Integer shopLevel);

}
