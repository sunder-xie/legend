<!--
    create by sky 2016-04-19
    管理报表--服务类型分析
-->
<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/type-analysis.css?5d903223ab2bf358f56a37dfb2412c09"/>
<div class="wrapper clearfix">
    <div class="aside">
        左边导航栏，请查看report/index.ftl
    </div>
    <div class="main">
        <!-- 搜索头部 start -->
        <div class="main-head">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid">
                        <div class="col-6">
                            <div class="form-label">统计时间：</div>
                            <div class="form-item w150">
                                <input type="text" id="d111" class="rp-input rp-input-icon"
                                       onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'d222\')||\'%y-%M-%d\'}'})" />
                                <span class="rp-suffix-icon rp-calendar-icon"></span>
                            </div>
                            <span>&nbsp;至&nbsp;</span>
                            <div class="form-item w150">
                                <input type="text" id="d222" class="rp-input rp-input-icon"
                                       onfocus="WdatePicker({minDate: '#F{$dp.$D(\'d111\')}',maxDate: '%y-%M-%d'})" />
                                <span class="rp-suffix-icon rp-calendar-icon"></span>
                            </div>
                        </div>
                        <div class="col-4 quick-search">
                            <span class="qs-item">本周</span>
                            <span class="qs-item">上周</span>
                            <span class="qs-item">本月</span>
                            <span class="qs-item">上月</span>
                        </div>
                        <div class="col-2 right">
                            <button class="btn btn-1" id="searchBtn" type="button">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="rp-tool-row clearfix">
                <div class="rp-tool fr">
                    <a href="javascript:;" class="tab-card-type tab-control" data-ref="cardType">卡片</a>
                    <span>&nbsp;|&nbsp;</span>
                    <a href="javascript:;" class="tab-list-type tab-control" data-ref="listType">列表</a>
                    <span>&nbsp;|&nbsp;</span>
                    <a class="rp-export-excel" href="#">导出Excel</a>
                </div>
            </div>
        </div>
        <!-- 搜索头部 end -->

        <div class="mb20 clearfix">
            <!-- 销售额比例 start -->
            <div class="rp-panel col-6 fl">
                <div class="rp-panel-head">
                    <h1 class="rp-panel-title">
                        <span class="sale-ratio-icon"></span>
                        <small>销售额比例（2016.3.1-2016.3.14）</small>
                    </h1>
                </div>
                <div class="rp-panel-body">
                    <div class="h345" id="saleRatioChart"></div>
                </div>
            </div>
            <!-- 销售额比例 end -->
            <!-- 接车台次比例 start -->
            <div class="rp-panel col-6 fl">
                <div class="rp-panel-head">
                    <h1 class="rp-panel-title">
                        <span class="times-ratio-icon"></span>
                        <small>接车台次比例（2016.3.1-2016.3.14）</small>
                    </h1>
                </div>
                <div class="rp-panel-body">
                    <div class="h345" id="timesRatioChart"></div>
                </div>
            </div>
            <!-- 接车台次比例 end -->
        </div>

        <!-- 服务销量趋势 start -->
        <div class="rp-panel">
            <div class="rp-panel-head clearfix">
                <h1 class="rp-panel-title">
                    <span class="tendency-icon"></span>
                    <small>车流趋势（2016.3.1-2016.3.14）</small></h1>
                <div class="rp-panel-tool fr">
                    <div class="form-item w130">
                        <input type="text" class="rp-input rp-input-icon" placeholder="工单状态"/>
                        <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                    </div>
                </div>
            </div>
            <div class="rp-panel-body">
                <div class="tendency-chart" id="saleTendencyChart"></div>
            </div>
        </div>
    </div>
    <!-- 服务销量趋势 end -->
</div>
<script src="${BASE_PATH}/resources/js/echarts/echarts-all.js?e2bfd1e079dac28b25dd00a2ac458b24"></script>
<script src="${BASE_PATH}/resources/js/report/rp-common.js?a9101f6d273e478d666d3d8625fbd697"></script>
<script src="${BASE_PATH}/resources/js/report/type-analysis.js?c22389ce77a1bbb8f4a1c8f2b6e4e8a3"></script>
<#include "layout/footer.ftl">