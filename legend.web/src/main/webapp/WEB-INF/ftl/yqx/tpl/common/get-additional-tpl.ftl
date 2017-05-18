<#--
    选择服务公共模板，用到此模板页面，请在下面登记一下
    wurenyuan 2016-04-14 星期四

    用到的页面
    yqx/page/shop/order/common-add  综合维修单
 -->
<style>
    .layui-layer-page .layui-layer-content {
        overflow: initial;
    }

    .additional-head {
        height: 54px;
        background: #232e49;
        color: #fff;
        text-align: center;
        line-height: 54px;

    }

    .additional-body .yqx-table .additional-data-btn {
        width: 50px;
        text-align: center;
        padding: 0;
    }

    .additional-body .yqx-table td .additional-data {
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .additional-head h2 {
        font-size: 16px;
        font-weight: 600;
    }

    .additional-search {
        padding: 20px;
    }

    .additional-search-result {
        height: 260px;
        overflow-y: scroll;
    }

    .additional-search-input input {
        width: 576px;
        height: 40px;
    }

    .additional-search-input, .additional-search-btn {
        display: inline-block;
    }

    .additional-search-btn button {
        margin-left: 8px;
    }

    .m-get-additional table.yqx-table tr th {
        background: #f9f9f9;
        padding-left: 12px;
    }
    .m-get-additional .yqx-table>tbody>tr>td {
        padding: 4px;
        padding-left: 12px;
    }

    .m-get-additional .yqx-table tr th:first-child, .m-get-additional .yqx-table tr td:first-child {
        text-align: left;
    }

    .m-get-additional .yqx-table tr th, .m-get-additional .yqx-table tr td {
        text-align: right;
    }

    .m-get-additional table.yqx-table tr th:nth-child(2) div,
    .m-get-additional table.yqx-table tr td:nth-child(2) div {
        margin-right: 50%;
    }

    .m-get-additional table.yqx-table tr th:last-child {
        text-align: center;
    }

    .m-get-additional .yqx-table>tbody>tr>td:last-child{
        text-align: center;
        padding-left: 4px;
    }
</style>
<div class="m-get-additional" id="getadditional" data-tpl-ref="get-additional-tpl">
    <div class="additional-head">
        <h2>选择附加</h2>
    </div>
    <div class="additional-body">
        <div class="additional-search">
            <div class="additional-search-input">
                <input class="yqx-input js-additional-input" placeholder="输入费用名称">
            </div><div class="additional-search-btn">
                <button class="yqx-btn yqx-btn-3 js-addition-search-btn">
                    搜索
                </button>
            </div>
        </div>
        <div class="additional-datas">
            <table class="yqx-table">
                <thead>
                </thead>
            </table>
        </div>
        <div class="additional-search-result">
            <table class="yqx-table">
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
</div>
<script id="additional-tbody" type="text/template">
    <%if(json.data != null){%>
    <% for(var i in json.data) { %>
    <tr additional-id="<%=i%>">
        <% var j = 0; %>
        <% while(j < json.colspans) {%>
        <td>
            <div class="additional-data" title="<% if(j === 0) {%><%=json.data[i][json.descs[j]]%> <%}%>">
                <% if(j === json.colspans - 1) {%>
                <button class="yqx-btn yqx-btn-3 additional-data-btn">选择</button>
                <%} else {%>
                <%=json.data[i][ json.descs[j] ]%>
                <%}%>
            </div>
            <% j++ %>
        </td>
        <%}%>
    </tr>
    <%}%>
    <%}%>
</script>

<script>
    function getadditional(opt) {
        var options = {};
        var global = false;
        var tableData = {
            colspans: 0,
            tr: {}
        };
        var selectedData = {}, currentAjax;
        var defaultOpt = {
            tableHead: [{
                name: '费用名称',
                desc: 'name'
            }, {
                name: '金额',
                desc: 'servicePrice'
            }, {
                name: '操作',
                type: 'btn'
            }],
            selectBtn: '<button class="yqx-btn yqx-btn-2 additional-data-btn">选择</button>',
            ajaxData: {
                url: '/shop/shop_service_info/json',
                data: {
                    serviceName: '',
                    type: 2
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
        var $box = $('.m-get-additional').remove(), $table = $box.find('table');

        /********** 函数部分 开始 *************/

        function init(options) {
            // 显示 选择配件
            setadditionalShow(options.dom, $table, options.ajaxData);

            makeTableHead($table, options.tableHead);

            setSearchEvent(options.ajaxData);

            setDataSelectEvent();

        }

        /** 显示主体
         * dom: 点击事件绑定的选择器
         * $table: 主体的table jquery 对象
         * ajaxData: 传入的相关数据
         */
        function setadditionalShow(dom, $table, ajaxData) {
            $('body').on('click', dom, function () {
                seajs.use('dialog', function (dg) {
                    $('body').css('overflow-y', 'hidden');
                    dg.open({
                        area: ['708px', '432px'],
                        // 给的是 html，所以 $table, $box 并不是弹窗的对象
                        content: $box[0].outerHTML,
                        // 关闭时清除数据
                        end: clearSelect
                    });
                });

                makeTableBody($('.m-get-additional table'), ajaxData);
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
            var $tbody = $('.additional-search-result tbody')

            var tdHTML = ['<td><div class="additional-data">', '</div></td>'],
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
                                var trHTML = at("additional-tbody", {json: json});
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

                    var html = at("additional-tbody", {json: ajaxData});
                    $table.find('tbody').eq(0).html(html);
                }
            });


        }

        $(document).on('click', '.m-get-additional .js-addition-search-btn', function () {
            $('.m-get-additional .js-additional-input').trigger('input');
        });

        function setSearchEvent(ajaxData) {
            $('body').on('input', '.m-get-additional .js-additional-input', function () {
                var val = $(this).val();

                if (ajaxData && ajaxData.data)
                    ajaxData.data.serviceName = val;
                makeTableBody($('.m-get-additional table'), ajaxData);
            });
        }

        // 数据选择
        function setDataSelectEvent() {
            $('body').on('click', '.additional-data-btn', function () {
                var tr = $(this).parent().parent().parent();
                var id = tr.attr('additional-id'),
                        selected = tr.hasClass('additional-selected');

                if (!selected) {
                    $(this).text('已选择');
                    tr.addClass('additional-selected');

                    selectedData[id] = tableData.tr[id];
                    if (options.callback && typeof options.callback === 'function') {
                        options.callback(tableData.tr[id]);
                    }
                }
            });
        }

        function clearSelect() {
            var $selects = $('.additional-selected');
            $selects.find('button').text('选择');
            $selects.removeClass('.additional-selected');

            selectedData = {};

            $('body').css('overflow-y', 'scroll');

        }

        /********** 函数部分 结束 *************/

        /********** 模块初始化    *************/
        init(options);

        return {
            data: selectedData,
            allData: tableData
        }
    }
</script>