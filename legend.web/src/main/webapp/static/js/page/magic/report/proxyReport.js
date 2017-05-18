/*
 *  zmx 2016/10/14
 *  钣喷受托数据统计
 */
$(function(){
    var doc = $(document);
    seajs.use([
        'dialog',
        'art',
        'date',
        'select'
    ],function(dg,at,dp,st){
        dg.titleInit();
        // 开始结束日期
        dp.dpStartEnd();

        //是否股东店
        st.init({
            dom: '.js-shareholder-select',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '-1',
                value: '请选择'
            },{
                key: '1',
                value: '是'
            },{
                key: '2',
                value: '否'
            }],
            callback:function(showKey,showValue){
                $('.key').val(showKey)
            },
            selectedKey:'-1'
        });

        //查询按钮
        doc.on('click','.js-search',function(){
            record();
        });
        //重置按钮
        doc.on('click','.js-reset',function(){
            $('#startDate').val('');
            $('#endDate').val('');
            $('.js-shareholder-select').val('请选择');
            $('.key').val('-1');
        });

        //跳转到详情页
        doc.on('click','.js-detail',function(){
            var startTime = $('.start-time').val(),
                endTime = $('.end-time').val(),
                shopId = $(this).find('.shop-id').val();
            if(startTime =='' && endTime == ''){
                window.location.href = BASE_PATH + '/proxy/report/toProxyReporyDtl?shopId=' + shopId
            }else{
                window.location.href = BASE_PATH + '/proxy/report/toProxyReporyDtl?startTime='+ startTime
                + '&endTime=' + endTime
                + '&shopId=' + shopId
            }
        });

        //排序
        doc.on('click','.js-car-up',function(){
            var carSortStatus = $(this).data('carSortStatus');
            var partnerStatus = $('.key').val();
            if( partnerStatus == ''){
                partnerStatus = -1
            }
            var data = {
                proxyStartTime:$('#startDate').val(),
                proxyEndTime:$('#endDate').val(),
                partnerStatus:partnerStatus,
                carSortStatus:carSortStatus
            };
            $.ajax({
                type:'post',
                url:BASE_PATH + '/proxy/report/proxyReport',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        var html = at('tableTpl',{json:result});
                        $('#tableCon').html(html);
                        $('.start-time').val( $('#startDate').val() );
                        $('.end-time').val( $('#endDate').val() );
                        if( $('.js-car-up').hasClass('icon-caret-up')){
                            $('.js-car-up').removeClass('icon-caret-up').addClass('icon-caret-down');
                            $('.js-car-up').data('car-sort-status','2');
                        }else{
                            $('.js-car-up').removeClass('icon-caret-down').addClass('icon-caret-up');
                            $('.js-car-up').data('car-sort-status','1');
                        }
                    }else{
                        dg.fail(result.message)
                    }
                }
            });

        });

        doc.on('click','.js-surface-up',function(){
            var surfaceStatus = $(this).data('surfaceStatus');
            var partnerStatus = $('.key').val();
            if( partnerStatus == ''){
                partnerStatus = -1
            }
            var data = {
                proxyStartTime:$('#startDate').val(),
                proxyEndTime:$('#endDate').val(),
                partnerStatus:partnerStatus,
                surfaceStatus:surfaceStatus
            };
            $.ajax({
                type:'post',
                url:BASE_PATH + '/proxy/report/proxyReport',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        var html = at('tableTpl',{json:result});
                        $('#tableCon').html(html);
                        $('.start-time').val( $('#startDate').val() );
                        $('.end-time').val( $('#endDate').val() );
                    }else{
                        dg.fail(result.message)
                    }
                }
            });
            if( $(this).hasClass('icon-caret-up')){
                $(this).removeClass('icon-caret-up').addClass('icon-caret-down');
                $(this).data('surface-status','2');
            }else{
                $(this).removeClass('icon-caret-down').addClass('icon-caret-up');
                $(this).data('surface-status','1');
            }
        });

        //表格数据填充
        function record(){
            var partnerStatus = $('.key').val();
            if( partnerStatus == ''){
                partnerStatus = -1
            }
            var data = {
                proxyStartTime:$('#startDate').val(),
                proxyEndTime:$('#endDate').val(),
                partnerStatus:partnerStatus
            };
            $.ajax({
                type:'post',
                url:BASE_PATH + '/proxy/report/proxyReport',
                data: JSON.stringify(data),
                dataType: 'json',
                contentType: "application/json",
                success:function(result){
                    if(result.success){
                        var html = at('tableTpl',{json:result});
                        $('#tableCon').html(html);
                        $('.start-time').val( $('#startDate').val() );
                        $('.end-time').val( $('#endDate').val() );
                        $('.isPartner').val(partnerStatus);

                    }else{
                        dg.fail(result.message)
                    }
                }
            });
        }

    })
});

//导出
$(document).on('click','.export-excel',function(){
    var startTime = $('.start-time').val(),
        endTime = $('.end-time').val(),
        partnerStatus = $(".isPartner").val();

    window.location.href = BASE_PATH + '/proxy/report/exportProxyReport?startTime='+startTime+'&endTime='+endTime+'&partnerStatus='+partnerStatus;
});