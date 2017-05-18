$(function () {
    seajs.use([
        'table',
        'select',
        'date',
        'dialog'
    ], function (tb, st, dt, dg) {

        var doc = $(document);

        //溢出隐藏
        dg.titleInit();

        //工单业务类型下拉选择
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
            showValue: "name",
            isClear: true,
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

        // 初始化表格
        tb.init({
            url: BASE_PATH + '/shop/warehouse/out/order-out-list/list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            //扩展参数,可选
            data: {size: 12},
            //关联查询表单id，可选
            formid: 'listForm',
            enableSearchCache: true,
            isfirstfill: false,
            //渲染表格数据完后的回调方法,可选
            callback: function () {
                // enableSearchCache
                var status = $('input[name="search_hasOut"]').val();
                if(status) {
                    $('.current-tab').removeClass('current-tab');
                    $('.list-tab[data-key="' + status + '"]').addClass('current-tab');
                }
            }
        });

        $('.js-list-tab .list-tab').on('click', function () {
            var key = $(this).data('key');

            $('#listForm').find('[name=search_hasOut]').val(key);
            $('.list-tab.current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');
            $('.js-search-btn').click();
        });

        $('.list-tab').eq(2).click();

        // 列表跳入
        doc.on('click', '.detail-list', function () {
            var itemId = $(this).data('itemid');
            window.location.href = BASE_PATH + "/shop/warehouse/out/order-quote-detail?orderId=" + itemId + "&refer=outbound";
        });

    });
});