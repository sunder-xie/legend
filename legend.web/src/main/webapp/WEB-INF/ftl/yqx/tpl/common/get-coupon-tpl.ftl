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

    .m-get-service .service-search-result {
        height: 260px;
        overflow-y: scroll;
    }

    .m-get-service .service-search-input input {
        width: 556px;
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
        text-align: left;
        width: 20%;
    }
    .m-get-service .yqx-table>tbody>tr td {
        padding: 4px;
        text-align: left;
        width: 23%;
    }

    .m-get-service .yqx-table>tbody>tr>td:last-child{
        text-align: left;
        width: 20%;
    }
</style>
<div class="m-get-service" id="getservice" data-tpl-ref="get-coupon-tpl">
    <div class="service-head">
        <h2>选择优惠券</h2>
    </div>
    <div class="service-body">
        <div class="service-search">
            <div class="service-search-input">
                <input class="yqx-input search-input" placeholder="输入优惠券名称">
            </div><div class="service-search-btn">
            <button class="yqx-btn yqx-btn-3">
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
            <div class="service-data" title="<%=json.data[i][json.descs[j]]%>">
                <%if(json.data[i].couponType == 2 && j == 2) {%>
                <%='-'%>
                <%} else {%>
                <%=json.data[i][ json.descs[j] ]%>
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
        var currentAjax;
        var selectedData = {};
        var defaultOpt = {
            tableHead: [{
                name: '优惠券名称',
                desc: 'couponName'
            }, {
                name: '优惠券类型',
                desc: 'couponTypeName'
            }, {
                name: '抵扣金额(元)',
                desc: 'discountAmount'
            }, {
                name: '有效期(天)',
                desc: 'effectivePeriodDays'
            }, {
                name: '操作',
                type: 'btn'
            }],
            selectBtn: '<button class="yqx-btn yqx-btn-3 service-data-btn">选择</button>',
            ajaxData: {
                url: '/account/coupon/search',
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
        var $box = $('.m-get-service').remove(), $table = $box.find('table');

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
                            global: false
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
            $('body').on('input', '.search-input', function () {
                var val = $(this).val();

                if (ajaxData && ajaxData.data)
                    ajaxData.data.serviceName = val;
                makeTableBody($('.m-get-service table'), ajaxData);
            });
        }

        // 数据选择
        function setDataSelectEvent() {
            $('body').on('click', '.service-data-btn', function () {
                var tr = $(this).parent().parent();

                var id = tr.attr('service-id'),
                        selected = tr.hasClass('service-selected');

                if (!selected) {
                    $(this).text('已选择');
                    tr.addClass('service-selected');

                    selectedData[id] = tableData.tr[id];
                    if (options.callback && typeof options.callback === 'function') {
                        options.callback(tableData.tr[id]);
                    }
                }
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

        /********** 函数部分 结束 *************/

        /********** 模块初始化    *************/
        init(options);

        return {
            data: selectedData,
            allData: tableData
        }
    }
</script>