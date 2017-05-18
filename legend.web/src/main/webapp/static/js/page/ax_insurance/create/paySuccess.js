/**
 * Created by huage on 2017/3/8.
 */
$(function () {
    addHeadStyle(6);
    var sn = $('#sn').val();
    $(document).on('click','.go-insure',function () {
        window.location.href = BASE_PATH + '/insurance/anxin/flow/insurance-flow'
    })
});


