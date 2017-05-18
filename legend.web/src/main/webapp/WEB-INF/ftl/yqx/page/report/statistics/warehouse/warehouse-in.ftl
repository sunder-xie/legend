<#include "yqx/layout/header.ftl">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/report/statistics/warehouse/warehouse.css?ee648cf6e40fa55fca48488a91ee6dcc">

<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <#-- 入库 START -->
    <div class="content fr">
    <div class="content-head">
        <div class="tab-item js-tab-item current-item"
             data-desc="in"
             data-target=".warehouse-in-content" data-tab="0">入库明细
        </div>
        <div class="tab-item js-tab-item"
             data-desc="out"
             data-target=".warehouse-out-content" data-tab="1">出库明细
        </div>
    </div>
        </div>
    <div class="content fr warehouse-in-content current-content" data-desc="warehouse-in">
        <div class="container table-box">
            <div class="search-form" id="inSearchForm">
                <div class="show-grid">
                    <div class="input-group">
                        <label>统计时间</label>

                        <div class="form-item">
                            <input type="text" id="createTimeStart" name="startTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="开始时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <label>至</label>

                        <div class="form-item">
                            <input type="text" id="createTimeEnd" name="endTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="结束时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>
                    <div class="input-group">
                        <label>配件名称</label>
                        <div class="form-item width-2">
                            <input class="yqx-input yqx-input-small" name="goodsName">
                        </div>
                    </div>
                    <div class="input-group">
                        <label>零件号</label>
                        <div class="form-item width-2">
                            <input class="yqx-input yqx-input-small"
                                   name="goodsFormat">
                        </div>
                    </div>

                </div>
                <div class="show-grid">
                    <div class="input-group">
                        <label>入库单号</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small" name="warehouseInSn">
                        </div>
                    </div>
                    <div class="input-group">
                        <label>供应商</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-supplier"
                                   placeholder="输入查询供应商"
                                    >
                            <i class="fa icon-angle-down icon-small"></i>
                            <input type="hidden"
                                   name="supplierId"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <label>采购人</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-employee">
                            <i class="fa icon-angle-down icon-small"></i>
                            <input type="hidden"
                                   name="purchaseAgent"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <label>单据类型</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-order-type-in">
                            <input type="hidden"
                                   name="status"
                                    >
                            <i class="fa icon-angle-down icon-small"></i>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="search-btns fr btn-small">
                        <a href="javascript:void(0)" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                    </div>
                </div>
            </div>
            <div class="form-options text-right">
                <div class="form-option">
                    <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                </div>

                <div class="vertical-line"></div>
                <div class="form-option">
                    <a href="javascript:;" class="js-excel" data-target="services" type="button"><i
                            class="icon-signout"></i>导出excel</a>
                </div>
            </div>
            <div class="dropdown hide">
                <ul class="dropdown-menu dropdown-menu-in" data-desc="warehouse-in">
                    <li><label for="cb1"><input type="checkbox" data-ref="cb1" checked>单据号</label></li>
                    <li><label for="cb2"><input type="checkbox" data-ref="cb2" checked>单据类型</label></li>
                    <li><label for="cb3"><input type="checkbox" data-ref="cb3" checked>入库日期</label></li>
                    <li><label for="cb4"><input type="checkbox" data-ref="cb4" checked>零件号</label></li>
                    <li><label for="cb5"><input type="checkbox" data-ref="cb5" checked>配件名称</label></li>
                    <li><label for="cb6"><input type="checkbox" data-ref="cb6" checked>成本价</label></li>
                    <li><label for="cb7"><input type="checkbox" data-ref="cb7" checked>成本金额</label></li>
                    <li><label for="cb8"><input type="checkbox" data-ref="cb8" checked>入库数量</label></li>
                    <li><label for="cb9"><input type="checkbox" data-ref="cb9" checked>单位</label></li>
                    <li><label for="cb10"><input type="checkbox" data-ref="cb10" checked>类别</label></li>
                    <li><label for="cb11"><input type="checkbox" data-ref="cb11" checked>仓位</label></li>
                    <li><label for="cb12"><input type="checkbox" data-ref="cb12" checked>适用车型</label></li>
                    <li><label for="cb13"><input type="checkbox" data-ref="cb13" checked>供应商</label></li>
                    <li><label for="cb14"><input type="checkbox" data-ref="cb14" checked>采购人</label></li>
                    <li><label for="cb15"><input type="checkbox" data-ref="cb15" checked>开单人</label></li>
                </ul>
            </div>
        </div>
        <div class="container  table-box scroll-x">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th class="cb1">单据号</th>
                    <th class="cb2">单据类型</th>
                    <th class="cb3">入库日期</th>
                    <th class="cb4">零件号</th>
                    <th class="cb5">配件名称</th>
                    <th class="cb6">成本价</th>
                    <th class="cb7">成本金额</th>
                    <th class="cb8">入库数量</th>
                    <th class="cb9">单位</th>
                    <th class="cb10">类别</th>
                    <th class="cb11">仓位</th>
                    <th class="cb12">适用车型</th>
                    <th class="cb13">供应商</th>
                    <th class="cb14">采购人</th>
                    <th class="cb15">开单人</th>
                </tr>
                </thead>
                <tbody id="inListFill"></tbody>
            </table>
        </div>
        <div id="inTotalFill" class="table_total"></div>
        <div class="yqx-page" id="inListPage"></div>
    </div>
    <#-- 入库 END -->

    <#-- 出库 START -->
    <div class="content fr warehouse-out-content hide" data-desc="warehouse-out">
        <div class="container table-box">
            <input id="listUrl" hidden value="">
            <div class="search-form" id="outSearchForm">
                <div class="show-grid">
                    <div class="input-group">
                        <label>统计时间</label>

                        <div class="form-item">
                            <input type="text" id="createTimeStart2" name="startTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="开始时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                        <label>至</label>

                        <div class="form-item">
                            <input type="text" id="createTimeEnd2" name="endTime"
                                   class="yqx-input yqx-input-small js-date" placeholder="结束时间">
                            <span class="fa icon-small icon-calendar"></span>
                        </div>
                    </div>
                    <div class="input-group">
                        <label>配件名称</label>
                        <div class="form-item width-2">
                            <input class="yqx-input yqx-input-small" name="goodsName">
                        </div>
                    </div>
                    <div class="input-group">
                        <label>零件号</label>
                        <div class="form-item width-2">
                            <input class="yqx-input yqx-input-small"
                                   name="goodsFormat">
                        </div>
                    </div>

                </div>
                <div class="show-grid">
                    <div class="input-group">
                        <label>出库单号</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small"
                                   name="warehouseOutSn"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <label>工单号</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small" name="orderSn">
                        </div>
                    </div>
                    <div class="input-group">
                        <label>领料人</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-employee">
                            <i class="fa icon-angle-down icon-small"></i>
                            <input type="hidden"
                                   name="goodsReceiver"
                                    >
                        </div>
                    </div>
                    <div class="input-group">
                        <label>单据类型</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-order-type-out">
                            <input type="hidden"
                                   name="status"
                                    >
                            <i class="fa icon-angle-down icon-small"></i>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="input-group">
                        <label>　　车主</label>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small"
                                   name="customerName"
                                    >
                        </div>
                    </div>
                    <div class="search-btns fr btn-small">
                        <a href="javascript:void(0)" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                    </div>
                </div>
            </div>
                <div class="form-options text-right">
                    <div class="form-option">
                        <i class="js-list-option" data-target=".dropdown"><span class="icon-plus"></span>列表字段设置</i>
                    </div>
                    <div class="vertical-line"></div>
                    <div class="form-option">
                        <a href="javascript:;" class="js-excel" type="button"><i
                                class="icon-signout"></i>导出excel</a>
                    </div>
                </div>
                <div class="dropdown hide">
                    <ul class="dropdown-menu dropdown-menu-out" data-desc="warehouse-out">
                        <li><label for="cb1"><input type="checkbox" data-ref="cb1" checked>单据号</label></li>
                        <li><label for="cb2"><input type="checkbox" data-ref="cb2" checked>单据类型</label></li>
                        <li><label for="cb3"><input type="checkbox" data-ref="cb3" checked>出库日期</label></li>
                        <li><label for="cb4"><input type="checkbox" data-ref="cb4" checked>车牌</label></li>
                        <li><label for="cb5"><input type="checkbox" data-ref="cb5" checked>车主</label></li>
                        <li><label for="cb6"><input type="checkbox" data-ref="cb6" checked>零件号</label></li>
                        <li><label for="cb7"><input type="checkbox" data-ref="cb7" checked>配件名称</label></li>
                        <li><label for="cb8"><input type="checkbox" data-ref="cb8" checked>出库价</label></li>
                        <li><label for="cb9"><input type="checkbox" data-ref="cb9" checked>出库金额</label></li>
                        <li><label for="cb10"><input type="checkbox" data-ref="cb10" checked>成本价</label></li>
                        <li><label for="cb11"><input type="checkbox" data-ref="cb11" checked>成本金额</label></li>
                        <li><label for="cb12"><input type="checkbox" data-ref="cb12" checked>出库数量</label></li>
                        <li><label for="cb13"><input type="checkbox" data-ref="cb13" checked>单位</label></li>
                        <li><label for="cb14"><input type="checkbox" data-ref="cb14" checked>类别</label></li>
                        <li><label for="cb15"><input type="checkbox" data-ref="cb15" checked>仓位</label></li>
                        <li><label for="cb16"><input type="checkbox" data-ref="cb16" checked>工单号</label></li>
                        <li><label for="cb17"><input type="checkbox" data-ref="cb17" checked>业务类型</label></li>
                        <li><label for="cb18"><input type="checkbox" data-ref="cb18" checked>维修接待</label></li>
                        <li><label for="cb19"><input type="checkbox" data-ref="cb19" checked>领料人</label></li>
                        <li><label for="cb20"><input type="checkbox" data-ref="cb20" checked>开单人</label></li>
                    </ul>
                </div>

        </div>
        <div class="container  table-box scroll-x">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th class="cb1">单据号</th>
                    <th class="cb2">单据类型</th>
                    <th class="cb3">出库日期</th>
                    <th class="cb4">车牌</th>
                    <th class="cb5">车主</th>
                    <th class="cb6">零件号</th>
                    <th class="cb7">配件名称</th>
                    <th class="cb8">出库价</th>
                    <th class="cb9">出库金额</th>
                    <th class="cb10">成本价</th>
                    <th class="cb11">成本金额</th>
                    <th class="cb12">出库数量</th>
                    <th class="cb13">单位</th>
                    <th class="cb14">类别</th>
                    <th class="cb15">仓位</th>
                    <th class="cb16">工单号</th>
                    <th class="cb17">业务类型</th>
                    <th class="cb18">维修接待</th>
                    <th class="cb19">领料人</th>
                    <th class="cb20">开单人</th>
                </tr>
                </thead>
                <tbody id="outListFill"></tbody>
            </table>
        </div>
        <div id="outTotalFill" class="table_total"></div>
        <div class="yqx-page" id="outListPage"></div>
    </div>
    <#-- 出库 END -->
