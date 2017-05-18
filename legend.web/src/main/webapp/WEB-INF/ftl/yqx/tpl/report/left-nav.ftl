<#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
<div class="report-left fl">
    <#assign report_menu=false/>
    <#if SESSION_USER_IS_ADMIN == 1>
        <#assign report_menu=true/>
    <#else>
        <#list SESSION_USER_ROLE_FUNC as temp>
            <#if temp.name == "营业日报"
            || temp.name == "营业月报"
            || temp.name == "绩效管理"
            || temp.name == "经营分析报告"
            || temp.name == "工单结算收款表"
            || temp.name == "工单流水表"
            || temp.name == "经营毛利明细"
            || temp.name == "销售明细表"
            || temp.name == "出入库明细表"
            || temp.name == "员工考勤统计"
            || temp.name == "卡券充值记录表"
            || temp.name == "卡券消费记录表"
            >
                <#assign report_menu = true/>
                <#break />
            </#if>
        </#list>
    </#if>
    <#if report_menu==true>
    <ul class="aside-nav" data-tpl-ref="report/left-nav">
    </#if>
        <!-- 导航栏 start -->
        <input type="hidden" id="tag" value="${tag}"/>

        <#if SESSION_USER_IS_ADMIN == 1>
        <#--管理员可以看到所有管理报表-->
            <li class="aside-nav-root js-nav-title">管理报表</li>
            <li class="aside-nav-list hide">
                <dl>
                    <dd id="business_daily">
                        <a class="<#if moduleUrlTab == "report_business_daily">current</#if>" href="${BASE_PATH}/shop/report/business/daily">营业日报</a>
                    </dd>
                    <dd id="business_daily">
                        <a class="<#if moduleUrlTab == "report_business_month">current</#if>" href="${BASE_PATH}/shop/report/business/month">营业月报</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "report_staff_perf">current</#if>" href="${BASE_PATH}/shop/report/staff/perf">绩效管理</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "summary_business_month">current</#if>" href="${BASE_PATH}/shop/report/business/month/summary">经营分析报告</a>
                    </dd>
                </dl>
            </li>
        <#else>
            <#assign mgr_report = 0/>
            <#list SESSION_USER_ROLE_FUNC as temp>
                <#if temp.name == "营业日报"
                    || temp.name == "营业月报"
                    || temp.name == "绩效管理"
                    || temp.name == "经营分析报告">
                    <#assign mgr_report = 1/>
                    <#break />
                </#if>
            </#list>
            <#if mgr_report == 1>
                <li class="aside-nav-root js-nav-title">管理报表</li>
                <li class="aside-nav-list hide">
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "营业日报">
                        <dd id="business_daily">
                            <a class="<#if moduleUrlTab == "report_business_daily">current</#if>" href="${BASE_PATH}/shop/report/business/daily">营业日报</a>
                        </dd>
                        <#break />
                    </#if>
                </#list>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "营业月报">
                        <dd id="business_daily">
                            <a class="<#if moduleUrlTab == "report_business_month">current</#if>" href="${BASE_PATH}/shop/report/business/month">营业月报</a>
                        </dd>
                        <#break />
                    </#if>
                </#list>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <#if temp.name == "绩效管理">
                        <dd>
                            <#if YBD == true>
                                <a class="<#if moduleUrlTab == "report_staff_perf">current</#if>" href="${BASE_PATH}/shop/report/gather/staff/perf">绩效管理</a>
                            <#else >
                                <a class="<#if moduleUrlTab == "report_staff_perf">current</#if>" href="${BASE_PATH}/shop/report/staff/perf">绩效管理</a>
                            </#if>
                        </dd>
                        <#break />
                    </#if>
                </#list>
                <#list SESSION_USER_ROLE_FUNC as temp>
                    <!-- 钣喷中心暂时不显示“经营分析报告” -->
                    <#if BPSHARE != 'true'>
                        <#if temp.name == "经营分析报告">
                            <dd>
                                <a class="<#if moduleUrlTab == "summary_business_month">current</#if>" href="${BASE_PATH}/shop/report/business/month/summary">经营分析报告</a>
                            </dd>
                            <#break />
                        </#if>
                    </#if>
                </#list>
                </li>
            </#if>
        </#if>

        <#if SESSION_USER_IS_ADMIN == 1>
            <li class="aside-nav-root js-nav-title">经营报表</li>
            <li class="aside-nav-list hide">
                <dl>
                    <dd id="order_payment">
                        <a class="<#if moduleUrlTab == "order_payment">current</#if>" href="${BASE_PATH}/shop/stats/order_payment">工单结算收款表</a>
                    </dd>
                    <dd id="n12">
                        <a class="<#if moduleUrlTab == "order_info_detail">current</#if>" href="${BASE_PATH}/shop/stats/order_info_detail">工单流水表</a>
                    </dd>
                    <dd id="n12">
                        <a class="<#if moduleUrlTab == "report_gross_profits">current</#if>" href="${BASE_PATH}/shop/report/gross-profits">经营毛利明细</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "order-sale-detail">current</#if>" href="${BASE_PATH}/shop/stats/order/goods">销售明细表</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "stats_warehouse_in">current</#if>" href="${BASE_PATH}/shop/stats/warehouse-info/in">出入库明细表</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "attendance">current</#if>" href="${BASE_PATH}/shop/stats/staff/attendance">员工考勤统计</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "card-coupon-recharge">current</#if>" href="${BASE_PATH}/shop/stats/card/coupon-recharge">卡券充值记录表</a>
                    </dd>
                    <dd>
                        <a class="<#if moduleUrlTab == "card-consume">current</#if>" href="${BASE_PATH}/shop/stats/card/coupon-consume">卡券消费记录表</a>
                    </dd>
                    <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
                        <dd>
                            <a class="<#if moduleUrlTab == "">current</#if>" href="${BASE_PATH}/proxy/report/toProxyReport">钣喷受托统计表</a>
                        </dd>
                    </#if>
                </dl>
            </li>
        <#else>
            <#assign business_report = 0/>
            <#list SESSION_USER_ROLE_FUNC as temp>
                <#if temp.name == "工单结算收款表"
                || temp.name == "工单流水表"
                || temp.name == "经营毛利明细"
                || temp.name == "销售明细表"
                || temp.name == "出入库明细表"
                || temp.name == "员工考勤统计"
                || temp.name == "卡券充值记录表"
                || temp.name == "卡券消费记录表">
                    <#assign business_report = 1/>
                    <#break />
                </#if>
            </#list>
                <#if business_report == 1>
                    <li class="aside-nav-root js-nav-title">经营报表</li>
                    <li class="aside-nav-list hide">
                        <dl>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "工单结算收款表">
                                    <dd id="order_payment">
                                        <a class="<#if moduleUrlTab == "order_payment">current</#if>" href="${BASE_PATH}/shop/stats/order_payment">工单结算收款表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "工单流水表">
                                    <dd id="n12">
                                        <a class="<#if moduleUrlTab == "order_info_detail">current</#if>" href="${BASE_PATH}/shop/stats/order_info_detail">工单流水表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "经营毛利明细">
                                    <dd id="n12">
                                        <a class="<#if moduleUrlTab == "report_gross_profits">current</#if>" href="${BASE_PATH}/shop/report/gross-profits">经营毛利明细</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "销售明细表">
                                    <dd>
                                        <a class="<#if moduleUrlTab == "order-sale-detail">current</#if>" href="${BASE_PATH}/shop/stats/order/goods">销售明细表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "出入库明细表">
                                    <dd>
                                        <a class="<#if moduleUrlTab == "stats_warehouse_in">current</#if>" href="${BASE_PATH}/shop/stats/warehouse-info/in">出入库明细表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "员工考勤统计">
                                    <dd>
                                        <a class="<#if moduleUrlTab == "attendance">current</#if>" href="${BASE_PATH}/shop/stats/staff/attendance">员工考勤统计</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "卡券充值记录表">
                                    <dd>
                                        <a class="<#if moduleUrlTab == "card-coupon-recharge">current</#if>" href="${BASE_PATH}/shop/stats/card/coupon-recharge">卡券充值记录表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#list SESSION_USER_ROLE_FUNC as temp>
                                <#if temp.name == "卡券消费记录表">
                                    <dd>
                                        <a class="<#if moduleUrlTab == "card-consume">current</#if>" href="${BASE_PATH}/shop/stats/card/coupon-consume">卡券消费记录表</a>
                                    </dd>
                                    <#break />
                                </#if>
                            </#list>
                            <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
                                <dd>
                                    <a class="<#if moduleUrlTab == "">current</#if>" href="${BASE_PATH}/proxy/report/toProxyReport">钣喷受托统计表</a>
                                </dd>
                            </#if>
                        </dl>
                    </li>
                <#else>
                    <#if SESSION_SHOP_WORKSHOP_STATUS == 1>
                        <li class="aside-nav-root js-nav-title">经营报表</li>
                        <dd>
                            <a class="<#if moduleUrlTab == "">current</#if>" href="${BASE_PATH}/proxy/report/toProxyReport">钣喷受托统计表</a>
                        </dd>
                    </#if>
                </#if>
        </#if>
        <!-- 导航栏 end -->
    <#if report_menu==true>
    </ul>
    </#if>

