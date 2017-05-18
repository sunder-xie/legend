<#-- 列表 end -->
<script id="goodsList" type="text/html">
    <table class="Z-dataLists">
        <thead>
        <tr>
            <th><input class="Z-select"
                <%if(goodsList && goodsList.content && isAllSelected(goodsList.content, selectedCustomer)) {%>
                        <%='checked'%>
                        <%}%>
                       type="checkbox"></th>
            <th>车牌</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>车型</th>
            <th>最近消费时间</th>
            <th>累计消费金额</th>
            <th>累计消费次数</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% if(goodsList) {%>
        <% var content = goodsList.content;%>
        <% for(var i=0;i < content.length ;i++) {%>
            <tr>
                <td>
                    <input class="Z-select-item" data-mobile="<%= content[i].mobile %>"
                           data-customer-car-id="<%= content[i].customerCarId%>"
                           <%if(selectedCustomer[content[i].customerCarId]) {%>
                           <%='checked'%>
                           <%}%>
                           type="checkbox">
                </td>
                <td title="<%= content[i].carLicense %>">
                    <a class="revisit-btn revisitBtn" target="_blank"
                     href="${BASE_PATH}/shop/customer/info?cid=<%= content[i].customerCarId%>">
                        <%=content[i].carLicense %></a>
                </td>
                <td title="<%= content[i].customerName %>">
                    <%= content[i].customerName %></td>
                <td title="<%= content[i].mobile %>">
                    <%= content[i].mobile %></td>
                <td title="<%= content[i].carModel %>">
                    <%= content[i].carModel %></td>
                <td title="<%= content[i].lastPayTimeStr %>">
                    <%= content[i].lastPayTimeStr %></td>
                <td title="<%= content[i].totalAmount %>">
                    <%= content[i].totalAmount %></td>
                <td title="<%= content[i].totalNumber %>">
                    <%= content[i].totalNumber %></td>
                <td>
                    <a class="revisit-btn revisitBtn"
                       data-customer-car-id="<%= content[i].customerCarId %>"
                       data-user-data="<%= toStringify(content[i]) %>"
                       href="javascript:;">
                        回访
                    </a>
                </td>
            </tr>
            <%}%>
            <%}%>
        </tbody>
    </table>
</script>
<#-- 列表 end -->

