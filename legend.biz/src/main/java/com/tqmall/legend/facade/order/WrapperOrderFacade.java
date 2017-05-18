package com.tqmall.legend.facade.order;

import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.legend.biz.order.vo.OrderGoodsVo;
import com.tqmall.legend.biz.order.vo.OrderServicesVo;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.order.OrderGoods;
import com.tqmall.legend.entity.order.OrderInfo;
import com.tqmall.legend.entity.order.OrderServices;
import com.tqmall.legend.facade.order.vo.OrderInfoVo;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by lixiao on 16/4/15.
 */
public interface WrapperOrderFacade {

    /**
     * wrapper model of edited page
     *
     * @param orderId
     * @param model
     * @param shopId
     */
    public OrderInfo wrapperModelOfEditedPage(Long orderId,Model model, Long shopId) throws BusinessCheckedException;

    /**
     * 钣喷中心预检信息
     *
     * @param orderId
     * @param model
     */
    void wrapperModelOfEditedPageAboutPrecheck(Long orderId,Model model);


    /**
     * 批量关联维修工名称
     *
     * @param orderServicesList 从数据库查询出的服务列表
     */
    public List<OrderServicesVo> orderServiceListReferWorderName(List<OrderServices> orderServicesList);

    /**
     * 批量关联销售员
     *
     * @param orderGoodsList
     * @return
     */
    public List<OrderGoodsVo> orderGoodsListReferSaleName(List<OrderGoods> orderGoodsList);

    /**
     * wrapper customercar
     *
     * @param orderInfo 工单实体
     * @return
     */
    CustomerCar wrapperCustomerCar(OrderInfo orderInfo);

    /**
     * wrapper orderInfo
     * @param orderInfo
     * @param customerCar
     * @param customer
     * @return
     */
    public OrderInfo wrapperOrderInfo(OrderInfo orderInfo, CustomerCar customerCar, Customer customer);

    /**
     * 组织工单信息：服务项目、配件项目
     *
     * @return
     */
    List<OrderInfoVo> wrapperOrderInfoVo(Long shopId, List<OrderInfoVo> orderInfoVos);
}
