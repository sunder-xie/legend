/**
 * zmx  2016-06-02
 * 付款流水记录
 */

$(function () {
    var $doc = $(document);

    seajs.use([
        'table',
        'dialog',
        'date',
        'select'
    ], function (tb, dg, dp, st) {

        dg.titleInit();
        //付款类型
        st.init({
            dom: '.js-pay-type',
            url: BASE_PATH + '/shop/settlement/pay/get-pay-type',
            showKey: "id",
            showValue: "typeName",
            allSelect: true
        });
        //付款方式下拉列表
        st.init({
            dom: '.js-payment',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name",
            allSelect: true
        });
        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/pay/pay-flow-list',
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

        dp.dpStartEnd({
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
    }

    //表单按钮选中状态
    $doc.on('click', '.js-cdt-btn', function () {
        var $this = $(this);
        $this.addClass('condition-active').siblings().removeClass('condition-active');
    });

});
    $(document).on('click', '.js-inforlink', function () {
        var id = $(this).data('id');
        window.location.href = BASE_PATH + "/shop/settlement/pay/pay-detail?id=" + id + "&refer=pay-flow";
    });


$(document).on('click', '.js-export', function () {
    seajs.use('formData', function (f) {
        params = f.get('#searchForm');
    });
    var url = BASE_PATH + "/shop/settlement/pay/export-pay-flow-list?";
    $.each(params, function (index, element) {
        url += index + "=" + element + "&";
    });
    url = url.substr(0, url.length - 1);
    window.location.href = url;

});