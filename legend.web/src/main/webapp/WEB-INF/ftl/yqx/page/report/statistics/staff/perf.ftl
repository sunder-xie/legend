<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/staff/perf-common.css?9698ce167cd7842fcea413c2f989cdff">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/staff/perf.css?3ad8734885810cca95e1754eae4592b0">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <input type="hidden" id="isYBD" value="${YBD}">
<#assign now = .now>

    <div class="aside-main fl setting-main hide">
        <h3 class="headline">
            设置奖励规则
            <button class="yqx-btn-1 yqx-btn js-setting-back fr" style="margin-top: -7px;">返回</button>
        </h3>
    <#if YBD == "true">
        <ul class="navigator clearfix box">
            <li class="current">1 设置加点提成规则</li>
            <li>2 设置维修服务提成规则</li>
            <li>3 设置配件销售提成规则</li>
            <li>4 设置服务顾问提成规则</li>
        </ul>
    <#else>
        <ul class="navigator clearfix box not-ybd">
            <li class="current">1 设置维修服务提成规则</li>
            <li>2 设置配件销售提成规则</li>
            <li>3 设置服务顾问提成规则</li>
        </ul>
    </#if>
    <#-- 绩效规则设置 1.规则说明 -->
        <div class="setting-box number-1 setting-rule current-setting hide box">
            <div class="setting-header">
                规则说明
            </div>
            <div class="setting-content">
            <#if YBD == "true">
                <p>员工业绩 = 加点提成 + 维修业绩 + 销售业绩 + 服务顾问业绩</p>
                <p>加点提成 = 新客户到店奖励 + 业绩归属奖励 + 销售之星</p>
            <#else>
                <p>员工业绩 = 维修业绩 + 销售业绩</p>
            </#if>
                <p>维修业绩 = (服务工时 × 工时费 - 优惠) ÷ 维修工数量</p>
                <p>销售业绩 = 配件数量 × 售价 - 优惠</p>
                <p>服务顾问业绩 = 服务顾问工单应收金额总计</p>
                <div class="setting-footer clearfix">
                    <button class="yqx-btn yqx-btn-1 js-rule-next"
                            <#if YBD == "true">
                            data-target=".setting-box.setting-gather"
                            <#else>
                            data-target=".setting-box.setting-service"
                            </#if>
                            >下一步</button><i class="detail">设定提成规则</i>
                </div>
            </div>
        </div>
        <#if YBD == "true">
        <div class="box setting-box setting-gather hide">
            <h4 class="box-title">新客户到店奖励</h4>
            <ul class="setting-detail">
                <li>
                    <label class="col-2 right">
                        <i class="must">奖励金额</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="newCustomerReward" data-v-type="required | price">
                        </div>
                        <div class="col-2 info">元</div>
                    </div>
                </li>
                <li>
                    <label class="col-2" style="white-space: nowrap; text-align: left;">
                        <input type="checkbox" class="checkbox js-order-rule"><i class="">工单金额必须满</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="orderMinAmount" readonly>
                        </div>
                        <div class="col-2 no-wrap info">元以上才进行奖励</div>
                    </div>
                </li>
            </ul>
            <h4 class="box-title">业绩归属奖励</h4>
            <ul class="setting-detail">
                <li>
                    <label class="col-2 right">
                        <i class="must">提成方式</i>
                    </label>
                    <div class="col-4">
                        <div class="tag-control selected col-5 js-percent-method" data-value="0">按照营业额提成</div>
                        <div class="col-1 invisibility">empty</div>
                        <div class="tag-control col-5 js-percent-method" data-value="1">按照毛利率提成</div>
                    </div>
                </li>
                <li>
                    <label class="col-2 right">
                        <i class="must">提成比例</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="percentageRate" data-v-type="required|maxValue:100|integer">
                        </div>
                        <div class="col-2 no-wrap info">%</div>
                    </div>
                    <p style="clear: both;">
                        <label>
                            <input class="checkbox js-carwash" type="checkbox" checked><i class="must">提成不包含洗车单</i>
                        </label>
                    </p>
                </li>
            </ul>
            <h4 class="box-title">销售之星</h4>
            <ul class="setting-detail">
                <li>
                    <label class="col-2 right">
                        <i class="must">统计方式</i>
                    </label>
                    <div class="col-4">
                        <div class="tag-control selected col-5 js-reward-type" data-value="0">营业额</div>
                        <div class="col-1 invisibility">empty</div>
                        <div class="tag-control col-5 js-reward-type" data-value="1">
                            毛利
                        </div>
                        <div class="col-1 no-wrap info">亏损金额也计入统计</div>
                    </div>
                </li>
                <li>
                    <label class="col-2 right">
                        <i class="must">奖励周期</i>
                    </label>
                    <div class="col-4">
                        <div class="tag-control selected col-5 js-cycle" data-value="0">周</div>
                        <div class="col-1 invisibility">empty</div>
                        <div class="tag-control col-5 js-cycle" data-value="1">
                            月
                        </div>
                    </div>
                </li>
                <li>
                    <label class="col-2 right">
                        <i class="must">第一奖励金额</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="firstSaleReward" data-v-type="required|price" >
                        </div>
                        <div class="col-1 info">元</div>
                    </div>
                </li>
                <li>
                    <label class="col-2 right">
                        <i class="must">第二奖励金额</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="secondSaleReward" data-v-type="required|price">
                        </div>
                        <div class="col-1 info">元</div>
                    </div>
                </li>
                <li>
                    <label class="col-2 right">
                        <i class="must">第三奖励金额</i>
                    </label>
                    <div class="col-3">
                        <div class="form-item col-10">
                            <input class="yqx-input" name="thirdSaleReward" data-v-type="required|price">
                        </div>
                        <div class="col-1 info">元</div>
                    </div>
                </li>
                <li style="line-height: initial;">
                    <p class="col-2 invisibility">empty</p>
                    <p class="col-10" style="margin-top: 6px;color: #666;">
                        业绩提成以确认账单时间为准，如果单项不列入提成计算，请填写<i class="font-money">0</i>
                    </p>
                </li>
            </ul>
            <footer class="setting-footer clearfix">
                <button class="yqx-btn yqx-btn-2 js-complete-gather"
                        data-current="gather"
                        >完成设置</button>
                <button class="yqx-btn yqx-btn-1 js-rule-next"
                        data-target=".setting-box.setting-service"
                        data-current="gather"
                        >下一步</button><i class="detail">设定维修服务提成规则</i>
            </footer>
        </div>
        </#if>
        <div class="setting-box number-2 setting-service box hide">
        <#if YBD == "true">
            <div class="setting-header">
                2、设置维修服务提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        <#else>
            <div class="setting-header">
                1、设置维修服务提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        </#if>
            <div class="setting-content">
                <div class="margin-bt-28 tag-control-btns">
                    <label class="form-label-must">　　提成方式：</label><div class="form-item">
                    <div class="tag-control selected js-percent-method" data-value="0">
                        按照营业额提成
                    </div>
                </div>
                </div>
                <div class="margin-bt-28">
                    <label class="form-label-must">服务提成比例：</label><div class="form-item">
                    <input class="yqx-input width-138"
                           data-v-type="required | maxValue: 100 | integer"
                           name="percentageRate"><i class="percent font-arial margin-l-5">%</i>
                </div>
                </div>
                <p>添加特殊规则：</p>
                <table class="yqx-table" id="serviceSettingTable">
                    <thead>
                    <tr>
                        <th class="text-l width-50">序号</th>
                        <th>服务名称</th>
                        <th class="width-90">工时费</th>
                        <th class="width-70">按比例提成</th>
                        <th class="width-120">按单数提成</th>
                        <th class="width-70">操作</th>
                    </tr>
                    </thead>
                </table>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-2 js-get-service btn-add-service">选择服务</button>
                </div>
                <footer class="setting-footer clearfix">
                    <button class="yqx-btn yqx-btn-1 js-rule-next"
                            data-target=".setting-box.setting-goods"
                            data-current="service">下一步</button><i class="detail">设定配件销售提成规则</i>
                </footer>
            </div>
        </div>
        <div class="setting-box number-3 setting-goods box hide">
        <#if YBD == "true">
            <div class="setting-header">
                3、设置配件销售提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        <#else>
            <div class="setting-header">
                2、设置配件销售提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        </#if>
            <div class="setting-content">
                <div class="margin-bt-28 tag-control-btns">
                    <label class="form-label-must">　　提成方式：</label><div class="form-item">
                    <div class="tag-control selected js-percent-method" data-value="0">
                        按照营业额提成
                    </div><div class="tag-control js-percent-method" data-value="1">
                    按照毛利率提成
                </div>
                </div>
                </div>
                <div class="margin-bt-28">
                    <label class="form-label-must">配件提成比例：</label><div class="form-item">
                    <input class="yqx-input width-138"
                           data-v-type="required | maxValue: 100 |integer"
                           name="percentageRate"><i class="percent font-arial margin-l-5">%</i>
                </div>
                </div>
                <p>添加特殊规则：</p>
                <table class="yqx-table" id="goodsSettingTable">
                    <thead>
                    <tr>
                        <th class="text-l width-50">序号</th>
                        <th>配件名称</th>
                        <th class="width-90">售价</th>
                        <th class="width-70">按比例提成</th>
                        <th class="width-120">按单数提成</th>
                        <th class="width-70">操作</th>
                    </tr>
                    </thead>
                </table>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-2 js-get-goods btn-add-goods">选择配件</button>
                </div>
                <footer class="setting-footer clearfix">
                    <button class="yqx-btn yqx-btn-1 js-rule-next"
                            data-target=".setting-box.setting-sa-service"
                            data-current="goods">
                        下一步</button><i class="detail">设定服务顾问提成规则</i>
                </footer>
            </div>
        </div>
        <div class="setting-box number-4 setting-sa-service box hide">
        <#if YBD == "true">
            <div class="setting-header">
                4、设置服务顾问提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        <#else>
            <div class="setting-header">
                3、设置服务顾问提成规则<i class="detail">(此规则修改只会影响本月及以后数据，对之前产生的业绩不做修改)</i>
            </div>
        </#if>
            <div class="setting-content">
                <div class="margin-bt-28 tag-control-btns">
                    <label class="form-label-must">　　　　提成方式：</label><div class="form-item">
                    <div class="tag-control selected js-percent-method" data-value="0">
                        按照营业额提成
                    </div><div class="tag-control js-percent-method" data-value="1">
                    按照毛利率提成
                </div>
                </div>
                </div>
                <div class="margin-bt-28">
                    <label class="form-label-must">服务顾问提成比例：</label><div class="form-item">
                    <input class="yqx-input width-138"
                           data-v-type="required | maxValue: 100 |integer"
                           name="percentageRate"><i class="percent font-arial margin-l-5">%</i>
                </div>
                </div>
                <p class="margin-bt-28 strong">业绩提成以确认账单时间为准，如果服务顾问不列入提成计算，请填写 0</p>

                <div class="setting-footer clearfix">
                    <button class="yqx-btn yqx-btn-2 js-rule-next"
                            data-current="sa-service"
                            >
                        完成设置</button>
                </div>
            </div>
        </div>
    </div>

    <div class="perf-content content fl">
        <h1 class="headline">绩效管理</h1>

        <div class="container top">
            <div class="tab-box clearfix">
                <div class="tab-item js-tab-item current-item"
                     data-desc="summary"
                     data-target=".summary"
                        >业绩汇总
                </div>
            <#if YBD == 'true'>
                <div class="tab-item js-tab-item"
                     data-desc="gather"
                     data-target=".gather"
                        >加点提成
                </div>
            </#if>
                <div class="tab-item js-tab-item"
                     data-desc="repair"
                     data-target=".repair"
                        >维修业绩
                </div>
                <div class="tab-item js-tab-item"
                     data-desc="sale"
                     data-target=".sale"
                        >销售业绩
                </div>
                <div class="tab-item js-tab-item"
                     data-desc="service"
                     data-target=".service"
                        >服务顾问业绩
                </div>

                <div class="fr rule-box js-rule-detail">
                    <i class="rule-detail font-yahei">当月绩效规则</i><i class="c-question-i"></i>
                </div>
            </div>

        <#--业绩汇总的查询-->
            <div class="form summary-form summary" id="summaryForm">
                <div class="show-grid">
                    <label>选择日期</label><div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div><button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                    <i class="fr js-reset-setting reset-setting" data-current=".summary">重新设定规则</i>
                </div>
            </div>
        <#--加点提成的查询-->
            <div class="form gather-form gather hide" id="gatherForm">
                <div class="show-grid">
                    <label>选择日期</label><div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div><button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                    <i class="fr js-reset-setting reset-setting">重新设定规则</i>
                </div>
            </div>
        <#--维修业绩的查询-->
            <div class="form repair-form repair hide" id="repairForm">
                <div class="show-grid">
                    <label>选择日期</label><div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div><button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                    <i class="fr js-reset-setting reset-setting" data-current=".repair">重新设定规则</i>
                </div>
            </div>
        <#--销售业绩的查询-->
            <div class="form sale-form sale hide" id="saleForm">
                <div class="show-grid">
                    <label>选择日期</label><div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div><button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                    <i class="fr js-reset-setting reset-setting" data-current=".sale">重新设定规则</i>
                </div>
            </div>
        <#--服务顾问业绩的查询-->
            <div class="form service-form service hide" id="serviceForm">
                <div class="show-grid">
                    <label>选择日期</label><div class="form-item form-date">
                    <input class="yqx-input js-date"
                           value="${now?string["yyyy-MM"]}"
                           name="dateStr"><i class="fa icon-calendar"></i>
                </div><button class="yqx-btn yqx-btn-3 js-search-btn">查询</button>
                    <i class="fr js-reset-setting reset-setting" data-current=".service">重新设定规则</i>
                </div>
            </div>
        <#--维修业绩详情——服务-->
            <div class="detail-box repair-service-detail hide">
                <div class="detail-header font-yahei">
                    员工服务项目统计-<i class="month"></i>
                    <div class="tool fr js-excel">
                        <i class="fa icon-signout" data-desc="repair_service"></i><i>导出Excel</i>
                    </div>
                </div>
                <div class="detail-info">
                    <strong class="empName"></strong><i class="see-order-detail js-order-detail"
                                                        data-current=".repair-service-detail"
                                                        data-target=".repair-order-detail">工单明细</i>
                    <div class="quick-look">
                        <strong>快速查看<i class="fa icon-angle-down"></i></strong><ul class="name-list">
                    </ul>
                    </div>
                </div>
                <div id="repairServiceDetailForm" class="hide">
                    <input name="empId" hidden>
                    <input name="dateStr" hidden>
                </div>
                <div class="detail-content">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th colspan="2">维修工</th>
                            <th colspan="3">服务项目</th>
                            <th colspan="2">维修业绩</th>
                        </tr>
                        </thead>
                        <tr>
                            <td>序号</td>
                            <td>员工姓名</td>
                            <td>服务名称</td>
                            <td>提成规则</td>
                            <td>项目数</td>
                            <td class="text-r">金额</td>
                            <td class="text-r">提成</td>
                        </tr>

                        <tbody id="repairServiceDetailFill">

                        </tbody>
                    </table>
                    <div id="repairServiceDetailPage" class="yqx-page"></div>
                </div>
                <button class="js-back yqx-btn yqx-btn-2">返回</button>
            </div>
        <#--维修业绩详情——工单-->
            <div class="detail-box repair-order-detail hide">
                <div class="detail-header font-yahei">
                    员工服务项目明细-<i class="month"></i>
                    <div class="tool fr js-excel">
                        <i class="fa icon-signout" data-desc="repair_order"></i><i>导出Excel</i>
                    </div>
                </div>
                <div class="detail-info">
                    <strong class="empName"></strong>
                    <div class="quick-look">
                        <strong>快速查看<i class="fa icon-angle-down"></i></strong><ul class="name-list">
                    </ul>
                    </div>
                </div>
                <div id="repairOrderDetailForm" class="hide">
                    <input name="empId" hidden>
                    <input name="dateStr" hidden>
                </div>
                <div class="detail-content">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th class="width-70">日期</th>
                            <th>工单号</th>
                            <th>车牌</th>
                            <th>服务项目</th>
                            <th class="text-r">工时费</th>
                            <th>维修工</th>
                            <th>工时</th>
                            <th>提成规则</th>
                            <th class="text-r">提成金额</th>
                        </tr>
                        </thead>
                        <tbody id="repairOrderDetailFill">

                        </tbody>
                    </table>
                    <div id="repairOrderDetailPage" class="yqx-page"></div>
                </div>
                <button class="js-back yqx-btn yqx-btn-2">返回</button>
            </div>
        <#--销售业绩详情——配件项目-->
            <div class="detail-box sale-goods-detail hide">
                <div class="detail-header font-yahei">
                    员工配件项目统计-<i class="month"></i>
                    <div class="tool fr js-excel">
                        <i class="fa icon-signout" data-desc="sale_goods"></i><i>导出Excel</i>
                    </div>
                </div>
                <div class="detail-info">
                    <strong class="empName"></strong><i class="see-order-detail js-order-detail"
                                                        data-current=".sale-goods-detail"
                                                        data-target=".sale-order-detail">工单明细</i>
                    <div class="quick-look">
                        <strong>快速查看<i class="fa icon-angle-down"></i></strong><ul class="name-list">
                    </ul>
                    </div>
                </div>
                <div id="saleGoodsDetailForm" class="hide">
                    <input name="dateStr" hidden>
                    <input name="empId" hidden>
                </div>
                <div class="detail-content">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th colspan="2">销售员</th>
                            <th colspan="2">配件项目</th>
                            <th colspan="3">销售业绩</th>
                        </tr>
                        </thead>
                        <tr>
                            <td>序号</td>
                            <td>销售员</td>
                            <td>配件名称</td>
                            <td>提成规则</td>
                            <td>数量</td>
                            <td class="text-r">配件金额</i></td>
                            <td class="text-r">提成金额</td>
                        </tr>
                        <tbody id="saleGoodsDetailFill">

                        </tbody>
                    </table>
                </div>
                <button class="js-back yqx-btn yqx-btn-2">返回</button>
            </div>
        <#--销售业绩详情——工单明细-->
            <div class="detail-box sale-order-detail hide">
                <div class="detail-header font-yahei">
                    员工配件项目明细-<i class="month"></i>
                    <div class="tool fr js-excel">
                        <i class="fa icon-signout" data-desc="sale_order"></i><i>导出Excel</i>
                    </div>
                </div>
                <div class="detail-info">
                    <strong class="empName"></strong>
                    <div class="quick-look">
                        <strong>快速查看<i class="fa icon-angle-down"></i></strong><ul class="name-list">
                    </ul>
                    </div>
                </div>
                <div id="saleOrderDetailForm" class="hide">
                    <input name="empId" hidden>
                    <input name="dateStr" hidden>
                </div>
                <div class="detail-content">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>工单号</th>
                            <th>车牌</th>
                            <th>配件名称</th>
                            <th>配件数量</th>
                            <th class="text-r">配件单价</th>
                            <th class="text-r">优惠</th>
                            <th class="text-r">配件金额</i></th>
                            <th>提成规则</th>
                            <th class="text-r">提成金额</th>
                            <th>销售员</th>
                        </tr>
                        </thead>
                        <tbody id="saleOrderDetailFill">

                        </tbody>
                    </table>
                    <div id="saleOrderDetailPage" class="yqx-page"></div>
                </div>
                <button class="js-back yqx-btn yqx-btn-2">返回</button>
            </div>
        <#--服务顾问详情——工单明细-->
            <div class="detail-box service-order-detail hide">
                <div class="detail-header font-yahei">
                    服务顾问业绩统计-<i class="month"></i>
                    <div class="tool fr js-excel">
                        <i class="fa icon-signout" data-desc="sa_order"></i><i>导出Excel</i>
                    </div>
                </div>
                <div class="detail-info">
                    <strong class="empName"></strong>
                    <div class="quick-look">
                        <strong>快速查看<i class="fa icon-angle-down"></i></strong><ul class="name-list">
                    </ul>
                    </div>
                </div>
                <div id="serviceOrderDetailForm" class="hide">
                    <input name="empId" hidden>
                    <input name="dateStr" hidden>
                </div>
                <div class="detail-content">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>工单号</th>
                            <th>车牌</th>
                            <th class="text-r">营业额</i></th>
                            <th>提成规则</th>
                            <th class="text-r">提成金额</th>
                        </tr>
                        </thead>
                        <tbody id="serviceOrderDetailFill">

                        </tbody>
                    </table>
                    <div id="serviceOrderDetailPage" class="yqx-page"></div>
                </div>
                <button class="js-back yqx-btn yqx-btn-2">返回</button>
            </div>
        </div>

        <div class="gather-detail gather-header hide">
            <strong>员工加点提成<i class="month">${now?string["yyyy-MM"]}</i>详情</strong>
            <div class="fr export-box js-excel">
                <i class="fa icon-signout" data-desc="export_staff_commission"></i><i>导出Excel</i>
            </div>
        </div>
        <div class="gather hide container container-list">
            <div class="container-header">
                <i class="table-header-icon"></i>加点提成汇总
                <div class="export-box fr js-excel">
                    <i class="fa icon-signout" data-desc="export_commission_summary"></i><i>导出Excel</i>
                </div>
            </div>
            <div class="table-box">
                <div class="empty-table hide">
                    <p><i class="exclamation-mark"></i>当月未设置员工加点提成</p>
                </div>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th class="text-r">营业额</th>
                        <th class="text-r">客户消费提成</th>
                        <th>到店新客户</th>
                        <th class="text-r">新客户提成</th>
                        <th class="text-r">销售之星提成</th>
                        <th class="text-r">加点提成</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="gatherFill">

                    </tbody>
                </table>
            </div>
        </div>
    <#--加点提成-->
        <div class="container gather-detail hide gather-container">
            <h4>销售排名</h4>
            <div class="quick-look-box">
                <i>快速查看：</i><ul class="look-list clearfix">
            </ul>
            </div>
            <div id="gatherDetailForm" class="hide form">
                <input name="userId" hidden>
                <input name="dateStr" hidden>
            </div>
            <div id="gatherDetailForm2" class="hide form">
                <input name="empId" hidden>
                <input name="dateStr" hidden>
            </div>
            <h5>加点提成总计</h5>
            <div class="echarts-box">
                <div class="gather-echarts" id="gatherCount">

                </div>
                <div class="gather-echarts-info">
                    <div class="echarts-total">
                        总计<strong class="fr text"><i class="money-font"></i></strong>
                    </div>
                    <ul>
                        <li class="business">业绩归属奖励：<i class="business-belone"></i></li>
                        <li class="customer">新客户到店奖励：<i class="new-customer"></i></li>
                        <li class="star">销售之星：<i class="sale-star"></i></li>
                    </ul>
                </div>
            </div>
            <h5>销售之星奖励</h5>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th>排名时间</th>
                        <th>销售排名</th>
                        <th class="text-r">销售之星奖励</th>
                    </tr>
                    </thead>
                    <tbody id="saleStartFill">

                    </tbody>
                </table>
            </div>
            <h5>新客户到店奖励</h5>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th>车辆</th>
                        <th>到店消费时间</th>
                        <th class="text-r">营业额</i></th>
                        <th class="text-r">新客户到店奖励</th>
                    </tr>
                    </thead>
                    <tbody id="newCustomerFill">

                    </tbody>
                </table>
                <div class="yqx-page" id="newCustomerPage"></div>
            </div>
            <h5>业绩归属奖励</h5>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th>车辆</th>
                        <th>到店消费时间</th>
                        <th class="text-r">营业额</i></th>
                        <th class="text-r">业绩归属奖励</th>
                    </tr>
                    </thead>
                    <tbody id="businessBeloneFill">

                    </tbody>
                </table>
                <div class="yqx-page" id="businessBelonePage"></div>
            </div>
            <div class="clearfix">
                <button class="gather-detail js-back yqx-btn yqx-btn-2 fr">返回</button>
            </div>
        </div>

        <div class="container container-list summary summary-container">
            <div class="container-header">
                <i class="table-header-icon"></i><i>员工业绩汇总</i>
                <div class="export-box fr js-excel">
                    <i class="fa icon-signout" data-desc="collect"></i><i>导出Excel</i>
                </div>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th class="text-r">维修业绩</th>
                        <th class="text-r reward-width"
                                >维修提成</th>
                        <th class="text-r">配件金额</th>
                        <th class="text-r reward-width"
                                >销售提成</th>
                        <th class="text-r">营业额</th>
                        <th class="text-r reward-width"
                                >服务顾问提成</th>
                    <#if YBD == 'true'>
                        <th class="text-r reward-width">加点提成</th>
                    </#if>
                        <th class="text-r reward-width"
                                >总提成</th>
                    </tr>
                    </thead>
                    <tbody id="summaryFill"></tbody>
                </table>
            </div>
        </div>
        <div class="container container-list repair repair-container hide">
            <div class="container-header">
                <i class="table-header-icon"></i><i>维修业绩汇总</i>
                <div class="export-box fr js-excel">
                    <i class="fa icon-signout" data-desc="repair_collect"></i><i>导出Excel</i>
                </div>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>维修工</th>
                        <th>单数</th>
                        <th>台次</th>
                        <th>服务项目数</th>
                        <th class="text-r">服务金额</th>
                        <th class="text-r">提成总额</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="repairFill">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="container container-list sale sale-container hide">
            <div class="container-header">
                <i class="table-header-icon"></i><i>销售业绩汇总</i>
                <div class="export-box fr js-excel">
                    <i class="fa icon-signout" data-desc="sale_collect"></i><i>导出Excel</i>
                </div>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>销售员</th>
                        <th>销售数量</th>
                        <th class="text-r">配件销售总额</th>
                        <th class="text-r">配件毛利</th>
                        <th>毛利率</th>
                        <th class="text-r">提成总额</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="saleFill">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="container container-list service service-container hide">
            <div class="container-header">
                <i class="table-header-icon"></i><i>服务顾问业绩汇总</i>
                <div class="export-box fr js-excel">
                    <i class="fa icon-signout" data-desc="sa_collect"></i><i>导出Excel</i>
                </div>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>服务顾问</th>
                        <th>单数</th>
                        <th>台次</th>
                        <th class="text-r">营业额</i></th>
                        <th>业绩提成规则</th>
                        <th class="text-r">提成总额</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="serviceFill">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var service = json[i]%>
    <tr class="data-tr" data-id="<%=service.id || service.relId%>">
        <td class="text-l"><%=(index || 0) + (+i + 1)%></td>
        <!--服务名称-->
        <td class="js-show-tips">
            <div class="form-item">
                <input type="text" name="relName" class="js-show-tips yqx-input yqx-input-small"
                       value="<%=service.name || service.relName%>" disabled/>
                <input type="hidden" name="relId" value="<%=service.id || service.relId%>"/>
            </div>
        </td>
        <!--工时费-->
        <td class="js-show-tips">
            <div class="form-item">
                <input type="text" value="<%=service.servicePrice || service.price%>"
                       disabled
                       class="js-show-tips yqx-input yqx-input-small js-service-price"
                       maxlength="10"/>
            </div>
        </td>
    <#-- 按比例提成 -->
        <td>
            <div class="form-item js-tag-control tag-control <%if(service.percentageType != 2){%> selected<%}%> js-by-rate">
                <input class="yqx-input"
                       data-type="required | maxValue: 100 | number"
                <%if(service.percentageType !== 2){%>data-v-type="required | maxValue: 100 | number"<%}%>
                <%if(service.percentageType === 1){%>value="<%=service.percentageRate%>"<%}%>
                name="percentageRate">
            </div><i class="percent font-arial">%</i>
        </td>
    <#-- 按单数提成 -->
        <td>
            <div class="form-item width-50 js-tag-control tag-control <%if(service.percentageType == 2){%> selected<%}%> js-by-num">
                <input class="yqx-input"
                       data-type="required | number"
                <%if(service.percentageType === 2){%>value="<%=service.percentageAmount%>"<%}%>
                name="percentageAmount">
            </div><i class="margin-l-5">元</i>
        </td>
        <td class="js-show-tips">
            <button class="table-del js-del">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<!--物料-->
