$(function(){
    var couponTypeId = util.getPara('search_couponTypeId');
    //表格模块初始化
    seajs.use(['table','select','date','dialog'], function(tb, select, date, dialog ){

        //异步获取下拉列表数据
        select.init({
            dom: '#couponType',
            url: BASE_PATH + '/account/coupon/search?couponType=1',
            showKey: "id",
            showValue: "couponName",
            allSelect: true,
            selectedKey: couponTypeId == '' ? null : couponTypeId,
            selectedCallback: function(){
                tb.init({
                    url: BASE_PATH + '/shop/wechat/op/qry-acount-coupon-list',
                    fillid: 'tableList',
                    pageid: 'paging',
                    tplid: 'tableTpl',
                    data: {},
                    formid: 'searchForm',
                    isfirstfill: true
                });
            }
        });
        // 配置的日期
        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'endTime\')}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'startTime\')}'
            }
        })
    });


})

