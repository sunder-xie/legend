/**
 * Created by ZhuangQianQian on 16/8/2.
 */
$(function () {
    var $doc = $(document);
    seajs.use([
        'table',
        'dialog',
        'art',
        'select',
        'date'
    ], function (tb, dg, at, st, dp) {
        at.helper("$dateFormat", function dateFormat(date, format) {
            date = new Date(date);
            var map = {
                M: date.getMonth() + 1,
                //月份
                d: date.getDate(),
                //日
                h: date.getHours(),
                //小时
                m: date.getMinutes(),
                //分
                s: date.getSeconds(),
                //秒
                q: Math.floor((date.getMonth() + 3) / 3),
                //季度
                S: date.getMilliseconds()
            };
            format = format.replace(/([yMdhmsqS])+/g, function (all, t) {
                var v = map[t];
                if (v !== undefined) {
                    if (all.length > 1) {
                        v = "0" + v;
                        v = v.substr(v.length - 2);
                    }
                    return v;
                } else if (t === "y") {
                    return (date.getFullYear() + "").substr(4 - all.length);
                }
                return all;
            });
            return format;
        });
        function tabSelect(obj, type) {
            var url, rewardUrl;
            var type = type ? $(obj).data("type") : 'tbd';
            $(".Z-tab-item").removeClass("on");
            $(obj = obj ? obj : ".first").addClass("on");
            if ($(obj).hasClass("first")) {
                $(".Z-search").data('status', 1);
                url = BASE_PATH + '/insurance/anxin/view/getTouBaoDanList';
                rewardUrl = BASE_PATH + '/insurance/anxin/api/getInsureStatus';
                $(".Z-carOwner, .Z-status-fictitious,.Z-time").addClass('hidden');
                $(".Z-status, .Z-insurerName, .Z-vin").removeClass('hidden');
            } else if ($(obj).hasClass("second")) {
                $(".Z-search").data('status', 2);
                url = BASE_PATH + '/insurance/anxin/view/getBaoDanList';
                rewardUrl = BASE_PATH + '/insurance/anxin/api/getQuitStatus';
                $(".Z-carOwner, .Z-status-fictitious,.Z-time").addClass('hidden');
                $(".Z-status, .Z-insurerName, .Z-vin").removeClass('hidden');
            } else {
                //虚拟投保单
                $(".Z-search").data('status', 3);
                url = BASE_PATH + '/insurance/anxin/view/virtual-list';
                rewardUrl = BASE_PATH + '/insurance/anxin/api/virtual-status';
                $(".Z-carOwner, .Z-status-fictitious,.Z-time, .Z-vin").removeClass('hidden');
                $(".Z-status, .Z-insurerName").addClass('hidden');
            }
            $(".yqx-select-options").remove();
            $(".insuredName").val('');
            $(".carOwner").val('');
            $(".vehicleSn").val('');
            $(".insureStatus").val('');
            $(".insureStatus-fictitious").val('');
            $("#startDate").val('');
            $("#endDate").val('');
            $(".Z-status span").attr("class", "fa icon-small icon-angle-down");
            $(".Z-status-fictitious span").attr("class", "fa icon-small icon-angle-down");
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableTest',
                //分页容器id，必需
                pageid: 'pagingTest',
                //表格模板id，必需
                tplid: 'tableTestTpl',
                //如果模板需要自定义参数,可选
                tpldata: {'type': type},
                //扩展参数,可选
                data: {},
                //关联查询表单id，可选
                formid: null,
                // 是否开启hash功能
                enabledHash: false,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
            if ($(".Z-search").data('status') == 3) {
                st.init({
                    dom: '.js-select',
                    url: rewardUrl,
                    showKey: "code",
                    showValue: "name",
                    pleaseSelect: true,
                    callback: function () {
                        $(".insureStatus-fictitious").data("insurancevirtualstatus", $(this).data("key"));
                    }
                });
            } else {
                st.init({
                    dom: '.js-select',
                    url: rewardUrl,
                    showKey: "mapKey",
                    showValue: "mapValue",
                    pleaseSelect: true,
                    callback: function () {
                        $(".insureStatus").data("insurestatus", $(this).data("key"));
                    }
                });
            }
            // 开始结束日期
            dp.dpStartEnd();

            // 时分秒日历
            dp.dpStartEnd({
                start: 'startDate',
                end: 'endDate',
                startSettings: {
                    dateFmt: 'yyyy-MM-dd'
                    //maxDate: '#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'
                },
                endSettings: {
                    dateFmt: 'yyyy-MM-dd',
                    minDate: '#F{$dp.$D(\'startDate\')}'
                    //maxDate: '%y-%M-%d'
                }
            });
        }

        tabSelect();
        $doc.on("click", ".Z-tab-item", function () {
            tabSelect($(this), $(this).data('type'));
        }).on("click", ".Z-search", function () {
            var data = {};
            if ($(".Z-search").data('status') == 3) {
                data = {
                    'vehicleOwnerName': $(".carOwner").val(),
                    'vehicleSn': $(".vehicleSn").val(),
                    //以下三个参数字段名还没有定，暂时自己定义
                    //虚拟投保单状态
                    'insuranceVirtualStatus': $(".insureStatus-fictitious").data("insurancevirtualstatus"),
                    //生效开始时间
                    'gmtStartUserNotified': $("#startDate").val(),
                    //生效结束时间
                    'gmtEndUserNotified': $("#endDate").val()
                };
            } else {
                data = {
                    'peopleName': $(".insuredName").val(),
                    'carNumber': $(".vehicleSn").val(),
                    'insuranceStatus': $(".insureStatus").data("insurestatus")
                };
            }

            var url;
            var type;
            if ($(this).data('status') == 1) {
                url = BASE_PATH + '/insurance/anxin/view/getTouBaoDanList';
                type = 'tbd';
            } else if ($(this).data('status') == 2) {
                url = BASE_PATH + '/insurance/anxin/view/getBaoDanList';
                type = 'bd';
            } else {
                //虚拟投保单
                url = BASE_PATH + '/insurance/anxin/view/virtual-list';
                type = 'tbd-fictitious';
            }
            location.hash = 1;
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableTest',
                //分页容器id，必需
                pageid: 'pagingTest',
                //表格模板id，必需
                tplid: 'tableTestTpl',
                //如果模板需要自定义参数,可选
                tpldata: {'type': type},
                //扩展参数,可选
                data: data,
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }).on("click", ".Z-check-detail", function () {
            var formId = $(this).data("id");
            var type = $(this).parents(".Z-right").find(".on").data("type");
            var basicId = $(this).data('basicid');
            var detailUrl = '';
            if ($(".Z-search").data('status') == 3) {
                //虚拟投保单
                detailUrl += BASE_PATH + '/insurance/anxin/view/virtual-detail?id=' + basicId;
            } else {
                detailUrl += BASE_PATH + '/insurance/anxin/view/insurance-detail?type=' + type + "&formId=" + formId;
            }
            window.location.href = detailUrl;
        }).on('click', '.Z-delete', function () {
            var formId = $(this).data("id");
            layer.confirm('确认删除该保单？',function(){
                $.ajax({
                    type:"post",
                    url:BASE_PATH + '/insurance/anxin/view/delForm?formId='+formId,
                    success:function(result){
                        if(result.success){
                            dg.success("删除成功");
                            setTimeout(function () {
                                window.location.reload();
                            },2000);

                        }else{
                            dg.fail(result.errorMsg);
                        }
                    }
                })
            });

        }).on('click', '.rendImg', function () {
            var $this = $(this),
                sn = $this.data('sn'),
                url = BASE_PATH + '/insurance/anxin/flow/insurance-result?orderSn=' + sn;
            payInfo(url, sn);
        }).on('click', '.goPay', function () {
            var $this = $(this),
                sn = $this.data('sn'),
                url = BASE_PATH + '/insurance/anxin/pay/choose?sn=' + sn;
            payInfo(url, sn);
        });

        //点击上传照片或者去缴费
        function payInfo(url, sn) {
            $.ajax({
                url: BASE_PATH + '/insurance/anxin/flow/pay-info',
                data: {
                    orderSn: sn
                },
                success: function (result) {
                    if (result.success) {
                        var time = result.data.beforeTime,
                            nowTime = new Date().getTime(),
                            sy = result.data.commercialInsuredFee,
                            jq = result.data.forcibleInsuredFee,
                            shouldPay = result.data.insuredTotalFee;
                        if (nowTime > time) {
                            dg.confirm('抱歉，由于当前保单的缴费时间晚于保单的生效时间，无法进行支付，请重新投保！', function () {

                            }, function () {

                            }, ['我知道了']);
                            return;
                        }
                        var attr = '<p>以下为待支付信息，请确认是否继续投保:</p>';
                        if (sy) {
                            attr += '<p>商业险:<span class="color-word">' + sy.toFixed(2) + '</span></p>'
                        }
                        if (jq) {
                            attr += '<p>交强险(含车船税):<span class="color-word">' + jq.toFixed(2) + '</span></p>'
                        }
                        attr += '<p>应付金额:<span class="color-word">' + shouldPay.toFixed(2) + '</span></p>';
                        dg.confirm(attr, function () {
                            window.location.href = url;
                        }, function () {

                        }, ['确认']);
                    } else {
                        dg.fail('获取支付信息失败');
                    }
                }
            });
        }
    });

});