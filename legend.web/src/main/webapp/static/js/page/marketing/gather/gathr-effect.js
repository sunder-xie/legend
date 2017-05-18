/**
 * Create by zmx 2016/12/20
 * 集客效果
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
        initialize();
        //初始化
        function initialize(){
            var  mydate = new Date();
            var year = mydate.getFullYear();
            var month = mydate.getMonth() + 1;
            var url = '/marketing/gather/effect/panhuo/stat';
            var tpl = 'activateTpl';
            var data = {
                userId: $('.staff-current').data('userId'),
                dateStr:year + '-' + month
            };
            $('.time').val(year + '-' + month);
            getTable(url,tpl,data);
        }


        //选项卡
        doc.on('click','.js-tab',function(){
            $(this).addClass('tab-hover').siblings().removeClass('tab-hover');
            $('.js-search').trigger('click');
        });


        //员工选择
        doc.on('click','.js-staff',function(){
            $(this).addClass('staff-current').siblings().removeClass('staff-current');
            $('.js-search').trigger('click');
        });
        //搜索
        doc.on('click','.js-search',function(){
            var tabIndex = $('.tab-hover').data('tab');
            var url;
            var data = {
                userId: $('.staff-current').data('userId'),
                dateStr:$('.time').val()
            };

            if(tabIndex == 1){
                url = '/marketing/gather/effect/panhuo/stat';
                getTable(url,'activateTpl',data);
            }else{
                url = '/marketing/gather/effect/laxin/stat';
                getTable(url,'oldNewTpl',data)
            }
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

        //详情页跳转
        doc.on('click','.js-details',function(){
            var url = BASE_PATH + '/marketing/gather/detail';
            var userId = $('.staff-current').data('userId');
            var dateStr = $('.time').val();
            if(userId) {
                url = url + '?userId=' + userId;
                if(dateStr) {
                    url = url + '&dateStr=' + dateStr;
                }
            } else {
                url = url + '?dateStr=' + dateStr;
            }
            window.location.href = url;
        });

        //表格填充
        function getTable(url,tpl,data){
            $.ajax({
                url: BASE_PATH + url,
                data:data,
                success:function(result){
                    if(result.success){
                        var html = at(tpl,{json:result});
                        $('#tabCon').html(html)
                    }else{
                        dg.fail(result.message)
                    }

                }
            })
        }

    });
});