package com.tqmall.legend.dao.prechecks;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.precheck.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface PrechecksDao extends BaseDao<Prechecks> {
    List<PrecheckItemValueVO> getPrecheckItemValues();

    List<Prechecks> getPrecheckOrder(Map<String, Object> params);

    List<Prechecks> selectPrechecks(Map<String, Object> parameters);

    List<Prechecks> getRemindPrechecks(Map<String, Object> params);

    Long updatePrecheckComment(Map<String, Object> parameters);

    /**
     * 查询预检车辆数,以车为维度
     * @param params
     * @return
     */
    public Integer countPrecheckCar(Map<String, Object> params);
}
