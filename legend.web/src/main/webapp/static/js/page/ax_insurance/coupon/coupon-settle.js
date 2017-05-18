/**
 * Created by sky on 16/12/8.
 */

$(function () {
    seajs.use(['select', 'table', 'dialog', 'ajax'],
        function (st, tb, dg) {
            dg.titleInit();
            st.init({
                dom: '.js-settle-status',
                showKey: 'id',
                showValue: 'name',
                data: [
                    {
                        id: 0,
                        name: '请选择'
                    },{
                        id: 1,
                        name: '未结算'
                    },
                    {
                        id: 2,
                        name: '已结算'
                    }]
            });

            tb.init({
                url: BASE_PATH + '/insurance/anxin/coupon/coupon-settle/data',
                formid: 'searchForm',
                pageid: 'pageBox',
                tplid: 'contentList',
                fillid: 'contentListBox'
            });
        });
    
    /**
     * 互斥条件
     */
    $(document).on('click','input[name="search_currentMonth"]',function(){
        if( $(this).is(':checked')){
            $('input[name="search_mobile"]').val('').prop('disabled',true);
            $('.js-settle-status').val('').prop('disabled',true);
            $('input[name="search_settleStatus"]').val('').prop('disabled',true);
            $('input[name="search_currentMonth"]').val('true')
        }else{
            $('input[name="search_mobile"]').prop('disabled',false);
            $('.js-settle-status').prop('disabled',false);
            $('input[name="search_settleStatus"]').prop('disabled',false);
            $('input[name="search_currentMonth"]').val('')
        }
    })
});
