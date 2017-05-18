$(function(){

    // 全局加载...
    var loading;
    $.ajaxSetup({
        beforeSend: function() {
            loading = taoqi.loading();
        },
        complete: function() {
            taoqi.close(loading);
        }
    });

    $.post('list/ng',function(result){
        var temp=doT.template($('#temp').html());
        $('.noticeList').html(temp(result));
        $('.counter').counter();
    });
    //$(document).on('mouseenter','.notice .title,.notice .noticeBody',function(){
    //    $p=$(this).parent();
    //    $(this).parent().find('.activityMask,.activityHoverShow').removeClass('heightEnd').addClass('heightStart');
    //});
    //$(document).on('mouseleave','.notice',function(){
    //    $(this).parent().find('.activityMask,.activityHoverShow').addClass('heightEnd');
    //});
    $(document).on('click','.voidNotice',function(){
        window.open('add_act','_target');
    });
    $(document).on('click','.edit',function(){
        var $p=$(this).parents('.notice');
        $p.find('.operateArea a').hide().end().find('.operateArea').append('<a class="save">保存</a>').end()
            .find('.noticeBody>div').toggle();
        // 计数
        $('.counter').counter();
    });
    $(document).on('click','.save',function(){
        var $p=$(this).parents('.notice').find('.noticeBody .startEdit');
        //验证数据
        if(!validaData( this )) {
            return false;
        }
        var data={};
        var queryList=['id','couponType','couponName','couponDesc','startTimeStr','endTimeStr'];
        for(var i=0;i<queryList.length;i++) {
            if ($p.find('.' + queryList[i]).val() || $p.parent().data(queryList[i])) {
                data[queryList[i]] = $p.find('.' + queryList[i]).val() || $p.parent().data(queryList[i]);
            }
        }
        $.ajaxSetup({
            contentType:'application/json'
        });
        $.post('add/ng',JSON.stringify(data),function(result){
            if (result.success != true) {
                layer.msg(result.errorMsg,3,3);
                return false;
            } else {
                layer.msg('保存成功',2,1,function(){
                    history.go(0);
                });
            }
        });
    });
    $(document).on('click','.open,.close,.cancelOpen,.share',function(){
        var url;
        var id=$(this).parents('.notice').find('.noticeBody').data('id');
        if($(this).is('.close')||$(this).is('.cancelOpen')){
            url="off_coupon/ng";
        }else if($(this).is('.open')){
            url="release_coupon/ng";
        }else{
            //todo
            return ;
        }

        layer.confirm("您确定要"+$(this).html()+"该优惠公告吗?",function(){
            $.ajax({
                type : "GET",
                url : url,
                data : {
                    id : id
                },
                success: function (result){
                    if (result.success != true) {
                        layer.msg(result.errorMsg,3,3);
                        return false;
                    } else {
                        layer.msg(result.data,2,1,function(){
                            history.go(0);
                        });
                    }
                }
            });
        },function (){
            return false;
        });
    });

    $(document)
        .on('mouseenter', '.notice', function() {

            var $mask = $(this).find(".maskBox");
            $mask.stop();
            $mask.animate({
                height: "205px"
            },{
                duration: "slow"
            });
        }).on('mouseleave', '.notice', function() {

            var $mask = $(this).find(".maskBox");
            $mask.animate({
                height: "0"
            },{
                duration: "slow"
            });
        });

    $(document).on('click', '.show', function() {
        taoqi.activityNotice();
    });

    function validaData( _t ) {
        var $p = $(_t).parents(".notice");
        if(!$.trim($p.find('.couponName').val())){
            layer.msg("请填写活动名称!",2,3);
            return false;
        } else if($.trim($p.find('.couponName').val()).length > 20){
            layer.msg("活动名称不能超过20个字!",2,3);
            return false;
        } else if(!$.trim($p.find('.couponDesc').val())){
            layer.msg("请填写活动简介!",2,3);
            return false;
        } else if($.trim($p.find('.couponDesc').val()).length > 40){
            layer.msg("优惠说明不能超过40个字!",2,3);
            return false;
        } else if(!$p.find('.startTimeStr').val()){
            layer.msg("请选择活动开始时间!",2,3);
            return false;
        }
        return true;
    }

});