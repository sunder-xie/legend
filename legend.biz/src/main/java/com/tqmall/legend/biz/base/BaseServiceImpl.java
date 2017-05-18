package com.tqmall.legend.biz.base;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.legend.entity.base.IdEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

public abstract class BaseServiceImpl implements BaseService {
    @Override
    public <T extends IdEntity> Page<T> getPage(BaseDao<T> baseDao, Pageable pageable,
        Map<String, Object> searchParams) {
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            searchParams.put("sorts", sorts);
        }
        Integer totalSize = baseDao.selectCount(searchParams);
        PageRequest pageRequest =
            new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<T> data = baseDao.select(searchParams);
        DefaultPage<T> page = new DefaultPage<T>(data, pageRequest, totalSize);
        return page;
    }


    @Override
    public <T extends IdEntity> List<T> getAll(BaseDao<T> baseDao) {
        return baseDao.select(null);
    }

    @Override
    public <T extends IdEntity> T getById(BaseDao<T> baseDao, Object id) {
        return baseDao.selectById(id);
    }

    @Transactional
    @Override
    public <T extends IdEntity> boolean save(BaseDao<T> baseDao, T entity) {
        if(entity instanceof BaseEntity){
            ((BaseEntity) entity).setDefaultBizValue();
        }
        if (entity.getId() == null) {
            return baseDao.insert(entity) > 0;
        } else {
            return baseDao.updateById(entity) > 0;
        }
    }

    @Transactional
    @Override
    public <T extends IdEntity> boolean deleteById(BaseDao<T> baseDao, Object id) {
        T t = baseDao.selectById(id);
        if (t == null)
            return false;

        return baseDao.deleteById(id) > 0;
    }



    @Override
    public <T extends IdEntity> int deleteByIds(BaseDao<T> baseDao, Object[] ids) {
        if (ids.length == 0) {
            return 0;
        }
        return baseDao.deleteByIds(ids);
    }


    public <T extends IdEntity> List<T> select(BaseDao<T> baseDao,Map<String, Object> searchParams) {
        return baseDao.select(searchParams);
    }


    public <T extends IdEntity> int batchInsert(BaseDao<T> baseDao , List<T> list , Integer MAX_SIZE ) {
        if(MAX_SIZE == null){
            MAX_SIZE = 300 ;
        }
        if(CollectionUtils.isEmpty(list)){
            return 0;
        }
        int totalSize = list.size();
        int size = totalSize % MAX_SIZE == 0 ? totalSize / MAX_SIZE : totalSize / MAX_SIZE + 1;
        for (int i = 0; i < size; i++) {
            if (i + 1 == size) {
                baseDao.batchInsert(list.subList(i * MAX_SIZE, totalSize));
            } else {
                baseDao.batchInsert(list.subList(i * MAX_SIZE, i * MAX_SIZE + MAX_SIZE ));
            }
        }
        return totalSize;
    }

    public <T extends IdEntity> T selectByIdAndShopId(BaseDao<T> baseDao, Long id, Long shopId) {
        Map map = new HashMap();
        if(id==null || shopId == null){
            return null;
        }
        map.put("shopId",shopId);
        map.put("id",id);
        List<T> list = baseDao.select(map);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }else{
            return null;
        }
    }

    public <T extends IdEntity> List<T>  selectByIds(BaseDao<T> baseDao,List<Long> ids){
        int size = ids.size();
        Long[] idsArr = (Long[])ids.toArray(new Long[size]);
        return baseDao.selectByIds(idsArr);
    }

}
