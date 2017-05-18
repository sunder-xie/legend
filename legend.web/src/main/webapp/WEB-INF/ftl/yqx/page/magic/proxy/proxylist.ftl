<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy/proxylist.css?d6b26d6c40fe88509ac74efbaae762ed"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- 标题 start -->
        <div class="order-list-title clearfix">
            <#--<h3 class="headline fl js-headline">-->
                <#--受托单查询-->
            <#--</h3>-->
            <#if BPSHARE == 'true'>
            <h3 class="headline fl js-headline">受托查询</h3>
            <#elseif SESSION_SHOP_JOIN_STATUS == 1>
            <h3 class="headline fl">委托查询</h3>
            </#if>

            <div class="tools-btn fr">
                <#if BPSHARE != 'true'>
                <a href="${BASE_PATH}/shop/order/carwash?refer=order-list" class="yqx-btn btn-1"><span
                        class="fa icon-plus"></span>洗车单</a>
                <a href="${BASE_PATH}/shop/order/speedily?refer=order-list" class="yqx-btn btn-2"><span
                        class="fa icon-plus"></span>快修快保单</a>
                </#if>
                <a href="${BASE_PATH}/shop/order/common-add?refer=order-list" class="yqx-btn btn-3"><span
                        class="fa icon-plus"></span>综合维修单</a>
                <#if BPSHARE != 'true'>
                <a href="${BASE_PATH}/shop/order/sell-good?refer=order-list" class="yqx-btn btn-4"><span
                        class="fa icon-plus"></span>销售单</a>
                </#if>
            </div>
        </div>
        <!-- 标题 end -->
        <div class="tab">
            <div class="tabs-control">
            <#if BPSHARE == 'true'>
                <span class="tab-item current-item js-tab-item" data-tab="entrusted">受托查询</span><#if SESSION_SHOP_JOIN_STATUS == 1><span class="tab-item js-tab-item" data-tab="entrust">委托查询</span></#if>
            <#elseif SESSION_SHOP_JOIN_STATUS == 1>
                <span class="tab-item current-item js-tab-item" data-tab="entrust">委托查询</span>
            </#if>
            </div>
        </div>

        <!-- 查询条件 start -->
        <div class="condition-box">
            <div class="proxy-text js-entrust">
                <div class="condition-input" id="authorizeForm">
                    <div class="show-grid">

                        <div class="form-label">
                            受托方
                        </div>
                        <div class="form-item form-item-width">
                            <input type="text" name="proxyShopName" class="yqx-input yqx-input-small" value="" placeholder="">
                        </div>
                        <div class="form-label">
                            车牌号
                        </div>
                        <div class="form-item form-item-width">
                            <input type="text" name="carLicense" class="yqx-input yqx-input-small" value="" placeholder="">
                        </div>
                        <div class="form-label">
                            委托单号
                        </div>
                        <div class="form-item form-item-width">
                            <input type="text" name="proxySn" class="yqx-input yqx-input-small" value="" placeholder="">
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="form-label">
                            委托单状态
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStatus" class="yqx-input yqx-input-icon yqx-input-small js-proxylist"
                                   value="" placeholder="请选择">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStartTime" class="yqx-input yqx-input-icon yqx-input-small" id="start0"
                                   value="" placeholder="开始日期">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        至
                        <div class="form-item">
                            <input type="text" name="proxyEndTime" class="yqx-input yqx-input-icon yqx-input-small" id="end0"
                                   value="" placeholder="结束日期">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        <div class="search-btns fr">
                            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="proxy-text js-entrusted">
                <div class="condition-input" id="trusteeForm">
                    <div class="show-grid">
                        <div class="proxy-text">
                            <div class="form-label">
                                委托方
                            </div>
                            <div class="form-item form-item-width">
                                <input type="text" name="shopName" class="yqx-input yqx-input-small" value="" placeholder="">
                            </div>
                        </div>
                        <div class="form-label">
                            车牌号
                        </div>
                        <div class="form-item form-item-width">
                            <input type="text" name="carLicense" class="yqx-input yqx-input-small" value="" placeholder="">
                        </div>
                        <div class="form-label">
                            委托单号
                        </div>
                        <div class="form-item form-item-width">
                            <input type="text" name="proxySn" class="yqx-input yqx-input-small" value="" placeholder="">
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="form-label">
                            委托单状态
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStatus" class="yqx-input yqx-input-icon yqx-input-small js-proxylist"
                                   value="" placeholder="请选择">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                        <div class="form-item">
                            <input type="text" name="proxyStartTime" class="yqx-input yqx-input-icon yqx-input-small" id="start1"
                                   value="" placeholder="开始日期">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                        至
                        <div class="form-item">
                            <input type="text" name="proxyEndTime" class="yqx-input yqx-input-icon yqx-input-small" id="end1"
                                   value="" placeholder="结束日期">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>


                        <div class="search-btns fr">
                            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="tab-box">
            <div class="tools-bar clearfix select-btns">
                <a href="javascript:;" class="yqx-link js-order-list-card"><i class="icon-th-large"></i>卡片</a>
                <span>|</span>
                <a href="javascript:;" class="yqx-link js-order-list-table"><i class="icon-reorder"></i>列表</a>
            </div>
            <div class="tools-bar clearfix select-btns-right">
                <a href="javascript:;" class="yqx-link js-export-excel"><i class="icon-signout"></i>导出Excel</a>
            </div>
        </div>
        <!-- 查询条件 end -->

        <div class="order-list-content" id="orderListContent"></div>

        <div class="yqx-page" id="orderListPage"></div>

    </div>


    <!-- 右侧内容区 end -->

