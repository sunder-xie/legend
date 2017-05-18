package com.tqmall.legend.biz.shop;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.ServiceTemplateCateRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/8/24.
 */
public interface ServiceTemplateCateRelService {

    public List<ServiceTemplateCateRel> select(Map<String, Object> searchMap);

    /**
     * 添加记录
     * @param serviceTemplateCateRel
     * @return
     */
    public Result add(ServiceTemplateCateRel serviceTemplateCateRel);

    /**
     * 更新记录，不更新null的字段
     * @param serviceTemplateCateRel
     * @return
     */
    public Integer update(ServiceTemplateCateRel serviceTemplateCateRel);

    /**
     * 根据ID软删除
     * @param id
     * @return
     */
    public Integer deleteById(Long id);

    /**
     * 根据ID获取
     * @param id
     * @return
     */
    public ServiceTemplateCateRel selectById(Long id);

    /**
     * 分页数据获取
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<ServiceTemplateCateRel> getPage(Pageable pageable, Map<String, Object> searchParams);

    /**
     * 获取模板服务的关联类别信息
     *
     * @return 以模板id，类别形式返回
     */
    Map<Long, List<ServiceTemplateCateRel>> getServiceTemplateCateRel(Map<String, Object> searchMap);
}
