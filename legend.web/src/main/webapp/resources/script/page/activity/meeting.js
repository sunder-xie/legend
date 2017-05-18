function dataType( obj ) {
    return Object.prototype.toString.call(obj).slice(8, -1).toLowerCase();
}

var model = {
    joinActivity: function (shopReason, success) {
        seajs.use(['ajax','dialog'], function (ajax,dg) {
            var $serviceTplDiv = $("#serviceTplDiv"),
                actTplId = $serviceTplDiv.attr('act_tpl_id'),
                serviceTplId = $serviceTplDiv.attr('service_tpl_id'),
                servicePrice = $.trim($("#servicePrice").val());
                data = {
                    actTplId: actTplId,
                    serviceTplId: serviceTplId,
                    servicePrice:servicePrice
                };

            if( dataType(shopReason) == 'function' ) {
                success = shopReason;
                shopReason = undefined;
            }

            if( shopReason != undefined ) {
                data['shopReason'] = shopReason;
            }

            ajax.post({
                url: BASE_PATH + "/shop/activity/join_activity",
                data: data,
                success: function (result) {
                    success && success(result);
                }
            })
        });
    },
    exitActivity: function (success) {
        var dialogIndex = null;
        seajs.use(['ajax', "dialog", "artTemplate"], function (ajax, dg, art) {
            var html = art('exit_activity_dialog', null);

            dialogIndex = dg.dialog({
                html: html
            });
            success && success();
        })
    },
    exitAct: function(success) {
        seajs.use(['ajax', 'dialog'], function(ajax, dg) {
            var $serviceTplDiv = $("#serviceTplDiv"),
                actTplId = $serviceTplDiv.attr('act_tpl_id'),
                serviceTplId = $serviceTplDiv.attr('service_tpl_id'),
                shopReason = $('.reason').val();

            if(shopReason == "" || shopReason == undefined) {
                dg.info("请填写取消原因！");
                return;
            }

            if(shopReason.length > 500) {
                dg.info("原因不能超过500字！");
            }

            ajax.post({
                url: BASE_PATH + "/shop/activity/exit_activity",
                data: {
                    actTplId: actTplId,
                    serviceTplId: serviceTplId,
                    shopReason: shopReason
                },
                success: function (result) {
                    success && success(result);
                }
            });
        })
    }
};

jQuery(function ($) {
    var fn = {
        // 协议弹窗方法
        protocol: function () {
            seajs.use(['dialog'], function (dg) {
                var html = $('#protocolTpl').html(),    // 读取协议信息
                    dgId = dg.dialog({      // 调用协议弹窗
                        html: html
                    }),
                    checked = $('#J-agree').prop("checked");

                $('#agreeProtocol').prop("checked", checked);

                $(document)
                    .off('click', '.P-closeBtn')
                    .on('click', '.P-closeBtn', function () {        // 确定关闭按钮
                        dg.close(dgId);
                    })
                    .off('change', '#agreeProtocol')
                    .on('change', '#agreeProtocol', function () {    // 弹窗协议同意按钮
                        var checked = $('#agreeProtocol').prop("checked");
                        $('#J-agree').prop("checked", checked);
                    })
                    .off('change', '#J-agree')
                    .on('change', '#J-agree', function () {      // 弹窗协议同意按钮
                        var checked = $('#J-agree').prop("checked");
                        $('#agreeProtocol').prop("checked", checked);
                    })
            });
        }
    };

    $(document)
        // 查看协议弹窗
        .on('click', '#protocolBtn', function () {
            fn.protocol();
        })
        // 报名参加
        .on('click', '#J-join', function () {
            var $this = $(this);
            seajs.use(['dialog'], function (dg) {
                var checked = $('#J-agree').prop("checked");
                if (!checked) {
                    dg.info("您还未阅读同意协议！");
                    return;
                }
                var servicePrice = $.trim($("#servicePrice").val());
                if(servicePrice){
                    dg.confirm('是否确认"夏日活动"服务价格为<br/><span class="red">' + servicePrice + '</span>元',function(){
                        model.joinActivity(function (result) {

                                $this.prop("disabled", true);
                                dg.info('<div style="text-align: center;">恭喜您成功报名<br/>将在<strong style="color:#ff7800;">3</strong>个工作日进行审核</div>', 1, 2, function () {
                                    window.location.reload();
                                });
                        });
                    },function(){
                        return false;
                    });
                }else{
                    model.joinActivity(function (result) {
                        if(result.success) {
                            $this.prop("disabled", true);
                            dg.info('<div style="text-align: center;">恭喜您成功报名<br/>将在<strong style="color:#ff7800;">3</strong>个工作日进行审核</div>', 1, 2, function () {
                                window.location.reload();
                            });
                        }else{
                            dg.info('<div style="text-align: center;">报名失败<br/></div>', 5, 2, function () {
                                // window.location.reload();
                            });
                        }
                    });
                }

            });
        })
        // 调用取消参加弹窗
        .on('click', '#J-exit', function () {
            var $this = $(this);
            seajs.use(['dialog'], function (dg) {
                dg.confirm('确定要退出活动吗？', function () {
                    model.exitActivity();
                });
            });
        })
        // 取消参加
        .on('click', '.P-exit', function() {
            seajs.use(['dialog'], function(dg) {
                model.exitAct(function(result) {
                    if(result.success) {
                        window.location.reload();
                    } else {
                        dg.info(result.errorMsg, 3, 3);
                    }
                });
            })
        })
        // 查看申请失败原因
        .on('click', '#J-view-fail', function () {
            var reason = $(this).attr('reason');
            seajs.use(['dialog'], function (dg) {
                dg.info(reason + "<br><span style='color:red'>如有问题，请拨打客服电话400-9937-288</span>", 3, 5);
            });
        })
        // 调用重新参加弹窗
        .on('click', '#J-rejoin', function() {
            seajs.use(['dialog'], function(dg) {
                var checked = $('#J-agree').prop('checked');

                if( !checked ) {
                    dg.info("您还未阅读协议！");
                    return;
                }
                var servicePrice = $.trim($("#servicePrice").val());
                if(servicePrice){
                    dg.confirm('是否确认"夏日活动"服务价格为<br/><span class="red">' + servicePrice + '</span>元',function(){
                        dg.dialog({
                            html: $('#rejoinTpl').html()
                        })
                    },function(){
                        return false;
                    });
                }else{
                    dg.dialog({
                        html: $('#rejoinTpl').html()
                    })
                }

            })
        })
        // 重新参加
        .on('click', '#P-rejoin', function() {
            seajs.use(['dialog'], function(dg) {
                var shopReason = $('.reason').val();

                if( shopReason == "" || shopReason == undefined ) {
                    dg.info("请填写再次参加的原因！");
                    return;
                }

                if(shopReason.length > 500) {
                    dg.info("原因不能超过500字！");
                }

                model.joinActivity(shopReason, function(result) {
                    window.location.reload();
                })
            })
        })
});