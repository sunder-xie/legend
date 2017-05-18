/**
 * zmx  2016-06-08
 * 账户详情 页面
 */
$(function () {
    var $doc = $(document);
    seajs.use([
        'art',
        "ajax",
        'table',
        'select',
        'dialog',
        'downlist'
    ], function (at, ax, tb, select, dg, dl) {
        var _upgradeDialog = null;
        var dialogId = null;
        dg.titleInit();

        var accountId = $("#accountId").val();
        var customerId = $("#customerId").val();

        var name = util.getPara('flag');
        if (name === '0' || name == null || name == "") {
            $('.tab-item').eq(0).addClass('current-item').siblings().removeClass('current-item');
            selectTab();
        } else if (name === '1') {
            $('.tab-item').eq(1).addClass('current-item').siblings().removeClass('current-item');
            selectTab();
        } else if (name === '2') {
            $('.tab-item').eq(2).addClass('current-item').siblings().removeClass('current-item');
            selectTab();
        }

        //模板填充
        function selectTab() {
            var index = $('.tab .current-item').index(),
                url = BASE_PATH + '/account/coupon/get?accountId=' + accountId,
                tpl = 'DiscountTpl';
            if (index == 0) {
                url = BASE_PATH + '/account/coupon/get?accountId=' + accountId,
                    tpl = 'DiscountTpl';
            } else if (index == 1) {
                url = BASE_PATH + '/account/combo/combo/list?accountId=' + accountId,
                    tpl = 'MeterTpl';
            } else if (index == 2) {
                url = BASE_PATH + '/account/cardInfoQuery?accountId=' + accountId,
                    tpl = 'vipTpl';
            }
            //券信息填充
            $.ajax({
                url: url,
                data: {},
                success: function (result) {
                    if (result.success) {
                        var html = at(tpl, {json: result});

                        $('#tabCon').html(html);
                    } else {
                        dg.fail('该帐户未绑定会员卡')
                    }
                }
            });
        }

        //tab切换
        selectTab();
        $doc.on('click', '.tab-item', function () {
            $(this).addClass('current-item').siblings('.tab-item').removeClass('current-item');
            selectTab();
        });

        //表格填充
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/account/accountUsedList?accountId=' + accountId,
            //表格数据目标填充id，必需
            fillid: 'recordTable',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'tableTpl',
            //扩展参数,可选
            data: {},
            //关联查询表单id，可选
            formid: null,
            //渲染表格数据完后的回调方法,可选
            callback: null
        });

        // 是否更改车主
        var change = false,
            lastLicense = '',
            selectedData;

        function checkNewCarInfo(data) {
            var $addLicenseTip = $('.js-add-license-tip'),
                // 是否已有车主
                flag;
            lastLicense = $('.js-search').val();
            selectedData = data;

            // if != 0 or != null
            if (data.customerId && ((data.customerName !== '' && data.customerName !== 'null') || (data.mobile !== '' && data.mobile !== 'null'))) {
                change = true;

                dg.confirm('该车辆已有车主（' + data.customerName + '，' +
                data.mobile + '），是否更换车主？', function () {

                    change = true;
                    bundleCar();
                }, function () {

                    change = false;
                    bundleCar();
                }, ['是', '否']);
                $addLicenseTip.removeClass('visible');
                flag = true;
            } else {
                change = true;
                $addLicenseTip.html('<p>提示：该车辆暂无车主，添加车辆后，该车辆车主信息将更新为该车主</p>');
                $addLicenseTip.addClass('visible');
                flag = false;
            }
            // 可点击提交
            $('.js-bundle').prop('disabled', false);
            return flag;
        }

        //新增车辆弹窗
        $doc.on('click', '.js-add-license', function () {
            change = false;
            dialogId = dg.dialog({
                shadeClose: false,
                area: ['500px', 'auto'],
                content: document.getElementById('addlicenseTpl').innerHTML
            });

            dl.init({
                url: BASE_PATH + '/shop/customer/search/mobile',
                searchKey: 'com_license',
                showKey: 'license',
                hiddenKey: 'id',
                dom: '.js-search',
                hasTitle: false,
                hasInput: false,
                notClearInput: true,
                autoFill: true,
                callbackFn: function ($this, data) {
                    checkNewCarInfo(data);
                },
                clearCallbackFn: function (data) {
                    checkNewCarInfo(data);
                }
            });
        });


        //删除车辆
        $doc.on('click', '.js-deletecar', function () {
            var car_id = $(this).data("carid");
            var accountId = $("#accountId").val();
            if (car_id == '') {
                dg.fail("车牌号不能为空");
                return;
            }
            var $this = $(this);
            seajs.use(['dialog'], function (dialog) {
                dialog.confirm('确认解绑车辆？', function () {
                    //确定
                    removeCar(car_id, accountId, $this, dialog);
                });
            });
        });

        $doc.on('blur', '.js-search', function () {
            if (lastLicense == '' || lastLicense != this.value) {
                $('.js-bundle').prop('disabled', true);
            } else {
                $('.js-bundle').prop('disabled', false);
            }

        });

        function bundleCar() {
            var license = $("input[name='license']").val(),
                accountId = $("#accountId").val();

            $.ajax({
                url: BASE_PATH + "/account/bundleCar",
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify({
                    license: license,
                    accountId: accountId,
                    change: change
                }),
                success: function (result) {
                    if (result.success) {
                        // 车辆绑定成功，返回
                        dg.success(result.data, function () {
                            location.reload(true);
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        //绑定车辆
        $doc
            .on('click', '.js-bundle', function () {
                var license = $("input[name='license']").val();

                if (license == '') {
                    dg.warn("车牌号不能为空");
                    return;
                }

                if (!checkNewCarInfo(selectedData)) {
                    bundleCar();
                }
            })
            .on('click', '.js-unbundle', function () {
                dg.close(dialogId);
            });

        // 升级
        $doc.on('click', '.upgrade-dialog .js-confirm', function () {
            var warn;
            var $box = $(this).parents('.upgrade-dialog');
            var data = {
                cardId: $(this).data("cardId"),
                cardTypeId: $box.find('input[name="cardTypeId"]').val(),
                expireTimeType: $box.find('input[name="expireTimeType"]:checked').val()
            };

            if(!data.cardTypeId) {
                warn = '请选择正确的的会员卡类型';
            }

            if (warn) {
                dg.warn(warn);
                return;
            }

            $.post(BASE_PATH + '/account/member/upgrade', data, function (json) {
                if (json.success) {
                    dg.close();
                    dg.success('会员卡升级成功', function () {
                        location.reload();
                    });
                } else {
                    dg.fail(json.errorMsg || '升级失败');
                }
            });
        })
            .on('click', '.upgrade-dialog .js-cancel', function () {
                dg.close(upgradeDialog);
            });

        var upgradeDialog;

        function showUpgradeDialog(id) {

            $.get(BASE_PATH + '/account/member/upgrade/get-info', {
                cardId: id
            }, function (json) {
                var t;
                if (json.success && json.data) {
                    t = json.data.cardInfoList;
                    var html = at('upgradeDialogTpl', {
                        data: json.data
                    });

                    if (t && t.length) {
                        _upgradeDialog = $(html).addClass('hide').appendTo(document.body);

                        upgradeDialog = dg.open({
                            content: _upgradeDialog
                        });

                        if (json.data.expireTime < new Date()) {
                            $('.upgrade-dialog input[name="expireTimeType"]').each(function () {
                                var $this = $(this);
                                $this.prop('checked', this.value == 1);
                                $this.prop('disabled', this.value != 1);
                            });
                        }

                        select.init({
                            dom: '.js-card-type',
                            data: t.map(function (e) {
                                return {
                                    id: e.cardInfoId,
                                    name: e.cardInfoName
                                }
                            }),
                            canInput:true,
                            callback: function (a, b, c, index) {
                                $('.upgrade-dialog .upgrade-expire-time').text(t[index].expireTimeStr);
                            }
                        });
                    } else {
                        dg.warn('暂无其他可用会员卡，请增加新的会员卡类型');
                    }
                } else {
                    dg.fail(json.errorMsg || '获取会员卡信息失败');
                }
            });
        }

        $(document).on('click',function(){
            var cardTypeId = $('input[name="cardTypeId"]').val();
            if(cardTypeId == ''){
                $('.js-card-type').val('');
                $('.yqx-select-options').remove();
            }
        });

        $('.js-upgrade').click(function () {
            var cardId = $(this).data("cardId");
            showUpgradeDialog(cardId);
        });
    });// seajs end

    function removeCar(id, accountId, ele, dg, fn) {
        var $pares = ele && ele.parents('.car-item');

        $.ajax({
            url: BASE_PATH + "/account/unBundleCarById",
            type: 'post',
            data: {
                carId: id,
                accountId: accountId
            },
            success: function (result) {
                if (result.success) {
                    if (!fn) {
                        dg.success(result.data);
                    } else {
                        fn(id);
                    }

                    if (ele) {
                        if ($pares.find('.car-license').length == 1) {
                            $pares.remove();
                        } else {
                            ele.parents('.car-license').remove();
                        }
                    }

                } else {
                    dg.fail(result.errorMsg);
                }
            }
        });
    }

    //券信息展开
    $doc.on('click', '.js-show-btn', function () {
        var btnText = $(this).find('span'),
            tag = $(this).find('i'),
            showDiv = $(this).parents('.voucher-title').next('.voucher-content'),
            $showBtn = $('.js-show-btn');

        if (showDiv.is(":hidden")) {

            $('.voucher-content').hide();
            $showBtn.find('span').text('展开');
            $showBtn.find('i').removeClass('icon-angle-up').addClass('icon-angle-down');
            showDiv.show();
            btnText.text('收起');
            tag.removeClass('icon-angle-down').addClass('icon-angle-up');
        } else {

            btnText.text('展开');
            tag.removeClass('icon-angle-up').addClass('icon-angle-down');
            showDiv.hide();
        }
    });

});