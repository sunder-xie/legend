/**
 * zmx 2016-04-07
 * 新建预约单页面。
 */

$(function(){
    var $document = $(document);

    //tab初始化
    $(".js-tab a").first().addClass('hover');
    $(".tabcon").eq(0).show();


    seajs.use([
        'art',
        'select',
        'downlist',
        'formData',
        'date',
        'check',
        'dialog',
        'date'
    ],function(at, st, dl, fd,date,ck,dg,dt){
        ck.init();
        dg.titleInit();

        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            tplId: 'carLicenceTpl',
            //tplCols: {'license':'车牌'},
            showKey: 'license',
            dom: 'input[name="license"]',
            hasInput: false,
            callbackFn: function(obj,item){
                $('input[name="customerName"]').val(item["customerName"]).blur();
                $('input[name="mobile"]').val(item["mobile"]).blur();
                $('input[name="customerCarId"]').val(item["id"]).blur();
            }
        });

        //预约时间框绑定最小时间
        date.datePicker('.js-appoint-time', {
            minDate: '%y-%M-%d',
            dateFmt: 'yyyy-MM-dd HH:mm',
            doubleCalendar: true
        });


        $document.on('blur','input[name="license"]',function(){
            var nolicense = $('input[name="license"]').val();
            if(nolicense == ''){
                $('input[name="customerName"]').val('');
                $('input[name="mobile"]').val('');
                $('input[name="customerCarId"]').val('');
            }
        })

        //预约登记人
        st.init({
            dom: ".js-select-registrant",
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });

        //添加服务，需要引入模板 tpl/common/get-service-tpl.ftl
        getService({
            // 点击按钮的选择器
            dom: '#select-server-btn',
            // 回调函数，处理选择的数据
            callback: function(json){
                if (json.suiteNum > 0) {
                    $.ajax({
                        async:false,
                        type: 'get',
                        url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                        data: {
                            serviceId:json.id
                        },
                        success:function(packageJson){
                            var shopServiceInfoList = packageJson.data.shopServiceInfoList;

                            if(packageJson.success){
                                var html = at("serviceTpl",{json:shopServiceInfoList});
                                $('#serviceBox').append(html);
                                var googsList = packageJson.data.goodsList;
                                var goodsHtml = at("goodsTpl",{json:googsList});
                                $('#goodsBox').append(goodsHtml);
                                //有物料显示提示框
                                if(googsList && googsList.length>0){
                                    var tip = $(".res-tab .tip");
                                    tip.show();
                                }
                            }
                        }
                    });
                } else {
                    var jsons = [json];
                    var html = at("serviceTpl",{json:jsons});
                    $('#serviceBox').append(html);
                    seajs.use('check', function(ck) {
                        ck.init();
                    });
                }
                calculateUtil.drawup();
            }
        });

        $document.on('input', '.js-number', function() {
            var val = $(this).val();

            $(this).val(checkFloat(val, 0));
        });

        $document.on('input', '.js-float-1', function() {
            var val = $(this).val();

            $(this).val(checkFloat(val, 1));
        });

        $document.on('input', '.js-float-2', function() {
            var val = $(this).val();

            $(this).val(checkFloat(val, 2));
        });


        //服务项目删除行
        $document.on('click','.js-delbtn',function(){
            $(this).parents('tr').remove();
            calculateUtil.drawup();
            if($(this).parents('tr').find('input[name="suiteNum"]').val()>0){
                dg.warn("此服务套餐包含配件物料，请自行删除！");
            }
        });

        //物料信息删除行
        $document.on('click','.js-goods-delbtn',function(){
            $(this).parents('tr').remove();
            calculateUtil.drawup();
        });

        //保存
        $document.on('click', '.js-save-btn', function () {
            //必填项校验
            //ck.check('.js-form');//验证.js-form作用域
            if(!ck.check()){//作用域为整个页面的验证
                return;
            }
            var appoint = fd.get('.js-appoint-box'),
                appointServiceVoList = [];
            appoint['appointAmount']=Number($(".js-amount").text());
            appoint['comment']=$("textarea[name='comment']").val();
            $('tr', '.serves-box').each(function () {
                var item = fd.get($(this));
                if(item.serviceId!=null){
                    appointServiceVoList.push(item);
                }
            });
            var appointEntityVo = {
                appoint : appoint,
                appointServiceJson:JSON.stringify(appointServiceVoList)
            };
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/shop/appoint/save',
                dataType:'json',
                contentType:'application/json',
                data: JSON.stringify(appointEntityVo),
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/appoint/appoint-detail?appointId=" + result.data.id;
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

    });

    //tab切换
    $document.on('click', '.js-tab a', function () {
        var $this = $(this),
            $tabcon = $('.tabcon');
        $this.addClass('current-item').siblings().removeClass('current-item');
        $tabcon.eq($this.index()).show().siblings('.tabcon').hide();
    });

    //返回按钮
    $document.on('click','.js-comeback',function(){
        util.goBack();
    });

    $document.on('click','.js-newgoods',function(){
        $('.js-tip').hide();
    })

    //init
    $document.ready(function() {
        calculateUtil.drawup();
    });


});

// calculate price
var calculateUtil = calculateUtil || {};
(function ($) {
    calculateUtil.drawup = function () {
        // 物料金额
        var serviceTotalAmount = (function () {
            var count = 0;
            var tbody = $("#serviceBox");
            var arr = $("tr", tbody).get().reverse();
            $.each(arr, function (i, ele) {
                // 服务金额
                var serviceAmount = Number($("input[name='serviceAmount']", ele).val());
                var discountAmount = Number($("input[name='discountAmount']", ele).val());
                count += serviceAmount - discountAmount;
            });
            return count;
        })();
        // 物料金额
        var goodsTotalAmount = (function () {
            var count = 0;
            var tbody = $("#goodsBox");
            var arr = $("tr", tbody).get().reverse();
            $.each(arr, function (i, ele) {
                var goodsAmount = Number($("input[name='goodsAmount']", ele).val());
                count += goodsAmount;
            });
            return count;
        })();
        // 服务总金额
        $(".js-service-amount").text(serviceTotalAmount.toFixed(2));
        // 物料总金额
        $(".js-goods-amount").text(goodsTotalAmount.toFixed(2));
        // 应收金额
        $(".js-amount").text(serviceTotalAmount.Jia(goodsTotalAmount).toFixed(2));
    };

})(jQuery);






