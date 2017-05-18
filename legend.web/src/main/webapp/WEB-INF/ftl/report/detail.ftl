<#include "layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/detail.css?c83ca41d8cc7043ed8559e873eae5a27"/>
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
        <!-- 表单部分 start -->
        <div class="rp-panel">
            <div class="rp-panel-head clearfix">
                <div class="dropdown fr" id="refTable">
                    <p class="dropdown-title"><span class="rp-plus-icon">+</span>添加</p>
                    <ul class="dropdown-menu">
                        <li><label for="cb1"><input id="cb1" type="checkbox" checked>工单编号</label></li>
                        <li><label for="cb2"><input id="cb2" type="checkbox" checked>工单类型</label></li>
                        <li><label for="cb3"><input id="cb3" type="checkbox" checked>车牌</label></li>
                        <li><label for="cb4"><input id="cb4" type="checkbox" checked>车主/客户</label></li>
                        <li><label for="cb5"><input id="cb5" type="checkbox" checked>开单时间</label></li>
                        <li><label for="cb6"><input id="cb6" type="checkbox" checked>工单状态</label></li>
                        <li><label for="cb7"><input id="cb7" type="checkbox" checked>销售员</label></li>
                        <li><label for="cb8"><input id="cb8" type="checkbox" checked>维修工</label></li>
                        <li><label for="cb9"><input id="cb9" type="checkbox" checked>实际应收金额</label></li>
                        <li><label for="cb10"><input id="cb10" type="checkbox" checked>实收金额</label></li>
                        <li><label for="cb11"><input id="cb11" type="checkbox" checked>挂账金额</label></li>
                        <li><label for="cb12"><input id="cb12" type="checkbox" checked>物料成本</label></li>
                        <li><label for="cb13"><input id="cb13" type="checkbox" checked>工时</label></li>
                        <li><label for="cb14"><input id="cb14" type="checkbox" checked>毛利</label></li>
                        <li><label for="cb15"><input id="cb15" type="checkbox" checked>毛利率</label></li>
                    </ul>
                </div>
            </div>
            <div class="rp-panel-body">
                <div class="scroller" id="tableContent"></div>
            </div>
        </div>
        <!-- 表单部分 end -->
    </div>
</div>

<script id="tableTpl" type="text/html">
    <%  var idx = 0; %>
    <table class="table" id="tableDetail">
        <thead class="highlight-head">
        <tr>
            <% if (isShow[idx++]) { %>
            <th class="w80">工单编号</th>
            <% } if (isShow[idx++]) { %>
            <th class="w100">工单类型</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">车牌</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">车主/客户</th>
            <% } if (isShow[idx++]) { %>
            <th class="w100">开单时间</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">工单状态</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">销售员</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">维修工</th>
            <% } if (isShow[idx++]) { %>
            <th class="w100 right">实际应收金额</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80 right">实收金额</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80 right">挂账金额</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80 right">物料成本</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80">工时</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80 right">毛利</th>
            <% } if (isShow[idx++]) { %>
            <th class="w80 right">毛利率</th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <% idx = 0; %>
        <tr>
            <% if (isShow[idx++]) { %>
            <td>1</td>
            <% } if (isShow[idx++]) { %>
            <td>维修</td>
            <% } if (isShow[idx++]) { %>
            <td>浙A23456</td>
            <% } if (isShow[idx++]) { %>
            <td>淘汽档口</td>
            <% } if (isShow[idx++]) { %>
            <td>2016年04月15日 12:00:00</td>
            <% } if (isShow[idx++]) { %>
            <td>已结算</td>
            <% } if (isShow[idx++]) { %>
            <td>李海忠</td>
            <% } if (isShow[idx++]) { %>
            <td>李海忠</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">2000元</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">1000元</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">1000元</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">500元</td>
            <% } if (isShow[idx++]) { %>
            <td>4</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">1500元</td>
            <% } if (isShow[idx++]) { %>
            <td class="right">75%</td>
            <% } %>
        </tr>
        </tbody>
        <tfoot>
        <% idx = 0; %>
        <tr>
            <% if (isShow[idx++]) { %>
            <th>总计</th>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } if (isShow[idx++]) { %>
            <td></td>
            <% } %>
        </tr>
        </tfoot>
    </table>
</script>

<script src="${BASE_PATH}/resources/js/report/detail.js?e34381c30142d065b34af595c9c60db8"></script>
<#include "layout/footer.ftl" >