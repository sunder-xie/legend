<#--
    新建盘点单——添加配件tpl
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
        width: 150px;
    }

    .add-goods-dg .search-box .format1 {
        width: 105px;
    }

    .add-goods-dg .search-box .goods-name {
        width: 260px;
    }

    .add-goods-dg .search-result .yqx-btn {
        width: 50px;
        height: 32px;
        line-height: 32px;
        padding: 0;
    }

    .add-goods-dg .search-result {
        max-height: 236px;
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
        text-align: left;
    }

    .add-goods-dg .yqx-table > tbody > tr td {
        padding: 4px;
        padding-left: 12px;
        text-align: left;
    }

    .add-goods-dg .yqx-table tr th:nth-child(4), .add-goods-dg .yqx-table tr td:nth-child(4),
    .add-goods-dg .yqx-table tr th:nth-child(5), .add-goods-dg .yqx-table tr td:nth-child(5) {
        text-align: right;
    }

    .add-goods-dg table.yqx-table tr th:nth-child(3) div {
        margin-right: -6px;
    }

    .add-goods-dg table.yqx-table tr th:nth-child(2) div,
    .add-goods-dg table.yqx-table tr td:nth-child(2) div {
        margin-left: 40%;
    }

    .add-goods-dg table.yqx-table tr th:last-child {
        text-align: center;
    }

    .add-goods-dg .yqx-table > tbody > tr > td:last-child {
        text-align: center;
        padding-left: 4px;
    }

    .add-goods-dg .col-1 {
        width: 130px;
    }

    .add-goods-dg .col-2 {
        width: 240px;
    }

    .add-goods-dg .col-3 {
        width: 100px;
    }

    .add-goods-dg .col-4 {
        width: 50px;
    }

    .add-goods-dg .search-box .form-item {
        margin-right: 10px;
    }
</style>
<script type="text/html" id="add-goods-tpl">
    <div class="add-goods-dg" data-tpl-ref="add-accessories-tpl">
        <div class="dialog-title">选择配件</div>
        <div class="search-box">
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="goodsName" placeholder="配件名称"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="goodsFormat" placeholder="零件号"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format goods_type_input" name="goodsCatLike"
                           placeholder="配件类别"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format js-goods-brand" placeholder="品牌"/>
                    <input type="hidden" name="brandId">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="carInfoLike" placeholder="请输入适配车型"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="depotLike" placeholder="仓库货位"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format1 js-sale" placeholder="上架状态"/>
                    <input type="hidden" name="onsaleStatus">
                    <span class="fa icon-angle-down"></span>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format1 js-stock" placeholder="库存状态"/>
                    <input type="hidden" name="zeroStockRange">
                    <span class="fa icon-angle-down"></span>
                </div>
                <a href="javascript:;" class="yqx-btn yqx-btn-3 js-goods-search-btn">搜索</a>
            </div>
        </div>
        <div class="search-result-head">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th class="col-1">零件号</th>
                    <th class="col-2">配件名称</th>
                    <th class="col-3">配件类别</th>
                    <th class="col-4">现库存</th>
                    <th>
                        操作(<a href="#" class="js-allgoods" style="text-decoration: underline">全部配件</a>)
                    </th>
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
<!-- 配件品牌模板 start -->
<script type="text/html" id="add-accessories-brand">
    <div class="yqx-select-options">
        <dl>
            <%for(var i=0;i< data.length;i++){%>
            <% var item = data[i]%>
            <dd data-key="<%= item.id %>" class="yqx-select-option js-show-tips"><%= item.brandName %></dd>
            <%}%>
        </dl>
    </div>
</script>
<script type="text/html" id="goods-add-tr">
    <%for(var i=0;i < json.data.length;i++){%>
    <%var item=json.data[i];%>
    <tr>
        <td class="col-1 js-show-tips"><%=item.format%></td>
        <td class="col-2 js-show-tips"><%=item.name%></td>
        <td class="col-3 js-show-tips"><%=item.goodsCat%></td>
        <td class="col-4 js-show-tips"><%=item.stock%></td>
        <td class="js-show-tips">
            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-goods-chosen">选择</a>
            <input type="hidden" value="<%=toStr(item)%>">
        </td>
    </tr>
    <%}%>
</script>
<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<script>
    $(function () {
        seajs.use([
            'select',
            'table',
            'dialog',
            'formData',
            'art'
        ], function (st, tb, dg, fd, at) {
            // 上架状态
            st.init({
                dom: '.js-sale',
                data: [{id: "2", name: "请选择"}, {id: "1", name: "上架"}, {id: "0", name: "下架"}],
                selectedKey: 1,
                showKey: "id",
                showValue: "name"
            });

            // 库存状态
            st.init({
                dom: '.js-stock',
                data: [{id: "1", name: "非0库存"}, {id: "0", name: "0库存"}],
                showKey: "id",
                pleaseSelect: true,
                showValue: "name"
            });

            st.init({
                url: BASE_PATH + '/shop/goods_brand/shop_list',
                dom: '.add-goods-dg .js-goods-brand',
                canInput: true,
                tplId: '#add-accessories-brand'
            })

        });
    });


    function addGoodsInit(opt) {
        var doc = $(document),
                url = BASE_PATH + '/shop/goods/json',
                args = $.extend({
                    dom: '',
                    callback: null,
                    allCallBack: null
                }, opt);
        var currentAjax;

        seajs.use([
            "ajax",
            "formData",
            "art",
            'select',
            'dialog'
        ], function (ax, fd, at, select, dg) {
            var currentDialogId;
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
            }

            //对象绑定弹框。
            doc.on('click', args.dom, function () {
                var html = at(args.tpl || 'add-goods-tpl', {});
                currentDialogId = dg.open({
                    content: html,
                    area: ['700px', '475px'],
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

                if (!$(this).hasClass('selected')) {
                    $('input[name="goodsId"]').each(function () {
                        var val = this.value;

                        if (val == json.id) {
                            hasId = true;
                            return false;
                        }
                    });

                    $(this).text('已选择');
                    $(this).addClass('selected');
                    // 判断重复选择服务
                    if (hasId) {
                        dg.warn('已添加该配件');
                    }
                } else {
                    return;
                }
                !hasId && args.callback && args.callback(json);
            });

            // 盘点所有配件
            doc.on('click.allGoods', '.js-allgoods', function (e) {
                var data = fd.get('.search-box');
                data.goodsType = 0;
                // 查询所有配件
                $.ajax({
                    url: BASE_PATH + '/shop/goods/all/json',
                    global: false,
                    data:data
                }).done(function (json) {
                    var existedGoods = {}, t;
                    var data = [];
                    if (json.success) {
                        $('input[name="goodsId"]').each(function () {
                            var val = this.value;

                            existedGoods[val] = true;
                        });

                        if(json.data && json.data.length) {
                            for (var i in json.data) {
                                t = json.data[i];
                                if(!existedGoods[ t.id ]) {
                                    data.push(t);
                                }
                            }

                        }
                        args.allCallBack && args.allCallBack(data);
                        dg.close(currentDialogId);
                    }
                });

                e.stopImmediatePropagation();
            });

        });

        doc.on('blur','.js-goods-brand',function(){
            var brandId = $('input[name="brandId"]').val();
            if( !brandId ){
                $('.js-goods-brand').val('')
            }
        });
    }
</script>