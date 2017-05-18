package com.tqmall.legend.biz.base;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.entity.base.IdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BaseService {

    public <T extends IdEntity> List<T> getAll(BaseDao<T> baseDao);

    public <T extends IdEntity> Page<T> getPage(BaseDao<T> baseDao, Pageable pageable,
        Map<String, Object> parameters);

    public <T extends IdEntity> T getById(BaseDao<T> baseDao, Object id);

    public <T extends IdEntity> boolean save(BaseDao<T> baseDao,T entity);

    public <T extends IdEntity> boolean deleteById(BaseDao<T> baseDao, Object id);
    public <T extends IdEntity> int deleteByIds(BaseDao<T> baseDao, Object[] ids);



    public <T extends IdEntity> List<T> select(BaseDao<T> baseDao, Map<String, Object> parameters);

    public <T extends IdEntity> int batchInsert(BaseDao<T> baseDao, List<T> list, Integer maxSize);

    public <T extends IdEntity> T selectByIdAndShopId(BaseDao<T> baseDao, Long id, Long shopId);

    public <T extends IdEntity> List<T> selectByIds(BaseDao<T> baseDao, List<Long> ids);

}
