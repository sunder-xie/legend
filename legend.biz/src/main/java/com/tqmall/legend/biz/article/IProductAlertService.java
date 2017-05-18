package com.tqmall.legend.biz.article;

import com.google.common.base.Optional;
import com.tqmall.legend.entity.article.ProductAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * legend首页-产品公告 服务接口
 */
public interface IProductAlertService {


    /**
     * 分页查询云修产品咨询
     *
     * @param pageable 分页参数
     * @param paramMap
     * @return Page<ProductAlert>
     */
    Page<ProductAlert> getPage(Pageable pageable, Map<String, Object> paramMap);

    /**
     * 获取首页TOP3
     *
     * @return List<ProductAlert>
     */
    List<ProductAlert> getTop3();

    /**
     * 获取特定资讯广告
     *
     * @param itemId 主键ID
     * @return
     */
    Optional<ProductAlert> get(Long itemId);
}
