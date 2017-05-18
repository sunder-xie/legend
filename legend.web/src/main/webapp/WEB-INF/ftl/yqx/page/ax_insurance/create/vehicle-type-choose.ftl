
<#--车型选择-->
<script id="vehicleList" type="text/html">
    <% if(success&&data) { %>
    <tr>
        <th>品牌</th>
        <th>车系</th>
        <th>发动机描述</th>
        <th>驱动形式</th>
        <th>配置型号</th>
        <th>新车购置价</th>
    </tr>
    <% for(var i = 0; i < data.length; i++){ %>
    <% var vehicleList = data[i].vehicleList; %>
    <% for(var j = 0; j < vehicleList.length; j++){ %>
    <% var vehicleL = vehicleList[j] %>
    <tr class="modelResult" data-price="<%= vehicleL.purchasePrice %>" data-cProdPlace="<%= vehicleL.vehicleImport%>"
        data-seatNum="<%= vehicleL.seat%>" data-vehicleCode="<%= vehicleL.vehicleCode%>"
        data-yearPattern="<%= vehicleL.yearPattern%>">
        <td><%= vehicleL.brandName %></td>
        <td><%= vehicleL.vehicleName %></td>
        <td><%= vehicleL.engineDesc %></td>
        <td><%= vehicleL.gearboxType %></td>
        <td><%= vehicleL.configName %></td>
        <td><%= vehicleL.purchasePrice %></td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>
<#--品牌选择的弹出层-->
<script id="maskTpl" type="text/html">
    <div class="popup">
        <div class="popup_head">
            请选择车辆配置
        </div>
        <div class="select_conf">
            <div class="div">
                <input type="text" class="search" placeholder="输入关键字查询">
                <div class="search-button"></div>
                <span class="tips">若您的车不在品牌型号可选范围内,请致电<em>400-884-5678</em>咨询</span>
            </div>
            <div class="div query">
                <div class="queryDiv">
                    <div class="brand ipt">品牌</div>
                    <div class="brand-btn s_btn s_up choose_btn"></div>
                    <ul class="ul1 dis familyL"></ul>
                </div>
                <div class="queryDiv m-left5">
                    <div class="brand ipt">车系</div>
                    <div class="brand-btn s_btn s_up choose_btn"></div>
                    <ul class="ul2 dis brandL">
                    </ul>
                </div>
                <div class="queryDiv m-left5">
                    <div class="brand ipt">发动机描述</div>
                    <div class="brand-btn s_btn s_up choose_btn"></div>
                    <ul class="ul3 dis engineDescL"></ul>
                </div>
                <div class="queryDiv m-left5">
                    <div class="brand ipt">驱动形式</div>
                    <div class="brand-btn s_btn s_up choose_btn"></div>
                    <ul class="ul4 dis gearboxTypeL"></ul>
                </div>
                <div class="queryList">
                    <div class="tiS">        <!--初进来提示-->
                        暂无结果,请检查是否已输入关键词进行查询
                    </div>
                    <table class="vehicleList"></table>
                </div>
            </div>
        </div>
    </div>
</script>


