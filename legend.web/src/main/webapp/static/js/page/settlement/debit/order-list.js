/**
 * zmx  2016-06-02
 * 工单收款
 */
$(function(){
    var $doc = $(document);

    // tab标签组
    var tabMap = {
        '0': '待结算工单信息',
        '1': '已挂账工单信息',
        '2': '已结清工单信息',
        '3': '无效工单信息'
    };

    seajs.use([
        'table',
        'dialog',
        'select',
        'listStyle',
        'date',
    ],function(tb,dg,st,li,dt){
        var cardOrTable = li.getListStyle(),
            listUrl = BASE_PATH + '/shop/settlement/list/list';
        dg.titleInit();

        // 日历绑定
        dt.datePicker('.js-date',{
            maxDate:'%y-%M-%d'
        });

        //初始化业务类型数据
        st.init({
            dom: '.js-order-type',
            url: BASE_PATH + '/shop/order/order_type/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "name"
        });

        //初始化接待人员的下拉数据
        st.init({
            dom: '#receiver',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        function tableFill(url,firstFill) {
            if(firstFill !== false) {
                firstFill = true;
            }
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
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'searchForm',
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
            if(firstFill !== false) {
                firstFill = true;
            }
            //卡片渲染
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
                formid: 'searchForm',
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

        //工单状态下拉列表渲染
        st.init({
            dom: '.js-order-status',
            url: BASE_PATH + '/shop/order/order-status-list',
            showKey: "key",
            pleaseSelect: true,
            showName: "name"
        });

        $('.js-list-tab .list-tab').on('click', function () {
            var key = $(this).data('key');
            exportSecurity["exportBrief"] = ["工单收款—",tabMap[key] || "全部工单信息"].join("");
            var text = $.trim($(this).text());
            var input = $('input[name="search_payStatus"]');
            if(key == 0 && key != null && key !== '') {
                $('#searchForm').append( $('<input value="DDWC" name="search_orderStatuss" class="ddwc" type="hidden">') );
                $('#searchForm').append( $('<input value="true" name="search_orderStatusSale" class="ddwc2" type="hidden">') );
                //$('#searchForm').append( $('<input value="0,1" name="search_proxyType" class="ddwc3" type="hidden">') );
            //} else if(key == 1 && key != null && key !== '') {
            //    $('.ddwc').remove();
            //    $('.ddwc2').remove();
            //    $('#searchForm').append( $('<input value="0,1" name="search_proxyType" class="ddwc3" type="hidden">') );
            } else {
                $('.ddwc').remove();
                $('.ddwc2').remove();
                //$('.ddwc3').remove();
            }

            if(input.length) {
                input.val(key);
            } else {
                $('<input name="search_payStatus" type="hidden">').val(key)
                    .appendTo($('#searchForm'));
            }
            $('.list-tab.current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');

            $('.js-search-btn').click();
        });
        $('.list-tab').eq(0).click();

        //单击卡片渲染
        $doc.on('click', '.js-order-list-card', function () {
            cardFill(listUrl);
            li.setListStyle("card");
            $('#orderListTable').empty();

            $(this).parent().find('.hover').removeClass('hover');
            $(this).addClass('hover');
        });
        //单击表格渲染
        $doc.on('click', '.js-order-list-table', function () {
            tableFill(listUrl);
            li.setListStyle("table");
            $('#orderListCard').empty();

            $(this).parent().find('.hover').removeClass('hover');
            $(this).addClass('hover');
        });

    });

    // 安全导出
    exportSecurity.tip({'title':'导出工单收款信息'});
    exportSecurity.confirm({
        dom: '.export-excel',
        title: '工单收款—全部工单信息',
        callback: function(json){
            seajs.use('formData', function (f) {
                params = f.get('#searchForm');
            });
            var url = BASE_PATH + "/shop/settlement/list/export?";
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
        window.location.href = BASE_PATH + "/shop/settlement/debit/order-detail?orderId=" + orderId + "&refer=order-list";
    });
});