<script type=" text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var goods = json[i]%>
    <% if(goods.percentageType === 0) {continue;}%>
    <tr class="data-tr" data-id="<%=goods.id != null ? goods.id : goods.relId%>">
        <td class="text-l">
            <%=(index || 0) + +i + 1%>
        </td>
        <!--配件名称-->
        <td class="js-show-tips">
            <div class="form-item">
                <input type="text" name="relName" value="<%=goods.name||goods.relName%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
                <input name="relId" value="<%=goods.id||goods.relId%>" hidden>
            </div>
        </td>
        <!--售价-->
        <td class="js-show-tips">
            <div class="form-item">
                <input type="text" value="<%=goods.price%>"
                       disabled
                       class="yqx-input yqx-input-small js-goods-price js-show-tips">
            </div>
        </td>
        <!-- 按比例提成 -->
        <td>
            <div class="form-item js-tag-control tag-control <%if(goods.percentageType != 2){%> selected<%}%> js-by-rate">
                <input class="yqx-input" name="percentageRate"
                       data-type="required | maxValue: 100 | number"
                <%if(goods.percentageType === 1){%>value="<%=goods.percentageRate%>"<%}%>
                <%if(goods.percentageType !== 2){%>data-v-type="required | maxValue: 100 | number"<%}%>>
            </div><i class="percent font-arial">%</i>
        </td>
        <!-- 按单数提成 -->
        <td>
            <div class="form-item width-50 js-tag-control tag-control js-by-num
             <%if(goods.percentageType === 2){%> selected <%}%>">
                <input class="yqx-input" name="percentageAmount"
                       data-type="required | number"
                <%if(goods.percentageType === 2){%>value="<%=goods.percentageAmount%>"<%}%>
                >
            </div><i class="margin-l-5">元/<%=goods.measureUnit%></i>
        </td>
        <td class="js-show-tips">
            <button class="table-del js-del">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<#-- 业绩汇总 -->
