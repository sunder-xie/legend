/**
 * 2016-04-10 ch
 * 工单列表逻辑
 */
var cloumnLocalKey = "shop.order.order-list.tableColumnSet";
$(function () {
    seajs.use([
        'table',
        'select',
        'date',
        'listStyle',
        'dialog'
    ], function (tb, st, dt, li, dg) {
        var cardOrTable = li.getListStyle();
        listUrl = BASE_PATH + '/shop/order/order-list/list',
            doc = $(document);

        // TAB标签组
        var tabMap = {
            'DBJ': '待报价-工单信息',
            'YBJ': '已报价-工单信息',
            'YPG': '已派工-工单信息',
            'YWG': '已完工-工单信息',
            'YGZ': '已挂账-工单信息',
            'DDYFK': '已结清-工单信息'
        };

        dg.titleInit();
        function tableFill(url, firstFill) {
            if (firstFill !== false) {
                firstFill = true;
            }
            var enList = [],
                zhList = [],
                colums = li.getTableColumn(cloumnLocalKey);
            $.each(colums, function (index, element) {
                enList.push(element.field);
                zhList.push(element.name);
            });

            $('.tools-bar.select-btns-right .btn-setting').show();
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderListTable',
                //分页容器id，必需
                pageid: 'orderListTablePage',
                //表格模板id，必需
                tplid: 'orderListTableTpl',
                //如果模板需要自定义参数，可选
                tpldata: {
                    enList: enList,
                    zhList: zhList
                },
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback: function () {
                    $('#orderListTable .js-money-font').each(function () {
                        var val = +$(this).text();
                        $(this).text(val.priceFormat());
                    });
                },
                isfirstfill: firstFill

            });
        }

        function cardFill(url, firstFill) {
            if (firstFill !== false) {
                firstFill = true;
            }
            //卡片渲染
            $('.tools-bar.select-btns-right .btn-setting').hide();
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderListCard',
                //分页容器id，必需
                pageid: 'orderListCardPage',
                //表格模板id，必需
                tplid: 'orderListCardTpl',
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback: null,
                isfirstfill: firstFill
            });
        }

        if (!cardOrTable || cardOrTable === "table") {
            //表格渲染
            $('.js-order-list-table').addClass('hover');
            tableFill(listUrl, false);
        } else if (cardOrTable === "card") {
            //卡片渲染
            $('.js-order-list-card').addClass('hover');
            cardFill(listUrl, false);
        }

        $('.js-list-tab .list-tab').on('click', function () {

            var key = $(this).data('key');
            exportSecurity["exportBrief"] = tabMap[key] || "工单信息" ;
            var input = $('input[name="search_orderStatuss"]');
            console.log(exportSecurity["exportBrief"])
            if (input.length === 0) {
                $('<input name="search_orderStatuss" type="hidden">').val(key).appendTo($('#orderListForm'));
            }
            input.val(key);

            $('.list-tab.current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');

            $('.js-search-btn').click();
        });
        var tqmallVersion = $.trim($(".js-tqmall-version").val());
        if (tqmallVersion != '' && tqmallVersion) {
            $('.list-tab').eq(3).click();
        } else {
            $('.list-tab').eq(6).click();
        }

        //单击卡片渲染
        doc.on('click', '.js-order-list-card', function () {
            cardFill(listUrl);
            li.setListStyle("card");
            $('#orderListTable').empty();

            $(this).parent().find('.hover').removeClass('hover');
            $(this).addClass('hover');
        });
        //单击表格渲染
        doc.on('click', '.js-order-list-table', function () {
            tableFill(listUrl);
            li.setListStyle("table");
            $('#orderListCard').empty();

            $(this).parent().find('.hover').removeClass('hover');
            $(this).addClass('hover');
        });

        //全部类型下拉列表渲染
        st.init({
            dom: ".js-order-tag",
            url: BASE_PATH + '/shop/order/order-tag-list',
            showKey: "code",
            pleaseSelect: true,
            showValue: "name"
        });
        //工单业务类型下拉选择
        st.init({
            dom: '.js-order-type',
            url: BASE_PATH + '/shop/order/order_type/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "name"
        });
        //全部状态下拉列表渲染
        st.init({
            dom: '.js-order-status',
            url: BASE_PATH + '/shop/order/order-status-list',
            showKey: "key",
            pleaseSelect: true,
            showName: "name"
        });
        //日历绑定
        dt.dpStartEnd({
            start: 'start1',
            end: 'end1',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'end1\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'start1\')}',
                maxDate: '%y-%M-%d'
            }
        });

        //设置按钮事件
        doc.off('click.st').on('click.st', '.js-setting-btn', function () {
            var $this = $(this),
                settingBox = $('#orderListTable').find('.tools-bar'),
                isChecked = {};
            var colums = li.getTableColumn(cloumnLocalKey);
            $.each(colums, function (index, element) {
                isChecked[element.field] = true;
            });
            if (settingBox.is(':hidden')) {
                settingBox.slideDown('fast', function () {
                    //回显选择的表格字段
                    $(':checkbox', settingBox).each(function (i) {
                        var val = $(this).val();
                        if (isChecked[val]) {
                            $(this).prop("checked", "checked");
                        }
                    });
                });
            } else {
                settingBox.slideUp('fast');
            }
        });


        //表格字段动态配置
        doc.off('click.lb').on('click.lb', '.label-confirm', function () {
            var selectedList = (function () {
                var enArr = [],
                    zhArr = [],
                    colums = [];
                $('input:checked', '.setting-box').each(function () {
                    enArr.push($(this).val());
                    zhArr.push($(this).parent().text());
                    var colum = {
                        field: $(this).val(),
                        name: $(this).parent().text(),
                        display: true
                    };
                    colums.push(colum);
                });
                li.setTableColumn(cloumnLocalKey, colums);
                return {
                    enList: enArr,
                    zhList: zhArr
                }
            })();
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: listUrl,
                //表格数据目标填充id，必需
                fillid: 'orderListTable',
                //分页容器id，必需
                pageid: 'orderListTablePage',
                //表格模板id，必需
                tplid: 'orderListTableTpl',
                //如果模板需要自定义参数，可选
                tpldata: selectedList,
                //是否去远程查询数据，可选
                //remote: false,
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback: null,
                isfirstfill: false
            });
        });
        //是否委托渲染
        st.init({
            dom: '.js-proxy-type',
            showKey: "key",
            showValue: "value",
            data: [{
                key: "",
                value: "请选择"
            }, {
                key: 0,
                value: "否"
            }, {
                key: 1,
                value: "是"
            }]
        });
    });

    // 密码登录导出
    exportSecurity.tip({'title': '导出工单信息'});
    exportSecurity.confirm({
        dom: '.export-excel',
        title: '工单信息',
        callback: function (json) {
            seajs.use('formData', function (f) {
                params = f.get('#orderListForm');
            });
            var url = BASE_PATH + "/shop/order/order-list/export?";
            $.each(params, function (index, element) {
                url += index + "=" + element + "&";
            });
            url = url.substr(0, url.length - 1);
            window.location.href = url;
        }
    });

//列表页点击进入详情页
    $(document).on('click', '.js-inforlink', function () {
        var orderId = $(this).data('orderTableid');
        window.location.href = BASE_PATH + "/shop/order/detail?orderId=" + orderId + "&refer=order-list";
    });

    //校验工单导出按钮是否有权限
    if ($(".yqx-link").hasClass('export-excel')) {
        util.checkFunc("工单导出", ".export-excel");
    }
});


