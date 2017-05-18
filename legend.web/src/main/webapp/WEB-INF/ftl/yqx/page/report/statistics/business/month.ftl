<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/business/month.css?5592cee33fa1152a5efad89ed6b4233e">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/report/statistics/business/performance.css?80e98736510ff942f8784ee388b9156c">

<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fl">
        <h1 class="headline">营业月报</h1>
        <input id="isBPShare" type="hidden" value="${BPSHARE}"/>
        <div class="container search-form" id="searchForm">
            <div class="form-item select-date">
                <label class="font-yahei">选择日期：</label>

                <div class="form-item">
                <#assign aDateTime = .now>
                <#assign aDate = aDateTime?date>
                    <input class="yqx-input date" value="${aDate?string["yyyy-MM"]}"
                           name="month">
                    <span class="fa icon-calendar"></span>
                </div>
            </div>
            <button class="js-search-btn yqx-btn yqx-btn-3 search-btn">
                查询
            </button>
            <div class="quick-filter js-filter-date">
                <label>快速筛选：</label><i data-target="-1"
                   class="js-quick-select">上月</i>,<i class="js-quick-select"
                   data-target="0">本月</i>
            </div>
        </div>
        <div class="container no-border no-margin">
            <div class="tab-box">
                <div class="tab-item js-tab-item current-item"
                     data-desc="operate"
                     data-target=".operate-chart" >经营总览
                </div><div class="tab-item js-tab-item"
                     data-desc="service"
                     data-target=".service-chart" >服务统计
                </div><#if BPSHARE == 'true'><div class="tab-item js-tab-item"
                         data-desc="performance"
                         data-target=".performance-chart" >员工绩效
                    </div><div class="tab-item js-tab-item"
                               data-desc="matter"
                               data-target=".matter-chart" >物料成本
            </div><div class="tab-item js-tab-item"
                       data-desc="no-matter"
                       data-target=".no-matter-chart" >非物料成本
            </div><div class="tab-item js-tab-item"
                       data-desc="profit"
                       data-target=".profit-chart" >盈亏平衡
            </div>
                    <#else><div class="tab-item js-tab-item"
                           data-desc="card"
                           data-target=".card-chart" >卡券统计
                </div><div class="tab-item js-tab-item"
                           data-desc="goods"
                           data-target=".goods-chart" >配件销售统计
                </div><div class="tab-item js-tab-item"
                       data-desc="purchase"
                       data-target=".purchase-chart" >采购统计
                </div><div class="tab-item js-tab-item"
                       data-desc="stock"
                       data-target=".stock-chart" data-tab="0">库存统计
                </div><div class="tab-item js-tab-item"
                     data-desc="brand"
                     data-target=".brand-chart" >品牌车型统计
                </div><div class="tab-item js-tab-item"
                           data-desc="visit"
                           data-target=".visit-chart" >访客统计
                 </div>
                </#if>

            </div>
        </div>
        <div class="chart-wrapper current-chart operate-chart">
            <div class="main operate-main no-border-top">
                <div class="main-header clearfix">
                    <h3 class="fl"><i class="month-icon medal-icon"></i><i class="font-ranking js-show-tips"
                             data-tips="您当月营业额在所有云修门店中的排名，每24小时更新"
                            >云修排名：<i id="ranking"></i></i></h3>
                    <h3 class="fr">
                    <#if BPSHARE=='true'>
                        <a class="margin-left-10" data-target="" title="" target="_blank" href="">面漆总数:<i class="font-money" id="totalSurfaceNum"></i></a>
                        <i data-tips="对当月确认账单的工单中，喷漆和补漆服务的面漆总数" class="question-icon js-show-tips">?</i>
                    </#if><a
                            class="js-link margin-left-10" data-target="营业总额"
                            title="点击查看工单流水表"
                            target="_blank"
                            href="">营业总额：<i class="font-money" id="sumOfBusiness"></i><i class="margin-left-10"></i>毛利：<i class="font-money" id="grossAmount"></i></i><i
                            data-tips="营业总额-配件成本"
                            class="question-icon js-show-tips">?</i><i
                            class="margin-left-10">单车产值：<i class="font-money" id="outputValueByCar"></i></i></a><i
                            data-tips="当月确认账单的工单应收金额总和/当月营业总额除以单数"
                            class="question-icon js-show-tips">?</i><a
                            class="js-link margin-left-10" data-target="采购总额"
                            target="_blank"
                            title="点击查看采购入库表"
                            href="">采购总额：<i class="font-money" id="warehouseInTotalAmount"></i></a><i
                            data-tips="当月蓝字入库单金额总和，未扣除入库退货单金额"
                            class="question-icon js-show-tips">?</i>
                    </h3>
                </div>
                <div class="echarts-box echarts-operate operate-in" id="operateInChart">

                </div><div class="echarts-box echarts-operate operate-out" id="operateOutChart">

                </div>
                 <div class="font-yahei charts-title no-content"
                      style="left: 41px;top: 63px;">
                     <p class="before-line">收款金额：<i id="totalPaidAmountoperateIn"></i><i
                        data-tips="当月所有实收金额总和，含工单+收款单，包含充值、办卡、发券的收款"
                        class="question-icon js-show-tips">?</i></p>
                     <p>会员卡支付：<i id="memberCardPayAmountoperateIn"></i><i
                             data-tips="工单收款实收中使用会员卡支付的金额"
                             class="question-icon js-show-tips">?</i></p>
                 </div>
                <div class="font-yahei charts-title"
                     style="left:438px;top: 63px;">付款金额：<i id="totalPaidAmountoperateOut"></i><i
                        data-tips="当月采购付款及付款单付款金额、会员卡退卡退款金额总和，入库退货退款不会自动扣除，付完退款才会统计在内"
                        class="question-icon js-show-tips">?</i></div>
                <div class="charts-unit operate">单位：元</div>
            </div>
            <div class="main operate-main-2 margin-top-10">
                <div class="main-header">
                    <i class="month-icon chart-icon"></i>营业趋势<i id="trendTime"></i>
                </div>
                <div class="echarts-box echarts-operate-2" id="operateTrendChart">

                </div>
                <div class="charts-title">
                    <i class="js-trend-select current-trend" data-target="month">月</i><i class="separate-line"></i><i
                        class="js-trend-select" data-target="year">年</i>
                </div>
                <div class="charts-unit operate-trend">单位：元</div>
                <div class="echarts-total yye-color" >营业额：<i id="amount0"></i></div>
                <div class="echarts-total ssje-color">收款金额：<i id="amount1"></i></div>
            </div>
        </div>
        <#-- 库存统计 start -->
        <div class="chart-wrapper stock-chart hide mix-wrapper">
            <div class="main stock-main no-border-top">
                 <div class="main-header padding-tb-8 no-border">
                    <h3><i class="month-icon chart-icon"></i>配件类型库存统计
                    </h3>
                     <p style="float:right;margin-right: 16px;">库存总金额：<i class="font-money">&yen;</i><i class="font-money" id="stockTotal"></i>元</p>
                </div>
                <div class="table-box">
                    <table class="yqx-table no-border-lr">
                        <thead>
                        <tr>
                            <th class="text-l p-left width-50">序号</th>
                            <th class="text-l width-70" style="width: 210px;">配件类型</th>
                            <th class="width-70">分类方式</th>
                            <th class="text-r" style="width: 110px;">期初金额</th>
                            <th class="text-r" style="width: 110px;">期末金额</th>
                            <th class="text-r" style="width: 90px">借/进金额</th>
                            <th class="text-r" style="width: 90px">贷/出金额</th>
                            <th class="p-right text-r" style="100px;">周转率</th>
                        </tr>
                        </thead>
                        <tbody id="stockFill">
                        </tbody>
                        <tr class="stock-total hide">
                            <td colspan="3">总计</td>
                            <td class="text-r js-show-tips" id="totalBeginGoodsAmount"></td>
                            <td class="text-r js-show-tips" id="totalEndGoodsAmount"></td>
                            <td class="text-r js-show-tips" id="totalBorrowGoodsAmount"></td>
                            <td class="text-r js-show-tips" id="totalLendGoodsAmount"></td>
                            <td class="p-right text-r" id="rotationRate"></td>
                        </tr>
                    </table>
                    <div class="yqx-page" id="stockPage" style="margin-right: 10px;"></div>
                    <p style="position: absolute;bottom: 19px;left: 10px;">注：周转率 = 贷/出金额 /（期初库存金额+期末库存金额）/ 2 × 100%</p>
                </div>
                <h3 style="position:absolute;bottom: 310px;left: 10px; "><i class="month-icon chart-icon"></i>库存配件类型金额TOP10</h3>
                <div class="echarts-box echarts-stock" id="stockChart">
                </div>
            </div>
            <div class="main stock-main-2 margin-top-10">
                <div class="main-header padding-tb-8 no-border" id="stock2Form">
                    <h3><i class="month-icon chart-icon"></i>配件库存排行<i
                            data-tips="当月每个型号配件的库存排行"
                            class="question-icon js-show-tips">?</i></h3><div class="form-item">
                    <input class="yqx-input input-long"
                           placeholder="配件查询"
                           name="keyword"><button class="yqx-btn yqx-btn-3 js-second-search" data-target="stock|stock2">搜索</button>
                </div>
                </div>
                <div class="table-box" style="width: 100%;">
                <table class="yqx-table no-border-lr" id="stock2Table" freezeColumnNum="2">
                    <thead>
                    <tr>
                        <th class="text-l p-left width-50" rowspan="2">排名</th>
                        <th rowspan="2" style="width: 240px;">配件名称</th>
                        <th colspan="2" style="width: 160px;" class="border-right">期初</th>
                        <th colspan="2" style="width: 160px;" class="border-right">期末</th>
                        <th colspan="2" style="width: 160px;" class="border-right">借/进</th>
                        <th colspan="2" style="width: 160px;" class="border-right">贷/出</th>
                        <th colspan="2" style="width: 160px;">损/益</th>
                        <th rowspan="2" class="width-95">周转率<p>(用金额计算)</p></th>
                        <th rowspan="2" class="width-70">前3个月平均销量</th>
                        <th rowspan="2" class="p-right width-70">建议<p>采购量</p></th>
                    </tr>
                    <tr>
                        <th class="" style="width: 100px;">数量</th>
                        <th class=" border-right" style="width: 120px;">金额</th>
                        <th class="" style="width: 100px;">数量</th>
                        <th class=" border-right" style="width: 120px;">金额</th>
                        <th class="" style="width: 100px;">数量</th>
                        <th class=" border-right" style="width: 120px;">金额</th>
                        <th class="" style="width: 100px;">数量</th>
                        <th class=" border-right" style="width: 120px;">金额</th>
                        <th class="" style="width: 100px;">数量</th>
                        <th class="" style="width: 120px;">金额</th>
                    </tr>
                    </thead>
                    <tbody id="stock2Fill">
                    </tbody>
                </table>
                    </div>
                <div class="yqx-page margin-right-10" id="stock2Page"></div>
            </div>
        </div>
        <#-- 库存统计 end -->

        <#-- 配件销售统计 start -->
        <div class="chart-wrapper goods-chart hide mix-wrapper">
            <div class="main goods-main no-border-top">
                <div class="main-header no-border-tb">
                <div class="sub-tab js-goods-sub current-sub-tab" data-value="std">
                    按标准分类
                </div><div class="sub-tab js-goods-sub left-line" data-value="custom">
                    按自定义分类
                </div>
                    </div>
            <div class="table-box">
                <div class="font-yahei charts-title"
                     style="left: 10px; top: -30px;">配件类型销量<i
                    data-tips="对当月确认账单的工单中的配件，根据淘汽标准分类或门店自定义的分类进行统计"
                     class="question-icon js-show-tips">?</i></div>
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="text-l p-left width-50">序号</th>
                        <th class="text-l">配件类型</th>
                        <th>销售数量</th>
                        <th class="text-l" style="width: 88px;">成本总额(元)</th>
                        <th class="text-r" style="width: 88px;">销售总额(元)</th>
                        <th class="text-r width-50">毛利</th>
                        <th class="text-r p-right">毛利率</th>
                    </tr>
                    </thead>
                    <tbody id="goodsFill">
                    </tbody>
                </table>
                <div class="yqx-page" id="goodsPage"></div>
            </div><div class="echarts-box echarts-goods" id="goodsChart">
            </div>
                <div class="font-yahei charts-title"
                     style="left:610px;top: 60px;">配件类型比例<i data-tips="配件销售额前五的类型，剩余归为“其他”"
                                                             class="question-icon js-show-tips">?</i></div>
            </div>
            <div class="main goods-main-2 margin-top-10">
                <div class="main-header padding-tb-8 no-border" id="goods2Form">
                    <h3><i class="month-icon chart-icon"></i>配件销量排行<i
                            data-tips="当月每个型号配件的销量排行"
                            class="question-icon js-show-tips">?</i></h3><div class="form-item">
                    <input class="yqx-input input-long"
                           placeholder="配件查询"
                           name="keyword"><button class="yqx-btn yqx-btn-3 js-second-search" data-target="goods|goods2">搜索</button>
                </div>
                </div>
                <table class="yqx-table no-border-lr">
                    <thead>
                    <tr>
                        <th class="text-l p-left width-50">排名</th>
                        <th class="text-l" style="width: 240px;">配件名称</th>
                        <th class="text-l" style="width: 120px;">销售数量</th>
                        <th class="text-r" style="width: 90px;">平均成本价格</th>
                        <th class="text-r" style="width: 120px;">销售总额(元)</th>
                        <th class="text-r" style="width: 100px;">毛利</th>
                        <th class="text-r p-right" style="width: 90px;">毛利率</th>
                    </tr>
                    </thead>
                    <tbody id="goods2Fill">
                    </tbody>
                </table>
                <div class="yqx-page margin-right-10" id="goods2Page"></div>
            </div>
        </div>

        <#-- 采购统计 start -->
         <div class="chart-wrapper purchase-chart hide mix-wrapper">
            <div class="main service-main no-border-top">
            <div class="main-header">
                <h3 class="charts-title">配件类型采购量</h3>
                <i style="position: absolute;right: 290px;">采购总金额：<i id="sumOfPurchaseAmount" class="font-money"></i>元</i>
                <h3 class="service-type charts-title" style="margin-left: 500px;">配件类型采购总额比例</h3>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                        <tr>
                            <th class="text-l p-left width-50">排名</th>
                            <th class="text-l">配件类型</th>
                            <th class="text-l" style="width: 64px;">分类方式</th>
                            <th class="text-l" style="width: 64px;">采购数量</th>
                            <th class="text-l" style="width: 88px;">采购总额(元)</th>
                            <th class="text-l" style="width: 64px">销售数量</th>
                            <th class="text-r p-right" style="width: 88px;">销售总额(元)</th>
                        </tr>
                    </thead>
                    <tbody id="purchaseFill">
                    </tbody>
                </table>
                <div class="yqx-page" id="purchasePage"></div>
            </div><div class="echarts-box echarts-purchase" id="purchaseChart">

            </div>
            </div>
            <div class="main purchase-main-2 margin-top-10">
                <div class="main-header padding-tb-8 no-border" id="purchase2Form">
                    <h3 style="display: inline-block;"><i class="month-icon chart-icon"></i>配件采购排行</h3><div class="form-item">
                    <input class="yqx-input input-long"
                           placeholder="配件查询"
                           name="keyword"><button class="yqx-btn yqx-btn-3 js-second-search" data-target="purchase|purchase2">搜索</button>
                </div>
                </div>
                <table class="yqx-table no-border-lr">
                    <thead>
                        <tr>
                            <th class="text-l p-left width-50">排名</th>
                            <th class="text-l" style="width: 231px;">配件名称</th>
                            <th class="text-l" style="width: 80px;">采购数量</th>
                            <th class="text-r" style="width: 100px;">采购总额(元)</th>
                            <th class="text-r p-left" style="width: 80px;">销售数量</th>
                            <th class="text-r" style="width: 100px;">销售总额(元)</th>
                            <th class="text-r" style="width: 80px;">毛利</th>
                            <th class="text-r p-right" style="width: 100px;">毛利率</th>
                        </tr>
                    </thead>
                    <tbody id="purchase2Fill">
                    </tbody>
                </table>
                <div class="yqx-page margin-right-10" id="purchase2Page"></div>
            </div>
        </div>
        <#-- 采购统计 end -->

        <#--服务统计 start -->
        <div class="chart-wrapper service-chart hide mix-wrapper">
            <div class="main service-main no-border-top">
            <div class="main-header">
                <h3 class="charts-title">服务类型统计<i
                        data-tips="对当月确认账单的工单中的服务，根据服务类型进行统计"
                        class="question-icon js-show-tips">?</i></h3>
                <h3 class="service-type charts-title">服务类型比例<i
                        data-tips="服务类型比例：销量前六的类型比例，剩余归为“其他”"
                        class="question-icon js-show-tips">?</i></h3>
                <#if BPSHARE=='true'>
                    <div class="service-tab js-service-tab">
                        <span class="service-current" data-shared="true" data-tab="1">共享服务</span>
                        <span data-shared="false" data-tab="2">非共享服务</span>
                    </div>
                </#if>
            </div>
            <div class="table-box">
                <table class="yqx-table">
                    <thead>
                        <tr>
                            <th class="text-l p-left">排名</th>
                            <th class="text-l">服务类型</th>
                            <th class="text-l">销量(单)</th>
                            <th class="text-r">销售额(元)</th>
                            <#if BPSHARE=='true'>
                                <th class="text-r p-right paint-num">面漆数（面）</th>
                            </#if>
                        </tr>
                    </thead>
                    <tbody id="serviceFill">
                    </tbody>
                </table>
            <#if BPSHARE=='true'>
                <p class="total fl" style="margin-top: 10px;">销售额合计：<i class="money-font" id="totalSpraySaleAmount"></i></p>
            </#if>
                <div class="yqx-page" id="servicePage">
                </div>

            </div><div class="echarts-box echarts-service" id="serviceChart">
            </div>
            </div>
            <div class="main service-main-2 margin-top-10">
                <div class="main-header padding-tb-8 no-border" id="service2Form">
                    <h3><i class="month-icon chart-icon"></i>服务销量排行<i
                            data-tips="当月确认账单的每种服务的销量排行"
                            class="question-icon js-show-tips">?</i></h3><div class="form-item">
                    <input class="yqx-input input-long"
                           placeholder="服务查询"
                           name="keyword"><button class="yqx-btn yqx-btn-3 js-second-search" data-target="service|service2">搜索</button>
                </div>
                </div>
                <table class="yqx-table no-border-lr">
                    <thead>
                        <tr>
                            <th class="text-l p-left width-50">排名</th>
                            <th class="text-l" style="width: 340px">服务名称</th>
                            <th class="text-l" style="width: 120px;">类型</th>
                            <th class="text-l" style="width: 120px;">销量(单)</th>
                            <th class="text-r p-right" style="width: 160px;">销售额(元)</th>
                        </tr>
                    </thead>
                    <tbody id="service2Fill">
                    </tbody>
                </table>
                <div class="yqx-page margin-right-10" id="service2Page"></div>
            </div>
        </div>
        <#-- 品牌车型 统计 start -->
        <div class="chart-wrapper hide brand-chart mix-wrapper">
            <div class="main no-border-top">
                <div class="main-header no-border">
                    <h3 class="fl">车型排行<i
                            data-tips="本月确认账单的有效工单按车型进行统计"
                            class="js-show-tips question-icon">?</i></h3>
                    <h3 class="brand-top fr">品牌Top10<i
                            data-tips="本月确认账单的有效工单按车型品牌进行统计"
                            class="js-show-tips question-icon">?</i></h3>
                </div>
                <div class="table-box">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th class="text-l p-left width-50">序号</th>
                            <th class="text-l">车型</th>
                            <th class="text-l width-50">品牌</th>
                            <th class="text-l width-50">单数</th>
                            <th class="text-r">销售额</th>
                            <th class="text-r">配件成本</th>
                            <th class="text-r p-right">毛利率</th>
                        </tr>
                        </thead>
                        <tbody id="brandFill">
                        </tbody>
                    </table>
                    <div class="yqx-page" id="brandPage">
                    </div>
                </div><div class="echarts-box echarts-brand" id="brandChart">

                </div>
            </div>
        </div>
        <#-- 品牌车型 统计 end -->

        <#-- 卡券统计 start -->
        <div class="chart-wrapper hide card-chart mix-wrapper">
            <div class="main no-border-tb">
                <h3 class="main-header">会员卡</h3>
                <div class="table-box" style="width: 436px; margin-right: 0;">
                    <table class="yqx-table member-card-table" >
                        <thead>
                        <tr>
                            <th class="text-l p-left width-50">序号</th>
                            <th class="text-l" style="width: 100px;">会员卡</th>
                            <th class="text-l width-60">发放(张)</th>
                            <th class="text-r" style="width: 83px;">收款(元)</th>
                            <th class="text-l width-60">退卡(张)</th>
                            <th class="text-r" style="width: 83px;">退款(元)</th>
                        </tr>
                        </thead>
                        <tbody id="memberCardFill"></tbody>
                    </table>
                </div><div class="echarts-box echarts-member-card" id="memberCardChart">

            </div>
            </div>
            <div class="main no-border-tb">
                <h3 class="main-header">计次卡</h3>
                <div class="table-box">
                    <table class="yqx-table combo-card-table">
                        <thead>
                        <tr>
                            <th class="text-l p-left width-60">序号</th>
                            <th class="text-l">计次卡</th>
                            <th class="text-l width-70">发放(张)</th>
                            <th class="text-r p-right">收款(元)</th>
                        </tr>
                        </thead>
                        <tbody id="comboCardFill"></tbody>
                    </table>
                </div><div class="echarts-box echarts-combo-card" id="comboCardChart" style="height: 100px;">

            </div>
            </div>
            <div class="main no-border-tb">
                <h3 class="main-header">优惠券</h3>
                <div class="table-box">
                    <table class="yqx-table coupon-card-table">
                        <thead>
                        <tr>
                            <th class="text-l p-left width-60">序号</th>
                            <th class="text-l">优惠券</th>
                            <th class="text-l width-70">发放(张)</th>
                            <th class="text-l width-70">使用(张)</th>
                            <th class="text-r p-right">抵扣金额(元)</th>
                        </tr>
                        </thead>
                        <tbody id="couponCardFill"></tbody>
                    </table>
                </div><div class="echarts-box echarts-coupon-card" id="couponCardChart" style="height: 50px;">

            </div>
            </div>
        </div>
        <#-- 卡券统计 end -->

        <#-- 访客统计 start -->
        <div class="chart-wrapper hide visit-chart">
            <div class="main no-border-top">
                <div class="main-header">
                    <div class="fr">
                    <a href="javascript: void(0)"
                       target="_blank" title="点击查看结算流水表"
                       data-target="服务总车辆" class="js-link">
                        <label><i class="js-date-month"></i>服务总车辆：<i id="receivedCarNumber"
                        class="font-money"></i>台(<i id="receptionNumber"
                        class="font-money"></i>次)</label></a><a  href="javascript: void(0)"
                            target="_blank"
                            data-target="新建车辆"><label class="margin-left-20">新建车辆：<i id="newCarNumber"
                        class="font-money"></i>台</label></a>
                    </div>
                </div>
                <div class="echarts-box echarts-visit" id="visitChart">

                </div>
            </div>
        </div>
        <#-- 访客统计 end -->
        <#-- 员工绩效 start -->
        <div class="performance-chart chart-wrapper hide">
        <#include "yqx/page/report/statistics/business/performance.ftl"/>
        </div>
        <#-- 员工绩效 end -->

        <#-- 物料成本 start -->
        <div class="matter-chart chart-wrapper hide">
        <#include "yqx/page/report/statistics/business/matter.ftl"/>
        </div>
        <#-- 物料成本 end -->

        <#-- 非物料成本 start -->
        <div class="no-matter-chart chart-wrapper hide">
            <#include "yqx/page/report/statistics/business/no-matter.ftl"/>
        </div>
        <#-- 非物料成本 end -->

        <#-- 盈亏平衡 start -->
        <div class="profit-chart chart-wrappe hide">
            <#include "yqx/page/report/statistics/business/profit.ftl"/>
        </div>
        <#-- 盈亏平衡 end -->
    </div>
    <div class="no-data hide" id="noData"></div>
