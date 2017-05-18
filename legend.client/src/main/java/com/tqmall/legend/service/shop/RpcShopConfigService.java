package com.tqmall.legend.service.shop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.note.ShopConfigParam;
import com.tqmall.legend.object.result.config.ShopConfigDTO;

import java.util.List;

/**
 * Created by twg on 16/4/11.
 * 门店配置
 */
public interface RpcShopConfigService {

    /**
     * 获取配置内容
     * @param shopId 门店id
     * @param confType 配置类型
     * @param confKey 配置键值
     * @return
     */
    Result getConfValue(Long shopId,Integer confType,String confKey);

    /**
     * 获取配置内容
     * @param shopId 门店id
     * @param confType 配置类型
     * @return
     */
    Result getConfValue(Long shopId,Integer confType);

    /**
     * 门店配置内容保存和更新
     * @param shopId 门店id
     * @param confType 配置类型
     * @param noteConfigParam 实体
     * @return
     */
    Result saveOrUpdateValue(Long shopId,Integer confType,ShopConfigParam noteConfigParam);

    /**
     * 根据门店id、配置类型配置信息列表
     * @param userGlobalId 门店userGlobalId
     * @param confType 配置类型，
     *                 0为工单打印配置，1为结算打印配置，2为预约提醒设置，3为回访提醒设置，4为保险到期提醒设置，
     *                 5为年检到期提醒设置，6为保养到期提醒设置，7为生日到期提醒设置，8为新流失客户到期提醒设置，
     *                 9上下班时间设置，10报表列显示，11门店页面样式配置，12门店新老版本切换，13报表显示字段，
     *                 14消息推送设置，15门店安全登录登记设置，16支付方式设置
     * @return
     */
    Result<List<ShopConfigDTO>> getShopConfigByShopIdAndConfType(Long userGlobalId,Integer confType);

}
