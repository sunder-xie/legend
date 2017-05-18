$(function () {
    seajs.use([
        'table',
        'select',
        'date',
        'listStyle',
        'dialog'
    ], function (tb, st, dt, li, dg) {


        //初始化接待人员的下拉数据
        st.init({
            dom: '#receiver',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name",
            isClear:true
        });

        var doc = $(document);

        dg.titleInit();

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

        //表格
        tb.init({
            //表格数据url，必需
             url: BASE_PATH + '/shop/warehouse/out/order-quote-list/list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //扩展参数,可选
            data: {size: 12},
            //表格模板id，必需
            tplid: 'tableTpl',
            enableSearchCache: true,
            //关联查询表单id，可选
            formid: 'listForm'
        });

        // 列表跳入
        doc.on('click', '.detail-list', function () {
            var itemId = $(this).data('itemid');
            var orderstatus = $(this).data('orderstatus');
            if (orderstatus == 'CJDD' || orderstatus == "DDBJ") {
                window.location.href = BASE_PATH + "/shop/warehouse/out/order-quote-detail?orderId=" + itemId + "&refer=quote";
            }
        });

        // 跳转到工单报价详情页
        doc.on('click', '.js-quote', function (event) {
            event.stopPropagation();
            var itemId = $(this).parents('tr').data('itemid');
            window.location.href = BASE_PATH + '/shop/warehouse/out/order-quote-detail?orderId='+ itemId + '&refer=quote';
        });
    });

});