</div>
<script type="text/template" id="inListTpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var len = json.data.content.length;%>
    <% for(var i=0;i<len;i++){%>
        <% var item = json.data.content[i];%>
        <tr
        <%if(item.status=='红字入库'){%>class="red"<%}%>>
        <td class="cb1"><%=item.warehouseInSn%></td>
        <td class="cb2"><%=item.status%></td>
        <td class="cb3"><%=item.gmtCreateStr%></td>
        <td class="cb4"><%=item.goodsFormat%></td>
        <td class="cb5"><%=item.goodsName%></td>
        <td class="cb6"><%if(item.purchasePrice!=null){%>
            &yen;<%=item.purchasePrice.toFixed(2)%>
            <%}%>
        </td>
        <td class="cb7"><%if(item.purchaseAmount!=null){%>
            &yen;<%=item.purchaseAmount.toFixed(2)%>
            <%}%>
        </td>
        <td class="cb8"><%=item.goodsCount%></td>
        <td class="cb9"><%=item.measureUnit%></td>
        <td class="cb10"><%=item.catName%></td>
        <td class="cb11"><%=item.depot%></td>
        <td class="cb12">
            <% if(item.carInfoStr!=null){%>
            <%=item.carInfoStr%>
            <%}%>
            <% var carInfoList = item.carInfoList;%>
            <% if(carInfoList!=null){ var size=carInfoList.length;%>
            <% for(var j=0;j<size ;j++){%>
                <%= carInfoList[j].carBrandName;%> <%= carInfoList[j].carSeriesName;%> <%=
                carInfoList[j].carAlias;%>
                &nbsp;
                <%}%>
                <%}%>
        </td>
        <td class="cb13"><%=item.supplierName%></td>
        <td class="cb14"><%=item.goodsReceiverName%></td>
        <td class="cb15"><%=item.creatorName%></td>
        </tr>

        <%}%>
    <%} else {%>
    <tr>
        <td colspan="15">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="inTotalTpl">
    <% var item = json.data;%>
    查询结果：合计: <%if(item.totalAmount == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalAmount.toFixed(2)%></span><%}%>
    成本金额: <%if(item.totalPurchase == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalPurchase.toFixed(2)%></span><%}%>
    税费: <%if(item.totalTax == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalTax.toFixed(2)%></span><%}%>
    运费: <%if(item.totalFreight == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalFreight.toFixed(2)%></span><%}%>
    入库数量总计:<%if(item.totalCount == null){%> 0 <%}else{%> <%=item.totalCount%> <%}%>
</script>
<script type="text/template" id="outListTpl">
    <%if(json.data && json.data.content && json.data.content.length) {%>
    <% var len = json.data.content.length;%>
    <% for(var i=0;i<len;i++){%>
    <% var item = json.data.content[i];%>
    <tr <%if(item.status=='红字出库'){%>class="red"<%}%>>
    <td class="cb1"><%=item.warehouseOutSn%></td>
    <td class="center cb2">
        <%=item.status%>
    </td>
    <td class="cb3"><%=item.gmtCreateStr%></td>
    <td class="center cb4"><%=item.carLicense%></td>
    <td class="center cb5"><%=item.customerName%></td>
    <td class="cb6"><%=item.goodsFormat%></td>
    <td class="cb7"><%=item.goodsName%></td>

    <td class="cb8"><%if(item.salePrice!=null){%>
        &yen;<%=item.salePrice.toFixed(2)%>
        <%}%>
    </td>
    <td class="cb9"><%if(item.salePriceAmount!=null){%>
        &yen;<%=item.salePriceAmount.toFixed(2)%>
        <%}%>
    </td>
    <td class="cb10"><%if(item.inventoryPrice!=null){%>
        &yen;<%=item.inventoryPrice.toFixed(2)%>
        <%}%>
    </td>
    <td class="cb11"><%if(item.inventoryPriceAmount!=null){%>
        &yen;<%=item.inventoryPriceAmount.toFixed(2)%>
        <%}%>
    </td>
    <td class="center cb12"><%=item.goodsCount%></td>
    <td class="center cb13"><%=item.measureUnit%></td>
    <td class="cb14"><%=item.catName%></td>
    <td class="cb15"><%=item.depot%></td>
    <td class="cb16"><%=item.orderSn%></td>
    <%if(item.orderTypeName == null||item.orderTypeName == ""){%>
        <td class="cb17">--</td>
    <%}else{%>
        <td class="cb17"><%=item.orderTypeName%></td>
    <%}%>
    <td class="center cb18"><%=item.receiverName%></td>
    <td class="center cb19"><%=item.goodsReceiverName%></td>
    <td class="center cb20"><%=item.operatorName%></td>
    </tr>

    <%}%>
    <%} else {%>
    <tr>
        <td colspan="19">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="outTotalTpl">
    <% var item = json.data;%>
    查询结果:

    出库金额总计: <%if(item.totalSalePriceAmount == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalSalePriceAmount.toFixed(2)%></span>
    <%}%>

    成本金额总计: <%if(item.totalInventoryPriceAmount == null){%> <span class="money-font">0.00</span> <%}else{%><span class="money-font">&yen;<%=item.totalInventoryPriceAmount.toFixed(2)%></span>
    <%}%>
    出库数量: <%if(item.totalInventoryPriceAmount == null){%> 0 <%}else{%> <%=item.totalCount%> <%}%>
</script>
<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<script src="${BASE_PATH}/static/js/common/report/report-common.js?6ba9ca39d54ea0d7587dd452bb409d86"></script>
<#--出入库 公共 js-->
<script type="text/javascript"
        src="${BASE_PATH}/static/js/page/report/statistics/warehouse/warehouse.js?a80be3a26461c4f5e55df74d61004db3"></script>
<#include "yqx/layout/footer.ftl">
