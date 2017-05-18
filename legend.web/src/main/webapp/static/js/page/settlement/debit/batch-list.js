/**
 * zmx  2016-06-01
 * 批量收款列表
 */

$(function () {
    var $doc = $(document);

    seajs.use([
        'table',
        'downlist',
        'date',
        'dialog',
        'formData'
    ], function (tb, dl, dt, dg, fd) {

        dg.titleInit();

        var doc = $(document);
        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            showKey: 'license',
            hasTitle: false,
            notClearInput: true,
            dom: 'input[name="search_carLicenseLike"]',
            hasInput: false
        });

        //初始化客户单位下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/company',
            showKey: 'company',
            searchKey: 'company',
            hasTitle: false,
            notClearInput: true,
            dom: 'input[name="search_companyLike"]',
            hasInput: false
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
        doc.off("click", ".pick_all_btn").on("click", ".pick_all_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //本周
        doc.off("click", ".pick_this_week_btn").on("click", ".pick_this_week_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //上周
        doc.off("click", ".pick_last_week_btn").on("click", ".pick_last_week_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //本月
        doc.off("click", ".pick_this_month_btn").on("click", ".pick_this_month_btn", function () {
            var key = $(this).attr('key');
            initTimeAndFill(key);
        });

        //上月
        doc.off("click", ".pick_last_month_btn").on("click", ".pick_last_month_btn", function () {
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

        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/settlement/batch_pay_list',
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

        // 批量收款
        doc.on('click', '.js-batch-pay', function () {
            var orderIds = [];
            $.each($(':checkbox[name="checkId"]'), function () {
                if (this.checked) {
                    orderIds.push(this.value);
                }
            });
            if (orderIds.length == 0) {
                dg.warn("请勾选工单");
                return false;
            }
            if (orderIds.length > 50) {
                dg.warn("批量收款的工单数不能超过50");
                return false;
            }
            window.location.href = BASE_PATH + '/shop/settlement/debit/batch-debit?orderIds=' + orderIds.join(',');
        });

        //全部结算
        doc.on('click', '.js-pay-all', function () {
            dg.confirm("批量收款上限：50条 <br>确认要收款吗？", function () {
                $.ajax({
                    url: BASE_PATH + "/shop/settlement/batch_pay_list",
                    data: $.extend(fd.get("#searchForm"), {page: 1, size: 50}),
                    success: function (json) {
                        if (json.success) {
                            var orderInfoList = json.data.content;
                            var orderIds = [];
                            for (var i in orderInfoList) {
                                var orderInfo = orderInfoList[i];
                                var id = orderInfo.id;
                                orderIds.push(id);
                            }
                            if (orderIds.length > 0) {
                                window.location.href = BASE_PATH + '/shop/settlement/debit/batch-debit?orderIds=' + orderIds.join(',');
                            } else {
                                dg.info("没有可结算的工单", 3);
                                return false;
                            }
                        }
                    }
                });
            }, function () {
                return false;
            });
        });
    });

    //表单按钮选中状态
    $doc.on('click', '.js-cdt-btn', function () {
        var $this = $(this);
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


    $doc.on('click','.js-detail',function(){
        var orderId = $(this).data('orderId');
        window.location.href = BASE_PATH +'/shop/settlement/debit/order-detail?orderId='+ orderId;
    });

    $doc.on('click','.js-detail input',function(event){
        event.stopPropagation();
    })


});