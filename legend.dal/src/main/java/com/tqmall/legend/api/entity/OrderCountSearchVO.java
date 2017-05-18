package com.tqmall.legend.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/3/22.
 */
@Data
public class OrderCountSearchVO implements Serializable{
    private static final long serialVersionUID = -1659401054915371612L;
    private Integer symbol;  //返回数标记

    private Integer pageSize;
    private Integer pageNum;
    private String sortBy;   //单字段 like:'gmtModified'  默认id
    private String sortType; // 排序类型 asc/desc 默认desc

    //准确查找
    private String mobileLike;     //电话号(车主(customer_mobile),联系人(contact_mobile))
    private Long receiver;         //服务顾问id
    private Integer orderType;     //业务类型
    private Integer[] orderTag;    //工单类型  1为普通工单，2为洗车工单，3为快修快保单，4为保险维修单
    private String company;        //单位
    private Long shopId;           //店铺id
    private Integer isVisit;       //是否回访过

    //范围查找
    private String startTime;      //开单开始时间 like:"2016-03-12 00:00:00"
    private String endTime;        //开单结束时间 like:"2016-03-12 23:59:59"
    private String payStartTime;   //结算开始时间 like:"2016-03-12 00:00:00"
    private String payEndTime;     //结算结束时间 like:"2016-03-12 23:59:59"
    private Long[] customerIds;    //车主列表范围查找
    private String customerCarId; //车辆范围查找 以“,”分割
    private String orderStatus;  //工单状态 以","分割  CJDD 创建订单，DDBJ订单报价，FPDD 分配订单，DDSG 订单施工，DDWC 订单完成，DDYFK订单已付款，WXDD无效订单
    private Integer[] payStatus;   //结算状态  0为未支付，2为已支付，1为挂账
    private Integer[] proxyType;   //委托状态：0：无1：委托2：受委托

    //like模糊查找
    private String carLicenseLike; //车牌号
    private String customerNameLike; //车主
    private String contactNameLike; //联系人
    private String carLike;        //车型
    private String orderSnLike;    //订单号
    private String vin;            //车架号
    private String likeKeyWords;   //模糊查找 查找车牌号、手机号(customer_mobile)、工单号、customer_name
    private String contactLikeKeyWords;//模糊查询  查找车牌号、手机号(contact_mobile)、工单号、contact_name
}
