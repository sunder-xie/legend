/**
 * zmx  2016-06-01
 * 供应商付款
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'select',
        'date',
        'table',
        'downlist',
        'art',
        'dialog'
    ], function(st, dp, tb, dl, at, dg) {
        dg.titleInit();

        at.helper('debug', function (data) {
            console.log(data);
        });

        //供应商下拉列表
        st.init({
            dom: '.js-supplier',
            url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
            showValue: "supplierName",
            showKey: "id",
            canInput: true,
            pleaseSelect: true
        });
        //付款方式下拉列表
        st.init({
            dom: '.js-payment',
            url: BASE_PATH + '/shop/setting/supplier/get_pay_method',
            showKey: "code",
            showValue: "name",
            pleaseSelect: true
        });

        //日历
        dp.dpStartEnd({
            startSettings: {
                maxDate: '#F{$dp.$D(\'endDate\')}'
            },
            endSettings: {
                minDate: '#F{$dp.$D(\'startDate\')}'
            }
        });

        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/pay/pay-supplier/summary',
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
            callback : function () {
                $('#tableCon .js-money-font').each(function () {
                    var val = +$(this).text();
                    $(this).text(val.priceFormat());
                });
            }
        });
    });
//列表页点击进入详情页
    $(document).on('click', '.js-inforlink', function () {
        var supplierId = $(this).data('paySupplierId');
        var supplierName = $(this).data('paySupplierName');
        var startInTime = $("input[name='search_startInTime']").val();
        var endInTime = $("input[name='search_endInTime']").val();
        window.location.href = BASE_PATH + "/shop/settlement/pay/pay-supplier-list?supplierId="+supplierId
        +"&supplierName="+supplierName+"&startInTime="+startInTime+"&endInTime="+endInTime+"&refer=pay-supplier";
    });
});

$(document).on('click', '.export-excel', function () {
    seajs.use('formData', function (f) {
        params = f.get('#searchForm');
    });
    var url = BASE_PATH + "/shop/settlement/pay/pay-supplier/export?";
    $.each(params, function (index, element) {
        url += index + "=" + element + "&";
    });
    url = url.substr(0, url.length - 1);
    window.location.href = url;

});
