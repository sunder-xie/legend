/**
 * Created by sky on 2017/2/23.
 */

$(function () {
    var $doc = $(document);
    /*
     * 权限配置
     * 注:  1. permission 中的[]之间为或运算连接，{}之间为与运算连接
     *      2. permission 控制是否展示当前标签
     *      3. remind 规则是由下级的permission中抽离出来的公共规则，与下级permission属于与关联
     */
    var menus = {
        reception: {
            type: 'vert',
            name: '接车维修',
            list: [
                {
                    name: '客户预约',
                    list: [
                        {
                            name: '预约单查询',
                            url: '/shop/appoint/appoint-list'
                        },
                        {
                            name: '新建预约单',
                            url: '/shop/appoint/appoint-edit'
                        }
                    ]
                },
                {
                    name: '车辆接待',
                    list: [
                        {
                            name: '车辆查询',
                            url: '/shop/customer/list'
                        },
                        {
                            name: '新建车辆',
                            url: '/shop/customer/edit'
                        },
                        {
                            name: '新建预检单',
                            url: '/shop/precheck/precheck'
                        }
                    ]
                },
                {
                    name: '维修开单',
                    list: [
                        {
                            name: '工单查询',
                            url: '/shop/order/order-list'
                        },
                        {
                            name: '受托单查询',
                            url: '/proxy/proxyList',
                            permission: [
                                [
                                    {
                                        prop: 'BPSHARE',
                                        opt: '==',
                                        res: 'true'
                                    }
                                ],
                                [
                                    {
                                        prop: 'SESSION_SHOP_JOIN_STATUS',
                                        opt: '==',
                                        res: 1
                                    }
                                ]
                            ]
                        },
                        {
                            name: '新建洗车单',
                            url: '/shop/order/carwash',
                            permission: [
                                [
                                    {
                                        prop: 'BPSHARE',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '新建快修快保单',
                            url: '/shop/order/speedily',
                            permission: [
                                [
                                    {
                                        prop: 'BPSHARE',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '新建综合维修单',
                            url: '/shop/order/common-add',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_IS_TQMALL_VERSION',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '新建销售单',
                            url: '/shop/order/sell-good',
                            permission: [
                                [
                                    {
                                        prop: 'BPSHARE',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '导入工单查询',
                            url: '/shop/order/history/history-list'
                        }
                    ]
                },
                {
                    name: '车间管理',
                    remind: [
                        [
                            {
                                prop: 'SESSION_SHOP_WORKSHOP_STATUS',
                                opt: '==',
                                res: 1
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '施工单查询',
                            url: '/workshop/workOrder/toWorkOrderList'
                        },
                        {
                            name: '施工作业扫描',
                            url: '/workshop/process/scanpage'
                        },
                        {
                            name: '中断管理',
                            url: '/workshop/workOrder/toWorkOrderBreakList'
                        },
                        {
                            name: '看板查询',
                            url: '/workshop/loadplate/loadplate-show'
                        }
                    ]
                }
            ]
        },
        /* 接车维修 end */
        warehouse: {
            type: 'vert',
            name: '仓库',
            list: [
                {
                    name: '油漆管理',
                    remind: [
                        [
                            {
                                prop: 'BPSHARE',
                                opt: '==',
                                res: 'true'
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '油漆库存',
                            url: '/goods/paintExt/toPaintStock'
                        },
                        {
                            name: '油漆盘点',
                            url: '/paint/inventory/toInventoryPaintList'
                        },
                        {
                            name: '使用记录',
                            url: '/shop/paint/record/toPaintUseRecordList'
                        }
                    ]
                },
                {
                    name: '库存管理',
                    list: [
                        {
                            name: '库存查询',
                            url: '/shop/warehouse/stock/stock-query'
                        },
                        {
                            name: '库存盘点',
                            url: '/shop/warehouse/stock/stock-inventory'
                        },
                        {
                            name: '库存预警',
                            url: '/shop/warehouse/stock/stock-warning'
                        }
                    ]
                },
                {
                    name: '出库管理',
                    list: [
                        {
                            name: '出库单查询',
                            url: '/shop/warehouse/out/out-list'

                        },
                        {
                            name: '工单报价',
                            url: '/shop/warehouse/out/order-quote-list',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_IS_TQMALL_VERSION',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '工单出库',
                            url: '/shop/warehouse/out/order-out-list',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_IS_TQMALL_VERSION',
                                        opt: '==',
                                        res: 'false'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '其他出库',
                            url: '/shop/warehouse/out/out-other'
                        }
                    ]
                },
                {
                    name: '入库管理',
                    list: [
                        {
                            name: '入库单查询',
                            url: '/shop/warehouse/in/in-list'
                        },
                        {
                            name: '采购入库',
                            url: '/shop/warehouse/in/in-edit/blue'
                        }
                    ]
                },
                {
                    name: '库存配件买卖',
                    remind: [
                        [
                            {
                                prop: 'SESSION_USER_IS_ADMIN',
                                opt: '==',
                                res: 1
                            }
                        ],
                        [
                            {
                                prop: 'SESSION_WAREHOUSE_SHARE_ROLE',
                                opt: '==',
                                res: 'true'
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '出售库存配件',
                            url: '/shop/warehouse/share/sale'
                        },
                        {
                            name: '购买库存配件',
                            url: '/shop/warehouse/share/buy'
                        }
                    ]
                }
            ]
        },
        /* 仓库 end */
        settlement: {
            type: 'vert',
            name: '财务',
            list: [
                {
                    name: '收款',
                    list: [
                        {
                            name: '工单收款',
                            url: '/shop/settlement/debit/order-list'
                        },
                        {
                            name: '引流活动对账单',
                            url: '/shop/settlement/activity',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_IS_TQMALL_VERSION',
                                        opt: '==',
                                        res: 'false'
                                    },
                                    {
                                        prop: 'SESSION_SHOP_LEVEL',
                                        opt: '!=',
                                        res: 10
                                    },
                                    {
                                        prop: 'SESSION_SHOP_LEVEL',
                                        opt: '!=',
                                        res: 11
                                    },
                                    {
                                        prop: 'SESSION_SHOP_LEVEL',
                                        opt: '!=',
                                        res: 12
                                    }
                                ]
                            ]
                        },
                        {
                            name: '新建收款单',
                            url: '/shop/settlement/debit/bill-add'
                        },
                        {
                            name: '收款流水记录',
                            url: '/shop/settlement/debit/flow-list'
                        },
                        {
                            name: '委托对账单',
                            url: '/proxy/settlement/index',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_JOIN_STATUS',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'BPSHARE',
                                        opt: '==',
                                        res: 'true'
                                    }
                                ]
                            ]
                        }
                    ]
                },
                {
                    name: '付款',
                    list: [
                        {
                            name: '供应商付款',
                            url: '/shop/settlement/pay/pay-supplier'
                        },
                        {
                            name: '新建付款单',
                            url: '/shop/settlement/pay/pay-add'
                        },
                        {
                            name: '付款流水记录',
                            url: '/shop/settlement/pay/pay-flow'
                        }
                    ]
                },
                {
                    name: '在线支付',
                    remind: [
                        [
                            {
                                prop: 'SESSION_USER_IS_ADMIN',
                                opt: '==',
                                res: 1
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '微信支付',
                            url: '/shop/settlement/online/online-payment'
                        }
                    ]
                }
            ]
        },
        /* 财务 end */
        buy: {
            type: 'horz',
            name: '淘汽采购',
            list: [
                {
                    name: '采购订单',
                    url: '/shop/buy'
                },
                {
                    name: '采购金明细',
                    url: '/shop/yunxiu/purchase/detail'
                },
                {
                    name: '缺件配件',
                    url: '/shop/buy/short_goods'
                }
            ]
        },
        /* 淘汽采购 end */
        marketing: {
            type: 'vert',
            name: '客户营销',
            remind: [
                [
                    {
                        prop: 'SESSION_SHOP_IS_TQMALL_VERSION',
                        opt: '!=',
                        res: 'true'
                    }
                ]
            ],
            list: [
                {
                    name: '集客方案',
                    remind: [
                        [
                            {
                                prop: 'YBD',
                                opt: '==',
                                res: 'true'
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '导入客户会员',
                            url: '/marketing/gather/import-page',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ]
                            ]
                        },
                        {
                            name: '分配客户',
                            url: '/marketing/gather/allot/allot-list',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ]
                            ]
                        },
                        {
                            name: '设置奖励规则',
                            url: '/marketing/gather/rule'
                        },
                        {
                            name: '业绩提成',
                            url: '/shop/report/staff/perf?hasBack=true',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ]
                            ]
                        },
                        {
                            name: '业绩提成',
                            url: '/shop/report/gather/staff/perf?hasBack=true',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '!=',
                                        res: 1
                                    }
                                ]
                            ]
                        },
                        {
                            name: '集客消费',
                            url: '/marketing/gather/plan',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '提醒中心'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '集客效果',
                            url: '/marketing/gather/effect',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '提醒中心'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '集客详情',
                            url: '/marketing/gather/detail',
                            permission: [
                                [
                                    {
                                        prop: 'YBD',
                                        opt: '==',
                                        res: 'true'
                                    },
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'YBD',
                                        opt: '==',
                                        res: 'true'
                                    },
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '提醒中心'
                                    }
                                ]
                            ]
                        }
                    ]
                },
                {
                    name: '客户营销',
                    list: [
                        {
                            name: '客户分析',
                            url: '/marketing/ng/analysis',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '客户分析'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '提醒中心',
                            url: '/marketing/ng/maintain/center',
                            permission: [
                                [
                                    {
                                        prop: 'YBD',
                                        opt: '==',
                                        res: 'false'
                                    },
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'YBD',
                                        opt: '==',
                                        res: 'false'
                                    },
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '提醒中心'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '精准营销',
                            url: '/marketing/ng/center/accurate',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '精准营销'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '短信充值',
                            url: '/marketing/ng/center/sms',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '短信充值'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '门店推广',
                            url: '/marketing/ng/analysis/promotion',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '门店推广'
                                    }
                                ]
                            ]
                        }
                    ]
                },
                {
                    name: '客户管理',
                    remind: [
                        [
                            {
                                prop: 'SESSION_USER_IS_ADMIN',
                                opt: '==',
                                res: 1
                            }
                        ],
                        [
                            {
                                prop: 'modules',
                                opt: '==',
                                res: '客户管理'
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '客户查询',
                            url: '/account'
                        },
                        {
                            name: '卡券办理',
                            url: '/account/grant',
                        },
                        {
                            name: '优惠设置',
                            url: '/account/setting'
                        }
                    ]
                },
                {
                    name: '微信公众号',
                    url: '/shop/wechat',
                    permission: [
                        [
                            {
                                prop: 'SESSION_SHOP_LEVEL',
                                opt: '!=',
                                res: 10
                            }
                        ]
                    ],
                    remind: [
                        [
                            {
                                prop: 'IS_WECHAT_SHOP',
                                opt: '==',
                                res: 1
                            }
                        ]
                    ],
                    list: [
                        {
                            name: '主页',
                            url: '/shop/wechat'
                        },
                        {
                            name: '文章管理',
                            url: '/shop/wechat/article-list'
                        },
                        {
                            name: '活动管理',
                            url: '/shop/wechat/activity-list'
                        },
                        {
                            name: '抽奖活动',
                            url: '/shop/wechat/activity-lottery-management'
                        },
                        {
                            name: '关注送券',
                            url: '/shop/wechat/wechat-coupon'
                        },
                        {
                            name: '卡券商城',
                            url: '/shop/wechat/favormall-list'
                        },
                        {
                            name: '救援定损',
                            url: '/shop/wechat/rescue-assessment-list'
                        },
                        {
                            name: '发布服务',
                            url: '/shop/wechat/appservice/list',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '设置'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '微信菜单配置',
                            url: '/shop/wechat/wechat-menu'
                        },
                        {
                            name: '自动回复',
                            url: '/shop/wechat/msg-list'
                        },
                        {
                            name: '设置WIFI',
                            url: '/shop/wechat/wifi-manage'
                        },
                        {
                            name: '二维码',
                            url: '/shop/wechat/qrcode-list'
                        },
                        {
                            name: '资料维护',
                            url: '/shop/wechat/wechat-info'
                        }
                    ]
                }
            ]
        },
        /* 客户营销 end */
        report: {
            type: 'vert',
            name: '报表',
            list: [
                {
                    name: '管理报表',
                    list: [
                        {
                            name: '营业日报',
                            url: '/shop/report/business/daily',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '营业日报'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '营业月报',
                            url: '/shop/report/business/month',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '营业月报'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '绩效管理',
                            url: '/shop/report/staff/perf',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '绩效管理'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '经营分析报告',
                            url: '/shop/report/business/month/summary',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '经营分析报告'
                                    }
                                ]
                            ]
                        }
                    ]
                },
                {
                    name: '经营报表',
                    list: [
                        {
                            name: '工单结算收款表',
                            url: '/shop/stats/order_payment',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '工单结算收款表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '工单流水表',
                            url: '/shop/stats/order_info_detail',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '工单流水表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '经营毛利明细',
                            url: '/shop/report/gross-profits',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '经营毛利明细'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '销售明细表',
                            url: '/shop/stats/order/goods',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '销售明细表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '出入库明细表',
                            url: '/shop/stats/warehouse-info/in',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '出入库明细表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '员工考勤统计',
                            url: '/shop/stats/staff/attendance',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '员工考勤统计'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '卡券充值记录表',
                            url: '/shop/stats/card/coupon-recharge',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '卡券充值记录表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '卡券消费记录表',
                            url: '/shop/stats/card/coupon-consume',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_USER_IS_ADMIN',
                                        opt: '==',
                                        res: 1
                                    }
                                ],
                                [
                                    {
                                        prop: 'modules',
                                        opt: '==',
                                        res: '卡券消费记录表'
                                    }
                                ]
                            ]
                        },
                        {
                            name: '钣喷受托统计表',
                            url: '/proxy/report/toProxyReport',
                            permission: [
                                [
                                    {
                                        prop: 'SESSION_SHOP_WORKSHOP_STATUS',
                                        opt: '==',
                                        res: 1
                                    }
                                ]
                            ]
                        }
                    ]
                }
            ]
        }
        /* 报表 end */
    };

    var model = {
            getFuncList: function () {
                return $.ajax({
                    url: BASE_PATH + '/shop/func/get_func_list',
                    global: false,
                    dataType: 'json'
                });
            }
        },
        isWaiting = false,
        isReady = false;

    // 重构授权配置
    var getSysCtrl = function (callback) {
        isWaiting = true;

        model.getFuncList()
            .done(function (result) {
                var perms = {}, data, key;

                if (result.success) {
                    data = result.data;

                    // 档口版和不支持开放的版本 不展示
                    if (data['SESSION_SHOP_IS_TQMALL_VERSION'] == 'true' || data['HEADER_MENU_VERSION'] != 'new') {

                        unbindEvent();
                        return false;
                    }

                    for (key in data) {
                        if (data.hasOwnProperty(key) && 'number, string, boolean'.indexOf(typeof data[key]) != -1) {
                            perms[key] = data[key];
                        }
                    }

                    perms.modules = [];

                    $.each(data.funcList || [], function (i, v) {
                        perms.modules.push(v.name);
                    });

                    perms.modules = perms.modules.join(',');

                    headMenus.sysctrl = perms;
                    isReady = true;

                    callback && callback();
                } else {
                    console.error('headMenus 权限获取失败！');
                }

                isWaiting = false;
            });
    };

    // 头部菜单生成器
    var headMenus = {
        sysctrl: null,
        currHeadMenu: null,
        baseMenu: '',
        menuPanelClassName: 'js-head-menu-panel',
        menuContentClassName: 'js-head-menu-content',
        menuArrowClassName: 'js-head-menu-arrow',
        menuCalcClassName: 'js-calc-item',
        getUrl: function (url) {
            if(url == null){
                return '';
            }
            return BASE_PATH + url + ( url.indexOf('?') == -1 ? '?' : '&' ) + 'refer=float-menu';
        },
        /**
         * 菜单信息初次过滤
         * @param context
         * @returns {headMenus}
         */
        getMenuInfo: function (context) {
            var $context = $(context),
                cacheMenu = $context.data('menu'),
                JS_CONTENT_SELECTOR = '.' + this.menuContentClassName,
                currHeadMenu, menu, menuInfo, menuTpl;

            this._buildBaseMenu();
            this.emptyMenu();

            if (cacheMenu) {
                // 缓存下拉菜单
                menuTpl = cacheMenu;
            } else {
                // 创建下拉菜单
                currHeadMenu = $context.data('mu').split(' ')[0];
                // 遍历顶级菜单
                for (menu in menus) {

                    if (menus.hasOwnProperty(menu) && menu == currHeadMenu) {

                        menuInfo = menus[menu];
                        break;
                    }
                }

                if (menuInfo) {
                    menuTpl = this._buildMenuController(menuInfo);
                    $context.data('cacheMenu', menuTpl);
                }
            }

            if (menuTpl) {
                this.baseMenu.find(JS_CONTENT_SELECTOR).html(menuTpl);
            }

            return this;
        },
        /**
         * 构建基础菜单模板
         * @private
         */
        _buildBaseMenu: function () {
            if (!this.baseMenu) {
                this.baseMenu = $('<div class="yunui-head-menu-panel ' +
                    this.menuPanelClassName + '"><i class="yunui-head-menu-arrow ' +
                    this.menuArrowClassName + '"></i><div class="yunui-head-menu-content ' +
                    this.menuContentClassName + '"></div></div>')
                    .appendTo('body').hide();
            }
        },
        /**
         * 单条规则检查
         * @param opt
         * @param res
         * @param sysctrl
         * @param prop
         * @returns {boolean}
         * @private
         */
        _checkPerm: function (opt, res, sysctrl, prop) {
            var reg;

            if (prop == 'modules') {
                reg = new RegExp('(?:^|,)' + res + '(?:,|$)');
                return reg.test(sysctrl);
            }

            switch (opt) {
                case '==' :
                    return res == sysctrl;
                case '!=' :
                    return res != sysctrl;
                default:
                    console.error('为支持表达式(' + res + opt + sysctrl + ')');
            }
        },
        /**
         * 权限控制器
         * @param permissions
         * @returns {boolean}
         * @private
         */
        _accessRight: function (permissions) {
            var isSuccess = true,
                len = permissions.length,
                subLen, permission, p, i, j;

            // || 或操作处理
            for (i = 0; i < len; i++) {
                permission = permissions[i];
                subLen = permission.length;

                // && 与操作处理
                for (j = 0; j < subLen; j++) {
                    p = permission[j];

                    isSuccess = this._checkPerm(p.opt, p.res, this.sysctrl[p.prop], p.prop);

                    // 如果单条规则结果为false，跳出循环
                    if (!isSuccess) {
                        break;
                    }
                }

                // 如果一组规则集结果为true，跳出循环
                if (isSuccess) {
                    break;
                }
            }

            return isSuccess;
        },
        /**
         * 菜单控制器，控制菜单展示样式
         * @param menuInfo
         * @returns {*}
         * @private
         */
        _buildMenuController: function (menuInfo) {

            if (menuInfo.remind && !this._accessRight(menuInfo.remind)) {
                return '';
            }

            switch (menuInfo.type) {
                case 'horz':
                    return this._buildHorMenu(menuInfo.list);
                case 'vert':
                    return this._buildVerMenu(menuInfo.list);
                default:
                    console.error('菜单的展示类型type未确定！');
            }
        },
        /**
         * 二级菜单
         * @param menuList
         * @private
         */
        _buildHorMenu: function (menuList) {
            var menuTpl = '',
                len = menuList instanceof Array ? menuList.length : 0,
                menuInfo2, size, perSize = 7, i, tmpTpl,
                name, url, list, permission;

            size = 0;
            for (i = 0; i < len; i++) {
                menuInfo2 = menuList[i];
                name = menuInfo2.name;
                permission = menuInfo2.permission;
                list = menuInfo2.list;
                url = menuInfo2.url;

                // 如果权限未通过
                if (permission && !this._accessRight(permission)) {
                    continue;
                }

                size++;
                if (size % perSize == 1) {
                    tmpTpl = '<ul class="yunui-horizontal">';
                }

                url = this.getUrl(url);
                tmpTpl += '<li class="' + this.menuCalcClassName +
                    ' yunui-horizontal-item"><a href="' + url +
                    '">' + name + '</a></li>';

                if (size % perSize == 0) {
                    tmpTpl += '</ul>';
                    menuTpl += tmpTpl;
                }
            }

            if (size % perSize != 0) {
                tmpTpl += '</ul>';
                menuTpl += tmpTpl;
            }

            size || this.destroyMenu();

            return menuTpl;
        },
        /**
         * 三级菜单
         * @param menuList
         * @private
         */
        _buildVerMenu: function (menuList) {
            var menuTpl = '', tmpTpl, tmpTpl2,
                len = menuList instanceof Array ? menuList.length : 0,
                menuInfo2, menuInfo3, len2, size, size2, perSize = 7, i, j,
                name, url, menuList2, permission, remind;

            size = 0;

            // 遍历二级菜单
            for (i = 0; i < len; i++) {

                menuInfo2 = menuList[i];
                name = menuInfo2.name;
                url = menuInfo2.url;
                menuList2 = menuInfo2.list;
                permission = menuInfo2.permission;
                remind = menuInfo2.remind;

                if (permission && !this._accessRight(permission)) {
                    continue;
                }

                if (!remind || remind && this._accessRight(remind)) {

                    len2 = menuList2 instanceof Array ? menuList2.length : 0;
                    if (!menuList2 || !len2) {
                        continue;
                    }

                    tmpTpl = '<dl><dt>' + name + '</dt><dd class="flex-box">';

                    size++;
                    size2 = 0;
                    // 遍历三级菜单
                    for (j = 0; j < len2; j++) {

                        menuInfo3 = menuList2[j];
                        name = menuInfo3.name;
                        url = menuInfo3.url;
                        permission = menuInfo3.permission;

                        if (permission && !this._accessRight(permission)) {
                            continue;
                        }

                        size2++;
                        if (size2 % perSize == 1) {

                            tmpTpl2 = '<ul class="yunui-vertical flex-box ' +
                                this.menuCalcClassName + '">';
                        }

                        url = this.getUrl(url);
                        tmpTpl2 += '<li class="yunui-vertical-item"><a href="' + url +
                            '">' + name + '</a></li>';

                        if (size2 % perSize == 0) {

                            tmpTpl2 += '</ul>';
                            tmpTpl += tmpTpl2;
                        }
                    }

                    if (size2 % perSize != 0) {

                        tmpTpl2 += '</ul>';
                        tmpTpl += tmpTpl2;
                    }

                    if (size2 != 0) {

                        tmpTpl += '</dd></dl>';
                        menuTpl += tmpTpl;
                    } else {
                        size--;
                    }
                } else if (url) {

                    size++;
                    url = this.getUrl(url);
                    tmpTpl = '<dl><dt class="' + this.menuCalcClassName +
                        '"><a href="' + url + '">' + name + '</a></dt></dl>';
                    menuTpl += tmpTpl;
                }
            }

            size || this.destroyMenu();

            return menuTpl;
        },
        /**
         * 菜单定位器
         * @param x
         * @param y
         * @returns {boolean}
         */
        setPosition: function (x, y) {
            var arrowSelector = '.' + this.menuArrowClassName,
                $arrow,
                $panel = this.baseMenu,
                menuCalcSelector = '.' + this.menuCalcClassName,
                panelWidth = 0,
                panelOffsetLeft,
                maxWidth = window.innerWidth;

            if ($panel == null) {
                return false;
            }

            $arrow = $panel.find(arrowSelector);

            $panel.find(menuCalcSelector).each(function () {
                panelWidth += $(this).outerWidth(true);
            });

            $panel.css({
                'top': y + 'px',
                'width': panelWidth + 'px'
            });

            panelOffsetLeft = x - panelWidth * 0.4;

            if (panelOffsetLeft < 0) {
                panelOffsetLeft = 0;
            } else if (panelOffsetLeft + panelWidth > maxWidth) {
                panelOffsetLeft = maxWidth - panelWidth;
            }

            $panel.css('left', panelOffsetLeft + 'px');

            $arrow.css({
                'left': (x - panelOffsetLeft) + 'px'
            });
        },
        showMenu: function () {
            this.baseMenu && this.baseMenu.show();
        },
        hideMenu: function () {
            this.baseMenu && this.baseMenu.hide();
        },
        emptyMenu: function () {
            var JS_CONTENT_SELECTOR;

            if (this.baseMenu) {
                JS_CONTENT_SELECTOR = '.' + this.menuContentClassName;
                this.baseMenu.find(JS_CONTENT_SELECTOR).empty();
            }
        },
        destroyMenu: function () {
            var menuPanelSelector = '.' + this.menuPanelClassName;
            $(menuPanelSelector).remove();
            this.baseMenu = null;
        }
    };

    var MENU_PANEL_SELECTOR = '.' + headMenus.menuPanelClassName;

    function unbindEvent() {
        $doc
            .off('mouseenter', '.topmenu li[data-mu]')
            .off('mouseenter', MENU_PANEL_SELECTOR)
            .off('mouseleave', '.topmenu li[data-mu], ' + MENU_PANEL_SELECTOR)
    }

    getSysCtrl();

    $doc
        .on('mouseenter', '.topmenu li[data-mu]', function () {
            var $this = $(this),
                mu = $this.data('mu');

            function getMenu() {
                var offset, x, y;
                offset = $this.offset();
                x = offset.left - $doc.scrollLeft() + $this.width() / 2;
                y = offset.top - $doc.scrollTop() + $this.height();
                headMenus.getMenuInfo($this[0]).showMenu();
                // 定位放在展示之后，防止计算错误
                headMenus.setPosition(x, y);
            }

            if ('home' === mu || 'other' === mu) {
                
                headMenus.hideMenu();
            } else {

                if (!isReady && !isWaiting) {

                    getSysCtrl(getMenu);
                } else if (isReady) {

                    getMenu();
                }
            }

            return false;
        })
        .on('mouseenter', MENU_PANEL_SELECTOR, function () {
            headMenus.showMenu();
            return false;
        })
        .on('mouseleave', '.topmenu li[data-mu], ' + MENU_PANEL_SELECTOR, function () {
            headMenus.hideMenu();
            return false;
        });

});
