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

    .add-goods-dg .search-box .form-item {
        margin-right: 10px;
    }
    .add-goods-dg .search-result .yqx-btn {
        width: 50px;
        height: 32px;
        line-height: 32px;
        padding: 0;
    }
    .col1,.col2,.col3,.col4{
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
    .col1{
        width: 120px;
        text-align: left;
    }
    .col2{
        width: 200px;
        text-align: left;
    }
    .col3{
        width: 80px;
        text-align: right;
    }
    .col4{
        width: 100px;
        text-align: center;
    }
</style>
<script type="text/html" id="add-goods-tpl">
    <div class="add-goods-dg" data-tpl-ref="add-accessories-tpl">
        <div class="dialog-title">选择配件</div>
        <div class="search-box">
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="search_goodsNameLike" placeholder="配件名称"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="search_goodsFormatLike" placeholder="零件号"/>
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format js-goods-brand" placeholder="品牌"/>
                    <input type="hidden" name="search_brandId">
                </div>
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="search_carInfoLike" placeholder="请输入适配车型"/>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <input type="text" class="yqx-input format" name="search_depotLike" placeholder="仓库货位"/>
                </div>
                <a href="javascript:;" class="yqx-btn yqx-btn-3 js-goods-search-btn fr">搜索</a>
            </div>
        </div>
        <div class="search-result-head">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>
                       <div class="col1">零件号</div>
                    </th>
                    <th>
                        <div class="col2">配件名称</div>
                    </th>
                    <th><div class="col3">现库存</div> </th>
                    <th>
                        <div class="col4">操作(<a href="#" class="js-allgoods" style="text-decoration: underline">全部配件</a><a href="#"
                                                                                                         class="js-accessories-all"
                                                                                                         style="text-decoration: underline;display:none">全部配件</a>)
                        </div>
                    </th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="search-result">
            <table class="yqx-table">
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
            <% if (item.tqmallStatus == 1) {%>
            <dd data-key="<%= item.tqmallBrandId %>" class="yqx-select-option js-show-tips"><%= item.brandName %></dd>
            <% } else { %>
            <dd data-key="<%= item.id %>" class="yqx-select-option js-show-tips"><%= item.brandName %></dd>
            <% } %>
            <%}%>
        </dl>
    </div>
</script>
<script type="text/html" id="goods-add-tr">
    <%for(var i=0;i < json.data.length;i++){%>
    <%var item=json.data[i];%>
    <tr>
        <td>
            <div class="col1 js-show-tips"><%=item.format%></div>
        </td>
        <td>
            <div class="col2 js-show-tips"><%=item.name%></div>
        </td>
        <td>
            <div class="col3 js-show-tips"><%=item.stock%></div>
        </td>
        <td>
            <div class="col4 js-show-tips">
            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-goods-chosen">选择</a>
            <input type="hidden" value="<%=toStr(item)%>" class="toStr">
            <input type="hidden" value="<%=item.id%>" class="goods-ids">
            </div>
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
        ], function (st) {

            st.init({
                url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
                dom: '.add-goods-dg .js-goods-brand',
                showKey: "id",
                showValue: "brandName",
                canInput: true
            })

        });
    });


    function addGoodsInit(opt) {
        var doc = $(document),
                url = BASE_PATH + '/goods/paintExt/selectPaints',
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
                var isEmpty = 0;
                for (var pro in data) {
                    if (data.hasOwnProperty(pro)) {
                        isEmpty = 1;
                        break;
                    }
                }
                // 通过筛选的: 选中列表
                // 未通过筛选的: 盘点全部
                if (isEmpty == 1) {
                    $(".js-allgoods").hide();
                    $(".js-accessories-all").show();
                } else {
                    $(".js-allgoods").show();
                    $(".js-accessories-all").hide();
                }
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

                        $('input[name="goodsId"]').each(function () {
                            var val = this.value;
                            $('.js-goods-chosen').each(function(){
                                var json = JSON.parse($(this).siblings('.toStr').val());
                                if (val == json.id) {
                                    $(this).text('已选择');
                                    $(this).addClass('selected');
                                }
                            });

                        });
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

            //单条配件选择
            doc.on('click', '.js-goods-chosen', function () {
                var goodsIds = $(this).siblings('.goods-ids').val();
                var json = JSON.parse($(this).siblings('.toStr').val());
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
                        return false;
                    }
                } else {
                    return;
                }

                $.ajax({
                    url: BASE_PATH + '/goods/paintExt/getSelectPaintList',
                    data:{
                        goodsIds:goodsIds
                    },
                    success:function(result){
                        if(result.success){
                            args.callback && args.callback(result);
                        }
                    }
                })
            });

            // 盘点所有配件
            var allGoods = true;
            doc.on('click', '.js-allgoods', function () {
                var hasId = false;
                $('.js-goods-chosen').each(function(){
                    if (!$(this).hasClass('selected')) {
                        var json = JSON.parse($(this).siblings('.toStr').val());
                        $('input[name="goodsId"]').each(function () {
                            var val = this.value;
                            if (val == json.id) {
                                hasId = true;
                                return false;
                            }
                        });
                        // 判断重复选择服务
                        if (hasId) {
                            dg.warn('包含重复添加的配件');
                            return false;
                        }else{
                            allGoods = true;
                        }
                    };
                });

                if(!allGoods){
                    dg.warn('包含重复添加的配件');
                    return false;
                }

                if(!hasId){
                    var goodsIds = [];
                    $('.goods-ids').each(function () {
                        var ids = $(this).val();
                        goodsIds.push(ids);
                        return goodsIds;
                    });
                    $('.js-goods-chosen').each(function(){
                        $(this).text('已选择');
                        $(this).addClass('selected');
                    });
                    $.ajax({
                        url: BASE_PATH + '/goods/paintExt/getSelectPaintList?goodsIds=' + goodsIds,
                        success: function (result) {
                            if (result.success) {
                                args.allCallBack && args.allCallBack(result);
                                allGoods = false;
                            }
                        }
                    })
                }

            });
        });
    };
</script>