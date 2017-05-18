/**
 * create by zmx 2016/12/13
 * 钣喷中心JS
 * */

$(function () {
    seajs.use(['art', 'dialog'], function (at, dg) {
        dg.titleInit();
        $(document).on('click', '.js-tab-item', function () {
            var desc = $(this).data('desc');
            var month = $('input[name="month"]').val();
            if (desc == 'performance') {
                getTable('/staffPerformance/processTimePerformance', 'timePerformanceTpl', $('#timePerformanceCon'));
                getTable('/staffPerformance/mechanicPerformance', 'mechanicPerformanceTpl', $('#mechanicPerformanceCon'));
                getTable('/staffPerformance/serviceSaPerformance', 'serviceSaPerformanceTpl', $('#serviceSaPerformanceCon'));
                function getTable(url, tpl, content) {
                    $.ajax({
                        url: BASE_PATH + url,
                        data: {
                            month: month
                        },
                        success: function (result) {
                            if (result.success) {
                                var html = at(tpl, {json: result});
                                content.html(html);
                            } else {
                                dg.fail(result.message);
                                return;
                            }
                        }

                    })
                }
            } else if (desc == 'matter') {
                getPaintTable('/bp/cost/accessiesCost', 'accessiesCostTpl', $('#accessiesCostCon'));
                getPaintTable('/bp/cost/paintCost', 'paintCostTpl', $('#paintCostCon'));

                function getPaintTable(url, tpl, content) {
                    $.ajax({
                        url: BASE_PATH + url,
                        data: {
                            searchMonth: month
                        },
                        success: function (result) {
                            if (result.success) {
                                var html = at(tpl, {json: result});
                                content.html(html);
                                var paintAmount = 0;
                                var totalCosting = 0;
                                $('input[name="paintAmount"]').each(function(){
                                    var paintPrice = Number($(this).val());
                                    paintAmount += paintPrice;
                                });
                                $('.js-cost').each(function(){
                                    var costing = Number($(this).text());
                                    totalCosting += costing
                                });
                                $('.js-paint-amount').text(paintAmount.toFixed(2));
                                $('.js-costing').text(totalCosting.toFixed(2))
                            } else {
                                dg.fail(result.message);
                                return;
                            }
                        }

                    })
                }
            }else if(desc == 'no-matter'){
                //非物料成本
                $.ajax({
                    url:BASE_PATH + '/bp/cost/nonGoodCost',
                    data:{
                        searchMonth:month
                    },
                    success:function(result){
                        if(result.success){
                            var html = at('tableTpl',{json:result});
                            $('#tableCon').html(html)
                        }else{
                            dg.fail(result.message)
                        }
                    }
                });
            } else if (desc == 'profit') {
                $.ajax({
                    url: BASE_PATH + '/bp/cost/getProfitLostBalance',
                    data: {
                        searchMonth: month
                    },
                    success: function (result) {
                        if (result.success) {
                            var html = at('profitTpl', {json: result});
                            $('#profitCon').html(html);
                        } else {
                            dg.fail(result.message);
                        }
                    }
                })
            }
        })
    })
});