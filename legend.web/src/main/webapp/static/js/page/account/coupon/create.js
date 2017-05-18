/**
 * Created by hyx on 16/6/7.
 * 优惠券类型
 */
$(function () {
    // 优惠券默认类型
    // 现金券
    var limit = $("input[name='limitAmount']").val() || 0;

    // 选择服务
    getService({
        dom: '.js-add-service',
        callback: serviceCallback
    });

    $('.js-tab-item').click(function () {
        var target = $(this).data('target');
        $(this).addClass('current-item');
        $(this).siblings().removeClass('current-item');

        $('.info-body.current-body').removeClass('current-body')
            .addClass('hide');

        $(target).addClass('current-body').removeClass('hide');
    });

    if(limit > 0){
        $('#cost').click();
        $("input[name='limitAmount']").val(limit);
    }
    $('.info-region li').click(function(){
        var selected = $('.info-region .selected');

        if(selected[0] == this) {
            return;
        }

        $(this).addClass('selected')
            .find('.table-box').removeClass('hide');
        // 隐藏
        selected.removeClass('selected')
            .find('.table-box').addClass('hide');

        // 清除数据
        selected.find('.service-datatr').remove();
    });

    $('.info-regular .tag-control').click(function(){
        if($(this).hasClass('selected')){
            $(this).removeClass('selected');
        }else{
            $(this).addClass('selected');
        }
    });

    $('.cost').click(function(){
        $(this).addClass('selected_btn');
        $(this).siblings().removeClass('selected_btn');
        if($(this).hasClass('no-cost')){
            $('.money_input').prop('disabled',true).val(null);

        }else{
            $('.money_input').prop('disabled',false).val(null);
        }
    });
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
    //选择生效时间
    seajs.use([
        'date',
        'check'
        ],function(dp,ck){
        dp.datePicker('.Date');
        ck.init();
    });
     //选择服务
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
        $(document).on('click','.delete-service',function(){
            var $this=$(this);
            dg.confirm('确定要删除吗？',function(){
                $this.parents('tr').remove();
            })
        })

    $(".submit").on("click",function(){
        var $container=$(this).parents('.info-body');
        var data = {
            remark: $container.find('textarea[name="remark"]').val()
        };
        var index = $('.current-item').data('type');
        var id = $("#id").val();
        var url = id ? BASE_PATH + '/account/coupon/update/cash' : BASE_PATH + '/account/coupon/create/cash';

        if(!ck.check()) {
            return;
        }

        // 通用券
        if(index == 2) {
            url = BASE_PATH + '/account/coupon/create/universal';

            if(id) {
                url =  BASE_PATH + '/account/coupon/update/universal'
            }
        }

        data.discountAmount = $container.find("[name='discountAmount']").val();
        data.couponName = $container.find("[name='couponName']").val();

        // 现金券
        if(index == 1) {
            // 使用范围
            data.useRange = $container.find('.useRange li.selected').data('id');
            // 消费金额限制
            data.limitAmount = $container.find('.money_input').val() || 0;

            // 是否自定义有效期
            data.periodCustomized = $container.find('.self-btn.selected_btn').length ? true : false;
        }

        // 判断服务项目
        if(data.useRange == 2 && index == 1) {
            data.serviceIds = [];
            // 指定服务项目
            $container.find("input[name='serviceId']").each(function () {
                data.serviceIds.push( $(this).val() );
            });

            if(data.serviceIds.length == 0) {
                dg.warn('请添加至少一个服务');
                return;
            }
        }

        data.compatibleWithCard = $container.find('#compatibleWithCard')
            .prop('checked');

        data.singleUse = $container.find('#singleUse')
            .prop('checked');

        data.effectiveDate = $container.find("input[name='effectiveDate']").val();
        data.expiredDate = $container.find("input[name='expiredDate']").val();
        data.effectivePeriodDays = $container.find("input[name='effectivePeriodDays']").val();

        if(!isNaN(id) && id != 0){
            data.id = id;
        }
        $.ajax({
            url:url,
            dataType: 'json',
            contentType : 'application/json',
            data: JSON.stringify(data),
            type:"POST",
            success:function(result){
                seajs.use('dialog',function(dg){
                if(result.success){
                    dg.success('提交成功');
                    window.location = BASE_PATH + "/account/setting?flag=0";
                }else{
                    dg.fail(result.message);
                }
                });

            }
        });
    });

    });


    $(document).on('click', '.js-del', function () {
        $(this).parents('tr').remove();
    });
    //返回
    $(document).on('click','.js-goback',function(){
       util.goBack();
    });

    function serviceCallback(json) {
        seajs.use('art', function (at) {

            var html = at("serviceTpl", {json: json});
            $('.service .yqx-table').append(html);
        });
    }

});
