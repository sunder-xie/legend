<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/in/in-list.css?46da08433279dd7a2e8f51966e6c87f6"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-list-title clearfix">
            <h3 class="headline fl">入库单查询</h3>
        </div>
        <!-- 标题 end -->
        <!-- 查询条件 start -->
        <div class="form-box" id="listForm">
            <div class="show-grid">
                <div class="form-item">
                    <input class="yqx-input yqx-input-icon yqx-input-small" id="start" name="search_startTime" placeholder="入库开始时间">
                    <i class="fa icon-calendar icon-small"></i>
                </div>
                至
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small" name="search_endTime" id="end" placeholder="入库结束时间">
                    <i class="fa icon-calendar icon-small"></i>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small yqx-input-icon js-supplier" placeholder="请选择供应商">
                    <i class="fa icon-angle-down icon-small"></i>
                    <input type="hidden" name="search_supplierId">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small yqx-input-icon js-agent" placeholder="请选择采购人">
                    <i class="fa icon-angle-down icon-small"></i>
                    <input type="hidden" name="search_purchaseAgent">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_conditionLike"
                           placeholder="配件编号/配件名称/零件号">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_warehouseInSn"
                           placeholder="入库单号">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small" name="search_carInfo"
                           placeholder="适配车型">
                </div>
                <input type="hidden" name="search_status">
                <div class="search-btns fr">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                </div>
            </div>
        </div>
        <div class="list-tab-box js-list-tab">
            <div class="list-tab" data-key="LZRK">蓝字入库
            </div><div class="list-tab" data-key="HZRK">红字入库
        </div><div class="list-tab" data-key="LZZF">蓝字作废
        </div><div class="list-tab" data-key="HZZF">红字作废
        </div><div class="list-tab current-tab" data-key="">全部
        </div>

            <div class="tools-bar clearfix fr">
                <a href="javascript:;" class="yqx-link draft js-draft" data-key="DRAFT" data-target=".draft-table">
                    <img src="${BASE_PATH}/static/img/page/warehouse/in/draft-icon.png" class="draft-icon">草稿箱</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link export-excel"><i
                        class="icon-signout"></i>导出Excel</a>
            </div>
        </div>
        <!-- 查询条件 end -->

        <!-- 数据列表>>表格 start -->
        <div class="order-list-table draft-table hide" id="draftFill"></div>
        <div class="order-list-table normal-table current-tab" id="listFill"></div>
        <!-- 数据列表>>表格  end -->

    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script type="text/html" id="listTpl">
    <table class="yqx-table yqx-table-hover yqx-table-link  yqx-table-hidden yqx-table-big">
        <thead>
        <tr class="tr-head">
            <th class="text-l tc-1">入库日期</th>
            <th class="text-l tc-2">入库单号</th>
            <th colspan="2" class="text-l tc-3">供应商</th>
            <th class="text-r tc-4">总计（元）</th>
            <th class="text-c">操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i < json.data.content.length;i++){%>
        <%var item = json.data.content[i];%>
        <tr class="js-inforlink tr-title" data-id="<%= item.id%>">
            <td class="text-l text-bold text-important"><%=item.inTime%></td>
            <td class="text-l text-bold text-important"><%=item.warehouseInSn%></td>
            <td colspan="2" class="js-show-tips text-bold text-important text-l"><%=item.supplierName%></td>
            <td class="money-font text-r js-show-tips"><%='&yen;' + item.totalAmount%></td>
            <td class="op-re text-c">
                <% if(item.status == "LZRK"){%>
                <a href="javascript:void(0)" class=" yqx-link yqx-link-2 js-stock-refund" data-id="<%= item.id%>">退货</a>
                <% }else if(item.status == "HZRK"){%>

                <a href="javascript:void(0)" class="yqx-link yqx-link-2 js-abolish" data-id="<%= item.id%>">作废</a>
                <% }else if(item.status == "DRAFT"){%>
                <a  href="javascript:void(0)"class="yqx-link yqx-link-3 js-draft-stock" data-id="<%= item.id%>">入库</a>
                <% }else{%>

                <a href="javascript:void(0)" class="yqx-link yqx-link-1 js-detail" data-id="<%= item.id%>">查看</a>
                <% }%>
            </td>
        </tr>
        <%if (item.detailVoList){%>
        <%for(var j=0; j< item.detailVoList.length; j++){%>
        <%var subItem = item.detailVoList[j];%>
        <tr class="detail-list">
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsFormat%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.goodsSn%></div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsName%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.carInfoStr%></div>
            </td>
            <td class="text-l">
                <div class="text-minor">入库 </div>
                <div class="ellipsis-1 js-show-tips"><%=subItem.goodsCount%><span class="text-minor"><%=subItem.measureUnit%></span></div>
            </td>
            <td>
                <div class="text-minor">入库价 </div>
                <div class="ellipsis-1 money-small-font js-show-tips">&yen;<%=subItem.purchasePrice.toFixed(2)%></div>
            </td>
            <td class="text-r">
                <div class="text-minor">金额</div>
                <div class="money-small-font">&yen;<%=subItem.purchaseAmount.toFixed(2)%></div>
            </td>
            <td class="text-r">
                <%if(j == item.detailVoList.length-1 ){%>
                <% if(item.status == "LZRK"){%>
                <% }else if(item.status == "HZRK"){%>
                <p class="seal-3 yth"></p>
                <% }else if(item.status == "DRAFT"){%>
                <% }else{%>
                <p class="seal-3 yzf"></p>
                <% }}%>
            </td>
        </tr>
        <%}}%>
        <% if (i < json.data.content.length - 1) { %>
        <tr class="blank">
            <td colspan="6"></td>
        </tr>
        <%}%>
        <%}%>
        <%}%>
        </tbody>
    </table>
    <%if(json.data && json.data.otherData){%>
    <%var total = json.data.otherData;%>
    <div class="search-result">
        查询结果
        <span>入库总计：<i class="money-font js-money-font"><%= total.totalAmountStatistics%></i></span>
    </div>
    <%}%>
    <div class="yqx-page" id="listPage">
    </div>
