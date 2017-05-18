/**
 * zmx  2016-06-03
 * 收款流水记录
 */

$(function () {
    var $doc = $(document);

    seajs.use([
        'table',
        'dialog',
        'select',
        'date',
        'formData'
    ], function (tb, dg, st, dt, fd) {

        dg.titleInit();

        //支付方式下拉列表
        st.init({
            dom: ".js-payment",
            url: BASE_PATH + '/shop/settlement/debit/flow/payment',
            showKey: "id",
            showValue: "name",
            allSelect: true
        });

        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/debit/find-flow-list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'tablePage',
            //表格模板id，必需
            tplid: 'tableTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: 'searchForm',
            //渲染表格数据完后的回调方法,可选
            callback: null
        });

        //类别下拉列表
        st.init({
            dom: '.js-category',
            url: BASE_PATH + '/shop/settlement/debit/type/list',
            showKey: "id",
            showValue: "typeName",
            allSelect: true
        });

        //日历绑定
        dt.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'endTime\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startTime\')}',
                maxDate: '%y-%M-%d'
            }
        });

        //全部
        $doc.off("click", ".pick_all_btn").on("click", ".pick_all_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //本周
        $doc.off("click", ".pick_this_week_btn").on("click", ".pick_this_week_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //上周
        $doc.off("click", ".pick_last_week_btn").on("click", ".pick_last_week_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //本月
        $doc.off("click", ".pick_this_month_btn").on("click", ".pick_this_month_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //上月
        $doc.off("click", ".pick_last_month_btn").on("click", ".pick_last_month_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        function initTimeAndFill(key) {
            $.ajax({
                url: BASE_PATH + "/shop/settlement/get_date",
                data: {"key": key},
                success: function (json) {
                    if (json.success) {
                        var data = json.data;
                        $("#startTime").val(data.startTime);
                        $("#endTime").val(data.endTime);

                        $('.js-search-btn').click();
                    }
                }
            });
        };

        // 导出
        exportSecurity.tip({'title':'导出收款流水'});
        exportSecurity.confirm({
            dom: '.js-export',
            title: '收款流水信息',
            callback: function(json){
                params = fd.get('#searchForm');
                var url = BASE_PATH + "/shop/settlement/debit/export-flow-list?";
                $.each(params, function (index, element) {
                    url += index + "=" + element + "&";
                });
                url = url.substr(0, url.length - 1);
                window.location.href = url;
            }
        });

    });

    //表单按钮选中状态
    $doc.on('click', '.js-cdt-btn', function () {
        var $this = $(this);
        $this.addClass('condition-active').siblings().removeClass('condition-active');
    });

    // 跳转收款单详情
    $doc.on('click', '.js-bill-flow', function () {
        var billId = $(this).data("bill-id");
        var flowId = $(this).data("flow-id");
        window.location.href = BASE_PATH + '/shop/settlement/debit/detail?billId=' + billId + '&flowId=' + flowId;
    });

});