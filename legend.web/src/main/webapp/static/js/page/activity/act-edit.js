$(function () {
    if($('.list-container').find('input').length == 0) {
        $('.list-container').remove();
    } else {
        $('.list-container').removeClass('hide');
    }

    seajs.use(["ajax", "art", "dialog", "downlist"], function (ajax, art, dg, downlist) {
        var _target;
        dg.titleInit();
        downlist.init({
            url: BASE_PATH + '/shop/shop_service_info/serviceInfo',
            showKey: 'name,serviceNote,servicePrice',
            tplId: 'serviceTpl',
            dom: 'input[name=serviceName]',
            searchKey: 'serviceName',
            globalData: {type: 1},
            hasInput: false,
            callbackFn: serviceListCallFn
        });

        $('.js-back').on('click', util.goBack);

        if(!document.execCommand('copy')) {
            $(".js-copy").zclip({  //复制插件，复制url到剪切板
                path: BASE_PATH + "/resources/script/libs/zclip/ZeroClipboard.swf",
                copy: function () {
                    return $("#copy-input").val();
                }
            });
        } else {
            document.addEventListener('copy', function (e) {
                if (_target.className == 'copy-input') {
                    e.clipboardData.setData('text/plain', $('#copy-input').val());
                    _target = {};
                    e.preventDefault();
                }
            });
        }

        $(document).on("keyup", "#service_note", function () {
            //限制活动描述字数
            var $this = $(this),
                valNum = $this.val().length;
            if (valNum > 255) {
                $this.val($this.val().substr(0, 255));
            }

        })
            .on('click', '.js-copy', function () {
                _target = $('#copy-input')[0];
                document.execCommand('copy');

                dg.success('复制成功');
            })
            .on('click', '.js-add-service', function () {
                $(this)
                    .parent().parent()
                    .clone()
                    .find('input').val('')
                    .end().appendTo( $('.aside-main .list-container') );
                $(this).removeClass('add-icon js-add-service')
                    .addClass('del-icon js-del-service');
            })
            .on('click', '.js-del-service', function () {
                $(this).parents('.show-grid').remove();
            })
            .on("click", ".btn-cancel", function () {

                location.href = BASE_PATH + "/shop/activity";
            })
            .on("click", ".js-publish", function () {
                //生成二维码，将数据传给后台，获得二维码地址与图片并弹窗展示
                var $this = $(this);

                //START 创建数据对象
                var maxCol = 5,
                    templateId = $("input[name=templateId]").val(),
                    saveData = {
                        id: $("input[name=id]").val(),
                        status: $("input[name=status]").val(),
                        shopId: $("input[name=shopId]").val(),
                        templateId: templateId,
                        title: $("input[name=title]").val(),
                        templateUrl: $("input[name=templateUrl]").val(),
                        serviceNum: maxCol
                    },
                    url = BASE_PATH + "/shop/activity/save",
                    arr = [], num = 0;
                if (!templateId) {
                    location.href = BASE_PATH + "/shop/activity";
                    return false;
                }
                $(".aside-main .list-container").find(".show-grid").each(function () {
                    var serviceId = $("input[name=serviceId]", this).val();
                    if (serviceId) {
                        var $this = $(this),
                            obj = {
                                serviceId: serviceId,
                                serviceName: $this.find("input[name=serviceName]").val(),
                                serviceNote: $this.find("input[name=serviceNote]").val(),
                                servicePrice: $this.find("input[name=servicePrice]").val()
                            };
                        arr.push(obj);
                        num++;
                    }
                });
                if (num > parseInt(maxCol)) {
                    dg.warn("最多只能加" + maxCol + "条,请核对信息！");
                    return false;
                }
                saveData.serviceInfos = arr;
                //END 创建数据对象

                $.ajax({
                    type:'post',
                    url: url,
                    data: JSON.stringify(saveData),
                    contentType: "application/json",
                    success: function (result) {
                        if (result.success) {
                            var data = {
                                src: result.data.imgUrl,
                                url: result.data.templateUrl
                            };
                            $("input[name=id]").val(result.data.id);
                            dg.open({
                                content: art("scancopyTpl", {data: data}),
                                area: ['840px', '230px']
                            });


                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }

                });
            });

        function serviceListCallFn(obj, item) {
            var scope = obj.parents(".show-grid");

            $("input[name='serviceId']", scope).val(item.id);
            $("input[name='serviceName']", scope).val(item.name);
            $("input[name='serviceNote']", scope).val(item.serviceNote);
            $("input[name='servicePrice']", scope).val(item.servicePrice);

        }
    });
});