<script id="summaryCollectTpl" type="text/template">
    <% if(json.success) {%>
    <% var hasType = {};%>
    <% for(var i in json.data) {%>
    <% var t = json.data[i];%>
    <tr>
        <td class="js-show-tips">
            <%=+i+1%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
        <td class="js-show-tips text-r">
            &yen;<%=t.repairAmount%>
        </td>
        <td class="js-show-tips money-font">
            &yen;<%=t.repairPercentage%>
        </td>
        <td class="text-r">
            &yen;<%=t.saleOrderOrGrossAmount%>
            <%if(t.salePrefType && !hasType.sale) {%>
            <input type="hidden" class="perf-type" value="<%=t.salePrefType%>">
            <% hasType.sale = true;%>
            <%}%>
        </td>
        <td class="money-font">
            &yen;<%=t.salePercentage%>
        </td>
    <#--服务顾问业绩-->
        <td class="text-r">
            &yen;<%=t.saOrderOrGrossAmount%>
            <%if(t.saPrefType && !hasType.sa) {%>
            <input type="hidden" class="perf-type"  value="<%=t.saPrefType%>">
            <% hasType.sa = true;%>
            <%}%>
        </td>
    <#--服务业绩提成-->
        <td class="money-font">
            &yen;<%=t.saPercentage%>
        </td>
    <#--加点提成-->
        <%if(t.addPointPercentage !== undefined) {%>
        <td class="money-font">
            &yen;<%=t.addPointPercentage%>
        </td>
        <%}%>
        <td class="money-font">
            <%var sum = (+t.salePercentage).Jia(t.repairPercentage)
                .Jia(t.saPercentage).Jia(t.addPointPercentage || 0)%>
            <%var len = (sum + '').replace(/\d+\./, '').length;%>

            &yen;<%= len > 2 ? sum : sum.toFixed(2)%>
        </td>
    </tr>
    <%}%>
    <%}%>
    <% if(i == null) {%>
    <tr>
    <#if YBD>
        <td colspan="10">暂无数据</td>
    <#else>
        <td colspan="9">暂无数据</td>
    </#if>
    </tr>
    <%}%>
