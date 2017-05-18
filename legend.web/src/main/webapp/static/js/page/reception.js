/**
 * hyx 2016-04-15
 *  接车维修首页js
 */
$(function(){
    seajs.use('ajax');
    var doc = $(document);
        $.ajax({
            url:BASE_PATH+'/shop/order/work',
            success:function(result) {
                seajs.use('art', function (tpl) {
                    if (result.success) {
                        var data = tpl('tableTestTpl', result);
                        $('#tableTest').html(data);
                        $("#table").freezeHeader({ 'height': '300px' });
                    }
                });
            }
        });
    //click 跳转
    doc.on("click",'#table tbody tr',function(){
        var orderId= $(this).data("orderId");
        window.location.href=BASE_PATH + "/shop/order/detail?refer=reception&orderId=" + orderId;
    });
    //click 跳转 end
    /*
    * @params 依赖
    * @params factory
    */
    //车牌下拉
    seajs.use(['downlist','dialog'], function(dl,dg) {
        dg.titleInit();
        dl.init({
            url:BASE_PATH+ '/shop/customer/search/key',
            searchKey:"com_license",
            tplId:'carLicenceTpl',
            showKey:'searchValue',
            dom:'input[name="orderInfo.carLicense"]',
            hasInput:false,
            notClearInput: true
        });
        doc.on("click",".js-save",function(){
            var license= $.trim($(".license").val());
            if(license == ''){
                dg.warn("请先输入关键字查询");
                return false;
            }
            location.href = BASE_PATH+'/shop/customer/search/common?refer=home&keyword='+license;

        });
    });

    //页面链接下拉框
    doc.on("mouseover",".js-car-tab",function(){
        $(this).find(".link-content").show();
    });
    doc.on("mouseout",".js-car-tab",function(){
            $(this).find(".link-content").hide();
    });


    var elems = 'input[name="orderInfo.carLicense"],input[name="license"],input[name="plateNumber"]',
        elemsCopy = '';

    doc.on('keyup', elems, function () {
        elemsCopy = $(this).val();
    }).on('click', '.yqx-dl-no-data > a', function () {
            var url = BASE_PATH + '/shop/customer/edit';
            window.location.href = url + '?refer=home-search&license=' + elemsCopy;
            return false;
        });
    //$(".yqx-group-content").scroll(function(){
    //    $("#table").freezeHeader({ 'height': '300px' });
    //})
});