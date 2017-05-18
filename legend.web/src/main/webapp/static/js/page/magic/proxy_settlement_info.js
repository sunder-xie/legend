/*
 * hyx 2016-05-12
 * 对账单汇总页面
 */
var url=window.location.href;
var num=url.indexOf('flag=1');
if(num==-1){
    var flag=2;
}else{
    flag=1
}
$(function(){
    if(flag==1){
        $('#client').show();
        $('#server').hide();
        $('.js-headline').text('委托方对账单汇总');

    }else if(flag==2){
        $('#client').hide();
        $('#server').show();
        $('.js-headline').text('受托方对账单汇总');
    }



    seajs.use([
        'date'
    ],function(dp){
        // 交车时间插件
        dp.dpStartEnd({
            start: 'carStartTime',
            end: 'carEndTime'
        });
        dp.dpStartEnd({
            start: 'carStartDate',
            end: 'carEndDate'
        });

        // 委托时间插件
        dp.dpStartEnd({
            start: 'wtStartTime',
            end: 'wtEndTime'
        });
        dp.dpStartEnd({
            start: 'wtStartDate',
            end: 'wtEndDate'
        });
    });


    var proxyShopId = $(".proxyShopId").val();
    var proxyShopName=$(".proxyName").val();

    //委托方下拉列表
    seajs.use([
        'downlist',
        'dialog'
    ], function(dl,dg){
        dg.titleInit();
        //委托方下拉
        dl.init({
            url: BASE_PATH + '/proxy/settlement/share/list',
            searchKey: "shopNameKey",
            tplId:'entrustingTpl',
            showKey:'shopName',
            hiddenKey:'shopId',
            hiddenSelector: 'input[name=proxyShopId]',
            scope: 'order-right',
            dom: '.js-wt-select',
            hasInput: false,
            notClearInput: true
        });

        //受托方下拉
        dl.init({
            url: BASE_PATH + '/proxy/settlement/shop/list',
            searchKey: "shopNameKey",
            tplId: 'principalTpl',
            showKey:'name',
            hiddenKey:'id',
            hiddenSelector:'input[name=proxyShopId]',
            scope:'order-right',
            dom: '.js-st-select',
            hasInput: false,
            notClearInput: true,
        });

    });



    //委托单查询
    $(document).on('click','.js-wt-search',function () {
        seajs.use([
            'art',
            'ajax',
            'dialog'
        ],function (at,ax,dg) {
            var proxyStartTime = $('.getform1').find("input[name='proxyStartTime']").val();
            var proxyEndTime = $('.getform1').find("input[name='proxyEndTime']").val();
            var proxyShopId = $('.wt-shop-id').val();
            var data={
                proxyShopId:proxyShopId,
                startDate:proxyStartTime,
                endDate:proxyEndTime,
                proxyShopName:proxyShopName,
                shopFlag:1
            };

            $.ajax({
                url: BASE_PATH + '/proxy/settlement/summary',
                type:"get",
                data:data,
                success:function(result){
                    if (result.success) {
                        var html = at('detailTpl1',{json:result});
                        $('#detailInfo1').html(html);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        })

    });
    //受托单查询
    $(document).on('click','.js-st-search',function () {
        seajs.use([
            'art',
            'ajax',
            'dialog'
        ],function (at,ax,dg) {
            var proxyStartTime = $('.getform2').find("input[name='proxyStartTime']").val();
            var proxyEndTime = $('.getform2').find("input[name='proxyEndTime']").val();
            var proxyShopId = $('.wt-shop-id').val();
            var data={
                startDate:proxyStartTime,
                endDate:proxyEndTime,
                proxyShopId:proxyShopId,
                proxyShopName:proxyShopName,
                shopFlag:2
            };

            $.ajax({
                url: BASE_PATH + '/proxy/settlement/summary',
                type:"get",
                data:data,
                success:function(result){
                    if (result.success) {
                        var html = at('detailTpl2',{json:result});
                        $('#detailInfo2').html(html);
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        })

    });

    //查看详细 弹窗
    $(document).on('click','.check-detail',function(){
        seajs.use(['dialog','art'],function(dg,tpl){
            dg.open({
                type:1,
                title:false,
                area:['auto','300px'],
                content:tpl('dialogTpl',{})
            });
            tableFill();
        })
    });


    // 弹窗列表内容
    function tableFill(){
        seajs.use('table',function(tb){
            if(flag==1){
                var proxyStartTime = $('.getform1').find("input[name='proxyStartTime']").val();
                var proxyEndTime = $('.getform1').find("input[name='proxyEndTime']").val();
                var completeStartTime = $('.getform1').find("input[name='completeStartTime']").val();
                var completeEndTime = $('.getform1').find("input[name='completeEndTime']").val();
                var carLincense = $('.getform1').find("input[name='carLincense']").val();
            }else if(flag==2){
                var proxyStartTime = $('.getform2').find("input[name='proxyStartTime']").val();
                var proxyEndTime = $('.getform2').find("input[name='proxyEndTime']").val();
                var completeStartTime = $('.getform2').find("input[name='completeStartTime']").val();
                var completeEndTime = $('.getform2').find("input[name='completeEndTime']").val();
                var carLincense = $('.getform2').find("input[name='carLincense']").val();
            };

            tb.init({
                //表格数据url，必需
                url: BASE_PATH + '/proxy/settlement/info',
                //表格数据目标填充id，必需
                fillid: 'tableTpl',
                //分页容器id，必需
                pageid: 'paging',
                //表格模板id，必需
                tplid: 'tableCon',
                //扩展参数,可选
                data: {
                    proxyShopId:proxyShopId,
                    proxyStartTime:proxyStartTime,
                    proxyEndTime:proxyEndTime,
                    completeStartTime:completeStartTime,
                    completeEndTime:completeEndTime,
                    carLincense:carLincense,
                    shopFlag:flag
                }
            });
        });
    }

    //委托单导出
    $(document).on('click','.js-wt-export',function(){
        var proxyStartTime = $(".js-formInfo1").find("input[name='proxyStartTime']").val();
        var proxyEndTime = $(".js-formInfo1").find("input[name='proxyEndTime']").val();
        var completeStartTime = $(".js-formInfo1").find("input[name='completeStartTime']").val();
        var completeEndTime = $(".js-formInfo1").find("input[name='completeEndTime']").val();
        var carLincense = $(".js-formInfo1").find("input[name='carLincense']").val();
        var proxyShopIdWT = $('.wt-shop-id').val();

        window.location.href = BASE_PATH + '/proxy/settlement/export/info?'
            + 'proxyStartTime='
            + proxyStartTime
            + '&proxyEndTime='
            + proxyEndTime
            + '&completeStartTime='
            + completeStartTime
            + '&completeEndTime='
            + completeEndTime
            + '&carLincense='
            + carLincense
            + '&proxyShopId='
            + proxyShopIdWT
            + '&shopFlag=1';
    });

    //受托单导出
    $(document).on('click','.js-st-export',function(){
        var proxyStartTime = $(".js-formInfo2").find("input[name='proxyStartTime']").val();
        var proxyEndTime = $(".js-formInfo2").find("input[name='proxyEndTime']").val();
        var completeStartTime = $(".js-formInfo2").find("input[name='completeStartTime']").val();
        var completeEndTime = $(".js-formInfo2").find("input[name='completeEndTime']").val();
        var carLincense = $(".js-formInfo2").find("input[name='carLincense']").val();
        var proxyShopIdWT = $('.wt-shop-id').val();
        window.location.href = BASE_PATH + '/proxy/settlement/export/info?'
            + 'proxyStartTime='
            + proxyStartTime
            + '&proxyEndTime='
            + proxyEndTime
            + '&completeStartTime='
            + completeStartTime
            + '&completeEndTime='
            + completeEndTime
            + '&carLincense='
            + carLincense
            + '&proxyShopId='
            + proxyShopIdWT
            + '&shopFlag=2';
    });
});