</div>
<!-- 委托查询表格模板 start -->
<script type="text/html" id="TableEntrustTpl" >
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link">
                <thead>
                <tr>
                    <th class="td-center">序号</th>
                    <th>车牌</th>
                    <th>车型</th>
                    <th>委托单号</th>
                    <th>委托方</th>
                    <th>委托时间</th>
                    <th>委托单状态</th>
                    <th>数量</th>
                    <th>委托价</th>
                    <th>受托方</th>
                </tr>
                </thead>
                <tbody class="TableEntrustTplClass">
                <%if(json && json.data && json.data.content){%>
                <%for(var i=0;i
                <json.data.content.length ;i++){%>
                    <%var item = json.data.content[i];%>
                    <tr data-order-tableid="<%=item.id%>" class="yqx-link js-inforlink">
                        <td class="td-center"><%=json.data.size*(json.data.number-1)+i+1%></td>
                        <td class="js-show-tips"><%=item.carLicense%></td>
                        <td>
                            <div class="max-text js-show-tips ellipsis-1" title="<%=item.carInfo%>"><%=item.carInfo%>
                            </div>
                        </td>
                        <td><%=item.proxySn%></td>
                        <td><%=item.shopName%></td>
                        <td><%=item.proxyTimeStr%></td>
                        <%if(item.proxyStatus == 'YQX'){%>
                        <td class="invalid"><%=item.proxyStatusStr%></td>
                        <%}else{%>
                        <td class="card-status"><%=item.proxyStatusStr%></td>
                        <%}%>
                        <td><%=item.serviceNum%></td>
                        <td><%=item.proxyAmount%></td>
                        <td>
                            <div class="max-text js-show-tips"><%=item.proxyShopName%></div>
                        </td>
                    </tr>
                    <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!-- 表格模板 end -->


<!-- 受托查询表格模板 start -->
<script type="text/html" id="TableBeEntrustpl" >
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover yqx-table-link">
                <thead>
                <tr>
                    <th class="td-center">序号</th>
                    <th>车牌XX</th>
                    <th>车型</th>
                    <th>委托单号</th>
                    <th>委托方</th>
                    <th>委托时间</th>
                    <th>委托单状态</th>
                    <th>数量</th>
                    <th>委托价</th>
                    <th>受托方</th>
                </tr>
                </thead>
                <tbody class="TableBeEntrustplClass">
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                    <%var item = json.data.content[i];%>
                    <tr data-order-tableid="<%=item.id%>" class="yqx-link js-inforlink">
                        <td class="td-center"><%=json.data.size*(json.data.number-1)+i+1%></td>
                        <td class="js-show-tips"><%=item.carLicense%></td>
                        <td>
                            <div class="max-text js-show-tips ellipsis-1" title="<%=item.carInfo%>"><%=item.carInfo%>
                            </div>
                        </td>
                        <td><%=item.proxySn%></td>
                        <td><%=item.shopName%></td>
                        <td><%=item.proxyTimeStr%></td>
                        <td class="card-status"><%=item.proxyStatusStr%></td>
                        <td><%=item.serviceNum%></td>
                        <td><%=item.proxyAmount%></td>
                        <td><%=item.proxyShopName%></td>
                    </tr>
                    <%}%>
                    <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!-- 表格模板 end -->


<!-- 卡片模板 start -->
<script type="text/html" id="CardEntrustTpl" >
    <div class="card-box">
        <%var orderTagClassList = ["synthetical","carwarsh","speedily","synthetical","sale"];%>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i
        <json.data.content.length
                ;i++){%>
            <%var item = json.data.content[i];%>

            <div class="card-item">
                <a href="${BASE_PATH}/proxy/detail?proxyOrderId=<%=item.id%>">
                    <div class="card-info">
                        <div class="card-check-box">
                            <input type="hidden" name="id" value="<%=item.id%>"/>

                            <div>
                                <span class="card-licence"><%=item.carLicense%></span>

                                <%if(item.proxyStatus == 'YQX'){%>
                                <div class="card-status invalid">
                                    <%=item.proxyStatusStr%>
                                </div>
                                <%}else{%>
                                <div class="card-status">
                                    <%=item.proxyStatusStr%>
                                </div>
                                <%}%>
                            </div>
                            <div class="card-car-type js-show-tips ellipsis-1" title="<%=item.carInfo%>">
                                <%=item.carInfo%>
                            </div>

                        </div>
                        <div class="car-check-body">
                            <p class="type-parent">
                                <i>委托方：</i><span><%=item.shopName%></span>
                            </p>

                            <p>
                                <i>受托方：</i><span><%=item.proxyShopName%></span>
                            </p>

                            <p class="card-time">
                                <i>委托日期：</i><%=item.proxyTimeStr%>
                            </p>
                        </div>
                    </div>
                    <div class="card-money cleafix">
                        <div class="card-price">
                            <%if(item.payStatus == 1){%>
                            挂帐：<span class="card-price-1">￥<%=item.proxyAmount%></span>
                            <%}else{%>
                            总计：<span class="card-price-1">￥<%=item.proxyAmount%></span>
                            <%}%>
                            <a href="${BASE_PATH}/proxy/detail?proxyOrderId=<%=item.id%>"
                               class="yqx-input yqx-input-3 card-btn">详情</a>
                        </div>
                    </div>
                </a>
            </div>

            <%}%>
            <%}%>
    </div>

    <!-- 分页容器 start -->
    <div class="yqx-page" id="orderListCardPage"></div>
    <!-- 分页容器 end -->

</script>


<!-- 卡片模板 end -->
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/proxy/proxylist.js?206e55145599ab68445aa5af3312bd81"></script>
<!-- 脚本引入区 end -->

<!-- 脚本引入区 end -->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<#include "yqx/layout/footer.ftl">