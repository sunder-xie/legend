$(function(){
    var myDialog,
    shopActivityStatus = $('#shopActivityStatus').val();

    seajs.use(['dialog', 'select', 'art' ,'date', 'check', 'formData'], function (dialog, select, art, date, check, formData) {

       // 配置的日期
        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                maxDate: '#F{$dp.$D(\'endTime\')}',
                minDate: $('#tplStartTime').val(),
                autoUpdateOnChanged:true,
                oncleared: function(){
                    buttonCss()
                }
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                minDate: '#F{$dp.$D(\'startTime\')}',
                maxDate: $('#tplEndTime').val(),
                autoUpdateOnChanged: true,
                oncleared: function(){
                    buttonCss()
                }
            }
        });

        //异步获取下拉列表数据
        select.init({
            dom: '.js-choose-coupon',
            url: BASE_PATH + '/account/coupon/search?couponType=1',
            showKey: "id",
            showValue: "couponName",
            noDataCallback: function () {
                dialog.msg("您还没有卡券，点击新增优惠券去设置吧~", 1);
            }
        });
        (function(){
            var tplTime = $('#tplEndTime').val();
            if( new Date(tplTime).getTime() < new Date().getTime()){
                dialog.msg("活动已过期，不能重新编辑");
                return;
            }
            $('.content-footer').show();
        })();
        var couponMinValue = function (val, v) {
            var msg = [
                "",
                "不能小于" + v,
                "请输入数字"
            ];
            if (!$.isNumeric(val)) {
                return {msg: msg[2], result: false};
            }
            if (Number(val) >= v) {
                return {msg: msg[0], result: true};
            } else {
                return {msg: msg[1], result: false};
            }
        }

        check.helper('couponMinValue', couponMinValue);
        check.init();

        $(document)
            .on('click', '.auto-set-confirm, .auto-set-cancel', function(){
                dialog.close(myDialog);
                var data = {
                    gameStatus: 3
                }
                if($(this).hasClass('auto-set-confirm')){
                    data.isConfigMenu = 1;
                }else{
                    data.isConfigMenu = 0;
                }
                save(data, function(){
                    preview(true);
                });
             })
            .on('click', '.end-activity-confirm', function(){
                dialog.close(myDialog);
                var data = {
                    gameStatus: 1,
                    isConfigMenu : 1
                }
                save(data, function(){
                    location.href = BASE_PATH + '/shop/wechat/activity-list?actType=2';
                });
            })

            .on('click', '.manual-set-confirm', function(){
                dialog.close(myDialog);
                location.href = BASE_PATH + '/shop/wechat/wechat-menu';
             })
            .on('click', '.manual-set-cancel', function(){
                dialog.close(myDialog);
                dialog.success('发布成功',function(){
                    preview(true);
                })
            })
            .on('click', '.js-confirm-qrcode, .end-activity-cancel', function(){
                dialog.close(myDialog);
            });

        $('.js-input').on('keyup' ,function(){
            buttonCss();
        });
        $('.js-select').on('blur', function(){
            buttonCss();
        })
        $('.js-radio').on('change', function(){
            buttonCss();
        })

        $('.js-publish').on('click', function(){
            if(!check.check('.choose-box', false) || $(this).hasClass('disable')){
                return;
            }
            var shopWechatStatus = $('#shopWechatStatus').val();
            if(shopWechatStatus == 3){
                var data = {
                    title: '提示',
                    content: '活动可以在微信菜单中显示，是否需要自动配置？',
                    confirmClass: 'auto-set-confirm',
                    cancelClass: 'auto-set-cancel'
                }
                var html = art('bounceTpl', {data: data});
                myDialog = dialog.open({
                    area: ['400px', 'auto'],
                    content: html
                });
            }else{
                var data = {
                    gameStatus: 3,
                    isConfigMenu: 0
                }
                save(data, function(){
                    preview(true);
                });
            }

        })

        $('.js-preview').on('click', function(){
            if($(this).hasClass('disable')){
                return;
            }
            var shopActivityStatus = $('#shopActivityStatus').val();
            if(shopActivityStatus!=3) {
                var data = {
                    gameStatus: 2,
                    isConfigMenu: 0
                }
              save(data ,function(){
                  preview(false);
              });
            }else{
                preview(false);
            }

        })
        $('.js-end').on('click', function(){
            var data = {
                title: '提示',
                content: '确定结束本期活动？',
                confirmClass: 'end-activity-confirm',
                cancelClass: 'end-activity-cancel'
            }
            var html = art('bounceTpl', {data: data});
            myDialog = dialog.open({
                area: ['400px', 'auto'],
                content: html
            });

        })
        $('#view-checking').on('click', function(){
            var html = art('checkTpl', {data: {}});
            myDialog = dialog.open({
                area: ['600px', 'auto'],
                content: html
            });
        })

        function preview(isReload){
            var gameId = $('#gameId').val();
            $.ajax({
                url: BASE_PATH + '/shop/wechat/op/get-activity-url?wechatActivityType=2&actId=' + gameId,
                dataType: 'json',
                contentType: 'application/json',
                type:'POST',
                success: function (json) {
                    if (json && json.success) {
                        var url = json.data;
                        var html = art('qrcodeTpl', {url: url});
                        myDialog = dialog.open({
                            area: ['340px', '410px'],
                            content: html,
                            end: function(){
                                if(isReload){
                                    location.href = BASE_PATH + '/shop/wechat/activity-list?actType=2';
                                }
                            }
                        });
                        //Render in canvas
                        $("#qrcodeView div").qrcode({
                            width: 270,
                            height: 270,
                            text: url
                        });
                    } else {
                        dialog.fail(json.errorMsg || '请求失败');
                    }
                }
            })
        }
        function save(otherData, callback){
            var getData = formData.get('.choose-box', true);
            getData.gameCouponDTOs = $('.prize-item').map(function(){
                var $this = $(this);
                var data = {
                    couponInfoId: $this.find($('.couponInfoId')).val(),
                    couponCount: $this.find($('.js-input')).val(),
                    scoreToplimit: $this.find($('.scoreToplimit')).val(),
                    scoreLowlimit: $this.find($('.scoreLowlimit')).val()
                }
                return data;
            }).get();
            getData.startTime=new Date($('#startTime').val());
            getData.endTime = new Date($('#endTime').val());
            var data = $.extend(getData,otherData);
            $.ajax({
                url: BASE_PATH + '/shop/wechat/op/save-wechat-game-activity',
                dataType: 'json',
                contentType: 'application/json',
                type:'POST',
                data: JSON.stringify(data),
                success: function (json) {
                    if ((json && json.success)){
                        if(json.data && json.data.id){
                            $('#gameId').val(json.data.id);
                        }
                        if(json.data && json.data.autoMenuSuccess==0){
                            //自动配置菜单失败,需要提示用户手动去配置
                            var data = {
                                title: '提示',
                                content: '您的[门店活动]微信菜单已满5个,需要手动去配置',
                                confirmClass: 'manual-set-confirm',
                                cancelClass: 'manual-set-cancel'
                            }
                            var html = art('bounceTpl', {data: data});
                            myDialog = dialog.open({
                                area: ['400px', 'auto'],
                                content: html,
                                shadeClose: false,
                                cancel: function(){
                                    dialog.success('操作成功',function(){
                                        preview(true);
                                    })
                                }
                            });
                        } else{
                            dialog.success('操作成功',function(){
                                callback && callback();
                            });
                        }
                    } else{
                        dialog.fail(json.errorMsg ||'发布失败');
                    }
                }
            })
        }


    });

    //控制按钮的样式
    function buttonCss(){
        //活动是未发布状态
        if(shopActivityStatus == 0 || shopActivityStatus == 1 || shopActivityStatus == 2){
            isEmpty()? $('.js-publish, .js-preview').addClass('disable') : $('.js-publish, .js-preview').removeClass('disable');
        }
        //活动已经是发布状态
        else{
            if(!isChange()){
                //设置没有发生变化:可以预览，不能发布
                $('.js-preview').show();
                $('.js-publish').addClass('disable');
            }else{
                //设置有变化:不能预览，必填项不为空时可以发布
                $('.js-preview').hide();
                isEmpty()? $('.js-publish').addClass('disable') : $('.js-publish').removeClass('disable');
            }
        }
    }

    function isEmpty(){
        var result = false;
        $.each($('.require'), function(){
            if(this.value === ""){
                result = true;
                return;
            }
        })
        return result;
    }

    function isChange(){
        var result = false;
        var formObj =  $('.yqx-input').add('.js-radio:checked');
        $.each(formObj, function(){
            if($.trim(this.value) !== $.trim($(this).data('origin'))){
                result = true;
                return;
            }
        })
        return result;
    }
});


