/**
 * zmx  2016-06-01
 * 公司付款明细账单
 */

$(function () {
    var $doc = $(document);

    seajs.use([
        'group',
        'art',
        'downlist',
        'formData',
        'select',
        'dialog',
        'table'
    ], function (gp, at, dl, fd, st, dg, tb) {

        //表格填充
        var ids = $('#ids').val();
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/pay/pay-supplier-list/list',
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
        $doc.on('click', '.js-pay', function () {
            var id = $(this).data('id');
            var arr = [id];
            jumpHref(arr);
        });
        //批量付款
        $doc.on('click', '.js-batch-pay', function () {
            var arr = [];
            $('.js-select').each(function () {
                if ($(this).is(":checked")) {
                    arr.push($(this).val());
                }
            });
            if (arr.length <= 0) {
                dg.warn("请选择要付款的记录");
                return;
            }

            jumpHref(arr);
        });
       //全部收款
        $doc.on('click', '.js-all-pay', function () {
            var  searchParam = fd.get("#searchForm");
            $.ajax({
                url: BASE_PATH + "/shop/settlement/pay/pay-supplier-list/all",
                data:searchParam,
                type: 'GET',
                success: function (data) {
                    if (data.success) {
                        jumpHref(data.data);
                    } else {
                        dg.warn(data.errorMsg);
                    }
                }
            });
        });
        function jumpHref(arr) {
            var ids = JSON.stringify(arr);
            $.ajax({
                url: BASE_PATH + "/shop/settlement/pay/pay-supplier-behavior/before",
                data: {
                    ids : ids
                    },
                type: 'GET',
                success: function (data) {
                    if (data.success) {
                        window.location.href = BASE_PATH + "/shop/settlement/pay/pay-supplier-behavior?ids=" + ids;
                    } else {
                        dg.warn(data.errorMsg);
                    }
                }
            });
        }
    });


    //表单按钮选中状态
    $doc.on('click', '.js-cdt-btn', function () {
        var $this = $(this);
        if ("search_part" == $this.attr("id")) {
            $('[name=search_paymentStatus]').val(1);
        } else {
            $('[name=search_paymentStatus]').val(null);
        }
        $this.addClass('condition-active').siblings().removeClass('condition-active');
    });

    //表格数据全选
    $doc.on('click', '.js-select-all', function () {
        var $this = $(this);
        if ($this.is(':checked')) {
            $(".js-select").prop('checked', true)
        } else {
            $(".js-select").prop('checked', false)
        }
    });
    $doc.on('click', '.js-select', function () {
        var $this = $(this);
        if (!($this.is(':checked'))) {
            $('.js-select-all').prop('checked', false);
        }
    });
    $(document).on('click', '.js-inforlink', function () {
        var id= $(this).data('id');
        window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id="+id;
    });

});