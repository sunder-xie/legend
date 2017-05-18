package com.tqmall.legend.biz.article.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.article.IProductAlertService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.article.ProductAlertDao;
import com.tqmall.legend.entity.article.ProductAlert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * legend首页-产品公告 服务实现类
 */
@Slf4j
@Service
public class ProductAlertServiceImpl extends BaseServiceImpl implements IProductAlertService {

    @Autowired
    ProductAlertDao productAlertDao;


    @Override
    public Page<ProductAlert> getPage(Pageable pageable, Map<String, Object> paramMap) {
        paramMap.put("isPublished", "1");
        return super.getPage(productAlertDao, pageable, paramMap);
    }


    @Override
    public List<ProductAlert> getTop3() {
        // 首页产品咨询 TOP3
        List<ProductAlert> topProductAlerts = productAlertDao.getTopList();
        if (CollectionUtils.isEmpty(topProductAlerts)) {
            topProductAlerts = new ArrayList<ProductAlert>();
        }
        return topProductAlerts;
    }

    @Override
    public Optional<ProductAlert> get(@NotNull Long itemId) {
        return Optional.fromNullable(productAlertDao.selectById(itemId));
    }
}