</div>


<#-- 服务类型销量 -->
<script type="text/template" id="serviceTpl">
<%if(json.data && json.data.content && json.data.content.length) {%>
<% var t%>
<% for(var i in json.data.content) {%>
<% t = json.data.content[i];%>
<tr>
    <td class="text-l p-left"><%=t.rank%></td>
    <td class="js-show-tips text-l"><%=t.serviceCatName%></td>
    <td class="text-l"><%=t.saleNum%></td>
    <td class="js-show-tips text-r"><%=toPrice(t.saleAmount)%></td>
    <#if BPSHARE=='true'>
    <td class="js-show-tips text-r p-right paint-num"><%=t.surfaceNum%></td>
    </#if>
</tr>
<%}}%>
</script>
<#-- 服务销量排行 -->
<script type="text/template" id="service2Tpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var t%>
    <% for(var i in json.data.content) {%>
    <% t = json.data.content[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
        <td class="js-show-tips text-l"><%=t.serviceName%></td>
        <td class="text-l"><%=t.serviceCatName%></td>
        <td class="text-l"><%=t.saleNum%></td>
        <td class="text-r p-right"><%=toPrice(t.saleAmount)%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 配件采购排行 -->
<script type="text/template" id="purchase2Tpl">
    <%if(json.data && json.data.records && json.data.records.length) {%>
    <% var t%>
    <% for(var i in json.data.records) {%>
    <% t = json.data.records[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
        <td class="js-show-tips text-l"><%=t.goodsName%></td>
        <td class="js-show-tips text-l"><%=t.purchaseCount + ' ' + t.unit%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.purchaseAmount)%></td>
        <td class="js-show-tips text-r p-left"><%=t.saleCount + ' ' + t.unit%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.saleAmount)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.profit)%></td>
        <td class="js-show-tips text-r p-right"><%=t.profitRate + '%'%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="9">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 配件类型采购量 -->
<script type="text/template" id="purchaseTpl">
    <%if(json.data && json.data.records && json.data.records.length) {%>
    <% var t%>
    <% for(var i in json.data.records) {%>
    <% t = json.data.records[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
        <td class="js-show-tips text-l"><%=t.categoryName%></td>
        <td class="js-show-tips text-l"><%=t.source%></td>
        <td class="js-show-tips text-l"><%=t.purchaseCount%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.purchaseAmount)%></td>
        <td class="js-show-tips text-l p-left"><%=t.saleCount%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(t.saleAmount)%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 车型排行 -->
<script type="text/template" id="brandTpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var t%>
    <% for(var i in json.data.content) {%>
    <% t = json.data.content[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
        <td class="js-show-tips text-l"><%=t.carSeries%></td>
        <td class="js-show-tips text-l"><%=t.carBrand%></td>
        <td class="js-show-tips text-l"><%=t.receptionNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.income)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.cost)%></td>
        <td class="js-show-tips text-r p-right"><%=t.profitRate + "%"%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 计次卡 -->
<script type="text/template" id="comboCardTpl">
    <%if(json.data && json.data) {%>
    <% var t, handOutSum = 0, reciveSum = 0%>
    <% for(var i in json.data) {%>
    <% t = json.data[i];%>
    <tr>
        <td class="text-l p-left"><%=(+i+1)%></td>
        <td class="js-show-tips text-l"><%=t.comboName%></td>
        <% handOutSum += (+t.handOutNum || 0) %>
        <% reciveSum += (+t.reciveAmount || 0)%>
        <td class="js-show-tips text-l"><%=t.handOutNum%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(t.reciveAmount)%></td>
    </tr>
    <%}%>
    <tr class="table-total">
        <td class="text-l p-left"><%=(json.data.length + 1)%></td>
        <td class="text-l">合计</td>
        <td class="text-l"><%=handOutSum%></td>
        <td class="text-r p-right"><%=toPrice(reciveSum)%></td>
    </tr>
    <%} else {%>
    <tr>
        <td colspan="4">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 会员卡 -->
<script type="text/template" id="memberCardTpl">
    <%if(json.data && json.data) {%>
    <% var t, sum = 0, payAmountSum = 0, amountSum = 0, retreatCardNumsSum = 0, retreatCardAmountSum = 0%>
    <% for(var i in json.data) {%>
    <% t = json.data[i];%>
    <% sum += +t.handOutNum%>
    <tr>
        <td class="text-l p-left"><%=(+i+1)%></td>
        <td class="js-show-tips text-l"><%=t.cardName%></td>
        <% payAmountSum += (+t.payAmount || 0)%>
        <% amountSum += (+t.amount || 0)%>
        <% retreatCardNumsSum += (+t.retreatCardNums || 0)%>
        <% retreatCardAmountSum += (+t.retreatCardAmount || 0)%>
        <td class="js-show-tips text-l"><%=t.handOutNum%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(t.payAmount)%></td>
        <td class="js-show-tips text-l"><%=t.retreatCardNums%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(t.retreatCardAmount)%></td>
    </tr>
    <%}%>
    <tr class="table-total">
        <td class="text-l p-left"><%=(json.data.length + 1)%></td>
        <td class="text-l">合计</td>
        <td class="text-l"><%=sum%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(payAmountSum)%></td>
        <td class="js-show-tips text-l"><%=retreatCardNumsSum%></td>
        <td class="js-show-tips text-r p-right"><%=toPrice(retreatCardAmountSum)%></td>
    </tr>
    <%} else {%>
    <tr>
        <td colspan="3">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 优惠券 -->
<script type="text/template" id="couponCardTpl">
    <%if(json.data && json.data) {%>
    <% var t, handOutSum = 0, useSum = 0, discountSum = 0%>
    <% for(var i in json.data) {%>
    <% t = json.data[i];%>
    <tr>
        <td class="text-l p-left"><%=(+i+1)%></td>
        <td class="js-show-tips text-l"><%=t.couponName%></td>
        <% useSum += (t.usedNum || 0) %>
        <td class="js-show-tips text-l"><%=t.handOutNum%></td>
        <% handOutSum += (t.handOutNum || 0) %>
        <td class="js-show-tips text-l"><%=t.usedNum%></td>
        <% discountSum += (t.discountAmount || 0)%>
        <td class="js-show-tips text-r p-right"><%=toPrice(t.discountAmount)%></td>
    </tr>

    <%}%>
    <tr class="table-total">
        <td class="text-l p-left"><%=(json.data.length + 1)%></td>
        <td class="text-l">合计</td>
        <td class="text-l"><%=handOutSum%></td>
        <td class="text-l"><%=useSum%></td>
        <td class="text-r p-right"><%=toPrice(discountSum)%></td>
    </tr>
    <%} else {%>
    <tr>
        <td colspan="5">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 配件销量排行 -->
<script type="text/template" id="goods2Tpl">
    <%if(json.data && json.data.resultList && json.data.resultList.length) {%>
    <% var t%>
    <% for(var i in json.data.resultList) {%>
    <% t = json.data.resultList[i];%>
    <tr>
        <td class="text-l p-left"><%=(+t.rank + 1)%></td>
        <#--配件名称-->
        <td class="js-show-tips text-l"><%=t.goodsName%></td>
        <#-- 销售额 -->
        <td class="js-show-tips text-l"><%=t.saleCount + ' ' + t.unit%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.avgCost)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.income)%></td>
        <td class="js-show-tips text-r"><%=t.profit%></td>
        <td class="js-show-tips text-r p-right"><%=t.profitRate + "%"%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>
<#-- 配件类型销量 -->
<script type="text/template" id="goodsTpl">
    <%if(json.data && json.data.resultList && json.data.resultList.length) {%>
    <% var t%>
    <% for(var i in json.data.resultList) {%>
    <% t = json.data.resultList[i];%>
    <tr>
        <td class="text-l p-left"><%=(+t.rank + 1)%></td>
    <#--配件名称-->
        <td class="js-show-tips text-l"><%=t.catName%></td>
        <#-- 销量 -->
        <td class="js-show-tips"><%=t.saleCount%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.cost)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.income)%></td>
        <td class="js-show-tips text-r"><%=t.profit%></td>
        <td class="js-show-tips text-r p-right"><%=t.profitRate + "%"%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>
<#--配件类型库存-->
<script type="text/template" id="stockTpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var t%>
    <% for(var i in json.data.content) {%>
    <% t = json.data.content[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
    <#--配件名称-->
        <td class="js-show-tips text-l"><%=t.goodsCateName%></td>
    <#-- 销量 -->
        <td class="js-show-tips"><%=t.goodsCateWay%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.beginGoodsAmount)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.endGoodsAmount)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.borrowGoodsAmount)%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.lendGoodsAmount)%></td>
        <td class="js-show-tips text-r p-right"><%=t.rotationRate%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>
<#--配件销量排行-->
<script type="text/template" id="stock2Tpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var t%>
    <% for(var i in json.data.content) {%>
    <% t = json.data.content[i];%>
    <tr>
        <td class="text-l p-left"><%=t.rank%></td>
    <#--配件名称-->
        <td class="js-show-tips text-l"><%=t.goodsName%></td>
    <#-- 销量 -->
        <td class="js-show-tips "><%=t.beginGoodsNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.beginGoodsAmount)%></td>
        <td class="js-show-tips "><%=t.endGoodsNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.endGoodsAmount)%></td>
        <td class="js-show-tips "><%=t.borrowGoodsNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.borrowGoodsAmount)%></td>
        <td class="js-show-tips "><%=t.lendGoodsNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.lendGoodsAmount)%></td>
        <td class="js-show-tips "><%=t.profitGoodsNumber%></td>
        <td class="js-show-tips text-r"><%=toPrice(t.profitGoodsAmount)%></td>
        <td class="js-show-tips"><%=t.rotationRate%></td>
        <td class="js-show-tips text-r"><%=t.averageNumber%></td>
        <td class="js-show-tips p-right"><%=t.suggestGoodsNumber%> <%=t.measureUnit%></td>
    </tr>
    <%}%>
    <%} else {%>
    <tr>
        <td colspan="15">暂无数据</td>
    </tr>
    <%}%>
