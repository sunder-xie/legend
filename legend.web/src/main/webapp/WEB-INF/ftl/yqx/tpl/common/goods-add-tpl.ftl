<#-- 
    选择配件公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-13
    
    用到的页面：
    新增销售单: yqx/page/order/sell-good.ftl 
 -->
<style>
    .add-goods-dg {
        width: 100%;
    }

    .add-goods-dg .dialog-title {
        background: #232e49;
        color: #fff;
        text-align: center;
        font-size: 16px;
        font-weight: 900;
        line-height: 54px;
    }

    .add-goods-dg .search-box {
        padding: 20px;
    }

    .add-goods-dg .search-box .format {
        width: 145px;
    }

    .add-goods-dg .search-box .goods-name {
        width: 260px;
    }

    .add-goods-dg .search-result {
        max-height: 306px;
        overflow-y: scroll;
    }

    .add-goods-dg .yqx-table > thead > tr > th {
        background: #f9f9f9;
        color: #333;
        font-weight: 900;
    }

    .add-goods-dg .search-result::-webkit-scrollbar {
        width: 8px;
    }

    .add-goods-dg .search-result::-webkit-scrollbar-track-piece {
        background: #f9f9f9;
    }

    .add-goods-dg .search-result::-webkit-scrollbar-thumb {
        background: #a8ac9d;
    }

    /* .add-goods-dg .search-result-head{padding-right:8px;} */


    .add-goods-dg .search-result td {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .add-goods-dg table.yqx-table tr th {
        background: #f9f9f9;
        padding-left: 12px;
    }
    .add-goods-dg .yqx-table>tbody>tr td {
        padding: 4px 4px 4px 12px;
    }

    .add-goods-dg table.yqx-table tr th:nth-child(3) div {
        margin-right: -6px;
    }

    .add-goods-dg table.yqx-table tr th:nth-child(2) div,
    .add-goods-dg table.yqx-table tr td:nth-child(2) div {
        margin-left: 40%;
    }

    .add-goods-dg .yqx-table>tbody>tr>td:last-child{
        text-align: center;
        padding-left: 4px;
    }
    .add-goods-dg .col2 {
        width: 80px;
    }

    .add-goods-dg .search-box .form-item{
        margin-right: 10px;
    }
    .add-goods-dg .col3 {
        width: 110px;
    }
    .add-goods-dg .col4 {
        width: 190px;
    }
    .add-goods-dg .col1 {
        width: 70px;
    }
</style>
<script type="text/html" id="add-goods-tpl">
    <div class="add-goods-dg" data-tpl-ref="goods-add-tpl">
        <div class="dialog-title">选择配件</div>
        <div class="search-box text-center">
            <div class="form-item">
                <input type="text" class="yqx-input format" name="goodsFormat" placeholder="零件号"/>
            </div><div class="form-item">
            <input type="text" class="yqx-input format" name="carInfoLike" placeholder="车辆型号"/>
        </div><div class="form-item">
                <input type="text" class="yqx-input goods-name" name="goodsName" placeholder="配件名称"/>
            </div><a href="javascript:;" class="yqx-btn yqx-btn-3 js-goods-search-btn">搜索</a>
        </div>
        <div class="search-result-head">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th class="col3 text-l">零件号</th>
                    <th class="col4 text-l">配件名称</th>
                    <th class="col1 text-l">适配车型</th>
                    <th class="col2 text-r">售价</th>
                    <th class="col2">库存</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="search-result">
            <table class="yqx-table yqx-table-hidden yqx-table-hover">
                <tbody id="goods-result-list"></tbody>
            </table>
        </div>
    </div>
</script>
<script type="text/html" id="goods-add-tr">
    <% if (json && json.data && json.data.length) { %>
    <%for (var i = 0;i < json.data.length; i++) {%>
    <%var item = json.data[i];%>
    <tr>
        <td class="col3 text-l js-show-tips"><%=item.format%></td>
        <td class="col4 text-l js-show-tips"><%=item.name%></td>
        <td class="col1 text-l js-show-tips"><#-- 适配车型，显示4个车型，其它隐藏，鼠标移上显示全部。-->
            <% var carInfoList = item.carInfoList;%>
            <% if (carInfoList && carInfoList.length) { %>
            <% var carInfoLength = carInfoList.length;%>
            <% var tempHtml="";%>
            <% for(var j = 0; j < carInfoLength; j++) { %>
            <% var carBrandName = "" ;%>
            <% if(carInfoList[j].carBrandName) {%>
            <% carBrandName = carInfoList[j].carBrandName; %>
            <% } %>
            <% tempHtml += carBrandName; %>
            <% if((j + 1) < carInfoLength) { %>
            <% tempHtml += " | "%>
            <% }%>
            <% }%>
            <span title="<%= tempHtml%>">
            <% for(var j = 0; j < carInfoLength; j++) { %>
            <% if(j < 4) { %>
                <%= carInfoList[j].carBrandName;%>
                <% if(j < 3 && j< carInfoLength-1) { %><span>|</span><%}%>
            <% }%>
            <%}%>
            <% if(carInfoLength > 4) { %>...<%}%>
            </span>
            <%}%>
            <% if(item.carInfoStr != null) { %>
            <%=item.carInfoStr;%>
            <%}%>
        </td>
        <td class="col2 text-r js-show-tips"><%=item.price.toFixed(2)%></td>
        <td class="col2 js-show-tips"><%=item.stock%></td>
        <td>
            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-goods-chosen">选择</a>
            <input type="hidden" value="<%=toStr(item)%>">
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<script>
    function addGoodsInit(opt) {
        var doc = $(document),
                url = BASE_PATH + '/shop/goods/json',
                args = $.extend({
                    dom: '',
                    callback: function () {
                    }
                }, opt);
        var currentAjax;
        seajs.use([
            "ajax",
            "formData",
            "art",
            'dialog'
        ], function (ax, fd, at, dg) {
            //搜索事件
            var search = function () {
                var data = fd.get('.search-box');
                data.goodsType = 0;

                currentAjax && currentAjax.abort();
                currentAjax = $.ajax({
                    url: url,
                    global: false,
                    data: data
                }).done(function (json) {
                    if (json.success) {
                        var html = at(args.bodyTpl || 'goods-add-tr', {json: json});
                        $('#goods-result-list').html(html);

                    } else {
                        $('#goods-result-list').html('');
                    }
                });
            };

            //对象绑定弹框。
            doc.on('click', args.dom, function () {
                var html = at(args.tpl || 'add-goods-tpl', {});
                dg.open({
                    content: html,
                    area: ['700px', '475px']
                });
                search();
            });
            //json对象转化成字符串
            at.helper('toStr', function (obj) {
                return JSON.stringify(obj);
            });
            //按钮搜索
            doc.on('click', '.js-goods-search-btn', function () {
                search();
            });
            //输入的时候搜索
            doc.on('keyup', '.format,.goods-name', function () {
                search();
            });
            //单条配件选择
            doc.on('click', '.js-goods-chosen', function () {
                var json = JSON.parse($(this).siblings('input').val());
                var hasId = false;

                if ( !$(this).hasClass('selected') ) {
                    $('input[name="goodsId"]').each(function () {
                        var val = this.value;

                        if(val == json.id) {
                            hasId = true;
                            return false;
                        }
                    });

                    $(this).text('已选择');
                    $(this).addClass('selected');
                    // 判断重复选择服务
                    if(hasId && !args.notAllowRepeat) {
                        dg.confirm('已添加此配件，继续添加该配件吗', function () {
                            args.callback && args.callback(json);
                        });
                    }
                    if(hasId && args.notAllowRepeat) {
                        dg.warn('已添加该配件');
                    }
                } else {
                    return;
                }
                !hasId && args.callback && args.callback(json);
            });

        });
    }
</script>