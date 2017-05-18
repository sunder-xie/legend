<style>
.recommend-goods-dialog .yqx-table {
    margin: 15px 0 0;
}

/* 选择配件弹框 start */
.goods-dialog {
    width: 823px;
    padding-bottom: 50px;
    position: relative;
}

.goods-dialog .yqx-table .sale-price {
    padding-right: 15px;
}
.goods-dialog .container {
    padding:0 15px;
}
.goods-dialog .table-box {
    max-height: 390px;
    overflow-y: auto;
}

.goods-dialog footer {
    position: absolute;
    bottom: 15px;
    left: 15px;
    right: 15px;
}
.goods-dialog .main {
    padding: 0 0 5px;
    border-bottom: 1px solid #ddd;
}
.goods-dialog .dialog-nav {
    padding: 6px;
}

.goods-dialog .radio-box {
    display: inline-block;
    padding-top: 8px;
    font-size: 14px;
}

.goods-dialog .radio-box input {
    margin-right: 5px;
    vertical-align: baseline;
}

.goods-dialog .operate-box .yqx-btn {
    margin-left: 14px;
    height: 30px;
    line-height: 30px;
}

.goods-dialog .operate-box .yqx-input {
    width: 160px;
    height: 30px;
}

.goods-dialog .goods-name {
    width: 100%;
    word-break: break-all;
    max-height: initial;
}

.goods-dialog table .goods-name {
    padding-left: 0;
}
.goods-dialog .stock-unit {
    width: 50px;;
}

.goods-dialog .check {
    width: 15px;
}
.goods-dialog .no {
    width: 20px;
    text-align: left;
    padding-left: 0;
}
.goods-dialog .picked .no {
    width: 40px;
    text-align: left;
    padding-left: 4px;
}
</style>
<div class="hide choose-goods-dialog goods-dialog yqx-dialog" data-ref="choose-goods-tpl">
    <header>
        <h2 class="dialog-title">选择需要出售配件</h2>
    </header>
    <section class="container">
    <article class="main">
        <nav class="dialog-nav clearfix">
            <div class="radio-box">
                <label>
                    <input type="radio" class="js-choose-type"
                           name="choose" data-target=".table-box.all" checked>全部配件<i class="goods-count all-count"></i>
                </label><label>
                    <input type="radio" class="js-choose-type"
                           name="choose" data-target=".table-box.picked">已选配件<i class="goods-count picked-count"></i>
                </label>
            </div>
            <div class="operate-box fr" id="goodsForm">
                    <input class="yqx-input" name="goodsName"
                           placeholder="请输入配件名称"
                            ><button class="js-search-goods yqx-btn yqx-btn-3">查询</button>
            </div>
        </nav>
        <div class="table-box hide picked">
         <table class="yqx-table ">
            <thead>
                <tr>
                    <th class="check">
                        <label>
                            <input class="js-pick-all" type="checkbox">
                        </label>
                    </th>
                    <th class="no">序号</th>
                    <th class="name">配件名称</th>
                    <th class="stock-num">库存数量</th>
                    <th class="in-time">入库日期</th>
                    <th class="get-price">进货价(元)</th>
                    <th class="sale-price">销售价(元)</th>
                </tr>
            </thead>
            <tbody id="pickedGoodsFill"></tbody>
        </table>
        </div>
        <div class="table-box all">
            <table class="yqx-table yqx-table-hover">
            <thead>
                <tr>
                    <th class="check">
                        <label>
                            <input class="js-pick-all" type="checkbox" checked>
                        </label>
                    </th>
                    <th class="no">序号</th>
                    <th class="name">配件名称</th>
                    <th class="stock-num">库存数量</th>
                    <th class="in-time">入库日期</th>
                    <th class="get-price">进货价(元)</th>
                    <th class="sale-price">销售价(元)</th>
                </tr>
            </thead>
            <tbody id="goodsFill"></tbody>
            </table>
        </div>
        <div class="yqx-page" id="goodsPage"></div>
    </article>
    <footer>
        <button class="js-goods-confirm yqx-btn yqx-btn-2">确定</button>
        <button class="yqx-btn yqx-btn-1 fr js-close-dialog">取消</button>
    </footer>
        </section>