<#else>
<div class="report-left fl">
    <ul class="aside-nav" data-tpl-ref="report/left-nav">
        <!-- 导航栏 start -->
        <input type="hidden" id="tag" value="${tag}"/>
        <li class="aside-nav-root js-nav-title">管理报表</li>
        <li class="aside-nav-list hide">
            <dl>
                <dd id="business_daily"><a
                        class="<#if moduleUrlTab == "report_business_daily">current</#if>"
                        href="${BASE_PATH}/shop/report/business/daily">营业日报</a></dd>
            </dl>
        </li>
        <li class="aside-nav-root js-nav-title">经营报表</li>
        <li class="aside-nav-list hide">
            <dl>
                <dd id="order_payment"><a
                        class="<#if moduleUrlTab == "order_payment">current</#if>"
                        href="${BASE_PATH}/shop/stats/order_payment">工单结算收款表</a></dd>
                <dd id="n12"><a
                        class="<#if moduleUrlTab == "order_info_detail">current</#if>"
                        href="${BASE_PATH}/shop/stats/order_info_detail">工单流水表</a></dd>
                <dd id="n12">
                    <a class="<#if moduleUrlTab == "report_gross_profits">current</#if>" href="${BASE_PATH}/shop/report/gross-profits">经营毛利明细</a>
                </dd>
                <dd><a
                        class="<#if moduleUrlTab == "order-sale-detail">current</#if>"
                        href="${BASE_PATH}/shop/stats/order/goods">销售明细表</a></dd>
            </dl>
        </li>
        <!-- 导航栏 end -->
    </ul>