</script>

<#--维修业绩汇总 -->
<script id="repairCollectTpl" type="text/template">
    <% if(json.success) {%>
    <% for(var i in json.data) {%>
    <% var t = json.data[i];%>
    <tr>
        <td class="js-show-tips">
            <%=+i+1%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#-- 单数 -->
        <td class="js-show-tips">
            <%=t.orderNum%>
        </td>
    <#-- 台次 -->
        <td class="js-show-tips">
            <%=t.repairCarNum%>
        </td>
    <#-- 服务项目数 -->
        <td class="js-show-tips">
            <%=t.serviceNum%>
        </td>
        <td class="money-font">
            &yen;<%=t.repairAmount%>
        </td>
        <td class="money-font">
            &yen;<%=t.repairPercentage%>
        </td>
        <td class="js-see-detail"
            data-emp-name="<%=t.empName%>"
            data-amount="<%=t.repairAmount%>"
            data-reward="<%=t.repairPercentage%>"
            data-hours="<%=t.hours%>"
            data-current=".repair"
            data-target=".repair-service-detail" data-emp-id="<%=t.empId%>">
            <div class="see-detail">查看详情</div>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%if(i == null) {%>
        <tr>
            <td colspan="8">暂无数据</td>
        </tr>
    <%}%>
</script>

<#--维修业绩明细-->
<script id="repairServiceDetailTpl" type="text/template">
    <% if(json.success && json.data && json.data.content) {%>
    <% var page = json.data.page - 1; page = page >= 0 ? page : 0;%>
    <% for(var i in json.data.content) {%>
    <% var t = json.data.content[i];%>
    <tr>
        <td class="js-show-tips">
            <%=page * 10 + (+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#-- 服务名称 -->
        <td class="js-show-tips">
            <%=t.serviceName%>
        </td>
    <#-- 提成规则 -->
        <td class="js-show-tips">
            <%=t.percentageRule%> <%=t.percentageNum%>
        </td>
    <#-- 服务项目数 -->
        <td class="js-show-tips">
            <%=t.serviceNum%>
        </td>
        <td class="money-font js-show-tips">
            &yen;<%=t.repairAmount%>
        </td>
        <td class="money-font js-show-tips">
            &yen;<%=t.repairPercentage%>
        </td>
    </tr>
    <#--合计-->
    <%if(i == json.data.content.length - 1) {%>
    <% var sum = getTotal('repair', t.empId) ;%>
    <tr>
        <td>合计</td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td>
            --
        </td>
        <td class="money-font">
            &yen;<%=sum.amount%>
        </td>
        <td class="money-font">
            &yen;<%=sum.reward%>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>

<#--维修业绩工单明细-->
<script id="repairOrderDetailTpl" type="text/template">
    <% if(json.success && json.data && json.data.content) {%>
    <% var page = json.data.page - 1; page = page >= 0 ? page : 0;%>
    <% for(var i in json.data.content) {%>
    <% var t = json.data.content[i];%>
    <tr>
        <td class="js-show-tips">
            <%=page * 10 + (+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.dateStr%>
        </td>
    <#--工单号-->
        <td class="js-show-tips">
            <%=t.orderSn%>
        </td>
        <td class="js-show-tips">
            <%=t.license%>
        </td>
    <#-- 服务名称 -->
        <td class="js-show-tips">
            <%=t.serviceName%>
        </td>
    <#--工时费-->
        <td class="js-show-tips money-font">
            &yen;<%=t.servicePrice%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#--工时-->
        <td class="js-show-tips">
            <%=t.hours%>
        </td>
    <#-- 提成规则 -->
        <td class="js-show-tips">
            <%=t.percentageRule%> <%=t.percentageNum%>
        </td>
        <td class="money-font js-show-tips">
            &yen;<%=t.repairPercentage%>
        </td>
    </tr>
    <#--合计-->
    <%if(i == json.data.content.length - 1) {%>
    <% var total = getTotal('repair', t.empId) ;%>
    <tr>
        <td class="js-show-tips">
            合计
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td class="text-r">--</td>
        <td>--</td>
        <td class="js-show-tips">
            <%=total.hours%>
        </td>
        <td>--</td>
        <td class="money-font">
            &yen;<%=total.reward;%>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>
<script id="saleCollectTpl" type="text/template">
    <% if(json.success) {%>
    <% for(var i in json.data) {%>
    <% var t = json.data[i];%>
    <tr>
        <td class="js-show-tips">
            <%=(+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#-- 销售数量 -->
        <td class="js-show-tips">
            <%=t.goodsNum%>
        </td>
    <#-- 销售总额 -->
        <td class="money-font">
            &yen;<%=t.saleAmount%>
        </td>
    <#-- 配件毛利 -->
        <td class="text-r">
            &yen;<%=t.benefitAmount%>
        </td>
    <#--毛利率-->
        <td class="js-show-tips">
            <%=t.benefitRate%><i class="percent font-arial">%</i>
        </td>
    <#--提成总额-->
        <td class="money-font js-show-tips">
            &yen;<%=t.percentageAmount%>
        </td>
        <td class="js-see-detail" data-target=".sale-goods-detail"
            data-current=".sale"
            data-amount="<%=t.saleOrderOrGrossAmount%>"
            data-reward="<%=t.percentageAmount%>"
            data-emp-name="<%=t.empName%>"
            data-emp-id="<%=t.empId%>">
            <div class="see-detail">查看详情</div>
        </td>
    </tr>
    <%}%>
    <%}%>
    <% if(i == null) {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 销售业绩详细 -->
<script id="saleGoodsDetailTpl" type="text/template">
    <% if(json.success && json.data) {%>
    <%var hasType;%>
    <% for(var i in json.data) {%>
    <% var t = json.data[i];%>
    <tr>
        <td class="js-show-tips">
            <%=(+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#-- 配件名称 -->
        <td class="js-show-tips">
            <%=t.goodsName%>
        </td>
    <#--提成规则-->
        <td class="js-show-tips">
            <%=t.percentageRule%> <%=t.percentageNum%>
        </td>
    <#--单数-->
        <td class="js-show-tips">
        <#--比例提成的时候显示 '-' -->
            <% if( ('' + t.percentageNum).indexOf('%') > -1) {%>
            <%='-'%>
            <%} else {%>
            <%=t.goodsNum + ' ' + t.measureUnit%>
            <%}%>
        </td>
    <#-- 销售总额 -->
        <td class="money-font js-show-tips">
            &yen;<%=t.saleOrderOrGrossAmount%>
            <%if(t.salePrefType && !hasType) {%>
            <input type="hidden" class="perf-type"  value="<%=t.salePrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>

    <#--提成总额-->
        <td class="money-font js-show-tips">
            &yen;<%=t.percentageAmount%>
        </td>
    </tr>
    <#--合计-->
    <%if(i == json.data.length - 1) {%>
    <% var a = getTotal('sale', json.data[0].empId);%>
    <tr>
        <td class="js-show-tips">
            合计
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td class="money-font js-show-tips">
            &yen;<%=a.amount%>
        </td>
        <td class="money-font js-show-tips">
            &yen;<%=a.reward%>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>

<#-- 销售业绩工单详细 -->
<script id="saleOrderDetailTpl" type="text/template">
    <% if(json.success && json.data && json.data.content) {%>
    <% var page = json.data.page - 1; page = page >= 0 ? page : 0;%>
    <% var hasType;%>
    <% var sum = {
    discountAmount: 0,
    }%>
    <% for(var i in json.data.content) {%>
    <% var t = json.data.content[i];%>
    <tr>
        <td class="js-show-tips">
            <%=page * 10 + (+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.dateStr%>
        </td>
        <td class="js-show-tips">
            <%=t.orderSn%>
        </td>
        <td class="js-show-tips">
            <%=t.license%>
        </td>
    <#-- 配件名称 -->
        <td class="js-show-tips">
            <%=t.goodsName%>
        </td>
    <#--配件数量-->
        <td class="js-show-tips">
            <%=t.goodsNum%> <%=t.measureUnit%>
        </td>
        <td class="js-show-tips money-font">
            &yen;<%=t.goodsPrice%>
        </td>
    <#--优惠-->
        <td class="text-r money-font js-show-tips">
            &yen;<%=t.discountAmount%>
            <% sum.discountAmount = sum.discountAmount.Jia(t.discountAmount)%>
        </td>
    <#--配件金额-->
        <td class="text-r money-font js-show-tips">
            &yen;<%=t.saleOrderOrGrossAmount%>
            <%if(t.salePrefType && !hasType) {%>
            <input type="hidden" class="perf-type"  value="<%=t.salePrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>

    <#--提成规则-->
        <td class="js-show-tips">
            <%=t.percentageRule%> <%=t.percentageNum%>
        </td>

    <#--提成总额-->
        <td class="money-font js-show-tips text-r">
            &yen;<%=t.percentageAmount%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    </tr>
    <#--合计-->
    <%if(i == json.data.content.length - 1) {%>
    <% var a = getTotal('sale', t.empId);%>
    <tr>
        <td class="js-show-tips">
            合计
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td class="text-r">--</td>
        <td class="money-font">
            &yen;<%=sum.discountAmount%>
        </td>
        <td class="money-font js-show-tips">
            &yen;<%=a.amount%>
        </td>
        <td>--</td>
        <td class="money-font js-show-tips">
            &yen;<%=a.reward%>
        </td>
        <td>--</td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>

<#--服务顾问业绩汇总 -->
<script id="serviceCollectTpl" type="text/template">
    <% if(json.success) {%>
    <% var hasType;%>
    <% for(var i in json.data) {%>
    <% var t = json.data[i];%>
    <tr>
        <td class="js-show-tips">
            <%=+i+1%>
        </td>
        <td class="js-show-tips">
            <%=t.empName%>
        </td>
    <#-- 单数 -->
        <td class="js-show-tips">
            <%=t.orderNum%>
        </td>
        <td class="js-show-tips">
            <%=t.carNum%>
        </td>
    <#-- 服务顾问业绩 -->
        <td class="money-font">
            &yen;<%=t.saOrderOrGrossAmount%>
            <%if(t.saPrefType && !hasType) {%>
            <input type="hidden" class="perf-type"  value="<%=t.saPrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>
    <#--提成规则-->
        <td>
            <%=t.percentageNum%>
        </td>
        <td class="money-font">
            &yen;<%=t.percentageAmount%>
        </td>
        <td class="js-see-detail"
            data-emp-name="<%=t.empName%>"
            data-amount="<%=t.saOrderOrGrossAmount%>"
            data-reward="<%=t.percentageAmount%>"
            data-current=".service"
            data-target=".service-order-detail" data-emp-id="<%=t.empId%>">
            <div class="see-detail">查看详情</div>
        </td>
    </tr>
    <%}%>
    <%}%>
    <% if(i == null) {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>

<#-- 服务顾问业绩--工单详细  start -->
<script id="serviceOrderDetailTpl" type="text/template">
    <% if(json.success && json.data && json.data.content) {%>
    <% var page = json.data.page - 1; page = page >= 0 ? page : 0;%>
    <% var hasType;
    <% for(var i in json.data.content) {%>
    <% var t = json.data.content[i];%>
    <tr>
        <td class="js-show-tips">
            <%=page * 10 + (+i+1)%>
        </td>
        <td class="js-show-tips">
            <%=t.dateStr%>
        </td>
        <td class="js-show-tips">
            <%=t.orderSn%>
        </td>
        <td class="js-show-tips">
            <%=t.license%>
        </td>
    <#--应收金额-->
        <td class="js-show-tips money-font">
            &yen;<%=t.saOrderOrGrossAmount%>
            <%if(t.saPrefType && !hasType) {%>
            <input type="hidden" class="perf-type"  value="<%=t.saPrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>
    <#--提成规则-->
        <td class="js-show-tips">
            <%=t.percentageRule || ''%> <%=t.percentageNum%>
        </td>

    <#--提成总额-->
        <td class="money-font js-show-tips text-r">
            &yen;<%=t.percentageAmount%>
        </td>
    </tr>
    <#--合计-->
    <%if(i == json.data.content.length - 1) {%>
    <%var a = getTotal('sa', t.empId);%>
    <tr>
        <td class="js-show-tips">
            合计
        </td>
        <td>--</td>
        <td>--</td>
        <td>--</td>
        <td class="money-font">
            &yen;<%=a.amount%>
        </td>
        <td>--</td>
        <td class="money-font js-show-tips">
            &yen;<%=a.reward%>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>
<#-- 服务顾问业绩--工单详细  end -->
<script type="text/template" id="quickLookTpl">
    <%var t;%>
    <%for(var i in data) {%>
    <% t = data[i];%>
    <li class="js-quick-look"
        data-emp-name="<%=t.empName%>"
        data-target="<%=t.target%>"
        data-emp-id="<%=t.empId%>"><%=t.empName%>
    </li>
    <%}%>
</script>

<script type="text/template" id="gatherQuickLookTpl">
    <%var t;%>
    <%for(var i in data) {%>
    <% t = data[i];%>
    <li class="js-gather-look tag-control"
        data-emp-name="<%=t.empName%>"
        data-user-id="<%=t.userId%>"><%=t.userName%>
    </li>
    <%}%>
</script>

<#--提成规则详细 tips-->
<script type="text/template" id="ruleDetailTpl">
    <div class="rule-detail-tips">
        <div class="angle"></div>
        <% var item;%>
        <div class="tips-box">
            <h4><i class="month"><%=data.month%></i> 提成规则</h4>
            <%if(data.gather) {%>
            <% var t = data.gather.gatherPerfConfigVO;%>
            <p>加点提成：</p>
            <p>新客户到店奖励：<i class="rate"><%=t.newCustomerReward%>元</i><%if(t.orderMinAmount > 0) {%><i class="info">(工单金额满 <i class="rate"><%=t.orderMinAmount%></i> 元以上)</i><%}%>
            </p>
            <p>
                业绩归属奖励：<%=(data.gather.percentageMethod === 0 ? '营业额' : '毛利率')%> * <i class="rate"><%=data.gather.percentageRate > 0 ? data.gather.percentageRate + '%' : 0%></i><i class="info">（<%=t.isContainWashCarOrder == 0 ? '不包含洗车单' : '包含洗车单'%>）</i>
            </p>
            <p>销售之星：按照每<%=t.saleRewardCycle == 0 ? '周' : '月'%><%=(t.salePercentageType == 0 ? '营业额' : '毛利率')%>奖励</p>
            <p>第一名奖励：<i class="rate"><%=t.firstSaleReward%>元</i></p>
            <p>第二名奖励：<i class="rate"><%=t.secondSaleReward%>元</i></p>
            <p>第三名奖励：<i class="rate"><%=t.thirdSaleReward%>元</i></p>
            <%}%>
            <p>服务提成比例：<i class="rate"><%=(data.servicePer > 0 ? data.servicePer + '%' : 0)%></i><%if (data.servicerPer) {%><i class="info">（按照<%=data.serviceMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
            <% if(data.serviceRules) {%>
            <p>特殊规则：</p>
            <% for(i in data.serviceRules) {%>
            <% item = data.serviceRules[i];%>
            <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
            <%}%>
            <%}%>
            <p>配件提成比例：<i class="rate"><%=data.goodsPer > 0 ? data.goodsPer + '%' : 0%></i><%if (data.goodsPer) {%><i class="info">（按照<%=data.goodsMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
            <% if(data.goodsRules) {%>
            <p>特殊规则：</p>
            <% for(i in data.goodsRules) {%>
            <% item = data.goodsRules[i];%>
            <p><%=item.name%>：<i class="rate"><%=item.value%></i></p>
            <%}%>
            <%}%>
            <p>服务顾问提成比例：<i class="rate"><%=data.servicerPer > 0 ? data.servicerPer + '%' : 0%></i><%if (data.servicerPer) {%><i class="info">（按照<%=data.servicerMethod == 0 ? '营业额' : '毛利率'%>提成）</i><%}%></p>
        </div>
    </div>
</script>

<script type="text/template" id="gatherCollectTpl">
    <% var hasType;%>
    <%for(var i in json.data) {%>
    <% var t = json.data[i]%>
    <tr>
        <td>
            <%=(+i + 1) %>
        </td>
        <td class="js-show-tips">
            <%=t.userName%>
        </td>
        <td class="text-r">
            &yen;<%=t.addPointOrderOrGrossAmount%>
            <%if(t.addPointPrefType && !hasType) {%>
            <input type="hidden" class="perf-type" value="<%=t.addPointPrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>
        <td class="money-font">
            &yen;<%=t.businessBeloneReward%>
        </td>
        <td>
            <%=t.newCustomerNum%>
        </td>
        <td class="money-font">
            &yen;<%=t.newCustomerReward%>
        </td>
        <td class="money-font">
            &yen;<%=t.saleStarReward%>
        </td>
        <td class="money-font">
            &yen;<%=t.addPointPrefAmount%>
        </td>
        <td class="js-see-detail"
            data-user-name="<%=t.userName%>"
            data-business-belone="<%=t.businessBeloneReward%>"
            data-new-customer="<%=t.newCustomerReward%>"
            data-sale-start="<%=t.saleStarReward%>"
            data-desc="gather"
            data-current=".gather"
            data-target=".gather-detail" data-user-id="<%=t.userId%>">
            <div class="see-detail">查看详情</div>
        </td>
    </tr>
    <%}%>
    <%if(i == null) {%>
    <tr>
        <td colspan="9">暂无数据</td>
    </tr>
    <%}%>
</script>

<script type="text/template" id="saleStartTpl">
    <%if(json && json.data && json.data) {%>
    <%for(var i in json.data) {%>
    <%var t = json.data[i];%>
    <tr>
        <td>
            <%=(+i + 1) %>
        </td>
        <td>
            <%=t.userName%>
        </td>
        <td>
            <%=t.weekRange%>
        </td>
        <td>
            <%if(t.rank != 0&&t.saleAmount!=0) {%>
            第<%=t.rank%>名
            <%} else {%>
            <%= '-' %>
            <%}%>
        </td>
        <td class="money-font">
            <%if(t.rewardAmount !=0){%>
            &yen;<%=t.rewardAmount||0%>
            <%}else{%>
            <%= '-' %>
            <%}%>
        </td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%}%>
</script>

<script type="text/template" id="newCustomerTpl">
    <%if(json && json.data && json.data.content) {%>
        <%for(var i in json.data.content) {%>
        <%var t = json.data.content[i];%>
    <tr>
        <td>
            <%=json.data.size * (json.data.page - 1) + (+i + 1) %>
        </td>
        <td>
            <%=t.userName%>
        </td>
        <td>
            <%=t.license%>
        </td>
        <td>
            <%=t.confirmTime%>
        </td>
        <td class="money-font">
            &yen;<%=t.orderAmount||0%>
        </td>
        <td class="money-font">
            &yen;<%=t.newCustomerReward||0%>
        </td>
    </tr>
        <%}%>
    <%} else {%>
    <tr>
        <td colspan="6">暂无数据</td>
    </tr>
    <%}%>
</script>

<script type="text/template" id="businessBeloneTpl">
    <% var hasType;%>
    <%if(json && json.data && json.data.content) {%>
        <%for(var i in json.data.content) {%>
        <%var t = json.data.content[i];%>
    <tr>
        <td>
            <%=json.data.size * (json.data.page - 1) + (+i + 1) %>
        </td>
        <td>
            <%=t.userName%>
        </td>
        <td>
            <%=t.license%>
        </td>
        <td>
            <%=t.confirmTime%>
        </td>
        <td class="money-font">
            &yen;<%=t.beloneOrderOrGrossAmount%>
            <%if(t.belonePrefType && !hasType) {%>
            <input type="hidden"  class="perf-type" value="<%=t.belonePrefType%>">
            <% hasType = true;%>
            <%}%>
        </td>
        <td class="money-font">
            &yen;<%=t.businessBeloneReward||0%>
        </td>
    </tr>
        <%}%>
    <%} else {%>
    <tr>
        <td colspan="6">暂无数据</td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/third-plugin/echart/echarts.min.js"></script>
<script src="${BASE_PATH}/static/js/page/report/statistics/staff/perf.js?a1fc1b1fbef877c17e6d851a856cd5f5"></script>

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">

<#include "yqx/layout/footer.ftl" >