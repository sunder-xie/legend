<#--头部引入 start-->
<#include "layout/header.ftl" >
<#--头部引入 end-->
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/amount.css?aa5240048d8ca39f71bceb4c33e4c183"/>
<div class="wrapper clearfix">
    <div class="aside">
        左边导航栏，请查看report/index.ftl
    </div>
    <#--主体开始-->
    <div class="main" id="amount">
        <!-- 搜索头部 start -->
        <div class="main-head">
            <form id="searchForm">
                <div class="search-form">
                    <div class="show-grid">
                        <div class="col-6">
                            <div class="form-label">开单时间：</div>
                            <div class="form-item w150">
                                <input type="text" id="d111" class="rp-input rp-input-icon"
                                       onfocus="WdatePicker({maxDate: '#F{$dp.$D(\'d222\')||\'%y-%M-%d\'}'})" />
                                <span class="rp-suffix-icon rp-calendar-icon"></span>
                            </div>
                            <span>&nbsp;-&nbsp;</span>
                            <div class="form-item w150">
                                <input type="text" id="d222" class="rp-input rp-input-icon"
                                       onfocus="WdatePicker({minDate: '#F{$dp.$D(\'d111\')}',maxDate: '%y-%M-%d'})" />
                                <span class="rp-suffix-icon rp-calendar-icon"></span>
                            </div>
                        </div>
                        <div class="col-3">
                            <div class="form-label">车牌：</div>
                            <div class="form-item w150">
                                <input type="text" class="rp-input"/>
                            </div>
                        </div>
                        <div class="col-3 right">
                            <div class="form-label">车主：</div>
                            <div class="form-item w150">
                                <input type="text" class="rp-input"/>
                            </div>
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="col-8">
                            <div class="form-item w130">
                                <input type="text" class="rp-input rp-input-icon" placeholder="工单类型"/>
                                <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                            </div>
                            <div class="form-item w130">
                                <input type="text" class="rp-input rp-input-icon" placeholder="服务顾问"/>
                                <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                            </div>
                            <div class="form-item w130">
                                <input type="text" class="rp-input rp-input-icon" placeholder="维修工"/>
                                <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                            </div>
                            <div class="form-item w130">
                                <input type="text" class="rp-input rp-input-icon" placeholder="工单状态"/>
                                <span class="rp-suffix-icon rp-arrow-down-icon"></span>
                            </div>
                        </div>
                        <div class="col-4 right">
                            <button class="btn btn-1" id="searchBtn" type="button">查询</button>
                        </div>
                    </div>
                </div>
            </form>
            <div class="rp-tool-row clearfix">
                <a class="rp-export-excel fr" href="#"><span class="rp-export-icon"></span>导出Excel</a>
            </div>
        </div>
        <!-- 搜索头部 end -->
        <#--数据 start-->
        <div class="main-body clearfix" id="mainAmount">
        <#--收益-->
        <div class="rp-panel info">
            <div class="rp-panel-head">
                <h1 class="rp-panel-title">收益(2016.3.1-2016.3.21)</h1>
                <div class="dropdown fr">
                    <p class="dropdown-title"><span class="rp-plus-icon">+</span></p>
                    <ul class="dropdown-menu check-box ">
                        <li>
                            <label for="cb1">
                                <input id="cb1" type="checkbox" data-ref=""/>营业应收
                            </label>
                        </li>
                        <li>
                            <label for="cb2">
                                <input id="cb2" type="checkbox" data-ref=""/>实收
                            </label>
                        </li>
                        <li>
                            <label for="cb3">
                                <input id="cb3" type="checkbox" data-ref=""/>挂账
                            </label>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="rp-panel-body">
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>营业应收</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr data-url="http://localhost:8080/legend/report">
                        <td>物料成本</td>
                        <td>15,000</td>
                    </tr>
                    <tr>
                        <td data-url="">毛利</td>
                        <td>9,000</td>
                    </tr>
                    </tbody>
                </table>
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>实收</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr data-url="">
                        <td>实收</td>
                        <td>15,000</td>
                    </tr>
                    <tr data-url="">
                        <td>会员卡结算</td>
                        <td>9,000</td>
                    </tr>
                    <tr data-url="">
                        <td>优惠卡结算</td>
                        <td>400</td>
                    </tr>
                    <tr data-url="">
                        <td>减免</td>
                        <td>100</td>
                    </tr>
                    </tbody>
                </table>
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>挂账</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>


        </div>

        <#--支出-->
            <div class="rp-panel info">
                <div class="rp-panel-head">
                    <h1 class="rp-panel-title">支出(2016.3.1-2016.3.21)</h1>
                    <div class="dropdown fr">
                        <p class="dropdown-title"><span class="rp-plus-icon">+</span></p>
                        <ul class="dropdown-menu check-box">
                            <li>
                                <label for="cb1">
                                    <input id="cb1" type="checkbox" data-ref=""/>采购总额
                                </label>
                            </li>
                        </ul>
                    </div>
                </div>
            <div class="rp-panel-body">
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>采购总额</th>
                        <th>5100</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr data-url="">
                        <td>已付</td>
                        <td>15,000</td>
                    </tr>
                    <tr data-url="">
                        <td>待付</td>
                        <td>9,000</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            </div>
        <#--库存-->
            <div class="rp-panel info">
                <div class="rp-panel-head">
                    <h1 class="rp-panel-title">库存(2016.3.1-2016.3.21)</h1>
                    <div class="dropdown fr">
                        <p class="dropdown-title"><span class="rp-plus-icon">+</span></p>
                        <ul class="dropdown-menu check-box">
                            <li>
                                <label for="cb">
                                    <input id="cb" type="checkbox" data-ref=""/>入库总额
                                </label>
                            </li>
                            <li>
                                <label for="cb">
                                    <input id="cb" type="checkbox" data-ref=""/>出库总额
                                </label>
                            </li>
                            <li>
                                <label for="cb">
                                    <input id="cb" type="checkbox" data-ref=""/>期初总额
                                </label>
                            </li>
                            <li>
                                <label for="cb">
                                    <input id="cb" type="checkbox" data-ref=""/>期末总额
                                </label>
                            </li>
                        </ul>
                    </div>
                </div>
            <div class="rp-panel-body">
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>入库总额</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr data-url="">
                        <td>正常入库</td>
                        <td>15,000</td>
                    </tr>
                    <tr data-url="">
                        <td>采购退货入库</td>
                        <td>9,000</td>
                    </tr>
                    </tbody>
                </table>
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>出库总额</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr data-url="">
                        <td>正常出库</td>
                        <td>15,000</td>
                    </tr>
                    <tr data-url="">
                        <td>销售退货出库</td>
                        <td>9,000</td>
                    </tr>
                    </tbody>
                </table>

                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>期初总额</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
                <table class="table collect amount">
                    <thead>
                    <tr>
                        <th>期末总额</th>
                        <th>24,000.00</th>
                    </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
            </div>

        </div>
    <#--数据 end-->
<#--营业趋势start-->
        <div class="rp-panel">
            <div class="rp-panel-head clearfix">
                <h1 class="rp-panel-title"><span class="tendency-icon"></span><small>期间营业趋势（2016.3.1-2016.3.14）</small></h1>
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
<#--营业趋势end-->
</div>
<#--尾部引入start-->
<#include "layout/footer.ftl" >
<#--尾部引入end-->
<#--脚本引入start-->
<script type="text/javascript" src="${BASE_PATH}/resources/js/report/amount.js?7efcc472d177405e7de02c3b17e175ab"></script>
<#--脚本引入end-->