</#if>
    <!-- 下线提醒弹窗 start -->
    <script id="oldTipsTpl" type="text/html">
        <div class="dg-panel">
            <div class="dg-panel-head">
                <h1 class="dg-panel-title">下线提醒</h1>
            </div>
            <div class="dg-panel-body">
                <div class="dg-content">
                    <p class="dg-msg">亲，该报表将于2016年5月23日下线，请去新版“<%= msg %>”查看经营毛利，谢谢！</p>

                    <div class="dg-note">（有疑问请联系淘汽云修客服，联系电话400-9937-288转2转3，或添加客服QQ3320478090、3056630970、3274979298）
                    </div>
                    <div class="dg-btns-tool">
                        <a class="btn btn-1" href="<%= newLink %>">新版<%= msg %></a>
                        <a class="btn btn-2" href="<%= oldLink %>" target="_blank">继续看老版</a>
                    </div>
                </div>
            </div>
        </div>
    </script>
    <!-- 下线提醒弹窗 end -->
    <!-- 导航JS代码 start -->
    <script>
        $(function () {
            $(document).on('click', '.js-jump', function () {
                var $this = $(this),
                        data = {
                            newLink: $this.data('newLink'),
                            oldLink: $this.data('oldLink'),
                            msg: $this.data('msg')
                        };

                $.layer({
                    type: 1,
                    title: false,
                    area: ['auto', 'auto'],
                    border: [0],
                    shadow: ['0.5', '#000'],
                    closeBtn: [1, true],
                    shift: "top",
                    page: {
                        html: template('oldTipsTpl', data)
                    }
                });
            });
            var id = $("#tag").val();
            $("#" + id).addClass("current");

            var current = $('.aside-nav .current');

            if(current.length) {
                current.parents('.aside-nav-list:hidden').toggle(500);
            }

            // 点击展开隐藏列表
            $(document)
                    .on('click', '.aside-nav-root', function () {
                        $(this).next('.aside-nav-list').eq(0).toggle(500);
                    })

        })
    </script>
    <!-- 导航JS代码 end -->
</div>