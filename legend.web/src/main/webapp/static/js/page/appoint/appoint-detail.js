/**
 * zmx  2016-04-12
 * 预约单查询
 */
$(function() {
    var $document = $(document);

    seajs.use([
        'art',
        'dialog',
        'ajax'
    ], function(at,dg, ax) {

        dg.titleInit();
        //编辑
        $document.on('click','.js-edit',function(){
            var appointId = $(".js-appointId").data('appointId');
            window.location.href = BASE_PATH + "/shop/appoint/appoint-edit?appointId="+ appointId;
        });

        //复制
        $document.on('click','.js-copy',function(){
            var appointId = $(".js-appointId").data('appointId');
            window.location.href = BASE_PATH + "/shop/appoint/appoint-edit?reAppointId="+ appointId;
        });

        //预约单置为无效(作废)
        $(document).on("click",".js-invalid",function() {
            var appointId = $(".js-appointId").data('appointId');
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/shop/appoint/invalid-appoint",
                data: {
                    appointId: appointId
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/appoint/appoint-detail?appointId="+ appointId;
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        //删除
        $(document).on("click",".js-delete",function() {
            var appointId = $(".js-appointId").data('appointId');
            dg.confirm("您确定要删除该预约单吗?", function () {
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/shop/appoint/delete-appoint",
                    data: {
                        appointId: appointId
                    },
                    success: function (result) {
                        if (result.success) {
                            dg.success("操作成功", function () {
                                window.location.href = BASE_PATH + "/shop/appoint/appoint-list#1";
                            });
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            });
        });

        //确认预约
        $(document).on("click",".js-confirm",function() {
            var appointId = $(".js-appointId").data('appointId');
            var customerCarId = $(".js-customerCarId").data('customerCarId');
            if(customerCarId==null||customerCarId==''||customerCarId==0){//缺少客户(车辆)信息
                dg.confirm("此预约单缺少客户(车辆)信息,需要编辑完善后才能确认预约.", function () {
                    window.location.href = BASE_PATH + "/shop/appoint/appoint-edit?appointId="+ appointId;
                }, function () {
                    return false;
                });
            } else{
                dg.confirm("您确定要确认该预约单吗?", function () {
                    $.ajax({
                        type: 'post',
                        url: BASE_PATH + "/shop/appoint/confirm-appoint",
                        data: {
                            appointId: appointId
                        },
                        success: function (result) {
                            if (result.success) {
                                dg.success("操作成功", function () {
                                    window.location.href = BASE_PATH + "/shop/appoint/appoint-detail?appointId="+ appointId;
                                });
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    });
                }, function () {
                    return false;
                });
            }
        });

        //取消 弹窗
        var chanelDialog = null;
        $document.on('click','.js-chanel-dialog',function(){
            var appointId = $(".js-appointId").data('appointId'),
                html = at('chanel-dialog', {appointId: appointId});
            chanelDialog = dg.open({
                area: ['440px','auto'],
                content: html
            });
            return false;
        });

        //点击取消关闭弹窗
        $document.on('click', '.js-chanel-close', function () {
            dg.close(chanelDialog);
        });


        //提交 取消预约
        $(document).on('click','.js-cancelAppoint',function() {
            var appointId = $(".js-appointId").data('appointId');
            var cancelReason = $(".t_middle div.hover").text();
            if (cancelReason == "") {
                dg.warn("请选择取消预约的原因！");
            }
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/shop/appoint/cancel-appoint",
                data: {
                    appointId: appointId,
                    cancelReason: cancelReason
                },
                success: function (result) {
                    if (result.success) {
                        dg.close(chanelDialog);
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/appoint/appoint-detail?appointId="+ appointId;
                        });
                    } else {
                        dg.fail(result.errorMsg);

                    }
                }
            });
        });

        //开洗车单
        $document.on('click','.js-carwash',function() {
            var customerCarId = $(".js-customerCarId").data('customerCarId');
            var appointId = $(".js-appointId").data('appointId');
            window.location.href = BASE_PATH+"/shop/order/carwash?refer=appoint-detail&appointId="+appointId+"&customerCarId="+customerCarId;
        });
        //开快修快保单
        $document.on('click','.js-fast',function() {
            var appointId = $(".js-appointId").data('appointId');
            window.location.href = BASE_PATH+"/shop/order/speedily?refer=appoint-detail&category=3&appointId="+appointId;
        });
        //开综合维修单
        $document.on('click','.js-zh',function() {
            var appointId = $(".js-appointId").data('appointId');
            window.location.href = BASE_PATH+"/shop/order/common-add?refer=appoint-detail&category=1&appointId="+appointId;
        });

        //返回按钮
        $document.on('click','.js-comeback',function(){
            util.goBack();
        });

        //取消原因选中效果
        $document.on('click', '.js-hover', function () {
            var $this = $(this);
            $this.addClass('hover').siblings().removeClass('hover');
        });

    });
});