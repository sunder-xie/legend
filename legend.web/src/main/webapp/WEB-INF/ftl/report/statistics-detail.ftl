<!--
    create by sky 2016-04-19
    管理报表--统计详情页
-->
<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/statistics-detail.css?01e42f22eb148baa4a32f1c7a31b8a68"/>
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
                        <div class="col-9">
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
                            <div class="quick-search">
                                <span class="qs-item">本周</span>
                                <span class="qs-item">上周</span>
                                <span class="qs-item">本月</span>
                                <span class="qs-item">上月</span>
                            </div>
                        </div>
                        <div class="col-3 right">
                            <button class="btn btn-1" id="searchBtn" type="button">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="rp-tool-row clearfix">
                <a class="rp-export-excel fr" href="#">导出Excel</a>
            </div>
        </div>
        <!-- 搜索头部 end -->

        <!-- 车流统计 start -->
        <div class="rp-panel mb20">
            <div class="rp-panel-head clearfix">
                <h1 class="rp-panel-title"><span class="car-statistic-icon"></span><small>车流统计（1026.3.1 - 2016.3.14）</small></h1>
                <div class="rp-panel-tool fr">
                    <button type="button" class="btn btn-3">单数比例<!--金额比例--></button>
                </div>
            </div>
            <div class="rp-panel-body">
                <div class="flexbox">
                    <div class="flex">
                        <div class="statistics-ico money-ico"></div>
                        <p class="statistics-info">
                            <strong class="ratio">1:3:3</strong></br>
                            消费比例<br/>
                            （高:中:低）
                        </p>
                    </div>
                    <div class="flex">
                        <div class="statistics-ico customer-ico"></div>
                        <p class="statistics-info">
                            <strong>2:1</strong><br/>
                            新老客户比例
                        </p>
                    </div>
                    <div class="flex flex-2">
                        <div class="sum">120</div>
                        <p class="statistics-info">
                            开单总数
                        </p>
                    </div>
                    <div class="flex">
                        <div class="statistics-ico vip-ico"></div>
                        <p class="statistics-info">
                            <strong>10:1</strong><br/>
                            会员比例
                        </p>
                    </div>
                    <div class="flex">
                        <div class="statistics-ico car-ico"></div>
                        <p class="statistics-info">
                            <strong>1:3:7</strong><br/>
                            车辆级别比例
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <!-- 车流统计 end -->
        <div class="rp-panel">
            <div class="rp-panel-head clearfix">
                <h1 class="rp-panel-title"><span class="tendency-icon"></span><small>车流趋势（2016.3.1-2016.3.14）</small></h1>
                <div class="rp-panel-tool fr">
                    <div class="form-item w130">
                        <input type="text" class="rp-input rp-input-icon" placeholder="工单状态"/>
                        <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                    </div>
                </div>
            </div>
            <div class="rp-panel-body">
                <div class="tendency-chart" id="carTendencyChart"></div>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/resources/js/echarts/echarts-all.js?e2bfd1e079dac28b25dd00a2ac458b24"></script>
<script src="${BASE_PATH}/resources/js/report/rp-common.js?a9101f6d273e478d666d3d8625fbd697"></script>
<script src="${BASE_PATH}/resources/js/report/statistics-detail.js?9e9845f349bd3f4c11f9dc902a714e0c"></script>
<#include "layout/footer.ftl">