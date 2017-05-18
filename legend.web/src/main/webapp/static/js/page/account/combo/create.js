/**
 * Created by zz on 2016/6/7.
 */

seajs.use([
    "art",
    "select",
    "ajax",
    'dialog',
    'downlist',
    'check',
    'date',
    'formData'
], function (at, st, ax, dg, dl, ck, date, fd) {


    dg.titleInit();
    var doc = $(document);
    doc.on('click','.delete',function(){
        $(this).parents('tr').remove();
    })

    date.datePicker('.Date');
    // 有效期选择
    $('.effectiveTime button').click(function(){
        if($(this).hasClass('self-btn')){
            $(this).parents('ul').siblings('.self-ul').show();
            $('.im-input').prop('disabled',true);

            // 自定义时间
            $('.self-ul').find('input').prop('disabled', false);
        }else{
            // 自定义时间
            $('.self-ul').find('input').prop('disabled', true);
            $(this).parents('ul').siblings('.self-ul').hide();
            $('.im-input').prop('disabled',false);
        }
        $(this).addClass('selected_btn');
        $(this).siblings('button').removeClass('selected_btn');
    });

    // 编辑时的有效期
    var customizeTime = $("#customizeTime").val();
    if(customizeTime == 1){
        $(".self-btn").click();
    }

    // 生效时间校验
    $('input[name=effectiveDateStr]').on('blur', checkExpireDate);
    $('input[name=expireDateStr]').on('blur', checkExpireDate);

    //提交
    doc.on('click', '.js-submit', function () {
        //校验
        var flag = true;
        seajs.use('check', function(ck) {
            if(!ck.check()) {
                flag =  false;
            }
        });
        var length=$('.js-move').length;
        if(length==0){
            seajs.use('dialog',function(dg){
                dg.warn("请添加服务！");
                flag = false;
            })
        }else{
            for(var i=0;i<length;i++) {
                var serviceCount = $("input[name='serviceCount']").eq(i).val();
                if (serviceCount == "") {
                    seajs.use('dialog', function (dg) {
                        dg.warn("请添加服务次数！");
                        flag = false;
                    })
                } else if (serviceCount <= 0 || parseInt(serviceCount) != serviceCount) {
                    seajs.use('dialog', function (dg) {
                        dg.warn("请输入大于零的整数！");
                        flag = false;
                    })
                }
            }
        }
        if(!flag) {
            return;
        }
        seajs.use(['check'], function(ck){
            var $container = $('.yqx-wrapper');
            ck.init();
            if (!ck.check()) {//作用域为整个页面的验证
                return false;
            }else {
                /**
                 * 组装后台数据
                 * @type {{}}
                 */
                //组装基础信息
                var data = {
                    id: $('input[name="comboInfoId"]').val(),
                    comboName: $('.basic-info input[name="comboName"]').val(),
                    effectivePeriodDays: $('.info.info-expire input[name="effectivePeriodDays"]').val(),
                    salePrice: $('input[name="salePrice"]').val(),
                    remark: $('textarea[name="remark"]').val()
                };
                var serviceList = [];
                //组装关联服务
                $("#orderServiceTB tr").each(function(idx, dd){
                    var d = $(dd);
                    var service = {serviceId: d.data("id")};
                    service.serviceName = d.find("input[name=serviceName]").val();
                    service.serviceCount = d.find("input[name=serviceCount]").val();
                    serviceList.push(service);
                });

                // 时间
                if($(".self-btn").hasClass("selected_btn")){
                    data.customizeTime = 1;
                } else {
                    data.customizeTime = 0;
                }
                data.effectiveDateStr = $container.find("input[name='effectiveDateStr']").val();
                data.expireDateStr = $container.find("input[name='expireDateStr']").val();

                data.content = serviceList;
                if(serviceList && serviceList.length){
                    var url = BASE_PATH+'/account/combo/create';
                    if($('#editCombo').val() === 'true') {
                        url = BASE_PATH + '/account/combo/update';
                    }
                    $.ajax({
                        url: url,
                        dataType: 'json',
                        contentType : 'application/json',
                        data: JSON.stringify(data),
                        type:"POST",
                        success:function(result){
                            seajs.use('dialog',function(dg){
                                if(result.success){
                                    dg.success('提交成功');
                                    window.location = BASE_PATH + "/account/setting?flag=2"
                                }else{
                                    dg.fail(result.message);
                                }
                            });

                        }
                    });
                }else{
                    seajs.use(['dialog'],function(dg){
                        dg.msg('请添加服务！');
                    });
                }
            }
        });

    });

    doc.on('click', '.reset', function () {
        $('input[type="text"]').val("");
        $('.js-move').remove();
    })
    ck.init();
    // 添加基本服务
    getService({
        // 点击按钮的选择器
        dom: '.js-get-service',
        // 回调函数，处理选择的数据
        callback: function (json) {
            if (json.suiteNum > 0) {
                $.ajax({
                    async: false,
                    type: 'get',
                    url: BASE_PATH + "/shop/shop_service_info/getPackageByServiceId",
                    data: {
                        serviceId: json.id
                    },
                    success: function (packageJson) {
                        if (packageJson.success) {
                            var html = at("serviceTpl", {json: packageJson.data.shopServiceInfoList});
                            $('#orderServiceTB').append(html);
                        }
                    }
                });
            } else {
                var jsons = new Array();
                jsons[0] = json;
                var html = at("serviceTpl", {json: jsons});
                $('#orderServiceTB').append(html);
            }
        }
    });


    //返回按钮
    $(document).on('click','.js-goback',function(){
        util.goBack();
    });

    function checkExpireDate () {
        var start = $('input[name=effectiveDateStr]');
        var end = $('input[name=expireDateStr]');
        var date = [ new Date( start.val() ), new Date( end.val() ) ];

        if(date[1] < date[0]) {
            $(this).val('');
            dg.warn('失效时间不能比生效时间早，请重新选择');
        }
    }

})

