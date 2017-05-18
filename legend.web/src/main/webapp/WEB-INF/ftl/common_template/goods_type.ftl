<!-- 配件分类必须文件 start ----------------------- -->
<link rel="stylesheet" href="${BASE_PATH}/resources/style/page/common_tpl_style/goods_type.css?57ad2cbaa229c6abdfb1ddb83e78ebfe">
<script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/seajs/sea.js?0eba4f6d7ef30c34921228ec84c57814"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/online/script/libs/path.config.js?0397b8618de248acb71684539c75afe9"></script>
<!-- 全部分类弹框模板 start -->
<script type="text/html" id="goods_type_wrap_tpl">
    <div class="pop_goods_type_wrap">
        <div class="pop_head">
            <div class="search_input">
                <input type="text" placeholder="搜索类别..." flag="2" id="goods_search_input"/><button class="goods_all_search_btn"></button>
            </div>
            <h1 class="head_title">全部类别</h1>
        </div>
        <p class="tips-phone">找不到您想要的配件分类? 拨打电话：<b>0571-57874656</b>（工作日9:00-18:00）</p>
        <div class="pop_main">
            <ul class="nav_side" id="navSide">

            </ul>
            <div class="nav_content" id="navContent">

            </div>
        </div>
    </div>
</script>
<!-- 全部分类弹框模板 end -->
<!-- 1级配件分类模板 start -->
<script id="navSideTpl" type="text/html">
    <% if ( success ) { %>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <li data-catid="<%= item.catId %>"><p><%= item.catName %></p></li>
    <% }} %>
</script>
<!-- 1级配件分类模板 end -->
<!-- 2级配件分类模板 start -->
<script id="secondNavContentTpl" type="text/html">
    <div class="scroller grid-hor-2">
        <div class="second_nav">
            <ul class="secend_nav_list">
                <% for (var i = 0; i < data.length; i++) { %>
                <% var item = data[i]; %>
                <li data-catid="<%= item.catId %>"><%= item.catName %></li>
                <% } %>
            </ul>
        </div>
    </div>
    <div class="scroller grid-hor-2" id="thirdNavContent"></div>
</script>
<!-- 2级配件分类模板 end -->
<!-- 3级配件分类模板 start -->
<script id="thirdNavContentTpl" type="text/html">
    <div class="category_box">
        <p class="category"><%=catName%></p>
        <ul class="category_items">
            <% for (var i = 0; i < data.length; i++) { %>
            <%  var item = data[i];
                if(item.customCat){
            %>
               <li class="item" data-catid="<%= item.catId %>" data-customcat="true"><%= item.catName %></li>
            <%}else{%>
                <li class="item" data-catid="<%= item.catId %>" data-customcat="false"><%= item.catName %></li>
            <%}%>
        <% } %>
        </ul>
    </div>
</script>
<!-- 3级配件分类模板 end -->
<!-- 全部分类搜索结果模板 start -->
<script id="searchContentTpl" type="text/html">
    <div class="scroller search_scroller">
        <h3 class="headline">全部类别下搜索结果：</h3>
        <%for(var x=0;x<data.length;x++){
            var item1 = data[x];
        %>
            <div class="category_box search_categroy_box">
                <h3 class="category"><%=item1.name%></h3>
                <%for(var y=0;y<item1.list.length;y++){
                    var item2 = item1.list[y];
                %>
                    <dl class="category_items">
                        <dt class="title"><%=item2.name%></dt>
                        <%for(var z=0;z<item2.list.length;z++){
                            var item3 = item2.list[z];
                            if(item3.customCat){
                        %>
                            <dd class="item" data-catid="<%=item3.id%>" customcat="true"><%=item3.name%></dd>
                            <%}else{%>
                            <dd class="item" data-catid="<%=item3.id%>" customcat="false"><%=item3.name%></dd>
                            <%}%>
                        <%}%>
                    </dl>
                <%}%>
            </div>
        <%}%>
    </div>
</script>
<!-- 全部分类搜索结果模板 end -->


<script type="text/html" id="goods_type_part_wind">
    <div class="goods_type_part_wind">
        <div class="goods_type_search_box">
            <input type="text" placeholder="搜索类别" id="goods_search_input" flag="1"/><a href="javascript:;" class="goods_search_btn"></a><a href="javascript:;" class="goods_all_btn">全部分类</a>
        </div>
        <div class="hot-category" id="hotCategoryContent">
        </div>
        <div class="search_result">
            <div class="search_result_scroll">
            </div>
            <p class="tips-phone">找不到您想要的配件分类? 拨打电话：<b>0571-57874656</b>（工作日9:00-18:00）</p>
        </div>
    </div>
</script>

<#--加载热门分类模板start-->
<script id="hotCategoryContentTpl" type="text/html">
    <%if(json && json.data && json.data.length > 0){%>
    <%var cateLength = json.data.length;%>
    <p>
        <img src="${BASE_PATH}/static/img/page/order/redfire.png" class="query-alert"/>
        <i class="strong-i">热门分类:</i>
        <%for(var i = 0; i < cateLength; i++){%>
        <% var goodsCategory = json.data[i];%>
        <i class="i-service js-select-goods-category" data-id="<%= goodsCategory.tqmallCatId%>" data-name="<%= goodsCategory.catName%>"><%= goodsCategory.catName%></i>
        <%}%>
    </p>
    <%}%>
</script>
<#--加载热门分类end-->
<script type="text/html" id="goods_type_part_result">
<table>
    <thead>
    <tr>
        <td>一级类别</td>
        <td>二级类别</td>
        <td>三级类别</td>
    </tr>
    </thead>
<%  for(var x=0;x<data.length;x++){
        var item1 = data[x];
        for(var y=0;y<item1.list.length;y++){
            var item2 = item1.list[y];
            for(var z=0;z<item2.list.length;z++){
                var item3 = item2.list[z];
                if(item3.customCat){
%>
                <tr data-id="<%=item3.id%>" data-customcat="true" data-name="<%=item3.name%>">
                <%}else{%>
                <tr data-id="<%=item3.id%>" data-customcat="false" data-name="<%=item3.name%>">
                <%}%>
                    <td><%=item1.name%></td>
                    <td><%=item2.name%></td>
                    <td><%=item3.name%></td>
                </tr>
<%
            }
        }
    }
%>
</table>
</script>

<script type="text/javascript" src="${BASE_PATH}/resources/script/page/common_template/goods_type.js?9b679b88ebf5ffc39268ca7ee166a59d" ></script>
<!-- 配件分类必须文件 end ----------------------- -->