</script>
<script src="${BASE_PATH}/static/third-plugin/echart/echarts.min.js"></script>
<script src="${BASE_PATH}/static/js/common/tableFreeze.js?458932d19cef0aa190d6264514c2a7ec"></script>
<script src="${BASE_PATH}/static/js/page/report/statistics/business/month.js?89a3114b7c2e473ef9f9abe7b7709364"></script>
<script src="${BASE_PATH}/static/js/page/report/statistics/business/sheet.js?ecfbe4bc610b64706d32259483f45182"></script>
<#-- .yqx-page 模板 -->
<script type="text/template" id="pageTpl">
    <div class="yqx-page-inner">
        <span class="disabled yqx-page-count">共<%=args.itemSize%>条记录</span><a
            href="javascript:void(0)"
        <%if(args.current <= 1) {%>
        title="已在第一页"
        <%}%>
        class="<%if(args.current <= 1) {%> disabled <%}%>yqx-page-prev">上一页</a><a href="javascript:;"
        <%if(args.current == args.pageCount || args.pageCount == 0) {%>
        title="已在最后一页"
        <%}%>
        class="<%if(args.current == args.pageCount || args.pageCount == 0){%> disabled <%}%>yqx-page-next">下一页</a>
    </div>
</script>

<#include "yqx/layout/footer.ftl">
