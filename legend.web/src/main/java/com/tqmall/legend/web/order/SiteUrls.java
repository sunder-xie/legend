package com.tqmall.legend.web.order;

/**
 * 工单相关URL
 * <p/>
 * 工单Controller
 *
 * @see OrderController
 */
public class SiteUrls {

    // [[ 基础工单

    // 进入工单新增
    public static final String ADD = "common-add";
    // 保存工单
    public static final String SAVE = "common-save";
    // 进入工单编辑
    public static final String EDIT = "common-edit";
    // 更新工单
    public static final String UPDATE = "common-update";
    // 工单详情
    public static final String DETAIL = "common-detail";

    // [[ 虚拟工单
    // 保存虚拟工单
    public static final String VIRTUALSAVE = "virtual/save";
    // 进入虚拟工单新建/编辑
    public static final String VIRTUALEDIT = "virtualorder-edit";
    // 更新虚拟工单
    public static final String VIRTUALUPDATE = "virtual/update";
    // 删除虚拟工单
    public static final String VIRTUALDELETE = "virtual/delete";
    // ]]


    // [[ 洗车单
    // 洗车单入口
    public static final String CARWASH = "carwash";
    // 创建洗车单
    public static final String CARWASHSAVE = "carwash/save";
    // 洗车单完善客户信息
    public static final String CARWASHPERFECT = "carwash-perfect";
    // 完善客户信息保存
    public static final String CARWASHPERFECTSAVE = "carwashperfect/save";
    // 结算中洗车单详情页
    public static final String CARWASHDETAIL = "carwash-detail";
    // ]]

    // [[ 快修快保养
    // 快速工单入口
    public static final String SPEEDILY = "speedily";
    // 快修快保详情页
    public static final String SPEEDILYDETAIL = "speedily-detail";
    // 创建快速工单
    public static final String SPEEDILYSAVE = "speedily/save";
    // 更新快速工单
    public static final String SPEEDILYUPDATE = "speedily/update";

    // ]]


    // [[ 物料销售单
    // 入口
    public static final String SELLGOOD = "sell-good";
    // 详情
    public static final String SELLGOODDETAIL = "sell-good-detail";
    // ]]
}
