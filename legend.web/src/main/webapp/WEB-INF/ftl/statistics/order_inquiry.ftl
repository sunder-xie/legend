<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/order_inquiry.css">
<div class="wrapper">
<#include "statistics/statistics_nav.ftl">

    <div class="s_box">
        <form name="searchForm" id="searchForm" action="${BASE_PATH}/shop/order/order-list/list">
            <ul class="s_ul">
                <li class="full_row">搜索工单 :<input
                        class="stext" id="search_keyword" name="search_keyword"
                        placeholder="查找车牌、手机号、工单号" value=""/>开单时间 :<input
                        type="text" class="selcont w_ge startTime" name="search_startTime" id="d4311" value=""
                        onFocus="WdatePicker({doubleCalendar:true,maxDate:'#F{$dp.$D(\'d4312\')}'})"
                        placeholder="开始时间"/>　　至　　<input
                        type="text" class="selcont w_ge endTime" name="search_endTime" id="d4312" value=""
                        onFocus="WdatePicker({doubleCalendar:true,minDate:'#F{$dp.$D(\'d4311\')}'})"
                        placeholder="结束时间"/>
                </li>
                <li class="full_row">服务顾问 :<select
                        class="selcont chosen" id="receiver" name="search_receiver">
                    <script id="receiverTemplate" type="text/html">
                        <option value="">选择服务顾问</option>
                        <%for(var index in templateData){%>
                        <%item = templateData[index]%>
                        <option value="<%=item.id%>"><%=item.name%></option>
                        <%}%>
                    </script>

                </select>业务类型 :<select
                        class="selcont chosen" id="orderType" name="search_orderType">
                    <script id="orderTypeTemplate" type="text/html">
                        <option value="">选择业务类型</option>
                        <%for(var index in templateData){%>
                        <%item = templateData[index]%>
                        <option value="<%=item.id%>"><%=item.name%></option>
                        <%}%>
                    </script>
                </select>工单类型 :<select
                        class="selcont" id="orderType" name="search_orderType">
                    <option>全部工单</option>
                    <option>报价车辆</option>
                    <option>修理中车辆</option>
                    <option>已完工车辆</option>
                    <option>已挂账车辆</option>
                    <option>已结算车辆</option>
                </select>
                </li>
            </ul>
            <button class="s_right" type="submit"></button>
            <input type="hidden" name="search_orderStatuss" id="search_orderStatuss" value="CJDD,DDBJ"/>
            <input type="hidden" name="search_payStatus" id="search_payStatus" value=""/>
            <input type="hidden" name="page" id="search_page" value="1"/>

        </form>
    </div>

    <div class="content">
        <ul class="info_detail">
            <li>开单日期</li>
            <li>车牌</li>
            <li>车辆品牌</li>
            <li>车辆型号</li>
            <li class="car_owner">车主</li>
            <li>车主电话</li>
            <li class="contactor">联系人</li>
            <li>联系电话</li>
            <li>行驶里程</li>
            <li>预计出厂日期</li>
            <li>保险到期日</li>
            <li>购车日期</li>
            <li>车辆颜色</li>
            <li>领证日期</li>
            <li>进厂方式</li>
            <li>服务顾问</li>
            <li>业务类型</li>
            <li>状态</li>
            <li>操作员</li>
            <li>完工日期</li>
            <li>结算日期</li>
            <li>应收金额</li>
            <li>实收金额</li>
            <li>挂帐金额</li>
            <li>总费用</li>
            <li>折前材料费</li>
            <li>精品材料费</li>
            <li>总材料费</li>
            <li>材料管理费</li>
            <li>折前工时费</li>
            <li>精品工时费</li>
            <li>总工时费</li>
            <li>折前工时费</li>
            <li>其他费用</li>
            <li class="remark">备注信息</li>
        </ul>

        <div class="box">
            <a title="工单详情" href="#跳转到对应的工单页面">
                <ul class="info_detail info">
                    <li>开单日期</li>
                    <li>车牌</li>
                    <li>车辆品牌</li>
                    <li>车辆型号</li>
                    <li class="car_owner">车主</li>
                    <li>车主电话</li>
                    <li class="contactor">联系人</li>
                    <li>联系电话</li>
                    <li>行驶里程</li>
                    <li>预计出厂日期</li>
                    <li>保险到期日</li>
                    <li>购车日期</li>
                    <li>车辆颜色</li>
                    <li>领证日期</li>
                    <li>进厂方式</li>
                    <li>服务顾问</li>
                    <li>业务类型</li>
                    <li>状态</li>
                    <li>操作员</li>
                    <li>完工日期</li>
                    <li>结算日期</li>
                    <li>应收金额</li>
                    <li>实收金额</li>
                    <li>挂帐金额</li>
                    <li>总费用</li>
                    <li>折前材料费</li>
                    <li>精品材料费</li>
                    <li>总材料费</li>
                    <li>材料管理费</li>
                    <li>折前工时费</li>
                    <li>精品工时费</li>
                    <li>总工时费</li>
                    <li>折前工时费</li>
                    <li>其他费用</li>
                    <li class="remark">备注信息</li>
                </ul>
            </a>
        </div>

    </div>
<#include "layout/footer.ftl" >
