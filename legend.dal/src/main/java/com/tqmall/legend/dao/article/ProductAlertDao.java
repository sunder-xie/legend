package com.tqmall.legend.dao.article;


import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.article.ProductAlert;

import java.util.List;

@MyBatisRepository
public interface ProductAlertDao extends BaseDao<ProductAlert> {

    /**
     * 产品咨询TOP3
     *
     * @return List<ProductAlert>
     */
    List<ProductAlert> getTopList();

}
