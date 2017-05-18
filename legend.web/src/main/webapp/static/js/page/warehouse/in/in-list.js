$(function () {
    seajs.use(['ajax', 'table', 'date', 'select', 'formData','dialog'],
        function (ajax, table, date, select, fd,dialog) {
            var list = 'list',
                draftTable,
                tableRequest = null;
            var $doc = $(document),
                _startTime = util.getPara('startTime'),
                _endTime   = util.getPara('endTime');

            if( _startTime && _endTime
                && ( new Date(_startTime) < new Date(_endTime) )
                && ( new Date(_endTime) <= new Date() )
            ) {
                $('input[name=search_startTime]').val(_startTime);
                $('input[name=search_endTime]').val(_endTime);
            }

            dialog.titleInit();
            //采购人
            select.init({
                dom: ".js-agent",
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name",
                pleaseSelect: true
            });

            select.init({
                dom: '.js-supplier',
                url: BASE_PATH + '/shop/setting/supplier/get_supplier_list',
                showValue: "supplierName",
                showKey: "id",
                canInput: true,
                pleaseSelect: true
            });


            tableRequest = table.init({
                url: BASE_PATH + "/shop/warehouse/in/in-list/list",
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


            draftTable = table.init({
                url: BASE_PATH + "/shop/warehouse/in/in-list/list",
                fillid: 'draft' + 'Fill',
                pageid: 'draft' + 'Page',
                tplid: 'draft' + 'Tpl',
                formid: list + 'Form',
                isfirstfill: false,
                data: {
                    search_status: 'DRAFT'
                }
            });

            date.dpStartEnd({
                start: 'start',
                end: 'end'
            });

            $('.js-draft').on('click', function () {
                var key = $(this).data('key');
                var target = $(this).data('target');

                $('input[name="search_status"]')
                    .val(key || '');

                $('.current-tab').removeClass('current-tab');
                $('.normal-table').addClass('hide');
                $(target).removeClass('hide');

                $('.js-search-btn').click();
                $(this).addClass('hover');
            });

            $('.js-search-btn').click(function (e) {
                var currentTab = $('.list-tab.current-tab');

                if(currentTab.length) {
                    tableRequest(1);
                } else {
                    draftTable(1);
                }

                return false;
            });

            $('.js-list-tab .list-tab').on('click', function () {
                var key = $(this).data('key');
                $('input[name="search_status"]')
                    .val(key || '');

                exportSecurity["exportBrief"] = tabMap[key] || "入库单查询—入库单";

                $('.normal-table').removeClass('hide');
                $('.draft-table').addClass('hide');
                $('.js-draft').removeClass('hover');

                $(this).parent().find('.current-tab')
                    .removeClass('current-tab');
                $(this).addClass('current-tab');
                tableRequest(1);
            });

            // tab标签组
            var tabMap = {
                'LZRK': '入库单查询—蓝字入库',
                'HZRK': '入库单查询—红字入库',
                'LZZF': '入库单查询—蓝字作废',
                'HZZF': '入库单查询—红字作废'
            };

            // 导出
            exportSecurity.tip({'title':'导出入库单信息'});
            exportSecurity.confirm({
                dom: '.export-excel',
                title: '入库单查询—入库单',
                callback: function(json){
                    params = fd.get('#listForm');
                    var url = BASE_PATH + "/shop/warehouse/in/all-export?";
                    $.each(params, function (index, element) {
                        url += index + "=" + element + "&";
                    });
                    url = url.substr(0, url.length - 1);
                    window.location.href = url;
                }
            });

            //详情
            $(document).on('click', '.js-detail', function () {
               var id =$(this).data('id');
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id="+id+ "&refer=in-list";
            });
            //退货
            $(document).on('click', '.js-stock-refund', function (event) {
                event.stopPropagation();
                var id =$(this).data('id');
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-red?id="+id+ "&refer=in-list";
            });
            //转入库
            $doc.on('click', '.js-draft-stock', function (event) {
                var id = $(this).data('id'), that = this;
                event.stopPropagation();
                dialog.confirm('确认入库?', function () {
                    $.ajax({
                        url: BASE_PATH + '/shop/warehouse/in/in-detail/draft/stock',
                        data: {
                            id: id
                        },
                        type: 'POST',
                        success: function (data) {
                            if (data.success) {
                                dialog.success("入库成功", function () {
                                    draftTable(1);
                                });
                            } else {
                                dialog.fail(data.errorMsg);
                            }
                        }
                    });

                });

            });
            //作废
            $doc.on('click', '.js-abolish', function (event) {
                var id = $(this).data('id');
                event.stopPropagation();
                dialog.confirm('确认作废?', function () {
                    $.ajax({
                        url: BASE_PATH + '/shop/warehouse/in/abolish',
                        data: {
                            id: id
                        },
                        type: 'POST',
                        success: function (data) {
                            if (data.success) {
                                dialog.success("作废成功", function () {
                                    table.init({
                                        url: BASE_PATH + "/shop/warehouse/in/in-list/list",
                                        fillid: list + 'Fill',
                                        pageid: list + 'Page',
                                        tplid: list + 'Tpl',
                                        formid: list + 'Form'
                                    });
                                });
                            } else {
                                dialog.fail(data.errorMsg);
                            }
                        }
                    });

                });
                return;
            });
            //详情
            $(document).on('click', '.js-inforlink', function () {
                var id =$(this).data('id');
                window.location.href = BASE_PATH + "/shop/warehouse/in/in-detail?id="+id+ "&refer=in-list";
            });
        });

});


