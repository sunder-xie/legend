var module = {};
$(function(){
    var myDialog,
        shopActivityStatus = $('#shopActivityStatus').val(),
        oriShopActivityStartTimeStr = $('#startTime').val(),
        oriShopActivityEndTimeStr = $('#endTime').val(),
        moduleList = $('.module-vo').map(function(){
            return $(this)[0].id;
        }).get();

    seajs.use(['dialog', 'art' ,'date', 'check'], function (dialog, art, date, check) {

       // 配置的日期
        date.dpStartEnd({
            start: 'startTime',
            end: 'endTime',
            startSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                maxDate: '#F{$dp.$D(\'endTime\')}',
                minDate: $('#tplStartTime').val(),
                autoUpdateOnChanged:true
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                minDate: '#F{$dp.$D(\'startTime\')}',
                maxDate: $('#tplEndTime').val(),
                autoUpdateOnChanged:true
            }
        });
        (function(){
            var tplTime = $('#tplEndTime').val();
            if( new Date(tplTime).getTime() < new Date().getTime()){
                dialog.warn("活动已过期，不能重新编辑");
                return;
            }
            $('.content-footer').find('div').show();
        })();
        check.init();
        var lessThan = function (val, v) {
            var target = $(this).closest('.js-choose-form').find('[name='+ v +']'),
                targetName = target.parent().prev().text(),
                targetValue = $.trim(target.val());
            var msg = [
                "",
                "不能大于" + targetName,
            ];
            if(targetValue !== ''){
                if (+val > targetValue) {
                    return {msg: msg[1], result: false};
                }
                else {
                    return {msg: msg[0], result: true};
                }
            }else{
                return {msg: msg[0], result: true};
            }
        };
        var moreThan = function (val, v) {
            var target = $(this).closest('.js-choose-form').find('[name='+ v +']'),
                targetName = target.parent().prev().text(),
                targetValue = $.trim(target.val());
            var msg = [
                "",
                "不能小于" + targetName,
            ];
            if(targetValue !== ''){
                if (+val < targetValue) {
                    return {msg: msg[1], result: false};
                }
                else {
                    return {msg: msg[0], result: true};
                }
            }else{
                return {msg: msg[0], result: true};
            }
        };
        check.helper('lessThan', lessThan);
        check.helper('moreThan', moreThan);

        oriShopActivityStartTimeStr = $('#startTime').val();
        oriShopActivityEndTimeStr = $('#endTime').val();

        //修改日期时需要可以重新发布
        $('#startTime,#endTime').blur(function(){
        });

        //支付设置
        $('.js-set-payment').on('click', function(){
            if($('#isAdmin').val() == 1){
                location.href = BASE_PATH + '/shop/conf/payment-mode';
            }else{
                dialog.warn('需要管理员权限才能设置');
            }
        });
        //发布
        $('.js-publish').on('click', function(){
            if(isEmpty()){
                if(moduleList[0] == 'moduleCouponDiscount'){
                    dialog.warn('至少选择一张卡券');
                } else {
                    dialog.warn('至少选择一项服务');
                }
                return;
            }
            if(!checkValid()) return;
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
                    actStatus: 2,
                    autoConfigMenu: 0
                }
                saveShopActivity(data, function(){
                    preview(true);
                });
            }


        });

        //预览
        $('.js-preview').on('click', function(){
            if(isEmpty()){
                if(moduleList[0] == 'moduleCouponDiscount'){
                    dialog.warn('至少选择一张卡券');
                } else {
                    dialog.warn('至少选择一项服务');
                }
                return;
            }
            if(!checkValid()) return;
            var actId = $('#actId').val();
            var shopActivityStatus = $('#shopActivityStatus').val();
            if (!actId
                || (actId && shopActivityStatus == 1)|| (actId && shopActivityStatus == -1)) {//1草稿-1未参加两种类型的活动的预览需要重新保存活动
                var data = {
                    actStatus: 1
                }

                saveShopActivity(data ,function(){
                    preview(false);
                });
            }else{
                preview(false);
            }
        });
        //结束活动
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

        });

        $(document)
            //自动配置菜单
            .on('click', '.auto-set-confirm, .auto-set-cancel', function(){
                dialog.close(myDialog);
                var data = {
                    actStatus: 2
                }
                if($(this).hasClass('auto-set-confirm')){
                    data.autoConfigMenu = 1;
                }else{
                    data.autoConfigMenu = 0;
                }
                saveShopActivity(data, function(){
                    preview(true);
                });
            })
            //结束活动
            .on('click', '.end-activity-confirm', function(){
                if(isEmpty()){
                    if(moduleList[0] == 'moduleCouponDiscount'){
                        dialog.warn('至少选择一张卡券');
                    } else {
                        dialog.warn('至少选择一项服务');
                    }
                    return;
                }
                if(!checkValid()) return;
                dialog.close(myDialog);
                var data = {
                    actStatus: 0,
                    autoConfigMenu : 1
                }
                saveShopActivity(data, function(){
                    location.href = BASE_PATH + '/shop/wechat/activity-list';
                });
            })
            //手动配置菜单
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
            })


        function preview(isReload){
            var actId = $('#actId').val();
            $.ajax({
                url: BASE_PATH + '/shop/wechat/op/get-activity-url?actId=' + actId,
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
                                    location.href = BASE_PATH + '/shop/wechat/activity-list';
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
                },
                error: function () {
                    dialog.fail('请求失败');
                }
            })
        }
        function saveShopActivity(otherData, callback){
            var data = $.extend(getData(),otherData);

            $.ajax({
                url: BASE_PATH + '/shop/wechat/op/save-shop-wechat-activity',
                dataType: 'json',
                contentType: 'application/json',
                type:'POST',
                data: JSON.stringify(data),
                success: function (json) {
                    if(json &&json.data && json.data.id){
                        $('#actId').val(json.data.id);
                    }
                    if ((json && json.success)){
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
                                    dialog.success('发布成功',function(){
                                        preview(true);
                                    })
                                }
                            });
                        } else{
                            dialog.success('保存成功',function(){
                                callback && callback();
                            });
                        }
                    } else{
                        dialog.fail(json.errorMsg ||'保存失败');
                    }
                },
                error: function (json) {
                    dialog.fail(json.errorMsg || '保存失败');
                }
            })
        }

    });
    function getData(){
        var moduleVoList = [];
        for(var i in moduleList){
            moduleVoList.push(module[moduleList[i]].getModuleVo());
        }

        var pageVo = {
            pageIndex: $('.pageVoInfo').find($('[name="pageIndex"]')).val(),
            uniqueCode: $('.pageVoInfo').find($('[name="uniqueCode"]')).val(),
            moduleVoList: moduleVoList
        };
        var pageVoList = [pageVo];
        var data = {
            actId: $('#actId').val(),
            actTplId: $('#id').val(),
            startTime: new Date($('#startTime').val()),
            endTime: new Date($('#endTime').val()),
            pageVoList: pageVoList
        }
        return data;
    }
    
    function checkValid(){
        for(var i in moduleList){
            if(!module[moduleList[i]].checkValid) continue;
            if(!(module[moduleList[i]].checkValid())){
                return false;
            }
        }
        return true;
    }
    
    function isEmpty() {
        for(var i in moduleList){
            if(!module[moduleList[i]].isEmpty) continue;
            if(!module[moduleList[i]].isEmpty()){
                return false;
            }
        }
        return true;
    }

});


