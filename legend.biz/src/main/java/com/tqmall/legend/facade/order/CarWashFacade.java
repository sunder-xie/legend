package com.tqmall.legend.facade.order;

import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.biz.order.bo.CarwashOrderFormBo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.customer.CustomerPerfectOfCarWashEntity;
import com.tqmall.legend.entity.order.CarwashOrderFormEntity;
import com.tqmall.legend.entity.order.CustomerCompletionFormEntity;
import com.tqmall.legend.entity.order.OrderInfo;

/**
 * Created by lixiao on 16/4/7.
 */
public interface CarWashFacade {

    /**
     * 完善洗车单客户信息
     *
     * @param customerCompletionEntity 完善客户信息表单实体
     * @param userInfo                 当前操作人
     * @return
     */
    void perfectCustomer(CustomerCompletionFormEntity customerCompletionEntity, UserInfo userInfo) throws BusinessCheckedException;

    /**
     * 删除洗车单图片
     *
     * @param orderId 工单ID
     * @return {"true":成功;"false":失败}
     */
    boolean deletePictureOfCarWash(Long orderId);

    /**
     * 包装完善洗车单表单实体
     *
     * @param orderInfo             工单实体
     * @param customerPerfectEntity 客户车辆信息
     * @return
     */
    CustomerCompletionFormEntity wrapperPerfectCarWashFormEntity(OrderInfo orderInfo, CustomerPerfectOfCarWashEntity customerPerfectEntity);


    /**
     * [新版] WEB页面 创建洗车单入口
     * <p/>
     * <p/>
     * [确认结算]
     * 1. 账单确认
     * 2. 记录收款流水
     * [挂账]
     * 1. 账单确认
     *
     * @param orderFormEntity 洗车工单表单实体
     * @param userInfo        当前操作人
     * @return
     */
    Result create(CarwashOrderFormEntity orderFormEntity, UserInfo userInfo);

    /**
     * TODO 合并 [web]和[APP]入口,为统一入口
     * <p/>
     * [新版] APP页面 创建洗车单入口
     * <p/>
     * [确认结算]
     * 1. 账单确认
     * 2. 记录收款流水
     * [挂账]
     * 1. 账单确认
     *
     * @param userInfo        当前操作人
     * @return
     */
    Result createForApp(CarwashOrderFormBo orderFormBo, UserInfo userInfo);
}
