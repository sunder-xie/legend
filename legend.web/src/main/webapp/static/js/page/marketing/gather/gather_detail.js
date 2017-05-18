/**
 * Create by zmx 2016/12/23
 * 集客详情
 */
$(function(){
    var doc = $(document);
    seajs.use([
        'table',
        'date',
        'dialog',
        'art'
    ],function(tb,dp,dg,at){
        dg.titleInit();
        // 配置的日期
        dp.datePicker('.time', {
            dateFmt: 'yyyy-MM'
        });

        //员工选择
        doc.on('click','.js-staff',function(){
            $(this).addClass('staff-current').siblings().removeClass('staff-current');
            $('.js-search').trigger('click');
        });
        //搜索
        doc.on('click','.js-search',function(){
            var data = {
                userId: $('.staff-current').data('userId'),
                dateStr:$('.time').val()
            };
            getTable(data);
            thireeModule(data);
        });

       //导出
        doc.on('click','.js-export',function(){
            var dateStr = $('.time').val();
            var exportUrl = BASE_PATH + '/marketing/gather/detail/export?dateStr=' + dateStr;

            var userId = $('.staff-current').data('userId');
            if(userId) {
                exportUrl = exportUrl + '&userId=' + userId;
            }
            window.location.href = exportUrl;
        });


        //上月
        doc.on('click','.js-last-month',function(){
            var  mydate = new Date();
            var year = mydate.getFullYear();
            var month = mydate.getMonth();

            if( month == 0){
                month = 12;
                year = mydate.getFullYear() - 1;
            }

            $('.time').val( year + '-' + month );
            $('.js-search').trigger('click');
        });

        //本月
        doc.on('click','.js-this-month',function(){
            var  mydate = new Date();
            var year = mydate.getFullYear();
            var month = mydate.getMonth() + 1;
            $('.time').val( year + '-' + month);
            $('.js-search').trigger('click');
        });

        $('.js-this-month').trigger('click');

        function thireeModule(data){
            $.ajax({
                url:BASE_PATH + '/marketing/gather/detail/operate/stat',
                data:data,
                success:function(result){
                    if(result.success){
                        var html1 = at('operationTpl',{json:result});
                        var html2 = at('effictTpl',{json:result});
                        $('#operationCon').html(html1);
                        $('#effictCon').html(html2);
                    }
                }
            });

            $.ajax({
                url:BASE_PATH + '/marketing/gather/detail/performance/stat',
                data:data,
                success:function(result){
                    if(result.success){
                        var html3 = at('deductTpl',{json:result});
                        $('#deductCon').html(html3);
                    }
                }
            });
        };



        //表格填充
        function getTable(data){
            tb.init({
                //表格数据url，必需
                url: BASE_PATH + '/marketing/gather/detail/page',
                //表格数据目标填充id，必需
                fillid: 'activateCon',
                //分页容器id，必需
                pageid: 'paging',
                //表格模板id，必需
                tplid: 'activateTpl',
                //扩展参数,可选
                data: data,
                //关联查询表单id，可选
                formid: null
            });
        }

    });
});