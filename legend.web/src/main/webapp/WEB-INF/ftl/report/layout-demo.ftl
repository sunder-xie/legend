<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/js/lib/syntaxhighlighter_3.0.83/styles/shCore.css?604c1b198731fd52673cd931f426e3a9"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/js/lib/syntaxhighlighter_3.0.83/styles/shThemeDefault.css?d4d54a01169ef8077807f15f72a65aa2"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<style>
    .mb10{margin-bottom: 10px;}
    .copy-range{position: relative;margin-top: 10px;padding-top: 10px;border-top: 2px solid #ddd;overflow: hidden;}
</style>
<div class="wrapper">
    <!-- 嵌入框 start -->
    <div class="rp-panel mb10">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">嵌入框</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display">
                <div class="rp-panel">
                    <div class="rp-panel-head">
                        <h1 class="rp-panel-title">这是头</h1>
                    </div>
                    <div class="rp-panel-body">
                        这是内容
                    </div>
                    <div class="rp-panel-foot">
                        这是底
                    </div>
                </div>
            </div>

            <div class="copy-range js-copy-range">
                <pre class="brush: html">
                    <div class="rp-panel">
                        <div class="rp-panel-head">
                            <h1 class="rp-panel-title">这是头</h1>
                        </div>
                        <div class="rp-panel-body">
                            这是内容
                        </div>
                        <div class="rp-panel-foot">
                            这是底
                        </div>
                    </div>
                </pre>
            </div>
        </div>
    </div>
    <!-- 嵌入框 end -->

    <!-- 主体搜索头部 start -->
    <div class="rp-panel mb10">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">主体搜索头部</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display clearfix">
                <div class="main">
                    <div class="main-head">
                        <form id="searchForm">
                            <div class="search-form clearfix">
                                <button class="btn btn-1 fr" id="searchBtn" type="button">查询</button>
                                <div class="form-label">每日统计：</div>
                                <div class="form-item">
                                    <input type="text" class="rp-input rp-input-icon" onfocus="WdatePicker()"/>
                                    <span class="rp-suffix-icon rp-calendar-icon"></span>
                                </div>
                            </div>
                        </form>
                        <div class="rp-tool-row clearfix">
                            <a class="rp-export-excel fr" href="#"><span class="rp-export-icon"></span>导出Excel</a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="copy-range js-copy-range">
                <pre class="brush: html">
                    <div class="main">
                        <div class="main-head">
                            <form id="searchForm">
                                <div class="search-form clearfix">
                                    <button class="btn btn-1 fr" id="searchBtn" type="button">查询</button>
                                    <div class="form-label">每日统计：</div>
                                    <div class="form-item">
                                        <input type="text" class="rp-input rp-input-icon" onfocus="WdatePicker()"/>
                                        <span class="rp-suffix-icon rp-calendar-icon"></span>
                                    </div>
                                </div>
                            </form>
                            <div class="rp-tool-row clearfix">
                                <a class="rp-export-excel fr" href="#"><span class="rp-export-icon"></span>导出Excel</a>
                            </div>
                        </div>
                        <!-- 其它主体内容（比如，嵌套框） -->
                    </div>
                </pre>
            </div>
        </div>
    </div>
    <!-- 主体搜索头部 end -->

    <!-- 输入框样式 start -->
    <div class="rp-panel mb10">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">输入框</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display">
                <div class="form-label">每日统计：</div>
                <div class="form-item">
                    <input type="text" class="rp-input rp-input-icon" onfocus="WdatePicker()"/>
                    <span class="rp-suffix-icon rp-calendar-icon"></span>
                </div>
            </div>

            <div class="copy-range js-copy-range">
                <pre class="brush: html">
                    <div class="form-label">每日统计：</div>
                    <div class="form-item">
                        <input type="text" class="rp-input rp-input-icon" onfocus="WdatePicker()"/>
                        <span class="rp-suffix-icon rp-calendar-icon"></span>
                    </div>
                </pre>
            </div>
        </div>
    </div>
    <!-- 输入框样式 end -->

    <!-- 下拉列表样式 start -->
    <div class="rp-panel mb10">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">下拉列表</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display clearfix">
                <div class="dropdown fr">
                    <p class="dropdown-title"><span class="rp-plus-icon">+</span>添加</p>
                    <ul class="dropdown-menu">
                        <li><label for="cb1"><input id="cb1" type="checkbox" data-ref="order" checked/>工单</label></li>
                        <li><label for="cb2"><input id="cb2" type="checkbox" data-ref="revenue" checked/>营收</label></li>
                        <li><label for="cb3"><input id="cb3" type="checkbox" data-ref="purchase" checked/>采购</label></li>
                        <li><label for="cb4"><input id="cb4" type="checkbox" data-ref="recharge" checked/>会员充值</label></li>
                        <li><label for="cb5"><input id="cb5" type="checkbox" data-ref="account" checked/>账务</label></li>
                    </ul>
                </div>
            </div>

            <div class="copy-range js-copy-range">
                <pre class="brush: html">
                    <div class="dropdown fr">
                        <p class="dropdown-title"><span class="rp-plus-icon">+</span>添加</p>
                        <ul class="dropdown-menu">
                            <li><label for="cb1"><input id="cb1" type="checkbox" data-ref="order" checked/>工单</label></li>
                            <li><label for="cb2"><input id="cb2" type="checkbox" data-ref="revenue" checked/>营收</label></li>
                            <li><label for="cb3"><input id="cb3" type="checkbox" data-ref="purchase" checked/>采购</label></li>
                            <li><label for="cb4"><input id="cb4" type="checkbox" data-ref="recharge" checked/>会员充值</label></li>
                            <li><label for="cb5"><input id="cb5" type="checkbox" data-ref="account" checked/>账务</label></li>
                        </ul>
                    </div>
                </pre>
            </div>
        </div>
    </div>
    <!-- 下拉列表样式 end -->

    <!-- table样式 start -->
    <div class="rp-panel mb10">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">表单</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display">
                <table class="table table-hover" id="order">
                    <caption>
                        <h1 class="table-title">工单</h1>
                        <span class="date-title">2016.03.01</span>
                    </caption>
                    <thead>
                    <tr>
                        <th>类型</th>
                        <th>今日接单</th>
                        <th>今日结算</th>
                        <th>累计未结算车辆</th>
                        <th>今日新建预约</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>洗车</td>
                        <td>10</td>
                        <td>10</td>
                        <td>0</td>
                        <td>/</td>
                    </tr>
                    <tr>
                        <td>洗车</td>
                        <td>10</td>
                        <td>10</td>
                        <td>0</td>
                        <td>/</td>
                    </tr>
                    <tr>
                        <td>洗车</td>
                        <td>10</td>
                        <td>10</td>
                        <td>0</td>
                        <td>/</td>
                    </tr>
                    </tbody>
                    <tfoot>
                    <tr>
                        <th>总计</th>
                        <td>10</td>
                        <td>10</td>
                        <td>0</td>
                        <td>0</td>
                    </tr>
                    </tfoot>
                </table>
            </div>

            <div class="copy-range js-copy-range">
                <pre class="brush: html">
                    <table class="table table-hover" id="order">
                        <caption>
                            <h1 class="table-title">工单</h1>
                            <span class="date-title">2016.03.01</span>
                        </caption>
                        <thead>
                        <tr>
                            <th>类型</th>
                            <th>今日接单</th>
                            <th>今日结算</th>
                            <th>累计未结算车辆</th>
                            <th>今日新建预约</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>洗车</td>
                            <td>10</td>
                            <td>10</td>
                            <td>0</td>
                            <td>/</td>
                        </tr>
                        <tr>
                            <td>洗车</td>
                            <td>10</td>
                            <td>10</td>
                            <td>0</td>
                            <td>/</td>
                        </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th>总计</th>
                            <td>10</td>
                            <td>10</td>
                            <td>0</td>
                            <td>0</td>
                        </tr>
                        </tfoot>
                    </table>
                </pre>
            </div>
        </div>
    </div>
    <!-- table样式 end -->

    <!-- table高亮头样式 start -->
    <div class="rp-panel">
        <div class="rp-panel-head">
            <h1 class="rp-panel-title">表单高亮头</h1>
        </div>
        <div class="rp-panel-body">
            <div class="display">
                <table class="table collect">
                    <thead class="highlight-head">
                    <tr>
                        <th>今日收款</th>
                        <th>今日付款</th>
                        <th>今日结算</th>
                        <th>单车产值</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>29,000</td>
                        <td>12,000</td>
                        <td>30单</td>
                        <td>340</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="copy-range-js-copy-range">
                <pre class="brush: html">
                    <table class="table collect">
                        <thead class="highlight-head">
                        <tr>
                            <th>今日收款</th>
                            <th>今日付款</th>
                            <th>今日结算</th>
                            <th>单车产值</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>29,000</td>
                            <td>12,000</td>
                            <td>30单</td>
                            <td>340</td>
                        </tr>
                        </tbody>
                    </table>
                </pre>
            </div>
        </div>
    </div>
    <!-- table高亮头样式 end -->
</div>
<#include "layout/footer.ftl" >
<script src="${BASE_PATH}/resources/js/lib/jquery-zclip-master/jquery.zclip.js?a482e1500fb06553160312c7e3c9c681"></script>
<script src="${BASE_PATH}/resources/js/lib/syntaxhighlighter_3.0.83/scripts/shCore.js?488ca2f56c37f84283fc9be63219304f"></script>
<script src="${BASE_PATH}/resources/js/lib/syntaxhighlighter_3.0.83/scripts/shBrushXml.js?ba290ac0111d2c3f8e1ce36fbaf6a239"></script>
<script src="${BASE_PATH}/resources/js/lib/syntaxhighlighter_3.0.83/scripts/shBrushJScript.js?cdae918e2156986f76ada6d301c45f27"></script>
<!-- syntaxhighlighter start -->
<script type="text/javascript">
        SyntaxHighlighter.all();
</script>
<!-- syntaxhighlighter end -->
<!-- zeroClipboard start -->
<script type="text/javascript">
    $(function() {
        var $copyRange = $('.js-copy-range'),
            $pre = $copyRange.find('pre');

        $copyRange.attr('title', '点击复制该区域');
        $pre.zclip({
            path: BASE_PATH + '/resources/js/lib/jquery-zclip-master/ZeroClipboard.swf',
            copy: $pre.html(),
            afterCopy: function() {
                alert('已复制到剪切板');
            }
        });
    })
</script>
<!-- zeroClipboard end -->
