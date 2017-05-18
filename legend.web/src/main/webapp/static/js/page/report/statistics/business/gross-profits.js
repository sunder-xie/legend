$(function () {
    seajs.use(['ajax', 'dialog', 'formData', 'table', 'layer', 'select', 'date'],
        function (ajax, dialog, formData, table, layer, select, dp) {

            dp.dpStartEnd({
                startSettings: {
                    maxDate: '#F{$dp.$D(\'endDate\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'startDate\')}'
                },
                start: 'startDate',
                end: 'endDate'
            });

            $('.js-quick-select').each(function () {
                var t = getOffsetDay(+this.dataset.target);
                if (t.str) {
                    $(this).text(t.str);
                }

                $(this).attr('data-date',
                    t.date.toISOString().substr(0, 10));
            });

            $('.js-quick-select').on('click', function () {
                $("#startDate").val(this.dataset.date);
                $("#endDate").val(this.dataset.date);

                renderHTML();
            });
            $('.js-quick-select:last').trigger('click');

            $('.search-btn').on('click', function () {
                renderHTML();
            });

            function renderHTML() {
                var dd = {
                    startDate:$("#startDate").val(),
                    endDate:$("#endDate").val()
                };
                if(!dd.startDate || !dd.endDate) {
                    dialog.fail("请输入起止时间.");
                    return ;
                }
                $.ajax({
                    url:BASE_PATH + '/shop/report/gross-profits/summary',
                    data:dd,
                    success:function(result){
                        if(result.success){
                            if(result.success){
                                var total = $('.total-box');
                                total.find('.totalAmount').text((result.data.totalAmount).toFixed(2));
                                total.find('.totalInventoryAmount').text((result.data.totalInventoryAmount).toFixed(2));
                                total.find('.totalOrderCount').text((result.data.totalOrderCount));
                                total.find('.grossProfits').text((result.data.grossProfits).toFixed(2));
                            }
                        }
                    }
                })

                var newUrl = location.href.replace(/\#.*/, '') + '#' + 1;
                location.replace(newUrl);

                table.init({
                    url: BASE_PATH + '/shop/report/gross-profits/list',
                    fillid: 'grossProfitsFill',
                    tplid: 'grossProfitsTpl',
                    pageid: 'grossProfitsPage',
                    formid: 'searchForm',
                    // data: dd,
                    // fullString: true,
                    callback: function (html) {
                        var $table = $(html);

                        $('#grossProfitsFill').empty().append($table)
                        // 初次载入时避免出现一条边框
                            .parent().removeClass('hide');

                    }
                });
            };

            // 密码登录导出
            exportSecurity.tip({'title':'导出报表信息'});
            exportSecurity.confirm({
                dom: '#excelBtn',
                title: '经营报表—经营毛利明细',
                callback: function(json){
                    var url = BASE_PATH + '/shop/report/gross-profits/export?a=1';

                    var dd = {
                        startDate:$("#startDate").val(),
                        endDate:$("#endDate").val()
                    };
                    if(!dd.startDate || !dd.endDate) {
                        dialog.fail("请输入起止时间.");
                        return ;
                    }

                    for(var i in dd) {
                        url = url + '&' + i + '=' + dd[i];
                    }
                    window.location.href = url;
                }
            });

        });
    function getOffsetDay(offset) {
        var now = new Date();
        var t, d = now.getDate();
        var ret = {};

        t = now;
        t.setDate(d + offset);

        ret.date = t;

        return ret;
    }
});