</div>
<div class="hide recommend-goods-dialog goods-dialog yqx-dialog" data-ref="choose-goods-tpl/recommend-goods-dialog">
    <header>
        <h2 class="dialog-title">建议您出售以下商品</h2>
    </header>
    <section class="container">
        <article class="main">
            <div class="table-box recommend-box">
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="check">
                            <label>
                                <input class="js-pick-all" type="checkbox" checked>
                            </label>
                        </th>
                        <th class="no">序号</th>
                        <th class="name">配件名称</th>
                        <th class="stock-num">库存数量</th>
                        <th class="in-time">入库日期</th>
                        <th class="get-price">进货价(元)</th>
                        <th class="sale-price">销售价(元)</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </article>
        <footer>
            <button class="js-goods-confirm yqx-btn yqx-btn-2">确定</button>
            <button class="yqx-btn yqx-btn-1 fr js-close-dialog">取消</button>
        </footer>
    </section>
</div>
<script type="text/template" id="goodsTpl">
    <%var item;%>
    <%for(var i in json.data.content) {%>
    <%item = json.data.content[i];%>
    <tr data-index="<%if(json.data.picked){%><%=item.index%><%} else {%><%=i%><%}%>">
        <td>
            <label>
                <input class="js-pick" type="checkbox">
            </label>
        </td>
        <%if(json.data.size){%>
        <td class="no"><%=(+i + 1) + json.data.number * json.data.size%></td>
        <%} else {%>
        <td class="no"><%=(+i + 1)%></td>
        <%}%>
    <#--配件名称-->
        <td class="name"><i class="goods-name js-show-tips"><%=item.name%></i></td>
        <td class="stock-num"><%=item.stock%><i class="measure-unit"><%=item.measureUnit%></i></td>
    <#--入库日期-->
        <td><%=item.lastInTimeStr%></td>
    <#--进货价-->
        <td class="get-price"><%=toPrice(item.inventoryPrice)%></td>
    <#--销售价-->
        <td class="sale-price"><%=toPrice(item.price)%></td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="pickedGoodsTpl">
    <%var item;%>
    <%for(var i in json.data.content) {%>
    <%item = json.data.content[i];%>
    <tr data-index="<%=item.index%>">
        <td>
            <label>
                <input class="js-pick" type="checkbox" <%if(json.map[item.id] && json.map[item.id].checked) {%><%='checked'%><%}%>>
            </label>
        </td>
        <%if(json.data.size){%>
        <td class="no"><%=(+i + 1) + json.data.number * json.data.size%></td>
        <%} else {%>
        <td class="no"><%=(+i + 1)%></td>
        <%}%>
        <#--配件名称-->
        <td class="name"><i class="goods-name js-show-tips"><%=item.name%></i></td>
        <td class="stock-num"><%=item.stock%><i class="measure-unit"><%=item.measureUnit%></i></td>
        <#--入库日期-->
        <td><%=item.lastInTimeStr%></td>
        <#--进货价-->
        <td class="get-price"><%=toPrice(item.inventoryPrice)%></td>
        <#--销售价-->
        <td class="sale-price"><%=toPrice(item.price)%></td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>
<script type="text/template" id="recommendGoodsTpl">
    <%var item;%>
    <%for(var i in json.data.content) {%>
    <%item = json.data.content[i];%>
    <tr data-index="<%=i%>">
        <td>
            <label>
                <input class="js-pick" type="checkbox" checked>
            </label>
        </td>
        <%if(json.data.size){%>
        <td class="no"><%=(+i + 1) + json.data.number * json.data.size%></td>
        <%} else {%>
        <td class="no"><%=(+i + 1)%></td>
        <%}%>
    <#--配件名称-->
        <td class="name"><i class="goods-name js-show-tips"><%=item.name%></i></td>
        <td class="stock-num"><%=item.stock%><i class="measure-unit"><%=item.measureUnit%></i></td>
    <#--入库日期-->
        <td><%=item.lastInTimeStr%></td>
    <#--进货价-->
        <td class="get-price"><%=toPrice(item.inventoryPrice)%></td>
    <#--销售价-->
        <td class="sale-price"><%=toPrice(item.price)%></td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <tr>
        <td colspan="7">暂无数据</td>
    </tr>
    <%}%>
</script>