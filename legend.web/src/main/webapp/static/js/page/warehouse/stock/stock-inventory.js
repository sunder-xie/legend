/**
 * 库存查询
 * zmx 2016-08-08
 */

$(function () {
    var doc = $(document);
    seajs.use([
        'date',
        'table',
        'dialog'
    ], function (dp, tb, dg) {

        //开始结束日期
        dp.dpStartEnd();
        //溢出隐藏
        dg.titleInit();

        //表格
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/warehouse/stock/records_list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'pagingCon',
            //表格模板id，必需
            tplid: 'tableTpl',
            data: {
                size: 12
            },
            //关联查询表单id，可选
            formid: 'listForm',
            enableSearchCache: true,
            isfirstfill: false,
            callback: function () {
                // 计算盘盈\盘亏金额
                var itemIds = [];
                // enableSearchCache
                var status = $('input[name="search_status"]').val();
                if(status) {
                    $('.current-tab').removeClass('current-tab');
                    $('.list-tab[data-status="' + status + '"]').addClass('current-tab');
                }

                $.each($('input[name="itemId"]'), function () {
                    itemIds.push(this.value)
                });
                if (itemIds.length > 0) {
                    $.ajax({
                        type: 'get',
                        url: BASE_PATH + '/shop/warehouse/stock/stock-inventory-calculate',
                        data: {itemids: itemIds.join(",").toString()},
                        success: function (result) {
                            if (result.success) {
                                var records = result["data"];
                                for (var recordIndex in records) {
                                    var record = records[recordIndex];
                                    var recordId = record["recordId"];
                                    var row = $("#TR_" + recordId);
                                    var kuiTotal = Math.abs(Number(record["kuiTotal"]));
                                    var kuiTotalAmount = Math.abs(Number(record["kuiTotalAmount"].toFixed(2)));
                                    var yinTotal = Math.abs(Number(record["yinTotal"]));
                                    var yinTotalAmount = Math.abs(Number(record["yinTotalAmount"].toFixed(2)));
                                    $('td[name="kuiTotal"]', row).html((kuiTotal == 0 ? '' : '-') + kuiTotal);
                                    $('td[name="kuiTotalAmount"]', row).html('&yen;' + (kuiTotalAmount == 0 ? '' : '-') + kuiTotalAmount);
                                    $('td[name="yinTotal"]', row).html((yinTotal == 0 ? '' : '+') + yinTotal);
                                    $('td[name="yinTotalAmount"]', row).html('&yen;' + (yinTotalAmount == 0 ? '' : '+') + yinTotalAmount);
                                }
                            } else {
                                return false;
                            }
                        }
                    });
                }
            },
        });

        //条件选项卡
        doc.on('click', '.js-list-tab .list-tab', function () {
            // 筛选条件
            var status = $(this).data('status');
            $('input[name="search_status"]').val(status);
            $('.list-tab.current-tab').removeClass('current-tab');
            $(this).addClass('current-tab');
            $('.js-search-btn').click();
        });

        $('.list-tab').eq(2).click();

        // 删除
        $(document).on('click', ".js-record_del", function (event) {
            event.stopPropagation();
            var recordId = $(this).attr('record_id');
            dg.confirm("您确定要把草稿删除吗?", function () {
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/warehouse/stock/record_del',
                    data: {id: recordId},
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功");
                            window.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory";
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            }, function () {
                return false;
            });
        });
        //编辑
        $(document).on('click','.js-edit',function(event){
            event.stopPropagation();
            var itemId = $(this).parents('tr').data('itemId');
            window.location.href = BASE_PATH + '/shop/warehouse/stock/stock-inventory-edit?itemid='+ itemId
        });

        //查看
        $(document).on('click','.js-see',function(event){
            event.stopPropagation();
            var itemId = $(this).parents('tr').data('itemId');
            window.location.href = BASE_PATH + '/shop/warehouse/stock/stock-inventory-detail?itemid='+ itemId
        });

        // 列表跳入
        doc.on('click', '.detail-list', function () {
            var itemId = $(this).data('itemId');
            window.location.href = BASE_PATH + "/shop/warehouse/stock/stock-inventory-detail?itemid="+ itemId;
        });


    });

});
