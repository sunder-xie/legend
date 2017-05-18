/**
 * 工单出库
 * zmx 2016-08-25
 */
$(function(){
    seajs.use([
        'select'
    ],function(st){

        //领料人下拉列表
        st.init({
            dom: '.js-picking',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库人下拉列表
        st.init({
            dom: '.js-out-person',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //出库类型下拉列表
        st.init({
            dom: '.js-out-type',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        $(document).on('click','.js-return',function(){
           util.goBack();
        });
    });
});