package com.tqmall.legend.service.activity;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.activity.ShopActivityParam;
import com.tqmall.legend.object.result.activity.ActivityDTO;
import com.tqmall.legend.object.result.activity.ActivityTemplateDTO;
import com.tqmall.legend.object.result.activity.ShopActivityDTO;
import com.tqmall.legend.object.result.activity.ShopActivityPageDTO;

import java.util.List;

/**
 * Created by wushuai on 16/7/28.
 */
public interface RpcShopActivityService {
    /**
     * 分页查询门店活动实例
     * @param param
     * @return
     */
    public Result<ShopActivityPageDTO> getShopActivityPage(ShopActivityParam param);

    /**
     * 查询门店活动实体详情(包括活动实体包含的服务实体)
     * @param param
     * @return
     */
    public Result<ShopActivityDTO> getShopActivity(ShopActivityParam param);

    /**
     * 根据活动id查询活动模版
     * @param actId
     * @return
     */
    public Result<ActivityTemplateDTO> getActTplByActId(Long actId);

    /**
     * 获取当前有效活动
     * @return
     */
    public Result<List<ActivityDTO>> gainOnlineActivity();

    /**
     *获取活动
     * @return
     */
    public Result<ActivityDTO> gainActivity(Long activityId);
}
