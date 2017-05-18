package com.tqmall.legend.service.shop;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.shop.ApplyRecordSearchParam;
import com.tqmall.legend.object.param.shop.ShopWxpayConfigSaveParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.shop.ShopApplyRecordDTO;
import com.tqmall.legend.object.result.shop.ShopWxpayConfigDTO;

/**
 * Created by feilong.li on 16/10/17.
 */
public interface RpcShopApplyRecordService {
    /**
     * 获取门店支付申请数量
     * @return
     */
    public Result<Integer> getShopApplyRecordCount();

    /**
     * 获取门店支付申请列表
     * @param applyRecordSearchParam
     * @return
     */
    public Result<PageEntityDTO<ShopApplyRecordDTO>> getShopApplyRecordList(ApplyRecordSearchParam applyRecordSearchParam);

    /**
     * 获取门店支付申请配置详情
     * @param ucShopId  UC用户id
     * @param applyRecordId 申请记录id
     * @return
     */
    public Result<ShopWxpayConfigDTO> getShopWxpayConfigDetail(Long ucShopId, Long applyRecordId);

    /**
     * 保存门店支付配置
     * @param shopWxpayConfigSaveParam
     * @return
     */
    public Result<String> saveShopWxpayConfig(ShopWxpayConfigSaveParam shopWxpayConfigSaveParam);

    /**
     * 查询门店支付申请状态是否已"测试通过"
     * @param ucShopId
     * @return
     */
    public Result<Boolean> getShopApplyStatusIsSuccess(Long ucShopId);
}
