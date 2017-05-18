$(function () {
    seajs.use(['ajax', 'table', 'date', 'select', 'formData','dialog'],
        function (ajax, table, date, select, fd,dialog) {
            dialog.titleInit();
            var list = 'list';
            //采购人
            select.init({
                dom: ".js-agent",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name",
                pleaseSelect: true
            });
            
            table.init({
                url: BASE_PATH + "/shop/warehouse/out/out-list/list",
                fillid: list + 'Fill',
                pageid: list + 'Page',
                tplid: list + 'Tpl',
                formid: list + 'Form',
                enableSearchCache: true,
                callback: function () {
                    // enableSearchCache
                    var status = $('input[name="search_status"]').val();
                    if(status) {
                        $('.current-tab').removeClass('current-tab');
                        $('.list-tab[data-key="' + status + '"]').addClass('current-tab');
                    }

                    $('#listFill .js-money-font').each(function () {
                        var val = +$(this).text();
                        $(this).text(val.priceFormat());
                    });
                }
            });

            date.dpStartEnd({
                start: 'start',
                end: 'end'
            });



            $('.js-list-tab .list-tab').on('click', function () {
                var key = $(this).data('key');
                $('input[name="search_status"]')
                    .val(key || '');

                $('.normal-table').removeClass('hide');
                $('.draft-table').addClass('hide');
                $('.js-draft').removeClass('hover');

                $(this).parent().find('.current-tab')
                    .removeClass('current-tab');
                $(this).addClass('current-tab');
                $('.js-search-btn').click();
            });


            // 供应商搜索
            $(document).on('input', '.js-supplier', function () {
                var arr = $(this).parent().find('.yqx-select-options dd');
                var val = $.trim($(this).val()).toLowerCase();
                arr.each(function () {
                    var text = $(this).text().toLowerCase();

                    if (text.indexOf(val) > -1) {
                        $(this).removeClass('hide');
                    } else {
                        $(this).addClass('hide');
                    }
                });

            });


            //详情
            $(document).on('click', '.js-detail', function () {
                var id =$(this).data('id');
               window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id="+id+ "&refer=out-list";
            });
            //详情按钮
            $(document).on('click', '.js-inforlink', function () {
                var id =$(this).data('id');
                window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id="+id+ "&refer=out-list";
            });
            //作废
            $(document).on('click', '.js-abolish', function (event) {
                event.stopPropagation();
                var id =$(this).data('id');
                dialog.confirm('确认作废?',function(){
                    $.ajax({
                        url: BASE_PATH + '/shop/warehouse/out/abolish',
                        data: {
                            id : id
                        },
                        type: 'POST',
                        success: function (data) {
                            if (data.success) {
                                dialog.success("作废成功", function () {
                                    table.init({
                                        url: BASE_PATH + "/shop/warehouse/out/out-list/list",
                                        fillid: list + 'Fill',
                                        pageid: list + 'Page',
                                        tplid: list + 'Tpl',
                                        formid: list + 'Form',
                                        callback: function () {
                                            $('#listFill .js-money-font').each(function () {
                                                var val = +$(this).text();
                                                $(this).text(val.priceFormat());
                                            });
                                        }
                                    });
                                });
                            } else {
                                dialog.fail(data.errorMsg);
                            }
                        }
                    });
                })


            });

            //退货
            $(document).on('click', '.js-refund', function (event) {
                event.stopPropagation();
                var id =$(this).data('id');
                window.location.href = BASE_PATH + "/shop/warehouse/out/out-refund?id="+id+ "&refer=out-list";
            });

        });

});


