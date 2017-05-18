<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/warehouse/share/share-common.css?ae097a4de686f0eba0d6e4c670b7d664"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/share/buy.css?bccbc2004939a1b62fe027d0c64386f7"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="wrapper-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <div class="wrapper-main fr detail-main hide">
        <h1 class="title">库存配件详情<button class="yqx-btn yqx-btn-1 js-detail-back detail-btn hide">
            返回
        </button>
        </h1>
        <div class="detail-box">
        </div>
    </div>
    <div class="wrapper-main fr warehouse share-buy">
        <h1 class="title">购买库存配件</h1>
        <div class="buy-form" id="onSaleListForm">
            <div class="form-item">
                <input class="yqx-input goods_type_input" name="goodsCate" placeholder="请输入配件类型">
            </div><div class="form-item">
                <input class="yqx-input" name="goodsName" placeholder="请输入配件名称">
            </div><ul class="region-box">
                <li class="tag-control region js-region selected" data-region="1">
                    全国
                </li><li class="tag-control region js-region" data-region="2">
                    本省
                </li><li class="tag-control region js-region" data-region="3">
                    本市
                </li>
            </ul>
            <input name="region" type="hidden" value="1">
            <button class="fr yqx-btn yqx-btn-3 js-search-btn">查询</button>
        </div>
        <div class="list-box">
        <h2 class="sub-title"></h2>
        <table class="yqx-table yqx-table-hover">
            <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th class="name">配件名称</th>
                    <th style="width: 70px;">城市</th>
                    <th class="text-r sale-num" style="width: 84px;">数量</th>
                    <th class="text-r" style="width: 85px;">销售价(元)</th>
                    <th style="width: 75px;">操作</th>
                </tr>
            </thead>
            <tbody id="onSaleListFill"></tbody>
        </table>
        <div class="yqx-page" id="onSaleListPage"></div>
        </div>
    </div>
</div>

<script type="text/template" id="onSaleListTpl">
    <%var item%>
    <%for(var i in json.data.content){%>
    <% item = json.data.content[i];%>
    <tr class="js-detail-tr">
        <td><%=(+i + 1) + (json.data.number - 1) * json.data.size%></td>
        <td class="name"><%=item.goodsName%></td>
        <td class="city"><%=item.cityName%></td>
        <td class="text-r">
            <%=item.saleNumber%><i class="measure-unit"><%=item.measureUnit%></i>
        </td>
        <td class="text-r price">
            <%=item.goodsPrice%>/<i class="measure-unit"><%=item.measureUnit%></i>
        </td>
        <td><button class="js-detail to-detail" data-id="<%=item.id%>">查看详情</button></td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <tr>
        <td colspan="6">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="detailTpl">
        <h3 class="detail-title"><%=item.goodsName%></h3>
        <div class="table-box">
        <table class="yqx-table yqx-table-hover">
            <tr>
                <td class="key">零件号：</td>
                <td class="text">
                    <%=item.goodsFormat%>
                </td>
            </tr>
            <tr>
                <td class="key">适配车型：</td>
                <td class="car-info text">
                <%=getCarInfo(item.carInfo)%>
            </td>
            </tr>
            <tr>
                <td class="key">数量：</td>
                <td class="text">
                <%=item.saleNumber%> <%=item.measureUnit%>
            </td>
            </tr>
            <tr>
                <td class="key">销售价：</td>
                <td class="money-font text">
                    <%=item.goodsPrice%> 元/<%=item.measureUnit%>
                </td>
            </tr>
            <tr>
                <td class="key">联系人：</td>
                <td class="text">
                    <img class="person-icon" src="${BASE_PATH}/static/img/page/warehouse/share/person-icon.png"><i><%=item.contactName%></i>
                </td>
            </tr>
            <tr>
                <td class="key">联系电话：</td>
                <td class="text">
                    <%=item.contactMobile%>
                </td>
            </tr>
            <tr>
                <td class="key">
                    <div>
                        门店位置：
                    </div>
                </td>
                <td class="text">
                    <p><%=item.provinceName%> <%=item.cityName%> <%=item.streetName%> <i id="distance" class="hide">距离您约 <i class="text"></i> KM</i></p>
                    <div id="map" class="hide">
                    </div>
                </td>
            </tr>
            <tr>
                <td class="key">
                    声明：
                </td>
                <td class="text">
                    淘汽云修仅提供库存配件买卖信息平台，不负责产品真伪鉴定；凡购买配件的门店请与卖方核实产品质量及价格；双方产生的纠纷均与淘汽云修无关。
                </td>
            </tr>
    </table>
</div>
</script>

<#--百度地图-->
<script src="https://api.map.baidu.com/api?v=2.0&ak=cSGY6d9xfcvZdNIdGA38Y9zZ&s=1"></script>
<script src="${BASE_PATH}/static/js/page/warehouse/share/buy.js?7647e042c59cbe88104cdf9998d50dc3"></script>
<#include "yqx/layout/footer.ftl">