</script>

<script type="text/html" id="draftTpl">
    <table class="yqx-table yqx-table-link">
        <thead>
        <tr>
            <th class="text-l">草稿日期</th>
            <th colspan="2" class="text-l">草稿单号</th>
            <th>供应商</th>
            <th class="text-r">总计（元）</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i < json.data.content.length;i++){%>
        <%var item = json.data.content[i];%>
        <tr class="js-inforlink tr-title" data-id="<%= item.id%>">
            <td class="text-l text-bold text-important"><%=item.inTime%></td>
            <td class="text-l text-bold text-important"><%=item.warehouseInSn%></td>
            <td colspan="2" class="js-show-tips text-bold text-important text-l"><%=item.supplierName%></td>
            <td class="money-font text-r js-show-tips"><%='&yen;' + item.totalAmount%></td>
            <td class="op-re text-c">
                <% if(item.status == "LZRK"){%><a href="javascript:void(0)" class=" yqx-link yqx-link-2 js-stock-refund" data-id="<%= item.id%>">退货</a>
                <% }else if(item.status == "HZRK"){%>
                <a href="javascript:void(0)" class="yqx-link yqx-link-2 js-abolish" data-id="<%= item.id%>">作废</a>
                <% }else if(item.status == "DRAFT"){%>
                <a  href="javascript:void(0)"class="yqx-link yqx-link-3 js-draft-stock" data-id="<%= item.id%>">入库</a>
                <% }else{%>
                <a href="javascript:void(0)" class="yqx-link yqx-link-1 js-detail" data-id="<%= item.id%>">查看</a>
                <% }%>
            </td>
        </tr>
        <%if (item.detailVoList){%>
        <%for(var j=0; j< item.detailVoList.length; j++){%>
        <%var subItem = item.detailVoList[j];%>
        <tr class="detail-list">
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsFormat%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.goodsSn%></div>
            </td>
            <td class="text-l">
                <div class="ellipsis-1 text-bold js-show-tips"><%=subItem.goodsName%></div>
                <div class="ellipsis-1 text-minor js-show-tips"><%=subItem.carInfoStr%></div>
            </td>
            <td class="text-l">
                <div class="text-minor">入库 </div>
                <div class="ellipsis-1 js-show-tips"><%=subItem.goodsCount%><span class="text-minor"><%=subItem.measureUnit%></span></div>
            </td>
            <td>
                <div class="text-minor">入库价 </div>
                <div class="ellipsis-1 money-small-font js-show-tips">&yen;<%=subItem.purchasePrice.toFixed(2)%></div>
            </td>
            <td class="text-r">
                <div class="text-minor">金额</div>
                <div class="money-small-font">&yen;<%=subItem.purchaseAmount.toFixed(2)%></div>
            </td>
            <td class="text-r">
                <%if(j == item.detailVoList.length-1 ){%>
                <% if(item.status == "LZRK"){%>
                <% }else if(item.status == "HZRK"){%>
                <p class="seal-3 yth"></p>
                <% }else if(item.status == "DRAFT"){%>
                <% }else{%>
                <p class="seal-3 yzf"></p>
                <% }}%>
            </td>
        </tr>
        <%}}%>
        <% if (i < json.data.content.length - 1) { %>
        <tr class="blank">
            <td colspan="6"></td>
        </tr>
        <%}%>
        <%}%>
        <%}%>
        </tbody>
    </table>
    <div class="yqx-page" id="draftPage">
    </div>
</script>
<!-- 表格模板 end -->

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/in/in-list.js?7d2c6d4d952367769d716c622a2bdf83"></script>
<!-- 脚本引入区 end -->

<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">

<#include "yqx/layout/footer.ftl">