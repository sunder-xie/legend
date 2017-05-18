<#--
    选择服务公共模板，用到此模板页面，请在下面登记一下
    wurenyuan 2016-04-14 星期四

    用到的页面：
    yqx/page/order/speedily 快修快保
    yqx/page/shop/appoint/appoint-edit  新建预约单
    yqx/page/shop/order/common-add  综合维修单
 -->
<style>
    .layui-layer-page .layui-layer-content {
        overflow: initial;
    }

    .service-head {
        height: 54px;
        background: #232e49;
        color: #fff;
        text-align: center;
        line-height: 54px;

    }

    .service-body .yqx-table .service-data-btn{
        width: 50px;
        text-align: center;
        padding: 0;
    }

    .m-get-service .service-body .yqx-table td .service-data {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .m-get-service .service-head h2 {
        font-size: 16px;
        font-weight: 600;
    }

    .m-get-service .service-search {
        padding: 20px;
    }
    .service-search select {
        border: 1px solid #c9c9c9;
        width: 85px;
        height: 40px;
    }

    .m-get-service .service-search-result {
        height: 260px;
        overflow-y: scroll;
    }

    .m-get-service .service-search-input input {
        width: 300px;
        height: 40px;
    }

    .service-search-input, .service-search-btn {
        display: inline-block;
    }

    .service-search-btn button {
        margin-left: 8px;
    }

    .m-get-service table.yqx-table tr th {
        background: #f9f9f9;
        padding-left: 12px;
        text-align: left;
    }
    .m-get-service .yqx-table>tbody>tr td {
        padding: 4px;
        padding-left: 12px;
        text-align: left;
    }

    .m-get-service .yqx-table tr th:nth-child(3), .m-get-service .yqx-table tr td:nth-child(3) {
        text-align: right;
    }

    .m-get-service table.yqx-table tr th:nth-child(3) div {
        margin-right: -6px;
    }

    .m-get-service table.yqx-table tr th:nth-child(2) div,
    .m-get-service table.yqx-table tr td:nth-child(2) div {
        margin-left: 40%;
    }

    .m-get-service table.yqx-table tr th:last-child {
        text-align: center;
    }

    .m-get-service .yqx-table>tbody>tr>td:last-child{
        text-align: center;
        padding-left: 4px;
    }

    .m-get-service .select-category .form-item {
        width: 120px;
    }

    .m-get-service .select-category .form-item .yqx-input{
        padding-right: 32px;
    }
</style>
<div class="m-get-service hide" id="getservice" data-tpl-ref="get-service-tpl">
    <div class="service-head">
        <h2>选择服务</h2>
    </div>
    <div class="service-body">

        <div class="service-search">
            <div class="form-item select-category">
                <label>服务类别:</label>
                <div class="form-item">
                    <input class="yqx-input js-show-tips js-get-select-category"
                           placeholder="选择服务类别"
                            >
                    <input type="hidden" name="categoryId">
                    <span class="fa icon-angle-down"></span>
                </div>
            </div>
            <div class="service-search-input">
                <label>服务名称:</label>
                <input class="yqx-input search-input" placeholder="输入服务名称">
            </div>
            <div class="service-search-btn">
                <button class="yqx-btn yqx-btn-3 js-get-service-search">
                    搜索
                </button>
            </div>
        </div>
        <div class="service-datas">
            <table class="yqx-table">
                <thead>
                </thead>
            </table>
        </div>
        <div class="service-search-result">
            <table class="yqx-table">
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>
<script id="service-tbody" type="text/template">
    <%if(json.data != null){%>
    <% for(var i in json.data) { %>
    <tr service-id="<%=i%>">
        <% var j = 0; %>
        <% while(j < json.colspans) {%>
        <td>
                <% if(j === json.colspans - 1) {%>
                <button class="yqx-btn-small yqx-btn-3 service-data-btn">选择</button>
                <%} else {%>
                <div class="service-data js-show-tips" title="<%=json.data[i][json.descs[j]]%>">
                <%=json.data[i][ json.descs[j] ]%><%if(json.descs[j] === 'name' && json.data[i].flags === 'BPFW') {%><%='(共享中心服务)'%>
                <%}%>
                </div>
                <%}%>
            <% j++; %>
        </td>
        <%}%>
    </tr>
    <%}%>
    <%}%>
</script>

<script>
    function getService(opt) {
        var options = {};
        var tableData = {
            colspans: 0,
            tr: {}
        };
        var global = false;
        var currentAjax;
        var selectedData = {};
        var defaultOpt = {
            tableHead: [{
                name: '服务名称',
                desc: 'name'
            }, {
                name: '服务类别',
                desc: 'serviceCatName'
            }, {
                name: '工时费',
                desc: 'servicePrice'
            }, {
                name: '操作',
                type: 'btn'
            }],
            // chongfu
            allowRepeat: true,
            selectBtn: '<button class="yqx-btn yqx-btn-3 service-data-btn">选择</button>',
            ajaxData: {
                url: '/shop/shop_service_info/json',
                data: {
                    serviceName: '',
                    type: 1
                }
            }
        };

        defaultOpt.ajaxData.data = $.extend(true, defaultOpt.ajaxData.data, opt.data);
        if (opt.url) {
            defaultOpt.ajaxData.url = opt.url;
        }

        options = $.extend(true, defaultOpt, opt);
        var descs = defaultOpt.tableHead.map(function (e) {
            return e.desc
        });

        // 首先从 HTML 里移除
        var $box = $('.m-get-service').remove().removeClass('hide'),
                $table = $box.find('table');

        /********** 函数部分 开始 *************/

        function init(options) {
            // 显示 选择配件
            setserviceShow(options.dom, $table, options.ajaxData);

            makeTableHead($table, options.tableHead);

            setSearchEvent(options.ajaxData);

            setDataSelectEvent();

        }

        /** 显示主体
         * dom: 点击事件绑定的选择器
         * $table: 主体的table jquery 对象
         * ajaxData: 传入的相关数据
         */
        function setserviceShow(dom, $table, ajaxData) {
            $('body').on('click', dom, function () {
                seajs.use('dialog', function (dg) {
                    dg.open({
                        area: ['708px', '432px'],
                        // 给的是 html，所以 $table, $box 并不是弹窗的对象
                        content: $box[0].outerHTML,
                        // 关闭时清除数据
                        end: clearSelect
                    });
                });

                makeTableBody($('.m-get-service table'), ajaxData);
            });

        }

        function makeTableHead(node, head) {
            var $head = node.find('thead').length
                    ? node.find('thead')
                    : $('<thead></thead>').appendTo(node);
            var thHTML = ['<th><div>', '</div></th>'],
                    $currentTr, $ele;

            if (!head.length) {
                console.error('table head length = 0, fail');
                return false;
            }
            $currentTr = $('<tr></tr>').appendTo($head);
            for (var i in head) {
                $ele = $(thHTML[0] + head[i].name + thHTML[1]);

                $ele.find('div').addClass(head[i].className || 'form-label');
                if (head[i].must) {
                    $ele.find('div').addClass(head[i].mustClass || 'form-label-must')
                }

                $currentTr.append(
                        $ele
                );

            }

            tableData.colspans = head.length;

        }


        function makeTableBody($table, ajaxData) {
            var $tbody = $('.service-search-result tbody');

            var tdHTML = ['<td><div class="service-data">', '</div></td>'],
                    $tr;

            seajs.use('art', function (at) {
                if (ajaxData && ajaxData.url) {
                    setTimeout(function() {
                        currentAjax && currentAjax.abort();

                        currentAjax = $.ajax({
                            url: BASE_PATH + ajaxData.url,
                            data: ajaxData.data,
                            global: global
                        }).done(function (json) {
                            if (json && json.success) {
                                tableData.tr = json.data;
                                json.colspans = tableData.colspans;

                                json.descs = descs;
                                var trHTML = at("service-tbody", {json: json});
                                $table.find('tbody').eq(0).html(trHTML);
                            } else {
                                seajs.use('dialog', function (dl) {
                                    dl.msg(json && json.errorMsg, {icon: 2});
                                });
                            }
                        });
                    }, 500);
                } else if (ajaxData.data) {
                    tableData.tr = ajaxData.data;
                    ajaxData.colspans = tableData.colspans;

                    var html = at("service-tbody", {json: ajaxData});
                    $table.find('tbody').eq(0).html(html);
                }
            });


        }

        function setSearchEvent(ajaxData) {
            $('body').on('input.getService', '.m-get-service .search-input', function () {
                var val = $(this).val();
                var categoryId = $(".m-get-service input[name='categoryId']").val();

                if (ajaxData && ajaxData.data){
                    ajaxData.data.serviceName = val;
                    ajaxData.data.categoryId = categoryId;
                    makeTableBody($('.m-get-service table'), ajaxData);
                }
            });
        }

        // 数据选择
        function setDataSelectEvent() {
            seajs.use('dialog', function (dg) {
            $('body').on('click', '.service-data-btn', function () {
                var tr = $(this).parent().parent();
                var serviceId = [];

                var id = tr.attr('service-id'),
                        selected = tr.hasClass('service-selected');

                if(selected) {
                    return
                }
                $('input[name="serviceId"]').each(function () {
                    serviceId.push( $(this).val() );
                });

                if (!selected) {
                    $(this).text('已选择');
                    tr.addClass('service-selected');

                    // 判断重复选择服务
                    if(serviceId.indexOf(tableData.tr[id].id) > -1) {
                        if(options.allowRepeat === false) {
                            dg.warn('已添加该服务');
                            return;
                        }
                        dg.confirm('已添加此服务，继续添加该服务吗', function () {
                            selectedData[id] = tableData.tr[id];
                            if (options.callback && typeof options.callback === 'function') {
                                options.callback(tableData.tr[id]);
                            }
                        });
                        return;
                    }
                    selectedData[id] = tableData.tr[id];
                    if (options.callback && typeof options.callback === 'function') {
                        options.callback(tableData.tr[id]);
                    }
                }
            });
            });
        }

        function clearSelect() {
            var $selects = $('.service-selected');
            $selects.find('button').text('选择');
            $selects.removeClass('.service-selected');

            selectedData = {};
            options.ajaxData.data.serviceName = '';

            makeTableBody($table, options.ajaxData);
        }

        seajs.use('select', function (select) {
            select.init({
                url: BASE_PATH+"/shop/shop_service_cate/get_by_name",
                dom: '.js-get-select-category',
                canInput: true,
                callback: function () {
                    $('.m-get-service .search-input').trigger('input.getService');
                }
            });
        });

        $(document).on('click.service', '.m-get-service .js-get-service-search', function () {
                $('.m-get-service .search-input').trigger('input.getService');
            })
            .on('blur.service', '.js-get-select-category', function () {
            if($(this).val() == '') {
                $(this).parent().find('input[name="categoryId"]').val('');
                $('.m-get-service .search-input').trigger('input.getService');
            }
        });


        /********** 函数部分 结束 *************/

        /********** 模块初始化    *************/
        init(options);

        return {
            data: selectedData,
            allData: tableData
        }
    }
</script>