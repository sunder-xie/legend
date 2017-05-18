package com.tqmall.legend.biz.marketing.activity;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.marketing.activity.CzActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by jason on 15/11/10.
 */
public interface CzActivityService {

    /**
     * 车主,门店活动信息
     *
     * @param param 入参
     * @return
     */
    public List<CzActivity> select(Map param);

    /**
     * 删除车主,门店活动信息
     *
     * @param czActivity 入参
     * @return
     */
    public Result deleteAct(CzActivity czActivity);

    /**
     * 根据id获取活动对象
     *
     * @param id 入参
     * @return
     */
    public CzActivity getById(Long id);

    /**
     * 组装门店信息
     *
     * @param param 入参
     * @return
     */
    public CzActivity wrapActivity(Map param);

    /**
     * 保存车主,门店活动信息
     *
     * @param activity
     * @return
     */
    public Result save(CzActivity activity);

    /**
     * 更新车主,门店活动信息
     *
     * @param activity
     * @param flag 有3个地方调用 区分作用
     * @return
     */
    public Result update(CzActivity activity, Integer flag);

    /**
     * 更新车主,门店活动信息状态
     *
     * @param activity
     * @return
     */
    public Result updateActStatus(CzActivity activity);


    /**
     * 车主,门店活动信息列表分页数据
     *
     * @param pageable
     * @param searchParams
     * @return
     */
    public Page<CzActivity> getPage(Pageable pageable, Map<String, Object> searchParams);


}
