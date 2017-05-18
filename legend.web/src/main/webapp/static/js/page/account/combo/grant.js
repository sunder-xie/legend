/**
 * Created by majian on 16/6/12.
 */
$(function () {
    var a = '<div class="form-item license-num">\
        <a href><input type="text" name="carList" class="yqx-input yqx-input-small license-num1" value="" readonly>\
        </a>\
    </div>';

    var name = util.getPara('license');
    if(name){
        $("input[name='js-license-select']").val(name);
    }
    //填充数据
    seajs.use(['art', 'ajax', 'select'], function (tpl, ajax, st) {
        st.init({
            dom: ".js-combo-receiver",
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name"
        });
        $.ajax({
            url: BASE_PATH + "/account/combo/comboInfo/listEnable",
            success: function (result) {
                var html = tpl('chooseTpl', result);
                $('.js-choose-type').html(html);
            }
        });
    });
    //展开收起
    $(document).on('click', 'div.unfold', function () {
        $(this).hide();
        $(this).siblings('div.fold').show();
        $(this).parents('.info-item').siblings('.js-detail-box').show();
    });
    $(document).on('click', 'div.fold', function () {
        $(this).hide();
        $(this).siblings('div.unfold').show();
        $(this).parents('.info-item').siblings('.js-detail-box').hide();
    });

    $(document).on('click','.new-license',function(){
        seajs.use([
            'art',
            'dialog',
            'downlist'
        ],function(at,dg,dl){
            var html = at('addlicenseTpl', {});
            dg.open({
                area: ['500px','200px'],
                content: html
            });

            dl.init({
                url: BASE_PATH + '/shop/customer/search/mobile',
                searchKey:'com_license',
                showKey: 'license',
                hiddenKey:'id',
                dom: '.js-search',
                hasTitle: false,
                hasInput: false,
                autoFill: true,
                callbackFn:function(data, dd){
                    $.ajax({
                        url:BASE_PATH+"/account/queryAccountByLicense",
                        data:{carId:dd.id},
                        success:function(r){
                            if (r.success) {
                                $('.license-show').show();
                                $('.js-bundle').attr('disabled','');
                                $('.js-bundle').addClass('disabled-btn');
                                $('.un-bundle-mobile').text(r.data.accountInfo.mobile);
                                $('.carlicense-number').text($('.js-search').val());
                                var customerId = r.data.customer.id;
                                $(document).on('click','.js-link-bd',function(){
                                    window.location.href = BASE_PATH+'/account/detail?customerId='+ customerId;
                                });
                            }else{
                                $('.license-show').hide();
                                $('.js-bundle').removeAttr('disabled','');
                                $('.js-bundle').removeClass('disabled-btn');
                            }
                        }

                    });
                },
                clearCallbackFn: function () {
                    $('.license-show').hide();
                    $('.js-bundle').removeAttr('disabled','');
                    $('.js-bundle').removeClass('disabled-btn');
                }
            });

        });
    }).on('click','.js-bundle',function(){
        var license = $("input[name='license']").val();
        var accountId = $("#accountId").val();
        if(license == ''){
            dg.fail("车牌号不能为空");
            return;
        }
        $.ajax({
            url:BASE_PATH + "/account/bundleCar",
            type: 'post',
            data:{
                license:license,
                accountId : accountId
            },
            success: function (result) {
                if (result.success) {
                    //车辆已经绑定账户
                    if(result.data.id != '' && result.data.id != undefined){
                        $(".un-bundle-tip").removeClass("display: block");
                    }else{
                        //车辆绑定成功，返回
                        dg.success(result.data);
                        location.reload(true);
                    }
                }else{
                    $(".tip").text(result.errorMsg);
                }
            }
        });
    });
    //提交弹框
    $(document).on('click','.js-submit-btn',function(){

        var accountId = $("input[name='accountId']").val();
        var radio = $("input[type='radio']:checked").val();

        if(accountId == null || accountId == ''){
            seajs.use('dialog',function(dg){
                dg.warn('请选择车牌信息');
            })
        }else if(radio == null || radio == ''){
            seajs.use('dialog',function(dg){
                dg.warn('请选择计次卡类型');
            })
        } else {
            var result = {
                type: $('.selected .comboName').text(),
                amount: $('.selected .salePrice').text(),
                id: $("input[type='radio']:checked").val()
            };
            seajs.use(['art', 'dialog', 'check'], function (art, dl, check) {
                dl.open({
                    type: 1,
                    title: false,
                    area: ['600px', 'auto'],
                    content: art('collection', result)
                });

                check.init('.bounce-content');
            });
        }
    });
    $(document).on('click','input[type="radio"]',function(){
        $(".info-item").removeClass("selected");
        $(this).siblings('.info-item').addClass('selected');
    });

    //收款方式下拉
    seajs.use('select', function(st){
        st.init({
            dom: '.js-payment-select',
            url: BASE_PATH + '/shop/payment/get_payment',
            showKey: "id",
            showValue: "name"
        });
    });
    //车牌查询
    seajs.use(['downlist','dialog','art'], function(dl,dg,at) {
        dg.titleInit();
        dl.init({
            url:BASE_PATH+ '/shop/customer/search/mobile',
            searchKey:"com_license",
            tplId:'carLicenceTpl',
            showKey:'license',
            dom:'.js-license-select',
            hasInput:false,
            notClearInput: true,
            autoFill: true,
            callbackFn: function ($el, data, $scope) {
                $('input[name="carId"]').val(data.id);
                var carId=$('input[name="carId"]').val();
                $.ajax({
                    url: BASE_PATH + "/account/queryAccountByLicense?carId="+carId,
                    success: function (result) {
                        if(result.success){
                            $('.member-info').show();
                            var  t;
                            if(result.data&&result.data.customer){
                                $('.customerName').text(result.data.customer.customerName);
                                $('.mobile').text(result.data.customer.mobile);
                                $("input[name='accountId']").val(result.data.accountInfo.id);
                                var customerId=result.data.accountInfo.customerId;
                                $("input[name='customerId']").val(customerId);
                            }
                            if(result.data&&result.data.customerCarList){
                                $('.carLicense').empty();
                                for(var i=0;i<result.data.customerCarList.length;i++){
                                    var b=result.data.customerCarList[i].id;
                                    t = $(a.format('data-carid="' + b + '"'))
                                        .find('a')
                                        .attr('href', BASE_PATH + '/shop/customer/car-detail?refer=combo&id=' + b)
                                        .end();
                                    $('.carLicense').append(t);
                                    t.find("input[name='carList']").val(result.data.customerCarList[i].license);
                                }
                            }
                        }else{
                            dg.open({
                                type:1,
                                title:false,
                                area:['600px','auto'],
                                content:at("completeCarTpl",{message: result.errorMsg,carId:carId, carLicense: $(".member-search-box .search").val()})
                            });
                        }
                    }
                });
            }
        });
    });
    seajs.use(['art','dialog'],function(at,dg){
        var carId = $("input[name='carId']").val();
        //$('.js-search').val(carId);
        if (carId != "") {
            $.ajax({
                url:BASE_PATH+"/account/queryAccountByLicense",
                data:{
                    carId:carId
                },
                success:function(r){
                    accountInfoCallbackFn(r,carId);
                }
            });
        }
    });


    var accountInfoCallbackFn = function(r, carId) {
        seajs.use(["dialog", "art"], function (dg, at) {
            if (r.success) {
                $('.member-info').show();
                var t;
                if (r.data && r.data.customer) {
                    $('.customerName').text(r.data.customer.customerName);
                    $('.mobile').text(r.data.customer.mobile);
                    $("input[name='accountId']").val(r.data.accountInfo.id);
                    var customerId = r.data.accountInfo.customerId;
                    $("input[name='customerId']").val(customerId);
                }
                if(r.data&& r.data.customerCarList){
                    for(var i=0;i<r.data.customerCarList.length;i++){
                        var b=r.data.customerCarList[i].id;
                        t = $(a.format('data-carid="' + b + '"'));
                        $('.carLicense').append(t);
                        t.find("input[name='carList']").val(r.data.customerCarList[i].license);
                    }
                }
                else {
                    dg.open({
                        type: 1,
                        title: false,
                        area: ['600px', 'auto'],
                        content: at("completeCarTpl", {
                            message: r.errorMsg,
                            carId: carId,
                            carLicense: $(".member-search-box .js-search").val()
                        })
                    });
                }
            }
        })
    }

    //提交
    $(document).on("click",'.js-submit', function () {
        var data = {};
        var flag = true;
        seajs.use('check', function(ck) {
            if(!ck.check()) {
                flag =  false;
            }
        });
        if(!flag) {
            return;
        }
        var accountId = $("input[name='accountId']").val();
        data.comboInfoId = $("input[name='comboInfoId']").val();
        if(accountId != null){
            data.accountId = accountId;
        }
        data.recieverId = $("input[name='receiverId']").val();
        data.recieverName = $("input[name='receiverName']").val();//服务顾问
        data.amount = $("input[name='amount']").val();
        data.payAmount = $("input[name='payAmount']").val();
        data.paymentId = $("input[name='paymentId']").val();
        data.paymentName = $("input[name='paymentName']").val();//支付方式
        $.ajax({
            url: BASE_PATH + '/account/combo/recharge',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(data),
            type: "POST",
            success: function (result) {
                seajs.use('dialog', function (dg) {
                    if (result.success) {
                        dg.close();
                        dg.confirm('提交成功！是否打印回执单', function() {
                            util.print(BASE_PATH + '/account/combo/grantPrint?id=' +  + result.data.id,{
                                afterPrint: function () {
                                    window.location = BASE_PATH + "/account/detail?accountId=" + accountId+"&&flag=1";
                                }
                            });
                        }, function () {
                            window.location = BASE_PATH + "/account/detail?accountId=" + accountId+"&&flag=1";
                        }, ['打印', '取消']);
                    } else {
                        dg.fail(result.errorMsg);
                    }
                });

            }
        });
    }).on("click",".edit-btn",function(){
        var cid = $("input[name='customerId']").val();
        window.location.href = BASE_PATH + '/account/detail?customerId=' + cid;
    });

    $("body").on("click", ".js-new-car", function(){
        window.location = BASE_PATH + "/shop/customer/edit?page=1&&license=" + $(".js-license-select").val();
    });
    //车辆解绑
    $(document).on('click','.js-remove',function(){
        var $this = $(this),
            carid = $this.data("carid");
        seajs.use(['dialog'],function(dialog){
            dialog.confirm('确认解绑车辆？', function(){
                //确定
                $.ajax({
                    type: 'post',
                    data:{carId:carid},
                    url: BASE_PATH+"/account/unBundleCarById",
                    success:function(r){
                        if (r.success) {
                            $this.parent().remove();
                        } else {
                            dialog.fail(r.message);
                        }
                    }
                });
            }, function(){
                //取消
            });
        });
    });

});

